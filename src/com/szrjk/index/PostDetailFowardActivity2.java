package com.szrjk.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.szrjk.config.ConstantUser;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.IndexFragment;
import com.szrjk.dhome.R;
import com.szrjk.dhome.SelfActivity;
import com.szrjk.entity.Comment;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.Forward;
import com.szrjk.entity.ICallback;
import com.szrjk.entity.Like;
import com.szrjk.entity.OrdinaryPostDetail;
import com.szrjk.entity.PostAbstractList;
import com.szrjk.entity.PostDetail;
import com.szrjk.entity.PostStatis;
import com.szrjk.entity.UserCard;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.NormalForwardPostContent2Layout;
import com.szrjk.widget.PostContentForwardLayout;
import com.szrjk.widget.PostDetailBottomOperLayout;
import com.szrjk.widget.PostDetailViewCommentListLayout;

/**
 * 204 的帖子转发, 这里还是抽出来吧
 */
@ContentView(R.layout.activity_post_forward_detail)
public class PostDetailFowardActivity2 extends BaseActivity {

	// 底层布局
	@ViewInject(R.id.rl_case_detail_base)
	private RelativeLayout rl_base;

	@ViewInject(R.id.layout_pdvcl)
	private PostDetailViewCommentListLayout postDetaillviewLayout;

	@ViewInject(R.id.layout_pdbottom)
	private PostDetailBottomOperLayout postDetailBottomOperLayout;

	private PostDetailFowardActivity2 instance;
	private String postId;
	private String userSeqId;
	private int flag;

	/*** 转发的样式 ***/
	@ViewInject(R.id.pfncl_post)
	NormalForwardPostContent2Layout pfncl_post;

	/*** 原贴的样式 ***/
	@ViewInject(R.id.npcl_postForward)
	PostContentForwardLayout fowardPostContentLayout;

	OrdinaryPostDetail ordinaryPostDetail = new OrdinaryPostDetail();

	private OrdinaryPostDetail opostDetail;
	private static final int LOAD_CASEDETAIL_SUCCESS = 0;

	private UserCard userCard;
	private PostStatis postStatis;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == LOAD_CASEDETAIL_SUCCESS) {
				opostDetail = (OrdinaryPostDetail) msg.obj;
				userCard = opostDetail.getUserCard();
				postStatis = opostDetail.getPostStatis();
				setCaseData();
				postDetailBottomOperLayout.getBtn_laud().setClickable(true);
			}
		};
	};

	private int position;

	private boolean isFirstIn = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		initLayout();
	}

	public String getPostId() {
		if (opostDetail != null) {
			return opostDetail.getPostDetail().getPostId();
		}
		return null;
	}

	private void initLayout() {
		postId = getIntent().getStringExtra(Constant.POST_ID);
		position = getIntent().getIntExtra("position", 0);
		userSeqId = getIntent().getStringExtra(Constant.USER_SEQ_ID);
		userSeqId = ConstantUser.getUserInfo().getUserSeqId();
		String ptype = getIntent().getStringExtra("ptype");
		flag = getIntent().getIntExtra("flag", 0);
		// staticinit(ptype);
		loadPostDetailedData(userSeqId, postId, instance);
	}

	/** 病例分享内容和图片 */
	private void setCaseData() {

		// PostDetail postDetail = this.opostDetail.getPostDetail();
		// postContentLayout.setPostDetail(postDetail);
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
		busiParams.put("endNum", "5");
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
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
				if (jobj.getString("ReturnCode").equals("0006")
						&& jobj.getString("ErrorMessage").equals(
								"[queryPostForwardListByPostId]查询帖子信息异常")) {
					ToastUtils.showMessage(instance, "该帖子已被删除！");
					instance.finish();
				}
			}

			@Override
			public void success(JSONObject jsonObject) {
				try {
					dialog.dismiss();
					ErrorInfo errorObj = JSON.parseObject(
							jsonObject.getString("ErrorInfo"), ErrorInfo.class);
					if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
						JSONObject returnObj = jsonObject
								.getJSONObject("ReturnInfo");
						JSONObject listOut = returnObj.getJSONObject("ListOut");
						// CaseDetail caseDetail1 = new CaseDetail();

						// 下面会抛错 com.alibaba.fastjson.JSONException: exepct '[',
						// but string, type : class
						// com.szrjk.entity.OrdinaryPostDetail
						// OrdinaryPostDetail ordinaryPostDetail =
						// JSON.parseObject(returnObj.getString("ListOut"),OrdinaryPostDetail.class);

						// 赞，评论，转发的entity 化
						if (listOut.getString("forwardList") != null
								&& !listOut.getString("forwardList").equals("")) {
							ArrayList<Forward> forwardList = (ArrayList<Forward>) JSON
									.parseArray(
											listOut.getString("forwardList"),
											Forward.class);
							ordinaryPostDetail.setForwardList(forwardList);
							postDetaillviewLayout.setForwardList(forwardList);
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
							ordinaryPostDetail.setCommentList(commentList);
							postDetaillviewLayout.setCommentList(commentList);
						}
						if (listOut.getString("likeList") != null
								&& !listOut.getString("likeList").equals("")) {
							ArrayList<Like> likeList = (ArrayList<Like>) JSON
									.parseArray(listOut.getString("likeList"),
											Like.class);
							ordinaryPostDetail.setLikeList(likeList);
							postDetaillviewLayout.setLikeList(likeList);
						}
						UserCard userCard = JSON.parseObject(
								listOut.getString("userCard"), UserCard.class);

						if (listOut.getJSONObject("postDetail") != null) {
							PostDetail ppostDetail = JSON.parseObject(
									listOut.getString("postDetail"),
									PostDetail.class);
							List<PostAbstractList> palist = JSON.parseArray(
									listOut.getJSONObject("postDetail")
											.getString("postAbstractList"),
									PostAbstractList.class);
							ppostDetail.setPostAbstractList(palist);

							UserCard srcuc = null;
							PostAbstractList postAbstractInfo = null;
							for (int i = 0; i < palist.size(); i++) {
								postAbstractInfo = palist.get(i);
								String postLevel = postAbstractInfo
										.getPostLevel();
								if ("0".equals(postLevel)) {
									srcuc = postAbstractInfo.getUserCard();
									break;
								}
							}
							fowardPostContentLayout
									.setPostDetail(postAbstractInfo);
							fowardPostContentLayout.setUserCard(srcuc);
							fowardPostContentLayout
									.setBackgroundColor(getResources()
											.getColor(R.color.bg_global));
						}

						ordinaryPostDetail.setUserCard(userCard);
						PostStatis postStatis = JSON.parseObject(
								listOut.getString("postStatis"),
								PostStatis.class);
						postDetaillviewLayout.setPostStatis(postStatis);
						ordinaryPostDetail.setPostStatis(postStatis);
						ordinaryPostDetail.setPostType(listOut
								.getIntValue("postType"));

						PostDetail postDetail = JSON.parseObject(
								listOut.getString("postDetail"),
								PostDetail.class);
						// 不知为啥，下面几个都没set进去
						// postDetail.setpUserName(listOut.getJSONObject("postDetail").getString("pUserName"));
						// postDetail.setpContent(listOut.getJSONObject("postDetail").getString("pContent"));
						// postDetail.setpIsDelete(listOut.getJSONObject("postDetail").getString("pIsDelete"));
						// postDetail.setpUserSeqId(listOut.getJSONObject("postDetail").getString("pUserSeqId"));
						// postDetail.setSrcIsDelete(listOut.getJSONObject("postDetail").getString("srcIsDelete"));

						ordinaryPostDetail.setPostDetail(postDetail);
						ordinaryPostDetail.setMineLike(listOut
								.getBooleanValue("isMineLike"));

						// 设置入activity,用于转发等入参
						String username = ordinaryPostDetail.getPostDetail()
								.getPostAbstractList().get(0).getUserCard()
								.getUserName();

						PostAbstractList pp = PostAbstractList
								.fetchFirstLevel(ordinaryPostDetail
										.getPostDetail().getPostAbstractList());
						String postText = pp.getPostAbstract().getContent();
						if (postText == null || postText.isEmpty()) {
							postText = pp.getPostAbstract().getPostTitle();
						}
						String faceurl = ordinaryPostDetail.getUserCard()
								.getUserFaceUrl();
						String postType = ordinaryPostDetail.getPostType() + "";

						// 转发的内容
						pfncl_post.setPostDetail(postDetail);
						pfncl_post.setUserCard(userCard);

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
											if (postDetaillviewLayout
													.getLikeList() != null) {
												postDetaillviewLayout
														.getLikeList().clear();
											}
											postDetaillviewLayout.addLike();
											ToastUtils
													.showMessage(
															PostDetailFowardActivity2.this,
															"点赞成功!");
											loadPostDetailedData(userSeqId,
													postId, instance);
											postDetailBottomOperLayout
													.getBtn_laud()
													.setClickable(false);
										} else {
											if (postDetaillviewLayout
													.getLikeList() != null) {
												postDetaillviewLayout
														.getLikeList().clear();
											}
											postDetaillviewLayout.minusLike();
											ToastUtils
													.showMessage(
															PostDetailFowardActivity2.this,
															"取消点赞成功!");
											loadPostDetailedData(userSeqId,
													postId, instance);
											postDetailBottomOperLayout
													.getBtn_laud()
													.setClickable(false);
										}
										ordinaryPostDetail.setMineLike(islike);
									}
								},
								ordinaryPostDetail.isMineLike(),
								ordinaryPostDetail.getPostDetail()
										.getPostAbstractList().get(0)
										.getPostLevel(),
								ordinaryPostDetail
										.getPostDetail()
										.getPostAbstractList()
										.get(ordinaryPostDetail.getPostDetail()
												.getPostAbstractList().size() - 1)
										.getPostAbstract().getPostId(), 0);

						Message message = new Message();
						message.what = LOAD_CASEDETAIL_SUCCESS;
						message.obj = ordinaryPostDetail;
						handler.sendMessage(message);

						// setListViewHeight(lv_comment);
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
		if (!isFirstIn) {
			loadPostDetailedData(userSeqId, postId, instance);
		} else {
			isFirstIn = false;
		}
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
		// Intent intent = new Intent();
		// Bundle bundle = new Bundle();
		// bundle.putInt("position", position);
		// bundle.putString("transmitCount", postDetaillviewLayout
		// .getTv_transmitCount().getText().toString());
		// bundle.putString("commentCoumt", postDetaillviewLayout
		// .getTv_commentCoumt().getText().toString());
		// bundle.putString("laudCount", postDetaillviewLayout.getTv_laudCount()
		// .getText().toString());
		// bundle.putBoolean("isLike", postDetailBottomOperLayout.isIslike());
		// intent.putExtras(bundle);
		// setResult(Constant.NOTIFY_DATA_SET_CHANGE, intent);
		if (flag == Constant.INDEX_FLAG) {
			IndexFragment.POSITION = position;
			IndexFragment.FORWARD_NUM = Integer.parseInt(postDetaillviewLayout
					.getTv_transmitCount().getText().toString());
			IndexFragment.COMMEND_NUM = Integer.parseInt(postDetaillviewLayout
					.getTv_commentCoumt().getText().toString());
			IndexFragment.LIKE_NUM = Integer.parseInt(postDetaillviewLayout
					.getTv_laudCount().getText().toString());
			IndexFragment.ISLIKE = postDetailBottomOperLayout.isIslike();
			if (userCard.getUserSeqId()
					.equals(Constant.userInfo.getUserSeqId())) {
				if (postStatis != null) {
					IndexFragment.READ_NUM = postStatis.getREAD_NUM()+1;
				}
			}

			Log.i("data",
					position
							+ ","
							+ postDetaillviewLayout.getTv_transmitCount()
									.getText().toString()
							+ ","
							+ postDetaillviewLayout.getTv_commentCoumt()
									.getText().toString()
							+ ","
							+ postDetaillviewLayout.getTv_laudCount().getText()
									.toString() + ","
							+ postDetailBottomOperLayout.isIslike());
		}
		if (flag==Constant.SELF_FLAG) {
			if (userCard.getUserSeqId().equals(Constant.userInfo.getUserSeqId())) {
				if (postStatis!=null) {
					Log.i("num", postStatis.getREAD_NUM()+1+"");
					SelfActivity.READ_NUM=postStatis.getREAD_NUM()+1;
				}
			}
		}
	}
}
