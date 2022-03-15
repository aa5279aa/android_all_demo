package com.xt.client.activitys;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.FrameMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xt.client.R;
import com.xt.client.performance.ANRMonitor;
import com.xt.client.util.IOHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 性能优化案例
 */
public class PerformanceCaseActivity extends Activity {


    RecyclerView recyclerView;
    TextView title;
    ModelDataAdapter adapter;
    boolean flag = true;
    Handler handler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ANRMonitor.init(this);
        setContentView(R.layout.activity_recycler);
        bindView();
        //3.2 示例代码
        initData();

        //3.3 示例代码
        new Thread(() -> {
            while (flag) {
                List<Map<String, String>> data = getResponse();
                notifyData(data);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //优化代码
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 15);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = false;
    }

    private void notifyData(List<Map<String, String>> data) {
        handler.post(() -> {
            adapter.data = data;
            adapter.notifyDataSetChanged();
        });
    }

    private List<Map<String, String>> getResponse() {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("name", "股票编号" + i);
            map.put("value", "价格：" + Math.random() * 10);
            list.add(map);
        }
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData() {
        //3.2案例 原始有问题代码
        try {
            InputStream is = getAssets().open("content.txt");
            List<String> strings = IOHelper.readListStrByCode(is, "utf-8");
            String s = strings.get(0);
            title.setText(s);
        } catch (IOException e) {
            Log.i("lxltest", e.getMessage());
            e.printStackTrace();
        }

        //3.2案例 优化代码
        new Thread(() -> {
            String s = "";
            try {
                InputStream is = getAssets().open("content.txt");
                List<String> strings = IOHelper.readListStrByCode(is, "utf-8");
                s = strings.get(0);
            } catch (IOException e) {
                Log.i("lxltest", e.getMessage());
                e.printStackTrace();
            }
            String show = s;
            handler.post(() -> title.setText(show));
        }).start();
    }

    private void bindView() {
        recyclerView = findViewById(R.id.recycler_view);
        title = findViewById(R.id.title);
        adapter = new ModelDataAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }


    static class ModelDataAdapter extends RecyclerView.Adapter<InnerViewHolder> {
        List<Map<String, String>> data = new ArrayList<>();


        @Override
        public InnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_recycler_item, parent, false);
            return new InnerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {
            Map<String, String> map = data.get(position);
            ((TextView) holder.itemView.findViewById(R.id.id)).setText(String.valueOf(position));
            ((TextView) holder.itemView.findViewById(R.id.name)).setText(map.get("name"));
            ((TextView) holder.itemView.findViewById(R.id.value)).setText(map.get("value"));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    static class InnerViewHolder extends RecyclerView.ViewHolder {

        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    //优化前代码
    private List<String> show() {
        List<Model> list = new ArrayList<>();//长度10W
        List<String> input = new ArrayList<>();//长度1000

        List<String> showList = new ArrayList<>();
        for (String key : input) {
            for (Model model : list) {
                if (model.name.equals(key)) {
                    showList.add(model.value);
                }
            }
        }
        return showList.subList(0, 100);
    }

    //优化后代码
    private List<String> show2() {
        List<Model> list = new ArrayList<>();//长度10W
        List<String> input = new ArrayList<>();//长度1000

        Map<String, Model> cache = new HashMap<>();
        for (Model model : list) {
            cache.put(model.name, model);
        }
        List<String> showList = new ArrayList<>();
        for (int i = 0; i < Math.min(input.size(), 100); i++) {
            Model model = cache.get(input.get(i));
            showList.add(model.value);
        }
        return showList.subList(0, 100);
    }

    static class Model {
        String name = "";
        String value = "";
    }
}
