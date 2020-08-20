package com.example.project2.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.R
import com.example.project2.models.Task
import com.example.project2.ui.main.todo.AdapterToDoList

class SwipeToDeleteTaskCallback(private val context: Context, private val mAdapter:AdapterToDoList):ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {


    companion object{
        private const val TAG = "SwipeToDeleteCallback"
    }
    private val icon = ContextCompat.getDrawable(context,
    R.drawable.ic_baseline_delete_24);
    private val background = ColorDrawable(Color.rgb(255,95,95))
    private var listener:OnItemDeleteListener? = null
    private lateinit var mList:ArrayList<Task>


    interface OnItemDeleteListener{
        fun deleteItem(task: Task)
    }

    fun setData(list:ArrayList<Task>){
        mList = list
    }

    fun setOnItemDeleteListener(onItemDeleteListener: OnItemDeleteListener){
        listener = onItemDeleteListener
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener!!.deleteItem(mList!![viewHolder.adapterPosition])
        mAdapter.remove(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        Log.d(TAG, "onChildDraw: dx = $dX")
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val backgroundCornerOffset:Int = 20


        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop =
            itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        if (dX < 0) { // Swiping to the left
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top, itemView.right, itemView.bottom
            )
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0)
        }

        background.draw(c)
        icon.draw(c)
    }
}