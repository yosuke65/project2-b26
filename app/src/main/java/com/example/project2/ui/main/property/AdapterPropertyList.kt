package com.example.project2.ui.main.property

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.databinding.RowPropertyListBinding
import com.example.project2.models.Property

class AdapterPropertyList(private val mContext:Context) : RecyclerView.Adapter<AdapterPropertyList.MyViewHolder>() {

    private var mList = ArrayList<Property>()

    inner class MyViewHolder(private var binding: RowPropertyListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(property: Property) {
            binding.item = property
            binding.adapter = this@AdapterPropertyList
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding =
            RowPropertyListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        var layoutParams = RecyclerView.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        binding.root.layoutParams = layoutParams
        return MyViewHolder((binding))
    }

    fun setData(list: ArrayList<Property>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    fun onItemClicked(item:Property){
        Toast.makeText(mContext, "${item._id}", Toast.LENGTH_SHORT).show()
    }

    fun removeItem(position: Int) {
        mList.removeAt(position)
        notifyDataSetChanged()
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,mList.size)
    }
}