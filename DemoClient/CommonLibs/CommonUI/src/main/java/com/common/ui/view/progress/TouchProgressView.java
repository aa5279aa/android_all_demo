package com.common.ui.view.progress;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.common.ui.R;


public class TouchProgressView extends View {
    private static final String TAG = "TouchProgressView";

    private Paint linePaint;
    private Paint pointPaint;

    private int pointRadius = 10;//圆点默认半径,单位px
    private int pointColor = R.color.color_5b6775;//圆点默认颜色

    private int lineHeight = 2;//线默认高度,单位px
    private int lineClor = R.color.color_2870F6;//线默认颜色

    private int progress = 0;
    private final int PROGRESS_MIN = 0;
    private final int PROGRESS_MAX = 100;

    private OnProgressChangedListener progressChangedListener;

    public interface OnProgressChangedListener {
        void onProgressChanged(View view, int progress);
    }

    public TouchProgressView(Context context) {
        super(context, null);
    }

    public TouchProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置圆点半径
     *
     * @param radius
     */
    public void setPointRadius(final int radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("radius 不可以小于等于0");
        }

        if (getWidth() == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (radius * 2 > getWidth()) {
                        throw new IllegalArgumentException("radius*2 必须小于 view.getWidth() == " + getWidth());
                    }
                    pointRadius = radius;
                }
            });
        } else {
            if (radius * 2 > getWidth()) {
                throw new IllegalArgumentException("radius*2 必须小于 view.getWidth() == " + getWidth());
            }
            this.pointRadius = radius;
        }
    }

    /**
     * 设置圆点颜色
     *
     * @param color
     */
    public void setPointColor(int color) {
        this.pointColor = color;
    }

    /**
     * 设置直线高度
     *
     * @param height
     */
    public void setLineHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("height 不可以小于等于0");
        }

        this.lineHeight = height;
    }

    /**
     * 设置直线颜色
     *
     * @param color
     */
    public void setLineColor(int color) {
        this.lineClor = color;
    }

    /**
     * 设置百分比
     *
     * @param progress
     */
    public void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("progress 不可以小于0 或大于100");
        }
        this.progress = progress;
        invalidate();

        if (progressChangedListener != null) {
            progressChangedListener.onProgressChanged(this, progress);
        }
    }

    /**
     * 设置进度变化监听器
     *
     * @param onProgressChangedListener
     */
    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.progressChangedListener = onProgressChangedListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getX() < pointRadius) {
            setProgress(PROGRESS_MIN);
            return true;
        } else if (event.getX() > getWidth() - pointRadius) {
            setProgress(PROGRESS_MAX);
            return true;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setProgress(calculProgress(event.getX()));
                    return true;
                case MotionEvent.ACTION_MOVE:
                    setProgress(calculProgress(event.getX()));
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d(TAG, "[draw] .. in .. ");
        super.draw(canvas);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(lineHeight);
        linePaint.setColor(getResources().getColor(lineClor));

        //因为是以画布Canvas 为draw对象，所以RectF构造函数内的参数是以canvas为边界，而不是屏幕
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, linePaint);

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(getResources().getColor(pointColor));
        canvas.drawCircle(getCx(), getHeight() / 2, pointRadius, pointPaint);
    }

    /**
     * 获取圆点的x轴坐标
     *
     * @return
     */
    private float getCx() {
        float cx = 0.0f;
        cx = (getWidth() - pointRadius * 2);
        if (cx < 0) {
            throw new IllegalArgumentException("TouchProgressView 宽度不可以小于 2 倍 pointRadius");
        }
        return cx / 100 * progress + pointRadius;
    }

    /**
     * 计算触摸点的百分比
     *
     * @param eventX
     * @return
     */
    private int calculProgress(float eventX) {
        float proResult = (eventX - pointRadius) / (getWidth() - pointRadius * 2);
        return (int) (proResult * 100);
    }
}