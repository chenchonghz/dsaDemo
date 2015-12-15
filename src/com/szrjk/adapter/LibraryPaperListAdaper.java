package com.szrjk.adapter;

import java.util.List;

import com.szrjk.dhome.R;
import com.szrjk.entity.LibraryEntity;
import com.szrjk.util.ImageLoaderUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LibraryPaperListAdaper extends BaseAdapter{

	private Context mContext;
	private List<LibraryEntity> list;
	private LayoutInflater mInflater;
	
	public LibraryPaperListAdaper(Context mContext, List<LibraryEntity> list) {
		super();
		this.mContext = mContext;
		this.list = list;
		this.mInflater = LayoutInflater.from(mContext);
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
	public View getView(int position, View convertview, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertview==null) {
			viewHolder = new ViewHolder();
			convertview = mInflater.inflate(R.layout.item_library_paper, null);
			viewHolder.tv = (TextView) convertview.findViewById(R.id.tv_paper);
			viewHolder.iv = (ImageView) convertview.findViewById(R.id.iv_paper);
			convertview.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertview.getTag();
		}
		try {
			ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil(
					mContext, list.get(position).getKbFace(), viewHolder.iv, 0, 0);
			imageLoaderUtil.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		viewHolder.tv.setText(list.get(position).getName());
		return convertview;
	}

	private class ViewHolder{
		private ImageView iv;
		private TextView tv;
	}
}
