package com.example.echolock.util

import java.io.File
import java.security.MessageDigest

object AudioSteganography {

    private const val END_MARKER = "###ECHOLOCK###"
    private const val PASSWORD_SEPARATOR = "::PWD::"

    /* ================= CAPACITY ================= */

    fun getMaxMessageLength(wavFile: File): Int {
        val totalBytes = wavFile.length().toInt()
        if (totalBytes <= 44) return 0

        val dataBytes = totalBytes - 44
        val maxBytes = dataBytes / 8
        val endMarkerBytes = END_MARKER.toByteArray(Charsets.UTF_8).size

        return (maxBytes - endMarkerBytes).coerceAtLeast(0)
    }

    /* ================= ENCODE ================= */

    fun encode(
        wavFile: File,
        message: String,
        password: String,
        outputFile: File
    ) {
        val audioBytes = wavFile.readBytes()

        // WAV header (DO NOT TOUCH)
        val header = audioBytes.copyOfRange(0, 44)
        val data = audioBytes.copyOfRange(44, audioBytes.size)

        // Hash password
        val passwordHash = hashPassword(password)
        
        // Format: PASSWORD_HASH::PWD::MESSAGE###ECHOLOCK###
        val fullMessage =
            ("$passwordHash$PASSWORD_SEPARATOR$message$END_MARKER").toByteArray(Charsets.UTF_8)

        val requiredBits = fullMessage.size * 8

        if (requiredBits > data.size) {
            throw Exception("Message too large for this audio file")
        }

        var bitIndex = 0
        for (byte in fullMessage) {
            for (i in 7 downTo 0) {
                val bit = (byte.toInt() shr i) and 1
                data[bitIndex] =
                    ((data[bitIndex].toInt() and 0xFE) or bit).toByte()
                bitIndex++
            }
        }

        outputFile.writeBytes(header + data)
    }

    /* ================= DECODE ================= */
    fun decode(wavFile: File, password: String? = null): DecodeResult {

        val audioBytes = wavFile.readBytes()
        if (audioBytes.size <= 44) return DecodeResult("Decryption failed", false)

        val data = audioBytes.copyOfRange(44, audioBytes.size)

        val resultBytes = ArrayList<Byte>()
        var currentByte = 0
        var bitCount = 0

        val END = "###ECHOLOCK###"
        val MAX_BYTES_TO_READ = 5000   // 🔥 FAST EXIT LIMIT

        for (byte in data) {

            currentByte = (currentByte shl 1) or (byte.toInt() and 1)
            bitCount++

            if (bitCount == 8) {
                resultBytes.add(currentByte.toByte())

                val text =
                    try {
                        String(resultBytes.toByteArray(), Charsets.UTF_8)
                    } catch (e: Exception) {
                        return DecodeResult("No hidden message found", false)
                    }

                if (text.contains(END)) {
                    val content = text.substringBefore(END)
                    
                    // Check if password protected
                    if (content.contains(PASSWORD_SEPARATOR)) {
                        val parts = content.split(PASSWORD_SEPARATOR, limit = 2)
                        if (parts.size == 2) {
                            val storedHash = parts[0]
                            val message = parts[1]
                            
                            // Verify password if provided
                            if (password != null) {
                                val inputHash = hashPassword(password)
                                if (inputHash == storedHash) {
                                    return DecodeResult(message, true)
                                } else {
                                    return DecodeResult("Wrong password", false)
                                }
                            } else {
                                return DecodeResult("Password required", false)
                            }
                        }
                    } else {
                        // No password protection (old format)
                        return DecodeResult(content, true)
                    }
                }

                // 🚀 EARLY EXIT (NORMAL AUDIO)
                if (resultBytes.size > MAX_BYTES_TO_READ) {
                    return DecodeResult("No hidden message found", false)
                }

                currentByte = 0
                bitCount = 0
            }
        }

        return DecodeResult("No hidden message found", false)
    }
    
    /* ================= PASSWORD HASHING ================= */
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
    
    /* ================= RESULT MODEL ================= */
    data class DecodeResult(
        val message: String,
        val success: Boolean
    )

}
