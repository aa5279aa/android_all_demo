package com.xt.client.activitys

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.xt.client.R
import com.xt.client.viewholder.ViewHolder

abstract class BaseActivity : Activity(), View.OnClickListener {

    lateinit var viewHolder: ViewHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflate = View.inflate(this, R.layout.base_layout, null)
        setContentView(inflate)
        viewHolder = ViewHolder(inflate,this)
    }


    abstract override fun onClick(v: View?)

}