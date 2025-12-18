package com.example.echolock.api

data class RecentImageResponse(
    val status: String,
    val data: List<ImageItem>
)

data class ImageItem(
    val fileName: String,
    val size: String,
    val time: String
)
