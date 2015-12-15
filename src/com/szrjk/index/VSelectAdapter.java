package com.szrjk.index;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szrjk.dhome.R;

/**
 * denggm on 2015/10/20.
 * DHome
 * 列表的处理
 */
public class VSelectAdapter extends BaseAdapter {

    String[] itemTitles;

    String[] itemVals;

    private LayoutInflater mInflater;

    @Override
    public int getCount() {
        return itemTitles.length;
    }

    @Override
    public Object getItem(int i) {
        return itemTitles[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mInflater.inflate(R.layout.adapter_vselect, null);
        }
        TextView tv = (TextView) view.findViewById(R.id.itemTitle);
        tv.setText(itemTitles[position]);
        tv.setTag(itemVals[position]);
        return view;
    }


    public VSelectAdapter(Context context, String[] itemTitles,String[] itemVals) {
        this.mInflater = LayoutInflater.from(context);
        this.itemTitles = itemTitles;
        this.itemVals = itemVals;

    }

}