package com.szrjk.adapter;

import java.util.List;

import com.szrjk.dhome.R;
import com.szrjk.entity.PaperListInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LibraryPaperAdapter extends BaseAdapter {

	private Context mContext;
	private List<PaperListInfo> list;
	private LayoutInflater miInflater;
	
	public LibraryPaperAdapter(Context mContext, List<PaperListInfo> list) {
		super();
		this.mContext = mContext;
		this.list = list;
		this.miInflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return list.size();
	}

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
		if (convertview == null) {
			viewHolder = new ViewHolder();
			convertview = miInflater.inflate(R.layout.item_paper_list, null);
			viewHolder.tv_id = (TextView) convertview.findViewById(R.id.id);
			viewHolder.tv_title = (TextView) convertview.findViewById(R.id.tv_title);
			viewHolder.tv_author = (TextView) convertview.findViewById(R.id.tv_author);
			viewHolder.tv_summary = (TextView) convertview.findViewById(R.id.tv_summary);
			viewHolder.tv_provenance = (TextView) convertview.findViewById(R.id.tv_provenance);
			viewHolder.tv_date = (TextView) convertview.findViewById(R.id.tv_date);
			convertview.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertview.getTag();
		}
		viewHolder.tv_id.setText(list.get(position).getKnowledgeId());
		viewHolder.tv_title.setText(list.get(position).getPaperTitle());
		viewHolder.tv_author.setText(list.get(position).getAuthor());
		if (list.get(position).getSummary()==null) {
			viewHolder.tv_summary.setVisibility(View.GONE);
		}else{
			viewHolder.tv_summary.setText(list.get(position).getSummary());
		}
		if (list.get(position).getProvenance()==null) {
			viewHolder.tv_provenance.setText("未知");
		}else{
			viewHolder.tv_provenance.setText(list.get(position).getProvenance());
		}
		viewHolder.tv_date.setText(list.get(position).getReleaseDate());
		return convertview;
	}
	
	private class ViewHolder{
		private TextView tv_title;
		private TextView tv_author;
		private TextView tv_summary;
		private TextView tv_provenance;
		private TextView tv_date;
		private TextView tv_id;
	}

}
