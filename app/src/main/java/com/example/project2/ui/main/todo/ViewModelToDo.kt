package com.example.project2.ui.main.todo

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.project2.models.Task
import com.example.project2.utils.toast

class ViewModelToDo:ViewModel() {

    companion object{
        const val TAG ="ViewModelToDo"
    }
    private val repository = ToDoRepositories()

    fun addTask(task: Task){
        repository.addTaskToDatabase(task)
    }

    fun getData(){
        repository.getTasksFromDatabase()
    }

    fun getObservableTasks() = repository.getTasks()

    fun setRepositoryListener(onRepositoryListener: ToDoRepositories.OnRepositoryListener){
        repository.setListener(onRepositoryListener)
    }

    fun taskStatusChanged(task: Task){
        task.isCompleted = !task.isCompleted
        repository.updateTaskStatus(task)
        Log.v(TAG,"taskStatusChanged: ${task}")
    }
}