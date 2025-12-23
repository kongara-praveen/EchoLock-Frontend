package com.example.echolock.util

import android.graphics.Bitmap

object ImageSteganography {

    private const val MAGIC_HEADER = "ECHOLOCK::"

    /* ================== ENCODE ================== */
    fun encode(original: Bitmap, message: String): Bitmap {

        val mutableBitmap =
            original.copy(Bitmap.Config.ARGB_8888, true)

        // 🔐 Add header + end marker
        val fullMessage = MAGIC_HEADER + message + "\u0000"
        val msgBytes = fullMessage.toByteArray()

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
    fun decode(bitmap: Bitmap): String? {

        val bytes = ArrayList<Byte>()
        var currentByte = 0
        var bitIndex = 0

        loop@ for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width) {

                val pixel = bitmap.getPixel(x, y)
                val blue = pixel and 0xFF
                val bit = blue and 1

                currentByte = (currentByte shl 1) or bit
                bitIndex++

                if (bitIndex == 8) {
                    bytes.add(currentByte.toByte())

                    if (currentByte == 0) break@loop  // 🔚 End marker

                    bitIndex = 0
                    currentByte = 0
                }
            }
        }

        val extracted = String(bytes.toByteArray()).trimEnd('\u0000')

        // ✅ VALIDATION
        return if (extracted.startsWith(MAGIC_HEADER)) {
            extracted.removePrefix(MAGIC_HEADER)
        } else {
            null   // ❌ NOT an EchoLock encrypted image
        }
    }
}
