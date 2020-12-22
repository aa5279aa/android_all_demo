package com.xt.client.activitys

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class LocationActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewHolder.button1?.text = "修改定位"
    }

    fun show(msg: String) {

    }

    override fun onClick(v: View?) {

    }

}