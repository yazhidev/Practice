package com.yazhi1992.practice.hencoder.one;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
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
        //绘制椭圆
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawOval(100, 600, 500, 800, mPaint);
            mPaint.setStyle(Paint.Style.STROKE);
            //可见绘制椭圆指定的点就是左上右下四个边界点的坐标
            canvas.drawRect(100, 600, 500, 800, mPaint);
            //绘制圆角矩形
            canvas.drawRoundRect(100, 850, 500, 950, 50, 50, mPaint);
            //绘制扇形
            // sweepAngle 扫过的角度
            // useCenter 表示是否连接到圆心，如果不连接到圆心，就是弧形，如果连接到圆心，就是扇形。
            // STROKE 且连接到圆心，则是空心扇形
            canvas.drawArc(100, 1000, 300, 1200, 90, 90, true, mPaint);
            // STROKE 且不连接到圆心，就是弧形
            canvas.drawArc(100, 1000, 300, 1200, 270, 90, false, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            // FILL 且连接到圆心，则是实心扇形
            canvas.drawArc(100, 1300, 300, 1500, 90, 90, true, mPaint);
            canvas.drawArc(100, 1300, 300, 1500, 270, 90, false, mPaint);
        }
    }
}
