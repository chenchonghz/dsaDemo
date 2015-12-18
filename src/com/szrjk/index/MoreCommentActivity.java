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
import com.szrjk.entity.Comment;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.UserCard;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.pull.PullToRefreshBase;
import com.szrjk.pull.PullToRefreshBase.Mode;
import com.szrjk.pull.PullToRefreshBase.OnRefreshListener;
import com.szrjk.pull.PullToRefreshListView;

@ContentView(R.layout.activity_more_comment)
public class MoreCommentActivity extends BaseActivity{
	
	@ViewInject(R.id.ptrl_more_comment)
	private PullToRefreshListView ptrl_more_comment;
	
	private MoreCommentActivity instance;
	private int tabId=2;
	private boolean isMore=true;

	private ListView lv_comment;
	
	private ArrayList<Comment> comments=new ArrayList<Comment>();
	
	private String srcPostId;

	private String basePostId;
	
	private String maxBasePostId;
	
	private String minBasePostId;
	
	private String isNew="true"; 

	protected int y;

	protected boolean isFirst=true;

	private PostCommentAdapter<Comment> commentAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance=this;
		initLayout();
		setAdapter(comments);
		initListener();
	}

	private void initLayout() {
		lv_comment=ptrl_more_comment.getRefreshableView();
		ptrl_more_comment.setMode(Mode.BOTH);
		Intent intent=getIntent();
		srcPostId = intent.getStringExtra(Constant.POST_ID);
		basePostId = "0";
		getMoreComments();
	}

	private void getMoreComments() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPostOperationList");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("basePostId", basePostId);
		busiParams.put("srcPostId", srcPostId);
		busiParams.put("isNew", isNew);
		busiParams.put("queryType", "COMMENT");
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
							ArrayList<Comment> commentList = (ArrayList<Comment>) JSON
									.parseArray(
											returnObj.getString("ListOut"),
											Comment.class);
							//层次太深，抽出来
							com.alibaba.fastjson.JSONArray jsonCommentArray  = returnObj.getJSONArray("ListOut");
							for (int i = 0;i<jsonCommentArray.size();i++){
								String tmppusercard = jsonCommentArray.getJSONObject(i).getJSONObject("commentInfo").getString("pUserCard");
								if(tmppusercard==null||tmppusercard.equals(""))continue;
								UserCard tmpuserCard = JSON.parseObject(tmppusercard, UserCard.class);
								commentList.get(i).getCommentInfo().setpUserCard(tmpuserCard);
							}
							if (commentList!=null&&!commentList.isEmpty()) {
								if (isFirst) {
									comments.addAll(commentList);
									isFirst=false;
								}
								if (ptrl_more_comment.isFooterShown()) {
									comments.addAll(comments.size(), commentList);
								}else if (ptrl_more_comment.isHeaderShown()) {
									comments.addAll(0, commentList);
								}
								minBasePostId=comments.get(comments.size()-1).getCommentInfo().getPostId();
								maxBasePostId=comments.get(0).getCommentInfo().getPostId();
								//Log.i("comments", DjsonUtils.bean2Json(comments));
								commentAdapter.notifyDataSetChanged();
							}
							if (ptrl_more_comment.isRefreshing()) {
								ptrl_more_comment.onRefreshComplete();
							}
							lv_comment.setSelectionFromTop(y,0);
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

	protected void setAdapter(ArrayList<Comment> commentList) {
		commentAdapter=PostCommentAdapter.getPostCommentAdapter();
		commentAdapter.setData(instance, commentList, tabId,isMore);
		lv_comment.setAdapter(commentAdapter);
	}
	
	private void initListener(){
		ptrl_more_comment.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				//				new GetDataTask().execute();
				
				//发送请求得到新项目报文
				//下拉刷新
				if (ptrl_more_comment.isHeaderShown()) {
					y=0;
					basePostId=maxBasePostId;
					isNew="true";
				}
				//上拉刷新
				if (ptrl_more_comment.isFooterShown()) {
					basePostId=minBasePostId;
					y=comments.size()-1;
					isNew="false";
				}
				getMoreComments();
				//保存加载的位置
				//y =commentList.size();
			}
		});
	}
}
