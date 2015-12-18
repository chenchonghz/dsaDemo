package com.szrjk.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.PostCommentAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.Forward;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.pull.PullToRefreshBase;
import com.szrjk.pull.PullToRefreshBase.Mode;
import com.szrjk.pull.PullToRefreshBase.OnRefreshListener;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.util.DjsonUtils;

@ContentView(R.layout.activity_more_forward)
public class MoreForwardActivity extends BaseActivity{
	
	@ViewInject(R.id.ptrl_more_forward)
	private PullToRefreshListView ptrl_more_forward;
	
	private MoreForwardActivity instance;
	private int tabId=1;
	private boolean isMore=true;

	private ListView lv_forward;
	
	private ArrayList<Forward> forwards=new ArrayList<Forward>();
	
	private String srcPostId;

	private String basePostId;
	
	private String maxBasePostId;
	
	private String minBasePostId;
	
	private String isNew="true"; 

	protected int y;

	protected boolean isFirst=true;

	private PostCommentAdapter<Forward> forwardAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance=this;
		initLayout();
		setAdapter(forwards);
		initListener();
	}

	private void initLayout() {
		lv_forward=ptrl_more_forward.getRefreshableView();
		ptrl_more_forward.setMode(Mode.BOTH);
		Intent intent=getIntent();
		srcPostId = intent.getStringExtra(Constant.POST_ID);
		basePostId = "0";
		getMoreForwards();
	}

	private void getMoreForwards() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPostOperationList");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("basePostId", basePostId);
		busiParams.put("srcPostId", srcPostId);
		busiParams.put("isNew", isNew);
		busiParams.put("queryType", "FORWARD");
		busiParams.put("beginNum", "0");
		busiParams.put("endNum", "10");
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			public void success(JSONObject jsonObject) {
				try {
					ErrorInfo errorObj = JSON.parseObject(
							jsonObject.getString("ErrorInfo"), ErrorInfo.class);
					if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
						JSONObject returnObj = jsonObject
								.getJSONObject("ReturnInfo");
						if (returnObj.getString("ListOut") != null
								&& !returnObj.getString("ListOut").equals("")) {
							ArrayList<Forward> forwardList = (ArrayList<Forward>) JSON
									.parseArray(
											returnObj.getString("ListOut"),
											Forward.class);
							
							if (forwardList!=null&&!forwardList.isEmpty()) {
								if (isFirst) {
									forwards.addAll(forwardList);
									isFirst=false;
								}
								if (ptrl_more_forward.isFooterShown()) {
									forwards.addAll(forwards.size(), forwardList);
								}else if (ptrl_more_forward.isHeaderShown()) {
									forwards.addAll(0, forwardList);
								}
								minBasePostId=forwards.get(forwards.size()-1).getForwardInfo().getPostId();
								maxBasePostId=forwards.get(0).getForwardInfo().getPostId();
								//Log.i("forwards", DjsonUtils.bean2Json(forwards));
								forwardAdapter.notifyDataSetChanged();
							}
							if (ptrl_more_forward.isRefreshing()) {
								ptrl_more_forward.onRefreshComplete();
							}
							lv_forward.setSelectionFromTop(y,0);
						}
					}
				} catch (Exception e) {
					Log.e(TAG, "", e);
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

	protected void setAdapter(ArrayList<Forward> forwardList) {
		forwardAdapter=PostCommentAdapter.getPostCommentAdapter();
		forwardAdapter.setData(instance, forwardList, tabId,isMore);
		lv_forward.setAdapter(forwardAdapter);
	}
	
	private void initListener(){
		ptrl_more_forward.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				//				new GetDataTask().execute();
				
				//发送请求得到新项目报文
				//下拉刷新
				if (ptrl_more_forward.isHeaderShown()) {
					y=0;
					basePostId=maxBasePostId;
					isNew="true";
				}
				//上拉刷新
				if (ptrl_more_forward.isFooterShown()) {
					basePostId=minBasePostId;
					y=forwards.size()-1;
					isNew="false";
				}
				getMoreForwards();
				//保存加载的位置
				//y =forwardList.size();
			}
		});
	}
}
