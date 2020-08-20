package com.example.project2.ui.auth.register

import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project2.R
import com.example.project2.models.User
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterViewModel(private var repository: RegisterRepository) : ViewModel() {

    companion object {
        const val TAG = "RegisterViewModel"
    }

    var name = ObservableField<String>()
    var landlordEmail = ObservableField<String>()
    var email = ObservableField<String>()
    var password = ObservableField<String>()
    var type = ObservableField<Int>()

    fun register(
        nameField: TextInputLayout,
        emailField: TextInputLayout,
        passwordField: TextInputLayout,
        landlordEmailField: TextInputLayout,
        typeField: RadioGroup
    ) {
//    fun register() {

        if (validateUserInput(
                nameField,
                emailField,
                passwordField,
                landlordEmailField,
                typeField
            )) {

            var type = when (type.get()) {
                R.id.radio_landlord -> "landlord"
                R.id.radio_tenant -> "tenant"
                else -> ""
            }
            repository.registerUser(
                User(
                    _id = null,
                    name = name.get()!!,
                    email = email.get()!!,
                    password = password.get()!!,
                    type = type,
                    landlordEmail = landlordEmail.get() ?: null
                )
            )

        } else {

        }


    }

    private fun validateUserInput(
        nameField: TextInputLayout,
        emailField: TextInputLayout,
        passwordField: TextInputLayout,
        landlordEmailField: TextInputLayout,
        typeField: RadioGroup
    ):Boolean {
        Log.v(TAG,typeField.checkedRadioButtonId.toString())
        return if(typeField.checkedRadioButtonId == 0){
            var error = typeField[3] as EditText
            error.visibility = View.VISIBLE
            error.error = "Select user type"
            error.requestFocus()
            false
        }else if(type.get() == R.id.radio_tenant && landlordEmailField.editText?.text.isNullOrEmpty()){
            landlordEmailField.editText?.error = "Required"
            landlordEmailField.editText?.requestFocus()
            false
        }
        else if (nameField.editText?.text.isNullOrEmpty()) {
            nameField.editText?.error = "Required"
            nameField.editText?.requestFocus()
            false
        }else if(emailField.editText?.text.isNullOrEmpty()){
            emailField.editText?.error = "Required"
            emailField.editText?.requestFocus()
            false
        }else if(passwordField.editText?.text.isNullOrEmpty()){
            passwordField.editText?.error = "Required"
            passwordField.editText?.requestFocus()
            false
        } else
        {
            true
        }
    }

    fun hideUserTypeError(radioGroup: RadioGroup) {
        var error = radioGroup[3] as EditText
        error.clearFocus()
        error.error = ""
        error.visibility = View.GONE
    }


    fun getObservablePostResponse() = repository.getPostResponse()

    fun clearLandlordEmailField(landlordEmailField: TextInputLayout?) {
        landlordEmailField?.editText?.text?.clear()
    }
}

class RegisterViewModelFactory(private val repository: RegisterRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("It can only create RegisterViewModel object")
    }

}
