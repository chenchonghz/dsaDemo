package com.szrjk.self;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.IPostListCallback;
import com.szrjk.dhome.PostListComm;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.OtherHomePageInfo;
import com.szrjk.entity.PostInfo;
import com.szrjk.entity.PostList;
import com.szrjk.entity.UserCard;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.self.more.AboutActivity;
import com.szrjk.self.more.FeedbackActivity;
@ContentView(R.layout.activity_systemuser)
public class SystemUserActivity extends BaseActivity implements OnClickListener {

	private ImageView iv_vip;
	//头像图片
	private SystemUserActivity instance;
	//用户背景墙
	private ImageView iv_other_user_background;

	private TextView tv_other_location_city;//用户所在城市

	private TextView tv_other_location_district;//用户所在城区

	private TextView tv_other_user_name;//用户名

	private TextView tv_other_user_quarters;//用户职位

	private TextView tv_other_user_hospital;//用户所属医院

	private TextView tv_other_user_department;//用户所属科室

	private FrameLayout fl_message;//消息

	private ImageView iv_addfriends;//增加好友图片

	private TextView tv_addFriends;//增加好友文字

	private ImageView iv_focus;//是否关注

	private TextView tv_focus;//关注文字

	private FrameLayout fl_person_introduction;//个人简介

	private FrameLayout fl_friend;//好友

	private FrameLayout fl_focus;//关注

	private ImageView iv_back;

	private OtherHomePageInfo homePageInfo;
	//list
	private String userId;
	private Context context;
	private ArrayList<UserCard> userList; // 医生信息
	private ArrayList<PostInfo> postList; // 帖子详情
	private UserInfo userInfo;
	@ViewInject(R.id.lv_postlist)
	private PullToRefreshListView mPullRefreshListView;

	private ListView lv_postlist;

	private PostListComm postListComm;
	private String objId;
	private Dialog dialog;
	private FrameLayout fl_le_msg;
	private FrameLayout fl_about;
	private ImageView iv_system_avatar;
	private TextView tv_system_user_hospital;
	@Override
	protected void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		dialog = createDialog(this, "加载中...");

		lv_postlist = mPullRefreshListView.getRefreshableView(); // 获得listView对象
		lv_postlist.setVisibility(View.GONE);
		//		initData();
		//		String basePostId = "0";
		userInfo = Constant.userInfo;
		userId = userInfo.getUserSeqId();
		objId = instance.getIntent().getStringExtra(Constant.USER_SEQ_ID);
		//请求数据
		postListComm = new PostListComm(instance, objId, mPullRefreshListView, new IPostListCallback() {
			@Override
			public void getNewPosts(String userId2, String basePostId, boolean isNew, String beginNum, String endNum) {
				doGetNewPosts(userId2,basePostId,isNew,beginNum,endNum);
			}

			@Override
			public void getPosts(String userId2, String basePostId, boolean isNew, String beginNum, String endNum) {
				doGetPosts(userId2,basePostId,isNew,beginNum,endNum);
			}

			@Override
			public void getMorePosts(String userId2, String basePostId,
					boolean isNew, String beginNum, String endNum) {
				// TODO Auto-generated method stub
				doMoreGetPosts(userId2,basePostId,isNew,beginNum,endNum);
			}
		});

		//		ListView lv_postlist = mPullRefreshListView.getRefreshableView(); // 获得listView对象
		View v = View.inflate(instance, R.layout.systemuser_header, null);
		lv_postlist.addHeaderView(v);
		findHeaderViewId(v);
		findUserInfo();
	}

	protected void doMoreGetPosts(String userId2, String basePostId,
			boolean isNew, String beginNum, String endNum) {
		// TODO Auto-generated method stub
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPersonalPostList");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		//		busiParams.put("objUserSeqId",userId2);
		busiParams.put("objUserSeqId",objId);//目标用户ID是上一层传过来
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
				//				ToastUtils.showMessage(instance, "服务器返回数据失败，请检查网络");
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
					postListComm.operMorePostsSucc(postLists);
				}
				else
				{
					postListComm.operNOT_NEW_POST();
					postListComm.operrefreshComplete();
				}
			}
		});
	}
	//获得头部控件
	private void findHeaderViewId(View v) {
		iv_other_user_background = (ImageView) v.findViewById(R.id.iv_other_user_background);
		iv_back = (ImageView) v.findViewById(R.id.iv_back);
		tv_other_user_name = (TextView) v.findViewById(R.id.tv_other_user_name);
		tv_other_user_hospital = (TextView) v.findViewById(R.id.tv_other_user_hospital);
		fl_le_msg = (FrameLayout) v.findViewById(R.id.fl_le_msg);
		fl_about = (FrameLayout) v.findViewById(R.id.fl_about);
		iv_system_avatar = (ImageView) v.findViewById(R.id.iv_system_avatar);

		iv_back.setOnClickListener(this);
		fl_le_msg.setOnClickListener(this);
		fl_about.setOnClickListener(this);
		iv_system_avatar.setOnClickListener(this);
		
		iv_vip = (ImageView)v.findViewById(R.id.iv_vip);
		tv_system_user_hospital = (TextView)v.findViewById(R.id.tv_system_user_hospital);

	}

	/**
	 * 获取最新帖子
	 * @param userId2
	 * @param basePostId
	 * @param isNew
	 * @param beginNum
	 * @param endNum
	 */
	public void doGetNewPosts(String userId2, String basePostId,
			boolean isNew, String beginNum, String endNum) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPersonalPostList");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		//		busiParams.put("objUserSeqId",userId2);
		busiParams.put("objUserSeqId",objId);//目标用户ID是上一层传过来
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
				//				ToastUtils.showMessage(instance, "服务器返回数据失败，请检查网络");
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


	/**
	 * 发请求获取帖子列表
	 *
	 * @param userId
	 *            用户id
	 * @param basePostId
	 * @param isNew
	 *            是否获取最新帖子
	 * @param beginNum
	 *            起始页码
	 * @param endNum
	 *            终止页码
	 */
	public void doGetPosts(String userId, String basePostId, boolean isNew,
			String beginNum, String endNum)
	{
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPersonalPostList");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());//测试我的ID
		busiParams.put("objUserSeqId",objId);
		busiParams.put("basePostId", basePostId);
		busiParams.put("isNew", String.valueOf(isNew));
		busiParams.put("beginNum", beginNum);
		busiParams.put("endNum", endNum);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void start() {			}

			@Override
			public void loading(long total, long current, boolean isUploading) {			}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject) {
				postListComm.operrefreshComplete();
				//				ToastUtils.showMessage(instance, "服务器返回数据失败，请检查网络");
			}
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					List<PostList> postLists = JSON.parseArray(
							returnObj.getString("ListOut"), PostList.class);

					postListComm.operPostsSucc(postLists);
				} else {
					postListComm.operNOT_NEW_POST();
					postListComm.operrefreshComplete();
				}
			}
		});
	}


	//当别人跳转过来的时候，要把用户ID传过来
	//获取第三方用户的信息
	private void findUserInfo() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryUserHomePage");
		Map<String,Object> busiParam = new HashMap<String, Object>();
		busiParam.put("userId", userId);//当前用户ID  可以用userInfo = Constant.userInfo;userId=userInfo.getUserSeqId();
		busiParam.put("objUserId", objId);//这个是目标用户ID
		paramMap.put("BusiParams", busiParam);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void start() {	dialog.show();		}
			@Override
			public void loading(long total, long current, boolean isUploading) {			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {	
				dialog.dismiss();
				showToast(instance, "获取失败", 1);
			}
			@Override
			public void success(JSONObject jsonObject) {
				dialog.dismiss();
				lv_postlist.setVisibility(View.VISIBLE);
				ErrorInfo errorObj  =JSON.parseObject(jsonObject.getString("ErrorInfo"),ErrorInfo.class);
				if(Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					JSONObject returnObj = jsonObject.getJSONObject("ReturnInfo");
					if (returnObj!=null) {
						JSONObject listoutobj = returnObj.getJSONObject("ListOut");
						homePageInfo = JSON.parseObject(listoutobj.toString(),OtherHomePageInfo.class);
						try {
							updateUI(homePageInfo);
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						showToast(instance, "getJSONObject(ReturnInfo)出错", 1);
					}
				}
			}
		});
	}

	protected void updateUI(OtherHomePageInfo homePageInfo) throws DbException {
		BitmapUtils bitmapUtils = new BitmapUtils(instance);
		if(homePageInfo.getBackgroundUrl()==null||
				homePageInfo.getBackgroundUrl().isEmpty()){
			String uri_bg =getString(R.string.bg_1).toString();
			bitmapUtils.display(iv_other_user_background, uri_bg);

		}else{
			bitmapUtils.display(iv_other_user_background, homePageInfo.getBackgroundUrl());

		}
		if(homePageInfo.getUserFaceUrl()==null|| homePageInfo.getUserFaceUrl().isEmpty()){
			iv_system_avatar.setImageResource(R.drawable.icon_headfailed);
		}else{
			String url = homePageInfo.getUserFaceUrl();
			bitmapUtils.display(iv_system_avatar, url);
		}


		tv_other_user_name.setText(homePageInfo.getUserName());
		//		if (homePageInfo.getProfessionalTitle()==null) {
		//			tv_other_user_quarters.setText(R.string.notProfessionalTitle);
		//		}else{
		//			tv_other_user_quarters.setText(homePageInfo.getProfessionalTitle());
		//		}
		//		tv_other_user_hospital.setText(homePageInfo.getCompanyName());
		//判断是否加v
		if (homePageInfo.getUserLevel() == null) {
			Log.i("homePageInfo.getUserLevel()", "homePageInfo.getUserLevel() == null 检查");
		}else{
			
			if (homePageInfo.getUserLevel().equals("11")) {
				iv_vip.setVisibility(View.VISIBLE);
			}
		}
		
		tv_system_user_hospital.setText(homePageInfo.getCompanyName());
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			instance.finish();
			break;

		case R.id.iv_system_avatar:
			Intent intent = new Intent(instance,UserAvatarImageChangerActivity.class);
			intent.putExtra("image", homePageInfo.getUserFaceUrl());
			intent.putExtra("code", Constant.PICTURE_OTHER_CODE);
			startActivity(intent);
			break;

		case R.id.fl_le_msg:
			//留言
			//			showToast(instance, "未开发留言", 0);
			Intent is = new Intent(instance,FeedbackActivity.class);
			is.putExtra(Constant.USER_SEQ_ID, Constant.userInfo.getUserSeqId());
			startActivity(is);
			break;

		case R.id.fl_about:
			//关于我们
			startActivity(new Intent(instance,AboutActivity.class));
			break;
		}
	}

}

