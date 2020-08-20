package com.example.project2.models

data class PropertyPostResponse(
    val `data`: Property,
    val error: Boolean,
    val message: String
)
