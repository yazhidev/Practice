package com.yazhi1992.practice.annnotation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.yazhi1992.practice.R

@ContentView(R.layout.activity_test_annotation)
class TestAnnotationActivity : AppCompatActivity() {

    @InjectView(R.id.tv_test)
    lateinit var tv:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewIndectUtils.inject(this)

        tv.text = "修改文字"
    }

    @InjectOnClick(R.id.tv_test)
    private fun clickTv(v: View) {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        tv.text = "点击文字"
    }
}
