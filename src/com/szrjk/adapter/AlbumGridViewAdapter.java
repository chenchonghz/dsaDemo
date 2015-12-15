package com.szrjk.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.szrjk.dhome.R;
import com.szrjk.util.BitmapCache;
import com.szrjk.util.BitmapCache.ImageCallback;
import com.szrjk.util.ImageItem;

public class AlbumGridViewAdapter extends BaseAdapter
{
	private Context mContext;
	private List<ImageItem> dataList;
	private List<ImageItem> selectedDataList;
	private DisplayMetrics dm;
	BitmapCache cache;

	public AlbumGridViewAdapter(Context c, List<ImageItem> dataList,
			List<ImageItem> selectedDataList)
	{
		mContext = c;
		cache = new BitmapCache();
		this.dataList = dataList;
		this.selectedDataList = selectedDataList;
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
	}

	@Override
	public int getCount()
	{
		return dataList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return dataList.get(position);
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
				if (url != null && url.equals(imageView.getTag()))
				{
					imageView.setImageBitmap(bitmap);
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

	/**
	 * 存放列表项控件句柄
	 */
	private class ViewHolder
	{
		public ImageView iv_view;
		public ToggleButton btn_toggle;
		public CheckBox btn_choose;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)	{
		ViewHolder viewHolder;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.album_img_item, parent, false);
			viewHolder.iv_view = (ImageView) convertView.findViewById(R.id.iv_view);
			viewHolder.btn_toggle = (ToggleButton) convertView.findViewById(R.id.btn_toggle);
			viewHolder.btn_choose = (CheckBox) convertView.findViewById(R.id.btn_choose);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String path;
		if (dataList != null && dataList.size() > position){
			path = dataList.get(position).imagePath;
		}else {
			path = "camera_default";
		}
		if (path.contains("camera_default"))
		{
			viewHolder.iv_view.setImageResource(R.drawable.album_no_pictures);
		}
		else
		{
			final ImageItem item = dataList.get(position);
			viewHolder.iv_view.setTag(item.imagePath);
			cache.displayBmp(mContext,viewHolder.iv_view, item.thumbnailPath,
					item.imagePath, callback);
		}
//		viewHolder.btn_toggle.setTag(position);
		viewHolder.btn_choose.setTag(position);
//		viewHolder.btn_toggle.setOnClickListener(new ToggleClickListener(viewHolder.btn_choose));
		viewHolder.btn_choose.setOnClickListener(new ToggleClickListener(viewHolder.btn_choose));
		//这里很重要、如果选择了图片，下拉，返回，勾选的图片的标识会不见
		if (selectedDataList.contains(dataList.get(position)))
		{
			//对于已经选择的，勾选
//			viewHolder.btn_toggle.setChecked(true);
//			viewHolder.btn_choose.setVisibility(View.VISIBLE);
			viewHolder.btn_choose.setChecked(true);
//			viewHolder.btn_choose.setBackgroundResource(R.drawable.icon_addpic_02_36);
			//			Log.i("position", position+"");
		}
		else
		{
//			viewHolder.btn_toggle.setChecked(false);
			viewHolder.btn_choose.setChecked(false);
//			viewHolder.btn_choose.setVisibility(View.VISIBLE);
//			viewHolder.btn_choose.setBackgroundResource(R.drawable.icon_addpic_01_36);
		}
		return convertView;
	}

	public int dipToPx(int dip)
	{
		return (int) (dip * dm.density + 0.5f);
	}

	private class ToggleClickListener implements OnClickListener
	{
		CheckBox chooseBt;

		public ToggleClickListener(CheckBox choosebt)
		{
			this.chooseBt = choosebt;
		}

		@Override
		public void onClick(View view)
		{
			if (view instanceof ToggleButton)
			{
				ToggleButton toggleButton = (ToggleButton) view;
				toggleButton.setVisibility(View.GONE);
//				int position = (Integer) toggleButton.getTag();
//				if (dataList != null && mOnItemClickListener != null
//						&& position < dataList.size())
//				{
//					mOnItemClickListener.onItemClick(toggleButton, position,
//							toggleButton.isChecked(), chooseBt);
////					if (toggleButton.isChecked()) {
////						chooseBt.setVisibility(View.VISIBLE);
////						chooseBt.setBackgroundResource(R.drawable.icon_addpic_02);
////					}else{
////						Log.i("01", "01");
////						chooseBt.setVisibility(View.VISIBLE);
////						chooseBt.setBackgroundResource(R.drawable.icon_addpic_01);
////					}
//				}
			}
			if (view instanceof CheckBox) {
				CheckBox cb = (CheckBox) view;
				int position = (Integer) cb.getTag();
				if (dataList != null && mOnItemClickListener != null
						&& position < dataList.size())
				{
					mOnItemClickListener.onItemClick(null, position,cb.isChecked(), chooseBt);
					//可以在这里更改背景cb.isChecked()
					
					
					
//					if (cb.isChecked()) {
//						chooseBt.setVisibility(View.VISIBLE);
//						chooseBt.setBackgroundResource(R.drawable.icon_addpic_02);
//					}else{
//						Log.i("01", "01");
//						chooseBt.setVisibility(View.VISIBLE);
//						chooseBt.setBackgroundResource(R.drawable.icon_addpic_01);
//					}
				}
			}
//			chooseBt.setVisibility(View.VISIBLE);
		}

	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l)
	{
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener
	{
		public void onItemClick(ToggleButton view, int position,
				boolean isChecked, CheckBox chooseBt);
	}

}
