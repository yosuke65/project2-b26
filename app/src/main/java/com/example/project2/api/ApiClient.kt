package com.example.project2.api

import com.example.project2.models.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface Endpoint {
    @POST("auth/register")
    fun getRegisterUrl(@Body user: User): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("auth/login")
    fun getLoginUrl(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @POST("property")
    fun getAddPropertyUrl(@Body property: Property): Call<PropertyPostResponse>

    @Multipart
    @POST("upload/property/picture")
    fun getUploadImageUrl(@Part body: MultipartBody.Part):Call<UploadImageResponse>

    @GET("property/user/{userId}")
    fun getReadPropertyUrl(@Path("userId") userId:String):Call<PropertyGetResponse>

    @GET("property")
    fun getReadAllPropertyUrl():Call<PropertyGetResponse>
    @DELETE("property/{id}")
    fun getDeletePropertyUrl(@Path("id") id:String):Call<PropertyDeleteResponse>


}
