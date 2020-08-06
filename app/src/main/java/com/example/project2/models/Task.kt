package com.example.project2.models

data class Task(
    var id:String? = null,
    var isCompleted:Boolean = false,
    var note:String? = null,
    var title:String = ""
) {
}