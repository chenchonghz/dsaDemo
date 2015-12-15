package com.szrjk.adapter;

import java.util.HashMap;
import java.util.List;

import com.szrjk.dhome.R;
import com.szrjk.entity.UserCard;
import com.szrjk.widget.UserCardLayout;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public class CircleInviteAdapter extends BaseAdapter {
	private List<UserCard> list;
	private static  HashMap<String, Boolean> isSelected;
	private Context mContext;
	private LayoutInflater inflater;
	private int Flag = 0;

	public CircleInviteAdapter(List<UserCard> list,
			Context mContext) {
		super();
		this.list = list;
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
		isSelected = new HashMap<String, Boolean>();  
		initDate();  
		Flag = 1;
		// 初始化数据  
	}
	
	public void initDate() {
		for (int i = 0; i < list.size(); i++) {  
			isSelected.put(list.get(i).getUserSeqId(), false);  
		} 
		Log.i("TAG", "initDate is ok！");
	}
	
	
	public  HashMap<String, Boolean> getIsSelected() {  
		return isSelected;  
	}  

	public  void setIsSelected(HashMap<String, Boolean> isSelected) {  
		CircleInviteAdapter.isSelected = isSelected;  
	}  
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder= null;
		if (convertView==null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_circle_invite, null);
			viewHolder.userCard = (UserCardLayout) convertView.findViewById(R.id.circle_usercard);
			viewHolder.cb = (CheckBox) convertView.findViewById(R.id.item_cb);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.userCard.setContext(mContext);
		viewHolder.userCard.setUser((UserCard)list.get(position));
		// 根据isSelected来设置checkbox的选中状况
		if (isSelected.get(list.get(position).getUserSeqId())!=null) {
			viewHolder.cb.setChecked(isSelected.get(list.get(position).getUserSeqId()));
		}else{
			initDate();
			getIsSelected().put(list.get(position).getUserSeqId(),false);
			viewHolder.cb.setChecked(false);
		}
		
		return convertView;
	}
	public static class ViewHolder {  
        UserCardLayout userCard;  
        CheckBox cb;
		public UserCardLayout getUserCard() {
			return userCard;
		}
		public void setUserCard(UserCardLayout userCard) {
			this.userCard = userCard;
		}
		public CheckBox getCb() {
			return cb;
		}
		public void setCb(CheckBox cb) {
			this.cb = cb;
		}  
        
    }  
	public int getFlag(){
		return Flag;
	}

}
