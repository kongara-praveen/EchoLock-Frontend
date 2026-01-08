package com.example.echolock.model

import com.google.gson.annotations.SerializedName

data class FileItem(
    val name: String = "",
    val size: String = "",
    val date: String = "",
    val type: String = "",   // "audio" or "image"
    @SerializedName("file_id")
    val file_id: Int? = null,
    @SerializedName("file_url")
    val file_url: String? = null
)
