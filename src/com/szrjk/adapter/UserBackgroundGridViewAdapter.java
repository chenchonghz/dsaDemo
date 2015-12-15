package com.szrjk.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.szrjk.dhome.R;
/**
 * 用户背景墙缩略图浏览界面GridView适配器
 * @author 郑斯铭
 *
 */
public class UserBackgroundGridViewAdapter extends BaseAdapter {
	private Context mContext;
	private String[] imgs;
	private LayoutInflater mInflater;
	
	public UserBackgroundGridViewAdapter(Context c, String[]imgs) {
		super();
		this.mContext = c;
		this.imgs = imgs;
		this.mInflater= LayoutInflater.from(mContext);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return imgs.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imgs[position];
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	private class ViewHolder{
		public ImageView iv_bg_item;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.item_select_background, null);
			viewHolder.iv_bg_item = (ImageView) convertView.findViewById(R.id.iv_bg_select_item);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		BitmapUtils bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.display(viewHolder.iv_bg_item, imgs[position]);
		
		return convertView;
	}
	

}
