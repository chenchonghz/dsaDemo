package com.szrjk.explore;

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
import com.szrjk.adapter.FriendRequestAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.OtherPeopleActivity;
import com.szrjk.dhome.R;
import com.szrjk.dhome.SelfActivity;
import com.szrjk.dhome.R.layout;
import com.szrjk.entity.DialogItem;
import com.szrjk.entity.DialogItemCallback;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.RequestList;
import com.szrjk.entity.UserCard;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.self.FriendActivity;
import com.szrjk.util.BusiUtils;
import com.szrjk.util.DialogUtil;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.CustomListDialog;
import com.szrjk.widget.HeaderView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

@ContentView(R.layout.activity_friend_request)
public class FriendRequestActivity extends BaseActivity {
	@ViewInject(R.id.lv_friendrequest)
	private ListView lv_friendrequest;
	@ViewInject(R.id.hv_friend_request)
	private HeaderView hv_friend_req;
	private List<RequestList> requestlist ; 
	private FriendRequestActivity instance;
	private HashMap<String, Integer> RequestState;
	private FriendRequestAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(instance);
		hv_friend_req.showImageLLy(R.drawable.icon_messageset_40,new OnClickListener() {
			public void onClick(View arg0) {
				ToastUtils.showMessage(instance, "国明请吃饭");
			}
		});
		RequestState = new HashMap<String, Integer>();
		getrequest();
	}
	private void setAdapter(List<RequestList> requestlist,HashMap<String, Integer> RequestState ) {
		adapter = new FriendRequestAdapter(instance, requestlist,RequestState);
		lv_friendrequest.setAdapter(adapter);
		lv_friendrequest.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					final int position, long arg3) {
				DialogItem item = new DialogItem("删除该请求", R.color.font_titleanduname, new DialogItemCallback() {
					public void DialogitemClick() {
						deleteItem(position);
					}
				});
				List<DialogItem> list = new ArrayList<DialogItem>();
				list.add(item);
				CustomListDialog cld = new CustomListDialog(instance, list);
				cld.setCanceledOnTouchOutside(true);
//				Window dialogWindow = cld.getWindow();
//				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//				dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
				cld.show();
				return false;
			}
		});
		lv_friendrequest.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				UserCard userCard = FriendRequestActivity.this.requestlist.get(arg2).getUserCard();
				if(BusiUtils.isguest(instance)){
					//如果是游客,则弹框
					DialogUtil.showGuestDialog(instance,null);
				}else if (
						!Constant.userInfo.getUserSeqId().equals(userCard.getUserSeqId())) {
					//当名片为他人时，跳转第三人主页
					Intent intent = new Intent(instance, OtherPeopleActivity.class);
					intent.putExtra(Constant.USER_SEQ_ID, userCard.getUserSeqId());
					instance.startActivity(intent);
					Log.i("TAG", userCard.toString());
				}else if(
						Constant.userInfo.getUserSeqId().equals(userCard.getUserSeqId())){
					//当名片为自己时，跳转自己的主页
					Intent intent = new Intent(instance, SelfActivity.class);
					intent.putExtra(Constant.USER_SEQ_ID, userCard.getUserSeqId());
					instance.startActivity(intent);
				}
				
			}
		});
	}
	private void getrequest() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "getUnhandledUserFriendRequest");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId",Constant.userInfo.getUserSeqId());
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {

			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					requestlist =JSON.parseArray(
							returnObj.getString("requestList"),RequestList.class);
					Log.i("TAG", requestlist.size()+"");
					if (requestlist.size()==0) {
//						FriendActivity.changeremind(2);
						ToastUtils.showMessage(instance, "没有请求");
					}else{
//						FriendActivity.changeremind(1);
//						Log.i("TAG", requestlist.toString());
						for (RequestList rl:requestlist) {
							RequestState.put(rl.getUserCard().getUserSeqId(), 0);
						}
						setAdapter(requestlist,RequestState);

					}

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
	public void deleteItem(final int position){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "handleUserFriendRequest");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		busiParams.put("srcUserSeqId",requestlist.get(position).getUserCard().getUserSeqId());
		busiParams.put("operateType", String.valueOf(3));
		paramMap.put("BusiParams", busiParams);
		Log.i("TAG", paramMap.toString());
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					Toast.makeText(instance, "已忽略", Toast.LENGTH_SHORT).show();
					requestlist.remove(position);
					adapter.notifyDataSetChanged();
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
				ToastUtils.showMessage(instance, "请求失败");
			}
		});
	}
//	private void sendIgnore() {
//		HashMap<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("ServiceName", "handleUserFriendRequest");
//		Map<String, Object> busiParams = new HashMap<String, Object>();
//		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
//		busiParams.put("srcUserSeqId",requestlist.get(position).getUserCard().getUserSeqId());
//		busiParams.put("operateType", String.valueOf(3));
//		paramMap.put("BusiParams", busiParams);
//		Log.i("TAG", paramMap.toString());
//		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
//			@Override
//			public void success(JSONObject jsonObject) {
//				ErrorInfo errorObj = JSON.parseObject(
//						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
//				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
//				{
//					Toast.makeText(instance, "已忽略", Toast.LENGTH_SHORT).show();
//					state.put(list.get(position).getUserCard().getUserSeqId(), 3);
//					notifyDataSetChanged();
//				}
//			}
//
//			@Override
//			public void start() {
//
//			}
//			@Override
//			public void loading(long total, long current, boolean isUploading) {
//
//			}
//			@Override
//			public void failure(HttpException exception, JSONObject jobj) {
//				ToastUtils.showMessage(instance, "请求失败");
//			}
//		});
//	}
}
