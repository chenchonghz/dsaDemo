package com.szrjk.index;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szrjk.dhome.R;

public class VSelectEducationLevelAdapter extends BaseAdapter{
	private Context context;
	private String[] levels;
	private LayoutInflater inflater;
	
	public VSelectEducationLevelAdapter(Context context,String[] levels) {
		this.context=context;
		this.levels=levels;
		this.inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return levels.length;
	}

	@Override
	public Object getItem(int position) {
		return levels[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		 if (convertView == null) {
			 convertView = inflater.inflate(R.layout.adapter_vselect, null);
	        }
	        TextView tv = (TextView) convertView.findViewById(R.id.itemTitle);
	        tv.setText(levels[position]);
		return convertView;
	}

}
