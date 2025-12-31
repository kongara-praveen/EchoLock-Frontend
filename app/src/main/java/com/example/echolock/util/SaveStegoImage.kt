package com.example.echolock.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

fun saveStegoImage(context: Context, bitmap: Bitmap, originalFileName: String? = null): String {

    // Use original file name if provided, otherwise generate a name
    val fileName = if (!originalFileName.isNullOrBlank()) {
        // Preserve original name but ensure .png extension (since we're saving as PNG)
        val nameWithoutExt = if (originalFileName.contains(".")) {
            originalFileName.substringBeforeLast(".")
        } else {
            originalFileName
        }
        // Ensure we have a valid name (not empty)
        if (nameWithoutExt.isNotBlank()) {
            "$nameWithoutExt.png"
        } else {
            "EchoLock_${System.currentTimeMillis()}.png"
        }
    } else {
        "EchoLock_${System.currentTimeMillis()}.png"
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + File.separator + "EchoLock"
            )
        }

        val uri = context.contentResolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            values
        ) ?: throw Exception("MediaStore insert failed")

        context.contentResolver.openOutputStream(uri)?.use { output ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
        } ?: throw Exception("OutputStream failed")

        return fileName
    }

    val dir = File(
        Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        ),
        "EchoLock"
    )

    if (!dir.exists()) dir.mkdirs()

    val file = File(dir, fileName)

    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    return fileName
}
