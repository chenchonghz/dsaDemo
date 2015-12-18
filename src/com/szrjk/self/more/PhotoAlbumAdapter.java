package com.szrjk.self.more;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.szrjk.dhome.R;
import com.szrjk.entity.PhotoAlbum;
import com.szrjk.util.DDateUtils;
import com.szrjk.util.ImageLoaderUtil;

public class PhotoAlbumAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<PhotoAlbum> photoAlbumList;
	private LayoutInflater inflater;

	public PhotoAlbumAdapter(Context context,
			ArrayList<PhotoAlbum> photoAlbumList) {
		this.context = context;
		this.photoAlbumList = photoAlbumList;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return photoAlbumList.size();
	}

	@Override
	public Object getItem(int position) {
		return photoAlbumList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHodler hodler = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_photo_album, null);
			hodler = new ViewHodler();
			hodler.iv_photo_album = (ImageView) convertView
					.findViewById(R.id.iv_photo_album);
			hodler.tv_month = (TextView) convertView
					.findViewById(R.id.tv_month);
			hodler.tv_count = (TextView) convertView
					.findViewById(R.id.tv_count);
			convertView.setTag(hodler);
		} else {
			hodler = (ViewHodler) convertView.getTag();
		}
		PhotoAlbum photoAlbum = photoAlbumList.get(position);
		String picUrlList = photoAlbum.getPicUrls();
		String picUrl = null;
		if (picUrlList != null && !picUrlList.isEmpty()) {
			if (picUrlList.contains("|")) {
				picUrl = (String) picUrlList.subSequence(0,
						picUrlList.indexOf("|"));
//			Log.i("picUrl", picUrl);
			}else {
				picUrl = picUrlList;
//				Log.i("picUrl", picUrl);
			}
		} 
		ImageLoaderUtil imageLoaderUtil=new ImageLoaderUtil(context, picUrl, hodler.iv_photo_album, R.drawable.pic_downloadfailed_bg, R.drawable.pic_downloadfailed_bg);
		imageLoaderUtil.showImage();
		String month = photoAlbum.getMonth();
		Date date = new Date();
//		Log.i("currentmonth", date.getMonth() + "");
//		Log.i("month", (String) month.subSequence(4, 6));

		if (String.valueOf(date.getMonth() + 1).equals(month.subSequence(4, 6))) {
			hodler.tv_month.setText("本月");
		} else {
			try {
				month = DDateUtils.dformatOldstrToNewstr(month, "yyyyMM",
						"yyyy年MM月");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			hodler.tv_month.setText(month);
		}
		hodler.tv_count.setText(photoAlbum.getNum());
		return convertView;
	}

	class ViewHodler {
		private ImageView iv_photo_album;
		private TextView tv_month;
		private TextView tv_count;
	}
}
