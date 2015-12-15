package com.szrjk.search;

import java.text.ParseException;
import java.util.List;

import com.szrjk.dhome.R;
import com.szrjk.entity.SearchEntity;
import com.szrjk.util.DDateUtils;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.util.MxgsaTagHandler;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchFragmentAdapter extends BaseAdapter{

	private Context context;
	private List<SearchEntity> searchs;
	private LayoutInflater inflater;
	private String searchType;
	
	public SearchFragmentAdapter(Context context,List<SearchEntity> searchs, String searchType) {
		this.context=context;
		this.searchs=searchs;
		this.inflater=LayoutInflater.from(context);
		this.searchType=searchType;
	}
	
	@Override
	public int getCount() {
		return searchs.size();
	}

	@Override
	public Object getItem(int position) {
		return searchs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder=null;
		if (convertView==null) {
			convertView=inflater.inflate(R.layout.item_fragment_search, null);
			holder=new ViewHolder();
			holder.iv_picture=(ImageView) convertView.findViewById(R.id.iv_picture);
			holder.tv_title=(TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_summary=(TextView) convertView.findViewById(R.id.tv_summary);
			holder.tv_updateTime=(TextView) convertView.findViewById(R.id.tv_updateTime);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		SearchEntity search=searchs.get(position);
		if (searchType.equals("01")) {
			holder.iv_picture.setImageResource(R.drawable.icon_medical_80);
		}
		if (searchType.equals("02")) {
			holder.iv_picture.setImageResource(R.drawable.icon_sick_80);
		}
		else {
			holder.iv_picture.setImageResource(R.drawable.icon_sickcase_80);
		}
		holder.tv_title.setText(Html.fromHtml(search.getTitle(), null, new MxgsaTagHandler(context)));
		holder.tv_summary.setText(Html.fromHtml(search.getSummary(), null, new MxgsaTagHandler(context)));
		String updateTime = search.getUpdateTime();
		try {
			updateTime=DDateUtils.dformatOldstrToNewstr(updateTime, "yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日 HH:mm");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		holder.tv_updateTime.setText(updateTime);
		return convertView;
	}

	class ViewHolder{
		private ImageView iv_picture;
		private TextView tv_title;
		private TextView tv_summary;
		private TextView tv_updateTime;
	}
}
