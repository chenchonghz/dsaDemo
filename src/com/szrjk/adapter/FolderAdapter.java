package com.szrjk.adapter;

import java.util.ArrayList;

import com.szrjk.dhome.AlbumActivity;
import com.szrjk.dhome.R;
import com.szrjk.dhome.ShowAllPhoto;
import com.szrjk.util.BitmapCache;
import com.szrjk.util.BitmapCache.ImageCallback;
import com.szrjk.util.ImageItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FolderAdapter extends BaseAdapter
{

	private Context mContext;
	private Intent mIntent;
	private DisplayMetrics dm;
	BitmapCache cache;
	final String TAG = getClass().getSimpleName();

	public FolderAdapter(Context c)
	{
		cache = new BitmapCache();
		init(c);
	}

	// 初始化
	public void init(Context c)
	{
		mContext = c;
		mIntent = ((Activity) mContext).getIntent();
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
	}

	@Override
	public int getCount()
	{
		return AlbumActivity.contentList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	ImageCallback callback = new ImageCallback()
	{
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params)
		{
			if (imageView != null && bitmap != null)
			{
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag()))
				{
					((ImageView) imageView).setImageBitmap(bitmap);
				}
				else
				{
				}
			}
			else
			{
			}
		}
	};

	private class ViewHolder
	{
		// 封面
		public ImageView iv_image;
		public ImageView iv_sel;
		// 文件夹名称
		public TextView tv_name;
		// 文件夹里面的图片数量
		public TextView tv_number;
	}

	ViewHolder holder = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.album_folder_item, null);
			holder = new ViewHolder();
			holder.iv_image = (ImageView) convertView
					.findViewById(R.id.iv_image);
			holder.iv_sel = (ImageView) convertView.findViewById(R.id.iv_sel);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_number = (TextView) convertView
					.findViewById(R.id.tv_number);
			holder.iv_image.setAdjustViewBounds(true);
			holder.iv_image.setScaleType(ImageView.ScaleType.FIT_XY);
			convertView.setTag(holder);
		}
		else holder = (ViewHolder) convertView.getTag();
		String path;
		if (AlbumActivity.contentList.get(position).imageList != null)
		{

			// 封面图片路径
			path = AlbumActivity.contentList.get(position).imageList.get(0).imagePath;
			// 给folderName设置值为文件夹名称
			holder.tv_name
					.setText(AlbumActivity.contentList.get(position).bucketName);

			// 给fileNum设置文件夹内图片数量
			holder.tv_number.setText(""
					+ AlbumActivity.contentList.get(position).count);

		}
		else path = "android_hybrid_camera_default";
		if (path.contains("android_hybrid_camera_default")) holder.iv_image
				.setImageResource(R.drawable.album_no_pictures);
		else
		{
			final ImageItem item = AlbumActivity.contentList.get(position).imageList
					.get(0);
			holder.iv_image.setTag(item.imagePath);
			cache.displayBmp(mContext,holder.iv_image, item.thumbnailPath,
					item.imagePath, callback);
		}
		// 为封面添加监听
		holder.iv_image.setOnClickListener(new ImageViewClickListener(position,
				mIntent, holder.iv_sel));

		return convertView;
	}

	// 为每一个文件夹构建的监听器
	private class ImageViewClickListener implements OnClickListener
	{
		private int position;
		private ImageView choose_back;

		public ImageViewClickListener(int position, Intent intent,
				ImageView choose_back)
		{
			this.position = position;
			this.choose_back = choose_back;
		}

		public void onClick(View v)
		{
			ShowAllPhoto.dataList = (ArrayList<ImageItem>) AlbumActivity.contentList
					.get(position).imageList;
			Intent intent = new Intent();
			String folderName = AlbumActivity.contentList.get(position).bucketName;
			intent.putExtra("folderName", folderName);
			intent.setClass(mContext, ShowAllPhoto.class);
			mContext.startActivity(intent);
			choose_back.setVisibility(v.VISIBLE);
		}
	}

	public int dipToPx(int dip)
	{
		return (int) (dip * dm.density + 0.5f);
	}

}
