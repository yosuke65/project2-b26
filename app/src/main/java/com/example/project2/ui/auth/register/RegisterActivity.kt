package com.example.project2.ui.auth.register

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.project2.R
import com.example.project2.base.BaseApplication
import com.example.project2.databinding.ActivityRegisterBinding
import com.example.project2.ui.auth.login.LoginActivity
import com.example.project2.utils.toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.edit_text_email
import kotlinx.android.synthetic.main.activity_register.edit_text_password
import javax.inject.Inject

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    @Inject
    lateinit var registerRepository: RegisterRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {

        //Injection
        val baseApplication = application as BaseApplication
        baseApplication.getAppComponent().inject(this)

        //ViewModel
        viewModel = ViewModelProvider(
            this,
            RegisterViewModelFactory(registerRepository)
        ).get(RegisterViewModel::class.java)

        //Binding
        binding = DataBindingUtil.setContentView(this, (R.layout.activity_register))
        binding.activity = this
        binding.viewModel = viewModel
        binding.nameField = edit_text_name
        binding.emailField = edit_text_email
        binding.passwordField = edit_text_password
        binding.landlordEmailField = edit_text_landlord_email
        binding.typeField = radio_group_user_type

        edit_text_email.defaultHintTextColor = ColorStateList.valueOf(Color.WHITE).withAlpha(100)
        edit_text_password.defaultHintTextColor = ColorStateList.valueOf(Color.WHITE).withAlpha(100)
        edit_text_landlord_email.defaultHintTextColor = ColorStateList.valueOf(Color.WHITE).withAlpha(100)
        edit_text_name.defaultHintTextColor = ColorStateList.valueOf(Color.WHITE).withAlpha(100)

        observe()

    }

    private fun observe() {
        viewModel.getObservablePostResponse().observe(this, Observer<String>{
            if(it != null && it == "Created"){
                toast(it)
            }else{
                toast(it)
            }
        })
    }

    fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }


    fun fadeOutEditTextAndSlideDownRadioButton() {
        viewModel.hideUserTypeError(radio_group_user_type)
        viewModel.clearLandlordEmailField(edit_text_landlord_email)
        var animEditText =
            AnimationUtils.loadAnimation(
                this@RegisterActivity, R.anim.anim_fade_out_edit_text_in_register
            ).also {
                it.setInterpolator(
                    this@RegisterActivity,
                    android.R.interpolator.decelerate_quad
                )
                it.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(anim: Animation?) {
                    }

                    override fun onAnimationEnd(anim: Animation?) {
                        edit_text_landlord_email.visibility = View.INVISIBLE
                        edit_text_landlord_email.isEnabled = false
                        radio_group_user_type.animate()
                            .setDuration(500)
                            .translationY((edit_text_landlord_email.height).toFloat())
                            .withEndAction {
                                edit_text_landlord_email.visibility = View.INVISIBLE
                            }.start()
                    }

                    override fun onAnimationStart(anim: Animation?) {
                    }
                })
            }
        if (edit_text_landlord_email.visibility == View.VISIBLE) {
            edit_text_landlord_email.startAnimation(animEditText)
        }


    }



    fun fadeInEditTextAndSlideUpRadioButton() {
        viewModel.hideUserTypeError(radio_group_user_type)
        var animRadioGroup =
            AnimationUtils.loadAnimation(this, R.anim.anim_transition_up_radio_group)
                .also {
                    it.setInterpolator(this, android.R.interpolator.decelerate_quad)
                    it.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationRepeat(anim: Animation?) {

                        }

                        override fun onAnimationEnd(anim: Animation?) {
                            var animEditText = AnimationUtils.loadAnimation(
                                this@RegisterActivity,
                                R.anim.anim_fade_in_edit_text_in_register
                            )
                            edit_text_landlord_email.animation = animEditText.also { it.start() }
                            edit_text_landlord_email.visibility = View.VISIBLE
                            edit_text_landlord_email.isEnabled = true

                        }

                        override fun onAnimationStart(anim: Animation?) {
                            edit_text_landlord_email.visibility = View.INVISIBLE
                        }
                    })
                }

        if (edit_text_landlord_email.visibility == View.GONE) {
            //Initiate animation for first time
            radio_group_user_type.startAnimation(animRadioGroup)

        } else if (edit_text_landlord_email.visibility == View.INVISIBLE) {
            radio_group_user_type.animate()
                .translationYBy(-(edit_text_landlord_email.height).toFloat())
                .withEndAction {
                    var animEditText = AnimationUtils.loadAnimation(
                        this@RegisterActivity,
                        R.anim.anim_fade_in_edit_text_in_register
                    )
                    edit_text_landlord_email.startAnimation(animEditText)
                    edit_text_landlord_email.visibility = View.VISIBLE
                    edit_text_landlord_email.isEnabled = true

                }
                .setDuration(500)
                .start()
        }
    }
}