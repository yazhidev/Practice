package com.yazhi1992.practice.annnotation

import android.app.Activity
import android.view.View
import java.lang.reflect.Proxy

/**
 * Created by zengyazhi on 2017/8/23.
 */

object ViewIndectUtils {
    fun inject(activity: Activity) {
        injectContentView(activity)
        injectView(activity)
        injectOnclick(activity)
    }

    private fun injectContentView(activity: Activity) {
        val clazz = activity.javaClass
        //拿到 activity 的 ContentView 注解
        val contentView = clazz.getAnnotation(ContentView::class.java)
        if (contentView != null) {
            //如果这个activity上面存在这个注解的话，就取出这个注解对应的value值，其实就是前面说的布局文件。
            val layoutId = contentView.value
            //反射
            try {
                val method = clazz.getMethod("setContentView", Int::class.javaPrimitiveType)
                method.invoke(activity, layoutId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
//            activity.setContentView(layoutId)
        }
    }

    private fun injectView(activity: Activity) {
        val clazz = activity.javaClass
        //activity 的所有成员变量
        for (field in clazz.declaredFields) {
            val injectView = field.getAnnotation(InjectView::class.java)
            if (injectView != null) {
                try {
                    val findViewByIdMethod = clazz.getMethod("findViewById", Int::class.javaPrimitiveType)
                    val view = findViewByIdMethod.invoke(activity, injectView.value)
                    field.set(activity, view)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
//                val view = activity.findViewById(injectView.value)
//                field.set(activity, view)
            }
        }
    }

    private fun injectOnclick(activity: Activity) {
        val clazz = activity.javaClass
        for (method2 in clazz.declaredMethods) {
            val injectClick = method2.getAnnotation(InjectOnClick::class.java)
            if (injectClick != null) {
                method2.isAccessible = true
                val clickListener = Proxy.newProxyInstance(View.OnClickListener::class.java.classLoader
                        , arrayOf<Class<*>>(View.OnClickListener::class.java)) { proxy, method, args ->  method2.invoke(activity, *args)}
                try {
                    val findViewByIdMethod = clazz.getMethod("findViewById", Int::class.javaPrimitiveType)
                    val view = findViewByIdMethod.invoke(activity, injectClick.value)
                    val setOnClickListenerMethod = view.javaClass.getMethod("setOnClickListener", View.OnClickListener::class.java)
                    setOnClickListenerMethod.invoke(view, clickListener)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }
}
