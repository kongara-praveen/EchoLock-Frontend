package com.example.echolock.model

data class FileItem(
    val name: String,
    val size: String,
    val date: String,
    val type: String,   // "audio" or "image"
    val file_id: Int? = null,
    val file_url: String? = null
)
