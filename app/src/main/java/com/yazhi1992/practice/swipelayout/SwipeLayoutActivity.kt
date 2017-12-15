package com.yazhi1992.practice.swipelayout

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.yazhi1992.practice.R
import kotlinx.android.synthetic.main.activity_swipe_layout.*

class SwipeLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_layout)

        tv_content.setOnClickListener(View.OnClickListener { Toast.makeText(this, "内容", Toast.LENGTH_SHORT).show() })
        tvMenu1.setOnClickListener(View.OnClickListener { Toast.makeText(this, "菜单1", Toast.LENGTH_SHORT).show() })
        tvMenu2.setOnClickListener(View.OnClickListener { Toast.makeText(this, "菜单2", Toast.LENGTH_SHORT).show() })
    }
}
