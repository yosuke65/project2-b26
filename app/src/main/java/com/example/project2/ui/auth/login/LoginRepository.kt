package com.example.project2.ui.auth.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.project2.api.Endpoint
import com.example.project2.models.LoginResponse
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response

class LoginRepository (private val endpoint: Endpoint){

    companion object{
        const val TAG = "LoginRepository"
    }

    private var postResponse = MutableLiveData<LoginResponse>()

    fun getPostResponse() = postResponse

    fun login(email:String,password:String) {
        Log.v(TAG,"login")
        endpoint.getLoginUrl(email, password).enqueue(object:retrofit2.Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                postResponse.postValue(response.body())
            }

        })
    }
}