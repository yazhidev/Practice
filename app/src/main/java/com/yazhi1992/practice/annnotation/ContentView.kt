package com.yazhi1992.practice.annnotation

/**
 * Created by zengyazhi on 2017/8/23.
 */

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE) //可以修饰类、接口、枚举等
annotation class ContentView(val value: Int)
