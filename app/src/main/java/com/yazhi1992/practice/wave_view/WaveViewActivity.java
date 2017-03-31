package com.yazhi1992.practice.wave_view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yazhi1992.practice.R;

public class WaveViewActivity extends AppCompatActivity {

    private WaveView mWave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_view);

        mWave = (WaveView) findViewById(R.id.waveView);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWave.start();
            }
        });
    }
}
