package com.example.echolock.api

data class RecentAudioResponse(
    val status: String,
    val data: List<AudioItem>
)

data class AudioItem(
    val fileName: String,
    val size: String,
    val time: String
)
