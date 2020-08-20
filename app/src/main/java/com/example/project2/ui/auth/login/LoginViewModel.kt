package com.example.project2.ui.auth.login

import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.project2.utils.PreferenceManager
import com.example.project2.models.LoginResponse
import com.example.project2.models.User
import com.google.android.material.textfield.TextInputLayout

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    companion object{
        const val TAG = "LoginViewModel"
    }
    var email = ObservableField<String>()
    var password = ObservableField<String>()


    fun login(emailField: TextInputLayout, passwordField: TextInputLayout,lottie: LottieAnimationView) {
        Log.v(TAG,"login")
       if(validateUserInput(emailField, passwordField)) {
           repository.login(email.get()!!,password.get()!!)
           lottie.visibility = View.VISIBLE

           lottie.playAnimation()
       }
    }

    fun getObservablePostResponse() = repository.getPostResponse()

    private fun validateUserInput(
        emailField: TextInputLayout,
        passwordField: TextInputLayout
    ): Boolean {
        return if (emailField.editText?.text.isNullOrEmpty()) {
            emailField.editText?.error = "Required"
            emailField.editText?.requestFocus()
            false
        } else if (passwordField.editText?.text.isNullOrEmpty()) {
            passwordField.editText?.error = "Required"
            passwordField.editText?.requestFocus()
            false
        } else {
            true
        }
    }

    fun loginSuccess(response: LoginResponse) {

        saveUserInfo(response.user, response.token)
    }

    private fun saveUserInfo(user: User,token:String) {
        PreferenceManager.apply {
            savePreference(ID, user._id!!)
            savePreference(EMAIL, user.email!!)
            savePreference(USERNAME, user.name!!)
            savePreference(TOKEN, token)
            savePreference(TYPE, user.type!!)
            savePreference(IS_LOGGED_IN, true)
        }

    }
}

class LoginViewModelFactory(private val repository: LoginRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("It cannot only create LoginViewModel object ")
    }

}