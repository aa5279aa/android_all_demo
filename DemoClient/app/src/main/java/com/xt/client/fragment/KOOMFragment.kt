package com.xt.client.fragment

import android.os.Bundle
import android.view.View

class KOOMFragment : Base2Fragment() {

    var TAG = "KOOMFragment"

    companion object {
        var byteArray: ByteArray? = null;
    }

    override fun getShowData(): List<String> {
        return mutableListOf<String>().apply {
            this.add("进行内存泄漏")//0
            this.add("1")//1
            this.add("2")//2
            this.add("3")//3
            this.add("4")//4
            this.add("5")//5
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun clickItem(position: Int) {
        if (position == 0) {
            byteArray = ByteArray(1024 * 1024)
        }



    }

}