package com.example.echolock.api

data class RegisterResponse(
    val status: String,
    val message: String,
    val user_id: Int? = null
)
