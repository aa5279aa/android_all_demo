package com.common.ui.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.common.util.DeviceUtil;

import java.text.DecimalFormat;

/**
 * 简易 SurfaceView 进度条 圆环和中心文字 背景透明
 * 不使用自定义 xml 属性配置
 */

public class QTNCircleBarSurfaceView extends CircleBarSurfaceView implements SurfaceHolder.Callback {

    public QTNCircleBarSurfaceView(Context context) {
        super(context);
        init();
    }

    public QTNCircleBarSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QTNCircleBarSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    private Paint paint;
    int circleColor = 0xfff5f5f5;
    int progressColor = 0xffffffff;
    int textColor = 0xff00d8a0;
    float textSize = 60f;
    // 设置外环的宽度
    float roundWidth = 8f;
    //定义外环的弧度
    int outRadius;
    //定义内环的弧度
    int centerRadius;

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
        outRadius = (int) (width / 2 - roundWidth / 2);
        centerRadius = outRadius - DeviceUtil.getPixelFromDip(getContext(), 19);
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
        if (sweepAngle >= 360) {
            sweepAngle = 0;
        }
        if (currentTimeMillis - maxTime >= startTime) {
            pauseDraw = true;
            double v2 = (double) maxTime / 1000;
            text = new DecimalFormat("00.00").format(v2);
            if (onTimeListener != null) {
                onTimeListener.run();
            }
        }
    }

    private void drawTask() {
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

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5f);
            paint.setColor(Color.parseColor("#B7BEC9"));
            paint.setAntiAlias(true);
            canvas.save();
            // 画外环
            for (int i = 0; i < 60; i++) {
                canvas.drawLine(width / 2, 0, height / 2, DeviceUtil.getPixelFromDip(getContext(), 8), paint);
                canvas.rotate(6f, width / 2, height / 2);
            }
            canvas.restore();

            // 画内环底色
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(DeviceUtil.getPixelFromDip(getContext(), 8));
            paint.setColor(Color.parseColor("#F2F2F2"));
            paint.setAntiAlias(true);
            canvas.drawCircle(width / 2, height / 2, centerRadius, paint);

            // 画笔配置
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(DeviceUtil.getPixelFromDip(getContext(), 8));
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.parseColor("#0D84FE"));
            // 进度条圆环
            RectF rectF = new RectF(width / 2 - centerRadius, width / 2 - centerRadius, width / 2 + centerRadius, width / 2 + centerRadius);
            canvas.drawArc(rectF, startAngle, sweepAngle, false, paint);
            progressCalc();

            // 时间文字
            paint.setColor(Color.parseColor("#333333"));
            paint.setStyle(Paint.Style.FILL);
            Typeface normalTypeface = Typeface.create("sans-serif-medium", Typeface.NORMAL);
            paint.setTypeface(normalTypeface);
            paint.setTextSize(DeviceUtil.getPixelFromDip(getContext(), 32));
            float timeTextWidth = paint.measureText(text);
            float timeTextHeight = (paint.descent() + paint.ascent()) / 2 - dp2px(15f);
            canvas.drawText(text, (width - timeTextWidth) / 2, (height - timeTextHeight) / 2 - DeviceUtil.getPixelFromDip(getContext(), 8), paint);

            // 状态文字
//            paint.setColor(Color.parseColor("#60333333"));
//            paint.setStyle(Paint.Style.FILL);
//            paint.setTextSize(DeviceUtil.getPixelFromDip(getContext(), 12));
//            float statuesTextWidth = paint.measureText(statueText);
//            float statueTextHeight = (paint.descent() + paint.ascent()) / 2 - dp2px(15f);
//            switch (type) {
//                case 0:
//                    statueText = "未开启";
//                    break;
//                case 1:
//                    statueText = "识别完成";
//                    break;
//                case 2:
//                    statueText = "重新识别";
//                    break;
//            }
//
//            canvas.drawText(statueText, (width - statuesTextWidth) / 2, (height - statueTextHeight) / 2 + 40, paint);


            sleep(5);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }


    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
