package com.yazhi1992.practice.proxy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yazhi1992.practice.R;

import java.lang.reflect.Proxy;

public class ProxyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);

        //静态代理
        ADStaticProxy adStaticProxy = new ADStaticProxy();
        adStaticProxy.slogan("静态：广告 价格：" + adStaticProxy.getPrice());


        AdDynamicProxy adDynamicProxy = new AdDynamicProxy();
        AD ad = (AD) Proxy.newProxyInstance(AD.class.getClassLoader(), new Class[]{AD.class}, adDynamicProxy);
        ad.slogan("动态：广告 价格：" + ad.getPrice());

    }
}
