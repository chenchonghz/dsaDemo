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
import com.szrjk.entity.Like;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.pull.PullToRefreshBase;
import com.szrjk.pull.PullToRefreshBase.Mode;
import com.szrjk.pull.PullToRefreshBase.OnRefreshListener;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.util.DjsonUtils;

@ContentView(R.layout.activity_more_like)
public class MoreLikeActivity extends BaseActivity{
	
	@ViewInject(R.id.ptrl_more_like)
	private PullToRefreshListView ptrl_more_like;
	
	private MoreLikeActivity instance;
	private int tabId=3;
	private boolean isMore=true;

	private ListView lv_like;
	
	private ArrayList<Like> likes=new ArrayList<Like>();
	
	private String srcPostId;

	private String basePostId;
	
	private String maxBasePostId;
	
	private String minBasePostId;
	
	private String isNew="true"; 

	protected int y;

	protected boolean isFirst=true;

	private PostCommentAdapter<Like> likeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance=this;
		initLayout();
		setAdapter(likes);
		initListener();
	}

	private void initLayout() {
		lv_like=ptrl_more_like.getRefreshableView();
		ptrl_more_like.setMode(Mode.BOTH);
		Intent intent=getIntent();
		srcPostId = intent.getStringExtra(Constant.POST_ID);
		basePostId = "0";
		getMoreLikes();
	}

	private void getMoreLikes() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPostOperationList");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("basePostId", basePostId);
		busiParams.put("srcPostId", srcPostId);
		busiParams.put("isNew", isNew);
		busiParams.put("queryType", "LIKE");
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
							ArrayList<Like> likeList = (ArrayList<Like>) JSON
									.parseArray(returnObj.getString("ListOut"),
											Like.class);
							
							if (likeList!=null&&!likeList.isEmpty()) {
								if (isFirst) {
									likes.addAll(likeList);
									isFirst=false;
								}
								if (ptrl_more_like.isFooterShown()) {
									likes.addAll(likes.size(), likeList);
								}else if (ptrl_more_like.isHeaderShown()) {
									likes.addAll(0, likeList);
								}
								minBasePostId=likes.get(likes.size()-1).getLikeInfo().getPostId();
								maxBasePostId=likes.get(0).getLikeInfo().getPostId();
								//Log.i("likes", DjsonUtils.bean2Json(likes));
								likeAdapter.notifyDataSetChanged();
							}
							if (ptrl_more_like.isRefreshing()) {
								ptrl_more_like.onRefreshComplete();
							}
							lv_like.setSelectionFromTop(y,0);
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

	protected void setAdapter(ArrayList<Like> likeList) {
		likeAdapter=new PostCommentAdapter<Like>(instance, likeList, tabId,isMore);
		lv_like.setAdapter(likeAdapter);
	}
	
	private void initListener(){
		ptrl_more_like.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				//				new GetDataTask().execute();
				
				//发送请求得到新项目报文
				//下拉刷新
				if (ptrl_more_like.isHeaderShown()) {
					y=0;
					basePostId=maxBasePostId;
					isNew="true";
				}
				//上拉刷新
				if (ptrl_more_like.isFooterShown()) {
					basePostId=minBasePostId;
					y=likes.size()-1;
					isNew="false";
				}
				getMoreLikes();
				//保存加载的位置
				//y =likeList.size();
			}
		});
	}
}
