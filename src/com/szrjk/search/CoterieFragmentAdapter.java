package com.szrjk.search;

import java.util.List;

import com.szrjk.dhome.R;
import com.szrjk.entity.Coterie;
import com.szrjk.entity.SearchEntity;
import com.szrjk.util.ImageLoaderUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CoterieFragmentAdapter extends BaseAdapter {
	private Context context;
	private List<SearchEntity> list;
	private LayoutInflater inflater;

	public CoterieFragmentAdapter(Context context, List<SearchEntity> list) {
		this.context=context;
		this.list = list;
		this.inflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder=null;
		if (arg1==null) {
			arg1=inflater.inflate(R.layout.item_fragment_coterie, null);
			holder=new ViewHolder();
			holder.iv_coterie=(ImageView) arg1.findViewById(R.id.iv_coterie);
			holder.tv_coterie_name=(TextView) arg1.findViewById(R.id.tv_coterie_name);
			arg1.setTag(holder);
		}else {
			holder=(ViewHolder) arg1.getTag();
		}
		try {
			ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil
					(context, list.get(arg0).getCoterie_face_url(), holder.iv_coterie, R.drawable.header, R.drawable.header);
			imageLoaderUtil.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		holder.tv_coterie_name.setText(list.get(arg0).getCoterie_name());
		
		return arg1;
	}

	class ViewHolder{
		private ImageView iv_coterie;
		private TextView tv_coterie_name;
	}
	
}
