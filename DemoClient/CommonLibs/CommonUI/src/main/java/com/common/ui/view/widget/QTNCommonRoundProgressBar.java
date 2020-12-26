package com.common.ui.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.common.ui.R;


public class QTNCommonRoundProgressBar extends CommonRoundProgressBar {
    /**
     * mRadius of view
     */
    private int mRadius = dp2px(30);

    private int mMaxPaintWidth;
    private float startAngle = 270;

    public QTNCommonRoundProgressBar(Context context) {
        this(context, null);
    }

    public QTNCommonRoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        //    mReachedProgressBarHeight = (int) (mUnReachedProgressBarHeight * 2.5f);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonRoundProgressBar);
        mRadius = (int) ta.getDimension(R.styleable.CommonRoundProgressBar_radius, mRadius);
        ta.recycle();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    /**
     * 这里默认在布局中padding值要么不设置，要么全部设置
     */
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {

        mMaxPaintWidth = Math.max(mReachedProgressBarHeight, mUnReachedProgressBarHeight);
        int expect = mRadius * 2 + mMaxPaintWidth + getPaddingLeft() + getPaddingRight();
        int width = resolveSize(expect, widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);
        int realWidth = Math.min(width, height);

        mRadius = (realWidth - getPaddingLeft() - getPaddingRight()) / 2;

        setMeasuredDimension(realWidth, realWidth);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        String text = getProgress() + "%";
        float textWidth = mPaint.measureText(text);
        float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;

        // draw out bg
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5f);
        mPaint.setColor(Color.parseColor("#60333333"));
        mPaint.setAntiAlias(true);
        canvas.save();
        for (int i = 0; i < 60; i++) {
            canvas.drawLine(mRadius, 0, mRadius, mRadius / 10, mPaint);
            canvas.rotate(6f, mRadius, mRadius);
        }
        canvas.restore();

        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        mPaint.setStyle(Paint.Style.STROKE);

        // draw unreaded bar
        mPaint.setColor(mUnReachedBarColor);
        mPaint.setStrokeWidth(mUnReachedProgressBarHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius - 50, mPaint);
        // draw reached bar
        mPaint.setColor(mReachedBarColor);
        mPaint.setStrokeWidth(mReachedProgressBarHeight);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        RectF rectF = new RectF(50, 50, mRadius * 2 - 50, mRadius * 2 - 50);
        canvas.drawArc(rectF, startAngle, sweepAngle, false, mPaint);
        // draw text
        if (mIfDrawText) {
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, mRadius - textWidth / 2, mRadius - textHeight, mPaint);
        }
        canvas.restore();

    }
}
