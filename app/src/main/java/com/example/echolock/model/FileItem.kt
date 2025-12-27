package com.example.echolock.model

data class FileItem(
    val name: String,
    val size: String,
    val date: String,
    val type: String   // "audio" or "image"
)
