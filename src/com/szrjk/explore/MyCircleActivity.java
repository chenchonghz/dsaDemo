package com.szrjk.explore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.CircleListAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.CircleInfo;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.self.CircleHomepageActivity;
import com.szrjk.self.CreateCircleActivity;
import com.szrjk.self.HotCircleActivity;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.InnerListView;

@ContentView(R.layout.activity_my_circle)
public class MyCircleActivity extends BaseActivity {
	private MyCircleActivity instance;
	@ViewInject(R.id.rl_createCircle)
	private RelativeLayout rl_createCircle;
	@ViewInject(R.id.ib_more)
	private ImageButton ib_more;
	@ViewInject(R.id.lv_hotCircle)
	private InnerListView lv_hotCircle;
	@ViewInject(R.id.lv_myCircle)
	private InnerListView lv_myCircle;
	@ViewInject(R.id.rl_not_circle)
	private RelativeLayout rl_not_circle;
	private List<CircleInfo> allHotList;
	private List<CircleInfo> myCirclelist;
	private List<CircleInfo> hotlist_show;
	private static final int HAVE_HOT_CIRCLE = 1;
	private static final int HAVE_MY_CIRCLE = 2;
	private static final int HAVE_NOT_MY_CIRCLE = 3;
	private CircleListAdapter hotCircle_adapter;
	private CircleListAdapter myCircle_adapter;
	private boolean isFirstIn = true;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HAVE_HOT_CIRCLE:
				hotCircle_adapter = new CircleListAdapter(hotlist_show, instance);
				lv_hotCircle.setAdapter(hotCircle_adapter);
				break;
			case HAVE_MY_CIRCLE:
//				rl_not_circle.setVisibility(View.GONE);
//				lv_myCircle.setVisibility(View.VISIBLE);	
				myCircle_adapter = new CircleListAdapter(myCirclelist, instance);
				lv_myCircle.setAdapter(myCircle_adapter);

				break;
			case HAVE_NOT_MY_CIRCLE:
//				rl_not_circle.setVisibility(View.VISIBLE);
//				lv_myCircle.setVisibility(View.GONE);
//				Log.e("MyCircleActivity", "没有我的圈子");
				myCircle_adapter = new CircleListAdapter(myCirclelist, instance);
				lv_myCircle.setAdapter(myCircle_adapter);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(instance);
		getHotCircle();
		getMyCircle();
		initListner();
	}


	private void initListner() {
		// TODO Auto-generated method stub
		rl_createCircle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isFirstIn = false;
				Intent intent = new Intent(instance,CreateCircleActivity.class);
				startActivity(intent);
				
			}
		});
		lv_myCircle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				isFirstIn = false;
				CircleInfo circleInfo = myCirclelist.get(position);
				Intent intent = new Intent(instance, CircleHomepageActivity.class);
				intent.putExtra("circle", circleInfo);
				startActivity(intent);
			}
		});
		lv_hotCircle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				isFirstIn = false;
				CircleInfo circleInfo = hotlist_show.get(position);
				Intent intent = new Intent(instance, CircleHomepageActivity.class);
				intent.putExtra(Constant.CIRCLE, circleInfo);
				startActivity(intent);
			}
		});
		ib_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isFirstIn = false;
				if (allHotList.size()==0) {
					ToastUtils.showMessage(instance, "未有热门圈子");
				}else{
					Intent intent = new Intent(instance, HotCircleActivity.class);
					intent.putExtra(Constant.CIRCLE, (Serializable)allHotList);
					startActivity(intent);		
				}
			}
		});
	}


	private void getMyCircle() {
		// TODO Auto-generated method stub
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryUserRelativeCoterie");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId",Constant.userInfo.getUserSeqId());
		busiParams.put("beginNum",Integer.valueOf(0));
		busiParams.put("endNum",Integer.valueOf(500));
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
					if (returnObj==null) {
						Toast.makeText(instance, "还没有圈子", Toast.LENGTH_SHORT).show();
						handler.sendEmptyMessage(HAVE_NOT_MY_CIRCLE);
					}else{
						myCirclelist=JSON.parseArray(
								returnObj.getString("coterieList"), CircleInfo.class);
						if(myCirclelist != null && !myCirclelist.isEmpty()){					
							handler.sendEmptyMessage(HAVE_MY_CIRCLE);
						}else{
							handler.sendEmptyMessage(HAVE_NOT_MY_CIRCLE);
						}
					}
				}else{
					showToast(instance, "获取我的圈子失败", Toast.LENGTH_SHORT);
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
				showToast(instance, "获取数据失败，请检查网络", Toast.LENGTH_SHORT);
			}
		});
	}

	private void getHotCircle() {
		// TODO Auto-generated method stub
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "getHotCoterie");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId",Constant.userInfo.getUserSeqId());
		busiParams.put("beginNum",Integer.valueOf(0));
		busiParams.put("endNum",Integer.valueOf(20));
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
						showToast(instance, "现在还没有任何圈子哦，赶紧创建一个吧", Toast.LENGTH_SHORT);
					}else{
						allHotList=JSON.parseArray(
								returnObj.getString("ListOut"), CircleInfo.class);
						if(allHotList != null && !allHotList.isEmpty()){
							hotlist_show = getHotCircle2Show(allHotList);
							handler.sendEmptyMessage(HAVE_HOT_CIRCLE);
						}else{
							showToast(instance, "现在还没有任何圈子哦，赶紧创建一个吧", Toast.LENGTH_SHORT);
						}
					}
				}else{
					showToast(instance, "获取热门圈子失败", Toast.LENGTH_SHORT);
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

	protected List<CircleInfo> getHotCircle2Show(List<CircleInfo> allHotList) {
		// TODO Auto-generated method stub
		hotlist_show = new ArrayList<CircleInfo>();
		for (int i = 0; allHotList.size()>3?i<3:i<allHotList.size(); i++) {
			hotlist_show.add(allHotList.get(i));
		}
		return hotlist_show;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(!isFirstIn){
			getMyCircle();
		}
		super.onResume();
	}
}
