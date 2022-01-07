package com.xt.client.activitys

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View

class ThreadRefreshActivity : Base2Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread {
            mResult.visibility = View.GONE
        }.start()
    }

    override fun onResume() {
        super.onResume()
        Thread {
            mResult.visibility = View.GONE
        }.start()
    }

    override fun clickItem(position: Int) {
        if (position == 0) {
            Thread {
                //dialog显示
                Looper.prepare()
                Handler().post {
                    val dialog = Dialog(this)
                    dialog.setTitle("子线程刷新的内容-场景2")
                    dialog.show()
                }
                Looper.loop()
            }.start()
            return
        }
        if (position == 1) {
            Thread {
                mResult.text = "子线程刷新的内容-场景3"
            }.start()
            return
        }
    }

    override fun getShowData(): List<String> {
        return mutableListOf<String>().apply {
            //子线程刷新UI
            this.add("子线程刷新UI-场景2")
            this.add("子线程刷新UI-场景3")
        }
    }

}
