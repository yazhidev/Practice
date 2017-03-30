package com.yazhi1992.practice.rotate_circle;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * Created by zengyazhi on 17/3/21.
 * <p>
 * 旋转圆弧动画
 */

public class RotateCircleView extends View {

    //直径
    private int mRadiu = 40;
    private RectF mRect;
    private Paint mPaint;
    private int mHeight;
    private int mWidth;
    private int mRealAngle;
    private SweepGradient mSweepGradient;
    //是否已绘制完成，进入旋转阶段
    private boolean mCompletePaint;
    private int mColor;
    private ValueAnimator mValueAnimator;

    public RotateCircleView(Context context) {
        this(context, null);
    }

    public RotateCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mColor = Color.RED;

        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        int[] ints = new int[]{Color.WHITE, mColor};
        mSweepGradient = new SweepGradient(mWidth / 2, mHeight / 2, ints, null);
        mPaint.setShader(mSweepGradient);

        mRadiu = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());

        initAnim();
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        mValueAnimator = ValueAnimator.ofInt(0, 360);
        mValueAnimator.setDuration(1800);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRealAngle = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mCompletePaint = true;
            }
        });
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint != null) {
            int paintAngle;
            if (mCompletePaint) {
                paintAngle = 360;
            } else {
                paintAngle = mRealAngle;
            }
            mPaint.setShader(mSweepGradient);
            mPaint.setStrokeWidth(mRadiu);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            canvas.rotate(mRealAngle, mWidth / 2, mHeight / 2);
            canvas.drawArc(mRect, 360 - paintAngle, paintAngle, false, mPaint);

            //画被白色盖住的半个头部
            if (paintAngle > 2) {
                mPaint.setShader(null);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setStrokeCap(Paint.Cap.SQUARE);
                mPaint.setStrokeWidth(1);
                RectF oval2 = new RectF(mWidth - mRadiu * 3 / 2, mHeight / 2 - mRadiu / 2 - 2
                        , mWidth - mRadiu / 2, mHeight / 2 + mRadiu / 2 - 2);
                canvas.drawArc(oval2, 0, 180, true, mPaint);
            }

        }
    }

    //开始执行动画
    public void star() {
        if (mValueAnimator != null) {
            if (mValueAnimator.isRunning()) {
                mValueAnimator.cancel();
            } else {
                mCompletePaint = false;
                mValueAnimator.start();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mRect = new RectF();
        mRect.left = mRadiu;
        mRect.top = mRadiu;
        mRect.right = mWidth - mRadiu;
        mRect.bottom = mHeight - mRadiu;

    }
}
