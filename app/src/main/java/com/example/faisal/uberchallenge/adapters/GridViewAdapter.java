package com.example.faisal.uberchallenge.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.faisal.uberchallenge.components.GridItem;
import com.example.faisal.uberchallenge.components.ImageLoader;
import com.example.faisal.uberchallenge.R;

import java.util.ArrayList;


public class GridViewAdapter extends ArrayAdapter<GridItem> {

    public ImageLoader imageLoader;
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<GridItem> mGridData;

    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<GridItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
        imageLoader = new ImageLoader(mContext);
    }

    public ArrayList<GridItem> getmGridData() {
        return mGridData;
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
        //Call to get the image either from net/memory cache/file cache and the populate it in the imageView
        imageLoader.displayImage(mGridData.get(position).getUrl(), holder.imageView);
        return row;
    }

    public void addToGridData(GridItem item) {
        mGridData.add(item);
    }

    static class ViewHolder {
        ImageView imageView;
    }
}