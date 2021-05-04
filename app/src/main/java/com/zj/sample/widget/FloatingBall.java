package com.zj.sample.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class FloatingBall extends View {
    Paint mPaint;
    TextPaint mTextPaint;

    public FloatingBall(Context context) {
        super(context);
        init();
    }

    public FloatingBall(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatingBall(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Context context = getContext();

        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#ffe100"));
        mPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(sp2px(context, 16));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        int width = getWidth();
        int height = getHeight();

        canvas.drawCircle(width >> 1, height >> 1, width >> 1, mPaint);

        canvas.drawText("Tools", width >> 1, (float) (width * 0.60), mTextPaint);
    }

    private int sp2px(Context context, float sp) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }
}