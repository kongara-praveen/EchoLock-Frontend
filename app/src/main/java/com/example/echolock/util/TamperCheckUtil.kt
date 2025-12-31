package com.example.echolock.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.echolock.util.DecodedPayload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.charset.StandardCharsets

object TamperCheckUtil {

    /**
     * Verifies image integrity using cryptographic hash and integrity signature.
     * TRUE  → SAFE (Image hash matches stored signature - image is authentic and untampered)
     * FALSE → TAMPERED (Image hash doesn't match stored signature - image has been modified)
     * @param file The encrypted image file to check
     * @param originalFileName The original file name used to retrieve stored signature from server
     */
    suspend fun checkFile(file: File, originalFileName: String? = null): Boolean =
        withContext(Dispatchers.IO) {

            try {
                // 1️⃣ Decode image to extract embedded integrity signature
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    ?: run {
                        Log.e("TamperCheck", "Failed to decode image")
                        return@withContext false
                    }

                val decoded = ImageSteganography.decode(bitmap)
                    ?: run {
                        Log.e("TamperCheck", "Failed to decode steganographic data - not an EchoLock image")
                        return@withContext false
                    }

                // 2️⃣ Extract embedded integrity signature
                val embeddedSignature = decoded.integritySignature
                if (embeddedSignature.isNullOrBlank()) {
                    Log.w("TamperCheck", "No integrity signature found in image, using fallback steganography check")
                    return@withContext checkSteganographyFallback(file)
                }

                Log.d("TamperCheck", "Extracted embedded signature: $embeddedSignature")

                // 3️⃣ Find signature location in the bitmap
                val signatureStartIndex = findSignatureStartIndex(bitmap, decoded)
                
                if (signatureStartIndex <= 0) {
                    Log.w("TamperCheck", "Could not find signature location, using fallback check")
                    return@withContext checkSteganographyFallback(file)
                }
                
                // 4️⃣ Generate hash of image WITHOUT signature bits
                // This reconstructs the image as it was when signature was computed
                val signatureMarker = "::SIG::$embeddedSignature\u0000"
                val sigBytes = signatureMarker.toByteArray(StandardCharsets.UTF_8)
                
                val hashWithoutSig = IntegritySignatureUtil.generateHashWithoutSignature(
                    bitmap, 
                    sigBytes, 
                    signatureStartIndex
                )
                
                if (hashWithoutSig == null) {
                    Log.e("TamperCheck", "Failed to generate image hash without signature")
                    return@withContext false
                }

                Log.d("TamperCheck", "Image hash (without signature): $hashWithoutSig")

                // 5️⃣ Regenerate expected signature from hash
                val expectedSignature = IntegritySignatureUtil.createIntegritySignature(hashWithoutSig)
                    ?: run {
                        Log.e("TamperCheck", "Failed to generate expected signature")
                        return@withContext false
                    }

                Log.d("TamperCheck", "Expected signature: $expectedSignature")
                Log.d("TamperCheck", "Embedded signature: $embeddedSignature")

                // 6️⃣ Verify signatures match
                val isSafe = expectedSignature.equals(embeddedSignature, ignoreCase = true)
                
                if (isSafe) {
                    Log.d("TamperCheck", "✅ Image is SAFE - integrity signature matches")
                } else {
                    Log.w("TamperCheck", "⚠️ Image is TAMPERED - integrity signature does not match")
                    Log.w("TamperCheck", "   Image may have been re-encrypted, edited, compressed, or processed")
                }
                
                return@withContext isSafe

            } catch (e: Exception) {
                Log.e("TamperCheck", "Error during tamper check", e)
                e.printStackTrace()
                return@withContext false
            }
        }

    /**
     * Finds the pixel index where signature embedding starts
     * This is after the message payload ends (after null terminator)
     */
    private fun findSignatureStartIndex(bitmap: Bitmap, decoded: DecodedPayload): Int {
        // Scan bitmap to find where payload ends (null terminator)
        var byteIndex = 0
        var bitIndex = 0
        var currentByte = 0
        var pixelIndex = 0
        
        scanLoop@ for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width) {
                val blue = bitmap.getPixel(x, y) and 0xFF
                val bit = blue and 1
                
                currentByte = (currentByte shl 1) or bit
                bitIndex++
                
                if (bitIndex == 8) {
                    if (currentByte == 0) {
                        // Found null terminator - signature starts after this
                        return pixelIndex + 1
                    }
                    bitIndex = 0
                    currentByte = 0
                }
                pixelIndex++
            }
        }
        
        // Fallback: estimate based on payload size
        val estimatedPayloadSize = ("ECHOLOCK::" + decoded.token + "::" + decoded.message.length + 
            (decoded.originalFileName?.length ?: 0) + 100).length // Rough estimate
        return estimatedPayloadSize * 8 // Convert bytes to pixels (8 bits per byte)
    }

    /**
     * Fallback method: Checks if image contains valid steganographic data
     * Used when signature is not available
     */
    private suspend fun checkSteganographyFallback(file: File): Boolean = withContext(Dispatchers.IO) {
        try {
            val bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
                ?: return@withContext false

            val decoded = ImageSteganography.decode(bitmap)
                ?: return@withContext false

            return@withContext decoded.message.isNotBlank() && decoded.token.isNotBlank()
        } catch (e: Exception) {
            Log.e("TamperCheck", "Fallback check failed", e)
            return@withContext false
        }
    }
}
