package com.xt.appplugin;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.xt.appplugin.util.IOHelper;

import java.io.InputStream;

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
        webView.setBackgroundColor(Color.BLUE);
        try {
            String s = IOHelper.fromIputStreamToString(getAssets().open("a.html"));
            webView.getSettings().setJavaScriptEnabled(true);
//            webView.loadUrl("https://blog.csdn.net/rzleilei/article/details/125657645?spm=1001.2014.3001.5501");
            webView.loadData(s, "text/html", "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        InputStream open = ;
//        webView.loadUrl("https://blog.csdn.net/rzleilei/article/details/125657645?spm=1001.2014.3001.5501");

    }
}