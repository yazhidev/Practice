package com.yazhi1992.practice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yazhi1992.practice.eleme_button.ElemeBtnActivity;
import com.yazhi1992.practice.rotate_circle.RotateCircleActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.elemeBtn).setOnClickListener(this);
        findViewById(R.id.rotateBtn).setOnClickListener(this);
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
        default:
            break;
        }
    }
}
