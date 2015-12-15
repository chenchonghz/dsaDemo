package com.szrjk.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szrjk.config.Constant;
import com.szrjk.dhome.MainActivity;
import com.szrjk.dhome.OtherPeopleActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.NewsCommentEntity;
import com.szrjk.entity.UserCard;
import com.szrjk.self.SystemUserActivity;
import com.szrjk.util.DisplayTimeUtil;
import com.szrjk.util.ImageLoaderUtil;

public class CommendListAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<NewsCommentEntity> commendList;

	public CommendListAdapter(Context context, ArrayList<NewsCommentEntity> commendList) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;
		if(commendList != null){
			this.commendList = commendList;
		}else{
			commendList = new ArrayList<NewsCommentEntity>();
		}		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commendList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return commendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = View.inflate(context, R.layout.view_commend_doctorinfo, null);
			viewHolder.iv_smallPhoto = (ImageView)convertView.findViewById(R.id.iv_smallphoto);
			viewHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			viewHolder.tv_jobTitle = (TextView)convertView.findViewById(R.id.tv_jobtitle);
			viewHolder.tv_hospital = (TextView)convertView.findViewById(R.id.tv_hospital);
			viewHolder.tv_department = (TextView)convertView.findViewById(R.id.tv_department);
			viewHolder.ib_smallCommend = (ImageButton)convertView.findViewById(R.id.ib_small_commend);
			viewHolder.iv_yellow_icon = (ImageView)convertView.findViewById(R.id.iv_yellow_icon);
			viewHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
			viewHolder.tv_commend_text = (TextView)convertView.findViewById(R.id.tv_commend_text);
			viewHolder.rl_usercard = (RelativeLayout)convertView.findViewById(R.id.rl_usercard);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		NewsCommentEntity comments = commendList.get(position);
		UserCard userCard = comments.getSmallCard();
		if(comments == null){
			return convertView;
		}else{
			if(userCard == null || userCard.getUserName() == null || userCard.getUserName().isEmpty()){
				viewHolder.tv_name.setVisibility(View.GONE);
				viewHolder.iv_smallPhoto.setVisibility(View.GONE);
				viewHolder.iv_yellow_icon.setVisibility(View.GONE);
				viewHolder.tv_department.setVisibility(View.GONE);
				viewHolder.tv_hospital.setVisibility(View.GONE);
				viewHolder.tv_jobTitle.setVisibility(View.GONE);
				viewHolder.tv_department.setVisibility(View.GONE);
			}else{
				viewHolder.tv_name.setVisibility(View.VISIBLE);
				viewHolder.tv_name.setText(userCard.getUserName());
				viewHolder.iv_smallPhoto.setVisibility(View.VISIBLE);
//				viewHolder.iv_yellow_icon.setVisibility(View.VISIBLE);
				viewHolder.tv_department.setVisibility(View.VISIBLE);
				viewHolder.tv_hospital.setVisibility(View.VISIBLE);
				viewHolder.tv_jobTitle.setVisibility(View.VISIBLE);
				viewHolder.tv_department.setVisibility(View.VISIBLE);
				try {
					ImageLoaderUtil imageLoader = new ImageLoaderUtil(context, userCard.getUserFaceUrl(), viewHolder.iv_smallPhoto, R.drawable.icon_headfailed, R.drawable.icon_headfailed);
					imageLoader.showImage();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("ImageLoader", e.toString());
				}
			}
			if(userCard != null){
				if(userCard.getUserLevel() != null && !userCard.getUserLevel().isEmpty()){
					if(userCard.getUserLevel().equals("11")){
						viewHolder.iv_yellow_icon.setVisibility(View.VISIBLE);
					}else{
						viewHolder.iv_yellow_icon.setVisibility(View.GONE);
					}
				}
				if(userCard.getProfessionalTitle()!=null && !userCard.getProfessionalTitle().isEmpty()){	
						viewHolder.tv_jobTitle.setText(userCard.getProfessionalTitle());
				}else{
					viewHolder.tv_jobTitle.setText("");
				}
				if(userCard.getCompanyName()!=null && !userCard.getCompanyName().isEmpty()){
					viewHolder.tv_hospital.setText(userCard.getCompanyName());
				}else{
					viewHolder.tv_hospital.setText("");
				}
				if(userCard.getDeptName()!=null && !userCard.getDeptName().isEmpty()){
					viewHolder.tv_department.setText(userCard.getDeptName());
				}else{
					viewHolder.tv_department.setText("");
				}
			}
			
			if(comments.getCommentTime()!=null && !comments.getCommentTime().isEmpty()){
				try {
					String time = DisplayTimeUtil.displayDetailTimeString(comments.getCommentTime());
					viewHolder.tv_time.setText(time);
				} catch (Exception e) {
					Log.e("ExploreDetail", e.toString());
				}
			}else{
				viewHolder.tv_time.setText("");
			}
			if(comments.getCommentContent()!=null && !comments.getCommentContent().isEmpty()){
				viewHolder.tv_commend_text.setText(comments.getCommentContent());
			}else{
				viewHolder.tv_commend_text.setText("");
			}
			setListner(viewHolder.rl_usercard,userCard);
			
		}
		return convertView;
	}
	
	private void setListner(RelativeLayout rl_usercard, final UserCard userCard) {
		// TODO Auto-generated method stub
		rl_usercard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(userCard != null){	
					if(userCard.getUserSeqId().equals(Constant.userInfo.getUserSeqId())){
						skipToSelfFragment();
					}else if(userCard.getUserType().equals("1")){
						skipToSystemUserActivity(userCard.getUserSeqId());
					}else{			
						skipToOtherPeopleActivity(userCard.getUserSeqId());
					}
				}
			}
		});
	}

	protected void skipToOtherPeopleActivity(String userSeqId) {
		Intent intent = new Intent(context, OtherPeopleActivity.class);
		intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
		context.startActivity(intent);
	}

	protected void skipToSystemUserActivity(String userSeqId) {
		Intent intent = new Intent(context, SystemUserActivity.class);
		intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
		context.startActivity(intent);
	}

	protected void skipToSelfFragment() {
//		MainActivity.getMainActivity().skipToSelfFragment();
		Intent intent = new Intent(context, MainActivity.class);
		intent.putExtra("isSkipToSelf", true);
		context.startActivity(intent);
	}

	class ViewHolder{
		ImageView iv_smallPhoto,iv_yellow_icon;
		TextView tv_name,tv_jobTitle,tv_hospital,tv_department,tv_time,tv_commend_text;
		ImageButton ib_smallCommend;
		RelativeLayout rl_usercard;
	}

}
