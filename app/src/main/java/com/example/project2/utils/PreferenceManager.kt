package com.example.project2.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.project2.models.User

object SessionManager {

    private const val USER_INFO = "user_info"
    private const val ID = "id"
    private const val EMAIL = "email"
    private const val USERNAME = "username"
    private const val TOKEN = "token"
    private const val TYPE = "type"
    private const val ISLOGGEDIN = "isLoggedIn"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
    }

    fun getPreference(key:String,defaultValue:String):String{
        return sharedPreferences.getString(key,defaultValue)?:defaultValue
    }

    fun savePreference(key: String,value:String){
        sharedPreferences.edit().putString(key,value).apply()
    }

    fun saveLoginStatus(user: User, token: String) {
        editor.apply {
            putString(ID, user._id)
            putString(EMAIL, user.email)
            putString(USERNAME, user.name)
            putString(TOKEN, token)
            putString(TYPE, user.type)
            putBoolean(ISLOGGEDIN, true)
        }.commit()
    }

    fun getUserType() = sharedPreferences.getString(TYPE, null)

    fun getUserId() = sharedPreferences.getString(ID, null)

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(ISLOGGEDIN, false)
    }

    fun logout() {
        editor.clear().commit()
    }
}