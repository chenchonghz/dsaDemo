package com.szrjk.self.more.album;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.szrjk.dhome.R;
import com.szrjk.util.BitmapCache;
import com.szrjk.util.BitmapCache.ImageCallback;
import com.szrjk.util.ImageItem;

public class AlbumGalleryAdapter extends BaseAdapter {
	private Context mContext;
	private List<PhotoUpImageItem> dataList;
	private DisplayMetrics dm;
	BitmapCache cache;

	public AlbumGalleryAdapter(Context c, List<PhotoUpImageItem> dataList) {
		mContext = c;
		cache = new BitmapCache();
		this.dataList = dataList;
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
	}

	public int getCount() {
		return dataList.size();
	}

	public Object getItem(int position) {
		return dataList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	ImageCallback callback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				}
			}
		}
	};

	/**
	 * 存放列表项控件句柄
	 */
	private class ViewHolder {
		public ImageView iv_view;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_album_gallery, parent, false);
			viewHolder.iv_view = (ImageView) convertView
					.findViewById(R.id.iv_view);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String path;
		if (dataList != null && dataList.size() > position) {
			path = dataList.get(position).getImagePath();
		} else {
			path = "camera_default";
		}
		if (path.contains("camera_default")) {
			viewHolder.iv_view.setImageResource(R.drawable.album_no_pictures);
		} else {
			final PhotoUpImageItem item = dataList.get(position);
			viewHolder.iv_view.setTag(item.getImagePath());
			cache.displayBmp(mContext, viewHolder.iv_view, item.thumbnailPath,
					item.getImagePath(), callback);
		}
		return convertView;
	}

	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}
}
