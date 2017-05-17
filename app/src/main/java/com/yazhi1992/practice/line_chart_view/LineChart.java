package com.yazhi1992.practice.line_chart_view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.yazhi1992.practice.R;
import com.yazhi1992.practice.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengyazhi on 17/4/11.
 * <p>
 * 折线图
 * 参考：https://github.com/xiaoyunfei/LineChart
 */

public class LineChart extends View {

    //屏幕宽高
    private int mWidth;
    private int mHeight;
    //坐标轴字体大小
    private float mAxisTextSize;
    //坐标轴颜色
    private int mAxisColor;
    //坐标轴文字颜色
    private int mAxisTextColor;
    //折线颜色
    private int mChartColor;
    //绘制坐标轴画笔
    private Paint mAxisPaint;
    //绘制坐标轴文字画笔
    private Paint mAxisTextPaint;
    //绘制折线画笔
    private Paint mChartPaint;
    //坐标轴画笔宽度
    private int mAxisWidth;
    //数值
    private List<ValueBean> mValueList = new ArrayList<>();
    private int mMaxYValue;
    private int mMinYValue;
    //y轴坐标值最大宽度
    private float mMaxYWidth;
    //x轴坐标值高度
    private float mXHeight;
    private Context mContext;
    private Path mChartPath;
    //action_down时的横坐标
    private float mStartX;
    //绘制起始点的坐标
    private float mStartPaintX;
    //action_down时，开始绘制的x坐标
    private float mActionDownPaintX;
    //手指拖动时x轴的偏移量
    private float mOffset;
    //x轴起始绘制点
    private float mOriginalX;
    //折线点的点击范围
    private Map<Integer, Region> mRegionMap = new HashMap<>();
    //点击的点在数组中的位置
    private int mClickNum = -1;
    //y轴的最大/最小刻度值
    private int mYAxisMaxValue;
    private int mYAxisMinValue;
    //x刻度间隔
    private float mXDistance;
    //Y轴的高度与值得范围
    private float mYUsedHeight;
    private float mYUserValue;
    //刻度长度
    private float mLength;
    //每次ondraw绘制遍历时判断path是否已重置
    private boolean mReMoveTo;
    private ValueAnimator mValueAnimator;

    public LineChart(Context context) {
        this(context, null);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 设置数值
     *
     * @param valueList
     */
    public void setValueList(List<ValueBean> valueList) {
        mValueList = valueList;
        mMaxYValue = 0;
        mMinYValue = 0;
        calculateDrawValue();
        postInvalidate();
        startAnim();
    }

    /**
     * 计算绘制需要的一些值
     * y轴文字最大宽度、y轴最大、最小刻度值
     * 如果y轴是数值，即计算最大数值的宽度
     */
    private void calculateDrawValue() {
        if (mMaxYValue == 0) {
            for (int i = 0; i < mValueList.size(); i++) {
                ValueBean valueBean = mValueList.get(i);
                mMaxYValue = Math.max(mMaxYValue, valueBean.getYValue());
                if (i == 0) {
                    mMinYValue = valueBean.getYValue();
                } else {
                    mMinYValue = Math.min(mMinYValue, valueBean.getYValue());
                }
            }
        }

        //对y轴最大最小值的换算，以免都使用0为起始刻度，使得在数值较大时几乎就是一条直线
        String s = Integer.toString(mMaxYValue);
        //几位数
        int length = s.length();
        int pow = (int) Math.pow(10, length - 2);
        double d1 = mMaxYValue * 1d / pow;
        int newi1 = mMaxYValue / pow;
        BigDecimal bigDecimal1 = new BigDecimal(d1).setScale(0, BigDecimal.ROUND_HALF_UP);
        if (newi1 < bigDecimal1.doubleValue()) {
            //五入
            mYAxisMaxValue = (newi1 + 1) * pow;
        } else {
            //四舍
            int newPow = pow / 10;
            int newI = mMaxYValue / newPow;
            mYAxisMaxValue = (newI + 1) * newPow;
        }

        String s2 = Integer.toString(mMinYValue);
        //几位数
        int length2 = s2.length();
        if (length2 <= 2) {
            mYAxisMinValue = 0;
        } else {
            int pow2 = (int) Math.pow(10, length2 - 2);
            double d2 = mMinYValue * 1d / pow2;
            int i2 = mMinYValue / pow2;
            BigDecimal bigDecimal = new BigDecimal(d2).setScale(0, BigDecimal.ROUND_HALF_UP);
            if (i2 < bigDecimal.doubleValue()) {
                //五入
                int pow3 = pow2 / 10;
                int i3 = mMaxYValue / pow3;
                mYAxisMinValue = (i3 - 1) * pow3;
            } else {
                //四舍
                mYAxisMinValue = i2 * pow2;
            }
        }

        //计算坐标轴值得宽高，确定顶点位置
        if (mMaxYWidth == 0 && mMaxYValue != 0) {
            //y坐标值宽度，与y坐标距离10dp
            mMaxYWidth = mAxisTextPaint.measureText(Integer.toString(mMaxYValue)) + Utils.dp2px(mContext, 10);
            //x坐标值高度，与x坐标距离10dp
            mXHeight = -mAxisTextPaint.ascent() + Utils.dp2px(mContext, 10);
            mOriginalX = mMaxYWidth;
            mActionDownPaintX = mOriginalX;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private void init(Context context) {
        mContext = context;
        mChartPath = new Path();
        initDefaultValue();
        initPaint();
    }

    /**
     * 初始化默认值
     */
    private void initDefaultValue() {
        mAxisTextSize = Utils.dp2px(mContext, 12);
        mXDistance = Utils.dp2px(mContext, 40);
        mChartColor = mContext.getResources().getColor(R.color.my_color);
        mAxisColor = Color.BLACK;
        mAxisTextColor = Color.BLACK;
        mAxisWidth = 5;
    }

    private void initPaint() {
        mAxisPaint = new Paint();
        mAxisPaint.setAntiAlias(true);
        mAxisPaint.setStrokeWidth(mAxisWidth);
        mAxisPaint.setColor(mAxisColor);
        mAxisPaint.setStrokeCap(Paint.Cap.ROUND);

        mAxisTextPaint = new Paint();
        mAxisTextPaint.setAntiAlias(true);
        mAxisTextPaint.setTextSize(mAxisTextSize);
        mAxisTextPaint.setColor(mAxisTextColor);
        mAxisTextPaint.setStrokeCap(Paint.Cap.ROUND);

        mChartPaint = new Paint();
        mChartPaint.setAntiAlias(true);
        mChartPaint.setStrokeWidth(mAxisWidth);
        mChartPaint.setStrokeCap(Paint.Cap.ROUND);
        mChartPaint.setColor(mChartColor);
        mChartPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAxis(canvas);
        drawYAxis(canvas);
        drawXAxisAndLine(canvas);
    }

    /**
     * 绘制x轴与折线
     *
     * @param canvas
     */
    private void drawXAxisAndLine(Canvas canvas) {
        //开始绘制的第一个点的x坐标
        mStartPaintX = mActionDownPaintX + mOffset;
        if (mStartPaintX > mOriginalX) {
            //拖到最左边，不允许继续往右拖动
            mStartPaintX = mOriginalX;
        } else if (mStartPaintX < mOriginalX - (mValueList.size() - mWidth / mXDistance + 1) * mXDistance) {
            //拖到最右边，不允许继续往左拖动
            mStartPaintX = mOriginalX - (mValueList.size() - mWidth / mXDistance + 1) * mXDistance;
            if (mValueAnimator != null && mValueAnimator.isRunning()) {
                mValueAnimator.cancel();
            }
        }
        float startPaintX = mStartPaintX;
        mRegionMap.clear();
        mReMoveTo = false;
        mChartPath.reset();
        for (int i = 0; i < mValueList.size(); i++) {
            if (startPaintX < -mXDistance) {
                startPaintX += mXDistance;
                continue;
            }
            ValueBean valueBean = mValueList.get(i);
            if (startPaintX > 0 && startPaintX < mWidth && startPaintX >= mOriginalX) {
                //绘制x轴刻度
                canvas.drawLine(startPaintX, mHeight - mXHeight, startPaintX, mHeight - mXHeight - mLength, mAxisPaint);
                //绘制x轴刻度值
                String xValue = valueBean.getXValue();
                canvas.drawText(xValue, startPaintX - mAxisTextPaint.measureText(xValue) / 2, mHeight, mAxisTextPaint);
            }
            //构造折线path
            Path path = new Path();
            //点20dp范围内可点击
            path.addCircle(startPaintX, mHeight - mXHeight - (valueBean.getYValue() - mYAxisMinValue) / mYUserValue * mYUsedHeight, Utils.dp2px(mContext, 20), Path.Direction.CW);
            if (!mValueAnimator.isRunning()) {
                //动画停止时才计算点的点击范围
                Region region = new Region();
                region.setPath(path, new Region(0, 0, mWidth, mHeight));
                mRegionMap.put(i, region);
            }
            if (mReMoveTo) {
                mChartPath.moveTo(startPaintX, mHeight - mXHeight - (valueBean.getYValue() - mYAxisMinValue) / mYUserValue * mYUsedHeight);
                mReMoveTo = true;
            } else {
                mChartPath.lineTo(startPaintX, mHeight - mXHeight - (valueBean.getYValue() - mYAxisMinValue) / mYUserValue * mYUsedHeight);
            }
            startPaintX += mXDistance;
            if (startPaintX > mWidth + mXDistance) {
                //只绘制屏幕内的内容
                break;
            }
        }
        //新开图层
        int layerId = canvas.saveLayer(0, 0, mWidth, mHeight, null, Canvas.ALL_SAVE_FLAG);
        if (mClickNum != -1) {
            //点击了该点，绘制数值
            canvas.drawText(Integer.toString(mValueList.get(mClickNum).getYValue()), mStartPaintX + mXDistance * mClickNum - mAxisTextPaint.measureText(Integer.toString(mValueList.get(mClickNum).getYValue())) / 2, mHeight - mXHeight - (mValueList.get(mClickNum).getYValue() - mYAxisMinValue) / mYUserValue * mYUsedHeight - Utils.dp2px(mContext, 15), mAxisTextPaint);
        }
        //绘制折线
        mChartPaint.setXfermode(null);
        mChartPaint.setColor(mChartColor);
        mChartPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mChartPath, mChartPaint);
        //将折线超出x轴坐标的部分截取掉
        mChartPaint.setStyle(Paint.Style.FILL);
        mChartPaint.setColor(Color.TRANSPARENT);
        mChartPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        RectF rectF = new RectF(0, 0, mMaxYWidth, mHeight);
        canvas.drawRect(rectF, mChartPaint);
        //保存图层
        canvas.restoreToCount(layerId);
    }

    /**
     * 绘制y轴刻度与刻度值
     *
     * @param canvas
     */
    private void drawYAxis(Canvas canvas) {
        mLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        //y刻度间隔
        float yDistance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        //总共多少刻度，空余10%高度
        int number = (int) ((mHeight - mXHeight) * 0.9 / yDistance);
        //y刻度值间隔，间距取整
        int yValue = (int) Math.ceil((mYAxisMaxValue - mYAxisMinValue) / number);
        //y轴使用到的高度(用于计算每个值所在高度时使用)
        mYUsedHeight = number * yDistance;
        mYUserValue = number * yValue;
        //绘制y轴最小刻度值
        canvas.drawText(Integer.toString(mYAxisMinValue), 0 + mAxisTextPaint.measureText(Integer.toString(mMaxYValue)) - mAxisTextPaint.measureText(Integer.toString(mYAxisMinValue)), mHeight - mXHeight + mAxisWidth / 2 - mAxisTextPaint.ascent() / 2, mAxisTextPaint);
        for (int i = 1; i <= number; i++) {
            //绘制y轴刻度
            canvas.drawLine(mMaxYWidth, mHeight - mXHeight - yDistance * i + mAxisWidth / 2, mMaxYWidth + mLength, mHeight - mXHeight - yDistance * i + mAxisWidth / 2, mAxisPaint);
            //绘制y轴刻度值
            int yNumber = mYAxisMinValue + yValue * i;
            canvas.drawText(Integer.toString(yNumber), 0 + mAxisTextPaint.measureText(Integer.toString(mMaxYValue)) - mAxisTextPaint.measureText(Integer.toString(yNumber)), mHeight - mXHeight - yDistance * i + mAxisWidth / 2 - mAxisTextPaint.ascent() / 2, mAxisTextPaint);
        }
    }

    /**
     * 绘制坐标轴
     *
     * @param canvas
     */
    private void drawAxis(Canvas canvas) {
        //绘制x轴
        canvas.drawLine(mMaxYWidth, mHeight - mXHeight, mWidth, mHeight - mXHeight, mAxisPaint);
        //绘制y轴
        canvas.drawLine(mMaxYWidth, mHeight - mXHeight, mMaxYWidth, 0, mAxisPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mActionDownPaintX = mStartPaintX;
                mOffset = 0;
                mStartX = event.getX();
                //判断是否点击了折线点
                for (Map.Entry<Integer, Region> entry : mRegionMap.entrySet()) {
                    if (entry.getValue().contains((int) (event.getX()), (int) (event.getY()))) {
                        mClickNum = entry.getKey();
                        postInvalidate();
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                mOffset = x - mStartX;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    public void startAnim() {
        //最大移动距离
        float maxOffset = -(mValueList.size() - mWidth / mXDistance + 1) * mXDistance;
        mValueAnimator = ValueAnimator.ofFloat(0, maxOffset);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mValueAnimator.setDuration((long) (-maxOffset * 1.5));
        mValueAnimator.start();
    }
}
