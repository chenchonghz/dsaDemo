package com.szrjk.self;

import java.util.ArrayList;

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

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.dhome.R;
import com.szrjk.entity.RemindEvent;
import com.szrjk.entity.UserHomePageInfo;

import de.greenrobot.event.EventBus;


@ContentView(R.layout.activity_circle)
public class CircleActivity extends FragmentActivity implements OnClickListener {

	private CircleActivity instance;
	private ArrayList<Fragment> list;
	private MyCircleFragment myCircleFragment = new MyCircleFragment();
	private CircleRequestFragment circleRequestFragment = new CircleRequestFragment();

	@ViewInject(R.id.vp_circle)//viewpager
	private ViewPager vp_circle;
	@ViewInject(R.id.rly_mycircle)
	private RelativeLayout rly_mycircle;
	@ViewInject(R.id.rly_circlerequest)
	private RelativeLayout rly_circlerequest;
	@ViewInject(R.id.iv_mycircle)
	private ImageView iv_mycircle;
	@ViewInject(R.id.tv_mycircle)
	private TextView tv_mycircle;
	@ViewInject(R.id.v_line1)
	private View v_line1;
	@ViewInject(R.id.iv_circlerequest)
	private ImageView iv_circlerequest;
	@ViewInject(R.id.tv_circlerequest)
	private TextView tv_circlerequest;
	@ViewInject(R.id.v_line2)
	private View v_line2;
	@ViewInject(R.id.iv_remind)
	private static ImageView iv_remind;
	
	private MypagerAdapter adapter;
	private UserHomePageInfo userhomepageinfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		rly_circlerequest.setOnClickListener(instance);
		rly_mycircle.setOnClickListener(instance);
		vp_circle.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					clickMycircle();
					break;

				case 1:
					clickcircleRequest();
					break;
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		initLayout();
	}

	private void initLayout() {
		Intent i = instance.getIntent();
		userhomepageinfo = (UserHomePageInfo) i.getSerializableExtra("UserHomePageInfo");
		list = new ArrayList<Fragment>();
		list.add(myCircleFragment);
		list.add(circleRequestFragment);
		adapter = new MypagerAdapter(getSupportFragmentManager());
		vp_circle.setAdapter(adapter);
	}
	
	private void clickcircleRequest() {
		EventBus.getDefault().post(new RemindEvent(31));
		iv_mycircle.setImageResource(R.drawable.icon_circle1);
		tv_mycircle.setTextColor(getResources().getColor(R.color.font_titleanduname));
		v_line1.setVisibility(View.GONE);
		iv_circlerequest.setImageResource(R.drawable.icon_circlerequest2);
		tv_circlerequest.setTextColor(getResources().getColor(R.color.global_main));
		v_line2.setVisibility(View.VISIBLE);
	}
	private void clickMycircle() {
		iv_mycircle.setImageResource(R.drawable.icon_circle2);
		tv_mycircle.setTextColor(getResources().getColor(R.color.global_main));
		v_line1.setVisibility(View.VISIBLE);
		iv_circlerequest.setImageResource(R.drawable.icon_circlerequest1);
		tv_circlerequest.setTextColor(getResources().getColor(R.color.font_titleanduname));
		v_line2.setVisibility(View.GONE);
	}
	
	class MypagerAdapter extends FragmentPagerAdapter{
		
		public MypagerAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int position) {
			return list.get(position);
		}
		@Override
		public int getCount() {
			return list.size();
		}
	}
	
	public UserHomePageInfo getUserInfo(){
		return userhomepageinfo;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rly_circlerequest:
			clickcircleRequest();
			vp_circle.setCurrentItem(1);
			break;

		case R.id.rly_mycircle:
			clickMycircle();
			vp_circle.setCurrentItem(0);
			break;
		}
		
	}
	public static void changeremind(int flag){
		switch (flag) {
		case 1:	iv_remind.setVisibility(View.VISIBLE);
		break;
		case 2: iv_remind.setVisibility(View.GONE);
		break;
		}
	
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		myCircleFragment.lv_my_circle.setAdapter(null);
		myCircleFragment.getCircles();
		
		super.onNewIntent(intent);
	}

}
