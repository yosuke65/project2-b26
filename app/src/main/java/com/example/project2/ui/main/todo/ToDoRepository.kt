package com.example.project2.ui.main.todo

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.project2.models.Task
import com.example.project2.utils.PreferenceManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class ToDoRepository {

    companion object {
        const val TAG = "ToDoRepositories"
    }

    private var tasks = MutableLiveData<ArrayList<Task>>()
    private val ref = FirebaseDatabase.getInstance().reference.child(
        PreferenceManager.getPreference(
            PreferenceManager.ID,
            ""
        )
    ).child("Task")

    private var listener: OnRepositoryListener? = null


    interface OnRepositoryListener {
        fun onUploadSuccess()
        fun onDeleteSuccess()
        fun onUploadFailure()
        fun onUpdateSuccess()
    }

    fun setListener(onRepositoryListener: OnRepositoryListener) {
        Log.v(TAG, "setListener")
        listener = onRepositoryListener
    }

    fun getTasks() = tasks

    fun addTaskToDatabase(task: Task) {
        val key = ref.push().key
        task.id = key
        var uploadTask = ref.child(key.toString()).setValue(task)
            .addOnSuccessListener {
                Log.v(TAG, "success")
                listener!!.onUploadSuccess()
            }
            .addOnFailureListener {
                Log.v(TAG, "failed")
                listener!!.onUploadFailure()
            }


    }

    fun getTasksFromDatabase() {
        var list = ArrayList<Task>()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val task = data.getValue(Task::class.java)
                    Log.v(TAG, "${task.toString()}")
                    list.add(task!!)
//                    Log.v(TAG,"${list.toString()}")
                }
                tasks.postValue(list)

            }

        })
    }

    fun updateTaskStatus(task: Task) {
        var ref = ref.orderByChild("id").equalTo(task.id)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        child.ref.setValue(task)
                        listener!!.onUpdateSuccess()
                    }
                }
            })
    }

    fun deleteTask(task: Task) {
        val ref = ref.orderByChild("id").equalTo(task.id)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        child.ref.removeValue()
                        listener!!.onDeleteSuccess()
                    }
                }
            })
    }
}
