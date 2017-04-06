package com.dummy.test.presentationapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dummy.test.presentationapp.adapter.FlowAdapter;
import com.dummy.test.presentationapp.view.FlowLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        flowLayout.setAdapter(new FlowAdapter());
    }
}


