package com.example.project2.ui.auth

import android.animation.Animator
import android.animation.TimeInterpolator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.view.animation.TranslateAnimation
import android.widget.RadioGroup
import android.widget.Toast
import com.example.project2.R
import com.example.project2.utils.toast
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
    }

    private fun init() {
        text_view_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        radio_group_user_type.setOnCheckedChangeListener { _: RadioGroup, id: Int ->
            when (id) {
                R.id.radio_tenant -> {
                    fadeInEditTextAndSlideUpRadioButton()
                }
                R.id.radio_landlord -> {
                    fadeOutEditTextAndSlideDownRadioButton()
                }
            }
        }
    }

    private fun fadeOutEditTextAndSlideDownRadioButton() {
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
                        landlord_email.visibility = View.INVISIBLE
                        landlord_email.isEnabled = false
                        radio_group_user_type.animate()
                            .setDuration(500)
                            .translationY((landlord_email.height).toFloat())
                            .withEndAction {
                                landlord_email.visibility = View.INVISIBLE
                            }.start()
                    }

                    override fun onAnimationStart(anim: Animation?) {
                    }
                })
            }
        if (landlord_email.visibility == View.VISIBLE) {
            landlord_email.startAnimation(animEditText)
        }


    }

    private fun fadeInEditTextAndSlideUpRadioButton() {
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
                            landlord_email.animation = animEditText.also { it.start() }
                            landlord_email.visibility = View.VISIBLE
                            landlord_email.isEnabled = true

                        }

                        override fun onAnimationStart(anim: Animation?) {
                            landlord_email.visibility = View.INVISIBLE
                        }
                    })
                }

        if (landlord_email.visibility == View.GONE) {
            //Initiate animation for first time
            radio_group_user_type.startAnimation(animRadioGroup)

        } else if (landlord_email.visibility == View.INVISIBLE) {
            radio_group_user_type.animate()
                .translationYBy(-(landlord_email.height).toFloat())
                .withEndAction {
                    var animEditText = AnimationUtils.loadAnimation(
                        this@RegisterActivity,
                        R.anim.anim_fade_in_edit_text_in_register
                    )
                    landlord_email.startAnimation(animEditText)
                    landlord_email.visibility = View.VISIBLE
                    landlord_email.isEnabled = true

                }
                .setDuration(500)
                .start()
        }
    }
}