package com.yazhi1992.practice.bezier_anim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yazhi1992.practice.R;
import com.yazhi1992.practice.wave_view.BezierWave;

public class BezierAnimActivity extends AppCompatActivity {

    private BezierAnimView mView;
    private BezierWave mMWaveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_anim);

        mView = (BezierAnimView) findViewById(R.id.bezierView);
        mMWaveView = (BezierWave) findViewById(R.id.bezierWave);

        findViewById(R.id.bezieBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mView.start();
                mMWaveView.start();
            }
        });
    }
}
