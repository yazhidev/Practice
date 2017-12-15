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

        data.add("我要做远方的忠诚的儿子");
        data.add("和物质的");
        data.add("短暂情人");
        data.add("和所有以梦为马的");
        data.add("诗人一样");
        data.add("我");
        data.add("不得不和");
        data.add("烈士和小丑");
        data.add("走在同一道路上");

        Flowlayout flowLayout = (Flowlayout) findViewById(R.id.flowlayout);
        flowLayout.setData(data);
    }
}
