package com.crankycode.android.mall;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * Created by zuyi on 7/13/2015.
 */
public class CustomCategoryAdapter extends ArrayAdapter<String> {

    Context mContext;
    int mResource;
    String[] list;

    public CustomCategoryAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.mResource = textViewResourceId;
        this.list = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();

        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        View row = inflater.inflate(R.layout.row, parent, false);
        TextView label=(TextView)row.findViewById(R.id.spinner_condition);
        label.setText(list[position]);
        return row;
    }
}
