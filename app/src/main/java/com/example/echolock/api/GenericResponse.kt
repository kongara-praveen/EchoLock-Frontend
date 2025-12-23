package com.example.echolock.api

data class GenericResponse(
    val status: String,
    val message: String? = null,
    val image_name: String? = null   // ✅ ADD THIS
)
