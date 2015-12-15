package com.szrjk.index;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szrjk.dhome.R;

public class VSelectDepartmentAdapter extends BaseAdapter{
	private Context context;
	private String[] deptNames;
	private LayoutInflater inflater;
	
	public VSelectDepartmentAdapter(Context context,String[] deptNames) {
		this.context=context;
		this.deptNames=deptNames;
		this.inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return deptNames.length;
	}

	@Override
	public Object getItem(int position) {
		return deptNames[position];
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
	        tv.setText(deptNames[position]);
		return convertView;
	}

}
