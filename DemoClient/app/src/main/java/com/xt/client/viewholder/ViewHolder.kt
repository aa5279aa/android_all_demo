package com.xt.client.viewholder

import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.xt.client.R


class ViewHolder(view: View, listener: View.OnClickListener) {

    var descText: TextView? = null
    var line1: View? = null
    var line2: View? = null
    var line3: View? = null
    var button1: Button? = null
    var button2: Button? = null
    var button3: Button? = null
    var button4: Button? = null
    var button5: Button? = null
    var button6: Button? = null
    var resultText: TextView? = null
    var resultContainer: ViewGroup? = null

    var listener: OnClickListener? = null

    init {

        line1 = view.findViewById(R.id.line1)
        line2 = view.findViewById(R.id.line2)
        line3 = view.findViewById(R.id.line3)
        button1 = view.findViewById<Button>(R.id.button1)
        button2 = view.findViewById<Button>(R.id.button2)
        button3 = view.findViewById<Button>(R.id.button3)
        button4 = view.findViewById<Button>(R.id.button4)
        button5 = view.findViewById<Button>(R.id.button5)
        button6 = view.findViewById<Button>(R.id.button6)

        descText = view.findViewById<TextView>(R.id.desc_text)
        resultText = view.findViewById<TextView>(R.id.result_text)

        resultContainer = view.findViewById(R.id.result_container)

        button1?.setOnClickListener(listener)
        button2?.setOnClickListener(listener)
        button3?.setOnClickListener(listener)
        button4?.setOnClickListener(listener)
        button5?.setOnClickListener(listener)
        button6?.setOnClickListener(listener)
    }


}