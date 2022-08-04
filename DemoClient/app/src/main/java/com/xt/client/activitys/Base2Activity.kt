package com.xt.client.activitys

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xt.client.R

abstract class Base2Activity : FragmentActivity() {

    var hanlder = Handler()
    lateinit var mContent: RecyclerView;
    lateinit var mResult: TextView;
    private var mAdapter = GridAdapter()
    private var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        val inflate = View.inflate(this, R.layout.jni_layout, null)
        setContentView(inflate)
        mContent = findViewById(R.id.content)
        mResult = findViewById(R.id.result_text)

        mContent.layoutManager =
            GridLayoutManager(baseContext, 2, GridLayoutManager.VERTICAL, false)
        mContent.adapter = mAdapter
        mAdapter.dataList = getShowData()
    }


    abstract fun clickItem(position: Int)

    abstract fun getShowData(): List<String>


    internal inner class GridAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var dataList: List<String>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflate = View.inflate(baseContext, R.layout.base2_item, null)
            return object : RecyclerView.ViewHolder(inflate) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.findViewById<TextView>(R.id.text).text = dataList?.get(position)
            holder.itemView.findViewById<TextView>(R.id.text).setOnClickListener {
                clickItem(position)
            }
        }

        override fun getItemCount(): Int {
            return dataList?.size ?: 0
        }


    }

}