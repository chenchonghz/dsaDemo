package com.szrjk.dhome;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import com.szrjk.config.Constant;
import com.szrjk.config.ConstantUser;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.PostInfo;
import com.szrjk.entity.PostList;
import com.szrjk.entity.PostOtherImformationInfo;
import com.szrjk.entity.RemindEvent;
import com.szrjk.entity.TCity;
import com.szrjk.entity.UserCard;
import com.szrjk.entity.UserHomePageInfo;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.message.MessageListActivity;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.self.CircleActivity;
import com.szrjk.self.FriendActivity;
import com.szrjk.self.UserBackgroundSelectActivity;
import com.szrjk.self.more.ChangePortraitActivity;
import com.szrjk.self.more.MoreActivity;
import com.szrjk.self.more.MyAttentionActivity;
import com.szrjk.self.more.MyFansActivity;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.SelfChangeBgPopup;

import de.greenrobot.event.EventBus;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
@ContentView(R.layout.self_fragment)
public class SelfActivity extends BaseActivity implements OnClickListener {
	//用户背景墙
		private ImageView iv_self_user_background;
		//用户所在城市
		private TextView tv_self_location_city;
		//用户所在城区
		private TextView tv_self_location_district;
		//用户名
		private TextView tv_self_user_name;
		//用户职位
		private TextView tv_self_user_quarters;
		//用户所属医院
		private TextView tv_self_user_hospital;
		//用户所属科室
		private TextView tv_self_user_department;
		//用户头像
		private ImageView iv_self_avatar;
		//用户的帖子列表
		private ListView lv_self;
		//加V图片
		private ImageView iv_vip; 
		//医院与科室布局
		private RelativeLayout rly_hospital_dept;
		//用户关注数
		private LinearLayout ll_attention_count;
		private TextView tv_attention_num;
		//用户粉丝数
		private LinearLayout ll_fans_count;
		private TextView tv_fans_num;

		private SelfChangeBgPopup pop;
		//用户信息
		private String url;
		//回退按钮
		private LinearLayout lly_hv;

		private static final int CHANGE_PORTRAIT_SUCCESS=0;
		private String userId;
		@ViewInject(R.id.lv_postlist)
		private PullToRefreshListView mPullRefreshListView;
		private ListView lv_postlist;
		private MainActivity mContext;
		private PostListComm postListComm;
		public UserHomePageInfo userHomePageInfo;
		private Dialog dialog;
		private SelfActivity instance;
		private UserInfo userInfo;
		public static int POSITION;
		public static boolean ISDELETE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		dialog = createDialog(this, "加载中...");
		lv_postlist = mPullRefreshListView.getRefreshableView(); // 获得listView对象
		//		initData();
		//		String basePostId = "0";
		userInfo = Constant.userInfo;
		userId = Constant.userInfo.getUserSeqId();
		postListComm = new PostListComm(instance, Constant.userInfo.getUserSeqId(), mPullRefreshListView, new IPostListCallback() {
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
		lv_postlist = mPullRefreshListView.getRefreshableView(); // 获得listView对象
		View v = View.inflate(instance, R.layout.activity_self, null);
		lv_postlist.addHeaderView(v);
		iv_self_user_background = (ImageView) v.findViewById(R.id.iv_self_user_background);
		tv_self_location_city= (TextView) v.findViewById(R.id.tv_self_location_city);
		tv_self_location_district= (TextView) v.findViewById(R.id.tv_self_location_district);
		tv_self_user_name= (TextView) v.findViewById(R.id.tv_self_user_name);
		tv_self_user_quarters= (TextView) v.findViewById(R.id.tv_self_user_quarters);
		tv_self_user_hospital= (TextView) v.findViewById(R.id.tv_self_user_hospital);
		tv_self_user_department= (TextView) v.findViewById(R.id.tv_self_user_department);
		iv_self_avatar= (ImageView) v.findViewById(R.id.iv_self_avatar);
		iv_vip = (ImageView) v.findViewById(R.id.iv_vip);
		rly_hospital_dept = (RelativeLayout) v.findViewById(R.id.rly_hospital_dept);
		ll_attention_count =(LinearLayout) v.findViewById(R.id.ll_attention_count);
		ll_fans_count =(LinearLayout) v.findViewById(R.id.ll_fans_count);
		tv_attention_num = (TextView) v.findViewById(R.id.tv_attention_num);
		tv_fans_num = (TextView) v.findViewById(R.id.tv_fans_num);
		lly_hv = (LinearLayout) v.findViewById(R.id.lly_hv);
		getUserHpInfo(Constant.userInfo.getUserSeqId());
		initListener();
	}
	private void initListener() {
		//界面监听
		iv_self_avatar.setOnClickListener(this);
		iv_self_user_background.setOnClickListener(this);
		tv_self_user_name.setOnClickListener(this);
		tv_self_user_hospital.setOnClickListener(this);
		tv_self_user_quarters.setOnClickListener(this);
		tv_self_user_department.setOnClickListener(this);
		ll_attention_count.setOnClickListener(this);
		ll_fans_count.setOnClickListener(this);
		lly_hv.setOnClickListener(this);
	}

	protected void doMoreGetPosts(String userId2, String basePostId,
			boolean isNew, String beginNum, String endNum) {
		// TODO Auto-generated method stub
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPersonalPostList");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId",Constant.userInfo.getUserSeqId());
		busiParams.put("objUserSeqId", Constant.userInfo.getUserSeqId());
		busiParams.put("basePostId", basePostId);
		busiParams.put("isNew", String.valueOf(isNew));
		busiParams.put("beginNum", beginNum);
		busiParams.put("endNum", endNum);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack()
		{
			@Override
			public void success(JSONObject jsonObject)
			{
				dialog.dismiss();
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
			public void start() {dialog.show();

			}

			@Override
			public void loading(long total, long current, boolean isUploading) {

			}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject) {
				postListComm.operrefreshComplete();
				ToastUtils.showMessage(instance, "服务器返回数据失败，请检查网络");
				dialog.dismiss();
			}
		});
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
		busiParams.put("userSeqId",Constant.userInfo.getUserSeqId());
		busiParams.put("objUserSeqId", Constant.userInfo.getUserSeqId());
		busiParams.put("basePostId", basePostId);
		busiParams.put("isNew", String.valueOf(isNew));
		busiParams.put("beginNum", beginNum);
		busiParams.put("endNum", endNum);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack()
		{
			@Override
			public void success(JSONObject jsonObject)
			{
				dialog.dismiss();
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

			@Override
			public void start() {dialog.show();
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {

			}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject) {
				postListComm.operrefreshComplete();
				ToastUtils.showMessage(instance, "服务器返回数据失败，请检查网络");
				dialog.dismiss();
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
		busiParams.put("userSeqId", userId);
		busiParams.put("objUserSeqId", userId);
		busiParams.put("basePostId", basePostId);
		busiParams.put("isNew", String.valueOf(isNew));
		busiParams.put("beginNum", beginNum);
		busiParams.put("endNum", endNum);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				dialog.dismiss();
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

			@Override
			public void start() {dialog.show();
			//				if (!mPullRefreshListView.isRefreshing()) {
			//					dialog.show();
			//				}
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {

			}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject) {
				postListComm.operrefreshComplete();
				ToastUtils.showMessage(instance, "服务器返回数据失败，请检查网络");
				dialog.dismiss();
			}
		});
	}
	public void getUserHpInfo(String userId2) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryUserHomePage");
		Map<String,Object> busiParam = new HashMap<String, Object>();
		busiParam.put("userId", userId2);
		busiParam.put("objUserId", userId2);
		paramMap.put("BusiParams", busiParam);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				dialog.dismiss();
				ErrorInfo errorObj  =JSON.parseObject(
						jsonObject.getString("ErrorInfo"),ErrorInfo.class);
				if(Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					JSONObject resObj = returnObj
							.getJSONObject("ListOut");
					userHomePageInfo =JSON.parseObject(
							resObj.toJSONString(),UserHomePageInfo.class) ;
					//数据导入
					try {
						initViews(userHomePageInfo);
					} catch (DbException e) {
						e.printStackTrace();
					}
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
				try {
					dialog.dismiss();
					ToastUtils.showMessage(mContext, "获取失败");
				} catch (Exception e) {
					Log.i("TAG", "网络错误");
				}
			}
		});
	}public void initViews(UserHomePageInfo userHomePageInfo) throws DbException {
		//图片需要解析后写入   其他需要解析Json请求后得到结果
		tv_self_location_city.setText(new TCity().getProvince(instance, userHomePageInfo.getProvince()));

		tv_self_location_district.setText(new TCity().getCity(instance, userHomePageInfo.getCityCode()));
		tv_self_user_name.setText(userHomePageInfo.getUserName());
		if (userHomePageInfo.getUserType().equals("7")) {
			tv_self_user_quarters.setText(userHomePageInfo.getJobTitle());
		}else{
			tv_self_user_quarters.setText(userHomePageInfo.getProfessionalTitle());
		}
		tv_self_user_hospital.setText(userHomePageInfo.getCompanyName());
		tv_self_user_department.setText(userHomePageInfo.getDeptName());
		if (userHomePageInfo.getCompanyName().length()+userHomePageInfo.getDeptName().length()>18) {
			RelativeLayout.LayoutParams layoutParams = 
					new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
							LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.BELOW, R.id.tv_self_user_hospital);
			layoutParams.setMargins(0, 3, 0, 0);
//			layoutParams.setMargins(0, 5, 0, 0);
			tv_self_user_department.setLayoutParams(layoutParams);
		}else{
			RelativeLayout.LayoutParams layoutParams = 
					new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
							LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.tv_self_user_hospital);
			layoutParams.setMargins(10, 0, 0, 0);
			tv_self_user_department.setLayoutParams(layoutParams);
		}
		//		BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
		//		if(userHomePageInfo.getBackgroundUrl()==null||
		//				userHomePageInfo.getBackgroundUrl().isEmpty()){
		//			String uri_bg =getString(R.string.bg_1).toString();
		//			bitmapUtils.display(iv_self_user_background, uri_bg);
		//
		//		}else{
		//			bitmapUtils.display(iv_self_user_background, userHomePageInfo.getBackgroundUrl());
		//		}
		try {
			ImageLoaderUtil imageLoaderUtil2bg = new ImageLoaderUtil(
					instance, userHomePageInfo.getBackgroundUrl(), 
					iv_self_user_background, R.drawable.pic_popupmask, R.drawable.pic_popupmask);
			imageLoaderUtil2bg.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		url = userHomePageInfo.getUserFaceUrl();
		try {
			ImageLoaderUtil imageLoaderUtil = 
					new ImageLoaderUtil(instance, url, iv_self_avatar, 
							R.drawable.icon_headfailed, R.drawable.icon_headfailed);
			imageLoaderUtil.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		//设置加v图片显示，以后加上蓝v判断
		if (userHomePageInfo.getUserLevel().equals("11")) {
			iv_vip.setVisibility(View.VISIBLE);
		}
		tv_attention_num.setText(userHomePageInfo.getFocusCount());
		tv_fans_num.setText(userHomePageInfo.getFollowerCount());
		//iv_self_avatar.setImageBitmap(bm);
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_self_avatar:
			//模拟传bitmap去大头像浏览界面
			Intent intent = new Intent(instance,ChangePortraitActivity.class);
			Bundle bundle = new Bundle();
			// 把图片地址的urlList传递过去
			bundle.putString("userfaceUrl", url);
			intent.putExtras(bundle);
			startActivityForResult(intent, CHANGE_PORTRAIT_SUCCESS);
			break;
		case R.id.iv_self_user_background:
			pop= new SelfChangeBgPopup(instance, changebg);
			pop.showAtLocation(mPullRefreshListView, Gravity.RIGHT|Gravity.BOTTOM, 0, 0);

			break;
		case R.id.fl_message:
			Intent intent_message = new Intent(instance, MessageListActivity.class);
			startActivity(intent_message);
			EventBus.getDefault().post(new RemindEvent(11));
			break;
		case R.id.fl_circle:
			Intent intent_circle = new Intent(instance, CircleActivity.class);
			intent_circle.putExtra("UserHomePageInfo", userHomePageInfo);
			startActivity(intent_circle);
			break;
		case R.id.fl_friend:
			Intent intent_friend = new Intent(instance, FriendActivity.class);
			startActivity(intent_friend);
			break;
		case R.id.fl_more:
			Intent intent_more = new Intent(instance, MoreActivity.class);
			startActivityForResult(intent_more, CHANGE_PORTRAIT_SUCCESS);
			break;
		case R.id.tv_self_user_name:
			sendIntroduce();
			break;
		case R.id.tv_self_user_department:
			sendIntroduce();
			break;
		case R.id.tv_self_user_hospital:
			sendIntroduce();
			break;
		case R.id.tv_self_user_quarters:
			sendIntroduce();
			break;
		case R.id.ll_attention_count:
			Intent intent_attention = new Intent(instance, MyAttentionActivity.class);
			intent_attention.putExtra(Constant.USER_SEQ_ID, Constant.userInfo.getUserSeqId());
			instance.startActivity(intent_attention);
			break;
		case R.id.ll_fans_count:
			Intent intent_fans = new Intent(instance, MyFansActivity.class);
			intent_fans.putExtra(Constant.USER_SEQ_ID, Constant.userInfo.getUserSeqId());
			instance.startActivity(intent_fans);
			break;
		case R.id.lly_hv:
			finish();
			break;

		}
		
	}
	private void sendIntroduce() {
		Intent intent = new Intent(instance, IntroduceActivity.class);
		intent.putExtra("self", true);
		intent.putExtra(Constant.USER_SEQ_ID, Constant.userInfo.getUserSeqId());
		startActivity(intent);

	}


	//pop菜单的按钮监听
	private OnClickListener changebg = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_change_bg:
				Intent intent_changebg = new Intent(instance,UserBackgroundSelectActivity.class);
				startActivity(intent_changebg);
				pop.dismiss();
				break;
			}
		}
	};
	
	protected void onNewIntent(Intent intent) {
		getUserHpInfo(Constant.userInfo.getUserSeqId());
		try {
			initViews(userHomePageInfo);
		} catch (DbException e) {
			e.printStackTrace();
		}
		super.onNewIntent(intent);
	};

	@Override
	protected void onResume() {
		if(postListComm != null){		
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
		new Thread(){
			public void run() {
				try {
					Thread.sleep(3000);
					getUserHpInfo(ConstantUser.getUserInfo().getUserSeqId());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
		super.onResume();
	}
}
