package com.xt.client

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.xt.client.activitys.*
import com.xt.client.application.DemoApplication
import com.xt.client.fragment.*
import com.xt.client.fragment.base.BaseFragment
import com.xt.client.inter.RecyclerItemClickListener
import com.xt.client.util.ToastUtil
import com.xt.client.widget.tool.decoration.MyItemDecoration
import com.xt.router_api.BindSelfView
import java.io.File
import java.io.IOException
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * @author xiatian
 * @date 2019/6/11
 */
@BindSelfView(1)
class MainActivity : FragmentActivity() {
    var manager: FragmentManager? = null
    var mRecycler: RecyclerView? = null
    var dataList: MutableList<ItemState> = ArrayList()
    var `object` = Any()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(R.layout.main_page)
        mRecycler = findViewById(R.id.recycler)
        manager = supportFragmentManager
        initData()
        val baseContext = baseContext
        val applicationContext = applicationContext
        val application = application
        Log.i("lxltest", "$baseContext,$application")
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.i("lxltest", "MainActivity_onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("lxltest", "MainActivity_onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("lxltest", "MainActivity_onDestroy")
        System.exit(0)
    }

    private fun initData() {
        dataList.add(ItemState(getString(R.string.test_button), "ing", null))
        dataList.add(ItemState(getString(R.string.test_activity), "done", TestActivity::class.java))
        dataList.add(
            ItemState(
                getString(R.string.test_java_Activity),
                "done",
                TestJavaActivity::class.java
            )
        )
        dataList.add(
            ItemState(
                getString(R.string.protobuff),
                "done",
                ProtobuffFragment::class.java
            )
        )
        dataList.add(ItemState(getString(R.string.instrumentation), "no start", null))
        dataList.add(ItemState(getString(R.string.jni_use), "done", JNIActivity::class.java))
        dataList.add(
            ItemState(
                getString(R.string.performance_check),
                "done",
                PerformanceActivity::class.java
            )
        )
        dataList.add(
            ItemState(
                getString(R.string.catch_crash),
                "done",
                TryCrashFragment::class.java
            )
        )
        dataList.add(
            ItemState(
                getString(R.string.get_last_activity),
                "done",
                SaveLastActivity::class.java
            )
        )
        dataList.add(ItemState(getString(R.string.flutter), "notstart", null))
        dataList.add(ItemState(getString(R.string.use_aidl), "done", AidlFragment::class.java))
        dataList.add(
            ItemState(
                getString(R.string.prepareloadview),
                "ing",
                PrepareActivity::class.java
            )
        )
        dataList.add(ItemState(getString(R.string.wcdb), "ing", WCDBActivity::class.java))
        dataList.add(
            ItemState(
                getString(R.string.threadrefresh),
                "done",
                ThreadRefreshActivity::class.java
            )
        )
        dataList.add(ItemState(getString(R.string.dynamicload), "ing", DynamicFragment::class.java))
        dataList.add(ItemState(getString(R.string.permission), "done", null))
        dataList.add(
            ItemState(
                getString(R.string.performance_optimization),
                "done",
                PerformanceCaseActivity::class.java
            )
        )
        dataList.add(ItemState(getString(R.string.retrofit), "done", RetrofitFragment::class.java))
        dataList.add(ItemState(getString(R.string.mmkv), "done", MMKVFragment::class.java))
        dataList.add(ItemState(getString(R.string.kotlin), "ing", KotlinFragment::class.java))
        dataList.add(ItemState(getString(R.string.koom), "ing", KOOMFragment::class.java))
        dataList.add(ItemState(getString(R.string.router), "done", RouteFragment::class.java))
        dataList.add(ItemState(getString(R.string.jvmti), "done", JVMTIFragment::class.java))


        val layout = GridLayoutManager(this, 2)
        mRecycler!!.layoutManager = layout
        val adapter = MyAdapter()
        mRecycler!!.adapter = adapter
        mRecycler!!.addItemDecoration(MyItemDecoration())
        adapter.notifyDataSetChanged()
        mRecycler!!.addOnItemTouchListener(RecyclerItemClickListener(this) { view, position ->
            val itemState = dataList[position]
            if (itemState.c == null) {
                doActionWithoutClass(itemState.name)
            } else {
                doActionWithClass(itemState)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.i("lxltest", "onLowMemory")
    }

    var list: List<ByteArray> = ArrayList()
    private fun doActionWithoutClass(title: String) {
        if (getString(R.string.test_button).equals(title, ignoreCase = true)) {
//            val intent = Intent("my_self",Uri.fromFile( File("")))
//            sendBroadcast(intent)

            val intentFilter = IntentFilter("my_self")
//            intentFilter.priority = 3
//            registerReceiver(object : BroadcastReceiver() {
//                override fun onReceive(context: Context?, intent: Intent?) {
//                    Log.i("lxltest", "TestActivity_onReceive1_start")
//                    Thread.sleep(1_000)
//                    Log.i("lxltest", "TestActivity_onReceive1_end")
//                }
//            }, intentFilter)

//            Handler().postDelayed(Runnable {
//                Thread.sleep(70_000)
//            }, 5000)


            val intentFilter2 = IntentFilter("my_self")
            intentFilter2.priority = 5
            registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    Log.i("lxltest", "TestActivity_onReceive2_start")
                    Thread.sleep(10_000)
                    Log.i("lxltest", "TestActivity_onReceive2_end")
                }
            }, intentFilter2)
//            Handler().postDelayed({
//                Log.i("lxltest", "sleep 50000")
//                Thread.sleep(80_000)
//            }, 5000)

            return
        }
        if (getString(R.string.test_crash).equals(title, ignoreCase = true)) {
            val str: String? = null
            println(str!!.length)
            return
        }
        if (getString(R.string.permission).equals(title, ignoreCase = true)) {
            managerPermission()
            return
        }
    }

    private fun doActionWithClass(itemState: ItemState) {
        val c = itemState.c
        val name = c!!.name
        val title = itemState.name
        if (name.endsWith("Activity")) {
            val intent = Intent()
            intent.setClass(this@MainActivity, c)
            startActivity(intent)
            return
        }
        if (name.endsWith("Fragment")) {
            try {
                val fragment = c.newInstance() as Fragment
                val bundle = Bundle()
                bundle.putString(BaseFragment.TITLE, title)
                fragment.arguments = bundle
                val fragmentTransaction = manager!!.beginTransaction()
                fragmentTransaction.add(android.R.id.content, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commitAllowingStateLoss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }
        doActionWithoutClass(title)
    }

    internal inner class MyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflate = View.inflate(this@MainActivity, R.layout.vh_item, null)
            return object : RecyclerView.ViewHolder(inflate) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val itemState = dataList[position]
            (holder.itemView.findViewById<View>(R.id.text_name) as TextView).text =
                itemState.name
            (holder.itemView.findViewById<View>(R.id.text_state) as TextView).text = itemState.state
        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }

    private fun managerPermission() {
        val sdkInt = Build.VERSION.SDK_INT
        //判断版本
        //6.0以下
        try {
            if (sdkInt < 23) {
                writeFile()
                return
            }
            //6.0到10.0以下
            if (sdkInt < 29) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                    return
                }
                writeFile()
                return
            }
            //10.0
            if (sdkInt < 30) {
                //同上，另外额外设置requestLegacyExternalStorage=true
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                    return
                }
                writeFile()
                return
            }
            //11.0-12.0
            if (sdkInt >= 30) {
                if (!Environment.isExternalStorageManager()) {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivity(intent)
                    return
                }
                writeFile()
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun writeFile() {
        val absolutePath = Environment.getExternalStorageDirectory().absolutePath
        val file = File(absolutePath + File.separator + "a.txt")
        if (file.exists()) {
            file.delete()
            ToastUtil.showCenterToast("文件存在，删除成功")
        } else {
            file.createNewFile()
            ToastUtil.showCenterToast("文件不存在，创建成功")
        }
    }

    class ItemState(name: String, state: String, c: Class<*>?) {
        var name = ""
        var state = ""
        var c: Class<*>?

        //        ItemState(String name, String state) {
        //            this.name = name;
        //            this.state = state;
        //        }
        init {
            this.name = name
            this.state = state
            this.c = c
        }
    }

    private lateinit var mContext: Context;
    var mShowLogoutManager: WindowManager? = null
    fun dotest() {
        mContext = DemoApplication.getInstance()
        logoutBtn = LayoutInflater.from(mContext).inflate(R.layout.showcar_logout_btn, null)
        val touchView = logoutBtn.findViewById<View>(R.id.showcar_logout_container)
        mShowVideoList = logoutBtn.findViewById(R.id.bt_video)
        mShowLogout = logoutBtn.findViewById(R.id.showcar_logout)
        mShowVideoList.setOnClickListener(View.OnClickListener { ToastUtil.showCenterToast("图标") })
        mShowLogout.setOnClickListener(View.OnClickListener { ToastUtil.showCenterToast("退出") })
        mShowLogoutManager = mContext.getSystemService(WINDOW_SERVICE) as WindowManager
        mShowLogoutLayoutParams = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mShowLogoutLayoutParams!!.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }
        mShowLogoutLayoutParams!!.width = 332
        mShowLogoutLayoutParams!!.height = 72
        mShowLogoutLayoutParams!!.x = 30
        //        mShowLogoutLayoutParams.x = (int) ServiceApplication.getContext().getResources().getDimension(R.dimen.logout_btn_margin_left);
//        mShowLogoutLayoutParams.y = 15;
        mShowLogoutLayoutParams!!.y = 200
        mShowLogoutLayoutParams!!.flags =
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        //设置背景透明
//        mShowLogoutLayoutParams.format = PixelFormat.TRANSLUCENT;
        mShowLogoutLayoutParams!!.format = PixelFormat.RGBA_8888
        mShowLogoutLayoutParams!!.gravity = Gravity.END or Gravity.BOTTOM
        //        mShowVideoList.setVisibility(View.INVISIBLE);
        try {
            setViewTouchGrag(
                touchView,
                logoutBtn,
                mShowLogoutLayoutParams,
                mShowLogoutManager,
                true
            )
            mShowLogoutManager!!.addView(logoutBtn, mShowLogoutLayoutParams)
            //            logoutBtn.setAccessibilityPaneTitle(getContext().getResources().getString(R.string.logout_showcar_btn));
//            BeanAccessibilityManager.getInstance().registerListener(logoutBtn);
//            logoutBtnIsShowing = true;
        } catch (e: Exception) {
            Log.e(TAG, "showLogoutBtn: addview Error", e)
        }
        tt(MainActivity::class.java)
    }

    private fun tt(a: Class<out Activity?>) {}

    companion object {
        const val TAG = "lxltest"
        private var isMove = false
        private lateinit var logoutBtn: View
        private lateinit var mShowVideoList: ImageView
        private lateinit var mShowLogout: Button
        private var mShowLogoutLayoutParams: WindowManager.LayoutParams? = null
        private fun setViewTouchGrag(
            touchView: View,
            floatView: View?,
            layoutParams: WindowManager.LayoutParams?,
            windowManager: WindowManager?,
            isWelt: Boolean
        ) {
            touchView.isLongClickable = true
            val width = windowManager!!.defaultDisplay.width
            val height = windowManager.defaultDisplay.height
            //吸附效果OK了，但是沿吸附边移动未实现
            touchView.setOnTouchListener(object : OnTouchListener {
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    val rawX = event.rawX.toInt()
                    val rawY = event.rawY.toInt()
                    when (event.action) {
                        MotionEvent.ACTION_UP -> if (isMove && isWelt) {
                            val minValue = getMinValue(
                                rawX,
                                rawY,
                                width,
                                height,
                                floatView!!.width,
                                floatView.height
                            )
                            layoutParams!!.x = minValue[0]
                            layoutParams.y = minValue[1]
                            Log.i(TAG, "贴边，x=" + layoutParams.x + ",y=" + layoutParams.y)
                            updateXY()
                        }
                        MotionEvent.ACTION_DOWN ->                         //贴边显示
                            isMove = false
                        MotionEvent.ACTION_MOVE -> {
                            isMove = true
                            layoutParams!!.x = width - rawX
                            layoutParams.y = height - rawY - floatView!!.measuredHeight / 2
                            layoutParams.y =
                                Math.min(layoutParams.y, height - floatView.measuredHeight - 60)
                            layoutParams.x = Math.max(layoutParams.x, 30)
                            layoutParams.x =
                                Math.min(layoutParams.x, width - 30 - floatView.measuredWidth)
                            Log.i(
                                TAG,
                                "onTouch:ACTION_MOVE,x=" + layoutParams.x + ",y=" + layoutParams.y
                            )
                            updateXY()
                        }
                    }
                    return isMove
                }

                private fun updateXY() {
                    try {
                        Log.i(
                            TAG,
                            "updateViewLayout,x=" + layoutParams!!.x + ",y=" + layoutParams.y
                        )
                        windowManager.updateViewLayout(floatView, layoutParams)
                    } catch (e: Exception) {
                        Log.e(TAG, "onTouch: ")
                    }
                }
            })
        }

        private fun getMinValue(
            rawX: Int,
            rawY: Int,
            screenWidth: Int,
            screenHeight: Int,
            viewWidth: Int,
            viewHeight: Int
        ): IntArray {
            val x1 = screenWidth - rawX //靠右距离
            val x2 = rawX - viewWidth //靠左距离
            //        int y1 = height - rawY;
            val y2 = screenHeight - rawY
            //确定是贴近X还是Y
            val result = intArrayOf(rawX, rawY)
            val absX = Math.min(x1, x2)
            if (absX > y2) {
                //Y改0
//            if (y1 > y2) {
                result[1] = 30
                result[0] = screenWidth - result[0]
                //            } else {
//                result[1] = height;
//            }
            } else {
                //X改g
                if (x1 > x2) {
                    result[0] = screenWidth - 30 - viewWidth
                } else {
                    result[0] = 30
                }
                result[1] = screenHeight - result[1]
            }
            if (result[1] > screenHeight - 120) {
                result[1] = screenHeight - 120
            }
            return result
        }
    }
}