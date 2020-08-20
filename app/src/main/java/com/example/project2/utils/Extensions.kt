package com.example.project2.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

const val TAG = "Extension"
fun Context.toast(msg:String){
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}
@BindingAdapter("errorMessage")
fun TextInputEditText.setErrorMessage(errorMsg:String){
    Log.v(TAG,"errorMessage")
    if(this.text.isNullOrBlank()){
        this.error = errorMsg
        this.requestFocus()
    }
}

@BindingAdapter("loadImage")
fun ImageView.load(url:String){
    Glide.with(this).load(url).fitCenter().centerCrop().into(this)
}