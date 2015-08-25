package com.crankycode.android.mall;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zuyi on 7/14/2015.
 */
public class SpinnerAdapter extends ArrayAdapter<ItemData> {
    int groupid;
    int pixel_paddingleftright, pixel_paddingTopBottom;
    Activity context;
    ArrayList<ItemData> list;
    LayoutInflater inflater;


    public SpinnerAdapter(Activity context, int groupid, int id, ArrayList<ItemData> list) {
        super(context, id, list);
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pixel_paddingleftright = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, Resources.getSystem().getDisplayMetrics());
        pixel_paddingTopBottom = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, Resources.getSystem().getDisplayMetrics());
        this.groupid=groupid;

    }

    public View getView(int position, View convertedView, ViewGroup parent) {

        View itemView=inflater.inflate(groupid,parent,false);
        ImageView imageView=(ImageView)itemView.findViewById(R.id.img);
        imageView.setImageResource(list.get(position).getImageId());
        imageView.setPaddingRelative(pixel_paddingleftright,0,pixel_paddingleftright,0);
        TextView textView=(TextView) itemView.findViewById(R.id.txt);
        textView.setText(list.get(position).getText());

        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View itemView=inflater.inflate(groupid,parent,false);

        // Hide the first item which is the label "Category"
        if (position == 0) {
            ImageView imageView=(ImageView)itemView.findViewById(R.id.img);
            imageView.setVisibility(View.GONE);
            TextView textView=(TextView) itemView.findViewById(R.id.txt);
            textView.setHeight(0);

            textView.setVisibility(View.GONE);
        }
        else {
            ImageView imageView = (ImageView) itemView.findViewById(R.id.img);
            imageView.setImageResource(list.get(position).getImageId());
            imageView.setPaddingRelative(pixel_paddingleftright, pixel_paddingTopBottom ,pixel_paddingleftright,pixel_paddingTopBottom);
            TextView textView = (TextView) itemView.findViewById(R.id.txt);
            textView.setPaddingRelative(0,pixel_paddingTopBottom,0,pixel_paddingTopBottom);
            textView.setText(list.get(position).getText());
            
            LinearLayout layout = (LinearLayout) itemView.findViewById(R.id.spinner_linearlayout);
        }
        return itemView;
    }
}
