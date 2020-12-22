package com.xt.client;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

public class BlueToothActivity extends Activity {
    public String TAG = "BlueTooth";
    private LinearLayoutManager linearLayoutManager;
    private BluetoothAdapter mBluetoothAdapter;
    private IntentFilter mFilter;
    private BluetoothDevice mBluetoothDevice;//搜索出来的

    /**
     * 需要连接的蓝牙设备
     */
    private BluetoothDevice bluetoothDevice; //选择的设备
    private SharedPreferences sharedPreferences;
    private int clickSelect;//选择的蓝牙设备在列表中的位置
    private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();//搜索出的设备集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leftmenu_btreceiver);
        initPairingInfo();
//        Class[] classes = {Integer.TYPE};
//        Method createRfcommSocket = BluetoothDevice.class.getMethod("createRfcommSocket", new Class[]{Integer.TYPE});
    }

    /**
     * 填充已配对蓝牙设备信息
     */
    private void initPairingInfo() {
    }

    /**
     * 检测是否有蓝牙权限和模糊定位权限
     */
    private void methodRequiresTwoPermission() {
//        String[] perms = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION};
//        if (EasyPermissions.hasPermissions(this, perms)) {
//            inspect();
//        } else {
//            EasyPermissions.requestPermissions(this, getString(R.string.permissions_hint), 0, perms);
//        }
    }

    /**
     * 蓝牙是否开启，未开启则提示用户，开启则弹出选择框
     */
    private void inspect() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            /**注册搜索蓝牙receiver*/
            mFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            mFilter.addAction(BluetoothDevice.ACTION_FOUND);
            mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            mFilter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
            bluetoothDeviceList.clear();
            registerReceiver(mReceiver, mFilter);
            mBluetoothAdapter.startDiscovery();
            scanDialog();
        } else {
            Toast.makeText(this, "未开启蓝牙", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 点击扫描弹出框
     */
    private void scanDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LinearLayout scanDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.leftmenu_btreceiver_popup, null);
//        builder.setView(scanDialog);
//        builder.setCancelable(false);
//        final AlertDialog dialog = builder.create();
//        dialog.show();
//
//        Button cancel = scanDialog.findViewById(R.id.leftmenu_btreceiver_popup_cancel);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mBluetoothAdapter.cancelDiscovery();
//                dialog.cancel();
//            }
//        });
//        RecyclerView deviceRecycler = scanDialog.findViewById(R.id.leftmenu_btreceiver_popup_device);
//        linearLayoutManager = new LinearLayoutManager(this);
//        deviceRecycler.setLayoutManager(linearLayoutManager);
//        deviceRecycler.setHasFixedSize(true);
//        btReceiverAdapter = new BTReceiverAdapter(bluetoothDeviceList);
//        deviceRecycler.setAdapter(btReceiverAdapter);
//        btReceiverAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Log.d(TAG, "onItemClick: " + position);
//                clickSelect = position;
//                mBluetoothAdapter.cancelDiscovery();
//                dialog.cancel();
//                pairing();
//            }
//        });
    }

    /**
     * 进行检测配对操作
     */
    private void pairing() {
        //判断是否已经配对
        if (bluetoothDeviceList.get(clickSelect).getBondState() == BluetoothDevice.BOND_BONDED) {
            // 保存
            Log.d(TAG, "已配对： " + bluetoothDeviceList.get(clickSelect).getName());
//            Toast.makeText(BTReceiverActivity.this, R.string.BTReceiverActivity_pairing_succeed, Toast.LENGTH_SHORT).show();
//            SharedPreferences.Editor edt = sharedPreferences.edit();
//            edt.putString(UserInfoConstant.USERINFO_BLUETOOTHNAME, bluetoothDeviceList.get(clickSelect).getName());
//            edt.putString(UserInfoConstant.USERINFO_BLUETOOTHID, bluetoothDeviceList.get(clickSelect).getAddress());
//            edt.apply();
            initPairingInfo();
        } else {
            // 进行配对
            try {
                BTReceiverUtils.createBond(bluetoothDeviceList.get(clickSelect).getClass(), bluetoothDeviceList.get(clickSelect));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理广播
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            /** 搜索到的蓝牙设备*/
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                mBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothDeviceList.add(mBluetoothDevice);
                Log.d("mReceiver", "search......" + mBluetoothDevice.getName());
            } else if (action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
                /** 配对 */
                Log.d(TAG, "getDeviceName； " + bluetoothDeviceList.get(clickSelect).getName());
                bluetoothDevice = bluetoothDeviceList.get(clickSelect);
                Log.d(TAG, "bluetoothDevice.getName : " + bluetoothDevice.getName());
                try {
                    //终止有序广播
                    mReceiver.abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
                    //调用setPin方法进行配对...
                    boolean ret = BTReceiverUtils.setPin(bluetoothDevice.getClass(), bluetoothDevice, "setPin");
                    //确认配对
                    BTReceiverUtils.setPairingConfirmation(bluetoothDevice.getClass(), bluetoothDevice, true);
                    //取消用户输入
                    BTReceiverUtils.cancelPairingUserInput(bluetoothDevice.getClass(), bluetoothDevice);
                    Log.d("pairing", "ret: " + ret + "  name" + bluetoothDevice.getName() + " address " + bluetoothDevice.getAddress());
                    if (ret) {
                        saveBluetoothDevice();
                    } else {
                        Toast.makeText(BlueToothActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                switch (mBluetoothDevice.getBondState()) {
                    case BluetoothDevice.BOND_BONDING://正在配对
                        Log.d(TAG, "正在配对......");
                        break;
                    case BluetoothDevice.BOND_BONDED://配对结束
                        Log.d(TAG, "完成配对");
                        saveBluetoothDevice();
                        break;
                    case BluetoothDevice.BOND_NONE://取消配对/未配对
                        if (bluetoothDevice.getName().contains("Bluetooth Rec")){
                            saveBluetoothDevice();
                        }
                    default:
                        break;
                }
            }
        }
    };

    /**
     * 保存配对
     */
    private void saveBluetoothDevice(){
        Toast.makeText(BlueToothActivity.this, "success", Toast.LENGTH_SHORT).show();
        initPairingInfo();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
    }

}
