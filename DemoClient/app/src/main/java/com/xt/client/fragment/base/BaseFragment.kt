package com.xt.client.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xt.client.R
import com.xt.client.widget.viewholder.ViewHolder

abstract class BaseFragment : Fragment(), View.OnClickListener {

    companion object {
        const val TITLE = "title"
    }

    var viewHolder: ViewHolder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.base_layout, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewHolder = ViewHolder(view, this)
        arguments?.getString(TITLE).let {
            viewHolder?.descText?.text = it
        }
    }
}