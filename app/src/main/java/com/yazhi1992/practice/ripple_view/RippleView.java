package com.yazhi1992.practice.ripple_view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yazhi1992.practice.R;

/**
 * Created by zengyazhi on 17/3/21.
 * <p>
 * 涟漪
 */

public class RippleView extends View {

    //内部圆的半径
    private int ORIGINAL_RADIU;
    //最大半径
    private int MAX_RADIU;
    //三个不断散开的圆的半径
    private int mRadiu = ORIGINAL_RADIU;
    private int mRadiu2 = -1;
    private int mRadiu3 = -1;
    private Paint mPaint;
    //屏幕宽高
    private int mHeight;
    private int mWidth;
    //是否已绘制完成，进入旋转阶段
    private Thread mThread;
    private int mColor;
    private boolean mStartAnim;

    public RippleView(Context context) {
        super(context);
        init(context);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //关闭GPU硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mColor = context.getResources().getColor(R.color.my_color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint != null) {
            mPaint.setColor(mColor);
            //绘制外部圆
            drawCircle(canvas, mRadiu);
            drawCircle(canvas, mRadiu2);
            drawCircle(canvas, mRadiu3);
            //绘制内部白色圆
            mPaint.setColor(Color.WHITE);
            mPaint.setShader(null);
            canvas.drawCircle(mWidth / 2, mHeight / 2, ORIGINAL_RADIU, mPaint);
        }
    }

    /**
     * 绘制圆
     * @param canvas
     * @param radiu
     */
    private void drawCircle(Canvas canvas, int radiu) {
        if (radiu > ORIGINAL_RADIU && radiu < MAX_RADIU) {
            setAlpha(radiu);
            canvas.drawCircle(mWidth / 2, mHeight / 2, radiu, mPaint);
        }
    }

    /**
     * 设置内部空白圆圈的半径
     * @param width
     */
    public void setOriginalRadiu(int width) {
        ORIGINAL_RADIU = width - 10;
        mRadiu = ORIGINAL_RADIU;
        postInvalidate();
    }

    /**
     * 根据半径设置透明度
     * @param radiu
     */
    private void setAlpha(int radiu) {
        double v = (radiu - ORIGINAL_RADIU) * 1d / (MAX_RADIU - ORIGINAL_RADIU);
        double v1 = (1 - v) * 255;
        if (v1 > 255) {
            v1 = 255;
        }
        if (v1 < 0) {
            v1 = 0;
        }
        mPaint.setAlpha((int) v1);
    }

    /**
     * 开始执行动画
     */
    public void star() {
        mStartAnim = true;
        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mStartAnim) {
                        //当最外圈圆到达1/3出时开始绘制第二圈圆
                        if (mRadiu2 == -1 && mRadiu > ((MAX_RADIU - ORIGINAL_RADIU) / 3)) {
                            mRadiu2 = mRadiu - ((MAX_RADIU - ORIGINAL_RADIU) / 3);
                        }
                        //当最外圈圆到达2/3出时开始绘制第三圈圆
                        if (mRadiu3 == -1 && mRadiu > ((MAX_RADIU - ORIGINAL_RADIU) / 3)) {
                            mRadiu3 = mRadiu - 2 * ((MAX_RADIU - ORIGINAL_RADIU) / 3);
                        }

                        //三圈圆到达最大半径时则自动从最小半径开始重新绘制，不断重复
                        if (mRadiu >= MAX_RADIU) {
                            mRadiu = ORIGINAL_RADIU;
                        } else if (mRadiu2 >= MAX_RADIU) {
                            mRadiu2 = ORIGINAL_RADIU;
                        } else if (mRadiu3 >= MAX_RADIU) {
                            mRadiu3 = ORIGINAL_RADIU;
                        }

                        //半径加大，不断外散
                        if (mRadiu < MAX_RADIU) {
                            mRadiu++;
                        }
                        if (mRadiu2 != -1 && mRadiu2 < MAX_RADIU) {
                            mRadiu2++;
                        }
                        if (mRadiu3 != -1 && mRadiu3 < MAX_RADIU) {
                            mRadiu3++;
                        }
                        postInvalidate();
                        try {
                            Thread.sleep(12);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            mThread.start();
        }
    }

    /**
     * 停止动画
     */
    public void stop() {
        mStartAnim = false;
        mThread = null;
        mRadiu = ORIGINAL_RADIU;
        mRadiu2 = -1;
        mRadiu3 = -1;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(mColor);
        MAX_RADIU = mWidth / 2;
        postInvalidate();

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, MAX_RADIU);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRadiu = (int) animation.getAnimatedValue();
                if (mRadiu2 == -1 && mRadiu > ((MAX_RADIU - ORIGINAL_RADIU) / 3)) {
                    mRadiu2 = mRadiu - ((MAX_RADIU - ORIGINAL_RADIU) / 3);
                }
            }
        });
    }
}
