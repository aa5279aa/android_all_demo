package com.xt.client;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.xt.client.fragment.base.BaseMultiFragment;
import com.xt.client.function.dynamic.manager.DynamicResourceManager;

import java.util.List;


public class HostActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("lxltest", "");
        setContentView(R.layout.content_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class MyViewModel extends ViewModel {
        public final LiveData<List<String>> StringsList;

        public MyViewModel() {
            StringsList = new LiveData<List<String>>() {
                @Override
                public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<String>> observer) {
                    super.observe(owner, observer);
                }

                @Override
                protected void setValue(List<String> value) {
                    super.setValue(value);
                }
            };
        }

        class StringViewHolder extends RecyclerView.ViewHolder {

            public StringViewHolder(View itemView) {
                super(itemView);
            }
        }

        class StringAdapter extends ListAdapter<String, StringViewHolder> {
            public StringAdapter() {
                super(DIFF_CALLBACK);
            }

            @Override
            public StringViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(StringViewHolder holder, int position) {
//            holder.bindTo(getItem(position));
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

    private void test(){

    }
}