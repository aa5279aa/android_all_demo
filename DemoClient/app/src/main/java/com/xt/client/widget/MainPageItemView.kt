package com.xt.client.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.xt.client.R


//class Main(context: Context) : LinearLayout(context)
class MainPageItemView : LinearLayout {

    private var titleText: TextView
    private var valueText: TextView


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MainPageItemView)
        val title = a.getString(R.styleable.MainPageItemView_item_title)
        val value = a.getString(R.styleable.MainPageItemView_item_value)
        a.recycle()
        setPadding(10, 10, 10, 10)
        background = resources.getDrawable(R.drawable.item_bg)
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER

        titleText = TextView(context)
        valueText = TextView(context)
        titleText.text = title
        valueText.text = value


        var lp = LinearLayout.LayoutParams(-2, -2);
        lp.gravity = Gravity.CENTER;

        addView(titleText, lp)
        addView(valueText, lp)
    }


    fun getTitle(): String {
        return titleText.text.toString()
    }

    fun getValue(): String {
        return titleText.text.toString()
    }

}
