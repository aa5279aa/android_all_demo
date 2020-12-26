package com.common.ui.view.bar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;

import com.common.ui.R;
import com.common.util.DeviceUtil;


/**
 * Switch简单实现,只提供图片和状态切换，不提供文字显示
 *
 * @author lxl
 */
public class CommonSimpleSwitch extends CompoundButton {

    private static final int SWIPE_MIN_DISTANCE = 8;
    private static final int SWIPE_MAX_OFF_PATH = 240;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;
    private GestureDetector gestureDetector;
    private OnTouchListener gestureListener;

    public CommonSimpleSwitch(Context context) {
        this(context, null);
    }

    public CommonSimpleSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);

        setChecked(isChecked());

        setEnabled(true);
        setClickable(true);
        setFocusable(true);
        Drawable drawable = getResources().getDrawable(R.drawable.original_common_ico_switch_selector);
        setDrawable(drawable, DeviceUtil.getPixelFromDip(getContext(), 44), DeviceUtil.getPixelFromDip(getContext(), 24));

        // Gesture detection
        gestureDetector = new GestureDetector(context, new CommonSimpleSwitchGestureDetector());
        gestureListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        setOnTouchListener(gestureListener);
    }


    public void setDrawable(Drawable drawable, int width, int height) {
        if (drawable != null) {
            drawable.setBounds(0, 0, width, height);
            setCompoundDrawables(drawable, null, null, null);
        }
    }


    private class CommonSimpleSwitchGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }

        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }

        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
                setChecked(false);
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
                setChecked(true);
                return true;
            }
            return false;
        }
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        // invalidate();
    }

}
