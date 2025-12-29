package com.example.echolock.api
data class ProfileResponse(
    val status: String,
    val name: String?,
    val email: String,
    val profile_image: String?
)
