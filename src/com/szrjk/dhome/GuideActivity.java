package com.szrjk.dhome;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.szrjk.adapter.ViewPagerAdapter;
import com.szrjk.config.Constant;
import com.szrjk.util.SharePerferenceUtil;
import com.umeng.analytics.MobclickAgent;

@ContentView(R.layout.activity_guide)
public class GuideActivity extends FragmentActivity
{
	private ViewPager viewPager;
	/**
	 * 引导页显示内容的View
	 */
	private LinearLayout layout1, layout2, layout3, layout4;
	private RelativeLayout layout5;

	private ImageView imgregister;
	/**
	 * 存放显示内容的View
	 */
	private List<View> mViews = new ArrayList<View>();
	private SharePerferenceUtil perferenceUtil;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		perferenceUtil = SharePerferenceUtil.getInstance(this,
				Constant.APP_INFO);
		LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		/**
		 * 获取要显示的引导页内容
		 */
		layout1 = new LinearLayout(this);
		layout1.setLayoutParams(params);
		layout1.setOrientation(LinearLayout.VERTICAL);
		layout1.setBackgroundResource(R.drawable.app1);

		layout2 = new LinearLayout(this);
		layout2.setLayoutParams(params);
		layout2.setOrientation(LinearLayout.VERTICAL);
		layout2.setBackgroundResource(R.drawable.app2);

		layout3 = new LinearLayout(this);
		layout3.setLayoutParams(params);
		layout3.setOrientation(LinearLayout.VERTICAL);
		layout3.setBackgroundResource(R.drawable.app3);

		layout4 = new LinearLayout(this);
		layout4.setLayoutParams(params);
		layout4.setOrientation(LinearLayout.VERTICAL);
		layout4.setBackgroundResource(R.drawable.app4);

		// 添加第三张引导页
		layout5 = (RelativeLayout) getLayoutInflater().inflate(R.layout.item_guide,
				null);
		// 添加注册图片
//		imgregister = (ImageView) layout5.findViewById(R.id.iv_experience);

		findViewById();
		setListener();
		/**
		 * 添加View
		 */
		mViews.add(layout1);
		mViews.add(layout2);
		mViews.add(layout3);
		mViews.add(layout4);
		mViews.add(layout5);
		/**
		 * ViewPager设置适配器
		 */
		viewPager.setAdapter(new ViewPagerAdapter(mViews));
	}

	/**
	 * 绑定界面UI
	 */
	private void findViewById()
	{
		viewPager = (ViewPager) findViewById(R.id.vp_viewpager);
	}

	/**
	 * UI事件监听
	 */
	private void setListener()
	{

		layout5.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				perferenceUtil.setBooleanValue(Constant.GUIDE_STATE, true);
				jumpLoginActivity();
			}
		});
	}

	private void jumpLoginActivity()
	{
		Intent intent = new Intent();
		intent.setClass(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
