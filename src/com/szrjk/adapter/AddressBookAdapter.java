package com.szrjk.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.szrjk.dhome.R;
import com.szrjk.entity.AddressCard;
import com.szrjk.entity.AddressListEntity;
import com.szrjk.util.ImageLoaderUtil;

public class AddressBookAdapter extends BaseAdapter implements SectionIndexer{
	private List<AddressListEntity> list = null;
	private Context mContext;
	public AddressBookAdapter(Context mContext, List<AddressListEntity> sourceDateList) {
		this.mContext = mContext;
		if (sourceDateList == null) {
			this.list = new ArrayList<AddressListEntity>();
		}else{
			this.list = sourceDateList;
		}
		
	}
	
	/**
	 * ��ListView��ݷ���仯ʱ,���ô˷���������ListView
	 * @param list
	 */
	public void updateListView(List<AddressListEntity> list){
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return this.list.size();
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
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final AddressCard mContent = list.get(position).getUserCard();
//		mContent.setSortLetters(mContent.getFirstLetter());
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.address_book_item, null);
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.rl_zmu  = (RelativeLayout)view.findViewById(R.id.rl_zm);
			viewHolder.iv_smallphoto = (ImageView) view.findViewById(R.id.iv_smallphoto);
			viewHolder.iv_yellow_icon = (ImageView) view.findViewById(R.id.iv_yellow_icon);
			viewHolder.tv_jobtitle = (TextView) view.findViewById(R.id.tv_jobtitle);
			viewHolder.tv_hospital = (TextView) view.findViewById(R.id.tv_hospital);
			viewHolder.tv_department = (TextView) view.findViewById(R.id.tv_department);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		//���position��ȡ���������ĸ��Char asciiֵ
		int section = getSectionForPosition(position);
		
		//���ǰλ�õ��ڸ÷�������ĸ��Char��λ�� ������Ϊ�ǵ�һ�γ���
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
			viewHolder.rl_zmu.setVisibility(View.VISIBLE);
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
			viewHolder.rl_zmu.setVisibility(View.GONE);
		}
		
		AddressCard data = list.get(position).getUserCard();
		//bitmapUtils.display(viewHolder.iv_smallphoto, data.getUserFaceUrl());
		ImageLoaderUtil loaderUtil = new ImageLoaderUtil(mContext, data.getUserFaceUrl(),
				viewHolder.iv_smallphoto,R.drawable. pic_downloadfailed_bg, R.drawable.pic_downloadfailed_bg);
		loaderUtil.showImage();
		if ("11".equals(data.getUserLevel())) {
			viewHolder.iv_yellow_icon.setVisibility(View.VISIBLE);
		}else{
			viewHolder.iv_yellow_icon.setVisibility(View.GONE);
		}
		viewHolder.tv_name.setText(data.getUserName());
		viewHolder.tv_jobtitle.setText(data.getProfessionalTitle());
		viewHolder.tv_hospital.setText(data.getCompanyName());
		viewHolder.tv_department.setText(data.getDeptName());
		
		return view;

	}
	


	final static class ViewHolder {
		TextView tvLetter;
		TextView tv_name;
		TextView tv_jobtitle;
		TextView tv_hospital;
		TextView tv_department;
		RelativeLayout rl_zmu;
		ImageView iv_smallphoto;
		ImageView iv_yellow_icon;
		RelativeLayout rl_item;
	}


	/**
	 * ���ListView�ĵ�ǰλ�û�ȡ���������ĸ��Char asciiֵ
	 */
	@Override
	public int getSectionForPosition(int position) {
		return list.get(position).getUserCard().getSortLetters().charAt(0);
	}

	/**
	 * ��ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
	 */
	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getUserCard().getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * ��ȡӢ�ĵ�����ĸ����Ӣ����ĸ��#���档
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}