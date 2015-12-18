package com.szrjk.dhome;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.explore.MoreNewsActivity;
import com.szrjk.self.more.CaseSharePostActivity;
import com.szrjk.self.more.ProblemHelpActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ExploreFragment2 extends Fragment{
	@ViewInject(R.id.rl_news)
	private RelativeLayout rl_news;
	@ViewInject(R.id.rl_caseShare)
	private RelativeLayout rl_caseShare;
	@ViewInject(R.id.rl_problemHelp)
	private RelativeLayout rl_problemHelp;
	@ViewInject(R.id.rl_myCircle)
	private RelativeLayout rl_myCircle;
	@ViewInject(R.id.rl_message)
	private RelativeLayout rl_message;
	@ViewInject(R.id.ll_request)
	private LinearLayout ll_request;
	@ViewInject(R.id.rl_friendRequest)
	private RelativeLayout rl_friendRequest;
	@ViewInject(R.id.rl_circleRequest)
	private RelativeLayout rl_circleRequest;
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
	}

	private void initListner() {
		// TODO Auto-generated method stub
		rl_news.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MoreNewsActivity.class);
				startActivity(intent);
			}
		});
		rl_caseShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, CaseSharePostActivity.class);
				intent.putExtra("postType", "ALL");
				startActivity(intent);
			}
		});
		rl_problemHelp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ProblemHelpActivity.class);
				intent.putExtra("postType", "ALL");
				startActivity(intent);
			}
		});
	}
}
