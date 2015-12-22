package com.szrjk.self;

/**
 * 圈子首页
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.szrjk.entity.CircleInfo;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.PostInfo;
import com.szrjk.entity.PostList;
import com.szrjk.entity.PostOtherImformationInfo;
import com.szrjk.entity.UserCard;
import com.szrjk.explore.MyCircleActivity;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.index.SendPostActivity;
import com.szrjk.pull.PullToRefreshBase.Mode;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.util.ShowDialogUtil;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.SelfChangeBgPopup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
@ContentView(R.layout.activity_circle_homepage)
public class CircleHomepageActivity extends BaseActivity implements OnClickListener {

	/**可以只传circleid进来 **/
	public final static String  intent_param_circle_id = "intent_param_circle_id";

	private CircleHomepageActivity instance;
	private ImageView iv_circle_background;
	private ImageView iv_circle_avatar;
	private TextView tv_circle_name;
	private LinearLayout Lly_circle_btns;
	private LinearLayout Lly_other_circle_btns;
	private FrameLayout fl_circlepost;
	private FrameLayout fl_addfriends;
	private FrameLayout fl_profile;
	private FrameLayout fl_dissolvecircle;
	private FrameLayout fl_exitcircle;
	private FrameLayout fl_joincircle;
	private FrameLayout fl_circle_pofile;
	private CircleInfo circleInfo;
	private ImageView iv_back;
	private ImageView iv_circle_state;
	private TextView tv_circle_state;
	//list
	private String userId;
	private Context context;
	private ArrayList<UserCard> userList; // 医生信息
	private ArrayList<PostInfo> postList; // 帖子详情
	@ViewInject(R.id.lv_circle_postlist)
	private PullToRefreshListView mPullRefreshListView;
	private Dialog dialog;
	private static final String LOADING_POST = "正在加载帖子";

	private static final int EDIT_DATA_SUCCESS = 0;
	private ListView lv_postlist;
	private RelativeLayout rl_hint_text;
	private PostListComm postListComm;
	private String objId;
	private String circleId;
	public static int POSITION;
	public static boolean ISDELETE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		lv_postlist = mPullRefreshListView.getRefreshableView(); // 获得listView对象
		//		initData();
		//		String basePostId = "0";
		objId = instance.getIntent().getStringExtra(Constant.USER_SEQ_ID);
		dialog = ShowDialogUtil.createDialog(this, LOADING_POST);
		//请求数据
		//获得intent数据
		getData();
		//绑定控件
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
				doGetMorePosts(userId2,basePostId,isNew,beginNum,endNum);
			}
		});

		View v = View.inflate(instance, R.layout.circle_header, null);
		initLayout(v);
		lv_postlist.addHeaderView(v);
		//设置监听器
		initListener();
	}

	protected void doGetMorePosts(String userId2, String basePostId,
			boolean isNew, String beginNum, String endNum) {
		// TODO Auto-generated method stub
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryCoteriePostList");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		busiParams.put("coterieId",circleId);
		busiParams.put("basePostId", basePostId);
		busiParams.put("isNew", String.valueOf(isNew));
		busiParams.put("beginNum", beginNum);
		busiParams.put("endNum", endNum);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack()
		{
			@Override
			public void start() {dialog.show();}
			@Override
			public void loading(long total, long current, boolean isUploading) {}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject) {
				postListComm.operrefreshComplete();
				dialog.dismiss();
				showToast(CircleHomepageActivity.this, "获取帖子失败，请检查网络", Toast.LENGTH_SHORT);
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
					postListComm.operMorePostsSucc(postLists,Constant.NORMAL_POST);
				}
				else
				{
					postListComm.operNOT_NEW_POST();
					postListComm.operrefreshComplete();
				}
				dialog.dismiss();
			}
		});
	}

	//绑定控件
	private void initLayout(View v) {
		Lly_circle_btns = (LinearLayout) v.findViewById(R.id.Lly_circle_btns);
		Lly_other_circle_btns  = (LinearLayout) v.findViewById(R.id.Lly_other_circle_btns);
		iv_circle_avatar = (ImageView) v.findViewById(R.id.iv_circle_avatar);
		iv_circle_background = (ImageView) v.findViewById(R.id.iv_circle_background);
		tv_circle_name = (TextView) v.findViewById(R.id.tv_circle_name);
		fl_addfriends = (FrameLayout) v.findViewById(R.id.fl_addfriends);
		fl_circlepost = (FrameLayout) v.findViewById(R.id.fl_circlepost);
		fl_profile = (FrameLayout) v.findViewById(R.id.fl_profile);
		fl_dissolvecircle = (FrameLayout) v.findViewById(R.id.fl_dissolvecircle);
		fl_exitcircle = (FrameLayout) v.findViewById(R.id.fl_exitcircle);
		fl_joincircle = (FrameLayout) v.findViewById(R.id.fl_join_circle);
		fl_circle_pofile = (FrameLayout) v.findViewById(R.id.fl_circle_profile);
		iv_back = (ImageView) v.findViewById(R.id.iv_back);
		iv_circle_state = (ImageView) v.findViewById(R.id.iv_circle_state);
		tv_circle_state = (TextView) v.findViewById(R.id.tv_circle_state);
		rl_hint_text = (RelativeLayout)v.findViewById(R.id.rl_hint_text);
		if (circleInfo!=null) {
			setdata();
		}

	}



	//设置数据和显示布局
	private void setdata() {
		try {
			ImageLoaderUtil ilu_bg = new ImageLoaderUtil(instance, getResources().getString(R.string.bg_1)
					, iv_circle_background, R.drawable.pic_popupmask, R.drawable.pic_popupmask);
			ilu_bg.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		try {
			ImageLoaderUtil ilu_avatar = new ImageLoaderUtil(instance, circleInfo.getCoterieFaceUrl(), 
					iv_circle_avatar, R.drawable.icon_headfailed, R.drawable.icon_headfailed);
			ilu_avatar.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		tv_circle_name.setText(circleInfo.getCoterieName());
		if (circleInfo.getIsMember().toString().equals("false")) {
			Lly_circle_btns.setVisibility(View.GONE);
			Lly_other_circle_btns.setVisibility(View.VISIBLE);
		}else{
			if(circleInfo.getMemberType().equals("3")){
				fl_dissolvecircle.setVisibility(View.VISIBLE);
			}else if(circleInfo.getMemberType().equals("1")){
				fl_exitcircle.setVisibility(View.VISIBLE);
			}
		}
		if(circleInfo.getIsOpen().equals("No")&&circleInfo.getIsMember().equals("false")){
			Log.e("CircleHome", "设置提示文字没有权限");
			rl_hint_text.setVisibility(View.VISIBLE);
			mPullRefreshListView.setMode(Mode.DISABLED);

		}else{
			rl_hint_text.setVisibility(View.GONE);
			mPullRefreshListView.setMode(Mode.BOTH);
			Log.e("CircleHome", "设置提示文字有权限");
		}

		if (circleInfo.getIsOpen().equals("Yes")) {
			iv_circle_state.setImageResource(R.drawable.icon_public);
			tv_circle_state.setText("公开");
		}else{
			iv_circle_state.setImageResource(R.drawable.icon_privacy);
			tv_circle_state.setText("私密");

		}

	}


	private void initListener() {
		fl_circlepost.setOnClickListener(instance);
		fl_addfriends.setOnClickListener(instance);
		fl_profile.setOnClickListener(instance);
		fl_dissolvecircle.setOnClickListener(instance);
		fl_exitcircle.setOnClickListener(instance);
		fl_joincircle.setOnClickListener(instance);
		fl_circle_pofile.setOnClickListener(instance);
		iv_back.setOnClickListener(instance);
	}
	private  int FLAG = 0;
	private void getData() {
		Intent intent = getIntent();
		circleInfo = (CircleInfo) intent.getSerializableExtra(Constant.CIRCLE);
		circleId =  intent.getStringExtra(intent_param_circle_id);
		if (circleInfo==null) {
			getCoterieById();
		}else{
			circleId = circleInfo.getCoterieId();
		}
	}

	private void getCoterieById() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryCoterieById");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		busiParams.put("coterieId",circleId);
		busiParams.put("memberLimitCount", "0");
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
					circleInfo = JSON.parseObject(returnObj.getString("ListOut"), CircleInfo.class);
					setdata();

				}
//				else if(errorObj.getReturnCode().equals("0006")&& errorObj.getErrorMessage().equals("圈子["+circleId+"]不存在！")){
//					ToastUtils.showMessage(instance, "错误圈子");
//					instance.finish();
//				}
				if (dialog.isShowing())
				{
					dialog.dismiss();
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
				JSONObject errorInfo = jobj;
				String returnCode = (String) errorInfo.get("ReturnCode");
				String returnMessage = (String)errorInfo.get("ErrorMessage");
				if(returnCode.equals("0006")&&returnMessage.equals("圈子["+circleId+"]不存在！")){
					ToastUtils.showMessage(instance, "抱歉，圈子已解散");
					finish();
				}
				Log.e("CircleHomePage", "错误码："+returnCode);
					
				
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
		paramMap.put("ServiceName", "queryCoteriePostList");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		busiParams.put("coterieId",circleId);
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
				showToast(CircleHomepageActivity.this, "获取帖子失败，请检查网络", Toast.LENGTH_SHORT);
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
					postListComm.operNewPostsSucc(postLists,Constant.NORMAL_POST);
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
		paramMap.put("ServiceName", "queryCoteriePostList");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		busiParams.put("coterieId",circleId);
		busiParams.put("basePostId", basePostId);
		busiParams.put("isNew", String.valueOf(isNew));
		busiParams.put("beginNum", beginNum);
		busiParams.put("endNum", endNum);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {

			@Override
			public void success(JSONObject jsonObject) {
				// TODO Auto-generated method stub
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
					postListComm.operPostsSucc(postLists,Constant.NORMAL_POST);
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
				// TODO Auto-generated method stub

			}

			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				// TODO Auto-generated method stub
				if (dialog.isShowing())
				{
					dialog.dismiss();
				}
				postListComm.operrefreshComplete();
				showToast(CircleHomepageActivity.this, "获取帖子失败，请检查网络", Toast.LENGTH_SHORT);
			}
		});
	}

	private SelfChangeBgPopup pop_dissolvecircle;
	private SelfChangeBgPopup pop_exit;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fl_circlepost:
			Intent intent_post = new Intent(instance, SendPostActivity.class);
			intent_post.putExtra(Constant.CIRCLE, circleInfo.getCoterieId());
			startActivity(intent_post);
			break;
		case R.id.fl_addfriends:
			Intent intent_addfriends = new Intent(instance, CircleInviteFirendActivity.class);
			intent_addfriends.putExtra(Constant.CIRCLE,circleInfo.getCoterieId());
			startActivity(intent_addfriends);
			break;
		case R.id.fl_profile:
			Intent intent_profile = new Intent(instance, CircleIntroductionActivity.class);
			intent_profile.putExtra(Constant.CIRCLE,circleInfo.getCoterieId());
			startActivityForResult(intent_profile, EDIT_DATA_SUCCESS);
			break;
		case R.id.fl_dissolvecircle:
			pop_dissolvecircle = new SelfChangeBgPopup(instance, dissolvecircle);
			pop_dissolvecircle.setText("解散圈子");
			pop_dissolvecircle.showAtLocation(mPullRefreshListView, Gravity.RIGHT|Gravity.BOTTOM, 0, 0);
			break;
		case R.id.fl_exitcircle:
			pop_exit = new SelfChangeBgPopup(instance, exit);
			pop_exit.setText("退出圈子");
			pop_exit.showAtLocation(mPullRefreshListView, Gravity.RIGHT|Gravity.BOTTOM, 0, 0);
			break;
		case R.id.fl_circle_profile:
			Intent intent_profile2 = new Intent(instance, CircleIntroductionActivity.class);
			intent_profile2.putExtra(Constant.CIRCLE,circleInfo.getCoterieId());
			startActivity(intent_profile2);
			break;
		case R.id.fl_join_circle:
			JoinCircle();
			break;
		case R.id.iv_back:
			finish();
			break;
		}

	}

	private void JoinCircle() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "userRequestIntoCoterie");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId",Constant.userInfo.getUserSeqId());
		busiParams.put("coterieId",circleId);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					Toast.makeText(instance, "已发送申请", Toast.LENGTH_SHORT).show();
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

			}
		});

	}
	private OnClickListener dissolvecircle = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_change_bg:
				pop_dissolvecircle.dismiss();
				dissolvecircle();
				break;
			}
		}
	};
	private OnClickListener exit = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_change_bg:
				pop_exit.dismiss();
				exitcircle();
				break;
			}
		}
	};
	//解散圈子
	protected void dissolvecircle() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "maintainCoterie");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("operateType", "D");
		Map<String, Object> coterieInfo = new HashMap<String, Object>();
		coterieInfo.put("userSeqId", Constant.userInfo.getUserSeqId());
		coterieInfo.put("coterieId", circleInfo.getCoterieId());
		coterieInfo.put("coterieName", circleInfo.getCoterieName());
		coterieInfo.put("coterieDesc", circleInfo.getCoterieDesc());
		coterieInfo.put("coterieFaceUrl", circleInfo.getCoterieFaceUrl());
		coterieInfo.put("isOpen", circleInfo.getIsOpen());
		busiParams.put("coterieInfo", coterieInfo);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					Toast.makeText(instance, "解散成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(instance, MyCircleActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
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

			}
		});
	}

	protected void exitcircle() {
		List<String> list = new ArrayList<String>();
		list.add(Constant.userInfo.getUserSeqId());
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "removeUserFromCoterie");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());		
		busiParams.put("objUserSeqIds", list);
		busiParams.put("coterieId", circleInfo.getCoterieId());
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					Toast.makeText(instance, "退出成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(instance, MyCircleActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
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

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(Constant.FORWARD_RESULTCODE == resultCode){
			int position = data.getIntExtra(Constant.POSITION, 0);
			int forward_num = data.getIntExtra(Constant.FORWARD_NUM, 0);
			Log.e("IndexFragment", "位置："+position+"转发数量："+forward_num);
			postListComm.getPostOtherList().get(position).setFORWARD_NUM(forward_num);
			postListComm.getAdapter().notifyDataSetChanged();
		}
		//		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==EDIT_DATA_SUCCESS) {
			getCoterieById();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
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
		super.onResume();
	}

}
