package com.xt.client.service

import android.service.quicksettings.TileService
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import android.util.Log
import com.xt.client.MainActivity

class QuickSettingService : TileService() {

    val TAG = "lxltest"
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "TileService onCreate ++++++++++++++")
    }


    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    /**
     * TODO： 按钮开启/关闭操作
     */
    override fun onClick() {
        super.onClick()
        Log.i(TAG, "TileService onClick ++++++++++++++")
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivityAndCollapse(intent)
        // 更新 Tile 状态
//        val open = MMKV.defaultMMKV().decodeBool("open", false)
//        qsTile.state = if (open) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
//        qsTile.updateTile()
//        MMKV.defaultMMKV().encode("open", !open)
//
//        qsTile.icon = Icon.createWithResource(this, R.drawable.ic_baseline_architecture_24)
//        qsTile.label = "Studio"
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//            qsTile.subtitle = "This is a TileService"
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    /**
     * 下拉菜单时被调用，Tile 没有从 Editor 栏拖到设置栏中，则不会调用
     *  onTileAdded() 时会被调用一次
     */
    override fun onStartListening() {
        super.onStartListening()
    }

    /**
     * 同上 同理
     */
    override fun onStopListening() {
        super.onStopListening()
    }

    /**
     * 用户将 Tile 从 Edit 中添加到设定栏中
     */
    override fun onTileAdded() {
        super.onTileAdded()
        Log.i(TAG, "TileService onTileAdded ++++++++++++++")
    }

    /**
     * 将 Tile 移除设定栏
     */
    override fun onTileRemoved() {
        super.onTileRemoved()
        Log.i(TAG, "TileService onTileRemoved ++++++++++++++")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }
}