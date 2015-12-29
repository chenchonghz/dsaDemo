package com.szrjk.explore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.MyCommentListAdapter;
import com.szrjk.config.Constant;
import com.szrjk.config.ConstantUser;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.CommentInfo;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.MyPostComments;
import com.szrjk.entity.PostInfo;
import com.szrjk.entity.UserCard;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.pull.PullToRefreshBase;
import com.szrjk.pull.PullToRefreshBase.Mode;
import com.szrjk.pull.PullToRefreshBase.OnRefreshListener;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.util.ToastUtils;

/**
 * denggm on 2015/12/25. DHome
 */
@ContentView(R.layout.activity_my_comment_list)
public class MyCommentListActivity extends BaseActivity {

	@ViewInject(R.id.ptrl_my_comment)
	private PullToRefreshListView ptrl_my_comment;

	private MyCommentListActivity instance;

	private ListView lv_myPostComments;

	private ArrayList<MyPostComments> myPostCommentsList = new ArrayList<MyPostComments>();

	private String basePostId;

	private String maxBasePostId;

	private String minBasePostId;

	private String isNew = "true";

	protected int y;

	protected boolean isFirst = true;

	private MyCommentListAdapter myCommentListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		initLayout();
		setAdapter(myPostCommentsList);
		initListener();
	}

	private void initLayout() {
		ptrl_my_comment.setMode(Mode.BOTH);
		ptrl_my_comment.getLoadingLayoutProxy(true, false).setPullLabel(
				getResources().getString(R.string.pull_down_lable_text));
		ptrl_my_comment.getLoadingLayoutProxy(false, true).setPullLabel(
				getResources().getString(R.string.pull_up_lable_text));
		ptrl_my_comment.getLoadingLayoutProxy(true, true).setRefreshingLabel(
				getResources().getString(R.string.refreshing_lable_text));
		ptrl_my_comment.getLoadingLayoutProxy(true, true).setReleaseLabel(
				getResources().getString(R.string.release_lable_text));
		ptrl_my_comment.getLoadingLayoutProxy(true, true).setLoadingDrawable(
				null);
		lv_myPostComments = ptrl_my_comment.getRefreshableView();
		basePostId = "0";
		getMyPostComments();
	}

	private void getMyPostComments() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryMyPostComments");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("basePostId", basePostId);
		busiParams.put("isNew", isNew);
		busiParams.put("userSeqId", ConstantUser.getUserInfo().getUserSeqId());
		busiParams.put("beginNum", "0");
		busiParams.put("endNum", "5");
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				try {
					dialog.dismiss();
					ErrorInfo errorObj = JSON.parseObject(
							jsonObject.getString("ErrorInfo"), ErrorInfo.class);
					if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
						JSONObject returnObj = jsonObject
								.getJSONObject("ReturnInfo");
						if (returnObj.getString("ListOut") != null
								&& !returnObj.getString("ListOut").equals("")) {
							JSONArray listOut = returnObj
									.getJSONArray("ListOut");

							JSONObject commentInfo = (JSONObject) listOut.get(0);
							ArrayList<MyPostComments> myCommentsList = new ArrayList<MyPostComments>();

							Object commentInfoObject = commentInfo.get("commentInfo");
							if(commentInfoObject instanceof String&&commentInfoObject.equals("")){
								//本来，这货应该是数组的，但是接口有时会返回“”，所以要这样判断一下
								if (ptrl_my_comment.isRefreshing()) {
									ptrl_my_comment.onRefreshComplete();
								}
								return;
							}

							JSONArray list = commentInfo.getJSONArray("commentInfo");
							if (commentInfo != null && !commentInfo.isEmpty()) {
								/*
									由于有些数据可能为空，这里对返回的数据一个字段一个字段地处理
								 */
								for (int i = 0; i < list.size(); i++) {
									JSONObject object = list.getJSONObject(i);
									MyPostComments myPostComments = new MyPostComments();
									if (object.getString("userCard") != null&& !object.getString("userCard").isEmpty()) {
										UserCard userCard = JSON.parseObject(object.getString("userCard"),UserCard.class);
										myPostComments.setUserCard(userCard);
									}
									if (object.getString("abstractInfo") != null
											&& !object
													.getString("abstractInfo")
													.isEmpty()) {
										PostInfo abstractInfo = JSON.parseObject(
												object.getString("abstractInfo"),
												PostInfo.class);
										myPostComments
												.setAbstractInfo(abstractInfo);
									}
									if (object
											.getString("userCard_SecondLayer") != null
											&& !object.getString(
													"userCard_SecondLayer")
													.isEmpty()) {
										UserCard userCard_SecondLayer = JSON.parseObject(
												object.getString("userCard_SecondLayer"),
												UserCard.class);
										myPostComments
												.setUserCard_SecondLayer(userCard_SecondLayer);
									}
									if (object.getString("userCard_FirstLayer") != null
											&& !object.getString(
													"userCard_FirstLayer")
													.isEmpty()) {
										UserCard userCard_FirstLayer = JSON.parseObject(
												object.getString("userCard_FirstLayer"),
												UserCard.class);
										myPostComments
												.setUserCard_FirstLayer(userCard_FirstLayer);
									}
									if (object.getString("commentInfo_SecondLayer") != null&& !object.getString("commentInfo_SecondLayer").isEmpty()) {
										CommentInfo commentInfo_SecondLayer = JSON.parseObject(
                                                object.getString("commentInfo_SecondLayer"),
                                                CommentInfo.class);

										String tmppusercard = object.getJSONObject("commentInfo_SecondLayer").getString("pUserCard");
                                        UserCard tmpuserCard = null;
                                        //判断 First的 pusercard是否存在
                                        if (tmppusercard == null|| tmppusercard.equals("")){
                                            //什么都不做
                                        }else{
                                            tmpuserCard = JSON.parseObject(tmppusercard,UserCard.class);
                                        }
										commentInfo_SecondLayer.setpUserCard(tmpuserCard);
                                        myPostComments.setCommentInfo_SecondLayer(commentInfo_SecondLayer);
									}
									if (object.getString("commentInfo_FirstLayer") != null&& !object.getString("commentInfo_FirstLayer").isEmpty()) {
										CommentInfo commentInfo_FirstLayer = JSON.parseObject(object.getString("commentInfo_FirstLayer"),CommentInfo.class);
										String tmppusercardstr = object.getJSONObject("commentInfo_FirstLayer")
												.getString("pUserCard");
                                        UserCard tmpuserCard = null;
                                        //判断 First的 pusercard是否存在
                                        if (tmppusercardstr == null|| tmppusercardstr.equals("")){
                                            //什么都不做
                                        }else{
                                            tmpuserCard = JSON.parseObject(tmppusercardstr,UserCard.class);
                                        }

										commentInfo_FirstLayer
												.setpUserCard(tmpuserCard);
										myPostComments
												.setCommentInfo_FirstLayer(commentInfo_FirstLayer);
									}
									myCommentsList.add(myPostComments);
								}
							}
							if (myCommentsList != null
									&& !myCommentsList.isEmpty()) {
								if (isFirst) {
									myPostCommentsList.addAll(myCommentsList);
									isFirst = false;
								}
								if (ptrl_my_comment.isFooterShown()) {
									myPostCommentsList.addAll(
											myPostCommentsList.size(),
											myCommentsList);
								} else if (ptrl_my_comment.isHeaderShown()) {
									myPostCommentsList
											.addAll(0, myCommentsList);
								}
								minBasePostId = myPostCommentsList
										.get(myPostCommentsList.size() - 1).getCommentInfo_FirstLayer().getPostId();
//										.getAbstractInfo().getPostId();
								maxBasePostId = myPostCommentsList.get(0).getCommentInfo_FirstLayer().getPostId();
								myCommentListAdapter.notifyDataSetChanged();
							}
							if (ptrl_my_comment.isRefreshing()) {
								ptrl_my_comment.onRefreshComplete();
							}
							lv_myPostComments.setSelectionFromTop(y, 0);
						}
					}
				} catch (Exception e) {
					Log.e(TAG, "", e);
				}
			}

			@Override
			public void start() {
				dialog.show();
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {
			}

			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				dialog.dismiss();
				ToastUtils.showMessage(instance, jobj.getString("ErrorMessage"));
			}
		});
	}

	protected void setAdapter(final ArrayList<MyPostComments> myPostCommentsList) {
		myCommentListAdapter = new MyCommentListAdapter(instance,
				myPostCommentsList);
		lv_myPostComments.setAdapter(myCommentListAdapter);
	}

	private void initListener() {
		ptrl_my_comment.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// new GetDataTask().execute();

				// 发送请求得到新项目报文
				// 下拉刷新
				if (ptrl_my_comment.isHeaderShown()) {
					y = 0;
					basePostId = maxBasePostId;
					isNew = "true";
				}
				// 上拉刷新
				if (ptrl_my_comment.isFooterShown()) {
					basePostId = minBasePostId;
					y = myPostCommentsList.size() - 1;
					isNew = "false";
				}
				getMyPostComments();
				// 保存加载的位置
				// y =myPostCommentsList.size();
			}
		});
	}
}
