package com.yazhi1992.practice.immersion_status_bar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yazhi1992.practice.R;

/**
 * 沉浸式图片
 */
public class ImmersionImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immersion_image);

        StatusBarUtils.setStatusTransparentBySetFullScreen(this);
    }
}
