package com.yazhi1992.practice.line_chart_view;

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

import com.yazhi1992.practice.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    //x轴字体大小
    private float mXAxisTextSize;
    private float mYAxisTextSize;
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
    private float mStartX;
    private float mStartPaintX;
    //点击时，获得开始绘制的x坐标
    private float mActionDownPaintX;
    //手指拖动时x轴的偏移量
    private float mOffset;
    //x轴起始绘制点
    private float mOriginalX;
    private List<Region> mRegionList = new ArrayList<>();
    //点击的点在数组中的位置
    private int mClickNum = -1;
    //y轴的最大/最小刻度值
    private int mYAxisMaxValue;
    private int mYAxisMinValue;

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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        if (wMode == MeasureSpec.EXACTLY) {
//            mWidth = wSize;
        } else {
        }
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
        getYAxisTextWidth();
        postInvalidate();
    }

    /**
     * 获取y轴文字最大宽度与y轴最大、最小刻度值
     * 如果y轴是数值，即计算最大数值得宽度
     */
    private void getYAxisTextWidth() {
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
        int i = mMaxYValue / pow;
        mYAxisMaxValue = (i + 1) * pow;

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
                mYAxisMinValue = i2 * pow2;
            } else {
                //四舍
                mYAxisMinValue = (i2 - 1) * pow2;
            }
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
        mXAxisTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
        mYAxisTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
        mAxisColor = Color.BLACK;
        mAxisTextColor = Color.BLACK;
        mChartColor = Color.BLACK;
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
        mAxisTextPaint.setTextSize(mXAxisTextSize);
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
        //x刻度间隔
        float xDistance = Utils.dp2px(mContext, 40);

        //计算坐标轴值得宽高，确定顶点位置
        if (mMaxYWidth == 0 && mMaxYValue != 0) {
            //y坐标值宽度，与y坐标距离10dp
            mMaxYWidth = mAxisTextPaint.measureText(Integer.toString(mMaxYValue)) + Utils.dp2px(mContext, 10);
            //x坐标值高度，与x坐标距离10dp
            mXHeight = -mAxisTextPaint.ascent() + Utils.dp2px(mContext, 10);
            mOriginalX = mMaxYWidth + Utils.dp2px(mContext, 20);
            mActionDownPaintX = mOriginalX;
        }

        //绘制x轴
        canvas.drawLine(mMaxYWidth, mHeight - mXHeight, mWidth, mHeight - mXHeight, mAxisPaint);
        //绘制y轴
        canvas.drawLine(mMaxYWidth, mHeight - mXHeight, mMaxYWidth, 0, mAxisPaint);
        //刻度长度
        float length = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        //y刻度间隔
        float yDistance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        //总共多少刻度，空余10%高度
        int number = (int) ((mHeight - mXHeight) * 0.9 / yDistance);
        //y刻度值间隔，间距取整
        int yValue = (int) Math.ceil((mYAxisMaxValue - mYAxisMinValue) / number);
        //y轴使用到的高度(用于计算每个值所在高度时使用)
        float yUsedHeight = number * yDistance;
        float yUserValue = number * yValue;
        //绘制y轴最小刻度值
        canvas.drawText(Integer.toString(mYAxisMinValue), 0 + mAxisTextPaint.measureText(Integer.toString(mMaxYValue)) - mAxisTextPaint.measureText(Integer.toString(mYAxisMinValue)), mHeight - mXHeight + mAxisWidth / 2 - mAxisTextPaint.ascent() / 2, mAxisTextPaint);
        for (int i = 1; i <= number; i++) {
            //绘制y轴刻度
            canvas.drawLine(mMaxYWidth, mHeight - mXHeight - yDistance * i + mAxisWidth / 2, mMaxYWidth + length, mHeight - mXHeight - yDistance * i + mAxisWidth / 2, mAxisPaint);
            //绘制y轴刻度值
            int yNumber = mYAxisMinValue + yValue * i;
            canvas.drawText(Integer.toString(yNumber), 0 + mAxisTextPaint.measureText(Integer.toString(mMaxYValue)) - mAxisTextPaint.measureText(Integer.toString(yNumber)), mHeight - mXHeight - yDistance * i + mAxisWidth / 2 - mAxisTextPaint.ascent() / 2, mAxisTextPaint);
        }

        //开始绘制的第一个点的x坐标
        mStartPaintX = mActionDownPaintX + mOffset;
        if (mStartPaintX > mOriginalX) {
            //拖到最左边，不允许继续拖动
            mStartPaintX = mOriginalX;
        } else if (mStartPaintX < mOriginalX - (mValueList.size() - mWidth / xDistance + 1) * xDistance) {
            //拖到最右边，不允许继续拖动
            mStartPaintX = mOriginalX - (mValueList.size() - mWidth / xDistance + 1) * xDistance;
        }
        float startPaintX = mStartPaintX;
        mRegionList.clear();
        for (int i = 0; i < mValueList.size(); i++) {
            ValueBean valueBean = mValueList.get(i);
            if (startPaintX > 0 && startPaintX < mWidth && startPaintX >= mOriginalX) {
                //绘制x轴刻度
                canvas.drawLine(startPaintX, mHeight - mXHeight, startPaintX, mHeight - mXHeight - length, mAxisPaint);
                //绘制x轴刻度值
                String xValue = valueBean.getXValue();
                canvas.drawText(xValue, startPaintX - mAxisTextPaint.measureText(xValue) / 2, mHeight, mAxisTextPaint);
            }
            //构造折线path
            Region region = new Region();
            Path path = new Path();
            //点20dp范围内可点击
            path.addCircle(startPaintX, mHeight - mXHeight - (valueBean.getYValue() - mYAxisMinValue) / yUserValue * yUsedHeight, Utils.dp2px(mContext, 20), Path.Direction.CW);
            region.setPath(path, new Region(0, 0, mWidth, mHeight));
            mRegionList.add(region);
            if (i == 0) {
                mChartPath.reset();
                mChartPath.moveTo(startPaintX, mHeight - mXHeight - (valueBean.getYValue() - mYAxisMinValue) / yUserValue * yUsedHeight);
            } else {
                mChartPath.lineTo(startPaintX, mHeight - mXHeight - (valueBean.getYValue() - mYAxisMinValue) / yUserValue * yUsedHeight);
            }
            startPaintX += xDistance;
        }

        //新开图层
        int layerId = canvas.saveLayer(0, 0, mWidth, mHeight, null, Canvas.ALL_SAVE_FLAG);
        if (mClickNum != -1) {
            //点击了该点，绘制数值
            canvas.drawText(Integer.toString(mValueList.get(mClickNum).getYValue()), mStartPaintX + xDistance * mClickNum - mAxisTextPaint.measureText(Integer.toString(mValueList.get(mClickNum).getYValue())) / 2, mHeight - mXHeight - (mValueList.get(mClickNum).getYValue() - mYAxisMinValue) / yUserValue * yUsedHeight - Utils.dp2px(mContext, 15), mAxisTextPaint);
        }
        //绘制折线
        mChartPaint.setXfermode(null);
        mChartPaint.setColor(mChartColor);
        mChartPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mChartPath, mChartPaint);
        // 将折线超出x轴坐标的部分截取掉
        mChartPaint.setStyle(Paint.Style.FILL);
        mChartPaint.setColor(Color.TRANSPARENT);
        mChartPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        RectF rectF = new RectF(0, 0, mMaxYWidth, mHeight);
        canvas.drawRect(rectF, mChartPaint);
        //保存图层
        canvas.restoreToCount(layerId);
    }

    // TODO: 17/4/11 滑动 惯性

    // TODO: 17/4/11 和scrollview一起的问题

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mActionDownPaintX = mStartPaintX;
                mOffset = 0;
                mStartX = event.getX();
                //判断是否点击了折线点
                for (int i = 0; i < mRegionList.size(); i++) {
                    Region region = mRegionList.get(i);
                    if (region.contains((int) (event.getX()), (int) (event.getY()))) {
                        mClickNum = i;
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
}
