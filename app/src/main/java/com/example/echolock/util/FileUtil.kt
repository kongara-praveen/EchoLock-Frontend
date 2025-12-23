package com.example.echolock.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream

fun uriToFile(context: Context, uri: Uri): File {

    val resolver = context.contentResolver

    val inputStream = resolver.openInputStream(uri)
        ?: throw IllegalStateException("Cannot open input stream")

    // 🔹 Get file name safely
    var fileName = "file_${System.currentTimeMillis()}"

    resolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (cursor.moveToFirst() && nameIndex >= 0) {
            fileName = cursor.getString(nameIndex)
        }
    }

    // 🔹 Get extension safely
    val mimeType = resolver.getType(uri)
    val extension = MimeTypeMap.getSingleton()
        .getExtensionFromMimeType(mimeType)
        ?: fileName.substringAfterLast('.', "")

    // 🔹 Ensure extension exists
    val finalName =
        if (fileName.contains(".")) fileName
        else "$fileName.$extension"

    val file = File(context.cacheDir, finalName)

    FileOutputStream(file).use { output ->
        inputStream.copyTo(output)
    }

    inputStream.close()
    return file
}
