package com.yazhi1992.practice.hook_icon;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.yazhi1992.practice.utils.Utils;

/**
 * Created by zengyazhi on 2017/8/14.
 * <p>
 * 仿支付宝支付成功打钩动画
 */

public class HookIcon extends View {

    private Paint mPaint;
    private int mCircleRadiu;
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private ValueAnimator mValueAnimator;
    //已绘制的角度
    private int mRealAngle;
    //起始旋转角度
    private int mBeginAngle;
    //圆是否已绘制完成
    private boolean mCompletePaint;
    private RectF mRect;
    //钩的绘制进度 0-1
    private float mDrawProgress;
    private float mDrawHookProgress;
    //钩长短比例为1：2，故小段的绘制时间占总时长的1/3
    private float mFirstHookPercent = 0.33f;
    private float mSecondHookPercent = 0.66f;
    private float mPaintWidth;

    public HookIcon(Context context) {
        this(context, null);
    }

    public HookIcon(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HookIcon(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
        initAnim();
    }

    private void init() {
        //圆的半径默认20
        mCircleRadiu = (int) Utils.dp2px(mContext, 30);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);

        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaintWidth = 7;
        mPaint.setStrokeWidth(mPaintWidth);

        //从270度处开始绘制
        mBeginAngle = 270;
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        mValueAnimator = ValueAnimator.ofFloat(0, 2);
        mValueAnimator.setDuration(1000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDrawProgress = (Float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mValueAnimator.setInterpolator(new LinearInterpolator());
    }

    public void startAnim() {
        mValueAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mRect = new RectF();
        mRect.left = mWidth / 2 - mCircleRadiu;
        mRect.top = mHeight / 2 - mCircleRadiu;
        mRect.right = mWidth / 2 + mCircleRadiu;
        mRect.bottom = mHeight / 2 + mCircleRadiu;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint != null && mRect != null) {
            //绘制圆
            canvas.save();
            if (mDrawProgress > 1) {
                //圆已绘制完成
                canvas.rotate(360 + mBeginAngle, mWidth / 2, mHeight / 2);
                canvas.drawArc(mRect, 0, 360, false, mPaint);
            } else {
                //根据百分比应绘制计算扇形的弧度
                int realAngle = (int) (360 * mDrawProgress);
                canvas.rotate(realAngle + mBeginAngle, mWidth / 2, mHeight / 2);
                canvas.drawArc(mRect, 360 - realAngle, realAngle, false, mPaint);
            }
            canvas.restore();
            if (mDrawProgress > 1) {
                mDrawHookProgress = mDrawProgress - 1;
                //绘制钩
                Path path = new Path();
                //圆心 mWidth/2 ，mHeight/2
                //钩的左边起点
                float startPointX = (float) (mWidth / 2 - 0.53d * mCircleRadiu);
                float startPointY = (float) (mHeight / 2 + 0.05d * mCircleRadiu);
                path.moveTo(startPointX, startPointY);
                //钩的转折点
                //从左起点到转折点 x/y 移动的距离（因为是45度，所以x/y 移动距离相等）
                float firstHookMoveDistance = (float) (0.35d * mCircleRadiu);
                float secondHookMoveDistance = 2 * firstHookMoveDistance;
                if (mDrawHookProgress >= mFirstHookPercent) {
                    //开始绘制第二段了
                    path.lineTo(startPointX + firstHookMoveDistance, startPointY + firstHookMoveDistance);
                    //转折点到右终点的移动距离
                    float secondPercent = (mDrawHookProgress - mFirstHookPercent) / (1 - mFirstHookPercent);
                    path.lineTo(startPointX + firstHookMoveDistance + secondHookMoveDistance * secondPercent
                            , startPointY + firstHookMoveDistance - secondHookMoveDistance * secondPercent);
                    //绘制钩第二段
                    canvas.drawPath(path, mPaint);
                } else {
                    //还在绘制钩的第一段
                    path.lineTo(startPointX + firstHookMoveDistance * (mDrawHookProgress / mFirstHookPercent), startPointY + firstHookMoveDistance * (mDrawHookProgress / mFirstHookPercent));
                    canvas.drawPath(path, mPaint);
                }
            }
        }
    }
}
