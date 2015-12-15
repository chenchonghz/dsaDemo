package com.szrjk.adapter;

import com.szrjk.dhome.R;
import com.szrjk.util.ImageLoaderUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CoterieMemberPortraitGridAdapter extends BaseAdapter{
	private String[] coterieFaceUrls;
	private Context context;
	private LayoutInflater inflater;
	
	public CoterieMemberPortraitGridAdapter(Context context,String[] coterieFaceUrls) {
		this.context=context;
		this.coterieFaceUrls=coterieFaceUrls;
		this.inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (coterieFaceUrls.length<5) {
			return coterieFaceUrls.length;
		}
		return 5;
	}

	@Override
	public Object getItem(int position) {
		return coterieFaceUrls[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		convertView=inflater.inflate(R.layout.item_portrait_grid, null);
		ImageView iv_portrait=(ImageView) convertView.findViewById(R.id.iv_portrait);
		String coterieFaceUrl = coterieFaceUrls[position];
		try {
			ImageLoaderUtil imageLoaderUtil=new ImageLoaderUtil(context, coterieFaceUrl, iv_portrait, R.drawable.header, R.drawable.header);
			imageLoaderUtil.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		return convertView;
	}

}
