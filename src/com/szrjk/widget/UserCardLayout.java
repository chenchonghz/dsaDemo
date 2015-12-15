package com.szrjk.widget;

import com.szrjk.config.Constant;
import com.szrjk.dhome.OtherPeopleActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.UserCard;
import com.szrjk.util.BusiUtils;
import com.szrjk.util.DialogUtil;
import com.szrjk.util.ImageLoaderUtil;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *
 */
public class UserCardLayout extends RelativeLayout{
	private Context mContext;
	private ImageView user_face;
	private TextView user_name;
	private TextView user_ptitle;
	private TextView user_company;
	private TextView user_dept;
	private ImageView user_vip;
	private RelativeLayout rl_usercard;

	//	public UserCardLayout(Context context) {
	//		super(context);
	//		this.mContext = context;
	//	}


	public Context getmContext() {
		return mContext;
	}


	@Override
	public void setBackgroundColor(int colorid){
		rl_usercard.setBackgroundColor(colorid);
	}


	public void setContext(Context mContext) {
		this.mContext = mContext;
	}


	public UserCardLayout(Context context, AttributeSet attributeSet)
	{
		super(context, attributeSet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.usercard, this);
		user_face = (ImageView) findViewById(R.id.iv_user_avatar);
		user_name = (TextView) findViewById(R.id.tv_user_name);
		user_ptitle = (TextView) findViewById(R.id.tv_user_type);
		user_dept = (TextView) findViewById(R.id.tv_user_dept);
		user_company = (TextView) findViewById(R.id.tv_user_hospital);
		rl_usercard = (RelativeLayout) findViewById(R.id.rl_usercard);
		user_vip = (ImageView) findViewById(R.id.iv_vip);
		
		this.mContext = context;


	}
	public void setUser(final com.szrjk.entity.UserCard userCard){
		try {
			ImageLoaderUtil imageLoaderUtil = 
					new ImageLoaderUtil(mContext, userCard.getUserFaceUrl(), user_face, 
							R.drawable.icon_headfailed, R.drawable.icon_headfailed);
			imageLoaderUtil.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		user_name.setText(userCard.getUserName());
		user_ptitle.setText(userCard.getProfessionalTitle());
		user_company.setText(userCard.getCompanyName());
		user_dept.setText(userCard.getDeptName());
		
		//黄v显示判断，以后加上蓝v显示判断
		if (userCard.getUserLevel().equals("11")) {
			user_vip.setVisibility(View.VISIBLE);
		}
		rl_usercard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(BusiUtils.isguest(mContext)){
					//如果是游客,则弹框
					DialogUtil.showGuestDialog(getContext(),null);
				}else if (v.getId()==R.id.rl_usercard&&
						!Constant.userInfo.getUserSeqId().equals(userCard.getUserSeqId())) {
					Intent intent = new Intent(mContext, OtherPeopleActivity.class);
					intent.putExtra(Constant.USER_SEQ_ID, userCard.getUserSeqId());
					mContext.startActivity(intent);
					Log.i("TAG", userCard.toString());
				}

			}
		});
	}
	public void closeClick(){
		rl_usercard.setClickable(false);
	}

	public void openClick(){
		rl_usercard.setClickable(true);
	}
	public void changeline(UserCard userCard){
		if (userCard.getCompanyName().length()+userCard.getDeptName().length()>18) {
			RelativeLayout.LayoutParams layoutParams = 
					new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.BELOW, R.id.tv_user_hospital);
//			layoutParams.setMargins(0, 10, 0, 0);
			layoutParams.setMargins(0, 3, 0, 0);
			user_dept.setLayoutParams(layoutParams);
		}else{
			RelativeLayout.LayoutParams layoutParams = 
					new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.tv_user_hospital);
			layoutParams.setMargins(10, 0, 0, 0);
			user_dept.setLayoutParams(layoutParams);
		}
	}
}
