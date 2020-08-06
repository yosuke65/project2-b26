package com.example.project2.ui.main.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.project2.R
import com.example.project2.models.Task
import com.example.project2.utils.toast
import kotlinx.android.synthetic.main.activity_add_to_do.*

class AddToDoActivity : AppCompatActivity(),ToDoRepositories.OnRepositoryListener {
    private lateinit var viewModel: ViewModelToDo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_do)

        init()
    }

    //Use DataBinding for only one button view?

    private fun init() {
        viewModel = ViewModelProvider(this).get(ViewModelToDo::class.java)
        button_create_task.setOnClickListener{
            createTask()
        }
        viewModel.setRepositoryListener(this)
    }

    private fun createTask(){
        if(edit_text_title.text.isNullOrEmpty()){
            edit_text_title.error = "Add title"
            edit_text_title.requestFocus()
        }else{
            var title = edit_text_title.text.toString()
            var note:String? = edit_text_note?.text.toString()
            var status = false
            var id = null
            viewModel.addTask(Task(id,status,note, title))
        }
    }

    override fun onUploadSuccess() {
        toast("Upload Success")
        finish()
    }

    override fun onUploadFailure() {
        toast("Upload failed")
        finish()
    }

    override fun onUpdateSuccess() {
    }
}