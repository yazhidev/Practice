package com.yazhi1992.practice.flowlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yazhi1992.practice.R;

import java.util.ArrayList;
import java.util.List;

public class FlowLayoutActivity extends AppCompatActivity {

    List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout);

        data.add("[a123123");
        data.add("[a12312dfasdfa3");
        data.add("[a12daf3123");
        data.add("[a313");
        data.add("[a123adfad123");
        data.add("[a12312sssss3");
        data.add("[a12312fad3");
        data.add("[a12badfffff123");
        data.add("[a1d23123");
        data.add("[a13123");
        data.add("[a3");

        Flowlayout flowLayout = (Flowlayout) findViewById(R.id.flowlayout);
        flowLayout.setData(data);
    }
}
