package com.dummy.test.presentationapp.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dummy.test.presentationapp.R;

/**
 * Created by thomas.pienaar on 06/04/17.
 */


public class FlowAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
            convertView = View.inflate(parent.getContext(), R.layout.flow_layout, null);

        ((TextView) convertView.findViewById(R.id.list_text)).setText("Text: " + position);

        return convertView;
    }
}
