package com.yazhi1992.practice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.yazhi1992.practice.annnotation.TestAnnotationActivity;
import com.yazhi1992.practice.bezier_anim.BezierAnimActivity;
import com.yazhi1992.practice.eleme_button.ElemeBtnActivity;
import com.yazhi1992.practice.hencoder.HenCoder;
import com.yazhi1992.practice.hook_icon.HookIconActivity;
import com.yazhi1992.practice.immersion_status_bar.MainImmersionActivity;
import com.yazhi1992.practice.line_chart_view.LineChartActivity;
import com.yazhi1992.practice.ripple_view.RippleActivity;
import com.yazhi1992.practice.rotate_circle.RotateCircleActivity;
import com.yazhi1992.practice.vertical_text.VerticalTextActivity;
import com.yazhi1992.practice.wave_view.WaveViewActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.elemeBtn).setOnClickListener(this);
        findViewById(R.id.immersionStatusBarBtn).setOnClickListener(this);
        findViewById(R.id.rotateBtn).setOnClickListener(this);
        findViewById(R.id.waveBtn).setOnClickListener(this);
        findViewById(R.id.verticalTextBtn).setOnClickListener(this);
        findViewById(R.id.bezierBtn).setOnClickListener(this);
        findViewById(R.id.rippleBtn).setOnClickListener(this);
        findViewById(R.id.lineChart).setOnClickListener(this);
        findViewById(R.id.wheelView).setOnClickListener(this);
        findViewById(R.id.alipayHookIconBtn).setOnClickListener(this);
        findViewById(R.id.hencoder_practive).setOnClickListener(this);
        findViewById(R.id.annotation_test).setOnClickListener(this);
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
            //波浪效果
            startActivity(new Intent(this, WaveViewActivity.class));
            break;
        case R.id.verticalTextBtn:
            //竖排文字
            startActivity(new Intent(this, VerticalTextActivity.class));
            break;
        case R.id.bezierBtn:
            //贝塞尔动画
            startActivity(new Intent(this, BezierAnimActivity.class));
            break;
        case R.id.rippleBtn:
            //涟漪
            startActivity(new Intent(this, RippleActivity.class));
            break;
        case R.id.lineChart:
            //折线图
            startActivity(new Intent(this, LineChartActivity.class));
            break;
        case R.id.wheelView:
            //折线图
            startActivity(new Intent(this, LineChartActivity.class));
            break;
        case R.id.alipayHookIconBtn:
            //支付宝支付成功打钩动画
            startActivity(new Intent(this, HookIconActivity.class));
            break;
        case R.id.immersionStatusBarBtn:
            //沉浸式状态栏
            startActivity(new Intent(this, MainImmersionActivity.class));
            break;
        case R.id.hencoder_practive:
            //HenCoder 练习代码
            startActivity(new Intent(this, HenCoder.class));
            break;
        case R.id.annotation_test:
            //注解小 demo
            startActivity(new Intent(this, TestAnnotationActivity.class));
            break;
        default:
            break;
        }
    }
}
