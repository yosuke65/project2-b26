package com.example.project2.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project2.R
import com.example.project2.ui.main.activities.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init() {
        text_view_register.setOnClickListener{
            startActivity(Intent(this,
                RegisterActivity::class.java))
        }

        button_login.setOnClickListener{
            startActivity(Intent(this,HomeActivity::class.java))
        }

    }
}