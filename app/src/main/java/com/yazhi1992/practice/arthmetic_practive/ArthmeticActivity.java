package com.yazhi1992.practice.arthmetic_practive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yazhi1992.practice.R;

public class ArthmeticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arthmetic);

        findViewById(R.id.arthmetic_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择排序
                int[] array = new int[]{2, 8, 12, 4, 10, 25, 7, 9};
                //选择排序，每一轮都选出最小的数并排在该轮的头部
                for (int i = 0; i < (array.length - 1); i++) {
                    int temp = array[i];
                    int tempIndex = i;
                    for (int j = (i + 1); j < array.length; j++) {
                        if (array[j] < temp) {
                            temp = array[j];
                            tempIndex = j;
                        }
                    }
                    //一轮比较完，将最小值排在该轮的头部
                    array[tempIndex] = array[i];
                    array[i] = temp;
                }

                for (int i = 0; i < array.length; i++) {
                    Log.e("zyz", "" + array[i]);
                }
            }
        });
    }
}
