package com.yazhi1992.practice.bezier_anim;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yazhi1992.practice.R;

/**
 * Created by zengyazhi on 17/4/1.
 * <p>
 * 贝塞尔动画
 * 参考：http://www.jcodecraeer.com/a/anzhuokaifa/2017/0105/6936.html
 *      http://blog.csdn.net/z82367825/article/details/51599245
 */

public class BezierAnimView extends View {

    private Paint mPaint;
    private int mHeight;
    private int mWidth;
    private Path mPath;
    private int mColor1;
    private int mColor2;
    private int mRadiu;
    //Y轴偏移量
    private int mYOffset;
    private ValueAnimator mValueAnimator;
    private int Y_OFFSET = 10;

    public BezierAnimView(Context context) {
        this(context, null);
    }

    public BezierAnimView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mColor1 = context.getResources().getColor(R.color.my_color);
        mColor2 = context.getResources().getColor(R.color.my_color2);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);

        mPath = new Path();

        mRadiu = 60;
    }

    // TODO: 17/4/1  QQ未读粘脸效果

    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(0, mHeight / 2);
        mPath.lineTo(mWidth, mHeight / 2);
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();
        //画正方形
        mPaint.setColor(mColor1);
        canvas.drawPath(mPath, mPaint);

        //画水滴
        mPaint.setColor(mColor2);
        canvas.drawCircle(mWidth / 2, mHeight * 3 / 5 + mRadiu * 2 - mYOffset, mRadiu, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mValueAnimator = ValueAnimator.ofInt(0, mHeight / 2);
        mValueAnimator.setDuration(2000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mYOffset = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

    }

    public void start() {
        mValueAnimator.start();
    }
}
