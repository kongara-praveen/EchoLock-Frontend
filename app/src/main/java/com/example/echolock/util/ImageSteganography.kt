package com.example.echolock.util

import android.graphics.Bitmap
import java.nio.charset.Charset

object ImageSteganography {

    private const val MAGIC_HEADER = "ECHOLOCK::"
    private const val SEPARATOR = "::"
    private val CHARSET = Charsets.UTF_8

    /* ================== ENCODE ================== */
    fun encode(
        original: Bitmap,
        serverToken: String,
        message: String
    ): Bitmap {

        val mutableBitmap =
            original.copy(Bitmap.Config.ARGB_8888, true)

        // 🔐 Payload structure:
        // ECHOLOCK::<SERVER_TOKEN>::<MESSAGE>\0
        val payload =
            "$MAGIC_HEADER$serverToken$SEPARATOR$message\u0000"

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

    /* ================== DECODE ================== */
    fun decode(bitmap: Bitmap): DecodedPayload? {

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

        val parts =
            content.split(SEPARATOR, limit = 2)

        // ❌ Corrupted / tampered payload
        if (parts.size != 2) return null

        return DecodedPayload(
            token = parts[0],
            message = parts[1]
        )
    }
}

/* ================== MODEL ================== */
data class DecodedPayload(
    val token: String,
    val message: String
)
