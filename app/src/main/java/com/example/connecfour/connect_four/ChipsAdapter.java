package com.example.connecfour.connect_four;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class ChipsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<View> views;

    public ChipsAdapter(Context context) {
        this.context = context;
        views = new ArrayList<View>();
    }

    @Override
    public int getCount() {
        return 42;
    }

    @Override
    public Object getItem(int position) {
        return views.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        int pxHeight = displayMetrics.heightPixels;

        if (convertView == null) {
            ImageView img = new ImageView(this.context);
            img.setImageResource(R.drawable.red);
            img.setVisibility(View.INVISIBLE);
            img.setAdjustViewBounds(true);
            img.setLayoutParams(new GridView.LayoutParams(pxWidth/7, (int) (pxHeight/11.5)));
            views.add(img);
            return img;
        }
        return convertView;
    }
}
