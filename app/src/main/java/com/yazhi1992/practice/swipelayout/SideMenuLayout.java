package com.yazhi1992.practice.swipelayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class SideMenuLayout extends LinearLayout {

    private View viewContent;
    private View viewMenu;
    private int menuWidth;

    private Scroller mScroller;//用于滑动
    private VelocityTracker mVelocityTracker; //用于侦测速度的
    //开始位置
    private float xStart;
    private float yStart;
    //坐标位置
    private float xMove;
    private float yMove;
    //记录上次坐标位置
    private float xLast;

    private int touchSlop;

    public SideMenuLayout(Context context) {
        this(context, null);
    }

    public SideMenuLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //设置水平显示,初始化Scroller
        setOrientation(HORIZONTAL);
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //初始化两个View
        if (getChildCount() != 2) {
            throw new IllegalArgumentException("子view的数量必须为2个");
        }
        viewContent = getChildAt(0);
        viewMenu = getChildAt(1);
    }

    //设置两个子View的位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //设置内容的位置
        //设置菜单的位置
        menuWidth = viewMenu.getMeasuredWidth();
        viewContent.layout(0, 0, r, b - t);
        viewMenu.layout(r - l, 0, r - l + menuWidth, b - t);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//
//        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//        int setWidth = windowManager.getDefaultDisplay().getWidth();
//        int setHeight = windowManager.getDefaultDisplay().getHeight();
//
//        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(setWidth, setHeight);
//        } else if (widthMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(setWidth, height);
//        } else if (heightMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(width, setHeight);
//        }
//    }


//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        menuWidth = viewMenu.getWidth();
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        xMove = ev.getRawX();
        yMove = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录点下的位置
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                xStart = xMove;
                yStart = yMove;
                xLast = xMove;
                break;
            case MotionEvent.ACTION_MOVE:
                //当前位置与下，x,y位置比较
                float xDistance = Math.abs(xMove - xStart);
                float yDistance = Math.abs(yMove - yStart);
                //横向滑动大于纵向，触发条目的滑动，否则，交给子View去处理
                if (xDistance > yDistance && xDistance > touchSlop) {
                    requestDisallowInterceptTouchEvent(true);
                    intercept = true;
                } else {
                    requestDisallowInterceptTouchEvent(false);
                    intercept = false;
                }

        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        xMove = event.getRawX();
        //处理横向滑动
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                float viewX = -getScrollX();
                float viewToX = viewX + xMove - xLast;
                int viewToXDec = (int) (Math.min(Math.max(viewToX, -menuWidth), 0));
                scrollTo(-viewToXDec, 0);
                xLast = xMove;
                //滑动时新坐标
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起时,根据目前位置判断如何滚动
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();
                //先根据速度，当速度很快时根据速度方向判断收起还是展开，当速度小时，根据位置判断收起还是展开
                if (Math.abs(xVelocity) >= 50) {
                    if (xVelocity > 0) {
                        close();
                    } else {
                        open();
                    }
                } else {
                    float viewUpX = getScrollX();
                    if (viewUpX > 0 && viewUpX < menuWidth / 2) {
                        //隐藏菜单
                        close();
                    } else if (viewUpX >= menuWidth / 2 && viewUpX < menuWidth) {
                        //全部展示菜单
                        open();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    //获取当前是否是打开的
    public boolean isOpen() {
        boolean isOpen = getScrollX() > 0;
        return isOpen;
    }

    //全部打开
    public void open() {
        smoothScrollTo(menuWidth);
    }

    //全部关闭
    public void close() {
        smoothScrollTo(0);
    }

    private void smoothScrollTo(int destX) {
        int scrollX = getScrollX();
        int deltaX = destX - scrollX;;
        mScroller.startScroll(scrollX, getScrollY(), deltaX, 0, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

}
