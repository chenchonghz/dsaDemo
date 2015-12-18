package com.szrjk.dhome;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.PostList;
import com.szrjk.entity.RemindEvent;
import com.szrjk.entity.TCity;
import com.szrjk.entity.UserHomePageInfo;
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
/**
 *
 * @author 郑斯铭
 * 1）已完成基本控件绑定
 * 2）点击用户头像跳转至新界面显示头像，并放大至屏幕宽度最大值
 * 3）点击背景墙显示popwindow选择操作
 */
public class SelfFragment extends Fragment implements OnClickListener
{	//搜索按钮
	//用户背景墙
	@ViewInject(R.id.iv_self_user_background)
	private ImageView iv_self_user_background;
	//用户所在城市
	@ViewInject(R.id.tv_self_location_city)
	private TextView tv_self_location_city;
	//用户所在城区
	@ViewInject(R.id.tv_self_location_district)
	private TextView tv_self_location_district;
	//用户名
	@ViewInject(R.id.tv_self_user_name)
	private TextView tv_self_user_name;
	//用户职位
	@ViewInject(R.id.tv_self_user_quarters)
	private TextView tv_self_user_quarters;
	//用户所属医院
	@ViewInject(R.id.tv_self_user_hospital)
	private TextView tv_self_user_hospital;
	//用户所属科室
	@ViewInject(R.id.tv_self_user_department)
	private TextView tv_self_user_department;
	//用户头像
	@ViewInject(R.id.iv_self_avatar)
	private ImageView iv_self_avatar;
	//消息跳转按钮
	@ViewInject(R.id.fl_message)
	private FrameLayout fl_message;
	//好友跳转按钮
	@ViewInject(R.id.fl_friend)
	private FrameLayout fl_friend;
	//圈子跳转按钮
	@ViewInject(R.id.fl_circle)
	private FrameLayout fl_circle;
	//更多跳转按钮
	@ViewInject(R.id.fl_more)
	private FrameLayout fl_more;
	//用户的帖子列表
	@ViewInject(R.id.lv_self)
	private ListView lv_self;
	//加V图片
	private ImageView iv_vip; 
	//医院与科室布局
	private RelativeLayout rly_hospital_dept;
	//消息提示红点
	public ImageView iv_remind_message;
	public boolean haveMessage =false;
	//好友提示红点
	public ImageView iv_remind_friend;
	public boolean haveFriend =false;
	//圈子提示红点
	public ImageView iv_remind_circle;
	public boolean haveCircle =false;
	
	private LinearLayout ll_attention_count;
	private LinearLayout ll_fans_count;

	private SelfChangeBgPopup pop;
	//用户信息
	private String url;

	private static final int CHANGE_PORTRAIT_SUCCESS=0;

	@ViewInject(R.id.lv_postlist)
	private PullToRefreshListView mPullRefreshListView;
	private MainActivity mContext;
	private PostListComm postListComm;
	public UserHomePageInfo userHomePageInfo;
	private Dialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.self_fragment, null);
		ViewUtils.inject(this, view);
		dialog = createDialog(getActivity(), "加载中...");
		//		userInfo = Constant.userInfo;
		//		userId=userInfo.getUserSeqId();
		//请求数据
		//添加线程
		mContext = (MainActivity) getActivity();
		Activity activity = getActivity();
		postListComm = new PostListComm(activity, Constant.userInfo.getUserSeqId(),SelfFragment.this, mPullRefreshListView, new IPostListCallback() {
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

		ListView lv_postlist = mPullRefreshListView.getRefreshableView(); // 获得listView对象
		View v = View.inflate(getActivity(), R.layout.self_fragement_header, null);
		lv_postlist.addHeaderView(v);

		iv_self_user_background = (ImageView) v.findViewById(R.id.iv_self_user_background);
		tv_self_location_city= (TextView) v.findViewById(R.id.tv_self_location_city);
		tv_self_location_district= (TextView) v.findViewById(R.id.tv_self_location_district);
		tv_self_user_name= (TextView) v.findViewById(R.id.tv_self_user_name);
		tv_self_user_quarters= (TextView) v.findViewById(R.id.tv_self_user_quarters);
		tv_self_user_hospital= (TextView) v.findViewById(R.id.tv_self_user_hospital);
		tv_self_user_department= (TextView) v.findViewById(R.id.tv_self_user_department);
		iv_self_avatar= (ImageView) v.findViewById(R.id.iv_self_avatar);
		fl_message= (FrameLayout) v.findViewById(R.id.fl_message);
		fl_friend= (FrameLayout) v.findViewById(R.id.fl_friend);
		fl_circle= (FrameLayout) v.findViewById(R.id.fl_circle);
		fl_more= (FrameLayout) v.findViewById(R.id.fl_more);
		iv_vip = (ImageView) v.findViewById(R.id.iv_vip);
		rly_hospital_dept = (RelativeLayout) v.findViewById(R.id.rly_hospital_dept);
		iv_remind_message = (ImageView) v.findViewById(R.id.iv_remind_message);
		iv_remind_circle = (ImageView) v.findViewById(R.id.iv_remind_circle);
		iv_remind_friend = (ImageView) v.findViewById(R.id.iv_remind_friend);
		ll_attention_count = (LinearLayout)v.findViewById(R.id.ll_attention_count);
		ll_fans_count = (LinearLayout)v.findViewById(R.id.ll_fans_count);

		getUserHpInfo(Constant.userInfo.getUserSeqId());
		//设置界面监听器
		initListener();
		mContext.fragment2getSharedPreference();
		//		EventBus.getDefault().register(getActivity());
		return view;
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
					List<PostList> postLists = JSON.parseArray(
							returnObj.getString("ListOut"), PostList.class);

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
				ToastUtils.showMessage(getActivity(), "服务器返回数据失败，请检查网络");
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
					List<PostList> postLists = JSON.parseArray(
							returnObj.getString("ListOut"), PostList.class);
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
				ToastUtils.showMessage(getActivity(), "服务器返回数据失败，请检查网络");
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
				ToastUtils.showMessage(getActivity(), "服务器返回数据失败，请检查网络");
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



	}



	public void initViews(UserHomePageInfo userHomePageInfo) throws DbException {
		//图片需要解析后写入   其他需要解析Json请求后得到结果
		tv_self_location_city.setText(new TCity().getProvince(getActivity(), userHomePageInfo.getProvince()));

		tv_self_location_district.setText(new TCity().getCity(getActivity(), userHomePageInfo.getCityCode()));
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
					getActivity(), userHomePageInfo.getBackgroundUrl(), 
					iv_self_user_background, R.drawable.pic_popupmask, R.drawable.pic_popupmask);
			imageLoaderUtil2bg.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		url = userHomePageInfo.getUserFaceUrl();
		try {
			ImageLoaderUtil imageLoaderUtil = 
					new ImageLoaderUtil(getActivity(), url, iv_self_avatar, 
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
		//iv_self_avatar.setImageBitmap(bm);
	}

	private void initListener() {
		//界面监听
		iv_self_avatar.setOnClickListener(this);
		iv_self_user_background.setOnClickListener(this);
		fl_message.setOnClickListener(this);
		fl_friend.setOnClickListener(this);
		fl_circle.setOnClickListener(this);
		fl_more.setOnClickListener(this);
		tv_self_user_name.setOnClickListener(this);
		tv_self_user_hospital.setOnClickListener(this);
		tv_self_user_quarters.setOnClickListener(this);
		tv_self_user_department.setOnClickListener(this);
		ll_attention_count.setOnClickListener(this);
		ll_fans_count.setOnClickListener(this);
	}




	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_self_avatar:
			//模拟传bitmap去大头像浏览界面
			Intent intent = new Intent(getActivity(),ChangePortraitActivity.class);
			Bundle bundle = new Bundle();
			// 把图片地址的urlList传递过去
			bundle.putString("userfaceUrl", url);
			intent.putExtras(bundle);
			startActivityForResult(intent, CHANGE_PORTRAIT_SUCCESS);
			break;
		case R.id.iv_self_user_background:
			pop= new SelfChangeBgPopup(getActivity(), changebg);
			pop.showAtLocation(getView(), Gravity.RIGHT|Gravity.BOTTOM, 0, 0);

			break;
		case R.id.fl_message:
			Intent intent_message = new Intent(getActivity(), MessageListActivity.class);
			startActivity(intent_message);
			EventBus.getDefault().post(new RemindEvent(11));
			break;
		case R.id.fl_circle:
			Intent intent_circle = new Intent(getActivity(), CircleActivity.class);
			intent_circle.putExtra("UserHomePageInfo", userHomePageInfo);
			startActivity(intent_circle);
			break;
		case R.id.fl_friend:
			Intent intent_friend = new Intent(getActivity(), FriendActivity.class);
			startActivity(intent_friend);
			break;
		case R.id.fl_more:
			Intent intent_more = new Intent(getActivity(), MoreActivity.class);
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
			Intent intent_attention = new Intent(getActivity(), MyAttentionActivity.class);
			intent_attention.putExtra(Constant.USER_SEQ_ID, Constant.userInfo.getUserSeqId());
			getActivity().startActivity(intent_attention);
			break;
		case R.id.ll_fans_count:
			Intent intent_fans = new Intent(getActivity(), MyFansActivity.class);
			intent_fans.putExtra(Constant.USER_SEQ_ID, Constant.userInfo.getUserSeqId());
			getActivity().startActivity(intent_fans);
			break;

		}

	}


	private void sendIntroduce() {
		Intent intent = new Intent(getActivity(), IntroduceActivity.class);
		intent.putExtra(Constant.USER_SEQ_ID, Constant.userInfo.getUserSeqId());
		startActivity(intent);

	}


	//pop菜单的按钮监听
	private OnClickListener changebg = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_change_bg:
				Intent intent_changebg = new Intent(getActivity(),UserBackgroundSelectActivity.class);
				startActivity(intent_changebg);
				pop.dismiss();
				break;
			}
		}
	};
	public  Dialog createDialog(Context context, String msg)
	{
		Dialog dialog = new Dialog(context, R.style.loading_dialog);
		dialog.setContentView(R.layout.loading_layout);
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		TextView textView = (TextView) dialog.findViewById(R.id.tv_msg);
		textView.setText(msg);
		return dialog;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CHANGE_PORTRAIT_SUCCESS:
			if (data==null) {
				break;
			}
			Bundle bundle=data.getExtras();
			if (bundle.getBoolean("CHANGE_PORTRAIT_SUCCESS")) {
				getUserHpInfo(Constant.userInfo.getUserSeqId());
			}
			break;
		}
	}
	//	public void onDestroy() {
	//		EventBus.getDefault().unregister(getActivity());
	//		super.onDestroy();
	//	}
	//	public void onEventMainThread(RemindEvent event){
	//		switch (event.getRemindMessage()) {
	//		//消息
	//		case 1: iv_remind_message.setVisibility(View.VISIBLE);
	//			break;
	//		//好友
	//		case 2:iv_remind_friend.setVisibility(View.VISIBLE);
	//			break;
	//		//圈子
	//		case 3:iv_remind_circle.setVisibility(View.VISIBLE);
	//			break;
	//
	//		}
	//	}
}
