package com.szrjk.dhome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.explore.MoreNewsActivity;
import com.szrjk.pull.PullToRefreshBase.Mode;
import com.szrjk.pull.PullToRefreshScrollView;
import com.szrjk.sdlv.Menu;
import com.szrjk.sdlv.MenuItem;
import com.szrjk.sdlv.SlideAndDragListView;
import com.szrjk.sdlv.SlideAndDragListView.OnSlideListener;


public class ExploreFragment2 extends Fragment{

	@ViewInject(R.id.sl_messages)
    private PullToRefreshScrollView sl_messages;
	private Context context;
	@ViewInject(R.id.lv_messages)
	private SlideAndDragListView lv_messages;
	@ViewInject(R.id.bt_botton)
	private Button test;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.explore_fragment_2, null);
		ViewUtils.inject(this, view);
		initData();
		initListner();
		return view;
	}

	private void initData() {
		// TODO Auto-generated method stub
		context = getActivity();
		sl_messages.setMode(Mode.PULL_DOWN_TO_REFRESH);
		sl_messages.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_down_lable_text));
		sl_messages.getLoadingLayoutProxy(true, false)
		.setRefreshingLabel(
				getResources()
				.getString(R.string.refreshing_lable_text));
		sl_messages.getLoadingLayoutProxy(true, false)
		.setReleaseLabel(
				getResources().getString(R.string.release_lable_text));
		Menu menu = new Menu((int) getResources().getDimension(R.dimen.view_listMenu_height), new ColorDrawable(Color.WHITE), true);
		menu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.view_listMenu_btn_width))//单个菜单button的宽度
				.setBackground(new ColorDrawable(getResources().getColor(R.color.base_bg)))//设置菜单的背景
				.setDirection(MenuItem.DIRECTION_RIGHT)
				.setText("置顶")//set text string
				.setTextColor(getResources().getColor(R.color.font_titleanduname))//set text color
				.setTextSize((int) getResources().getDimension(R.dimen.font_size_middle))//set text color
				.build());
		menu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.view_listMenu_btn_width))
				.setBackground(new ColorDrawable(Color.RED))
				.setDirection(MenuItem.DIRECTION_RIGHT)//设置方向 (默认方向为DIRECTION_LEFT)
				.setText("删除")//set text string
				.setTextColor(getResources().getColor(R.color.font_titleanduname))//set text color
				.setTextSize((int) getResources().getDimension(R.dimen.font_size_middle))//set text color
				.build());
		lv_messages.setMenu(menu);
	}

	private void initListner() {
		// TODO Auto-generated method stub
		lv_messages.setOnSlideListener(new OnSlideListener() {

			@Override
			public void onSlideOpen(View view, View parentView, int position,
					int direction) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSlideClose(View view, View parentView, int position,
					int direction) {
				// TODO Auto-generated method stub
				
			}
		});
		test.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MoreNewsActivity.class);
				startActivity(intent);
			}
		});
	}
}
