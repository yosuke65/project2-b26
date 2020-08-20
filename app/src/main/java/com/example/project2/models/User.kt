package com.example.project2.models

import com.google.gson.annotations.SerializedName

data class User(
    var _id:String?,
    var email:String,
    var landlordEmail:String?,
    var name:String,
    var password:String,
    var type:String
) {
}