package com.szrjk.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.szrjk.dhome.R;
import com.szrjk.widget.IndexGridView;

public class DeptGridViewAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<String> lsID ;
	private ArrayList<String> lsValue ;
	private IndexGridView gridView;
	public DeptGridViewAdapter(Context context,ArrayList<String> lsID,ArrayList<String> lsValue,IndexGridView gridView) {
		this.context = context;
		this.gridView = gridView;
		if (lsID == null) {
			this.lsID = new ArrayList<String>();
		}else{
			this.lsID = lsID;
		}
		if (lsValue == null) {
			this.lsValue = new ArrayList<String>();
		}else{
			this.lsValue = lsValue;
		}
	}
	
	public String getDept(int position){
		return lsValue.get(position);
	}
	
	public String getDeptId(int position){
		return lsID.get(position);
	}
	public void removeItem(int position){
		lsID.remove(position);
		lsValue.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return lsValue.size();
	}

	@Override
	public Object getItem(int arg0) {
		return lsValue.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

//	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View converview, ViewGroup arg2) {

		//		View v = LayoutInflater.from(context).inflate(R.layout.d, null);
		String deptid = lsID.get(position);
		String deptvalue = lsValue.get(position);
		
		converview  =View.inflate(context, R.layout.dept_item, null);
		TextView tv_dept = (TextView)converview.findViewById(R.id.tv_dept);
		tv_dept.setText(deptvalue);
		tv_dept.setTag(deptid);
		tv_dept.setTextColor(context.getResources().getColor(R.color.header_bg));
		tv_dept.setBackground(context.getResources().getDrawable(R.drawable.flow_dept_selector));
//		gridView.setColumnWidth(measureCellWidth(context, tv_dept));
		return converview;

	}
	class ViewHolder {
		TextView tv_dept;
	}
	public int measureCellWidth( Context context, View cell )
	{
	 // We need a fake parent
	 FrameLayout buffer = new FrameLayout( context );
	 android.widget.AbsListView.LayoutParams layoutParams = new android.widget.AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	 buffer.addView( cell, layoutParams);
	 cell.forceLayout();
	 cell.measure(1000, 1000);
	 int width = cell.getMeasuredWidth();
	 buffer.removeAllViews();
	 return width;
	}
}
