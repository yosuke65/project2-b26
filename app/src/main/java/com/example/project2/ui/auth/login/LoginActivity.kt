package com.example.project2.ui.auth.login

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.project2.R
import com.example.project2.base.BaseApplication
import com.example.project2.databinding.ActivityLoginBinding
import com.example.project2.models.LoginResponse
import com.example.project2.ui.auth.register.RegisterActivity
import com.example.project2.ui.main.home.HomeActivity
import com.example.project2.utils.toast
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel:LoginViewModel
    private lateinit var binding:ActivityLoginBinding

    @Inject
    lateinit var loginRepository: LoginRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    private fun init() {

        //Injection
        val baseApplication = application as BaseApplication
        baseApplication.getAppComponent().inject(this)
        //ViewModel
        viewModel = ViewModelProvider(this,LoginViewModelFactory(loginRepository)).get(LoginViewModel::class.java)

        //DataBinding
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        binding.activity = this
        binding.viewModel = viewModel
        binding.emailField = edit_text_email
        binding.passwordField = edit_text_password
        binding.lottie = lottie_loading_anim

        edit_text_email.defaultHintTextColor = ColorStateList.valueOf(Color.WHITE).withAlpha(100)
        edit_text_password.defaultHintTextColor = ColorStateList.valueOf(Color.WHITE).withAlpha(100)
        observe()
    }

    private fun observe() {
        viewModel.getObservablePostResponse().observe(this, Observer<LoginResponse>{
            Handler().postDelayed({
                if(it != null){
                    viewModel.loginSuccess(it)
                    startActivity(Intent(this,
                        HomeActivity::class.java))
                }else{
                    toast("Login Failed")
                }
                lottie_loading_anim.cancelAnimation()
                lottie_loading_anim.visibility = View.INVISIBLE
            },3000)

        })
    }

    fun startRegisterActivity(){
        startActivity(Intent(this,
            RegisterActivity::class.java))
    }
}