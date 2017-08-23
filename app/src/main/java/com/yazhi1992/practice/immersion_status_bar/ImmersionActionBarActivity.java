package com.yazhi1992.practice.immersion_status_bar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yazhi1992.practice.R;


public class ImmersionActionBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immersion_action_bar);

        StatusBarUtils.with(this)
                .setIsActionBar(true)
                .clearActionBarShadow()
                .setDrawable(getResources().getDrawable(R.drawable.shape))
                .init();

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape));
    }
}
