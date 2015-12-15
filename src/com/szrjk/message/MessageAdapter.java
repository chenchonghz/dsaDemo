package com.szrjk.message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.szrjk.dhome.R;
import com.szrjk.entity.UserCard;
import com.szrjk.util.DisplayTimeUtil;
import com.szrjk.util.ImageLoaderUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
	private Context mContext;
	private List<MessageEntity> list;
	private LayoutInflater mInflater;
	private UserCard selfUserCard;
	private UserCard objUserCard;
	private static final int TYPE_RIGHT = 0;
	private static final int TYPE_LEFT = 1;
	private static final int TYPE_MAX_COUNT = TYPE_LEFT + 1; 


	public MessageAdapter(Context mContext, List<MessageEntity> list,
			UserCard sendUserCard, UserCard receiveUserCard) {
		super();
		this.mContext = mContext;
		this.list = list;
		this.selfUserCard = sendUserCard;
		this.objUserCard = receiveUserCard;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
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
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		int type = getItemViewType(position);
		if (convertView==null) {
			viewHolder = new ViewHolder();
			switch (type) {
			case TYPE_LEFT:
				convertView = mInflater.inflate(R.layout.left_item_message, null);
				break;
			case TYPE_RIGHT:
				convertView = mInflater.inflate(R.layout.right_item_message,null);
				break;
			}
			viewHolder.iv_head = (ImageView) convertView
					.findViewById(R.id.iv_head);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_talk = (TextView) convertView
					.findViewById(R.id.tv_talk);
			convertView.setTag(viewHolder);

		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		MessageEntity message = list.get(position);
		MessageEntity lastmessage;
		if(position!=0){
			lastmessage = list.get(position-1);
		}else{
			lastmessage =null;
		}
		if (message!=null) {
			try {
				ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil(mContext, message.getSendUserCard().getUserFaceUrl(),
						viewHolder.iv_head, R.drawable.header,R.drawable.header);
				imageLoaderUtil.showImage();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("ImageLoader", e.toString());
			}
			viewHolder.tv_talk.setText(message.getContent());
			if (lastmessage!=null) {
				String lastDate = lastmessage.getCreateDate();
				Log.i("TAG", lastDate);
				if (Math.abs(getdaytime(lastDate) -getdaytime(message.getCreateDate())) < 5 * 60 * 1000)
				{
					viewHolder.tv_time.setVisibility(View.GONE);
				}
				else
				{
					viewHolder.tv_time.setVisibility(View.VISIBLE);
					try {
						viewHolder.tv_time
						.setText(DisplayTimeUtil.displayTimeStringt2message(message.getCreateDate()));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}else{
				viewHolder.tv_time.setVisibility(View.VISIBLE);
				try {
					viewHolder.tv_time.setText(DisplayTimeUtil.displayTimeString(message.getCreateDate()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return convertView;
	}
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return TYPE_MAX_COUNT;
	}
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return (list.get(position).getSendUserCard()
				.getUserSeqId().equals(selfUserCard.getUserSeqId()))
				?TYPE_RIGHT:TYPE_LEFT;
	}


	public static String getDateTimeString(long time)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		return sdf.format(new Date(time));
	}

	private final static class ViewHolder
	{
		public ImageView iv_head;
		public TextView tv_talk;
		public TextView tv_time;
		public long currTime;
	}
	public static long getdaytime(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
		Date dt2 = null;
		try {
			dt2 = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}        
		return dt2.getTime();
	}
}
