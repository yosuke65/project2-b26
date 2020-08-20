package com.example.project2.ui.main.todo

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project2.R
import com.example.project2.utils.SwipeToDeleteTaskCallback
import com.example.project2.utils.toast
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlinx.android.synthetic.main.activity_to_do.*
import kotlinx.android.synthetic.main.toolbar_collapsing.*


class ToDoActivity : AppCompatActivity(),ToDoRepository.OnRepositoryListener {

    companion object{
        const val TAG = "ToDoActivity"
    }
    private lateinit var viewModel: ViewModelToDo
    private lateinit var mAdapter:AdapterToDoList
    private lateinit var callback: SwipeToDeleteTaskCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)

        init()
    }

    override fun onRestart() {
        viewModel.getData()
        super.onRestart()
    }

    private fun init() {
        //ViewModel
        viewModel = ViewModelProvider(this).get(ViewModelToDo::class.java)
        viewModel.getData()
        viewModel.setRepositoryListener(this)
        //Adapter
        mAdapter = AdapterToDoList(this,viewModel)
        var itemDivider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
//        itemDivider.setDrawable(resources.getDrawable(R.drawable.item_divider))
        recycler_view_to_do.addItemDecoration(itemDivider)
        recycler_view_to_do.layoutManager = LinearLayoutManager(this)
        recycler_view_to_do.adapter = mAdapter
        callback = SwipeToDeleteTaskCallback(this,mAdapter)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recycler_view_to_do)
        viewModel.setOnItemDeleteListener(callback)

        observe()

        button_add_to_do.setOnClickListener{
            startActivity(Intent(this,
                AddToDoActivity::class.java))
        }

        //toolbar
        setToolbar()

    }

    private fun setToolbar() {
        toolbar_collapsing_expanded.setExpandedTitleColor(Color.WHITE)
        toolbar_collapsing_expanded.setCollapsedTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar_collapsed)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        var upArrow = resources.getDrawable(R.drawable.ic_baseline_arrow_back_ios_24)
        var upArrow = resources.getDrawable(R.drawable.ic_baseline_arrow_back_ios_24)
        upArrow.setColorFilter(resources.getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        supportActionBar!!.setHomeAsUpIndicator(upArrow)

        toolbar_collapsing_appbar
            .addOnOffsetChangedListener(object : OnOffsetChangedListener {
                var isShow = false
                var scrollRange = -1
                override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {

                    Log.v(TAG,"Measured width${toolbar_collapsing_expanded.measuredWidth}")
                    Log.v(TAG,"Offset Changed:${((scrollRange + verticalOffset).toFloat()/500).toFloat()} ")
                    text_view_task_count.alpha = ((scrollRange + verticalOffset).toFloat()/500)

                    if (scrollRange == -1) {
                        Log.v(TAG,scrollRange.toString())
                        scrollRange = appBarLayout.totalScrollRange
                    }
                    if (scrollRange + verticalOffset == 0) {
                        toolbar_collapsing_expanded.title = "To-do list"
//                        toolbar_collapsing_expanded.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
                        Log.v(TAG,toolbar_collapsing_expanded.title.toString())
                        isShow = true
                    } else if (isShow) {
                        toolbar_collapsing_expanded.title = "All"
                        toolbar_collapsing_expanded.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD)
                        Log.v(TAG,toolbar_collapsing_expanded.title.toString())
                        isShow = false
                    }
                }
            })
    }

    private fun observe() {
        viewModel.getObservableTasks().observe(this, Observer {
            if(it != null){
                Log.v(TAG,"${it}")
                mAdapter.setData(it)
                callback.setData(it)
                text_view_task_count.text = "${it.size} Tasks"
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_todo,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home ->{finish()}
            R.id.menu_more ->{}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onUploadSuccess() {
    }

    override fun onDeleteSuccess() {
        toast("Delete Success")
    }

    override fun onUploadFailure() {
    }

    override fun onUpdateSuccess() {
        toast("Update Success")
    }
}