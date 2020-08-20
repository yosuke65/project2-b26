package com.example.project2.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.R
import com.example.project2.models.Property
import com.example.project2.ui.main.property.AdapterPropertyList
import com.example.project2.ui.main.property.property.PropertyViewModel

class SwipeToDeletePropertyCallback(private val context: Context, private val mAdapter:AdapterPropertyList, private val viewModel:PropertyViewModel): ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {

    companion object{
        private const val TAG = "SwipeToDeleteProperty"
    }
    private val icon = ContextCompat.getDrawable(context,
        R.drawable.ic_baseline_delete_242);
    private val background = ColorDrawable(Color.rgb(255,95,95))
    private var mList = ArrayList<Property>()

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        Log.d(TAG, "onSwiped: Position: $position")
        Log.d(TAG, "onSwiped: Item: ${mList[position]}")
        viewModel.deleteProperty(mList[position])
        mAdapter.removeItem(position)
        Toast.makeText(context, "onSwiped: ${position}", Toast.LENGTH_SHORT).show()
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
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        if(dX < 0){
            //setBounds for background
            val rightCornerOffset = 20
            val leftCornerOffset = 50
            val topOffset = 40
            val bottomOffset = 10
            val bgLeft = (dX + viewHolder.itemView.right + rightCornerOffset - leftCornerOffset).toInt()
            val bgRight = viewHolder.itemView.right + rightCornerOffset
            val bgTop = viewHolder.itemView.top + topOffset
            val bgBottom = viewHolder.itemView.bottom - bottomOffset
            background.setBounds(bgLeft,bgTop,bgRight,bgBottom)

            //setBounds for icon
            val iconTopOffset = 10
            val iconMarginRight = 70
            val iconMargin = (viewHolder.itemView.height - icon!!.intrinsicHeight)/2
            val iconLeft = bgRight - icon!!.intrinsicWidth -iconMarginRight
            val iconRight = bgRight - iconMarginRight
            val iconTop = bgTop + iconMargin - iconTopOffset
            val iconBottom = iconTop + icon.intrinsicHeight
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom)
        }
        else{
            background.setBounds(0,0,0,0)
        }

        background.draw(c)
        icon!!.draw(c)
    }

    fun setData(list:ArrayList<Property>) {
        mList = list
        Log.d(TAG, "setData: Size:${mList.size}")
    }
}