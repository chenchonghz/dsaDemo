package com.szrjk.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.szrjk.dhome.R;
import com.szrjk.entity.MessagesEntity;

public class MessagesListAdapter extends BaseAdapter{
	private Context context;
	private List<MessagesEntity> messageList;
	private int flag;
	private ViewHolder viewHolder;
	public MessagesListAdapter(Context context,List<MessagesEntity> messageList,int flag) {
		this.context = context;
		if(messageList!=null){
			this.messageList = messageList;
		}else{
			this.messageList = new ArrayList<MessagesEntity>();
		}
		this.flag = flag;
	}

	@Override
	public int getCount() {
		return messageList.size();
	}

	@Override
	public Object getItem(int position) {
		return messageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = View.inflate(context, R.layout.view_messages, null);
			 viewHolder = new ViewHolder();
			 viewHolder.iv_smallPhoto = (ImageView)convertView.findViewById(R.id.iv_smallphoto);
			 viewHolder.iv_head_icon = (ImageView)convertView.findViewById(R.id.iv_yellow_icon);
			 viewHolder.bt_unRead_num = (Button)convertView.findViewById(R.id.bt_unread_num);
			 viewHolder.tv_messageName = (TextView)convertView.findViewById(R.id.tv_message_name);
			 viewHolder.tv_messageContent = (TextView)convertView.findViewById(R.id.tv_message_content);
			 viewHolder.tv_messageTime = (TextView)convertView.findViewById(R.id.tv_messageTime);
			 convertView.setTag(viewHolder);
		}else{
		     viewHolder = (ViewHolder) convertView.getTag();
		}
		MessagesEntity message = messageList.get(position);
		
		// TODO Auto-generated method stub
		return convertView;
	}

	class ViewHolder{
		ImageView iv_smallPhoto,iv_head_icon;
		Button bt_unRead_num;
		TextView tv_messageName,tv_messageContent,tv_messageTime;
	}
}
