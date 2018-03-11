package com.yazhi1992.practice.proxy;

import android.util.Log;

/**
 * Created by zengyazhi on 2018/3/11.
 *
 * 静态代理
 */

public class ADStaticProxy implements AD {

    private AD mClothAd = new ClothAd(); //静态代理类直接持有为委托类

    @Override
    public void slogan(String word) {
        //使用代理，可以在行为前后添加自定义的行为
        Log.e("zyz", "静态代理添加：广告可以跳过");
        mClothAd.slogan(word);
    }

    @Override
    public int getPrice() {
        //但静态代理的缺点：如果类方法数量越来越多的时候，代理类的代码量是十分庞大的。
        Log.e("zyz", "静态代理添加：统统8折");
        return mClothAd.getPrice();
    }
}
