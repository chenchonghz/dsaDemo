package com.szrjk.self;

import java.util.ArrayList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.dhome.R;
import com.szrjk.entity.RemindEvent;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
@ContentView(R.layout.activity_friend)
public class FriendActivity extends FragmentActivity implements OnClickListener
{
@ViewInject(R.id.vp_friend)
private ViewPager vp_friend;
@ViewInject(R.id.rly_myfriend)
private RelativeLayout rly_myfriend;
@ViewInject(R.id.iv_myfriend)
private ImageView iv_myfriend;
@ViewInject(R.id.tv_myfriend)
private TextView tv_myfriend;
@ViewInject(R.id.v_line1)
private View v_line1;
@ViewInject(R.id.rly_friendrequest)
private RelativeLayout rly_friendrequest;
@ViewInject(R.id.iv_friendrequest)
private ImageView iv_friendrequest;
@ViewInject(R.id.tv_friendrequest)
private TextView tv_friendrequest;
@ViewInject(R.id.v_line2)
private View v_line2;
@ViewInject(R.id.iv_remind)
private static ImageView iv_remind;
private MyFriendFragment myFriendFragment = new MyFriendFragment();;
private FriendRequestFragment friendRequestFragment = new FriendRequestFragment();;
private ArrayList<Fragment> Flist;
private MypagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		initListener();
		Flist = new ArrayList<Fragment>();
		Flist.add(myFriendFragment);
		Flist.add(friendRequestFragment);
		adapter = new MypagerAdapter(getSupportFragmentManager());
		vp_friend.setAdapter(adapter);
	}

	private void initListener() {
	rly_friendrequest.setOnClickListener(this);
	rly_myfriend.setOnClickListener(this);
	vp_friend.addOnPageChangeListener(new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 0:
				clickMyFriend();
				break;

			case 1:
				clickFriendRequest();
				break;
			}
			
		}
		
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		public void onPageScrollStateChanged(int arg0) {
			
		}
	});
	}


	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rly_myfriend:
			clickMyFriend();
			vp_friend.setCurrentItem(0);
			
				break;

			case R.id.rly_friendrequest:
			clickFriendRequest();
			vp_friend.setCurrentItem(1);
			
				break;
	
		}
	}


	private void clickFriendRequest() {
		EventBus.getDefault().post(new RemindEvent(21));
		iv_myfriend.setImageResource(R.drawable.icon_friend1);
		tv_myfriend.setTextColor(getResources().getColor(R.color.font_titleanduname));
		v_line1.setVisibility(View.GONE);
		iv_friendrequest.setImageResource(R.drawable.icon_friend_request2);
		tv_friendrequest.setTextColor(getResources().getColor(R.color.global_main));
		v_line2.setVisibility(View.VISIBLE);
	}
	public static void changeremind(int flag){
		switch (flag) {
		case 1:	iv_remind.setVisibility(View.VISIBLE);
		break;
		case 2: iv_remind.setVisibility(View.GONE);
		break;
		}
	
	}

	private void clickMyFriend() {
		iv_myfriend.setImageResource(R.drawable.icon_friend2);
		tv_myfriend.setTextColor(getResources().getColor(R.color.global_main));
		v_line1.setVisibility(View.VISIBLE);
		iv_friendrequest.setImageResource(R.drawable.icon_friend_request1);
		tv_friendrequest.setTextColor(getResources().getColor(R.color.font_titleanduname));
		v_line2.setVisibility(View.GONE);
	}
	
	class MypagerAdapter extends FragmentPagerAdapter{

		public MypagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public Fragment getItem(int position) {
			return Flist.get(position);
		}

		public int getCount() {
			return Flist.size();
		}
	}
}