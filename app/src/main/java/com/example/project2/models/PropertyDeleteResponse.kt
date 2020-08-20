package com.example.project2.models

data class PropertyDeleteResponse(
    val `data`: Property,
    val error: Boolean,
    val message: String
)
