package com.yazhi1992.practice.proxy;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by zengyazhi on 2018/3/11.
 *
 * 动态代理
 */

public class AdDynamicProxy implements InvocationHandler{

    AD mCloth = new ClothAd();

    // 委托类和代理类的中介类，当我们调用代理类对象的方法时，这个“调用”会转送到invoke方法中，代理类对象作为proxy
    // 参数传入，参数method标识了我们具体调用的是代理类的哪个方法，args为这个方法的参数。这样一来，我们对代理类
    // 中的所有方法的调用都会变为对invoke的调用，这样我们可以在invoke方法中添加统一的处理逻辑(也可以根据method
    // 参数对不同的代理类方法做不同的处理)。
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //每个方法调用前都自定义一些行为
        Log.e("zyz", "动态代理添加：所有解释权归本商场 " + method.getName());
        return method.invoke(mCloth, args);
    }
}
