package com.szrjk.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.IndexFragment;
import com.szrjk.dhome.R;
import com.szrjk.entity.Comment;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.Forward;
import com.szrjk.entity.ICallback;
import com.szrjk.entity.Like;
import com.szrjk.entity.OrdinaryPostDetail;
import com.szrjk.entity.PostDetail;
import com.szrjk.entity.PostStatis;
import com.szrjk.entity.UserCard;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.PostContentLayout;
import com.szrjk.widget.PostDetailBottomOperLayout;
import com.szrjk.widget.PostDetailViewCommentListLayout;

@ContentView(R.layout.activity_post1_detail)
public class PostDetailActivity extends BaseActivity {

	// 底层布局
	@ViewInject(R.id.rl_case_detail_base)
	private RelativeLayout rl_base;

	@ViewInject(R.id.layout_pdvcl)
	private PostDetailViewCommentListLayout postDetaillviewLayout;

	@ViewInject(R.id.layout_pdbottom)
	private PostDetailBottomOperLayout postDetailBottomOperLayout;

	OrdinaryPostDetail postDetail1 = new OrdinaryPostDetail();

	private PostDetailActivity instance;
	private String postId;
	private String userSeqId;
	private int flag;

	@ViewInject(R.id.npcl_post)
	PostContentLayout postContentLayout;

	private OrdinaryPostDetail postDetail2;
	private static final int LOAD_CASEDETAIL_SUCCESS = 0;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == LOAD_CASEDETAIL_SUCCESS) {
				postDetail2 = (OrdinaryPostDetail) msg.obj;
				UserCard userCard = postDetail2.getUserCard();
				postContentLayout.setUserCard(userCard);
				setCaseData();
				postDetailBottomOperLayout.getBtn_laud().setClickable(true);
			}
		}
	};

	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		initLayout();
	}

	public String getPostId() {
		if (postDetail2 != null) {
			return postDetail2.getPostDetail().getPostId();
		}
		return null;
	}

	private void initLayout() {
		postId = getIntent().getStringExtra(Constant.POST_ID);
		position = getIntent().getIntExtra("position", 0);
		// userSeqId = getIntent().getStringExtra(Constant.USER_SEQ_ID);
		userSeqId = Constant.userInfo.getUserSeqId();
		String ptype = getIntent().getStringExtra("ptype");
		flag = getIntent().getIntExtra("flag", 0);
		loadPostDetailedData(userSeqId, postId, instance);
	}

	/** 病例分享内容和图片 */
	private void setCaseData() {

		PostDetail postDetail = this.postDetail2.getPostDetail();

		// String title1 = "";
		// String caseContent = postDetail.getContent();
		// String caseUrl = postDetail.getPicList();
		//
		// PostDetailCaseView pdcv1 = new
		// PostDetailCaseView(this,title1,caseContent,caseUrl);
		// ll_case_detail_list.addView(pdcv1);

		postContentLayout.setPostDetail(postDetail);
	}

	/**
	 * 请求详情接口
	 * 
	 * @param userSeqId
	 * @param postId
	 * @param context
	 */
	private void loadPostDetailedData(final String userSeqId,
			final String postId, Context context) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPostDetailById");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", userSeqId);
		busiParams.put("postId", postId);
		busiParams.put("beginNum", "0");
		busiParams.put("endNum", "100");
		paramMap.put("BusiParams", busiParams);
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
				try {
					ErrorInfo errorObj = JSON.parseObject(
							jsonObject.getString("ErrorInfo"), ErrorInfo.class);
					if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
						JSONObject returnObj = jsonObject
								.getJSONObject("ReturnInfo");
						JSONObject listOut = returnObj.getJSONObject("ListOut");
						// CaseDetail caseDetail1 = new CaseDetail();

						// 赞，评论，转发的entity 化
						if (listOut.getString("forwardList") != null
								&& !listOut.getString("forwardList").equals("")) {
							ArrayList<Forward> forwardList = (ArrayList<Forward>) JSON
									.parseArray(
											listOut.getString("forwardList"),
											Forward.class);
							postDetail1.setForwardList(forwardList);
							postDetaillviewLayout.setForwardList(forwardList
									);
						}
						if (listOut.getString("commentList") != null
								&& !listOut.getString("commentList").equals("")) {
							ArrayList<Comment> commentList = (ArrayList<Comment>) JSON
									.parseArray(
											listOut.getString("commentList"),
											Comment.class);
							// 层次太深，抽出来
							com.alibaba.fastjson.JSONArray jsonCommentArray = listOut
									.getJSONArray("commentList");
							for (int i = 0; i < jsonCommentArray.size(); i++) {
								String tmppusercard = jsonCommentArray
										.getJSONObject(i)
										.getJSONObject("commentInfo")
										.getString("pUserCard");
								if (tmppusercard == null
										|| tmppusercard.equals(""))
									continue;
								UserCard tmpuserCard = JSON.parseObject(
										tmppusercard, UserCard.class);
								commentList.get(i).getCommentInfo()
										.setpUserCard(tmpuserCard);
							}
							// 层次太深，抽出来

							postDetail1.setCommentList(commentList);
							postDetaillviewLayout.setCommentList(commentList
									);
						}
						if (listOut.getString("likeList") != null
								&& !listOut.getString("likeList").equals("")) {
							ArrayList<Like> likeList = (ArrayList<Like>) JSON
									.parseArray(listOut.getString("likeList"),
											Like.class);
							postDetail1.setLikeList(likeList);
							postDetaillviewLayout.setLikeList(likeList);
						}
						UserCard userCard = JSON.parseObject(
								listOut.getString("userCard"), UserCard.class);
						postDetail1.setUserCard(userCard);
						PostStatis postStatis = JSON.parseObject(
								listOut.getString("postStatis"),
								PostStatis.class);
						postDetaillviewLayout.setPostStatis(postStatis);
						postDetail1.setPostStatis(postStatis);
						postDetail1.setPostType(listOut.getIntValue("postType"));

						PostDetail postDetail = JSON.parseObject(
								listOut.getString("postDetail"),
								PostDetail.class);
						postDetail1.setPostDetail(postDetail);
						postDetail1.setMineLike(listOut
								.getBooleanValue("isMineLike"));

						// 设置入activity,用于转发等入参
						String username = postDetail1.getUserCard()
								.getUserName();
						String postText = postDetail1.getPostDetail()
								.getContent();
						String faceurl = postDetail1.getUserCard()
								.getUserFaceUrl();
						String postType = postDetail1.getPostType() + "";

						/** 底部的init处理 **/
						postDetailBottomOperLayout.initData(
								postId,
								userSeqId,
								postText,
								faceurl,
								username,
								postType,
								new ICallback() {
									@Override
									public void doCallback(Map m) {
										boolean islike = (Boolean) m
												.get("islike");
										if (islike) {
											if (postDetaillviewLayout.getLikeList()!=null) {
												postDetaillviewLayout.getLikeList().clear();
											}
											postDetaillviewLayout.addLike();
											ToastUtils.showMessage(
													PostDetailActivity.this,
													"点赞成功!");
											loadPostDetailedData(userSeqId, postId, instance);
											postDetailBottomOperLayout.getBtn_laud().setClickable(false);
										} else {
											if (postDetaillviewLayout.getLikeList()!=null) {
												postDetaillviewLayout.getLikeList().clear();
											}
											postDetaillviewLayout.minusLike();
											ToastUtils.showMessage(PostDetailActivity.this,"取消点赞成功!");
											loadPostDetailedData(userSeqId, postId, instance);
											postDetailBottomOperLayout.getBtn_laud().setClickable(false);
										}
										postDetail1.setMineLike(islike);
									}
								}, postDetail1.isMineLike(), postDetail1
										.getPostDetail().getPostLevel(),
								postDetail1.getPostDetail().getSrcPostId(),
								postDetail1.getPostDetail().getIsOpen());//

						Message message = new Message();
						message.what = LOAD_CASEDETAIL_SUCCESS;
						message.obj = postDetail1;
						handler.sendMessage(message);

					}
				} catch (Exception e) {
					Log.e(TAG, "", e);
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadPostDetailedData(userSeqId, postId, instance);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			notifyIndexFramentSetDataSetChange();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void notifyIndexFramentSetDataSetChange() {

		if(flag == Constant.INDEX_FLAG){
			IndexFragment.POSITION=position;
			IndexFragment.FORWARD_NUM= Integer.parseInt(postDetaillviewLayout
					.getTv_transmitCount().getText().toString());
			IndexFragment.COMMEND_NUM=Integer.parseInt(postDetaillviewLayout
					.getTv_commentCoumt().getText().toString());
			IndexFragment.LIKE_NUM=Integer.parseInt(postDetaillviewLayout.getTv_laudCount()
					.getText().toString());
			IndexFragment.ISLIKE=postDetailBottomOperLayout.isIslike();

			android.util.Log.i("data", position
					+ ","
					+ postDetaillviewLayout.getTv_transmitCount().getText()
							.toString()
					+ ","
					+ postDetaillviewLayout.getTv_commentCoumt().getText()
							.toString() + ","
					+ postDetaillviewLayout.getTv_laudCount().getText().toString()
					+ "," + postDetailBottomOperLayout.isIslike());
		}	
	}
}
