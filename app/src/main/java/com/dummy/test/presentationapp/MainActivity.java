package com.dummy.test.presentationapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.dummy.test.presentationapp.adapter.FlowAdapter;
import com.dummy.test.presentationapp.view.FlowLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        View baseView = super.onCreateView(parent, name, context, attrs);
//        FlowLayout flowLayout = (FlowLayout) baseView.findViewById(R.id.flow_layout);
//        flowLayout.setAdapter(new FlowAdapter());
        return baseView;

    }
}
