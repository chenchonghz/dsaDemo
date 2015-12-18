package com.szrjk.self;

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
import com.szrjk.adapter.CircleListAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.CircleInfo;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.pull.PullToRefreshBase;
import com.szrjk.pull.PullToRefreshBase.OnRefreshListener;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.pull.PullToRefreshBase.Mode;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@ContentView(R.layout.activity_hot_circle)
public class HotCircleActivity extends Activity {
	@ViewInject(R.id.lv_hotcircle)
	private PullToRefreshListView mPullToRefreshListView;
	private ListView lv_hot_circle;
	private List<CircleInfo> hotcircle;
	private HotCircleActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		mPullToRefreshListView.setMode(Mode.BOTH);
		lv_hot_circle = mPullToRefreshListView.getRefreshableView();
		getData();
		initListerner();
	}


	private void getData() {
		Intent intent = getIntent();
		hotcircle = (List<CircleInfo>) intent.getSerializableExtra(Constant.CIRCLE);
		setAdapter();

	}

	private void setAdapter() {
		CircleListAdapter circleListAdapter = new CircleListAdapter(hotcircle, instance);
		lv_hot_circle.setAdapter(circleListAdapter);
		lv_hot_circle.setSelectionFromTop(y, 0);
	}
	private int y =0;
	private void initListerner() {
		lv_hot_circle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				CircleInfo circleInfo = hotcircle.get(position-1);
				Intent intent = new Intent(instance, CircleHomepageActivity.class);
				intent.putExtra(Constant.CIRCLE, circleInfo);
				startActivity(intent);
			}
		});
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (mPullToRefreshListView.isHeaderShown())
				{
					F5circle();
				}else if (mPullToRefreshListView.isFooterShown()) {
					y = hotcircle.size();
					getmorecircle();

				}

			}
		});
	}
	protected void F5circle() {
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
						Toast.makeText(instance, "还没有圈子", Toast.LENGTH_SHORT).show();
					}else{
						hotcircle = new ArrayList<CircleInfo>();
						hotcircle=JSON.parseArray(
								returnObj.getString("ListOut"), CircleInfo.class);
						y=0;
						beginNum = 20;
						endNum = 20;
						setAdapter();
						if (mPullToRefreshListView.isRefreshing()) {
							mPullToRefreshListView.onRefreshComplete();
						}
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
				if (mPullToRefreshListView.isRefreshing()) {
					mPullToRefreshListView.onRefreshComplete();
				}
			}
		});

	}
	private int beginNum = 20;
	private int endNum = 20;
	protected void getmorecircle() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "getHotCoterie");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId",Constant.userInfo.getUserSeqId());
		busiParams.put("beginNum",beginNum);
		busiParams.put("endNum",endNum);
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
						Toast.makeText(instance, "还没有圈子", Toast.LENGTH_SHORT).show();
					}else{
						List<CircleInfo> newHotlist=JSON.parseArray(
								returnObj.getString("ListOut"), CircleInfo.class);
						hotcircle.addAll(newHotlist);
						setAdapter();
						beginNum+=20;
						if (mPullToRefreshListView.isRefreshing()) {
							mPullToRefreshListView.onRefreshComplete();
						}
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
				if (mPullToRefreshListView.isRefreshing()) {
					mPullToRefreshListView.onRefreshComplete();
				}
			}
		});


	}

}
