package com.yazhi1992.practice.question_button

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import com.yazhi1992.practice.R

class OnTouchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_touch)

//        findViewById(R.id.btn1).setOnTouchListener { v, event ->  true}
        findViewById(R.id.btn1).setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                Log.e("zyz", "btn1")
            }
        })

//        findViewById(R.id.btn2).setOnTouchListener { v, event ->  true}
        findViewById(R.id.btn2).setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                Log.e("zyz", "btn2")
            }
        })

//        findViewById(R.id.btn3).setOnTouchListener { v, event ->  true}
        findViewById(R.id.btn3).setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                Log.e("zyz", "btn3")
            }
        })
//        findViewById(R.id.btn4).setOnTouchListener { v, event ->  true}
        findViewById(R.id.btn4).setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                Log.e("zyz", "btn4")
            }
        })
    }
}
