package com.example.project2.models

data class RegisterResponse(
    val `data`: User,
    val error: Boolean,
    val message: String
)
