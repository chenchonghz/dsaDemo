package com.szrjk.self;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.adapter.CircleInviteAdapter;
import com.szrjk.adapter.CircleInviteAdapter.ViewHolder;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InviteFansFragment extends Fragment {
	private  CircleInviteFirendActivity instance;
	private ListView lv_fans;
	private TextView select_all;
	public List<UserCard> fanslist;
	public CircleInviteAdapter fans_adapter;
	private Dialog dialog;
	public InviteFansFragment() {
		super();
	}

	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,Bundle savedInstanceState) {
		dialog = createDialog(getActivity(), "加载中...");
		View v = inflater.inflate(R.layout.fragment_invite_fans, container, false);
		instance = (CircleInviteFirendActivity) getActivity();
		lv_fans = (ListView) v.findViewById(R.id.lv_invitefans);
		select_all = (TextView) v.findViewById(R.id.tv_fans_all);
		getFans();
		return v;
	}

	private void getFans() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryFocusUserList");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId",Constant.userInfo.getUserSeqId());
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
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
//						Toast.makeText(instance, "还没有粉丝", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				}
			}

			public void start() {dialog.show();

			}
			public void loading(long total, long current, boolean isUploading) {

			}
			public void failure(HttpException exception, JSONObject jobj) {
				dialog.dismiss();
				ToastUtils.showMessage(getActivity(), "获取失败");
			}
		});
	}

	protected void setAdapter(List<FriendList> friendlist) {
		fanslist = new ArrayList<UserCard>();
		for (int i = 0; i < friendlist.size(); i++) {
			fanslist.add(friendlist.get(i).getUserCard());
		}
		fans_adapter = new CircleInviteAdapter(fanslist, instance);
		lv_fans.setAdapter(fans_adapter);
		fans_adapter.initDate();
		lv_fans.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				String uid = fanslist.get(position).getUserSeqId();
				boolean state_now = fans_adapter.getIsSelected().get(uid);
				if (state_now==true) {
					fans_adapter.getIsSelected().put(uid, false);
				}else{
					fans_adapter.getIsSelected().put(uid, true);
				}
				dataChanged();
				//				ViewHolder viewHolder = (ViewHolder) view.getTag();
				//				CheckBox cb = viewHolder.getCb();
				//				cb.toggle();
				//				fans_adapter.getIsSelected().put(position, cb.isChecked());
				String userId = fanslist.get(position).getUserSeqId();
				boolean state = fans_adapter.getIsSelected().get(uid);
				//				instance.setCheckbox(userId,state);
				instance.changefriend(userId, state);
			}
		});
		select_all.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				for (int i = 0; i < fanslist.size(); i++) {
					if (fans_adapter.getIsSelected().get(fanslist.get(i).getUserSeqId())==null) {
						fans_adapter.initDate();
						Log.i("tag", "complete");
					}
					if (fans_adapter.getIsSelected().get(fanslist.get(i).getUserSeqId())==false) {
						fans_adapter.getIsSelected().put(fanslist.get(i).getUserSeqId(), true);
						instance.changefriend(fanslist.get(i).getUserSeqId(),
								fans_adapter.getIsSelected().get(fanslist.get(i).getUserSeqId()));
					}
				}
				dataChanged();
			}
		});
		if (fans_adapter.getFlag()==1) {
			dialog.dismiss();
		}
	}
	public void dataChanged() {  
		// 通知listView刷新  
		fans_adapter.notifyDataSetChanged();  
	};  
	public void setUidStatus(String uid,boolean status) {

		for (int i = 0; i <fanslist.size(); i++) {
			if(fanslist.get(i).getUserSeqId().equals(uid)){
				boolean b = fans_adapter.getIsSelected().get(uid);
				if(b==status)continue;
				fans_adapter.getIsSelected().put(uid, status);
				//				Log.i("TAG", follow_adapter.getIsSelected().toString());
				break;
			}
		}

		fans_adapter.notifyDataSetChanged();
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
