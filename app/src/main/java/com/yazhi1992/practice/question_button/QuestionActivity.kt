package com.yazhi1992.practice.question_button

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import com.yazhi1992.practice.R

class QuestionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        findViewById(R.id.dp_practive).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@QuestionActivity, SizeActivity::class.java))
            }
        })

        findViewById(R.id.onTouch_practive).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@QuestionActivity, OnTouchActivity::class.java))
            }
        })

        findViewById(R.id.arthmetic_practive).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val array1 = intArrayOf(1, 3, 8)
                val array2 = intArrayOf(2, 3, 4, 7, 9)

                val max = Math.max(array1.size, array2.size)

                var newArray = IntArray(array1.size + array2.size)
                var i = 0
                var j = 0
                var newIndex = 0

                while (i < array1.size && j < array2.size) {
                    if (array1[i] > array2[j]) {
                        newArray[newIndex++] = array2[j++]
                    } else {
                        newArray[newIndex++] = array1[i++]
                    }
                }

                while (i < array1.size) {
                    newArray[newIndex++] = array1[i++]
                }

                while (j < array2.size) {
                    newArray[newIndex++] = array2[j++]
                }

//                while (newIndex < newArray.size) {
//                    var value1 = -1
//                    var value2 = -1
//                    if (i < array1.size) {
//                        //未超出
//                        value1 = array1[i]
//                    } else {
//                        //有一个数组已添加完，另一个数组直接全部添加进新数组
//                        newArray[newIndex] = array2[j]
//                        j++
//                        newIndex++
//                        continue
//                    }
//                    if (j < array2.size) {
//                        //未超出
//                        value2 = array2[j]
//                    } else {
//                        //有一个数组已添加完，另一个数组直接全部添加进新数组
//                        newArray[newIndex] = array1[i]
//                        i++
//                        newIndex++
//                        continue
//                    }
//                    if (value1 != -1 && value2 != -1) {
//
//                    }
//                }

                for (i in 0..(newArray.size - 1)) {
                    Log.e("zyz", "" + newArray[i])
                }
            }
        })

        findViewById(R.id.arthmetic_practive_2).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val array = intArrayOf(2, 8, 12, 4, 10, 25, 7, 9)

                //选择排序，每一轮都选出最小的数并排在该轮的头部

                for (i in 0..(array.size - 2)) {
                    var temp = array[i]
                    var tempIndex = i
                    for (j in (i + 1)..(array.size-1)) {
                        if(array[j] < temp) {
                            //保存较小值的数值和位置
                            temp = array[j]
                            tempIndex = j
                        }
                    }
                    //一轮比较完，将最小值排在该轮的头部
                    array[tempIndex] = array[i]
                    array[i] = temp
                }

                for (i in 0..(array.size - 1)) {
                    Log.e("zyz", "" + array[i])
                }
            }
        })
    }
}
