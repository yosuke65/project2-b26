package com.example.project2.ui.main.property.addproperty

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project2.models.Property
import com.example.project2.ui.main.property.PropertyRepository
import com.google.android.material.textfield.TextInputEditText

class AddPropertyViewModel(private val repository: PropertyRepository) : ViewModel() {

    companion object {
        const val TAG = "AddPropertyViewModel"
        const val ERROR_MSG = "Required"
        const val NUMERIC_ERROR = "Invalid input"
    }

    var country = ObservableField<String>()
    var address1 = ObservableField<String>()
    var address2 = ObservableField<String>()
    var city = ObservableField<String>()
    var state = ObservableField<String>()
    var zip = ObservableField<String>()
    var owned = ObservableField<Boolean>(false)
    var mortgage = ObservableField<Boolean>(false)


    private var contentUri:Uri? = null
    private var imageFileName:String? = null
    private var imageFilePath:String? = null
    
    fun getObservableResponse() = repository.getPropertyResponse()

    fun getObservableCountry() = country
    fun getObservableAddress1() = address1
    fun getObservableCity() = city
    fun getObservableState() = state
    fun getObservableZip() = zip

//    fun setContentUri(contentUri: Uri){ this.contentUri = contentUri
    fun setContentUri(contentUri: Uri){ this.contentUri = contentUri }
    fun setImageFileName(imageFileName: String){ this.imageFileName = imageFileName }
    fun setPath(imageFilePath: String){this.imageFilePath = imageFilePath}



    fun addNewAddress(
        countryField: TextInputEditText,
        addressField: TextInputEditText,
        cityField: TextInputEditText,
        stateField: TextInputEditText,
        zipField: TextInputEditText,
        userType: String,
        userId: String
    ) {


        if (validateFields(countryField, addressField, cityField, stateField, zipField)) {
            repository.addNewProperty(
                Property(
                    _id = null,
                    country = country.get()!!,
                    address = address1.get()!!,
                    city = city.get()!!,
                    state = state.get()!!,
                    mortageInfo = mortgage.get()!!,
                    propertyStatus = owned.get()!!,
                    userId = userId,
                    userType = userType,
                    latitude = "",
                    longitude = "",
                    purchasePrice = ""
                ),
                imageFilePath
            )
        }
    }

    private fun validateFields(
        country: TextInputEditText,
        address1: TextInputEditText,
        city: TextInputEditText,
        state: TextInputEditText,
        zip: TextInputEditText
    ): Boolean {
        return if (country.text.isNullOrEmpty()) {
            country.error =
                ERROR_MSG
            country.requestFocus()
            false
        } else if (address1.text.isNullOrEmpty()) {
            address1.error =
                ERROR_MSG
            address1.requestFocus()
            false
        } else if (city.text.isNullOrEmpty()) {
            city.error =
                ERROR_MSG
            city.requestFocus()
            false
        } else if (state.text.isNullOrEmpty()) {
            state.error =
                ERROR_MSG
            state.requestFocus()
            false
        } else if (zip.text.isNullOrEmpty()) {
            zip.error =
                ERROR_MSG
            zip.requestFocus()
            false
        } else if (!zip.text!!.matches(Regex("\\d+"))) {
            zip.error =
                NUMERIC_ERROR
            zip.requestFocus()
            false
        } else {
            true
        }
    }

}


class AddPropertyViewModelProvider(private val repository: PropertyRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddPropertyViewModel::class.java)) {
            return AddPropertyViewModel(
                repository
            ) as T
        }
        throw IllegalArgumentException("Only AddPropertyViewModel object can be created")
    }

}