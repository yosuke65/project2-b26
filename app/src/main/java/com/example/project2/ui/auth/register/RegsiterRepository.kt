package com.example.project2.ui.auth.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.project2.api.Endpoint
import com.example.project2.models.RegisterResponse
import com.example.project2.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterRepository(private val endpoint: Endpoint) {

    private val postResponse = MutableLiveData<String>()

    companion object{
        const val TAG = "RegisterRepository"
    }

    fun getPostResponse() = postResponse
    fun registerUser(user: User){
        Log.v(TAG,"RegisterRepository")
        endpoint.getRegisterUrl(user).enqueue(object:Callback<RegisterResponse> {
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.v(TAG,"Failed")
            }

            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                Log.v(TAG,"Register Success: ${response.body()}")
                Log.v(TAG,response.message())
                Log.v(TAG,response.errorBody().toString())
                postResponse.value = response.message()
            }

        })
    }
}