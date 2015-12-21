package com.szrjk.self.more;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.IPostListCallback;
import com.szrjk.dhome.PostListComm;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.PostInfo;
import com.szrjk.entity.PostList;
import com.szrjk.entity.PostOtherImformationInfo;
import com.szrjk.entity.UserCard;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.util.ShowDialogUtil;

@ContentView(R.layout.activity_problem_help)
public class ProblemHelpActivity extends BaseActivity {

	private ProblemHelpActivity instance;
	@ViewInject(R.id.lv_problemhelp_postlist)
	private PullToRefreshListView mPullRefreshListView;
	private String userId;
	private Dialog dialog;
	private static final String LOADING_POST = "正在加载帖子";
	private PostListComm postListComm;
	private String type;
	public static int POSITION = -1;
	public static boolean ISDELETE;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(instance);
		initActivityData();
	}

	private void initActivityData() {
		userId = Constant.userInfo.getUserSeqId();
		dialog = ShowDialogUtil.createDialog(this, LOADING_POST);
		Intent intent = getIntent();
		type = intent.getStringExtra("postType");
		postListComm = new PostListComm(instance, userId, mPullRefreshListView, new IPostListCallback() {
			@Override
			public void getNewPosts(String userId2, String basePostId, boolean isNew, String beginNum, String endNum) {
				doGetNewPosts(userId2,basePostId,isNew,beginNum,endNum);
			}
			@Override
			public void getMorePosts(String userId2, String basePostId, boolean isNew, String beginNum, String endNum) {
				doGetMorePosts(userId2,basePostId,isNew,beginNum,endNum);
			}
			@Override
			public void getPosts(String userId2, String basePostId,
					boolean isNew, String beginNum, String endNum) {
				doGetPosts(userId2,basePostId,isNew,beginNum,endNum);
			}
		});
	}
	
	protected void doGetMorePosts(String userId2, String basePostId,
			boolean isNew, String beginNum, String endNum) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPostListByType");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", userId);
		busiParams.put("postType",Constant.PROBLEM_HELP);
		busiParams.put("queryType",type);
		busiParams.put("basePostId", basePostId);
		busiParams.put("isNew", String.valueOf(isNew));
		busiParams.put("beginNum", beginNum);
		busiParams.put("endNum", endNum);
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
					List<PostList> postLists = JSON.parseArray(returnObj.getString("ListOut"), PostList.class);
					postListComm.operMorePostsSucc(postLists);
				}
				else
				{
					postListComm.operNOT_NEW_POST();
					postListComm.operrefreshComplete();
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
				postListComm.operrefreshComplete();
				showToast(ProblemHelpActivity.this, "获取帖子失败，请检查网络", Toast.LENGTH_SHORT);
			}
		});
	}

	protected void doGetPosts(String userId2, String basePostId, boolean isNew,
			String beginNum, String endNum) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPostListByType");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", userId);
		busiParams.put("postType",Constant.PROBLEM_HELP);
		busiParams.put("queryType",type);
		busiParams.put("basePostId", basePostId);
		busiParams.put("isNew", String.valueOf(isNew));
		busiParams.put("beginNum", beginNum);
		busiParams.put("endNum", endNum);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			
			@Override
			public void success(JSONObject jsonObject) {
				if (dialog.isShowing())
				{
					dialog.dismiss();
				}
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					List<PostList> postLists = JSON.parseArray(returnObj.getString("ListOut"), PostList.class);
					postListComm.operPostsSucc(postLists);
				}
				else
				{
					postListComm.operNOT_NEW_POST();
					postListComm.operrefreshComplete();
				}
			}
			
			@Override
			public void start() {
				if (!mPullRefreshListView.isRefreshing())
				{
					dialog.show();
				}
				
			}
			
			@Override
			public void loading(long total, long current, boolean isUploading) {
				
			}
			
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				if (dialog.isShowing())
		        {
		            dialog.dismiss();
		        }
				postListComm.operrefreshComplete();
				showToast(ProblemHelpActivity.this, "获取帖子失败，请检查网络", Toast.LENGTH_SHORT);
			}
		});
	}

	protected void doGetNewPosts(String userId2, String basePostId,
			boolean isNew, String beginNum, String endNum) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPostListByType");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", userId);
		busiParams.put("postType",Constant.PROBLEM_HELP);
		busiParams.put("queryType",type);
		busiParams.put("basePostId", basePostId);
		busiParams.put("isNew", String.valueOf(isNew));
		busiParams.put("beginNum", beginNum);
		busiParams.put("endNum", endNum);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack()
		{
			@Override
			public void start() {		}
			@Override
			public void loading(long total, long current, boolean isUploading) {}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject) {
				postListComm.operrefreshComplete();
				showToast(ProblemHelpActivity.this, "获取帖子失败，请检查网络", Toast.LENGTH_SHORT);
			}
			@Override
			public void success(JSONObject jsonObject)
			{
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					List<PostList> postLists = JSON.parseArray(returnObj.getString("ListOut"), PostList.class);
					Collections.reverse(postLists);
					postListComm.operNewPostsSucc(postLists);
				}
				else
				{
					postListComm.operNOT_NEW_POST();
					postListComm.operrefreshComplete();
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(postListComm!= null){
			if(POSITION != -1){
				if(ISDELETE){
					ArrayList<PostInfo> postList = postListComm.getPostList();
					ArrayList<UserCard> userList = postListComm.getUserList();
					ArrayList<PostOtherImformationInfo> postOtherList = postListComm.getPostOtherList();
					postList.remove(POSITION);
					userList.remove(POSITION);
					postOtherList.remove(POSITION);
					postListComm.setPostList(postList);
					postListComm.setUserList(userList);
					postListComm.setPostOtherList(postOtherList);
					postListComm.updateData();
					POSITION = -1;
					ISDELETE = false;
				}
			}
		}
		super.onResume();
	}
}
