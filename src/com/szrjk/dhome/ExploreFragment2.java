package com.szrjk.dhome;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.MessagesEntity;
import com.szrjk.explore.MoreNewsActivity;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.message.MessageEntity;
import com.szrjk.pull.PullToRefreshBase.Mode;
import com.szrjk.pull.PullToRefreshScrollView;
import com.szrjk.sdlv.Menu;
import com.szrjk.sdlv.MenuItem;
import com.szrjk.sdlv.SlideAndDragListView;
import com.szrjk.sdlv.SlideAndDragListView.OnSlideListener;
import com.szrjk.util.ShowDialogUtil;
import com.szrjk.util.ToastUtils;



public class ExploreFragment2 extends Fragment{

	@ViewInject(R.id.sl_messages)
    private PullToRefreshScrollView sl_messages;
	private Context context;
	@ViewInject(R.id.lv_messages)
	private SlideAndDragListView lv_messages;
	@ViewInject(R.id.bt_botton)
	private Button test;
	private Dialog dialog;
	private static final String LOADING_POST = "正在加载推送消息";
	private List<MessagesEntity> messageList;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.explore_fragment_2, null);
		ViewUtils.inject(this, view);
		initData();
		initListner();
		getMessagesByNetwork();
		return view;
	}

	private void getMessagesByNetwork() {
		// TODO Auto-generated method stub
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryMessagePush");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", "1000000001");
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			
			@Override
			public void success(JSONObject jsonObject) {
				// TODO Auto-generated method stub
				if(dialog.isShowing()){
					dialog.dismiss();
				}
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					messageList = JSON.parseArray(returnObj.getString("messageListOut"), MessagesEntity.class);
					Log.e("ExploreFragment2", messageList.toString());
				}
			}
			
			@Override
			public void start() {
				// TODO Auto-generated method stub
				dialog.show();
			}
			
			@Override
			public void loading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				// TODO Auto-generated method stub
				if(dialog.isShowing()){
					dialog.dismiss();
				}
				ToastUtils.showMessage(context, "获取推送消息失败，请检查网络");
			}
		});
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
		dialog = ShowDialogUtil.createDialog(context, LOADING_POST);
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
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.e("ExploreFragment", "onResume");
		super.onResume();
	}
}
