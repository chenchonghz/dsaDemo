package com.szrjk.self;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.dhome.R.layout;
import com.szrjk.dhome.R.menu;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.UserCard;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.HeaderView;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_circle_invite_firend)
public class CircleInviteFirendActivity extends FragmentActivity implements OnClickListener {
	@ViewInject(R.id.hv_invite)
	private HeaderView hv_invite;
	@ViewInject(R.id.tv_invite)
	private TextView tv_invite;
	@ViewInject(R.id.rly_friend)
	private RelativeLayout rly_friend;
	@ViewInject(R.id.tv_friend)
	private TextView tv_friend;
	@ViewInject(R.id.rly_follow)
	private RelativeLayout rly_follow;
	@ViewInject(R.id.tv_follow)
	private TextView tv_follow;
	@ViewInject(R.id.rly_fans)
	private RelativeLayout rly_fans;
	@ViewInject(R.id.tv_fans)
	private TextView tv_fans;
	@ViewInject(R.id.v_line1)
	private View v_line1;
	@ViewInject(R.id.v_line2)
	private View v_line2;
	@ViewInject(R.id.v_line3)
	private View v_line3;
	@ViewInject(R.id.vp_invite)
	private ViewPager vp_invite;
	private CircleInviteFirendActivity instance;
	private List<Fragment> list;
	private String circleID;
	private InviteFriendFragment inviteFriendFragment ;
	private InviteFollowFragment invitefollowFragment;
	private InviteFansFragment invitefansFragment;
	private List<String> friend = new ArrayList<String>();
	private LinearLayout lly_btn;
	private Dialog dialog;
	private String fromCreate;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		Intent intent = getIntent();
		circleID = intent.getStringExtra(Constant.CIRCLE);
		fromCreate = intent.getStringExtra("Create");
		vp_invite.setOffscreenPageLimit(3);  
		lly_btn = hv_invite.getLLy();
		initListener();
		//延迟设置
		dialog = createDialog(this, "发送中，请稍候...");


	}
	private void initListener() {
		rly_fans.setOnClickListener(instance);
		rly_follow.setOnClickListener(instance);
		rly_friend.setOnClickListener(instance);
		tv_invite.setOnClickListener(instance);
		lly_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (fromCreate==null) {
					finish();
				}else if (fromCreate.equals("YES")) {
					Intent intent = new Intent(instance, CircleActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(intent);
				}
			}
		});
		vp_invite.addOnPageChangeListener(new OnPageChangeListener() {

			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					clickfriend();
					break;
				case 1:
					clickfollow();
					break;
				case 2:
					clickfans();
					break;
				}
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			public void onPageScrollStateChanged(int arg0) {

			}
		});
		inviteFriendFragment = new InviteFriendFragment();
		invitefollowFragment = new InviteFollowFragment();
		invitefansFragment = new InviteFansFragment();

		list = new ArrayList<Fragment>();
		list.add(inviteFriendFragment);
		list.add(invitefollowFragment);
		list.add(invitefansFragment);
		MypagerAdapter adpater = new MypagerAdapter(getSupportFragmentManager());
		vp_invite.setAdapter(adpater);
	}
	class MypagerAdapter extends FragmentPagerAdapter{

		public MypagerAdapter(FragmentManager fm) {
			super(fm);
		}
		public Fragment getItem(int position) {
			return list.get(position);
		}
		public int getCount() {
			return list.size();
		}
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rly_friend:
			clickfriend();
			vp_invite.setCurrentItem(0);
			break;
		case R.id.rly_follow:
			clickfollow();
			vp_invite.setCurrentItem(1);
			break;
		case R.id.rly_fans:
			clickfans();
			vp_invite.setCurrentItem(2);
			break;
		case R.id.tv_invite:
			if (friend.size()==0) {
				ToastUtils.showMessage(instance, "您还没有选择好友");
			}else{
				invite();
//				Log.i("TAG", friend.toString());
			}
			break;
		}
	}
	private void invite() {

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "inviteUserIntoCoterie");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId",Constant.userInfo.getUserSeqId());
		busiParams.put("coterieId",circleID);
		busiParams.put("objUserSeqIds",friend);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {

			public void success(JSONObject jsonObject) {
				dialog.dismiss();
				tv_invite.setClickable(true);

				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					if (fromCreate!=null&&fromCreate.equals("YES")) {
						ToastUtils.showMessage(instance, "已发送邀请");
						Intent intent = new Intent(instance, CircleActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(intent);

					}else{

						finish();
					}
				}
			}

			public void start() {
//				dialog.setCancelable(false);
				dialog.show();
				tv_invite.setClickable(false);
			}

			public void loading(long total, long current, boolean isUploading) {
			}

			public void failure(HttpException exception, JSONObject jobj) {
				runOnUiThread(new Runnable() {
					public void run() {
						dialog.dismiss();
						ToastUtils.showMessage(instance, "邀请失败，再试一次");
						tv_invite.setClickable(true);
					}
				});


			}
		});

	}
	private void clickfans() {
		tv_friend.setTextColor(getResources().getColor(R.color.font_titleanduname));
		tv_follow.setTextColor(getResources().getColor(R.color.font_titleanduname));
		tv_fans.setTextColor(getResources().getColor(R.color.global_main));
		v_line1.setVisibility(View.GONE);
		v_line2.setVisibility(View.GONE);
		v_line3.setVisibility(View.VISIBLE);

	}
	private void clickfollow() {
		tv_friend.setTextColor(getResources().getColor(R.color.font_titleanduname));
		tv_follow.setTextColor(getResources().getColor(R.color.global_main));
		tv_fans.setTextColor(getResources().getColor(R.color.font_titleanduname));
		v_line1.setVisibility(View.GONE);
		v_line2.setVisibility(View.VISIBLE);
		v_line3.setVisibility(View.GONE);
	}
	private void clickfriend() {
		tv_friend.setTextColor(getResources().getColor(R.color.global_main));
		tv_follow.setTextColor(getResources().getColor(R.color.font_titleanduname));
		tv_fans.setTextColor(getResources().getColor(R.color.font_titleanduname));
		v_line1.setVisibility(View.VISIBLE);
		v_line2.setVisibility(View.GONE);
		v_line3.setVisibility(View.GONE);

	}

	//传userid fragment位置 和点击后的选择状态  更改其他列表同id的选择状态
	//	public void setCheckbox(String userID,boolean state){
	//		invitefansFragment.setUidStatus(userID, state);
	//		invitefollowFragment.setUidStatus(userID, state);
	//		inviteFriendFragment.setUidStatus(userID, state);
	//	}

	public void changefriend(String userid,boolean state){
		if (state==true) {
			friend.add(userid);
			Log.i("TAG", friend.toString());
		}else{
			for (int i = 0; i < friend.size(); i++) {
				if (friend.get(i).equals(userid)) {
					friend.remove(i);
					Log.i("TAG", friend.toString());
				}
			}
		}
	}
	public  Dialog createDialog(Context context, String msg)
	{
		Dialog dialog = new Dialog(context, R.style.loading_dialog);
		dialog.setContentView(R.layout.loading_layout);
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		TextView textView = (TextView) dialog.findViewById(R.id.tv_msg);
		textView.setText(msg);
		return dialog;
	}
	protected void onDestroy() {
		inviteFriendFragment = null;
		invitefollowFragment = null;
		invitefansFragment = null;
		super.onDestroy();
	}

}
