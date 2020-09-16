package com.xt.client.activitys;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xt.client.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowActivity extends Activity {

    RecyclerView mRecyclerView;
    List<Item2InnerBean> mList = new ArrayList<>();

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomDensity(this, getApplication());
        Log.i("lxltest", "ShowActivity:");
        setContentView(R.layout.activity_show);
        mRecyclerView = findViewById(R.id.recycler);


        initData();

        Model2InnerAdapter adapter = new Model2InnerAdapter();
        adapter.setList(mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.i("lxltest", "time:" + mList.size());
    }

    private void initData() {
        for (int i = 0; i < 400; i++) {
            Item2InnerBean bean = new Item2InnerBean();
            bean.name = "名称" + i;
            mList.add(bean);
        }
    }


    static class Model2InnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<Item2InnerBean> list;

        public void setList(List<Item2InnerBean> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_model_2_inner, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Item2InnerBean item2InnerBean = list.get(position);
            TextView textView = holder.itemView.findViewById(R.id.tv_name);
            textView.setText(item2InnerBean.name);
        }

        @Override
        public int getItemCount() {
            Log.i("lxltest", "time:" + list.size());
            return list.size();
        }
    }


    static class Item2InnerBean {
        String name = "";
    }

    public static void setCustomDensity(Activity activity, Application application) {
        //application
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        int max = Math.max(appDisplayMetrics.widthPixels, appDisplayMetrics.heightPixels);
        //计算宽为360dp 同理可以设置高为640dp的根据实际情况
        final float targetDensity = Math.round((double) max / 640);
        final int targetDensityDpi = (int) (targetDensity * 160);

        appDisplayMetrics.density = appDisplayMetrics.scaledDensity = targetDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        //activity
        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();

        activityDisplayMetrics.density = appDisplayMetrics.scaledDensity = targetDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

}
