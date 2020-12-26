package com.common.ui.safe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


/**
 * 解决数据更新中滑动会导致崩溃的问题，虽然解决方式不优雅，但能解决崩溃问题。
 */
public class SafeRecyclerView extends RecyclerView {
    private boolean mIsRefreshing = false;

    public SafeRecyclerView(Context context) {
        super(context);
        init();
    }

    public SafeRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SafeRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (mIsRefreshing) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
        );
    }
}
