package com.szrjk.self.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.CoterieMemberListAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.OtherPeopleActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.FriendList;
import com.szrjk.entity.UserCard;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.HeaderView;

@ContentView(R.layout.activity_my_fans)
public class MyFansActivity extends BaseActivity{
	@ViewInject(R.id.hv_fans)
	private HeaderView hv_fans;
	@ViewInject(R.id.lv_my_fans)
	private ListView lv_my_fans;
	
	private MyFansActivity instance;
	public List<UserCard> fanslist;
	private JSONObject returnObj;

	private String userSeqId;
	private String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance=this;
		intiLayout();
		//添加listview的item监听
		lv_my_fans.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(instance, OtherPeopleActivity.class);
				intent.putExtra(Constant.USER_SEQ_ID, fanslist.get(arg2).getUserSeqId());
				startActivity(intent);
			}
		});
	}

	private void intiLayout() {
		Intent intent=getIntent();
		username = intent.getStringExtra("username");
		userSeqId=intent.getStringExtra(Constant.USER_SEQ_ID);
		if (username!=null) {
			hv_fans.setHtext(username+"的粉丝");
		}
		getFans();
	}
	
	private void getFans() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryFocusUserList");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId",userSeqId);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			
			
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					List<FriendList>  friendlist =JSON.parseArray(
							returnObj.getString("ListOut"), FriendList.class);
					setAdapter(friendlist);
				}
			}

			@Override
			public void start() {

			}
			@Override
			public void loading(long total, long current, boolean isUploading) {

			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {

			}
		});
	}
	
	protected void setAdapter(List<FriendList> friendlist) {
		if (friendlist!=null&&friendlist.size()!=0) {
			fanslist = new ArrayList<UserCard>();
			for (int i = 0; i < friendlist.size(); i++) {
				fanslist.add(friendlist.get(i).getUserCard());
			}
			CoterieMemberListAdapter adapter=new CoterieMemberListAdapter(instance, fanslist);
			lv_my_fans.setAdapter(adapter);
		}else {
			ToastUtils.showMessage(instance, returnObj.getString("ReturnInfo"));
		}
	}
}
