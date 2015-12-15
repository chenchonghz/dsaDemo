package com.szrjk.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szrjk.dhome.R;


public class ExploreMyTypeGridViewAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<String> typeList;
	private ArrayList<String> typeIdList;
	private Resources res;
	private int h;
	private int w;
	
	 

	public ExploreMyTypeGridViewAdapter(Context context,
			ArrayList<String> typeList, ArrayList<String> typeIdList, int h,
			int w) {
		super();
		this.context = context;
		this.typeList = typeList;
		this.typeIdList = typeIdList;
		this.h = h;
		this.w = w;
		res = context.getResources();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return typeList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return typeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String title = typeList.get(position);
		String type_id = typeIdList.get(position);
		convertView  = View.inflate(context, R.layout.view_explore_type, null);
		TextView tv_my_type = (TextView)convertView.findViewById(R.id.tv_my_type);
		tv_my_type.setText(title);
		tv_my_type.setTag(type_id);
		return convertView;
	}
	
	public void removeItem(int position){
		Log.e("ExploreAdapter", "typeId长度："+typeIdList.size()+"title长度："+typeList.size()+"位置："+position);
		typeIdList.remove(position);
		typeList.remove(position);
		notifyDataSetChanged();
	}
	
	public String getTitle(int position){
		return typeList.get(position);
	}
	
	public String getTypeId(int position){
		return typeIdList.get(position);
	}

}
