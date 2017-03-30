package com.yazhi1992.practice.rotate_circle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yazhi1992.practice.R;

public class RotateCircleActivity extends AppCompatActivity {

    private RotateCircleView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate_circle);

        mView = (RotateCircleView) findViewById(R.id.rotateCircelView);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.star();
            }
        });
    }
}
