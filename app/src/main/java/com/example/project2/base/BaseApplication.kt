package com.example.project2.base

import android.app.Application
import com.example.project2.di.AppComponent
import com.example.project2.di.DaggerAppComponent
import com.example.project2.utils.PreferenceManager

class BaseApplication: Application() {

    private lateinit var appComponent:AppComponent
    override fun onCreate() {
        super.onCreate()
        PreferenceManager.init(this)
        appComponent = DaggerAppComponent.builder().build()
    }

    fun getAppComponent() = appComponent
}