package com.example.echolock.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

object FileUtils {

    fun copyToCache(context: Context, uri: Uri): File {

        val resolver = context.contentResolver

        // 🔹 Get real file name from system
        val fileName = getFileName(resolver, uri)

        val outputFile = File(
            context.cacheDir,
            fileName
        )

        resolver.openInputStream(uri)?.use { input ->
            FileOutputStream(outputFile).use { output ->
                input.copyTo(output)
            }
        } ?: throw Exception("Cannot open input stream")

        return outputFile
    }

    private fun getFileName(
        resolver: android.content.ContentResolver,
        uri: Uri
    ): String {

        resolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getString(0)
            }
        }

        // fallback (never usually hit)
        return "audio_${System.currentTimeMillis()}"
    }
}
