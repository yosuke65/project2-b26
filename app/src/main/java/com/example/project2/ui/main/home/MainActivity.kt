package com.example.project2.ui.main.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project2.R
import com.example.project2.utils.PreferenceManager
import com.example.project2.ui.auth.login.LoginActivity
import com.example.project2.utils.PreferenceManager.IS_LOGGED_IN

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        //Check login status and navigate to activity
        if(PreferenceManager.getPreference(IS_LOGGED_IN,false)){
            startActivity(Intent(this, HomeActivity::class.java))
        }else{
            startActivity(Intent(this,
                LoginActivity::class.java))
        }
    }
}