package com.szrjk.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.szrjk.dhome.R;
import com.szrjk.util.ImageItem;

public class PhotoGridAdapter extends BaseAdapter
{

	private LayoutInflater inflater;
	private int selectedPosition = -1;
	private boolean shape;
	private Context context;
	private List<ImageItem> items ;
	private List<String> urls = new ArrayList<String>();


	public void addImageList(List<ImageItem> ilist){
		items.addAll(ilist);
	}

	public void setImageList(List<ImageItem> ilist){
		items = ilist;
	}
	public void addStringUrl(ArrayList<String> ll){
		items = new ArrayList<ImageItem>();
		for (int i = 0; i < ll.size(); i++) {
			ImageItem it = new ImageItem();
			it.setAbsPaht(ll.get(i));
			items.add(it);
		}
	}

	
	public List<ImageItem> returnImageInfo(){
		return items;
	}

	public PhotoGridAdapter(Context context, List<ImageItem> items)
	{
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.items = items;

	}

	public int getSelectedPosition()
	{
		return selectedPosition;
	}

	public void setSelectedPosition(int selectedPosition)
	{
		this.selectedPosition = selectedPosition;
	}

	public boolean isShape()
	{
		return shape;
	}

	public void setShape(boolean shape)
	{
		this.shape = shape;
	}

	@Override
	public int getCount()
	{
		if (items.size() == 9) { return 9; }
		return (items.size() + 1);
	}

	@Override
	public Object getItem(int position)
	{
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
//		ImageItem imageItem = items.get(position);
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.item_post_photo, parent,
					false);
			holder = new ViewHolder();
			
//			imageItem.getImagePath()
			
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.iv_photo);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == items.size())
		{
			Resources resources = context.getResources();
			holder.imageView.setImageBitmap(BitmapFactory.decodeResource(
					resources, R.drawable.icon_addpic2));
			if (position == 9)
			{
				holder.imageView.setVisibility(View.GONE);
			}
		}
		else
		{
//			holder.imageView.setImageBitmap(items.get(position).getBitmap());
//			ImageLoaderUtil util = new ImageLoaderUtil(context, items.get(position).getAbsPaht(),
//					holder.imageView, R.drawable.pic_downloadfailed_bg, R.drawable.pic_downloadfailed_bg);
			//Log.i("适配器图片", items.get(position).getAbsPaht());
//			util.showImage();
//			BitmapUtils.create(context).display(holder.imageView, "/sdcard/test.jpg"); 
			BitmapUtils bitmapUtils = new BitmapUtils(context);
			bitmapUtils.display(holder.imageView, items.get(position).getAbsPaht());
		}		

		return convertView;
	}

	public class ViewHolder
	{
		public ImageView imageView;
	}
}
