package com.yazhi1992.practice.line_chart_view;

/**
 * Created by zengyazhi on 17/4/11.
 *
 * 数值model
 */

public class ValueBean {
    private String mXValue;
    private int mYValue;

    public ValueBean(String XValue, int YValue) {
        mXValue = XValue;
        mYValue = YValue;
    }

    public String getXValue() {
        return mXValue;
    }

    public void setXValue(String XValue) {
        mXValue = XValue;
    }

    public int getYValue() {
        return mYValue;
    }

    public void setYValue(int YValue) {
        mYValue = YValue;
    }
}
