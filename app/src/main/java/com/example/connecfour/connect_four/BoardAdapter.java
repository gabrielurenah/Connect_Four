package com.example.connecfour.connect_four;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class BoardAdapter extends BaseAdapter {
    Context context;
    ArrayList<View> views;

    public BoardAdapter(Context context) {
        this.context = context;
        views = new ArrayList<View>();
    }
    @Override
    public int getCount() {
        return 42;
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return views.get(position);
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub

        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        int pxHeight = displayMetrics.heightPixels;

        if(convertView == null) {

            ImageView img = new ImageView(this.context);
            img.setImageResource(R.drawable.board_blue_mini);
            img.setAdjustViewBounds(true);
            img.setLayoutParams(new GridView.LayoutParams(pxWidth/7, (int) ((int)pxHeight/11.5)));
            views.add(img);
            return img;
        }
        return convertView;
    }
}