package com.example.project2.ui.main.todo

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.project2.models.Task
import com.example.project2.utils.SwipeToDeleteTaskCallback
import com.google.android.material.textfield.TextInputEditText

class ViewModelToDo:ViewModel(),SwipeToDeleteTaskCallback.OnItemDeleteListener {

    companion object{
        const val TAG ="ViewModelToDo"
    }

    val taskTitle = ObservableField<String>()
    val taskNote = ObservableField<String>()
    val taskDate =  ObservableField<String>()
    val taskTime = ObservableField<String>()

    private val repository = ToDoRepository()

    fun addTask(task: Task){
        Log.v(TAG,"addTask")
        repository.addTaskToDatabase(task)
    }

    fun getData(){
        repository.getTasksFromDatabase()
    }

    fun getObservableTasks() = repository.getTasks()

    fun setRepositoryListener(onRepositoryListener: ToDoRepository.OnRepositoryListener){
        repository.setListener(onRepositoryListener)
    }

    fun setOnItemDeleteListener(swipeToDeleteCallback: SwipeToDeleteTaskCallback){
        swipeToDeleteCallback.setOnItemDeleteListener(this)
    }

    fun taskStatusChanged(task: Task){
        task.isCompleted = !task.isCompleted
        repository.updateTaskStatus(task)
        Log.v(TAG,"taskStatusChanged: ${task}")
    }

    fun setObservableDateAndTimeField(){
        Log.v(TAG,"setSelectDate")
        taskDate.set("Select a Date")
        taskTime.set("Pick a time")
    }
    fun createTask(titleInput:TextInputEditText) {
        Log.v(TAG,"createTask")
        if (taskTitle.get().isNullOrEmpty()) {
            titleInput.error = "Cannot be blank"
            titleInput.requestFocus()
        } else {
            var title = taskTitle.get()
            var note: String? = taskNote.get()
            var status = false
            var id = null

            val dateTime = "${taskDate.get()} ${taskTime.get()}"
            Log.v(TAG,dateTime)

           addTask(Task(id, status, note, title!!, dateTime))
        }
    }

    override fun deleteItem(task: Task) {
        repository.deleteTask(task)
    }
}