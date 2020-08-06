package com.example.project2.ui.main.todo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.databinding.RowToDoListBinding
import com.example.project2.models.Task

class AdapterToDoList(var mContext: Context, var viewModel:ViewModelToDo):RecyclerView.Adapter<AdapterToDoList.MyViewHolder>() {
    companion object{
        const val TAG = "AdapterToDoList"
    }

    private var mList = ArrayList<Task>()

    inner class MyViewHolder(private var binding:RowToDoListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.item = task
            binding.executePendingBindings()
            binding.viewModel = viewModel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = RowToDoListBinding.inflate(LayoutInflater.from(parent.context))
        var layoutParams = RecyclerView.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
        binding.root.layoutParams = layoutParams
        return MyViewHolder(binding)
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    fun setData(tasks: ArrayList<Task>) {
        mList = tasks
        notifyDataSetChanged()
    }
}