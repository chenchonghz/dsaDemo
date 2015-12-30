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
import com.szrjk.adapter.MyFriendListAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.OtherPeopleActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.FriendList;
import com.szrjk.entity.RemindEvent;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.ToastUtils;

import de.greenrobot.event.EventBus;

import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
@ContentView(R.layout.activity_friend)
public class FriendActivity extends BaseActivity 
{
@ViewInject(R.id.lv_friend)
private ListView lv_friend;
private List<FriendList> friendlist;
private Dialog dialog;
private FriendActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		dialog = createDialog(instance, "加载中...");
		getfriends();
		initListener();
	}

	private void getfriends() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "getUserFriends");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId",Constant.userInfo.getUserSeqId());
		busiParams.put("startNum",0);
		busiParams.put("endNum",500);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				dialog.dismiss();
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					friendlist =JSON.parseArray(
							returnObj.getString("ListOut"), FriendList.class);
					setAdapter(friendlist);
				}else if("0006".equals(errorObj.getReturnCode())){
				}
			}

			@Override
			public void start() {dialog.show();

			}
			@Override
			public void loading(long total, long current, boolean isUploading) {

			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				dialog.dismiss();
				ToastUtils.showMessage(instance, "获取失败");
			}
		});
		
	}

	protected void setAdapter(List<FriendList> friendlist2) {
		MyFriendListAdapter myFriendListAdapter = new MyFriendListAdapter(instance, friendlist);
		lv_friend.setAdapter(myFriendListAdapter);
		
	}

	private void initListener() {
		lv_friend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(instance, OtherPeopleActivity.class);
				intent.putExtra(Constant.USER_SEQ_ID, friendlist.get(arg2).getUserCard().getUserSeqId());
				startActivity(intent);
			}
		});
	}

	
}