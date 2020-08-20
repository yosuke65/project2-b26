package com.example.project2.ui.main.property.addproperty

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.project2.R
import com.example.project2.base.BaseApplication
import com.example.project2.databinding.ActivityAddPropertyBinding
import com.example.project2.utils.PreferenceManager
import com.example.project2.models.PropertyPostResponse
import com.example.project2.ui.main.property.PropertyRepository
import com.example.project2.utils.toast
import com.google.android.libraries.places.api.model.Place
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_add_property.*
import kotlinx.android.synthetic.main.toolbar_collapsing_add_property.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import javax.inject.Inject

class AddPropertyActivity : AppCompatActivity(), ImageSelectionFragment.OnFragmentCallback {

    companion object {
        const val TAG = "AddPropertyActivity"
        const val REQUEST_CODE_GALLERY = 103
        const val REQUEST_CODE_SETTING = 101
        const val REQUEST_CODE_CAMERA = 100
    }

    private lateinit var viewModel: AddPropertyViewModel
    private lateinit var binding: ActivityAddPropertyBinding
    private var bottomSheet = ImageSelectionFragment()


    @Inject
    lateinit var addPropertyRepository: PropertyRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_property)

        init()
    }


    private fun init() {

        //Injection
        val baseApplication = application as BaseApplication
        baseApplication.getAppComponent().inject(this)

        //ViewModel
        viewModel = ViewModelProvider(
            this,
            AddPropertyViewModelProvider(
                addPropertyRepository
            )
        ).get(AddPropertyViewModel::class.java)

        //Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_property)
        binding.viewModel = viewModel
        binding.activity = this
        binding.country = edit_text_country
        binding.address1 = edit_text_address1
        binding.city = edit_text_city
        binding.state = edit_text_state
        binding.zip = edit_text_zip_code
        binding.userId = PreferenceManager.getPreference(PreferenceManager.ID, "")
        binding.userType = PreferenceManager.getPreference(PreferenceManager.TYPE, "")

        observe()

        //AutoComplete
        getPrediction()

        //toolbar
        setToolbar()

        //FragmentCallback
        bottomSheet.setOnFragmentCallback(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.v(TAG, "requestCode: $requestCode")
        Log.v(TAG, "onActivityResult")

        when (requestCode) {
            REQUEST_CODE_GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    val selectedPhotoUri = data?.data as Uri
                    try {
                        selectedPhotoUri?.also {
                            if(Build.VERSION.SDK_INT < 28) {
                                val bitmap = MediaStore.Images.Media.getBitmap(
                                    this.contentResolver,
                                    selectedPhotoUri
                                )
                                var uri = getImageUri(this, bitmap)
                                var path:String? = getRealPathFromURI(uri)
                                viewModel.setPath(path!!)
                            } else {
                                val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
                                val bitmap = ImageDecoder.decodeBitmap(source)
                                var uri = getImageUri(this, bitmap)
                                var path:String? = getRealPathFromURI(uri)
                                viewModel.setPath(path!!)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
//                    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//                    val imageFileName = timeStamp + "." +getFileExtension(contentUri)
//                    Log.v(TAG,imageFileName)
                    text_view_file_name.text = "1 image file attached"
//                    viewModel.setContentUri(contentUri)
//                    viewModel.setImageFileName(imageFileName)
                } else {
                    toast("Failed to pick image")
                }
            }
        }
    }
    // get URI from bitmap
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    // get actual path
    fun getRealPathFromURI(uri: Uri?): String? {
        val cursor: Cursor? = contentResolver.query(uri!!, null, null, null, null)
        cursor!!.moveToFirst()
        val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }

    private fun getFileExtension(contentUri: Uri): String? {
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver?.getType(contentUri))
    }

    private fun setToolbar() {
        toolbar_collapsing_add_prop.addOnOffsetChangedListener(object :
            AppBarLayout.OnOffsetChangedListener {
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout!!.totalScrollRange
                }
                Log.v(TAG, "ScrollRange: $scrollRange, VerticalOffset: $verticalOffset, Radius = ")
                var cardRadius = (verticalOffset + scrollRange) / 4
                toolbar_collapsed_cardview.radius = cardRadius.toFloat()
                Log.v(
                    TAG,
                    "ScrollRange: $scrollRange, VerticalOffset: $verticalOffset, Radius = $cardRadius"
                )
                //Fade in
                text_view_collapsed_title.alpha = 1 - (verticalOffset + scrollRange) / 500.toFloat()
                //Fixed position
                button_home.translationY = -verticalOffset.toFloat()
                //Fade out
                text_view_detail.alpha = (verticalOffset + scrollRange) / 500.toFloat()
                image_view_icon_location.alpha = (verticalOffset + scrollRange) / 500.toFloat()
            }

        })

        button_home.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getPrediction() {
        try {
            val address = intent.getStringExtra(Place.Field.ADDRESS.toString())
            val addressCityStateZip = address.split(",").toTypedArray()
            val stateAndZip = addressCityStateZip[2].trim().split(("\\s".toRegex())).toTypedArray()
            Log.v(TAG, "$address")
            viewModel.apply {
                getObservableAddress1().set(addressCityStateZip[0])
                getObservableCity().set(addressCityStateZip[1])
                getObservableCountry().set(addressCityStateZip[3])
                getObservableState().set(stateAndZip[0])
                getObservableZip().set(stateAndZip[1])
            }
        } catch (e: Exception) {
            Snackbar.make(layout_add_property, "Fields don't match", Snackbar.LENGTH_LONG).show()
            Log.e(TAG, e.toString())
        }
    }

    private fun observe() {
        viewModel
        viewModel.getObservableResponse().observe(this, Observer<PropertyPostResponse> {
            if (it != null) {
                finish()
                viewModel.getObservableResponse().value = null
            }
        })
    }

    fun uploadImage() {
        Log.v(TAG, "uploadImage")
        showImageSelectBottomSheet()
    }

    private fun showImageSelectBottomSheet() {
        bottomSheet.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.SheetDialog)
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    private fun hideImageSelectedBottomSheet() {
        bottomSheet.dismiss()
    }

    private fun cameraPermission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    openCamera()
                }

                override fun onPermissionRationaleShouldBeShown(
                    request: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }

                override fun onPermissionDenied(deniedResponse: PermissionDeniedResponse?) {
                }

            }).check()
    }

    private fun readAndWritePermission() {
        Dexter.withContext(this).withPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report?.grantedPermissionResponses?.size == 2) {
                    openGallery()
                } else {
                    showApplicationSettingDialog()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                list: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                token!!.continuePermissionRequest()
            }

        }).check()
    }

    private fun openCamera() {
        val mIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(mIntent, REQUEST_CODE_CAMERA)
    }

    private fun openGallery() {
        val mIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(
            mIntent,
            REQUEST_CODE_GALLERY
        )
    }

    private fun showApplicationSettingDialog() {
        var dialog = AlertDialog.Builder(this)
        dialog.setTitle("Permission needed!")
            .setMessage("Require user permission to use this feature.")
            .setNegativeButton("Dismiss") { dialog, i ->
                dialog.dismiss()
            }
            .setPositiveButton("Setting") { dialog, i ->
                dialog.dismiss()
                openSetting()
            }
            .create()
            .show()
    }

    private fun openSetting() {
        val mIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        mIntent.data = uri
        startActivityForResult(
            mIntent,
            REQUEST_CODE_SETTING
        )
    }

    override fun onOpenCameraClicked() {
        cameraPermission()
        hideImageSelectedBottomSheet()
    }

    override fun onOpenGalleryClicked() {
        readAndWritePermission()
        hideImageSelectedBottomSheet()
    }

    override fun onCancelClicked() {
        hideImageSelectedBottomSheet()
    }

}