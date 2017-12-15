package com.yazhi1992.practice.swipelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by zengyazhi on 2017/12/15.
 */

public class SwipeLayout extends ViewGroup {

    private View mContentView;
    private View mMenuLayout;
    //默认横向
    private int mGravity;
    //用于抬手时平滑滚动，也可以用animator实现
    private Scroller mScroller;
    //侦测滑动速度
    private VelocityTracker mVelocityTracker;
    private float mTouchX;
    private int mMenuWidth;
    private int mMoveDistance;
    private float mStartX;
    private int mScaledTouchSlop;

    public SwipeLayout(Context context) {
        super(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        mMenuLayout = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mMenuWidth = mMenuLayout.getMeasuredWidth();
        mContentView.layout(0, 0, r, b);
        mMenuLayout.layout(r - l, 0, r - l + mMenuWidth, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();
            width += measuredWidth;
            height = Math.max(height, measuredHeight);
        }

        //测量完后报告最终计算的宽高
        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom()//
        );
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = ev.getX();
                mTouchX = ev.getX() + mMoveDistance;
                break;
            case MotionEvent.ACTION_MOVE:
                float x = ev.getX();
                if (Math.abs(mStartX - x) > mScaledTouchSlop) {
                    //判断为左右滑动，拦截交由 onTouchEvent 处理侧滑逻辑
                    return true;
                } else {
                    return false;
                }
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX() - mTouchX;
                Log.e("zyz", moveX + " " + mMenuWidth);
                if (moveX < 0) {
                    //内容栏被左滑
                    mMoveDistance = (int) -moveX;
                    //左滑最大距离为菜单宽度
                    if (mMoveDistance > mMenuWidth) {
                        mMoveDistance = mMenuWidth;
                    }
                } else {
                    mMoveDistance = 0;
                }
                //手指拖动时实时滑动
                scrollTo(mMoveDistance, 0);
                break;
            case MotionEvent.ACTION_UP:
                //如果滑动速度快，根据滑动方向判断是打开还是关闭，如果速度慢，根据滑动位置判断
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();
                if (Math.abs(xVelocity) > 50) {
                    if (xVelocity > 0) {
                        //右滑关闭
                        close();
                    } else {
                        //左滑打开
                        open();
                    }
                } else {
                    //根据位置判断是打开还是关闭
                    if (mMoveDistance > mMenuWidth / 2) {
                        //打开
                        open();
                    } else {
                        //关闭
                        close();
                    }
                }
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    private void close() {
        int move = -mMoveDistance;
        mMoveDistance = 0;
        smoothScroll(move);
    }

    private void open() {
        int move = mMenuWidth - mMoveDistance;
        mMoveDistance = mMenuWidth;
        smoothScroll(move);
    }

    private void smoothScroll(int distance) {
        //平滑滚动
        mScroller.startScroll(getScrollX(), 0, distance, 0);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

}
