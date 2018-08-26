package com.example.faisal.uberchallenge.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.faisal.uberchallenge.GridItem;
import com.example.faisal.uberchallenge.ResultActivity;
import com.example.faisal.uberchallenge.R;

import java.util.ArrayList;


public class GridViewAdapter extends ArrayAdapter<GridItem> {
    private Context mContext;

    private int layoutResourceId;

    private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();

    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<GridItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }

    public void setGridData(ArrayList<GridItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }


    public int getCount() {
        return mGridData.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        GridItem item = mGridData.get(position);
        holder.imageView.setImageBitmap(item.getBitMap());
        return row;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}