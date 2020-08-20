package com.example.project2.ui.main.property.property

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project2.models.Property
import com.example.project2.ui.main.property.PropertyRepository

class PropertyViewModel(private val repository: PropertyRepository):ViewModel() {

    companion object{
        private const val TAG = "PropertyViewModel"
    }
    fun readProperties(){
        repository.readAllProperties()
//        repository.readProperties()
    }

    fun deleteProperty(property:Property){
        repository.deleteProperty(property)
    }


    fun getObservablePropertyUrls() = repository.getProperties()
}

class PropertyViewModelProvider(private val repository: PropertyRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PropertyViewModel::class.java)){
            return PropertyViewModel(
                repository
            ) as T
        }
        throw IllegalArgumentException("Only PropertyViewModel object can be created")
    }

}