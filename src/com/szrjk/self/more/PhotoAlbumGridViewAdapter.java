package com.szrjk.self.more;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.szrjk.dhome.R;
import com.szrjk.entity.IPhotoClickOper;
import com.szrjk.util.ImageLoaderUtil;

public class PhotoAlbumGridViewAdapter extends BaseAdapter{
	private String[] picsList;
	private Context context;
	private IPhotoClickOper iPhotoClickOper;
	private static final String PIC_SIZE = "_230_230_cut";
	
	public PhotoAlbumGridViewAdapter(Context context,String[] picsList,IPhotoClickOper iPhotoClickOper) {
		this.context = context;
		this.picsList = picsList;
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
			convertView = View.inflate(context,R.layout.item_index_normalpost_photo, null);
		    ImageView iv_pic = (ImageView)convertView.findViewById(R.id.iv_post_photo);
		if(picsList != null){
			try {
				String pic_url = picsList[position];
				ImageLoaderUtil loader = new ImageLoaderUtil(context, pic_url, iv_pic, R.drawable.pic_downloadfailed_bg,
						R.drawable.pic_downloadfailed_bg);
				loader.showImage();
			} catch (Exception e) {
				Log.e("error", e.toString());
			}
		}
		//九宫格点击后浏览
		iv_pic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				Intent intent = new Intent(context,IndexGalleryActivity.class);
//				intent.putExtra("position", position);
//				intent.putExtra("imgs", picsList);
//				intent.putExtra("title", (position+1)+"/"+picsList.length);
//				intent.putExtra("needOper", false);
//				context.startActivity(intent);
				iPhotoClickOper.clickoper(position, context);
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
