package com.example.echolock.util

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

data class StorageFile(
    val name: String,
    val size: Long,
    val dateModified: Long,
    val uri: Uri,
    val type: FileType,
    val path: String
)

enum class FileType {
    AUDIO, IMAGE, UNKNOWN
}

object StorageUtil {

    suspend fun getEchoLockFiles(context: Context): List<StorageFile> = withContext(Dispatchers.IO) {
        val files = mutableListOf<StorageFile>()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use MediaStore for Android 10+
                val projection = arrayOf(
                    MediaStore.Downloads._ID,
                    MediaStore.Downloads.DISPLAY_NAME,
                    MediaStore.Downloads.SIZE,
                    MediaStore.Downloads.DATE_MODIFIED,
                    MediaStore.Downloads.RELATIVE_PATH
                )

                val selection = "${MediaStore.Downloads.RELATIVE_PATH} LIKE ?"
                val selectionArgs = arrayOf("%EchoLock%")

                context.contentResolver.query(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    "${MediaStore.Downloads.DATE_MODIFIED} DESC"
                )?.use { cursor ->
                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID)
                    val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME)
                    val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.SIZE)
                    val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.DATE_MODIFIED)
                    val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.RELATIVE_PATH)

                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idColumn)
                        val name = cursor.getString(nameColumn)
                        val size = cursor.getLong(sizeColumn)
                        val dateModified = cursor.getLong(dateColumn) * 1000 // Convert to milliseconds
                        val path = cursor.getString(pathColumn)

                        val uri = ContentUris.withAppendedId(
                            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                            id
                        )

                        val fileType = when {
                            name.endsWith(".wav", ignoreCase = true) ||
                            name.endsWith(".mp3", ignoreCase = true) ||
                            name.endsWith(".m4a", ignoreCase = true) -> FileType.AUDIO
                            name.endsWith(".png", ignoreCase = true) ||
                            name.endsWith(".jpg", ignoreCase = true) ||
                            name.endsWith(".jpeg", ignoreCase = true) ||
                            name.endsWith(".webp", ignoreCase = true) -> FileType.IMAGE
                            else -> FileType.UNKNOWN
                        }

                        files.add(StorageFile(name, size, dateModified, uri, fileType, path))
                    }
                }
            } else {
                // Use File API for older Android versions
                val downloadsDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                )
                val echoLockDir = File(downloadsDir, "EchoLock")

                if (echoLockDir.exists() && echoLockDir.isDirectory) {
                    echoLockDir.listFiles()?.forEach { file ->
                        if (file.isFile) {
                            val fileType = when {
                                file.name.endsWith(".wav", ignoreCase = true) ||
                                file.name.endsWith(".mp3", ignoreCase = true) ||
                                file.name.endsWith(".m4a", ignoreCase = true) -> FileType.AUDIO
                                file.name.endsWith(".png", ignoreCase = true) ||
                                file.name.endsWith(".jpg", ignoreCase = true) ||
                                file.name.endsWith(".jpeg", ignoreCase = true) ||
                                file.name.endsWith(".webp", ignoreCase = true) -> FileType.IMAGE
                                else -> FileType.UNKNOWN
                            }

                            files.add(
                                StorageFile(
                                    name = file.name,
                                    size = file.length(),
                                    dateModified = file.lastModified(),
                                    uri = Uri.fromFile(file),
                                    type = fileType,
                                    path = file.absolutePath
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        files.sortedByDescending { it.dateModified }
    }

    suspend fun getStorageInfo(context: Context): StorageInfo = withContext(Dispatchers.IO) {
        val files = getEchoLockFiles(context)
        val totalSize = files.sumOf { it.size }
        val audioCount = files.count { it.type == FileType.AUDIO }
        val imageCount = files.count { it.type == FileType.IMAGE }

        StorageInfo(
            totalFiles = files.size,
            totalSize = totalSize,
            audioFiles = audioCount,
            imageFiles = imageCount
        )
    }

    suspend fun deleteFile(context: Context, file: StorageFile): Boolean = withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver.delete(file.uri, null, null) > 0
            } else {
                val fileObj = File(file.path)
                fileObj.exists() && fileObj.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

data class StorageInfo(
    val totalFiles: Int,
    val totalSize: Long,
    val audioFiles: Int,
    val imageFiles: Int
)

