package com.szrjk.dhome;

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
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.OtherHomePageInfo;
import com.szrjk.entity.PostInfo;
import com.szrjk.entity.PostList;
import com.szrjk.entity.TCity;
import com.szrjk.entity.UserCard;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.message.MessageActivity;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.self.UserAvatarImageChangerActivity;
import com.szrjk.self.more.MyAttentionActivity;
import com.szrjk.self.more.MyFansActivity;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.util.ToastUtils;
@ContentView(R.layout.activity_otherpeople)
public class OtherPeopleActivity extends BaseActivity implements OnClickListener {

	//头像图片
	private ImageView iv_self_avatar;
	private OtherPeopleActivity instance;
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
	//关注数
	private LinearLayout ll_attention_count;
	private TextView tv_attention_num;
	//粉丝数
	private LinearLayout ll_fans_count;
	private TextView tv_fans_num;

	private ImageView iv_back;

	//加v图片
	@ViewInject(R.id.iv_vip)
	private ImageView iv_vip;

	private OtherHomePageInfo homePageInfo;
	//list
	private String userId;
	private String mMaxPostId; // 最大postId
	private String mMinPostId; // 最小postId
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
		userId = Constant.userInfo.getUserSeqId();
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
		View v = View.inflate(instance, R.layout.other_header, null);
		lv_postlist.addHeaderView(v);
		findHeaderViewId(v);
		findUserInfo();
	}
	
	

	public String getObjId() {
		return objId;
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
		tv_other_location_city = (TextView) v.findViewById(R.id.tv_other_location_city);
		tv_other_location_district = (TextView) v.findViewById(R.id.tv_other_location_district);
		tv_other_user_name = (TextView) v.findViewById(R.id.tv_other_user_name);
		tv_other_user_quarters = (TextView) v.findViewById(R.id.tv_other_user_quarters);
		tv_other_user_hospital = (TextView) v.findViewById(R.id.tv_other_user_hospital);
		tv_other_user_department = (TextView) v.findViewById(R.id.tv_other_user_department);
		iv_addfriends = (ImageView) v.findViewById(R.id.iv_addfriends);
		tv_addFriends = (TextView) v.findViewById(R.id.tv_addFriends);
		iv_focus = (ImageView) v.findViewById(R.id.iv_focus);
		tv_focus = (TextView) v.findViewById(R.id.tv_focus);
		fl_message = (FrameLayout) v.findViewById(R.id.fl_message);
		fl_person_introduction = (FrameLayout) v.findViewById(R.id.fl_person_introduction);
		fl_friend = (FrameLayout) v.findViewById(R.id.fl_friend);
		fl_focus = (FrameLayout) v.findViewById(R.id.fl_focus);
		iv_self_avatar = (ImageView) v.findViewById(R.id.iv_self_avatar);
		iv_vip = (ImageView) v.findViewById(R.id.iv_vip);
		ll_attention_count = (LinearLayout)v.findViewById(R.id.ll_attention_count);
		ll_fans_count = (LinearLayout)v.findViewById(R.id.ll_fans_count);
		tv_attention_num = (TextView) v.findViewById(R.id.tv_attention_num);
		tv_fans_num = (TextView) v.findViewById(R.id.tv_fans_num);
		iv_back.setOnClickListener(this);
		fl_focus.setOnClickListener(this);
		fl_friend.setOnClickListener(this);
		fl_message.setOnClickListener(this);
		fl_person_introduction.setOnClickListener(this);
		iv_self_avatar.setOnClickListener(this);
		ll_attention_count.setOnClickListener(this);
		ll_fans_count.setOnClickListener(this);

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

				lv_postlist.setVisibility(View.VISIBLE);
				ErrorInfo errorObj  =JSON.parseObject(jsonObject.getString("ErrorInfo"),ErrorInfo.class);
				if(Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					JSONObject returnObj = jsonObject.getJSONObject("ReturnInfo");
					if (returnObj!=null) {
						JSONObject listoutobj = returnObj.getJSONObject("ListOut");
						homePageInfo = JSON.parseObject(listoutobj.toString(),OtherHomePageInfo.class);
						try {
							dialog.dismiss();
							if (homePageInfo.getUserSeqId()==null) {
								ToastUtils.showMessage(instance, "错误用户");
								instance.finish();
							}else{
								updateUI(homePageInfo);
							}
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						dialog.dismiss();
						showToast(instance, "getJSONObject(ReturnInfo)出错", 1);
					}
				}
			}
		});
	}

	protected void updateUI(OtherHomePageInfo homePageInfo) throws DbException {
		try {
			ImageLoaderUtil imageLoaderUtil2bg = new ImageLoaderUtil(
					instance, homePageInfo.getBackgroundUrl(), 
					iv_other_user_background, R.drawable.pic_popupmask, R.drawable.pic_popupmask);
			imageLoaderUtil2bg.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
//		BitmapUtils bitmapUtils = new BitmapUtils(instance);
//		if(homePageInfo.getBackgroundUrl()==null||
//				homePageInfo.getBackgroundUrl().isEmpty()){
//			String uri_bg =getString(R.string.bg_1).toString();
//			bitmapUtils.display(iv_other_user_background, uri_bg);
//
//		}else{
//			bitmapUtils.display(iv_other_user_background, homePageInfo.getBackgroundUrl());
//
//		}
		if(homePageInfo.getUserFaceUrl()==null||
				homePageInfo.getUserFaceUrl().isEmpty()){
			iv_self_avatar.setImageResource(R.drawable.icon_headfailed);
		}else{
			String url = homePageInfo.getUserFaceUrl();
			ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil(instance, url, iv_self_avatar,
					R.drawable.header, R.drawable.header);
			imageLoaderUtil.showImage();
//			bitmapUtils.display(iv_self_avatar, url);
		}
		//判断是否加v
		if (homePageInfo.getUserLevel().equals("11")) {
			iv_vip.setVisibility(View.VISIBLE);
		}

		tv_other_location_city.setText((new TCity()).getProvince(instance, homePageInfo.getProvince()));
		tv_other_location_district.setText((new TCity()).getCity(instance, homePageInfo.getCityCode()));
		tv_other_user_name.setText(homePageInfo.getUserName());
		if (homePageInfo.getProfessionalTitle()==null) {
			tv_other_user_quarters.setText(R.string.notProfessionalTitle);
		}else{
			tv_other_user_quarters.setText(homePageInfo.getProfessionalTitle());
		}
		tv_other_user_hospital.setText(homePageInfo.getCompanyName());
		tv_other_user_department.setText(homePageInfo.getDeptName());
		if (homePageInfo.getCompanyName().length()+homePageInfo.getDeptName().length()>18) {
			RelativeLayout.LayoutParams layoutParams = 
					new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
							LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.BELOW, R.id.tv_other_user_hospital);
			layoutParams.setMargins(0, 10, 0, 0);
//			layoutParams.setMargins(0, 5, 0, 0);
			tv_other_user_department.setLayoutParams(layoutParams);
		}else{
			RelativeLayout.LayoutParams layoutParams = 
					new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
							LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.tv_other_user_hospital);
			layoutParams.setMargins(10, 0, 0, 0);
			tv_other_user_department.setLayoutParams(layoutParams);
		}
		//+判断，是否关注，是否好友
		//		showToast(instance, "当前关注："+homePageInfo.getIsFollow()+ "  是否好友:"+homePageInfo.getIsFriend(), 1);


		if ("true".equals(homePageInfo.getIsFriend())) {
			fl_message.setVisibility(View.VISIBLE);
			iv_addfriends.setImageResource(R.drawable.icon_canclefriends);
			tv_addFriends.setText(R.string.canclefriends);
		}else{
			fl_message.setVisibility(View.GONE);
			iv_addfriends.setImageResource(R.drawable.icon_addfriends);
			tv_addFriends.setText(R.string.addfriends);
		}
		//		System.err.println("是否关注:  "+homePageInfo.getIsFollow());
		if ("false".equals(homePageInfo.getIsFollow())) {
			iv_focus.setImageResource(R.drawable.icon_addfocus);
			tv_focus.setText(R.string.addfocus);
		}else{
			iv_focus.setImageResource(R.drawable.icon_canclefocus);
			tv_focus.setText(R.string.canclefocus);
		}
		tv_attention_num.setText(homePageInfo.getFocusCount());
		tv_fans_num.setText(homePageInfo.getFollowerCount());
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			instance.finish();
			break;
		case R.id.fl_focus:
			//点击关注，调接口，如果提示关注成功，提示！，然后改变按钮
			sendFocusRequest();
			break;
		case R.id.fl_friend:
			//发送加好友的请求
			//先获得是否是好友
			sendFriendRequest();
			break;
		case R.id.iv_self_avatar:
			Intent intent = new Intent(instance,UserAvatarImageChangerActivity.class);
			intent.putExtra("image", homePageInfo.getUserFaceUrl());
			intent.putExtra("code", Constant.PICTURE_OTHER_CODE);
			startActivity(intent);
			break;
		case R.id.fl_message:
			//			showToast(instance, "未开发，测试跳转系统管理员主页", 0);
			//			Intent is = new Intent(instance,SystemUserActivity.class);
			//			is.putExtra(Constant.USER_SEQ_ID, "1001");
			//			startActivity(is);
			Intent intent_message = new Intent(instance, MessageActivity.class);
			UserCard usercard = new UserCard();
			usercard.setUserSeqId(homePageInfo.getUserSeqId());
			usercard.setUserName(homePageInfo.getUserName());
			usercard.setUserFaceUrl(homePageInfo.getUserFaceUrl());
			usercard.setCompanyName(homePageInfo.getCompanyName());
			usercard.setDeptName(homePageInfo.getDeptName());
			usercard.setProfessionalTitle(homePageInfo.getProfessionalTitle());
			usercard.setUserLevel(homePageInfo.getUserLevel());
			usercard.setUserType(homePageInfo.getUserType());
			intent_message.putExtra("otherusercard", usercard);
			startActivity(intent_message);	
			break;
		case R.id.fl_person_introduction:

			Intent intent2 = new Intent(instance ,IntroduceActivity.class);
			intent2.putExtra(Constant.USER_SEQ_ID, objId);
			startActivity(intent2);
			break;
			
		case R.id.ll_attention_count:
			Intent intent_attention = new Intent(instance, MyAttentionActivity.class);
			intent_attention.putExtra("username", homePageInfo.getUserName());
			intent_attention.putExtra(Constant.USER_SEQ_ID, objId);
			startActivity(intent_attention);
			break;
		case R.id.ll_fans_count:
			Intent intent_fans = new Intent(instance, MyFansActivity.class);
			intent_fans.putExtra("username", homePageInfo.getUserName());
			intent_fans.putExtra(Constant.USER_SEQ_ID, objId);
			startActivity(intent_fans);
			break;
		}
	}
	private void sendFriendRequest() {
		String st = homePageInfo.getIsFriend();
		if ("true".equals(st)) {
			//取消
			changeFriendState("D");
		}else{
			//加
			changeFriendState("A");
		}
	}
	private void changeFriendState(final String string) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "dealUserFriendRel");
		Map<String,Object> busiParam = new HashMap<String, Object>();
		busiParam.put("userSeqId", userId);//当前用户ID  可以用userInfo = Constant.userInfo;userId=userInfo.getUserSeqId();
		busiParam.put("objUserSeqId", objId);//这个是目标用户ID
		busiParam.put("operateType", string);//是不是加关注
		busiParam.put("description", "");
		paramMap.put("BusiParams", busiParam);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void start() {
			}
			@Override
			public void loading(long total, long current, boolean isUploading) {
			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
			}
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj  =JSON.parseObject(jsonObject.getString("ErrorInfo"),ErrorInfo.class);
				if(Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					if ("A".equals(string)) {
						showToast(instance, "已发送   加好友请求！", 0);
					}else{
						//						showToast(instance, "点击了取消", 0);
						fl_message.setVisibility(View.GONE);
						//						iv_addfriends.setImageResource(R.drawable.icon_addfriends);
						//						tv_addFriends.setText(R.string.addfriends);
						findUserInfo();
					}
				}
			}
		});

	}
	private void sendFocusRequest() {
		//先获得关系；如果已经关注，点击就是取消，反之
		String fol = homePageInfo.getIsFollow();
		if ("true".equals(fol)) {
			//发取消
			changeState("D");
			//			showToast(instance, "点击了取消", 0);
		}else{
			//发添加
			changeState("A");
			//			showToast(instance, "点击了关注", 0);
		}
	}
	private void changeState(final String state) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "dealUserFocus");
		Map<String,Object> busiParam = new HashMap<String, Object>();
		busiParam.put("userSeqId", userId);//当前用户ID  可以用userInfo = Constant.userInfo;userId=userInfo.getUserSeqId();
		busiParam.put("objUserSeqId", objId);//这个是目标用户ID
		busiParam.put("operateType", state);//是不是加关注
		paramMap.put("BusiParams", busiParam);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void start() {
			}
			@Override
			public void loading(long total, long current, boolean isUploading) {
			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
			}
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj  =JSON.parseObject(jsonObject.getString("ErrorInfo"),ErrorInfo.class);
				if(Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					if (state.equals("D")) {
						showToast(instance, "取消关注成功", 1);
						findUserInfo();
						//						iv_focus.setImageResource(R.drawable.icon_addfocus);
						//						tv_focus.setText(R.string.addfocus);
						//						homePageInfo.setIsFollow("false");
					}else{
						showToast(instance, "关注成功", 1);
						//						iv_focus.setImageResource(R.drawable.icon_canclefocus);
						//						tv_focus.setText(R.string.canclefocus);
						findUserInfo();
						//						homePageInfo.setIsFollow("true");
					}
				}
			}
		});

	}

}

