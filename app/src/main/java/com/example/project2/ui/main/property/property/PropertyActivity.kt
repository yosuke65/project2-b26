package com.example.project2.ui.main.property.property

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project2.BuildConfig
import com.example.project2.R
import com.example.project2.base.BaseApplication
import com.example.project2.models.Property
import com.example.project2.ui.main.property.AdapterPropertyList
import com.example.project2.ui.main.property.addproperty.AddPropertyActivity
import com.example.project2.ui.main.property.PropertyRepository
import com.example.project2.utils.SwipeToDeletePropertyCallback
import com.example.project2.utils.SwipeToDeleteTaskCallback
import com.example.project2.utils.toast
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_property.*
import kotlinx.android.synthetic.main.toolbar_collapsing_property.*
import javax.inject.Inject

class PropertyActivity : AppCompatActivity() {

    companion object {
        const val TAG = "PropertyActivity"
        const val API_KEY = BuildConfig.API_KEY
    }

    private val AUTOCOMPLETE_REQUEST_CODE = 1

    private lateinit var mAdapter: AdapterPropertyList
    private lateinit var viewModel: PropertyViewModel
    private lateinit var callback: SwipeToDeletePropertyCallback

    @Inject
    lateinit var propertyRepository: PropertyRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property)

        init()
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.readProperties()
    }

    private fun init() {

        val baseApplication = application as BaseApplication
        baseApplication.getAppComponent().inject(this)

        //viewModel
        viewModel = ViewModelProvider(
            this,
            PropertyViewModelProvider(
                propertyRepository
            )
        ).get(PropertyViewModel::class.java)
        //Recycler view
        mAdapter =
            AdapterPropertyList(this)
        recycler_view_property.layoutManager = LinearLayoutManager(this)
        recycler_view_property.adapter = mAdapter

        //Google place api
        Places.initialize(
            applicationContext,
            API_KEY
        ) //*******Add Api key to manifest file*************

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)

        fab_add_property.setOnClickListener {
            startAutoCompleteActivity()
        }

        viewModel.readProperties()
        observe()

        //Toolbar
        setToolBar()

        //SwipeToDeleteCallback
        callback = SwipeToDeletePropertyCallback(this,mAdapter,viewModel)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recycler_view_property)

    }

    private fun setToolBar() {

        button_home_property.setOnClickListener{
            onBackPressed()
        }

        val toolbar: AppBarLayout = toolbar_collapsing_property
        toolbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            private var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout!!.totalScrollRange
                }
                button_home_property.translationY = -verticalOffset.toFloat()
                button_more.translationY = -verticalOffset.toFloat()
            }
        })
    }

    private fun observe() {
        viewModel.getObservablePropertyUrls().observe(this, Observer<ArrayList<Property>> {
            if (it != null) {
                mAdapter.setData(it)
                if (it.size != 0) {
                    hideEmptyMessage()
                }else{
                    showEmptyMessage()
                }
                toast("Size: ${it.size}")
                callback.setData(it)
            } else {
                toast("failed")
            }
        })
    }

    private fun showEmptyMessage() {
        text_view_property_empty.visibility = View.VISIBLE
    }

    private fun hideEmptyMessage() {
        text_view_property_empty.visibility = View.INVISIBLE
    }

    private fun startAutoCompleteActivity() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .setCountry("USA")
            .setTypeFilter(TypeFilter.ADDRESS)
            .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i(TAG, "Place: ${place.name}, ${place.id},${place.address}")

                        startAddPropertyActivity(place)
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i(TAG, status.statusMessage)
                    }
                    toast("Error")
                }
                Activity.RESULT_CANCELED -> {
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun startAddPropertyActivity(place: Place) {
        var mIntent = Intent(this, AddPropertyActivity::class.java)
        mIntent.putExtra(Place.Field.ADDRESS.toString(), place.address)
        startActivity(mIntent)
    }

}