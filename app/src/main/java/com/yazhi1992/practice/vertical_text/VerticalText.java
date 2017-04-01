package com.yazhi1992.practice.vertical_text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zengyazhi on 17/3/31.
 * <p>
 * 竖直排列的文字控件
 * <p>
 * 参考：https://github.com/lybeat/PlumbTextView
 */

public class VerticalText extends View {

    private String mText = "世间所有的相遇，都是久别重逢";
    //分割符号
    private String mSubStringStr = "，";
    private Paint mTextPaint;
    private float mTextSize;
    //屏幕宽高
    private int mWidth;
    private int mHeight;
    //单个字符高度
    private float mCharHeight;
    //单个字符宽度
    private int mCharWidth;
    //上下字间距
    private float mTopBottomSpacing;
    //左右行间距
    private float mLeftRightSpacing;
    private List<String> mFormatTexts = new ArrayList<>();
    //计算每行可容纳文字数目后得出的上下边距
    private float mCalculatePaddingTopBottom;
    //计算总列数后得出的左右边距
    private float mCalculatePaddingLeftRight;
    //一列最多可容纳的字数
    private int mOneLineTextNum;
    //列数
    private int mLineNum;

    public VerticalText(Context context) {
        this(context, null);
    }

    public VerticalText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15f, getResources().getDisplayMetrics());
        mLeftRightSpacing = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, getResources().getDisplayMetrics());
        mTopBottomSpacing = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, getResources().getDisplayMetrics());

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(mTextSize);

        //测量文字宽度
        mCharWidth = (int) mTextPaint.measureText("一");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);

        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
            setCalculatePaddingTopBottom();
        } else {
            if (!mSubStringStr.isEmpty()) {
                //有分割符号
                String[] split = mText.split(mSubStringStr);
                for (String s : split) {
                    mHeight = Math.max(mHeight, getPaddingTop() + getTotalHeight(s) + getPaddingBottom());
                }
            } else {
                mHeight = getPaddingTop() + getTotalHeight(mText) + getPaddingBottom();
            }
            //防止超出父容器
            mHeight = mHeight > hSize ? hSize : mHeight;
        }

        mFormatTexts.clear();
        if (!mSubStringStr.isEmpty()) {
            //有分割符号
            String[] split = mText.split(mSubStringStr);
            for (String s : split) {
                getFormatTexts(s);
            }
        } else {
            getFormatTexts(mText);
        }

        if (wMode == MeasureSpec.EXACTLY) {
            mWidth = wSize;
            setCalculatePaddingLeftRight();
        } else {
            if (!mSubStringStr.isEmpty()) {
                //有分割符号
                mWidth = (int) (getPaddingLeft() + mFormatTexts.size() * (mCharWidth + mLeftRightSpacing) - mLeftRightSpacing + getPaddingRight());
            } else {
                //控件可容纳内容的高度
                int num = getOneLineTextNum();
                //总共的列数
                int lineNum = getLineNum(num);
                mWidth = (int) (getPaddingLeft() + lineNum * (mCharWidth + mLeftRightSpacing) - mLeftRightSpacing + getPaddingRight());
            }
            mWidth = mWidth > wSize ? wSize : mWidth;
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 所有字符竖排的高度（包括与下一个文字的边距）
     *
     * @param str
     * @return
     */
    private int getTotalHeight(String str) {
        return (int) ((getCharHeight() + mTopBottomSpacing) * str.length() - mTopBottomSpacing);
    }

    /**
     * 单个字符的高度
     *
     * @return
     */
    private float getCharHeight() {
        if (mCharHeight == 0) {
            mCharHeight = Math.abs(mTextPaint.ascent());
        }
        return mCharHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = mWidth - getPaddingRight() - mCalculatePaddingLeftRight;
        float y;
        for (int i = 0; i < mFormatTexts.size(); i++) {
            //遍历写列表中的文字
            String thisLineText = mFormatTexts.get(i);
            x = i == 0 ? x - mCharWidth : x - mCharWidth - mLeftRightSpacing;
            //换行后，y轴坐标重置
            y = getCharHeight() + mCalculatePaddingTopBottom;
            for (int j = 0; j < thisLineText.length(); j++) {
                y = j == 0 ? y + getPaddingTop() : y + getCharHeight() + mTopBottomSpacing;
                //绘制text中 j 到 j+1 的字，y坐标是baseline的高度
                canvas.drawText(thisLineText, j, j + 1, x, y, mTextPaint);
            }
        }
    }

    /**
     * 根据列数分割每列要绘制的字符串
     *
     * @param str
     * @return
     */
    public List<String> getFormatTexts(String str) {
        //控件可容纳内容的高度
        int num = getOneLineTextNum();
        //总共的列数
        int lineNum = getLineNum(num);
        if (lineNum > 1) {
            //大于一列
            for (int i = 0; i <= str.length(); i += num) {
                if (i + num >= str.length()) {
                    //超出边界，直接切割至字符串末位
                    String substring = str.substring(i);
                    if (!substring.isEmpty()) {
                        mFormatTexts.add(substring);
                    }
                } else {
                    mFormatTexts.add(str.substring(i, i + num));
                }
            }
        } else {
            mFormatTexts.add(str);
        }
        return mFormatTexts;
    }

    /**
     * 获取列数
     *
     * @param num
     * @return
     */
    private int getLineNum(int num) {
        if (mLineNum == 0) {
            int result = mText.length() % num;
            mLineNum = result == 0 ? mText.length() / num : mText.length() / num + 1;
        }
        return mLineNum;
    }

    /**
     * 获得一列可容纳的文字数
     *
     * @return
     */
    private int getOneLineTextNum() {
        if (mOneLineTextNum == 0) {
            int oneLineHeight = mHeight - getPaddingTop() - getPaddingBottom();
            //粗略计算一列可容纳多少字
            mOneLineTextNum = (int) (oneLineHeight / getCharHeight());
            while ((int) (mOneLineTextNum * (getCharHeight() + mTopBottomSpacing) - mTopBottomSpacing) > oneLineHeight) {
                //有可能加上上下间距后超出可容纳内容高度
                mOneLineTextNum--;
            }
        }
        return mOneLineTextNum;
    }

    /**
     * 给定控件宽高情况下，计算每列可容纳文字数目后得出的上下边距
     */
    private void setCalculatePaddingTopBottom() {
        if (mCalculatePaddingTopBottom == 0) {
            mCalculatePaddingTopBottom = (mHeight - getPaddingTop() - getPaddingBottom() - (getOneLineTextNum() * (getCharHeight() + mTopBottomSpacing) - mTopBottomSpacing)) / 2;
        }
    }

    /**
     * 给定控件宽高情况下，计算列数后得出的左右边距
     */
    private void setCalculatePaddingLeftRight() {
        if (mCalculatePaddingLeftRight == 0) {
            mCalculatePaddingLeftRight = (mWidth - getPaddingLeft() - getPaddingRight() - (getLineNum(getOneLineTextNum()) * (mCharWidth + mLeftRightSpacing) - mLeftRightSpacing)) / 2;
        }
    }
}
