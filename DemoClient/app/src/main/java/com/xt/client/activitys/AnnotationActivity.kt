package com.xt.client.activitys

import android.os.Bundle
import android.util.Log
import android.view.View
import com.xt.client.R
import com.xt.client.model.CountModel
import com.xt.client.util.AnnotateUtils

class AnnotationActivity : BaseActivity() {
    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var model = CountModel()
        model.num1 = 2
        model.num2 = 2
        model.num3 = 2
        model.num4 = 2
        model.str1 = "11"
        model.str2 = "22"
        model.str3 = "33"
        model.str4 = "44"


        //根据注解，去改变属性值
        AnnotateUtils.toString(model)

        Log.i("lxltest", "model.num1:" + model.num1)
        Log.i("lxltest", "model.num2:" + model.num2)
        Log.i("lxltest", "model.num3:" + model.num3)
        Log.i("lxltest", "model.num4:" + model.num4)
        Log.i("lxltest", "model.str1:" + model.str1)
        Log.i("lxltest", "model.str2:" + model.str2)
        Log.i("lxltest", "model.str3:" + model.str3)
        Log.i("lxltest", "model.str4:" + model.str4)

    }


}
