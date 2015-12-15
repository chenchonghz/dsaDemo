package com.szrjk.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.szrjk.dhome.R;
import com.szrjk.entity.NewsEntity;
import com.szrjk.explore.NewsDetailActivity;
import com.szrjk.util.BusiUtils;
import com.szrjk.util.DialogUtil;
import com.szrjk.util.ImageLoaderUtil;

public class LoopViewPagerAdapter extends PagerAdapter{
	private Context context;
	private ArrayList<NewsEntity> ggList;
	private boolean isTourist;
	
	public LoopViewPagerAdapter(Context context, ArrayList<NewsEntity> ggList) {
		super();
		this.context = context;
		isTourist = BusiUtils.isguest(context);
		if(ggList != null){	
			this.ggList = ggList;
		}else{
			this.ggList = new ArrayList<NewsEntity>();
			NewsEntity ne = new NewsEntity();
			this.ggList.add(ne);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ggList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return view == obj;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = View.inflate(context, R.layout.viewpager_imageview, null);
		ImageView imageView = (ImageView)view.findViewById(R.id.iv_img);
		NewsEntity gg = ggList.get(position);
		Log.e("LoopViewPagerAdapter", "图片地址："+gg.getInfImage());
		try {
			ImageLoaderUtil imageLoader = new ImageLoaderUtil(context, gg.getInfImage(), imageView, R.drawable.pic_downloadfailed_bg, R.drawable.pic_downloadfailed_bg);
			imageLoader.showBigImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		setListenr(imageView,gg);
		container.addView(view);
		return view;
	}

	private void setListenr(ImageView imageView,final NewsEntity gg) {
		// TODO Auto-generated method stub
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isTourist){
					DialogUtil.showGuestDialog(context, null);
				}else{				
					Intent intent = new Intent(context, NewsDetailActivity.class);
					intent.putExtra("newsId", gg.getInfId());
					context.startActivity(intent);
				}
			}
		});
	}

}
