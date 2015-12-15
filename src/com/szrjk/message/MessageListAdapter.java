package com.szrjk.message;

import java.util.List;

import com.szrjk.dhome.R;
import com.szrjk.util.DDateUtils;
import com.szrjk.util.DisplayTimeUtil;
import com.szrjk.widget.UserCardLayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageListAdapter extends BaseAdapter {
	private Context mContext;
	private List<MessageListEntity> list;
	private LayoutInflater mInflater;
	private int size;
	
	public MessageListAdapter(Context mContext, List<MessageListEntity> list) {
		super();
		this.mContext = mContext;
		this.list = list;
		this.mInflater = LayoutInflater.from(mContext);
	}
	

	public MessageListAdapter(Context mContext, List<MessageListEntity> list,
			 int size) {
		super();
		this.mContext = mContext;
		this.list = list;
		this.mInflater =LayoutInflater.from(mContext);
		this.size = size;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView==null) {
			convertView = mInflater.inflate(R.layout.item_message_list,null);
			viewHolder = new ViewHolder();
			viewHolder.usercard = (UserCardLayout) convertView.findViewById(R.id.message_usercard);
			viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.iv_unread = (ImageView) convertView.findViewById(R.id.iv_unread);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.usercard.setContext(mContext);
		viewHolder.usercard.setUser(list.get(position).getSendUserCard());
		viewHolder.usercard.closeClick();
		String date = list.get(position).getCreateDate();
		try {
			date = DDateUtils.dformatOldstrToNewstr(date,"yyyy-MM-dd HH:mm:SS", "yyyy-MM-dd HH:mm");
			date = DisplayTimeUtil.displayTimeString(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		viewHolder.tv_date.setText(date);
		if (position<size) {
			viewHolder.iv_unread.setVisibility(View.VISIBLE);
		}else{
			viewHolder.iv_unread.setVisibility(View.GONE);
		}
		return convertView;
	}
	private class ViewHolder{
		private UserCardLayout usercard;
		private TextView tv_date;
		private ImageView iv_unread;
	}
	

}
