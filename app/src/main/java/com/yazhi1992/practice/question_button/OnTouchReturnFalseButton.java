package com.yazhi1992.practice.question_button;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by zengyazhi on 2017/9/4.
 */

public class OnTouchReturnFalseButton extends Button {

    public OnTouchReturnFalseButton(Context context) {
        super(context);
    }

    public OnTouchReturnFalseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnTouchReturnFalseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        return false;
    }
}
