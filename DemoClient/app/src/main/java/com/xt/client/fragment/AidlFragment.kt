package com.xt.client.fragment

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.view.View
import com.xt.client.R
import com.xt.client.aidl.IClientCallBack
import com.xt.client.aidl.ProcessAidlInter
import com.xt.client.service.Other2ProcessService

class AidlFragment : BaseFragment() {

    internal var processAidlInter: ProcessAidlInter? = null
    internal var clientCallBack: IClientCallBack? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewHolder?.line2?.visibility = View.VISIBLE
        viewHolder?.button1?.text = "启动service进程"
        viewHolder?.button2?.text = "显示进程名"
        viewHolder?.button3?.text = "显示进程计数次数"
        viewHolder?.button4?.text = "关闭service进程"
    }

    override fun onClick(v: View?) {
        val id = v?.id
        if (id == R.id.button1) {

            val intent = Intent()
            intent.setClass(context!!, Other2ProcessService::class.java)
            context?.bindService(intent, object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName, service: IBinder) {
                    processAidlInter = ProcessAidlInter.Stub.asInterface(service)
                    try {
                        processAidlInter?.registerCallBack(object : IClientCallBack {

                            override fun asBinder(): IBinder? {
                                return service
                            }

                            @Throws(RemoteException::class)
                            override fun callback(time: Int) {
                                Log.i("lxltest", "time:" + time)
                            }
                        })
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }

                override fun onServiceDisconnected(name: ComponentName) {

                }
            }, Service.BIND_AUTO_CREATE)
            return
        }
        if (processAidlInter == null) {
            return
        }
        if (id == R.id.button2) {
            viewHolder?.resultText?.text = String.format("进程名:%s", processAidlInter!!.processName)
        } else if (id == R.id.button3) {
            viewHolder?.resultText?.text = String.format("进程计数:%s", processAidlInter!!.getProcessTime(0))
        } else if (id == R.id.button4) {
            val serviceRunning = processAidlInter?.setServiceRunning(false)
            viewHolder?.resultText?.text = (if (serviceRunning!!) "进程开启中" else "进程已关闭")
        }

    }

}