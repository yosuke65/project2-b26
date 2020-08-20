package com.example.project2.ui.main.property.addproperty

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.example.project2.R
import com.example.project2.databinding.FragmentImageSelectionBinding
import com.example.project2.utils.toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_add_property.*
import kotlinx.android.synthetic.main.fragment_image_selection.*
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "ImageSelectionFragment"

class ImageSelectionFragment() : BottomSheetDialogFragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding:FragmentImageSelectionBinding
    private var listener:OnFragmentCallback? = null

    interface OnFragmentCallback{
        fun onOpenCameraClicked()
        fun onOpenGalleryClicked()
        fun onCancelClicked()
    }

    fun setOnFragmentCallback(onFragmentCallback: OnFragmentCallback){
        listener = onFragmentCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_image_selection, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ImageSelectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun init(){
        binding.fragment = this@ImageSelectionFragment
    }

    fun openCamera(){
        listener?.onOpenCameraClicked()
    }

    fun openGallery(){
        listener?.onOpenGalleryClicked()
    }

    fun onCancelClicked(){
        listener?.onCancelClicked()
    }


}