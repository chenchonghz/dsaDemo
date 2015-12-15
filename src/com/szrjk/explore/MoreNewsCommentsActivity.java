package com.szrjk.explore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.CommendListAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.dhome.R.id;
import com.szrjk.dhome.R.layout;
import com.szrjk.dhome.R.string;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.NewsCommentEntity;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.pull.PullToRefreshBase;
import com.szrjk.pull.PullToRefreshBase.Mode;
import com.szrjk.pull.PullToRefreshBase.OnRefreshListener;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.util.ShowDialogUtil;
import com.szrjk.util.ToastUtils;
import com.umeng.common.message.Log;

@ContentView(R.layout.activity_more_comment)
public class MoreNewsCommentsActivity extends Activity {
	private MoreNewsCommentsActivity instance;
	@ViewInject(R.id.ptrl_more_comment)
	private PullToRefreshListView ptrl_more_comment;
	private ListView lv_comment;
	private String baseCommentTime;
	private String lastCommentTime;
	private String infId;
	private boolean isFirstIn = true;
	private ArrayList<NewsCommentEntity> commentsList;
	Dialog dialog;
	private CommendListAdapter commendAdapter;
	private static final String LOADING_POST = "正在加载评论";
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.HAVE_NEW_POST:
				commendAdapter = new CommendListAdapter(instance, commentsList);
				lv_comment.setAdapter(commendAdapter);
				break;
			case Constant.HAVE_NEW_POST_BY_REFRESH:
				commendAdapter.notifyDataSetChanged();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance=this;
		initData();
		initListner();
		getComments();
		

	}

	private void initListner() {
		// TODO Auto-generated method stub
		ptrl_more_comment.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				//下拉刷新
				if (ptrl_more_comment.isHeaderShown()) {
					getComments();
				}
				//上拉刷新
				if (ptrl_more_comment.isFooterShown()) {
					getMoreComments();
				}
			}
		});
	}

	protected void getMoreComments() {
		// TODO Auto-generated method stub
		try {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ServiceName", "queryInfComment");
			Map<String, Object> busiParams = new HashMap<String, Object>();
			busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
			busiParams.put("infId", infId);
			busiParams.put("baseCommentTime", lastCommentTime);
			busiParams.put("count", "10");
			paramMap.put("BusiParams", busiParams);
			DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {

				@Override
				public void success(JSONObject jsonObject) {
					// TODO Auto-generated method stub
					if (ptrl_more_comment.isRefreshing())
					{
						ptrl_more_comment.onRefreshComplete();
					}
					ErrorInfo errorObj = JSON.parseObject(
							jsonObject.getString("ErrorInfo"), ErrorInfo.class);
					if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
					{
						JSONObject returnObj = jsonObject
								.getJSONObject("ReturnInfo");
						JSONArray arr = JSON.parseArray(returnObj.getString("ListOut"));
						if(arr != null){
							if(!arr.isEmpty()){	
								for (int i = 0; i < arr.size(); i++) {
									NewsCommentEntity comment = JSON.parseObject(arr.getString(i), NewsCommentEntity.class);
									commentsList.add(comment);
								}
								if(commentsList.get(commentsList.size()-1).getCommentTime()!=null){
									lastCommentTime = commentsList.get(commentsList.size()-1).getCommentTime();
								}
							}else{
								ToastUtils.showMessage(instance, "没有更多评论了");
							}
							handler.sendEmptyMessage(Constant.HAVE_NEW_POST_BY_REFRESH);
							Log.e("MoreNewsComment", commentsList.toString());	
						}else{
							ToastUtils.showMessage(instance, "获取更多评论失败");
						}

					}else{
						ToastUtils.showMessage(instance, "获取更多评论失败");
					}
				}

				@Override
				public void start() {
					// TODO Auto-generated method stub
				}

				@Override
				public void loading(long total, long current, boolean isUploading) {
					// TODO Auto-generated method stub

				}

				@Override
				public void failure(HttpException exception, JSONObject jobj) {
					// TODO Auto-generated method stub
					if (ptrl_more_comment.isRefreshing())
					{
						ptrl_more_comment.onRefreshComplete();
					}
					ToastUtils.showMessage(instance, "获取更多评论失败，请检查网络");
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("MoreNewsComment", e.toString());
		}

	}

	private void getComments() {
		// TODO Auto-generated method stub
		try {
			commentsList = new ArrayList<NewsCommentEntity>();
			DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
			String currentTime = fmt.format(new Date());
			baseCommentTime = currentTime;
			Log.e("MoreNewsComment", "时间："+baseCommentTime);

			//		baseCommentTime = commentsList.get(commentsList.size()-1).getCommentTime();
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ServiceName", "queryInfComment");
			Map<String, Object> busiParams = new HashMap<String, Object>();
			busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
			busiParams.put("infId", infId);
			busiParams.put("baseCommentTime", baseCommentTime);
			busiParams.put("count", "10");
			paramMap.put("BusiParams", busiParams);
			DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {

				@Override
				public void success(JSONObject jsonObject) {
					// TODO Auto-generated method stub
					if (dialog.isShowing())
					{
						dialog.dismiss();
					}
					if (ptrl_more_comment.isRefreshing())
					{
						ptrl_more_comment.onRefreshComplete();
					}
					ErrorInfo errorObj = JSON.parseObject(
							jsonObject.getString("ErrorInfo"), ErrorInfo.class);
					if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
					{
						JSONObject returnObj = jsonObject
								.getJSONObject("ReturnInfo");
						JSONArray arr = JSON.parseArray(returnObj.getString("ListOut"));
						if(arr != null){
							if(!arr.isEmpty()){	
								for (int i = 0; i < arr.size(); i++) {
									NewsCommentEntity comment = JSON.parseObject(arr.getString(i), NewsCommentEntity.class);
									commentsList.add(comment);
								}
								if(commentsList.get(commentsList.size()-1).getCommentTime()!=null){
									lastCommentTime = commentsList.get(commentsList.size()-1).getCommentTime();
								}
							}else{
								ToastUtils.showMessage(instance, "没有更多评论了");
							}
							handler.sendEmptyMessage(Constant.HAVE_NEW_POST);
							Log.e("MoreNewsComment", commentsList.toString());	
						}else{
							ToastUtils.showMessage(instance, "获取更多评论失败");
						}

					}else{
						ToastUtils.showMessage(instance, "获取更多评论失败");
					}
				}

				@Override
				public void start() {
					// TODO Auto-generated method stub
					if(isFirstIn){	
						dialog.show();
						isFirstIn = false;
					}
				}

				@Override
				public void loading(long total, long current, boolean isUploading) {
					// TODO Auto-generated method stub

				}

				@Override
				public void failure(HttpException exception, JSONObject jobj) {
					// TODO Auto-generated method stub
					if (dialog.isShowing())
					{
						dialog.dismiss();
					}
					if (ptrl_more_comment.isRefreshing())
					{
						ptrl_more_comment.onRefreshComplete();
					}
					ToastUtils.showMessage(instance, "获取更多评论失败，请检查网络");
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("MoreNewsComment", e.toString());
		}

	}

	private void initData() {
		// TODO Auto-generated method stub
		ptrl_more_comment.setMode(Mode.BOTH);
		ptrl_more_comment.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_down_lable_text));
		ptrl_more_comment.getLoadingLayoutProxy(false, true).setPullLabel(
				getResources().getString(R.string.pull_up_lable_text));
		ptrl_more_comment.getLoadingLayoutProxy(true, true)
		.setRefreshingLabel(
				getResources()
				.getString(R.string.refreshing_lable_text));
		ptrl_more_comment.getLoadingLayoutProxy(true, true)
		.setReleaseLabel(
				getResources().getString(R.string.release_lable_text));
		lv_comment = ptrl_more_comment.getRefreshableView();
		Intent intent = getIntent();
		infId = intent.getStringExtra("infId");
		dialog = ShowDialogUtil.createDialog(this, LOADING_POST);
		
	}
}
