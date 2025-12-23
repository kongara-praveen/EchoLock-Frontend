package com.example.echolock.util

import java.io.File

object AudioSteganography {

    private const val END_MARKER = "###ECHOLOCK###"

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
        outputFile: File
    ) {
        val audioBytes = wavFile.readBytes()

        // WAV header (DO NOT TOUCH)
        val header = audioBytes.copyOfRange(0, 44)
        val data = audioBytes.copyOfRange(44, audioBytes.size)

        val fullMessage =
            (message + END_MARKER).toByteArray(Charsets.UTF_8)

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

    fun decode(wavFile: File): String {
        val audioBytes = wavFile.readBytes()

        if (audioBytes.size <= 44) {
            return "Invalid audio file"
        }

        val data = audioBytes.copyOfRange(44, audioBytes.size)

        val resultBytes = ArrayList<Byte>()
        var currentByte = 0
        var bitCount = 0

        for (byte in data) {
            val lsb = byte.toInt() and 1
            currentByte = (currentByte shl 1) or lsb
            bitCount++

            if (bitCount == 8) {
                resultBytes.add(currentByte.toByte())

                val decodedText =
                    String(resultBytes.toByteArray(), Charsets.UTF_8)

                if (decodedText.contains(END_MARKER)) {
                    return decodedText.substringBefore(END_MARKER)
                }

                currentByte = 0
                bitCount = 0
            }
        }

        return "No hidden message found"
    }
}
