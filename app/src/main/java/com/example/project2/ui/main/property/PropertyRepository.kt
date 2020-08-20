package com.example.project2.ui.main.property

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.project2.api.Endpoint
import com.example.project2.models.*
import com.example.project2.utils.PreferenceManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PropertyRepository(private val endpoint: Endpoint) {

    private val userId = PreferenceManager.getPreference(PreferenceManager.ID, "")
    private var propertyPostResponse = MutableLiveData<PropertyPostResponse>()
    private var propertyGetResponse = MutableLiveData<PropertyGetResponse>()
    private var properties = MutableLiveData<ArrayList<Property>>()
    private val storageRef = FirebaseStorage.getInstance().reference.child(userId)
    private val databaseRef = FirebaseDatabase.getInstance().reference.child(userId)

    companion object {
        const val TAG = "AddPropertyRepository"
    }

    init {
        Log.v(TAG, "AddPropertyRepository created :$this")
    }

    fun getPropertyResponse() = propertyPostResponse
    fun getProperties() = properties


    //    fun addNewProperty(property: Property, contentUri: Uri?, imageFileName: String?) {
    fun addNewProperty(
        property: Property,
        imageFilePath: String?
    ) {
        Log.v(TAG, "addNewProperty: $this")

        //upload image first to get the image location and save as property data
        if (imageFilePath != null) {
//            saveImageToFirebase(contentUri, imageFileName,property)
            saveImageToApi(imageFilePath, property)
        } else {
            postProperty(property)
        }

    }

    private fun postProperty(property: Property) {
        endpoint.getAddPropertyUrl(property).enqueue(object : Callback<PropertyPostResponse> {
            override fun onFailure(call: Call<PropertyPostResponse>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<PropertyPostResponse>,
                postResponse: Response<PropertyPostResponse>
            ) {
                Log.d(TAG, "postProperty: Post Success")
                propertyPostResponse.postValue(postResponse.body())

            }

        })
    }

    fun deleteProperty(property: Property){
        endpoint.getDeletePropertyUrl(property._id!!).enqueue(object:Callback<PropertyDeleteResponse>{
            override fun onFailure(call: Call<PropertyDeleteResponse>, t: Throwable) {
                Log.d(TAG, "deleteProperty: Failed")
            }

            override fun onResponse(
                call: Call<PropertyDeleteResponse>,
                response: Response<PropertyDeleteResponse>
            ) {
                Log.d(TAG, "deleteProperty(): ${response.body()!!.message}")
            }

        })
    }

    fun readAllProperties(){
        val userId = PreferenceManager.getPreference(PreferenceManager.ID, "")
        Log.d(TAG, "userId: $userId")
        endpoint.getReadAllPropertyUrl().enqueue(object : Callback<PropertyGetResponse> {
            override fun onFailure(call: Call<PropertyGetResponse>, t: Throwable) {
                Log.d(TAG, "onFailure")
            }

            override fun onResponse(
                call: Call<PropertyGetResponse>,
                getResponse: Response<PropertyGetResponse>
            ) {
                Log.d(TAG, "readAllProperty: ${getResponse.body()?.data}")
                var list = getResponse.body()?.data as List<Property>
                var filteredList:List<Property> = list.filter { it.userId == userId }
                Log.d(TAG, "filtered: ${filteredList}")
                properties.postValue(ArrayList(filteredList))
            }
        })
    }

    fun readProperties() {
        val userId = PreferenceManager.getPreference(PreferenceManager.ID, "")
        Log.d(TAG, "userId: $userId")
        endpoint.getReadPropertyUrl(userId).enqueue(object : Callback<PropertyGetResponse> {
            override fun onFailure(call: Call<PropertyGetResponse>, t: Throwable) {
                Log.d(TAG, "onFailure")
            }

            override fun onResponse(
                call: Call<PropertyGetResponse>,
                getResponse: Response<PropertyGetResponse>
            ) {
                Log.d(TAG, "readProperty: ${getResponse.body()?.data}")
                var list = getResponse.body()?.data as ArrayList<Property>
                properties.postValue(list)
            }
        })
    }

    private fun saveImageToApi(imageFilePath: String, property: Property) {
        val imageFile = File(imageFilePath)
        var requestFile = RequestBody.create(MediaType.parse("image/jpeg"),imageFile)
        //MultipartBody for uploading different data type and also to upload larger file size
        var body = MultipartBody.Part.createFormData("image",imageFile.name,requestFile)
        endpoint.getUploadImageUrl(body).enqueue(object : Callback<UploadImageResponse> {
            override fun onFailure(call: Call<UploadImageResponse>, t: Throwable) {
                Log.d(TAG, "onFailure()")
            }

            override fun onResponse(
                call: Call<UploadImageResponse>,
                response: Response<UploadImageResponse>
            ) {
                Log.d(TAG, "onResponse: ${response.body()?.data}")
                if (response.isSuccessful) {
                    Log.d(TAG, "saveImageToApi: Success Url = ${response.body()?.data?.location}")
                    property.image = response.body()?.data?.location
                    postProperty(property)
                } else {
                    Log.d(TAG, "onResponse: Failure")
                }
            }

        })
    }

//    private fun saveImageToFirebase(contentUri: Uri, imageFileName: String, property: Property) {
//        Log.v(TAG, "saveImage :$this")
//        val ref = storageRef.child("property_image/ +$imageFileName")
//        val uploadTask = ref.putFile(contentUri)
//        uploadTask.continueWithTask {
//            if (!it.isSuccessful) {
//                it.exception?.let { throw it }
//            }
//
//            ref.downloadUrl
//        }.addOnCompleteListener {
//
//            if (it.isSuccessful) {
//                val downloadUrl = it.result
//                property.image = downloadUrl.toString()
//                postProperty(property)
//                Log.d(TAG, "saveImage: Success")
//            } else {
//                Log.d(TAG, "saveImage: Failed")
//            }
//        }
//    }

}