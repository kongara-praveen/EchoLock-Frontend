package com.example.echolock.util

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream

fun uriToFile(context: Context, uri: Uri): File {

    val resolver = context.contentResolver
    val inputStream = resolver.openInputStream(uri)
        ?: throw IllegalStateException("Cannot open input stream")

    val mimeType = resolver.getType(uri)
    val extension = MimeTypeMap.getSingleton()
        .getExtensionFromMimeType(mimeType) ?: "jpg"

    val fileName =
        uri.lastPathSegment?.substringAfterLast("/") ?: "upload.$extension"

    val file = File(context.cacheDir, fileName)

    FileOutputStream(file).use { output ->
        inputStream.copyTo(output)
    }

    inputStream.close()
    return file
}
