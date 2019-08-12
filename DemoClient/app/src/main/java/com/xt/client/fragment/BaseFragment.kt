package com.xt.client.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xt.client.R
import com.xt.client.viewholder.ViewHolder

abstract class BaseFragment : Fragment(), View.OnClickListener {

    var viewHolder: ViewHolder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.base_layout, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewHolder = ViewHolder(view, this)
    }


}