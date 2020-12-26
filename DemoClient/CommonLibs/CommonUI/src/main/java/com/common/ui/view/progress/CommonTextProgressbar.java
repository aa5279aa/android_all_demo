package com.common.ui.view.progress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.common.ui.R;

public class CommonTextProgressbar extends ProgressBar {

    private String text = "加载中...";
    private int textSize = 12;
    private String progressText;
    private Paint mPaint;

    public CommonTextProgressbar(Context context) {
        super(context);
        initText(context, null, 0);
    }

    public CommonTextProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initText(context, attrs, 0);
    }

    public CommonTextProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initText(context, attrs, defStyleAttr);
    }

    @Override
    public void setProgress(int progress) {
//        setProgressText(progress);
        super.setProgress(progress);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextSize(int textSize) {
        float size = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, textSize, getResources().getDisplayMetrics());
        if (this.mPaint != null)
            this.mPaint.setTextSize(size);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        @SuppressLint("DrawAllocation")
        Rect rect = new Rect();
//        this.mPaint.getTextBounds(this.progressText, 0, this.progressText.length(), rect);
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(this.text, x, y, this.mPaint);
    }

    // 初始化，画笔
    private void initText(Context context, AttributeSet attrs, int defStyleAttr) {
        int color = getResources().getColor(R.color.color_blue);
        if (attrs != null) {
            final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CommonTextProgressbar,
                    defStyleAttr, 0);
            color = attributes.getColor(R.styleable.CommonTextProgressbar_progress_color, 0);
            attributes.recycle();
        }
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(color);
        float size = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, textSize, getResources().getDisplayMetrics());
        this.mPaint.setTextSize(size);
    }

    // 设置文字内容
    private void setProgressText(int progress) {
        int i = (int) ((progress * 1.0f / this.getMax()) * 100);
        this.progressText = i + "%";
    }
}
