package com.xt.client.activitys

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.xt.client.R
import com.xt.client.widget.viewholder.ViewHolder

abstract class BaseActivity : FragmentActivity(), View.OnClickListener {

    lateinit var viewHolder: ViewHolder
    var hanlder = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflate = View.inflate(this, R.layout.base_layout, null)
        setContentView(inflate)
        viewHolder = ViewHolder(inflate, this)
    }


    abstract override fun onClick(v: View?)

}