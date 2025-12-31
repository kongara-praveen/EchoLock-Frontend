package com.example.echolock.util

import android.graphics.Bitmap
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

/**
 * Utility for generating and verifying integrity signatures for encrypted images.
 * Uses SHA-256 hash combined with a secret EchoLock token to create tamper-proof signatures.
 */
object IntegritySignatureUtil {

    // Secret EchoLock-only token (known only to EchoLock)
    // In production, this should be stored securely (e.g., in Android Keystore)
    private const val SECRET_ECHOLOCK_TOKEN = "EchoLock_Secret_Token_2024_Integrity_Check_v1"

    /**
     * Generates SHA-256 hash of an image file
     * @param imageFile The image file to hash
     * @return SHA-256 hash as hexadecimal string, or null if error
     */
    fun generateImageHash(imageFile: File): String? {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            FileInputStream(imageFile).use { input ->
                val buffer = ByteArray(8192)
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    digest.update(buffer, 0, bytesRead)
                }
            }
            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            Log.e("IntegritySignature", "Error generating hash", e)
            null
        }
    }

    /**
     * Generates SHA-256 hash of a bitmap
     * @param bitmap The bitmap to hash
     * @return SHA-256 hash as hexadecimal string, or null if error
     */
    fun generateBitmapHash(bitmap: Bitmap): String? {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val pixels = IntArray(bitmap.width * bitmap.height)
            bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
            
            // Convert pixels to bytes
            val bytes = ByteArray(pixels.size * 4)
            for (i in pixels.indices) {
                val pixel = pixels[i]
                bytes[i * 4] = ((pixel shr 24) and 0xFF).toByte() // A
                bytes[i * 4 + 1] = ((pixel shr 16) and 0xFF).toByte() // R
                bytes[i * 4 + 2] = ((pixel shr 8) and 0xFF).toByte() // G
                bytes[i * 4 + 3] = (pixel and 0xFF).toByte() // B
            }
            
            digest.update(bytes)
            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            Log.e("IntegritySignature", "Error generating bitmap hash", e)
            null
        }
    }

    /**
     * Creates an integrity signature by combining image hash with secret token
     * @param imageHash SHA-256 hash of the encrypted image
     * @return Integrity signature as hexadecimal string, or null if error
     */
    fun createIntegritySignature(imageHash: String): String? {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            // Combine image hash with secret token
            val combined = "$imageHash$SECRET_ECHOLOCK_TOKEN"
            digest.update(combined.toByteArray(Charsets.UTF_8))
            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            Log.e("IntegritySignature", "Error creating signature", e)
            null
        }
    }

    /**
     * Verifies an integrity signature
     * @param imageHash SHA-256 hash of the image to verify
     * @param storedSignature The signature stored on the server
     * @return true if signature matches (image is safe), false otherwise
     */
    fun verifyIntegritySignature(imageHash: String, storedSignature: String): Boolean {
        val computedSignature = createIntegritySignature(imageHash) ?: return false
        val matches = computedSignature.equals(storedSignature, ignoreCase = true)
        
        Log.d("IntegritySignature", "Verification: matches=$matches")
        Log.d("IntegritySignature", "Computed: $computedSignature")
        Log.d("IntegritySignature", "Stored: $storedSignature")
        
        return matches
    }

    /**
     * Generates hash and signature for a bitmap (used during encryption)
     * @param bitmap The encrypted bitmap
     * @return Pair of (imageHash, integritySignature), or null if error
     */
    fun generateHashAndSignature(bitmap: Bitmap): Pair<String, String>? {
        val imageHash = generateBitmapHash(bitmap) ?: return null
        val signature = createIntegritySignature(imageHash) ?: return null
        return Pair(imageHash, signature)
    }

    /**
     * Generates hash and signature for an image file (used during tamper check)
     * @param imageFile The image file to process
     * @return Pair of (imageHash, integritySignature), or null if error
     */
    fun generateHashAndSignature(imageFile: File): Pair<String, String>? {
        val imageHash = generateImageHash(imageFile) ?: return null
        val signature = createIntegritySignature(imageHash) ?: return null
        return Pair(imageHash, signature)
    }

    /**
     * Generates hash of image without the signature bits.
     * This reconstructs the image as it was when the signature was computed (before signature embedding).
     * The signature bits are zeroed out to approximate the original state.
     * @param bitmap The bitmap to hash
     * @param signaturePayload The signature payload bytes that were embedded
     * @param signatureStartPixelIndex The pixel index where signature embedding started
     * @return SHA-256 hash as hexadecimal string, or null if error
     */
    fun generateHashWithoutSignature(
        bitmap: android.graphics.Bitmap,
        signaturePayload: ByteArray,
        signatureStartPixelIndex: Int
    ): String? {
        return try {
            // Create a copy of the bitmap
            val bitmapCopy = bitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, true)
            
            // Zero out the signature bits to restore image to pre-signature state
            // This approximates the original image by setting signature LSBs to 0
            var pixelIndex = 0
            var sigByteIndex = 0
            var sigBitIndex = 0
            val totalSigBits = signaturePayload.size * 8 // Total bits in signature
            
            for (y in 0 until bitmapCopy.height) {
                for (x in 0 until bitmapCopy.width) {
                    if (pixelIndex >= signatureStartPixelIndex && sigBitIndex < totalSigBits) {
                        // This pixel contains signature data - zero out the LSB
                        val pixel = bitmapCopy.getPixel(x, y)
                        val blue = pixel and 0xFF
                        // Zero out the LSB (set to even value, approximating original state)
                        val newBlue = blue and 0xFE
                        val newPixel = (pixel and 0xFFFFFF00.toInt()) or newBlue
                        bitmapCopy.setPixel(x, y, newPixel)
                        
                        sigBitIndex++
                        if (sigBitIndex % 8 == 0) {
                            sigByteIndex++
                        }
                    }
                    pixelIndex++
                }
            }
            
            // Now compute hash of the restored bitmap (without signature bits)
            generateBitmapHash(bitmapCopy)
        } catch (e: Exception) {
            Log.e("IntegritySignature", "Error generating hash without signature", e)
            null
        }
    }
}

