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

public class AlbumItemAdapter extends BaseAdapter {
	private List<PhotoUpImageItem> list;// 存放所选相册里面的图片列表
	private LayoutInflater layoutInflater;
	private BitmapCache cache;
	private DisplayMetrics dm;
	private Context context;

	public AlbumItemAdapter(List<PhotoUpImageItem> list, Context context) {
		this.context = context;
		this.list = list;
		cache = new BitmapCache();
		layoutInflater = LayoutInflater.from(context);
		dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_album_gallery,
					parent, false);
			holder = new Holder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.iv_view);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		String path;
		if (list != null && list.size() > position) {
			path = list.get(position).getImagePath();
		} else {
			path = "camera_default";
		}
		if (path.contains("camera_default")) {
			holder.imageView.setImageResource(R.drawable.album_no_pictures);
		} else {
			holder.imageView.setTag(list.get(position).getImagePath());
			cache.displayBmp(context, holder.imageView, list.get(position)
					.getThumbnailPath(), list.get(position).getImagePath(),
					callback);

		}
		return convertView;
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

	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}

	class Holder {
		ImageView imageView;
	}
}
