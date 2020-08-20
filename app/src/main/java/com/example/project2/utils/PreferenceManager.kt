package com.example.project2.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.project2.models.User

object PreferenceManager {

    private const val USER_INFO = "user_info"
    const val ID = "id"
    const val EMAIL = "email"
    const val USERNAME = "username"
    const val TOKEN = "token"
    const val TYPE = "type"
    const val IS_LOGGED_IN = "isLoggedIn"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
    }

    fun getPreference(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getPreference(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }


    fun savePreference(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun savePreference(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun logout() {
        sharedPreferences.edit().clear().commit()
    }
}