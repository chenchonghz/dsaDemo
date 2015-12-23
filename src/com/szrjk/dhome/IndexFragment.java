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
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.IndexListViewAdapter;
import com.szrjk.config.Constant;
import com.szrjk.entity.*;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.pull.PullToRefreshBase;
import com.szrjk.pull.PullToRefreshBase.OnRefreshListener;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.util.ShowDialogUtil;
import com.szrjk.util.ToastUtils;

public class IndexFragment extends Fragment 
{
	private String userId; // userId
	public String mMaxPostId; // 最大postId
	private String mMinPostId; // 最小postId
	private Context context;
	private ArrayList<UserCard> userList; // 帖子用户信息
	private ArrayList<PostInfo> postList; // 帖子详情
	private ArrayList<PostOtherImformationInfo> postOtherList; // 转发、评论、赞信息
	private ArrayList<UserCard> recommendUserList;
	private UserInfo userInfo;
	private String lastUserId;
	@ViewInject(R.id.lv_postlist)
	public PullToRefreshListView mPullRefreshListView;
	private ListView lv_postlist;
	private static final String HAVE_NOT_MORE_POST = "没有更多帖子了";
	private static final String LOADING_POST = "正在加载帖子";
	private boolean firstIn = true;  //标记是否下拉刷新
	private int index = 1; //产生推荐关注计数index
	String baseObjUserId = "0"; //推荐关注接口默认传入userId参数
	private Dialog dialog = null;
	private IndexListViewAdapter adapter;
	private MainActivity mainActivity;
	public static int POSITION = -1;
	public static int FORWARD_NUM = -1;
	public static int COMMEND_NUM = -1;
	public static int LIKE_NUM = -1;
	public static boolean ISLIKE = false;
	public static boolean ISSRCPOST = false;
	public static boolean ISDELETE = false;
	public static String SRC_POSEID = "-1";
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case Constant.HAVE_NEW_POST:
					if(firstIn){
						adapter = new IndexListViewAdapter(
								context, mainActivity,IndexFragment.this,userList, postList, postOtherList,userId, Constant.INDEX_FLAG,new IPullPostListCallback() {
							@Override
							public void skipToSelfFragment() {
								Intent intent = new Intent(context, SelfActivity.class);
								context.startActivity(intent);
							}
						});
						lv_postlist.setAdapter(adapter);
						firstIn = false;
					}
					adapter.notifyDataSetChanged();
					break;
				case Constant.HAVE_NEW_POST_BY_REFRESH:
					adapter = new IndexListViewAdapter(
							context, mainActivity,userList, postList, postOtherList,userId,Constant.INDEX_FLAG, new IPullPostListCallback() {
						@Override
						public void skipToSelfFragment() {
							Intent intent = new Intent(context, SelfActivity.class);
							context.startActivity(intent);
						}
					});
					lv_postlist.setAdapter(adapter);
					break;
				case Constant.NOT_NEW_POST:
					ToastUtils.showMessage(context, HAVE_NOT_MORE_POST);
					break;
			}

		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.index_fragment, null);
		ViewUtils.inject(this, view);
		initData();
		lv_postlist = mPullRefreshListView.getRefreshableView(); // 获得listView对象
		String basePostId = "0";
		userInfo = Constant.userInfo;
		userId = userInfo.getUserSeqId();
		boolean isNew = true;
//		getRecommendDoctor(userId,baseObjUserId,isNew,Constant.USER_BEGIN_NUM,Constant.USER_END_NUM);
		getPosts(userId, basePostId, isNew, Constant.POST_BEGIN_NUM,
				Constant.POST_END_NUM);
		setListner();
		return view;
	}

	/**
	 * 获取推荐用户
	 * @param userId
	 * @param baseObjUserId
	 * @param isNew
	 * @param userBeginNum
	 * @param userEndNum
	 */
	private void getRecommendDoctor(final String userId, String baseObjUserId,
			boolean isNew, String userBeginNum, String userEndNum) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryRecommendUsers");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", userId);
		busiParams.put("baseObjUserId", baseObjUserId);
		busiParams.put("isNew", String.valueOf(isNew));
		busiParams.put("beginNum", userBeginNum);
		busiParams.put("endNum", userEndNum);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			
			@Override
			public void success(JSONObject jsonObject) {
				recommendUserList = new ArrayList<UserCard>();
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					List<RecommendUserList> userLists = JSON.parseArray(
							returnObj.getString("ListOut"), RecommendUserList.class);
					if(userLists != null && !userLists.isEmpty()){
						for (RecommendUserList userList : userLists) {
							UserCard recommendUser = userList.getUserCard();
							recommendUserList.add(recommendUser);
						}
						lastUserId = recommendUserList.get(recommendUserList.size()-1).getUserSeqId();
						Log.e("IndexFragment", recommendUserList.toString());
						String basePostId = "0";
						boolean isNew = true;
						if(firstIn){
							getPosts(userId, basePostId, isNew, Constant.POST_BEGIN_NUM,
									Constant.POST_END_NUM);
						}else{
							isNew = false;
							basePostId = mMinPostId;
							if(!basePostId.isEmpty()){
								getMorePosts(userId, basePostId, isNew, Constant.POST_BEGIN_NUM, Constant.POST_END_NUM);
							}
						}	
					}else{
						lastUserId = "0";
						String basePostId = "0";
						boolean isNew = true;
						if(firstIn){
							getPosts(userId, basePostId, isNew, Constant.POST_BEGIN_NUM,
									Constant.POST_END_NUM);
						}else{
							isNew = false;
							basePostId = mMinPostId;
							if(!basePostId.isEmpty()){
							getMorePosts(userId, basePostId, isNew, Constant.POST_BEGIN_NUM, Constant.POST_END_NUM);
							}
						}
					}
				}else{
					lastUserId = "0";
					String basePostId = "0";
					boolean isNew = true;
					if(firstIn){
						getPosts(userId, basePostId, isNew, Constant.POST_BEGIN_NUM,
								Constant.POST_END_NUM);
					}else{
						isNew = false;
						basePostId = mMinPostId;
						if(!basePostId.isEmpty()){
						getMorePosts(userId, basePostId, isNew, Constant.POST_BEGIN_NUM, Constant.POST_END_NUM);
						}
					}
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
				String basePostId = "0";
				boolean isNew = true;
				if(firstIn){
					getPosts(userId, basePostId, isNew, Constant.POST_BEGIN_NUM,
							Constant.POST_END_NUM);
				}else{
					isNew = false;
					basePostId = mMinPostId;
					if(!basePostId.isEmpty()){
						getMorePosts(userId, basePostId, isNew, Constant.POST_BEGIN_NUM, Constant.POST_END_NUM);
					}
				}
			}
		});
	}

//	/**
//	 * 获取最小的postId值
//	 * 
//	 * @return
//	 */
//	protected String getMinPostId()
//	{
//		BigDecimal minPostId = null;
//		int index = 0;
//		int n = 0;
//		if (postList != null && !postList.isEmpty())
//		{
//			while(postList.get(index).getPostId() == null){
//				index++;
//				n++;
//				if(n >= postList.size()){
//					return "0";
//				}
//			}
//			minPostId = new BigDecimal(postList.get(index).getPostId());
//			if(postList.size()<=2){
//				return minPostId.toString();
//			}
//			for (int i = index+1; i < postList.size(); i++)
//			{
//				if(postList.get(i).getPostId()==null){
//					i++;
//				}
//				if(i>=postList.size() || postList.get(i).getPostId() == null){
//					return minPostId.toString();
//				}else{
//				BigDecimal bd = new BigDecimal(postList.get(i).getPostId());
//				minPostId = minPostId.compareTo(bd) == -1 ? minPostId : bd;
//				}
//			}
//			System.out.println("最小：" + minPostId);
//			return minPostId.toString();
//		}else{
//			return "0";
//		}
//		
//	}
//
//	/**
//	 * 获取最大的postId
//	 * 
//	 * @return
//	 */
//	protected String getMaxPostId()
//	{
//		BigDecimal maxPostId = null;
//		int index = 0;
//		int n = 0;
//		if (postList != null && !postList.isEmpty())
//		{
//			while(postList.get(index).getPostId() == null){
//				index++;
//				n++;
//				if(n >=postList.size()){
//					return "0";
//				}
//			}
//				maxPostId = new BigDecimal(postList.get(index).getPostId());
//				if(postList.size()<=2){
//					return maxPostId.toString();
//				}
//				for (int i = index+1; i < postList.size(); i++)
//				{
//					if(postList.get(i).getPostId()==null){
//						i++;
//					}
//					if(i >= postList.size() || postList.get(i).getPostId() == null){
//						return maxPostId.toString();
//					}else{
//					BigDecimal bd = new BigDecimal(postList.get(i).getPostId());
//					maxPostId = maxPostId.compareTo(bd) == 1 ? maxPostId : bd;
//					}
//				}
//				System.out.println("最大：" + maxPostId);
//				return maxPostId.toString();
//		}else{
//			return "0";
//		}
//	}

	/**
	 * 设置各种监听
	 */
	private void setListner()
	{
		// 设置listView上下拉监听
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>()
				{

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView)
					{
						if (mPullRefreshListView.isHeaderShown())
						{
							boolean isNew = true;
							String basePostId = mMaxPostId;
							getNewPosts(userId, basePostId, isNew,
									Constant.POST_BEGIN_NUM, Constant.POST_END_NUM);

						}
						else if (mPullRefreshListView.isFooterShown())
						{
//							Log.e("IndexFragment", "标记："+index);
//							if(index%4 == 0){
//								getRecommendDoctor(userId, lastUserId, false, Constant.USER_BEGIN_NUM, Constant.USER_END_NUM);
//							}else{
								boolean isNew = false;
								String basePostId = mMinPostId;
                                    recommendUserList = null;
									getMorePosts(userId, basePostId, isNew,
											Constant.POST_BEGIN_NUM, Constant.POST_END_NUM);
//							}
						}

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
	public void getNewPosts(String userId2, String basePostId,
			boolean isNew, String beginNum, String endNum) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPostListInMainPage");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", userId);
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
				if (dialog.isShowing())
				{
					dialog.dismiss();
				}
				if (mPullRefreshListView.isRefreshing())
				{
					mPullRefreshListView.onRefreshComplete();
				}
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					List<PostList> postLists = JSON.parseArray(
							returnObj.getString("ListOut"), PostList.class);
					Collections.reverse(postLists);
					if(!postLists.isEmpty()&& postLists != null){
						for (PostList list : postLists)
						{
							UserCard userCardInfo = list.getUserCard();
							userList.add(0, userCardInfo);
							PostInfo postInfo = list.getAbstractInfo();
							postList.add(0, postInfo);
							boolean isMineLike = list.isMineLike();
							PostOtherImformationInfo postOtherInfo = list
									.getStatisInfo();
							if(postOtherInfo!=null){
								postOtherInfo.setMineLike(isMineLike);
							}
							postOtherList.add(0, postOtherInfo);
						}
						if(postList.get(0).getPostId()!=null){		
							mMaxPostId = postList.get(0).getPostId();
						}else{
							mMaxPostId = "0";
						}
						System.out.println(userList.toString());
						System.out.println(postList.toString());
						System.out.println(postOtherList.toString());
						handler.sendEmptyMessage(Constant.HAVE_NEW_POST_BY_REFRESH);
						
					}else{
						handler.sendEmptyMessage(Constant.NOT_NEW_POST);
					}	
				}
				else
				{
					handler.sendEmptyMessage(Constant.NOT_NEW_POST);
				}
			}

			@Override
			public void start() {				
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {
				
			}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject) {
				if (mPullRefreshListView.isRefreshing())
				{
					mPullRefreshListView.onRefreshComplete();
					ToastUtils.showMessage(context, "服务器返回数据失败，请检查网络");
				}
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData()
	{
		context = getActivity();
		mainActivity = (MainActivity) getActivity();
		mPullRefreshListView
		.setMode(com.szrjk.pull.PullToRefreshBase.Mode.BOTH);
		mPullRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_down_lable_text));
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(
				getResources().getString(R.string.pull_up_lable_text));
		mPullRefreshListView.getLoadingLayoutProxy(true, true)
		.setRefreshingLabel(
				getResources()
				.getString(R.string.refreshing_lable_text));
		mPullRefreshListView.getLoadingLayoutProxy(true, true)
		.setReleaseLabel(
				getResources().getString(R.string.release_lable_text));
		userList = new ArrayList<UserCard>();
		postList = new ArrayList<PostInfo>();
		postOtherList = new ArrayList<PostOtherImformationInfo>();
		dialog = ShowDialogUtil.createDialog(context, LOADING_POST);
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
	public void getPosts(String userId, String basePostId, boolean isNew,
			String beginNum, String endNum)
	{
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPostListInMainPage");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", userId);
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
				if (dialog.isShowing())
				{
					dialog.dismiss();
				}
				if (mPullRefreshListView.isRefreshing())
				{
					mPullRefreshListView.onRefreshComplete();
				}
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					List<PostList> postLists = JSON.parseArray(
							returnObj.getString("ListOut"), PostList.class);
//					if(recommendUserList != null && !recommendUserList.isEmpty()){
//						PostInfo postRecommendUser = new PostInfo();
//						UserCard user = new UserCard();
//						PostOtherImformationInfo other = new PostOtherImformationInfo();
//						postRecommendUser.setPostType(Constant.RECOMMEND_USER);
//						postRecommendUser.setRecommendUserList(recommendUserList);
//						Log.e("IndexFragment", postRecommendUser.toString());
//						userList.add(user);
//						postOtherList.add(other);
//						postList.add(postRecommendUser);
//					}
					
					if(postLists != null && !postLists.isEmpty()){
						for (PostList list : postLists)
						{
							UserCard userCardInfo = list.getUserCard();
							userList.add(userCardInfo);
							PostInfo postInfo = list.getAbstractInfo();
							postList.add(postInfo);
							boolean isMineLike = list.isMineLike();
							PostOtherImformationInfo postOtherInfo = list
									.getStatisInfo();
							if(postOtherInfo!=null){
								postOtherInfo.setMineLike(isMineLike);
							}
							postOtherList.add(postOtherInfo);
						}
						if(postList.get(0).getPostId()!= null){
							mMaxPostId = postList.get(0).getPostId();
						}else{
							mMaxPostId = "0";
						}
						if(postList.get(postList.size()-1).getPostId()!=null){
							mMinPostId = postList.get(postList.size()-1).getPostId();
						}else{
							mMinPostId = "0";
						}
						Log.e("IndexFragment", "最大postId"+mMaxPostId+"最小postId"+mMinPostId);
						System.out.println(userList.toString());
						System.out.println(postList.toString());
						System.out.println(postOtherList.toString());
						handler.sendEmptyMessage(Constant.HAVE_NEW_POST);
						
					}else{
						Log.e("IndexFragment", "postLists为空");
						mMaxPostId = "0";
						mMinPostId = "0";
						handler.sendEmptyMessage(Constant.NOT_NEW_POST);
					}	
				}
				else
				{
					mMaxPostId = "0";
					mMinPostId = "0";
					handler.sendEmptyMessage(Constant.NOT_NEW_POST);
				}

			}

			@Override
			public void start()
			{
				if (!mPullRefreshListView.isRefreshing())
				{
					dialog.show();
				}
			}

			@Override
			public void loading(long total, long current, boolean isUploading)
			{

			}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject)
			{
				if (dialog!=null&&dialog.isShowing())
				{
					dialog.dismiss();
					ToastUtils.showMessage(context, "服务器返回数据失败，请检查网络");
				}
				if (mPullRefreshListView.isRefreshing())
				{
					mPullRefreshListView.onRefreshComplete();
					ToastUtils.showMessage(context, "服务器返回数据失败，请检查网络");
				}
			}
		});
	}
	/**
	 * 获取更多帖子
	 * @param userId
	 * @param basePostId
	 * @param isNew
	 * @param beginNum
	 * @param endNum
	 */
	public void getMorePosts(String userId, String basePostId, boolean isNew,
			String beginNum, String endNum)
	{
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPostListInMainPage");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", userId);
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
				if (dialog.isShowing())
				{
					dialog.dismiss();
				}
				if (mPullRefreshListView.isRefreshing())
				{
					mPullRefreshListView.onRefreshComplete();
				}
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					List<PostList> postLists = JSON.parseArray(
							returnObj.getString("ListOut"), PostList.class);
//					if(recommendUserList != null&&!recommendUserList.isEmpty()){
//						PostInfo postRecommendUser = new PostInfo();
//						UserCard user = new UserCard();
//						PostOtherImformationInfo other = new PostOtherImformationInfo();
//						postRecommendUser.setPostType(Constant.RECOMMEND_USER);
//						postRecommendUser.setRecommendUserList(recommendUserList);
//						Log.e("IndexFragment", postRecommendUser.toString());
//						userList.add(user);
//						postOtherList.add(other);
//						postList.add(postRecommendUser);
//					}
					if(!postLists.isEmpty()&& postLists != null){
						index++;
						for (PostList list : postLists)
						{
							UserCard userCardInfo = list.getUserCard();
							userList.add(userCardInfo);
							PostInfo postInfo = list.getAbstractInfo();
							postList.add(postInfo);
							boolean isMineLike = list.isMineLike();
							PostOtherImformationInfo postOtherInfo = list
									.getStatisInfo();
							if(postOtherInfo!=null){
								postOtherInfo.setMineLike(isMineLike);
							}
							postOtherList.add(postOtherInfo);
						}
						Log.e("indexFragment", "下拉刷新后帖子列表："+postList.toString());
						if(postList.get(postList.size()-1).getPostId()!=null){	
							mMinPostId = postList.get(postList.size()-1).getPostId();
						}else{
							mMinPostId = "0";
						}
						Log.e("IndexFragment", "上拉加载最小postId"+mMinPostId);
						handler.sendEmptyMessage(Constant.HAVE_NEW_POST);
					}else{
						if(index%4==0){
							index++;
						}
						handler.sendEmptyMessage(Constant.NOT_NEW_POST);
					}	
				}
				else
				{
					handler.sendEmptyMessage(Constant.NOT_NEW_POST);
					if (mPullRefreshListView.isRefreshing())
					{
						mPullRefreshListView.onRefreshComplete();
					}
				}

			}

			@Override
			public void start()
			{
				if (!mPullRefreshListView.isRefreshing())
				{
					dialog.show();
				}
			}

			@Override
			public void loading(long total, long current, boolean isUploading)
			{

			}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject)
			{
				if (dialog!=null&&dialog.isShowing())
				{
					dialog.dismiss();
					ToastUtils.showMessage(context, "服务器返回数据失败，请检查网络");
				}
				if (mPullRefreshListView.isRefreshing())
				{
					mPullRefreshListView.onRefreshComplete();
					ToastUtils.showMessage(context, "服务器返回数据失败，请检查网络");
				}
			}
		});
	}
	

//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		Log.e("IndexFragment", "resultCode="+resultCode);
//		if(Constant.FORWARD_RESULTCODE == resultCode){
//			int position = data.getIntExtra(Constant.POSITION, 0);
//			int forward_num = data.getIntExtra(Constant.FORWARD_NUM, 0);
//			Log.e("IndexFragment", "首页转发后："+"位置："+position+"转发数量："+forward_num);
//			postOtherList.get(position).setFORWARD_NUM(forward_num);
//			adapter.notifyDataSetChanged();
//		}
////		}else if(Constant.NOTIFY_DATA_SET_CHANGE == resultCode){
////			Log.e("IndexFragment", "返回了数据");
////			Bundle bundle = data.getExtras();
////			int position = bundle.getInt("position");
////			String forward_num = bundle.getString("transmitCount");
////			String comment_num = bundle.getString("commentCoumt");
////			String like_num = bundle.getString("laudCount");
////			boolean isLike = bundle.getBoolean("isLike");
////			android.util.Log.e("data", position
////					+ ","
////					+ forward_num
////					+ ","
////					+ comment_num + ","
////					+ like_num
////					+ "," + isLike);
////			postOtherList.get(position).setFORWARD_NUM(Integer.valueOf(forward_num));
////			postOtherList.get(position).setCOMMENT_NUM(Integer.valueOf(comment_num));
////			postOtherList.get(position).setLIKE_NUM(Integer.valueOf(like_num));
////			postOtherList.get(position).setMineLike(isLike);
////			adapter.notifyDataSetChanged();
////		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(adapter != null){
			if(POSITION != -1){
				if(!ISSRCPOST){
					if(FORWARD_NUM != -1){
						postOtherList.get(POSITION).setFORWARD_NUM(Integer.valueOf(FORWARD_NUM));
					}
					if(COMMEND_NUM != -1){
						postOtherList.get(POSITION).setCOMMENT_NUM(Integer.valueOf(COMMEND_NUM));
					}
				    if(LIKE_NUM != -1){
				    	postOtherList.get(POSITION).setLIKE_NUM(Integer.valueOf(LIKE_NUM));
				    }
				    postOtherList.get(POSITION).setMineLike(ISLIKE);
				    if(ISDELETE){
				    	userList.remove(POSITION);
				    	postList.remove(POSITION);
				    	postOtherList.remove(POSITION);
				    }
				    adapter.notifyDataSetChanged();
				    POSITION = -1;
				    FORWARD_NUM = -1;
				    COMMEND_NUM = -1;
				    LIKE_NUM = -1;
				    ISLIKE = false;
				    ISDELETE = false;
				}
			}
			ISSRCPOST = false;
		}
		
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
//		userList = null;
//		postList = null;
//		postOtherList = null;
//		dialog = null;
		index = 0;
		
	}
}
