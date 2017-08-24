package com.yazhi1992.practice.annnotation

/**
 * Created by zengyazhi on 2017/8/23.
 */

@Target(AnnotationTarget.FIELD) //修饰成员变量
annotation class InjectView(val value: Int)