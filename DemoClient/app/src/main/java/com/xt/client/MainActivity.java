package com.xt.client;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xt.client.activitys.JNIActivity;
import com.xt.client.activitys.PerformanceActivity;
import com.xt.client.activitys.PrepareActivity;
import com.xt.client.activitys.SaveLastActivity;
import com.xt.client.activitys.ShowActivity;
import com.xt.client.activitys.WCDBActivity;
import com.xt.client.fragment.AidlFragment;
import com.xt.client.fragment.BaseFragment;
import com.xt.client.fragment.DynamicFragment;
import com.xt.client.fragment.ProtobuffFragment;
import com.xt.client.activitys.ThreadRefreshActivity;
import com.xt.client.fragment.TryCrashFragment;
import com.xt.client.inter.RecyclerItemClickListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author xiatian
 * @date 2019/6/11
 */

public class MainActivity extends FragmentActivity {

    static final String TAG = "lxltest";
    FragmentManager manager;
    RecyclerView mRecycler;
    List<ItemState> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        mRecycler = findViewById(R.id.recycler);
        manager = getSupportFragmentManager();
        initData();
    }

    private void initData() {
        dataList.add(new ItemState(getString(R.string.test), "ing"));
        dataList.add(new ItemState(getString(R.string.protobuff), "done"));
        dataList.add(new ItemState(getString(R.string.instrumentation), "no start"));
        dataList.add(new ItemState(getString(R.string.jni_use), "done"));
        dataList.add(new ItemState(getString(R.string.performance_check), "done"));
        dataList.add(new ItemState(getString(R.string.catch_crash), "done"));
        dataList.add(new ItemState(getString(R.string.get_last_activity), "done"));
        dataList.add(new ItemState(getString(R.string.flutter), "notstart"));
        dataList.add(new ItemState(getString(R.string.use_aidl), "done"));
        dataList.add(new ItemState(getString(R.string.prepareloadview), "ing"));
        dataList.add(new ItemState(getString(R.string.wcdb), "ing"));
        dataList.add(new ItemState(getString(R.string.threadrefresh), "done"));
        dataList.add(new ItemState(getString(R.string.dynamicload), "ing"));


        GridLayoutManager layout = new GridLayoutManager(this, 2);
        mRecycler.setLayoutManager(layout);
        MyAdapter adapter = new MyAdapter();
        mRecycler.setAdapter(adapter);
        mRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.left = 10;
                outRect.bottom = 10;
                if (parent.getChildLayoutPosition(view) % 2 == 0) {
                    outRect.left = 0;
                }
            }
        });
        adapter.notifyDataSetChanged();
        mRecycler.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ItemState itemState = dataList.get(position);
                doAction(itemState.name);
            }
        }));
    }

    private void doAction(String title) {
        if (getString(R.string.test).equalsIgnoreCase(title)) {
            Intent intent = new Intent();
            intent.setClass(this, ShowActivity.class);
            startActivity(intent);
            return;
        }

        if (getString(R.string.test_crash).equalsIgnoreCase(title)) {
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentationM
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            String simSer = tm.getSimSerialNumber();
            int simState = tm.getSimState();
            int dataNetworkType = tm.getDataNetworkType();
            Log.i("lxltest", "simSer:" + simSer + ",simState:" + simState + ",dataNetworkType:" + dataNetworkType);
            return;
        }
        Fragment fragment = null;
        if (getString(R.string.protobuff).equalsIgnoreCase(title)) {
            fragment = new ProtobuffFragment();
        } else if (getString(R.string.instrumentation).equalsIgnoreCase(title)) {
            fragment = new ProtobuffFragment();
        } else if (getString(R.string.catch_crash).equalsIgnoreCase(title)) {
            fragment = new TryCrashFragment();
        } else if (getString(R.string.use_aidl).equalsIgnoreCase(title)) {
            fragment = new AidlFragment();
        } else if (getString(R.string.breakpoint_download).equalsIgnoreCase(title)) {
//            fragment = new DownLoadFragment();
        } else if (getString(R.string.dynamicload).equalsIgnoreCase(title)) {
            fragment = new DynamicFragment();
        }

        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(BaseFragment.TITLE, title);
            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(android.R.id.content, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
            return;
        }

        Intent intent = new Intent();
        if (getString(R.string.get_last_activity).equalsIgnoreCase(title)) {
            intent.setClass(MainActivity.this, SaveLastActivity.class);
        } else if (getString(R.string.jni_use).equalsIgnoreCase(title)) {
            intent.setClass(MainActivity.this, JNIActivity.class);
        } else if (getString(R.string.performance_check).equalsIgnoreCase(title)) {
            intent.setClass(MainActivity.this, PerformanceActivity.class);
        } else if (getString(R.string.prepareloadview).equalsIgnoreCase(title)) {
            intent.setClass(MainActivity.this, PrepareActivity.class);
        } else if (getString(R.string.wcdb).equalsIgnoreCase(title)) {
            intent.setClass(MainActivity.this, WCDBActivity.class);
        } else if (getString(R.string.threadrefresh).equalsIgnoreCase(title)) {
            intent.setClass(MainActivity.this, TestActivity.class);
        } else {
            return;
        }
        startActivity(intent);
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = View.inflate(MainActivity.this, R.layout.vh_item, null);
            return new RecyclerView.ViewHolder(inflate) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ItemState itemState = dataList.get(position);
            ((TextView) holder.itemView.findViewById(R.id.text_name)).setText(itemState.name);
            ((TextView) holder.itemView.findViewById(R.id.text_state)).setText(itemState.state);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    static class ItemState {
        String name = "";
        String state = "";

        ItemState(String name, String state) {
            this.name = name;
            this.state = state;
        }
    }
}
