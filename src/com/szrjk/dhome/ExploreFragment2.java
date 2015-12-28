package com.szrjk.dhome;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.pull.PullToRefreshBase.Mode;
import com.szrjk.pull.PullToRefreshScrollView;
import com.szrjk.widget.InnerListView;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

public class ExploreFragment2 extends Fragment{

	@ViewInject(R.id.lv_messages)
	private SlideAndDragListView lv_messages;
	private Context context;
	private Menu menu;

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
		menu = new Menu((int)getResources().getDimension(R.dimen.view_listMenu_height), new ColorDrawable(Color.WHITE), false);
		menu.addItem(new MenuItem.Builder().setWidth((int)getResources().getDimension(R.dimen.view_listMenu_btn_width))//单个菜单button的宽度
                .setBackground(new ColorDrawable(Color.RED))//设置菜单的背景
                .setText("删除")//set text string
                .setTextColor(getResources().getColor(R.color.font_titleanduname))//set text color
                .setTextSize((int)getResources().getDimension(R.dimen.font_size_middle))//set text color
                .build());
		lv_messages.setMenu(menu);
	}

	private void initListner() {
		// TODO Auto-generated method stub
		
	}
}
