package com.example.echolock.util

object HistoryTempStore {

    // IMAGE
    var lastImageFileName: String? = null

    // AUDIO ✅ ADD THIS
    var lastAudioFileName: String? = null

    fun clearImage() {
        lastImageFileName = null
    }

    fun clearAudio() {
        lastAudioFileName = null
    }
}
