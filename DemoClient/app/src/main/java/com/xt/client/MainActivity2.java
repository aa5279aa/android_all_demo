package com.xt.client;

import android.os.Bundle;
import android.view.View;

import com.common.ui.view.progress.TouchProgressView;

import androidx.fragment.app.FragmentActivity;

/**
 * @author xiatian
 * @date 2019/6/11
 */

public class MainActivity2 extends FragmentActivity {

    TouchProgressView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page2);
        view = findViewById(R.id.progress_bar);
        initData();
    }

    private void initData() {

    }


}
