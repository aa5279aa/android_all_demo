package com.xt.appplugin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * plugin5为插件中正常使用的。
 */
public class Plugin6Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getDelegate().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_plugin6);
        WebView webView = findViewById(R.id.webview);
        webView.loadUrl("https://www.baidu.com");

    }
}