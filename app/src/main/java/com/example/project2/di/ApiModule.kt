package com.example.project2.di

import android.util.Log
import com.example.project2.api.Endpoint
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    companion object{
        const val TAG = "ApiModule"
    }

   private val BASE_URL = "https://apolis-property-management.herokuapp.com/api/"

    @Provides
    @Singleton
    fun provideRetrofitClient(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }


    @Provides
    @Singleton
    fun provideEndpoint(retrofit: Retrofit): Endpoint{
        Log.v(TAG,"endpoint called")
        return retrofit.create(Endpoint::class.java)
    }


}
