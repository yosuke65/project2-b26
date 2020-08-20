package com.example.project2.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar

class CustomToolbar:Toolbar {

    constructor(context: Context):super(context)
    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet)
    constructor(context: Context,attributeSet: AttributeSet,defAttr:Int):super(context,attributeSet,defAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth+50,measuredHeight)
    }

}