package com.xt.client.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.xt.client.R;
import com.xt.client.widget.tool.decoration.MyItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 夏天
 * @date 2022/10/30
 */

public class TestJavaActivity extends AppCompatActivity implements View.OnClickListener {

    public static interface ItemClickListener {
        void onItemClick(int postion);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_main);
        MyViewModel viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new MyItemDecoration());
        StringAdapter adapter = new StringAdapter();
        viewModel.stringList.observe(this, list -> adapter.submitList(list));
        recyclerView.setAdapter(adapter);
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        adapter.submitList(list);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<String> list2 = new ArrayList<>();
                list2.add("A");
                list2.add("B");
                list2.add("D");
                adapter.submitList(list2);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }

    public static class MyViewModel extends ViewModel {
        public final LiveData<List<String>> stringList;

        public MyViewModel() {
            stringList = new LiveData<List<String>>() {
            };
        }
    }

    static class StringViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public StringViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }

        public void bindTo(String item) {
            text.setText(item);
        }
    }

    class StringAdapter extends ListAdapter<String, StringViewHolder> {
        public StringAdapter() {
            super(DIFF_CALLBACK);
        }

        @Override
        public StringViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_java_main_item, parent, false);
            return new StringViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(StringViewHolder holder, int position) {
            holder.bindTo(getItem(position));
        }

    }

    public final DiffUtil.ItemCallback<
            String> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<String>() {
                @Override

                public boolean areItemsTheSame(
                        String oldString, String newString) {
                    // String properties may have changed if reloaded from the DB, but ID is fixed
                    return oldString == newString;
                }


                @Override
                public boolean areContentsTheSame(
                        String oldString, String newString) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldString.equals(newString);
                }
            };
}

