package com.szrjk.self.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

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

@ContentView(R.layout.activity_my_attention)
public class MyAttentionActivity extends BaseActivity{

	@ViewInject(R.id.lv_my_attention)
	private ListView lv_my_attention;

	private MyAttentionActivity instance;
	public List<UserCard> followlist;
	private JSONObject returnObj;

	private String userSeqId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance=this;
		intiLayout();
		//添加listview的item监听
		lv_my_attention.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(instance, OtherPeopleActivity.class);
				intent.putExtra(Constant.USER_SEQ_ID, followlist.get(arg2).getUserSeqId());
				startActivity(intent);
			}
		});
	}

	private void intiLayout() {
		Intent intent=getIntent();
		userSeqId=intent.getStringExtra(Constant.USER_SEQ_ID);
		getFollows();
	}

	private void getFollows() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryUserFocusList");
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
			followlist = new ArrayList<UserCard>();
			for (int i = 0; i < friendlist.size(); i++) {
				followlist.add(friendlist.get(i).getUserCard());
			}
			CoterieMemberListAdapter adapter=new CoterieMemberListAdapter(instance, followlist);
			lv_my_attention.setAdapter(adapter);
		}else {
			ToastUtils.showMessage(instance, returnObj.getString("ReturnInfo"));
		}
	}
}
