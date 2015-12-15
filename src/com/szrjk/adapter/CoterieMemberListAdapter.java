package com.szrjk.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.szrjk.dhome.R;
import com.szrjk.entity.UserCard;
import com.szrjk.widget.UserCardLayout;

public class CoterieMemberListAdapter extends BaseAdapter{
	
	private Context context;
	private List<UserCard> members;
	private LayoutInflater inflater;
	
	public CoterieMemberListAdapter(Context context,List<UserCard> members) {
		this.context=context;
		this.members=members;
		this.inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return members.size();
	}

	@Override
	public Object getItem(int position) {
		return members.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		convertView=inflater.inflate(R.layout.item_coterie_member, null);
		UserCardLayout userCardLayout=(UserCardLayout) convertView.findViewById(R.id.ucl_usercardlayout);
		userCardLayout.setContext(context);
		userCardLayout.setUser(members.get(position));
		userCardLayout.closeClick();
		/*TextView professionalTitle = (TextView) userCardLayout.findViewById(R.id.tv_user_type);
		professionalTitle.setTextColor(context.getResources().getColor(R.color.red));*/
		return convertView;
	}
}
