package com.xt.appplugin;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Plugin3Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        setContentView(R.layout.activity_main);
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        TextView textView = new TextView(getApplicationContext());
        String string = getResources().getString(R.string.plugin_str1);
        textView.setText(string);
        linearLayout.addView(textView);
        setContentView(linearLayout);
    }
}