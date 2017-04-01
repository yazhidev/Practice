package com.yazhi1992.practice.wave_view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.yazhi1992.practice.R;

/**
 * Created by zengyazhi on 17/4/1.
 *
 * 使用贝塞尔曲线实现波浪效果
 */

public class BezierWave extends View {

    private Paint mPaint;
    private int mHeight;
    private int mWidth;
    private Path mPath;
    //Y轴偏移量
    //波浪高度
    private int Y_OFFSET;
    private int mX;
    private int mY;
    private ValueAnimator mValueAnimator;

    public BezierWave(Context context) {
        this(context, null);
    }

    public BezierWave(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierWave(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        int color = context.getResources().getColor(R.color.my_color);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);

        mPath = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(0, mHeight);
        mPath.lineTo(mX, mY);

        //画屏幕中波浪
        mPath.quadTo(mX + mWidth / 4, mY - Y_OFFSET, mX + mWidth / 2, mY);
        mPath.quadTo(mX + mWidth * 3 / 4, mY + Y_OFFSET, mX + mWidth, mY);
        //再画一部分一模一样的，超出屏幕
        mPath.quadTo(mX + mWidth * 5 / 4, mY - Y_OFFSET, mX + mWidth * 3 / 2, mY);
        mPath.quadTo(mX + mWidth * 7 / 4, mY + Y_OFFSET, mX + 2 * mWidth, mY);

        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        Y_OFFSET = mHeight / 10;
        mX = 0;
        mY = mHeight / 2;

        mValueAnimator = ValueAnimator.ofInt(0, -mWidth);
        mValueAnimator.setDuration(2000);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);

        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //x轴起点不断向右移动，只至移动距离为mWidth，即为完成了一段完整的波浪曲线
                mX = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

    public void start() {
        mValueAnimator.start();
    }
}
