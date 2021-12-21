package com.xt.client.activitys

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.xt.client.util.ReflectUtil

/**
 * 捕获并显示上一个activity的界面
 */
class SaveLastActivity : BaseActivity() {

    var img: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewHolder.button1?.text = "显示上一个actvity的界面"
        img = ImageView(this)
        viewHolder.resultContainer?.visibility = View.VISIBLE
        viewHolder.resultContainer?.addView(img)
        Log.i("lxltest", "SaveLastActivity:onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.i("lxltest", "SaveLastActivity:onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("lxltest", "SaveLastActivity:onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.i("lxltest", "SaveLastActivity:onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("lxltest", "SaveLastActivity:onDestroy")
    }

    override fun onClick(v: View?) {
        val global = ReflectUtil.getPrivateField(windowManager, "mGlobal")

        val mViews = ReflectUtil.getPrivateField(global, "mViews")
        val mRoots = ReflectUtil.getPrivateField(global, "mRoots")

        (mViews as? List<*>).let {
            if (it!!.size > 1) {
                val view = it.get(0)
                (view as? View).let {
                    it?.setDrawingCacheEnabled(true)
                    it?.buildDrawingCache()  //启用DrawingCache并创建位图
                    val bitmap =
                        it?.getDrawingCache()?.let { it1 -> Bitmap.createBitmap(it1) } //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
                    it?.setDrawingCacheEnabled(false)
                    img?.setImageBitmap(bitmap)
                }
            }
        }
    }

}