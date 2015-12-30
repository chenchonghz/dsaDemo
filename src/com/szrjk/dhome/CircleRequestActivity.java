package com.szrjk.dhome;

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
import com.szrjk.adapter.CircleRequestAdapter;
import com.szrjk.config.Constant;
import com.szrjk.entity.CircleRequest;
import com.szrjk.entity.DialogItem;
import com.szrjk.entity.DialogItemCallback;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.self.CircleHomepageActivity;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.CustomListDialog;
import com.szrjk.widget.HeaderView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
@ContentView(R.layout.activity_circle_request)
public class CircleRequestActivity extends Activity {
	@ViewInject(R.id.lv_circle_request)
	private ListView lv_circle_request;
	@ViewInject(R.id.hv_circle_request)
	private HeaderView hv_circle_request;
	private List<CircleRequest> requestList;
	private HashMap<String, Integer> RequestState;
	private CircleRequestActivity instance;
	private CircleRequestAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(instance);
		hv_circle_request.showImageLLy(R.drawable.icon_messageset_40,new OnClickListener() {
			public void onClick(View arg0) {
				ToastUtils.showMessage(instance, "国明请吃饭");
			}
		});
		RequestState = new HashMap<String, Integer>();
		getNewRequests();
	}
	private void getNewRequests() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "dealCoterieInvitation");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId",Constant.userInfo.getUserSeqId());
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
					}else{
						requestList=JSON.parseArray(
								returnObj.getString("notifyList"),CircleRequest.class);
						for (CircleRequest rl:requestList) {
							StringBuffer sb = new StringBuffer(rl.getObjUserSeqId());
							sb.append(rl.getCoterieId());
							Log.i("TAG", sb.toString());
							RequestState.put(sb.toString(), 0);
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
		adapter = new CircleRequestAdapter(instance, requestList2,RequestState);
		lv_circle_request.setAdapter(adapter);
		lv_circle_request.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(instance, CircleHomepageActivity.class);

			}
		});
		lv_circle_request.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				DialogItem item = new DialogItem("删除该请求", R.color.black, new DialogItemCallback() {
					public void DialogitemClick() {
						deleteItem(position);
					}
				});
				List<DialogItem> list = new ArrayList<DialogItem>();
				list.add(item);
				CustomListDialog cld = new CustomListDialog(instance, list);
				cld.show();
				return false;
			}
		});
	}
	//长按删除逻辑
	public void deleteItem(final int position){
	}
	

}
