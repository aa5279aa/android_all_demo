package com.xt.client.fragment.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xt.client.R
import com.xt.client.viewholder.ViewHolder
import com.xt.client.widget.tool.decoration.MyItemDecoration

abstract class BaseMultiFragment : BaseFragment(), ItemClickListener {

    companion object {
        const val TITLE = "title"

        val DIFF_CALLBACK: DiffUtil.ItemCallback<String> =
            object : DiffUtil.ItemCallback<String>() {
                override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun onClick(v: View?) {

    }

    private lateinit var multiViewHolder: MultiViewHolder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.base_multi_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        multiViewHolder = MultiViewHolder(view, this)
        arguments?.getString(TITLE).let {
            multiViewHolder.descText?.text = it
        }
        val stringAdapter = StringAdapter(this)
        multiViewHolder.rv.adapter = stringAdapter
        multiViewHolder.rv.layoutManager = GridLayoutManager(view.context, 2)
        multiViewHolder.rv.addItemDecoration(MyItemDecoration())
        val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        viewModel.stringList.observe(
            viewLifecycleOwner
        ) { list: List<String> ->
            stringAdapter.submitList(
                list
            )
        }
        stringAdapter.submitList(getButtonList())
    }

    abstract fun getButtonList(): List<String>

    @SuppressLint("FragmentLiveDataObserve")
    class MultiViewHolder(view: View, listener: View.OnClickListener) :
        ViewHolder(view, listener) {
        val rv: RecyclerView

        init {
            rv = view.findViewById(R.id.rv)
        }
    }

    class MyViewModel : ViewModel() {
        val stringList: LiveData<List<String>>

        init {
            stringList = object : LiveData<List<String>>() {}
        }
    }


    class StringViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {


        fun bindTo(item: String?) {
            text.text = item
        }

        var text: TextView

        init {
            text = view.findViewById(R.id.text)
        }
    }


    class StringAdapter(private var itemClick: ItemClickListener) :
        ListAdapter<String, StringViewHolder>(DIFF_CALLBACK),
        View.OnClickListener {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
            val inflate = LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_java_main_item, parent, false)
            return StringViewHolder(inflate);
        }

        override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
            holder.bindTo(getItem(position))
            holder.text.tag = position
            holder.text.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            itemClick.onItemClick(v?.tag as Int);
        }

    }


}