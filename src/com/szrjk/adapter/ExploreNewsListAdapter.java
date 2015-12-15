package com.szrjk.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.szrjk.dhome.R;
import com.szrjk.entity.NewsEntity;
import com.szrjk.util.DisplayTimeUtil;
import com.szrjk.util.ImageLoaderUtil;

public class ExploreNewsListAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<NewsEntity> newsList;
	
	

	public ExploreNewsListAdapter(Context context, ArrayList<NewsEntity> newsList) {
		super();
		this.context = context;
		this.newsList = newsList;
	}

	@Override
	public int getCount() {
		return newsList.size();
	}

	@Override
	public Object getItem(int position) {
		return newsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = View.inflate(context, R.layout.item_news, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_new_img = (ImageView)convertView.findViewById(R.id.iv_new_img);
			viewHolder.tv_new_text = (TextView)convertView.findViewById(R.id.tv_new_text);
			viewHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
			viewHolder.tv_type = (TextView)convertView.findViewById(R.id.tv_type);
			viewHolder.tv_commend_count = (TextView)convertView.findViewById(R.id.tv_commend_count);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		NewsEntity news = newsList.get(position);
		String type = null;
		if(news.getInfTab().equals("00")||news.getInfTab().isEmpty()){
			viewHolder.tv_type.setVisibility(View.INVISIBLE);
		}else{
			viewHolder.tv_type.setVisibility(View.VISIBLE);
			if(news.getInfTab().equals("DJ")){
				type = "独家";
			}else if(news.getInfTab().equals("FT")){
				type = "访谈";
			}else if(news.getInfTab().equals("HD")){
				type = "活动";
			}else if(news.getInfTab().equals("HT")){
				type = "话题";
			}else if(news.getInfTab().equals("TG")){
				type = "推广";
			}else if(news.getInfTab().equals("ZT")){
				type = "专题";
			}
			viewHolder.tv_type.setText(type);
		}
		try {
			String time = null;
			if(news.getInfSignTime()!=null){
				time = DisplayTimeUtil.displayDateString(news.getInfSignTime());
			}
			if(news.getInfCreateTime()!=null){
				time = DisplayTimeUtil.displayDateString(news.getInfCreateTime());
			}
			Log.e("Explore", "资讯时间："+time);
			viewHolder.tv_time.setText(time);
		} catch (Exception e) {
			Log.e("Explore", e.toString());
		}
		viewHolder.tv_new_text.setText(news.getInfTitleAbstract());
		viewHolder.tv_commend_count.setText(String.valueOf(news.getInfCommentCount()));
		try {
			ImageLoaderUtil imageLoader = new ImageLoaderUtil(context, news.getInfImage(), viewHolder.iv_new_img, R.drawable.pic_downloadfailed_230,  R.drawable.pic_downloadfailed_230);
			imageLoader.showBigImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		return convertView;
	}
	
	class ViewHolder{
		ImageView iv_new_img;
		TextView tv_new_text,tv_time,tv_commend_count,tv_type;
	}

}
