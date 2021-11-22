package com.xt.client.activitys

import android.os.Bundle
import android.view.View
import com.xt.client.R
import com.xt.client.jni.CalculationJNITest
import com.xt.client.jni.DynamicRegister
import com.xt.client.jni.Java2CJNI
import com.xt.client.model.JavaModel
import com.xt.client.util.IOHelper


class JNIActivity : BaseActivity() {

    var num1: Int = 0
    var num2: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewHolder.button1?.text = "JNI输出"
        viewHolder.button2?.text = "JNI加法"
        viewHolder.button3?.text = "JNI更改属性值"
        viewHolder.button4?.text = "动态注册"
        num1 = Math.random().toInt()
        num2 = Math.random().toInt()
        viewHolder.line2?.visibility = View.VISIBLE
    }


    override fun onClick(v: View?) {

        if (v?.id == R.id.button1) {
            var java2CJNI = Java2CJNI()
            val result = java2CJNI.java2C()
            viewHolder.resultText?.text = result
        } else if (v?.id == R.id.button2) {
            var calculationJNITest = CalculationJNITest()
            val inputStreamFromUrl = IOHelper.fromStringToIputStream("6")
            val input2byte = IOHelper.input2byte(inputStreamFromUrl)
            try {
                val calculationSum =
                    calculationJNITest.calculationSum(1, 2, "3", 4.0, "5".toCharArray(), input2byte)
                viewHolder.resultText?.text = calculationSum
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if (v?.id == R.id.button3) {
            try {
                var calculationJNITest = CalculationJNITest()
                val javaModel = JavaModel()
                javaModel.age = 10
                javaModel.moblie = "17863333330"
                javaModel.name = "lxl"
                val calculationSum = calculationJNITest.updateObjectValue(javaModel)
                viewHolder.resultText?.text = javaModel.moblie
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (v?.id == R.id.button4) {
            //动态注册
            var dynamicRegister = DynamicRegister()
            val result = dynamicRegister.encryptionStr("aaa")
            val result2 = DynamicRegister.staticencryptionStr("bbb")
            viewHolder.resultText?.text = "$result,$result2"

        }

    }

}