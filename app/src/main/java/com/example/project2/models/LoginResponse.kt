package com.example.project2.models

data class LoginResponse(
    val token: String,
    val user: User
)