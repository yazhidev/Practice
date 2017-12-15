package com.yazhi1992.practice.question_button;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by zengyazhi on 2017/9/4.
 */

public class OnTouchReturnTrueButton extends ViewGroup {

    public OnTouchReturnTrueButton(Context context) {
        super(context);
    }

    public OnTouchReturnTrueButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnTouchReturnTrueButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
//        return true;
    }
}
