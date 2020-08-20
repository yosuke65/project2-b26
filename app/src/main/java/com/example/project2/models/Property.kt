package com.example.project2.models

data class Property(
    val _id: String?,
    val address: String,
    val city: String,
    val country: String,
    val latitude: String,
    val longitude: String,
    val mortageInfo: Boolean,
    val propertyStatus: Boolean,
    val purchasePrice: String,
    val state: String,
    val userId: String,
    val userType: String,
    var image:String? = ""
)