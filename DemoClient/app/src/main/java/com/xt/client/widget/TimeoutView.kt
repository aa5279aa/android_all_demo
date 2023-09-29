package com.xt.client.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

class TimeoutView(context: Context?, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatTextView(
        context!!, attrs
    ) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Thread.sleep(20_000)
    }
}