package com.xt.appplugin;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Plugin1Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        TextView textView = new TextView(getApplicationContext());
        textView.setText("这是插件类Plugin1Activity");
        linearLayout.addView(textView);
        setContentView(linearLayout);
    }
}