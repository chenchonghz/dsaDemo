package com.szrjk.self;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.FriendRequestAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.OtherPeopleActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.RequestList;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

public class FriendRequestFragment extends Fragment {
	@ViewInject(R.id.lv_friendrequest)
	private ListView lv_friendrequest;
	private List<RequestList> requestlist ; 
	private FriendActivity  friendActivity;
	private HashMap<String, Integer> RequestState;

	

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friend_request, null);
		ViewUtils.inject(this, view);
		friendActivity = (FriendActivity) getActivity();
		RequestState = new HashMap<String, Integer>();
		getrequest();
		return view;
	}

	private void setAdapter(List<RequestList> requestlist,HashMap<String, Integer> RequestState ) {
		FriendRequestAdapter adapter = new FriendRequestAdapter(getActivity(), requestlist,RequestState);
		lv_friendrequest.setAdapter(adapter);
		
	}

	private void getrequest() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "getUnhandledUserFriendRequest");
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
					requestlist =JSON.parseArray(
							returnObj.getString("requestList"),RequestList.class);
					Log.i("TAG", requestlist.size()+"");
					if (requestlist.size()==0) {
						friendActivity.changeremind(2);
					}else{
						friendActivity.changeremind(1);
//						Log.i("TAG", requestlist.toString());
						for (RequestList rl:requestlist) {
							RequestState.put(rl.getUserCard().getUserSeqId(), 0);
						}
						setAdapter(requestlist,RequestState);

					}

				}
			}

			public void start() {
			}

			public void loading(long total, long current, boolean isUploading) {

			}

			public void failure(HttpException exception, JSONObject jobj) {

			}
		});
	
	}
	
	


}
