package com.szrjk.dhome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.CircleRequestAdapter;
import com.szrjk.config.Constant;
import com.szrjk.entity.CircleRequest;
import com.szrjk.entity.DialogItem;
import com.szrjk.entity.DialogItemCallback;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.TCircleRequest;
import com.szrjk.entity.TFriendRequest;
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
		//初始化表
		RequestState = new HashMap<String, Integer>();
		getOldRequests();

	}
	//获取数据库的通知列表和状态表
	private void getOldRequests() {
		try {
			requestList = new TCircleRequest().getlist(Constant.userInfo.getUserSeqId());
			RequestState = new TCircleRequest().getStatelist(Constant.userInfo.getUserSeqId());
			getNewRequests();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//从后台拉取新的通知
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
						List<CircleRequest> NewRequestList=JSON.parseArray(
								returnObj.getString("notifyList"),CircleRequest.class);
						HashMap<String, Integer > NewRequestState = new HashMap<String, Integer>();
						for (CircleRequest rl:NewRequestList) {
							StringBuffer sb = new StringBuffer(rl.getObjUserSeqId());
							sb.append(rl.getCoterieId());
							Log.i("TAG", sb.toString());
							NewRequestState.put(sb.toString(), 0);
						}
						//将新表放进数据库
						try {
							new TCircleRequest().addrequest(NewRequestList, NewRequestState);
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//混合两个表
						for (int i = 0; i < NewRequestList.size(); i++) {
							//判断此item表内是否存在：0不存在，1存在
							int a = 0;
							for (int j = 0; j <requestList.size(); j++) {
								if (requestList.get(j).getUserSeqId()
										.equals(NewRequestList.get(i).getUserSeqId())
										&&requestList.get(j).getCoterieId()
										.equals(NewRequestList.get(i).getCoterieId())
										&&requestList.get(j).getNotifyType()
										.equals(NewRequestList.get(i).getNotifyType())) 
								{
									requestList.remove(j);
									requestList.add(0,NewRequestList.get(i));
									a=1;
									break;
								}
							}
							if (a==0) {
								requestList.add(0, NewRequestList.get(i));
							}
							//如果是圈子邀请或者圈子请求，写入状态值
							if (NewRequestList.get(i).getNotifyType().equals("11")
									||NewRequestList.get(i).getNotifyType().equals("12")) 
							{
								String key = getKey(NewRequestList.get(i).getObjUserSeqId(), 
										NewRequestList.get(i).getCoterieId());
								RequestState.put(key,NewRequestState.get(key));
							}
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
		try {
			new TCircleRequest().deleteRequest(requestList.get(position));
			requestList.remove(position);
			adapter.notifyDataSetChanged();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//获得hash表的key值
	public String getKey(String userID,String circleID){
		StringBuffer sb = new StringBuffer(userID);
		sb.append(circleID);
		String key = sb.toString();
		return key;
	}

}
