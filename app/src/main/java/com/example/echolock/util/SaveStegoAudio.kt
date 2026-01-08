package com.example.echolock.util

import android.content.ContentValues
import android.content.Context
import android.os.Build
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
                // Use correct MIME type for WAV files - Android recognizes this better
                put(MediaStore.MediaColumns.MIME_TYPE, "audio/x-wav")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS + "/EchoLock"
                )
                // Set IS_PENDING to 1 during write (Android 10+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
            }

            val uri = resolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: return false

            // Write the file data
            resolver.openOutputStream(uri)?.use { output ->
                FileInputStream(stegoFile).use { input ->
                    input.copyTo(output)
                    output.flush()
                }
            } ?: return false

            // Mark as not pending so MediaStore indexes it properly (Android 10+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val updateValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.IS_PENDING, 0)
                }
                resolver.update(uri, updateValues, null, null)
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
