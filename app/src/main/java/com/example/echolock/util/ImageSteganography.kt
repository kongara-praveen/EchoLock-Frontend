package com.example.echolock.util

import android.graphics.Bitmap
import java.nio.charset.Charset
import java.security.MessageDigest

object ImageSteganography {

    private const val MAGIC_HEADER = "ECHOLOCK::"
    private const val SEPARATOR = "::"
    private const val PASSWORD_SEPARATOR = "::PWD::"
    private val CHARSET = Charsets.UTF_8

    /* ================== ENCODE ================== */
    fun encode(
        original: Bitmap,
        identifier: String,
        message: String,
        password: String,
        originalFileName: String? = null,
        integritySignature: String? = null
    ): Bitmap {

        val mutableBitmap =
            original.copy(Bitmap.Config.ARGB_8888, true)

        // Hash password
        val passwordHash = hashPassword(password)
        
        // 🔐 Payload structure:
        // ECHOLOCK::<IDENTIFIER>::<PASSWORD_HASH>::PWD::<MESSAGE>::FILE::<ORIGINAL_FILE_NAME>::SIG::<INTEGRITY_SIGNATURE>\0
        // If originalFileName is null, FILE:: part is omitted
        // If integritySignature is null, SIG:: part is omitted
        val filePart = if (!originalFileName.isNullOrBlank()) {
            "::FILE::$originalFileName"
        } else {
            ""
        }
        val signaturePart = if (!integritySignature.isNullOrBlank()) {
            "::SIG::$integritySignature"
        } else {
            ""
        }
        val payload =
            "$MAGIC_HEADER$identifier$SEPARATOR$passwordHash$PASSWORD_SEPARATOR$message$filePart$signaturePart\u0000"

        val msgBytes = payload.toByteArray(CHARSET)

        var byteIndex = 0
        var bitIndex = 0

        loop@ for (y in 0 until mutableBitmap.height) {
            for (x in 0 until mutableBitmap.width) {

                if (byteIndex >= msgBytes.size) break@loop

                val pixel = mutableBitmap.getPixel(x, y)
                val blue = pixel and 0xFF

                val bit =
                    (msgBytes[byteIndex].toInt() shr (7 - bitIndex)) and 1

                val newBlue = (blue and 0xFE) or bit
                val newPixel =
                    (pixel and 0xFFFFFF00.toInt()) or newBlue

                mutableBitmap.setPixel(x, y, newPixel)

                bitIndex++
                if (bitIndex == 8) {
                    bitIndex = 0
                    byteIndex++
                }
            }
        }

        return mutableBitmap
    }

    /* ================== ENCODE SIGNATURE ONLY ================== */
    /**
     * Embeds integrity signature in an already-encrypted image.
     * This is used after the message has been encoded and the signature has been computed.
     * The signature is embedded after the message payload.
     * @return Pair of (final bitmap, signature start pixel index) for verification
     */
    fun encodeWithSignature(
        encryptedBitmap: Bitmap,
        integritySignature: String
    ): Pair<Bitmap, Int> {
        val mutableBitmap = encryptedBitmap.copy(Bitmap.Config.ARGB_8888, true)
        
        // Find where the message payload ends (look for null terminator)
        // We'll embed the signature after the existing payload
        val signaturePayload = "::SIG::$integritySignature\u0000"
        val sigBytes = signaturePayload.toByteArray(CHARSET)
        
        // First, find the end of existing payload by scanning for null terminator
        var foundPayloadEnd = false
        var byteIndex = 0
        var bitIndex = 0
        var currentByte = 0
        var payloadEndPixelIndex = -1
        
        // Scan to find where payload ends
        scanLoop@ for (y in 0 until mutableBitmap.height) {
            for (x in 0 until mutableBitmap.width) {
                val blue = mutableBitmap.getPixel(x, y) and 0xFF
                val bit = blue and 1
                
                currentByte = (currentByte shl 1) or bit
                bitIndex++
                
                if (bitIndex == 8) {
                    if (currentByte == 0) {
                        // Found null terminator - this is where payload ends
                        payloadEndPixelIndex = y * mutableBitmap.width + x + 1
                        foundPayloadEnd = true
                        break@scanLoop
                    }
                    bitIndex = 0
                    currentByte = 0
                }
            }
        }
        
        if (!foundPayloadEnd || payloadEndPixelIndex == -1) {
            // Fallback: start embedding after a safe offset
            payloadEndPixelIndex = (mutableBitmap.width * mutableBitmap.height) / 4
        }
        
        val signatureStartIndex = payloadEndPixelIndex
        
        // Embed signature starting after the payload
        var sigByteIndex = 0
        var sigBitIndex = 0
        var pixelIndex = 0
        
        embedLoop@ for (y in 0 until mutableBitmap.height) {
            for (x in 0 until mutableBitmap.width) {
                if (pixelIndex >= signatureStartIndex) {
                    // Start embedding here
                    if (sigByteIndex < sigBytes.size) {
                        val pixel = mutableBitmap.getPixel(x, y)
                        val blue = pixel and 0xFF
                        
                        val bit = (sigBytes[sigByteIndex].toInt() shr (7 - sigBitIndex)) and 1
                        val newBlue = (blue and 0xFE) or bit
                        val newPixel = (pixel and 0xFFFFFF00.toInt()) or newBlue
                        
                        mutableBitmap.setPixel(x, y, newPixel)
                        
                        sigBitIndex++
                        if (sigBitIndex == 8) {
                            sigBitIndex = 0
                            sigByteIndex++
                            if (sigByteIndex >= sigBytes.size) {
                                break@embedLoop
                            }
                        }
                    }
                }
                pixelIndex++
            }
        }
        
        return Pair(mutableBitmap, signatureStartIndex)
    }

    /* ================== DECODE ================== */
    fun decode(bitmap: Bitmap, password: String? = null): DecodedPayload? {

        val bytes = ArrayList<Byte>()
        var currentByte = 0
        var bitIndex = 0

        loop@ for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width) {

                val blue = bitmap.getPixel(x, y) and 0xFF
                val bit = blue and 1

                currentByte = (currentByte shl 1) or bit
                bitIndex++

                if (bitIndex == 8) {
                    bytes.add(currentByte.toByte())

                    if (currentByte == 0) break@loop

                    bitIndex = 0
                    currentByte = 0
                }
            }
        }

        val extracted =
            String(bytes.toByteArray(), CHARSET).trimEnd('\u0000')

        // ❌ Not an EchoLock image
        if (!extracted.startsWith(MAGIC_HEADER)) return null

        val content =
            extracted.removePrefix(MAGIC_HEADER)

        // Check if password protected
        if (content.contains(PASSWORD_SEPARATOR)) {
            // Format: TOKEN::PASSWORD_HASH::PWD::MESSAGE::FILE::ORIGINAL_FILE_NAME::SIG::INTEGRITY_SIGNATURE (optional parts)
            val pwdParts = content.split(PASSWORD_SEPARATOR, limit = 2)
            if (pwdParts.size == 2) {
                val beforePwd = pwdParts[0]  // TOKEN::PASSWORD_HASH
                val afterPwd = pwdParts[1]   // MESSAGE::FILE::ORIGINAL_FILE_NAME::SIG::INTEGRITY_SIGNATURE (optional parts)
                
                // Extract signature if present (check this first as it's at the end)
                val signatureSeparator = "::SIG::"
                val integritySignature = if (afterPwd.contains(signatureSeparator)) {
                    afterPwd.substringAfter(signatureSeparator)
                } else {
                    null
                }
                val contentWithoutSig = if (afterPwd.contains(signatureSeparator)) {
                    afterPwd.substringBefore(signatureSeparator)
                } else {
                    afterPwd
                }
                
                // Extract file name if present
                val fileSeparator = "::FILE::"
                val originalFileName = if (contentWithoutSig.contains(fileSeparator)) {
                    contentWithoutSig.substringAfter(fileSeparator)
                } else {
                    null
                }
                val message = if (contentWithoutSig.contains(fileSeparator)) {
                    contentWithoutSig.substringBefore(fileSeparator)
                } else {
                    contentWithoutSig
                }
                
                val tokenParts = beforePwd.split(SEPARATOR, limit = 2)
                if (tokenParts.size == 2) {
                    val token = tokenParts[0]
                    val storedHash = tokenParts[1]
                    
                    // Verify password if provided
                    if (password != null) {
                        val inputHash = hashPassword(password)
                        if (inputHash == storedHash) {
                            return DecodedPayload(token, message, originalFileName, integritySignature)
                        } else {
                            return DecodedPayload(token, "WRONG_PASSWORD", originalFileName, integritySignature)
                        }
                    } else {
                        return DecodedPayload(token, "PASSWORD_REQUIRED", originalFileName, integritySignature)
                    }
                }
            }
        } else {
            // Old format without password: TOKEN::MESSAGE::FILE::ORIGINAL_FILE_NAME::SIG::INTEGRITY_SIGNATURE (optional parts)
            val signatureSeparator = "::SIG::"
            val integritySignature = if (content.contains(signatureSeparator)) {
                content.substringAfter(signatureSeparator)
            } else {
                null
            }
            val contentWithoutSig = if (content.contains(signatureSeparator)) {
                content.substringBefore(signatureSeparator)
            } else {
                content
            }
            
            val fileSeparator = "::FILE::"
            val originalFileName = if (contentWithoutSig.contains(fileSeparator)) {
                contentWithoutSig.substringAfter(fileSeparator)
            } else {
                null
            }
            val contentWithoutFile = if (contentWithoutSig.contains(fileSeparator)) {
                contentWithoutSig.substringBefore(fileSeparator)
            } else {
                contentWithoutSig
            }
            val parts = contentWithoutFile.split(SEPARATOR, limit = 2)
            if (parts.size == 2) {
                return DecodedPayload(parts[0], parts[1], originalFileName, integritySignature)
            }
        }

        return null
    }
    
    /* ================== PASSWORD HASHING ================== */
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}

/* ================== MODEL ================== */
data class DecodedPayload(
    val token: String,
    val message: String,
    val originalFileName: String? = null,
    val integritySignature: String? = null
)





