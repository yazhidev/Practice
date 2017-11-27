package com.yazhi1992.practice.hencoder.one;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zengyazhi on 2017/11/27.
 */

public class ViewOne extends View {
    private Paint mPaint;

    public ViewOne(Context context) {
        this(context, null);
    }

    public ViewOne(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewOne(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLUE);
        //空心
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(100, 100, 30, mPaint);
        canvas.drawRect(50, 150, 150, 200, mPaint);
        //实心
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(200, 100, 30, mPaint);
        canvas.drawRect(200, 150, 300, 200, mPaint);
        //绘制点（空心实心没有差别）
        mPaint.setStrokeWidth(50);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPoint(50, 300, mPaint);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        canvas.drawPoint(150, 300, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPoint(250, 300, mPaint);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        canvas.drawPoint(350, 300, mPaint);
        //绘制连续点
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        float[] points = {100, 400, 200, 400, 100, 500, 200, 500};
        canvas.drawPoints(points, mPaint);
        //绘制线
        mPaint.setStrokeWidth(10);
        canvas.drawLine(100, 550, 450, 600, mPaint);
    }
}
