package com.example.project2.ui.main.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project2.R
import com.example.project2.models.Task
import com.example.project2.utils.toast
import kotlinx.android.synthetic.main.activity_to_do.*

class ToDoActivity : AppCompatActivity(),ToDoRepositories.OnRepositoryListener {

    companion object{
        const val TAG = "ToDoActivity"
    }
    private lateinit var viewModel: ViewModelToDo
    private lateinit var mAdapter:AdapterToDoList
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
        //Adapter
        mAdapter = AdapterToDoList(this,viewModel)
        recycler_view_to_do.layoutManager = LinearLayoutManager(this)
        recycler_view_to_do.adapter = mAdapter
        viewModel.getData()
        observe()
        button_add_to_do.setOnClickListener{
            startActivity(Intent(this,
                AddToDoActivity::class.java))
        }
        viewModel.setRepositoryListener(this)
    }

    private fun observe() {
        viewModel.getObservableTasks().observe(this, Observer {
            toast("getObservableTasks")
            if(it != null){
                Log.v(TAG,"${it}")
                mAdapter.setData(it)
            }
        })
    }

    override fun onUploadSuccess() {
    }

    override fun onUploadFailure() {
    }

    override fun onUpdateSuccess() {
        toast("Update Success")
    }
}