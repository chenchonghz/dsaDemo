package com.szrjk.self.more.album;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.szrjk.dhome.R;
import com.szrjk.util.BitmapCache;
import com.szrjk.util.BitmapCache.ImageCallback;

public class AlbumGalleryAdapter extends BaseAdapter {
	/**
	 * 用户选择的图片，存储为图片的完整路径
	 * Linklist<>
	 */
	public static List<String> mSelectedImage = new ArrayList<String>();
	//显示可选个数
	private TextView tv_number;
	public static int num ;//暂时用1 
	private Context mContext;
	private List<PhotoUpImageItem> dataList;
	private DisplayMetrics dm;
	BitmapCache cache;

	public AlbumGalleryAdapter(Context c, List<PhotoUpImageItem> dataList,TextView tv_number,int num) {
		mContext = c;
		this.tv_number = tv_number;
		this.num = num;
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
		public ImageView iv_select;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
//			convertView = LayoutInflater.from(mContext).inflate(
//					R.layout.item_album_gallery, parent, false);
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.image_grid_item, parent, false);
			viewHolder.iv_view = (ImageView) convertView.findViewById(R.id.id_item_image);
			viewHolder.iv_select = (ImageView) convertView.findViewById(R.id.id_item_select);
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
		final PhotoUpImageItem item = dataList.get(position);
		if (path.contains("camera_default")) {
			viewHolder.iv_view.setImageResource(R.drawable.album_no_pictures);
		} else {
			viewHolder.iv_view.setTag(item.getImagePath());
			cache.displayBmp(mContext, viewHolder.iv_view, item.thumbnailPath,
					item.getImagePath(), callback);
		}
		viewHolder.iv_view.setColorFilter(null);
		//设置ImageView的点击事件
		viewHolder.iv_view.setOnClickListener(new OnClickListener()
		{
			//选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v)	{

				if (mSelectedImage.size() >= num) {
//					handler.sendEmptyMessage(9);
					// 已经选择过该图片,如果再点击，就取消勾选
					if (mSelectedImage.contains(item.getImagePath()))
					{
//						
						mSelectedImage.remove(item.getImagePath());
						viewHolder.iv_select.setImageResource(R.drawable.icon_addpic_01_36);
						viewHolder.iv_view.setColorFilter(null);

					}else
						// 未选择该图片
					{
						Toast.makeText(mContext, "你最多只能选择" + num+ "张图片", 0).show();
					}
				}else{

					// 已经选择过该图片
					if (mSelectedImage.contains(item.getImagePath()))
					{
						mSelectedImage.remove(item.getImagePath());
						viewHolder.iv_select.setImageResource(R.drawable.icon_addpic_01_36);
						viewHolder.iv_view.setColorFilter(null);

					} 
					else
					{
						// 未选择该图片
						mSelectedImage.add(item.getImagePath());
						viewHolder.iv_select.setImageResource(R.drawable.icon_addpic_02_36);
						viewHolder.iv_view.setColorFilter(Color.parseColor("#77000000"));
					}
				}
				if (mSelectedImage.size() == 0) {
					tv_number.setText("完成");
					
				}else{
					tv_number.setText("完成" + "("+mSelectedImage.size() + "/" + num + ")");
				}
				Log.i("", "--------------适配器的点击事件----------------");
				Log.i("选择之后的保存的地址：", mSelectedImage.toString());
			}
			
		});

		/**
		 * 已经选择过的图片，显示出选择过的效果
		 * [一定要注意else；从用view会保留之后有√的图片。]
		 */
		if (mSelectedImage.contains(item.getImagePath()))
		{
			//			mSelect.setImageResource(R.drawable.pictures_selected);
			viewHolder.iv_select.setImageResource(R.drawable.icon_addpic_02_36);
			viewHolder.iv_view.setColorFilter(Color.parseColor("#77000000"));
		}else{
			viewHolder.iv_select.setImageResource(R.drawable.icon_addpic_01_36);
			viewHolder.iv_view.setColorFilter(null);
		}
		
		
		return convertView;
	}

	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}
}
