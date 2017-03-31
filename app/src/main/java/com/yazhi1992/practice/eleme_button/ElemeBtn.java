package com.yazhi1992.practice.eleme_button;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.yazhi1992.practice.R;

/**
 * Created by zengyazhi on 17/3/29.
 *
 * 出处：http://blog.csdn.net/zxt0601/article/details/54235736
 *
 * 饿了么添加购物车按钮，感觉挺有意思的，所以看了源码后就跟着撸了一个，就实现了view的绘制还有相关动画，一些
 * 功能和逻辑懒得去实现啦。
 */

public class ElemeBtn extends View {

    public int mSparkleWidth;
    public int mCalibrationWidth;
    private Paint mAddPaint;
    private Paint mDelPaint;
    //文字画笔
    private Paint mTextPaint;
    //背景画笔
    private Paint mHintPaint;
    private RectF mRectF;
    private int mTest1 = 100;
    //圆的半径
    private float mRadiu;
    //圆之间间距
    private float mGapBetweenCircle;
    //圆圈的宽度
    private float mCircleWidth;
    //+ -号画笔宽度
    private float mLineWidth;
    //文字大小
    private float mTextSize;
    //文字
    private String mHintText = "添加购物车";
    private int mCount= 0;
    //文字颜色
    private int mTextColor;
    //背景颜色
    private int mBgColor;
    //+号背景颜色
    private int mAddBgColor;
    //+号前景颜色
    private int mAddFgColor;
    //-号背景颜色
    private int mDelBgColor;
    //-号前景颜色
    private int mDelFgColor;
    private int mWidth;
    private int mHeight;
    private int mTop;
    private int mLeft;
    //圆角值
    private float mBgRoundValue;
    private int mAnimTime = 500;
    private Path mDelPath;
    private Path mAddPath;
    private Region mAddRegion;
    private Region mDelRegion;
    //按钮还原动画
    private ValueAnimator mBtnRestoreAnim;
    //按钮收缩动画
    private ValueAnimator mBtnStartAnim;
    //按钮滚动时的分值
    private float mAnimBgFraction;
    //-号滚出来动画
    private ValueAnimator mRotateAnim;
    //-号回滚动画
    private ValueAnimator mRotateRestoreAnim;
    //-号滚出来时的分值,滚出来 0~1，回滚1~0
    private float mRotateFraction;
    //是否需要显示文字（点击 按钮收缩后就不显示文字）
    private boolean mIsShowHintText = true;
    //是否是按钮状态
    private boolean mIsHintMode = true;

    public ElemeBtn(Context context) {
        this(context, null);
    }

    public ElemeBtn(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElemeBtn(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initDefaultValue();

        mAddPaint = new Paint();
        mAddPaint.setStyle(Paint.Style.FILL);
        mAddPaint.setAntiAlias(true);

        mDelPaint = new Paint();
        mDelPaint.setStyle(Paint.Style.FILL);
        mDelPaint.setAntiAlias(true);

        mHintPaint = new Paint();
        mHintPaint.setAntiAlias(true);
        mHintPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);

        mRectF = new RectF();
        mRectF.set(mTest1, mTest1, mTest1 + 300, mTest1 + 300);

        mAddPath = new Path();
        mDelPath = new Path();

        mAddRegion = new Region();
        mDelRegion = new Region();

        initAnim();

        postInvalidate();

    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        mBtnRestoreAnim = ValueAnimator.ofFloat(1, 0);
        mBtnRestoreAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimBgFraction = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mBtnRestoreAnim.setDuration(mAnimTime);
        mBtnRestoreAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsShowHintText = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mBtnStartAnim = ValueAnimator.ofFloat(0, 1);
        mBtnStartAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimBgFraction = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mBtnStartAnim.setDuration(mAnimTime);
        mBtnStartAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsHintMode = false;
                //-号开始滚出来
                mRotateAnim.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mRotateAnim = ValueAnimator.ofFloat(0, 1);
        mRotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRotateFraction = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mRotateAnim.setDuration(mAnimTime);

        mRotateRestoreAnim = ValueAnimator.ofFloat(1, 0);
        mRotateRestoreAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRotateFraction = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mRotateRestoreAnim.setDuration(mAnimTime);
        mRotateRestoreAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsHintMode = true;
                mBtnRestoreAnim.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 初始化各参数
     */
    private void initDefaultValue() {
        mSparkleWidth = 30;
        mCalibrationWidth = 20;

        mRadiu = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, getResources().getDisplayMetrics());

        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, getResources().getDisplayMetrics());
        mCircleWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, getResources().getDisplayMetrics());
        mLineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, getResources().getDisplayMetrics());

        mTextColor = Color.WHITE;
        mBgColor = getResources().getColor(R.color.my_color);
        mAddBgColor = getResources().getColor(R.color.my_color);
        mAddFgColor = Color.WHITE;
        mDelBgColor = getResources().getColor(R.color.my_color);
        mDelFgColor = Color.WHITE;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (mGapBetweenCircle == 0) {
            mGapBetweenCircle = mTextPaint.measureText(mHintText);
        }

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                //wrap_content
                //不超过父控件范围
                int computeWidth = (int) (getPaddingLeft() + mRadiu * 2 + mGapBetweenCircle + mRadiu * 2 + getPaddingRight() + mCircleWidth * 2);
                widthSize = computeWidth > widthSize ? widthSize : computeWidth;
                break;
            case MeasureSpec.UNSPECIFIED:
                //自由发挥
                computeWidth = (int) (getPaddingLeft() + mRadiu * 2 + mGapBetweenCircle + mRadiu * 2 + getPaddingRight() + mCircleWidth * 2);
                widthSize = computeWidth;
                break;
            case MeasureSpec.EXACTLY:
                break;
            default:
                break;
        }
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                int computeHeight = (int) (getPaddingTop() + mRadiu * 2 + getPaddingBottom() + mCircleWidth * 2);
                heightSize = computeHeight > heightSize ? heightSize : computeHeight;
                break;
            case MeasureSpec.UNSPECIFIED:
                computeHeight = (int) (getPaddingTop() + mRadiu * 2 + getPaddingBottom() + mCircleWidth * 2);
                heightSize = computeHeight;
                break;
            case MeasureSpec.EXACTLY:
                break;
            default:
                break;
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTop = (int) (getPaddingTop() + mCircleWidth);
        mLeft = (int) (getPaddingLeft() + mCircleWidth);

        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mIsHintMode) {
            //绘制背景 圆角矩形
            mHintPaint.setColor(mBgColor);
            //按钮收缩后只剩一个圆圈的宽度
            RectF rectF = new RectF(mLeft + mAnimBgFraction * (mWidth - mCircleWidth - getPaddingRight() - mLeft - mRadiu * 2), mTop, mWidth - mCircleWidth - getPaddingRight(), mHeight - mCircleWidth - getPaddingBottom());
            float height = rectF.height();
            if (mBgRoundValue == 0) {
                mBgRoundValue = height / 2;
            }
            canvas.drawRoundRect(rectF, mBgRoundValue, mBgRoundValue, mHintPaint);
            //绘制文字
            if (mIsShowHintText) {
                mTextPaint.setColor(mTextColor);
                mTextPaint.setTextSize(mTextSize);
                //计算文字绘制坐标
                int baseX = (int) ((mWidth - mTextPaint.measureText(mHintText)) / 2);
                int baseY = (int) ((mHeight - (mTextPaint.descent() + mTextPaint.ascent())) / 2);
                canvas.drawText(mHintText, baseX, baseY, mTextPaint);
            }
        } else {
            //+号圆圈的起始绘制位置
            float addLeft = mLeft + mRadiu * 2 + mCircleWidth + mGapBetweenCircle + mRadiu;

            //绘制 - 号背景
            mDelPaint.setStrokeWidth(mCircleWidth);
            mDelPath.reset();
            //-号圆圈的圆形x坐标
            float delLeft = mLeft + mCircleWidth + mRadiu;
            mDelPath.addCircle(delLeft + (1 - mRotateFraction) * (addLeft - delLeft), mHeight / 2, mRadiu, Path.Direction.CW);
            mDelRegion.setPath(mDelPath, new Region(mLeft, mTop, mWidth - getPaddingRight(), mHeight - getPaddingBottom()));
            mDelPaint.setColor(mDelBgColor);
            mDelPaint.setAlpha((int) (255 * mRotateFraction));
            canvas.drawPath(mDelPath, mDelPaint);
            //绘制 - 号前景，旋转canvas
            mDelPaint.setColor(mDelFgColor);
            mDelPaint.setStrokeWidth(mLineWidth);
            canvas.save();
            //旋转中心即为圆圈圆心
            canvas.rotate(360 * (1 - mRotateFraction), delLeft + (1 - mRotateFraction) * (addLeft - delLeft), mHeight / 2);
            canvas.drawLine(delLeft - mRadiu / 2 + (addLeft - delLeft) * (1 - mRotateFraction), mTop + mRadiu, delLeft + mRadiu / 2 + (1 - mRotateFraction) * (addLeft - delLeft), mTop + mRadiu, mDelPaint);
            canvas.restore();
            //绘制文字，旋转canvas
            mTextPaint.setColor(Color.BLACK);
            mTextPaint.setTextSize(mTextSize);
            //计算文字绘制坐标
            int baseX = (int) (mWidth / 2 - mTextPaint.measureText(mCount + "") / 2 + (1 - mRotateFraction) * (mGapBetweenCircle / 2));
            int baseY = (int) ((mHeight - (mTextPaint.descent() + mTextPaint.ascent())) / 2);
            mTextPaint.setAlpha((int) (255 * mRotateFraction));
            canvas.save();
            //旋转中心为文字中心（起始位置加文字宽度的一般）
            canvas.rotate(360 * (1 - mRotateFraction), baseX + mTextPaint.measureText(mCount + "") / 2, mHeight / 2);
            canvas.drawText(mCount + "", baseX, baseY, mTextPaint);
            canvas.restore();

            //绘制 + 号背景
            mAddPaint.setStrokeWidth(mCircleWidth);
            mAddPath.reset();
            //加号圆圈的圆形x坐标
            mAddPath.addCircle(addLeft, mHeight / 2, mRadiu, Path.Direction.CW);
            mAddRegion.setPath(mAddPath, new Region(mLeft, mTop, mWidth - getPaddingRight(), mHeight - getPaddingBottom()));
            mAddPaint.setColor(mAddBgColor);
            canvas.drawPath(mAddPath, mAddPaint);
            //绘制 + 号前景
            //+ -号长度为半径
            mAddPaint.setColor(mAddFgColor);
            mAddPaint.setStrokeWidth(mLineWidth);
            canvas.drawLine(addLeft - mRadiu / 2, mTop + mRadiu, addLeft + mRadiu / 2, mTop + mRadiu, mAddPaint);
            canvas.drawLine(addLeft, mTop + mRadiu / 2, addLeft, mTop + mRadiu * 3 / 2, mAddPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mIsHintMode) {
                    //点击收缩按钮
                    btnStart();
                } else {
                    if (mDelRegion.contains((int) (event.getX()), (int) (event.getY()))) {
                        //点击了-
                        btnRestore();
                    } else if (mAddRegion.contains((int) (event.getX()), (int) (event.getY()))) {
                        //点击了+
                        mCount++;
                        postInvalidate();
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 按钮还原
     */
    private void btnRestore() {
        if (mRotateRestoreAnim != null) {
            mRotateRestoreAnim.start();
        }
    }

    /**
     * 按钮收缩动画
     */
    public void btnStart() {
        mIsShowHintText = false;
        if (mBtnStartAnim != null) {
            mBtnStartAnim.start();
        }
    }

}

