package com.example.echolock.api

data class SignatureResponse(
    val status: String,
    val message: String? = null,
    val image_hash: String? = null,
    val integrity_signature: String? = null
)

