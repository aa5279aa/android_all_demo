package com.xt.client.activitys

import com.xt.client.jni.CalculationJNITest
import com.xt.client.jni.DynamicRegister
import com.xt.client.jni.Java2CJNI
import com.xt.client.model.JavaModel
import com.xt.client.util.IOHelper
import com.xt.client.util.LogUtil
import com.xt.client.viewmodel.DemoRequest
import java.io.File

//改成recyclerView，一行两个。底部输出值
class JNIActivity : Base2Activity() {

    override fun getShowData(): List<String> {
        return mutableListOf<String>().apply {
            this.add("JNI基础调用")//0 课程1等等
            this.add("JNI多类型字符串拼接")//1
            this.add("JNI更改属性值")//2
            this.add("JNI动态注册")//3
            this.add("JNI读取文件")//6
            this.add("JNI线程通知安卓刷新")//position=5 第七章
            this.add("JNI加密解密")//第八章
            this.add("JNI查找父类名")//第九章
        }
    }

    override fun clickItem(position: Int) {
        if (position == 0) {
            var java2CJNI = Java2CJNI()
            val result = java2CJNI.java2C()
            mResult.text = result
            return
        }
        if (position == 1) {
            var calculationJNITest = CalculationJNITest()
            val inputStreamFromUrl = IOHelper.fromStringToIputStream("6")
            val input2byte = IOHelper.input2byte(inputStreamFromUrl)
            try {
                val calculationSum =
                    calculationJNITest.calculationSum(1, 2, "3", 4.0, "5".toCharArray(), input2byte)
                mResult.text = calculationSum
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }
        if (position == 2) {
            try {
                var calculationJNITest = CalculationJNITest()
                val javaModel = JavaModel()
                javaModel.age = 10
                javaModel.moblie = "17863333333"
                javaModel.name = "lxl"
                val calculationSum = calculationJNITest.updateObjectValue(javaModel)
                mResult.text = javaModel.moblie
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }
        if (position == 3) {
            //动态注册
            var dynamicRegister = DynamicRegister()
            val result = dynamicRegister.spliceString("Hello","World")
            mResult.text = "$result"
            return
        }
        var dynamicRegister = DynamicRegister()
        if (position == 4) {
            val file = File(filesDir.absolutePath + File.separator + "a.txt")
            IOHelper.writerStrByCodeToFile(file, "utf-8", false, "hello world")
            val readStrByPath = dynamicRegister.readStrByPath(file.absolutePath)
            mResult.text = "$readStrByPath"
            return
        }
        if (position == 5) {
            //C层启动一个线程，主动通知java层的刷新界面。
            dynamicRegister.refresh("ttA", this)
            return
        }
        if (position == 6) {

            /**
             * 字符串加密，输出加密后的字符串。
             * 简单把字符串的assic码*2-1，生成一串int数组
             */
            val encryption = dynamicRegister.encryption("12345abcde")
            LogUtil.logI(encryption)
            mResult.text = encryption
            /**
             * 字符串解密，输出解密后的字符串
             */
            val decrypt = dynamicRegister.decrypt(encryption)
            LogUtil.logI(decrypt)
            return
        }
        if (position == 7) {
            val superClassName = dynamicRegister.getSuperClassName("com/xt/client/viewmodel/DemoRequest")
            (superClassName as? Class)?.let {
                LogUtil.logI(it.name)
            }

            return
        }
    }


    /**
     * 供native方法调用通知安卓刷新
     * 这里是子线程刷新UI哦，并且可以正常运行不会报错，原因可以参考TextView源码中checkForRelayout方法
     */
    fun showMessage(str: String) {
        LogUtil.logI("showMessage:$str")
        mResult.text = str
    }
}