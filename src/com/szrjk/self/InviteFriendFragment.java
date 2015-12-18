package com.szrjk.self;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.adapter.CircleInviteAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.FriendList;
import com.szrjk.entity.UserCard;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.ToastUtils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class InviteFriendFragment extends Fragment {
	private  CircleInviteFirendActivity instance;
	private ListView lv_friend;
	private TextView select_all;
	public List<UserCard> friendlist;
	public CircleInviteAdapter friend_adapter;
	private Dialog dialog;
	public InviteFriendFragment() {
		super();
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		dialog = createDialog(getActivity(), "加载中...");
		View v = inflater.inflate(R.layout.fragment_invite_friend, container, false);
		instance = (CircleInviteFirendActivity) getActivity();
		lv_friend = (ListView) v.findViewById(R.id.lv_invitefriend);
		select_all = (TextView) v.findViewById(R.id.tv_friend_all);
		getFriends();
		return v;
	}

	//获取好友列表
	private void getFriends() {
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

				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					List<FriendList>  friendlist =JSON.parseArray(
							returnObj.getString("ListOut"), FriendList.class);
					if (friendlist!=null&&friendlist.size()!=0) {
						setAdapter(friendlist);
					}else{
						dialog.dismiss();
					}
				}else if("0006".equals(errorObj.getReturnCode())){
					dialog.dismiss();
					ToastUtils.showMessage(getActivity(), "获取失败");
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
				ToastUtils.showMessage(getActivity(), "获取失败");
			}
		});


	}

	//设置适配器，和list点击监听  全选按钮监听
	protected void setAdapter(List<FriendList> friendlist2) {
		friendlist = new ArrayList<UserCard>();
		for (int i = 0; i < friendlist2.size(); i++) {
			friendlist.add(friendlist2.get(i).getUserCard());
		}
		friend_adapter = new CircleInviteAdapter(friendlist, instance);
		lv_friend.setAdapter(friend_adapter);
		friend_adapter.initDate();

		lv_friend.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				//				ViewHolder viewHolder = (ViewHolder) view.getTag();
				String uid = friendlist.get(position).getUserSeqId();
				Log.i("TAG", "12"+uid);
				Log.i("TAG", "begin"+friend_adapter.getIsSelected().toString());
				if (friend_adapter.getIsSelected().get(uid)==null) {
					friend_adapter.initDate();
					Log.i("adpater", friend_adapter.getIsSelected().toString());
					dataChanged();
				}
				boolean state_now = friend_adapter.getIsSelected().get(uid);
				Log.i("TAG",""+position+state_now);
				if (state_now==true) {
					friend_adapter.getIsSelected().put(uid, false);
				}else{
					friend_adapter.getIsSelected().put(uid, true);
				}
				dataChanged();

				Log.i("TAG", "end"+friend_adapter.getIsSelected().toString());
				//				CheckBox cb = viewHolder.getCb();
				//				cb.toggle();
				//				friend_adapter.getIsSelected().put(position, cb.isChecked());
				String userId = friendlist.get(position).getUserSeqId();
				boolean state = friend_adapter.getIsSelected().get(userId);
				//				instance.setCheckbox(userId,state);
				instance.changefriend(userId, state);
			}
		});
		select_all.setOnClickListener(new OnClickListener() {


			@Override
			public void onClick(View arg0) {
				for (int i = 0; i < friendlist.size(); i++) {  
					if (friend_adapter.getIsSelected().get(friendlist.get(i).getUserSeqId())==null) {
						friend_adapter.initDate();
						Log.i("tag", "complete");
					}
					if(friend_adapter.getIsSelected().get(friendlist.get(i).getUserSeqId())==false){
						friend_adapter.getIsSelected().put(friendlist.get(i).getUserSeqId(), true); 
						instance.changefriend(friendlist.get(i).getUserSeqId(),
								friend_adapter.getIsSelected().get(friendlist.get(i).getUserSeqId()));
					}
				}  
				dataChanged();

			}
		});
		if (friend_adapter.getFlag()==1) {
			dialog.dismiss();
		}
	}
	public void dataChanged() {  
		// 通知listView刷新  
		friend_adapter.notifyDataSetChanged();  
	};

	public void setUidStatus(String uid,boolean status) {

		for (int i = 0; i <friendlist.size(); i++) {
			if(friendlist.get(i).getUserSeqId().equals(uid)){
				boolean b = friend_adapter.getIsSelected().get(uid);
				if(b==status)continue;
				friend_adapter.getIsSelected().put(uid, status);
				break;
			}
		}

		friend_adapter.notifyDataSetChanged();
	}
	public Dialog createDialog(Context context, String msg)
	{
		Dialog dialog = new Dialog(context, R.style.loading_dialog);
		dialog.setContentView(R.layout.loading_layout);
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		TextView textView = (TextView) dialog.findViewById(R.id.tv_msg);
		textView.setText(msg);
		return dialog;
	}
}
