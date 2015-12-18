package com.szrjk.adapter;

import java.util.List;

import com.szrjk.dhome.R;
import com.szrjk.entity.FriendList;
import com.szrjk.widget.UserCardLayout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyFriendListAdapter extends BaseAdapter{
	private Context mContext;
	private List<FriendList> list;
	private LayoutInflater mInflater;
	
	public MyFriendListAdapter(Context mContext, List<FriendList> list) {
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
		if(convertview==null){
			viewHolder= new ViewHolder();
			convertview = mInflater.inflate(R.layout.item_list_myfriend, null);
			viewHolder.userCard = (UserCardLayout) convertview.findViewById(R.id.myfriend_usercard);
			convertview.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertview.getTag();
		}
			viewHolder.userCard.setContext(mContext);
			com.szrjk.entity.UserCard userCard = list.get(position).getUserCard();
			viewHolder.userCard.setUser(userCard);
			viewHolder.userCard.closeClick();
		return convertview;
	}
	 class ViewHolder{
		 private UserCardLayout userCard;
	 }
	 @Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	
}
