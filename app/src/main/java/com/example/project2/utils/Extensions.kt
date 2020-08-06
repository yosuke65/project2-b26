package com.example.project2.utils

import android.content.Context
import android.widget.Toast
import androidx.core.app.ActivityCompat

fun Context.toast(msg:String){
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}