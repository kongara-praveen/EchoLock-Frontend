package com.example.echolock.api

import com.example.echolock.model.FileItem

data class FilesResponse(
    val status: String,
    val data: List<FileItem>? = null,
    val message: String? = null
)
