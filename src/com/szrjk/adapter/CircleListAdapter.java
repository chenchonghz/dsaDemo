package com.szrjk.adapter;

import java.util.List;

import com.szrjk.dhome.R;
import com.szrjk.entity.CircleInfo;
import com.szrjk.self.CircleHomepageActivity;
import com.szrjk.util.ImageLoaderUtil;
import com.umeng.analytics.c;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CircleListAdapter extends BaseAdapter{
	private List<CircleInfo> list;
	private Context mContext;
	private LayoutInflater mInflater;
	
	public CircleListAdapter(List<CircleInfo> list, Context mContext) {
		super();
		this.list = list;
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertview==null) {
			viewHolder = new ViewHolder();
			convertview=mInflater.inflate(R.layout.item_my_circle, null);
			viewHolder.iv_circle = (ImageView) convertview.findViewById(R.id.iv_circle);
			viewHolder.tv_circle_name = (TextView) convertview.findViewById(R.id.tv_circle_name);
			viewHolder.tv_circle_num = (TextView) convertview.findViewById(R.id.tv_circle_num);
			viewHolder.lly_item = (LinearLayout) convertview.findViewById(R.id.lly_item_circle);
			convertview.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertview.getTag();
		}
		try {
			ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil(
					mContext, list.get(position).getCoterieFaceUrl(), viewHolder.iv_circle, 
					R.drawable.icon_headfailed, R.drawable.icon_headfailed);
			imageLoaderUtil.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		viewHolder.tv_circle_name.setText(list.get(position).getCoterieName());	
		viewHolder.tv_circle_num.setText(list.get(position).getMemberCount());	
//		viewHolder.lly_item.setOnTouchListener(new touchListenr(position, viewHolder));
		return convertview;
	}
	
	public class ViewHolder{
		private ImageView iv_circle;
		private TextView tv_circle_name;
		private TextView tv_circle_num;
		private LinearLayout lly_item;
	}

//	class touchListenr implements OnTouchListener{
//		private int position;
//		private ViewHolder viewHolder;
//		
//		public touchListenr(int position, ViewHolder viewHolder) {
//			super();
//			this.position = position;
//			this.viewHolder = viewHolder;
//		}
//		public boolean onTouch(View view, MotionEvent event) {
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				viewHolder.lly_item.setBackgroundResource(R.drawable.pop_pressed);
//				CircleInfo circleInfo = list.get(position);
//				Intent intent = new Intent(mContext, CircleHomepageActivity.class);
//				intent.putExtra("circle", circleInfo);
//				mContext.startActivity(intent);
//				break;
//			case MotionEvent.ACTION_UP:
//				
//				viewHolder.lly_item.setBackgroundResource(R.color.white);
//				break;
//
//			}
//			return true;
//		}
//		
//	}
}
