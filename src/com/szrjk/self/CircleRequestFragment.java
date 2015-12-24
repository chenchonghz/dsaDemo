package com.szrjk.self;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.CircleRequestAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.CircleRequest;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CircleRequestFragment extends Fragment {

	private View v;
	@ViewInject(R.id.lv_circle_request)
	private ListView lv_circle_request;
	private List<CircleRequest> requestList;
	private HashMap<String, Integer> RequestState;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_circle_request, container, false);
		ViewUtils.inject(this,v);
		RequestState = new HashMap<String, Integer>();
		getRequests();
		return v;
	}

	private void getRequests() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "getCoterieInvitations");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId",Constant.userInfo.getUserSeqId());
		busiParams.put("invitationType", "");
		busiParams.put("beginNum","0");
		busiParams.put("endNum","100");
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
					if (returnObj==null) {
						CircleActivity circleActivity = (CircleActivity) getActivity();
						CircleActivity.changeremind(2);
					}else{
						CircleActivity circleActivity = (CircleActivity) getActivity();
						CircleActivity.changeremind(1);
						requestList=JSON.parseArray(
								returnObj.getString("returnList"),CircleRequest.class);
						for (CircleRequest rl:requestList) {
							StringBuffer sb = new StringBuffer(rl.getObjUserSeqId());
							sb.append(rl.getCoterieId());
							Log.i("TAG", sb.toString());
							RequestState.put(sb.toString(), 0);
//							//邀请
//							if (rl.getInvitationType().equals("I")) {
//								RequestState.put(rl.getUserCard().getUserSeqId(), 0);
//							//请求
//							}else if (rl.getInvitationType().equals("R")) {
//								RequestState.put(rl.getUserCard().getUserSeqId(), 4);
//							}
						}
						setAdapter(requestList,RequestState);
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

	protected void setAdapter(List<CircleRequest> requestList2,HashMap<String, Integer> RequestState) {
		CircleRequestAdapter adapter = new CircleRequestAdapter(getActivity(), requestList2,RequestState);
		lv_circle_request.setAdapter(adapter);
		lv_circle_request.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), CircleHomepageActivity.class);


			}
		});
	}

}
