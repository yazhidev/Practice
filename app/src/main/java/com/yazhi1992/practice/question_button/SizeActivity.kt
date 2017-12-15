package com.yazhi1992.practice.question_button

import android.graphics.Point
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.TextView

import com.yazhi1992.practice.R

class SizeActivity : AppCompatActivity() {

    lateinit var mTvSize: TextView
    lateinit var mTvInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_size)

        mTvSize = findViewById(R.id.tv_size) as TextView
        mTvInfo = findViewById(R.id.tv_info) as TextView

        mTvSize.post(object : Runnable {
            override fun run() {
                mTvSize.text = "width:${mTvSize.width}"
            }
        })

        var point = Point()
        val stringBuffer = StringBuffer()

        val defaultDisplay = windowManager.defaultDisplay
        defaultDisplay.getSize(point)
        stringBuffer.append("defaultDisplay.getSize() ：")
        stringBuffer.append("\n     w: ${point.x} h: ${point.y}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            defaultDisplay.getRealSize(point)
            stringBuffer.append("\n\ndefaultDisplay.getRealSize() (API >= 17) ：")
            stringBuffer.append("\n     point.x: ${point.x} point.y: ${point.y}")
        }

        val dm = DisplayMetrics()
        defaultDisplay.getMetrics(dm)
        stringBuffer.append("\n\ndisplayMetrics.density ： ${dm.density}")
        stringBuffer.append("\n\ndefaultDisplay.getMetrics() ：")
        stringBuffer.append("\n     widthPixels: ${dm.widthPixels} heightPixels: ${dm.heightPixels}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            defaultDisplay.getRealMetrics(dm)
            stringBuffer.append("\n\ndefaultDisplay.getRealMetrics() (API >= 17) ：")
            stringBuffer.append("\n     widthPixels: ${dm.widthPixels} heightPixels: ${dm.heightPixels}")
        }

        val displayMetrics = application.resources.displayMetrics
        stringBuffer.append("\n\ngetResources().getDisplayMetrics ：")
        stringBuffer.append("\n     widthPixels: ${displayMetrics.widthPixels} heightPixels: ${displayMetrics.heightPixels}")


        mTvInfo.text = stringBuffer.toString()
    }
}
