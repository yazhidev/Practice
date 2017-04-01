package com.yazhi1992.practice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yazhi1992.practice.eleme_button.ElemeBtnActivity;
import com.yazhi1992.practice.rotate_circle.RotateCircleActivity;
import com.yazhi1992.practice.vertical_text.VerticalTextActivity;
import com.yazhi1992.practice.wave_view.WaveViewActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.elemeBtn).setOnClickListener(this);
        findViewById(R.id.rotateBtn).setOnClickListener(this);
        findViewById(R.id.waveBtn).setOnClickListener(this);
        findViewById(R.id.verticalTextBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.elemeBtn:
            //饿了么购物车按钮
            startActivity(new Intent(this, ElemeBtnActivity.class));
            break;
        case R.id.rotateBtn:
            //饿了么购物车按钮
            startActivity(new Intent(this, RotateCircleActivity.class));
            break;
        case R.id.waveBtn:
            //水波纹
            startActivity(new Intent(this, WaveViewActivity.class));
            break;
        case R.id.verticalTextBtn:
            //竖排文字
            startActivity(new Intent(this, VerticalTextActivity.class));
            break;
        default:
            break;
        }
    }
}
