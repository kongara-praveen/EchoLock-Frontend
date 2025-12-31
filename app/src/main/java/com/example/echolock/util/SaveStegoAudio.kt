package com.example.echolock.util

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream

object SaveStegoAudio {

    fun saveStegoAudioToDownloads(
        context: Context,
        stegoFile: File,
        originalFileName: String? = null
    ): Boolean {

        val resolver = context.contentResolver
        // Use original file name if provided, otherwise use stego file name
        val fileName = if (!originalFileName.isNullOrBlank()) {
            // Preserve original name but ensure .wav extension (since we're saving as WAV)
            val nameWithoutExt = if (originalFileName.contains(".")) {
                originalFileName.substringBeforeLast(".")
            } else {
                originalFileName
            }
            // Ensure we have a valid name (not empty)
            if (nameWithoutExt.isNotBlank()) {
                "$nameWithoutExt.wav"
            } else {
                stegoFile.name
            }
        } else {
            stegoFile.name
        }

        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "audio/wav")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS + "/EchoLock"
                )
            }

            val uri = resolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: return false

            val outputStream: OutputStream =
                resolver.openOutputStream(uri) ?: return false

            FileInputStream(stegoFile).use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
