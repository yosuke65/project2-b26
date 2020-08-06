package com.example.project2.ui.main.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project2.R
import com.example.project2.ui.auth.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        var isLoggedIn = false
        //Check login status and navigate to activity
        if(isLoggedIn){
            startActivity(Intent(this, HomeActivity::class.java))
        }else{
            startActivity(Intent(this,
                LoginActivity::class.java))
        }
    }
}