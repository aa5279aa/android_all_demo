package com.xt.client.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Main extends LinearLayout {
    TextView text;

    public Main(Context context) {
        super(context);

        text = new TextView(context);
    }

}
