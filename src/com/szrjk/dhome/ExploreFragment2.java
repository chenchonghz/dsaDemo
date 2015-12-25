package com.szrjk.dhome;

import android.content.Context;
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

public class ExploreFragment2 extends Fragment{

	@ViewInject(R.id.sl_explore)
	private PullToRefreshScrollView sl_explore;
	@ViewInject(R.id.lv_messages)
	private InnerListView lv_messages;
	private Context context;

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
		sl_explore.setMode(Mode.PULL_DOWN_TO_REFRESH);
		sl_explore.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_down_lable_text));
		sl_explore.getLoadingLayoutProxy(true, false)
		.setRefreshingLabel(
				getResources()
				.getString(R.string.refreshing_lable_text));
		sl_explore.getLoadingLayoutProxy(true, true)
		.setReleaseLabel(
				getResources().getString(R.string.release_lable_text));
	}

	private void initListner() {
		// TODO Auto-generated method stub
		
	}
}
