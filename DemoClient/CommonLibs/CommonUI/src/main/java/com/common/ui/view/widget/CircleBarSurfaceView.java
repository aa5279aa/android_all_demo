package com.common.ui.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.text.DecimalFormat;

/**
 * 简易 SurfaceView 进度条 圆环和中心文字 背景透明
 * 不使用自定义 xml 属性配置
 */
public class CircleBarSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    public CircleBarSurfaceView(Context context) {
        super(context);
        init();
    }

    public CircleBarSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleBarSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        paint = new Paint();
        getHolder().addCallback(this);
        startTime = System.currentTimeMillis();
    }

    Thread drawThread;
    private int width;
    private int height;
    boolean stopDraw;
    boolean pauseDraw;
    private Paint paint;
    float strokeWidth = 12f;
    int startAngle = 270;
    int sweepAngle = 0;
    long maxTime = 99 * 1000L;
    long startTime;
    boolean isFirst = false;

    int circleColor = 0xfff5f5f5;
    int progressColor = 0xff44b4ff;
    int textColor = 0xffffffff;

    String text = "0";
    float textSize = 14f * 1.0f;

    public void setProgress(int progress) {
        this.sweepAngle += progress;
    }

    public void setStopDraw(boolean stopDraw) {
        this.stopDraw = stopDraw;
    }

    public void setPauseDraw(boolean pauseDraw) {
        this.pauseDraw = pauseDraw;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
        startTime = System.currentTimeMillis();
    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.width = getWidth();
        this.height = getHeight();
        stopDraw = false;
        pauseDraw = true;
        drawThread = new Thread(this::drawTask);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDraw = true;
        try {
            drawThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Runnable onTimeListener;

    public void setOnTimeListener(Runnable onTimeListener) {
        this.onTimeListener = onTimeListener;
    }

    public void progressCalc() {
        long currentTimeMillis = System.currentTimeMillis();
        long duration = currentTimeMillis - startTime;
        double v = (double) duration / 1000;
        text = new DecimalFormat("00.00").format(v);
        sweepAngle += 4;
        if (currentTimeMillis - maxTime >= startTime) {
            pauseDraw = true;
            double v2 = (double) maxTime / 1000;
            text = new DecimalFormat("00.00").format(v2);
            if (onTimeListener != null) {
                onTimeListener.run();
            }
        }
    }

    public void reset() {
        startTime = System.currentTimeMillis();
        isFirst = false;
        pauseDraw = false;
        text = "00.00";
        sweepAngle = 0;
    }

    public void pause() {
        startTime = System.currentTimeMillis();
        isFirst = false;
        pauseDraw = true;
        text = "00.00";
        sweepAngle = 0;
    }

    private void drawTask() {
//        firstView();
        while (!stopDraw) {
            if (pauseDraw) {
                if (isFirst) {
                    sleep(5);
                    continue;
                }
                isFirst = true;
                sweepAngle = 360;
            }
            Canvas canvas = getHolder().lockCanvas();
            if (canvas == null) {
                sleep(5);
                continue;
            }
            // 结合init 配置 使背景透明
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            // 画笔配置
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(strokeWidth);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(circleColor);
            // 背景圆环
            canvas.drawCircle(width / 2, height / 2, (width >> 1) - strokeWidth / 2, paint);

            // 进度条圆环
            paint.setColor(progressColor);
            canvas.drawArc(new RectF(0 + strokeWidth / 2, 0 + strokeWidth / 2, width - strokeWidth / 2, height - strokeWidth / 2), startAngle,
                    sweepAngle, false, paint);

            progressCalc();
            // 360度后复原
            if (sweepAngle >= 360) {
                sweepAngle = 0;
                paint.setColor(progressColor);
                canvas.drawCircle(width / 2, height / 2, (width >> 1) - strokeWidth / 2, paint);
            }

            // 文字
            paint.setColor(textColor);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(textSize);
            float textWidth = paint.measureText(text);
            float textHeight = (paint.descent() + paint.ascent()) / 2 - dp2px(15f);
            canvas.drawText(text, (width - textWidth) / 2, (height - textHeight) / 2, paint);


            sleep(5);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void firstView() {
        Canvas canvas = getHolder().lockCanvas();
        if (canvas == null) {
            sleep(5);
            firstView();
        }
        // 结合init 配置 使背景透明
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        // 画笔配置
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(circleColor);
        // 背景圆环
        canvas.drawCircle(width / 2, height / 2, (width >> 1) - strokeWidth / 2, paint);

        getHolder().unlockCanvasAndPost(canvas);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
