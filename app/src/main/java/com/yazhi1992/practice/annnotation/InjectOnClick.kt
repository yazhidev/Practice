package com.yazhi1992.practice.annnotation

/**
 * Created by zengyazhi on 2017/8/23.
 */

@Target(AnnotationTarget.FUNCTION) //修饰成员变量
annotation class InjectOnClick(val value: Int)