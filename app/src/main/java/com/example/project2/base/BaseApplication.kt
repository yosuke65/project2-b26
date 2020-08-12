package com.example.project2

import android.app.Application
import com.example.project2.utils.PreferenceManager

class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        PreferenceManager.init(this)
    }
}