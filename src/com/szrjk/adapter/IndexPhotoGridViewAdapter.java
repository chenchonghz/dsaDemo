package com.szrjk.adapter;

import android.content.Intent;

import com.szrjk.dhome.IndexGalleryActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.IContextClickOper;
import com.szrjk.entity.IPhotoClickOper;
import com.szrjk.self.UserBackgroundSelectActivity;
import com.szrjk.self.UserBackgroundShower;
import com.szrjk.util.ImageLoaderUtil;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class IndexPhotoGridViewAdapter extends BaseAdapter{
	private String[] picsList;
	private Context context;
	private static final String PIC_SIZE = "_230_230_cut";
	private int screenWidth;
	private IPhotoClickOper iPhotoClickOper;
	
	public IndexPhotoGridViewAdapter(Context context,String[] picsList,int screenWidth,IPhotoClickOper iPhotoClickOper) {
		this.context = context;
		this.picsList = picsList;
		this.screenWidth = screenWidth;
		this.iPhotoClickOper = iPhotoClickOper;
	}

	@Override
	public int getCount() {
		return picsList.length;
	}

	@Override
	public Object getItem(int position) {
		return picsList[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = View.inflate(context, R.layout.item_album_gallery, null);
			ImageView iv_img = (ImageView)convertView.findViewById(R.id.iv_view);
			String pic_url = getPicUrl(picsList[position]);
			ImageLoaderUtil loader = new ImageLoaderUtil(context, pic_url, iv_img, R.drawable.pic_downloadfailed_bg,
					R.drawable.pic_downloadfailed_bg);
			loader.showImage();
			iv_img.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					iPhotoClickOper.clickoper(position,context);
				}
			});
			return convertView;
	}
	
	private String getPicUrl(String pic) {
		if(!pic.isEmpty()){
			int index = pic.indexOf(".jpg");
			//有可能没有这个jpg
			if(index==-1)return "";
			return pic.substring(0, index)+PIC_SIZE+pic.substring(index, pic.length());
		}else{
			return "";
		}

	}

}
