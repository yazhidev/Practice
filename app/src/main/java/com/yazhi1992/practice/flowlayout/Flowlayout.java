package com.yazhi1992.practice.flowlayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengyazhi on 2017/12/5.
 */

public class Flowlayout extends ViewGroup {
    private List<String> mData;
    private Context mContext;
    //每一行view
    protected List<List<View>> mAllViews = new ArrayList<List<View>>();
    protected List<Integer> mLineHeight = new ArrayList<Integer>();
    protected List<Integer> mLineWidth = new ArrayList<Integer>();
    private List<View> mLineViews = new ArrayList<>();
    private int mGravity = -1;
    private static final int LEFT = -1;
    private static final int CENTER = 0;
    private static final int RIGHT = 1;

    public Flowlayout(Context context) {
        this(context, null);
    }

    public Flowlayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Flowlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //每行的宽高
        int lineWidth = 0;
        int lineHeight = 0;
        //测量子view后计算出的自身的宽高
        int width = 0;
        int height = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            //测量该子view
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            //获取子view宽高
            int childWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            int childHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                //已经超出viewgroup宽度了，换行
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                lineHeight = childHeight;
                //换行了，要加上高度
                height += childHeight;
            } else {
                //还未超出viewgroup宽度
                lineWidth += childWidth;
                //实时计算高度
                if (childHeight > lineHeight) {
                    //如果同一行出现了更高的view，则重新计算高度
                    height = height - lineHeight;
                    lineHeight = childHeight;
                    height += lineHeight;
                }
            }
        }
        //测量完后报告最终计算的宽高
        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom()//
        );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();
        mLineWidth.clear();
        mLineViews.clear();

        int lineWidth = 0;
        int lineHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) continue;
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            int childHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;;
            if (childWidth + lineWidth > getWidth() - getPaddingLeft() - getPaddingRight()) {
                //换行
                mLineHeight.add(lineHeight);
                mLineWidth.add(lineWidth);
                mAllViews.add(mLineViews);
                //重置line相关参数
                lineWidth = 0;
                lineHeight = childHeight;
                mLineViews = new ArrayList<>();
            }
            lineWidth += childWidth;
            lineHeight = Math.max(lineHeight, childHeight);
            mLineViews.add(child);
        }
        mLineHeight.add(lineHeight);
        mLineWidth.add(lineWidth);
        mAllViews.add(mLineViews);

        int left = getPaddingLeft();
        int top = getPaddingTop();

        int lineNum = mAllViews.size();

        //为每一行的view排位
        for (int i = 0; i < lineNum; i++) {
            mLineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            //设置位置
            // set gravity
            int currentLineWidth = this.mLineWidth.get(i);
            switch (this.mGravity) {
                case LEFT:
                    left = getPaddingLeft();
                    break;
                case CENTER:
                    left = (getWidth() - currentLineWidth) / 2 + getPaddingLeft();
                    break;
                case RIGHT:
                    left = getWidth() - currentLineWidth + getPaddingLeft();
                    break;
            }

            for (int j = 0; j < mLineViews.size(); j++) {
                View child = mLineViews.get(j);
                if (child.getVisibility() == View.GONE) continue;
                MarginLayoutParams lp = (MarginLayoutParams) child
                        .getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.leftMargin
                        + lp.rightMargin;
            }
            top += lineHeight;
        }
    }

    public void setData(List<String> data) {
        this.mData = data;
        if (data == null || data.isEmpty()) return;
        for (int i = 0; i < data.size(); i++) {
            TextView textView = new TextView(mContext);
            textView.setText(data.get(i));
            textView.setTextSize(20);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            textView.setLayoutParams(layoutParams);
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundColor(Color.LTGRAY);
            addView(textView);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}
