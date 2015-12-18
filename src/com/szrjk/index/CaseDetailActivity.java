package com.szrjk.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.PostCommentAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.IndexFragment;
import com.szrjk.dhome.R;
import com.szrjk.entity.CaseDetail;
import com.szrjk.entity.CasePostDetail;
import com.szrjk.entity.Comment;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.Forward;
import com.szrjk.entity.ICallback;
import com.szrjk.entity.Like;
import com.szrjk.entity.PostStatis;
import com.szrjk.entity.UserCard;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.DisplayTimeUtil;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.DeptButton;
import com.szrjk.widget.FlowDeptLayout;
import com.szrjk.widget.PostDetailBottomOperLayout;
import com.szrjk.widget.PostDetailCaseView;
import com.szrjk.widget.PostDetailViewCommentListLayout;
import com.szrjk.widget.UserCardLayout;

@ContentView(R.layout.activity_case_detail)
public class CaseDetailActivity extends BaseActivity {

	private String TAG = CaseDetailActivity.class.getCanonicalName();
	// 标题
	@ViewInject(R.id.tv_case_detail_casetitle)
	private TextView tv_caseTitle;
	// 病例分享
	@ViewInject(R.id.tv_case_detail_share)
	private TextView tv_shareCase;
	// 完成度百分比
	@ViewInject(R.id.tv_case_detail_completerate)
	private TextView tv_completeRate;
	// 科室布局
	@ViewInject(R.id.fl_post_detailed_section)
	private FlowDeptLayout fl_section;

	@ViewInject(R.id.ll_post_type_content)
	private LinearLayout ll_post_type_content;

	@ViewInject(R.id.tv_post_detailed_time)
	private TextView tv_post_detailed_time;

	// 底层布局
	@ViewInject(R.id.rl_case_detail_base)
	private RelativeLayout rl_base;

	@ViewInject(R.id.ll_case_detail_list)
	private LinearLayout ll_case_detail_list;

	@ViewInject(R.id.layout_pdvcl)
	private PostDetailViewCommentListLayout postDetaillviewLayout;

	@ViewInject(R.id.layout_pdbottom)
	private PostDetailBottomOperLayout postDetailBottomOperLayout;

	@ViewInject(R.id.ucl_post)
	private UserCardLayout userCardLayout;

	private CaseDetailActivity instance;
	private String postId;
	private String userSeqId;

	CaseDetail caseDetail;

	public String getPostId() {
		if (caseDetail != null) {
			return caseDetail.getPostDetail().getPostId();
		}
		return null;
	}

	/** 发帖人名片 */
	public void setUserCard(UserCard userCard) {
		userCardLayout.setUser(userCard);
	}

	private static final int LOAD_CASEDETAIL_SUCCESS = 0;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == LOAD_CASEDETAIL_SUCCESS) {

				caseDetail = (CaseDetail) msg.obj;
				UserCard userCard = caseDetail.getUserCard();
				setUserCard(userCard);
				setCaseData();
				postDetailBottomOperLayout.getBtn_laud().setClickable(true);
			}
		}
	};
	private PostStatis postStatis;
	private PostCommentAdapter postCommentAdapter;
	private Resources resources;
	private int position;
	private int flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		initLayout();
	}

	private void staticinit(String ptype) {
		if (ptype.equals(Constant.PROBLEM_HELP)) {
			// 疑难杂证
			tv_shareCase.setText(getResources().getString(
					R.string.post_detailed_problem));
			ll_post_type_content.setBackgroundColor(getResources().getColor(
					R.color.post_type_problemhelp));
		} else if (ptype.equals(Constant.CASE_SHARE)) {
			// 病例分享
			tv_shareCase.setText(getResources().getString(
					R.string.post_detailed_share));
			ll_post_type_content.setBackgroundColor(getResources().getColor(
					R.color.post_type_caseshare));
		}
	}

	private void initLayout() {
		resources = getResources();
		postId = getIntent().getStringExtra(Constant.POST_ID);
		position = getIntent().getIntExtra("position", 0);
		// userSeqId = getIntent().getStringExtra(Constant.USER_SEQ_ID);
		userSeqId = Constant.userInfo.getUserSeqId();
		// String ptype = getIntent().getStringExtra("ptype");
		flag = getIntent().getIntExtra("flag", 0);
		loadPostDetailedData(userSeqId, postId, instance);
	}

	/** 病例分享内容和图片 */
	private void setCaseData() {
		// tv_shareCase.setText(getResources().getString(
		// R.string.post_detailed_share));
		// 病例标题

		// 完整度
		CasePostDetail casePostDetail = caseDetail.getPostDetail();
		String caseTitle = casePostDetail.getPostTitle();
		if (caseTitle != null) {
			tv_caseTitle.setText(caseTitle);
		}
		String completeRate = casePostDetail.getCompleteRate();
		if (!completeRate.equals("")) {
			tv_completeRate.setText("完整度 " + completeRate + "%");
		}

		// 科室显示
		String deptIds = casePostDetail.getDeptIds();
		String deptNames = casePostDetail.getDeptNames();
		if (deptIds != null) {
			String[] ids = deptIds.split(";");
			String[] names = deptNames.split(",");
			fl_section.removeAllViews();
			for (int i = 0; i < names.length; i++) {
				// TextView tv = getLabel(names[i]);
				// tv.setTag(ids[i]);
				// tv.setBackgroundResource(R.drawable.flow_text_selector);
				// fl_section.addView(tv, new
				// ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT,
				// ViewGroup.MarginLayoutParams.WRAP_CONTENT));

				int txtColor = resources.getColor(R.color.black);
				DeptButton btn = new DeptButton(this);
				btn.setTextColor(txtColor);
				btn.setGravity(Gravity.CENTER);

				setStyle(btn);

				btn.setTag(ids[i]);
				btn.setText(names[i]);
				fl_section.addView(btn);
			}
		} else {
			fl_section.setVisibility(View.GONE);
		}

		// 内容 填充
		// 主诉、病史
		String title1 = casePostDetail.getChapterTitle1();
		String caseContent = casePostDetail.getContent1();
		String caseUrl = casePostDetail.getPicList1();
		// 查体、辅查
		String title2 = casePostDetail.getChapterTitle2();
		String checkContent = casePostDetail.getContent2();
		String checkUrl = casePostDetail.getPicList2();

		String title3 = casePostDetail.getChapterTitle3();
		String treatContent = casePostDetail.getContent3();
		String treatUrl = casePostDetail.getPicList3();

		String title4 = casePostDetail.getChapterTitle4();
		String visitContent = casePostDetail.getContent4();
		String visitUrl = casePostDetail.getPicList4();

		ll_case_detail_list.removeAllViews();
		PostDetailCaseView pdcv1 = new PostDetailCaseView(this, title1,
				caseContent, caseUrl);
		PostDetailCaseView pdcv2 = new PostDetailCaseView(this, title2,
				checkContent, checkUrl);
		PostDetailCaseView pdcv3 = new PostDetailCaseView(this, title3,
				treatContent, treatUrl);
		PostDetailCaseView pdcv4 = new PostDetailCaseView(this, title4,
				visitContent, visitUrl);

		ll_case_detail_list.addView(pdcv1);
		ll_case_detail_list.addView(pdcv2);
		ll_case_detail_list.addView(pdcv3);
		ll_case_detail_list.addView(pdcv4);

	}

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
		dialog.show();
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			

			@Override
			public void start() {
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {
			}

			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				dialog.dismiss();
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
						CaseDetail caseDetail1 = new CaseDetail();
						if (listOut.getString("forwardList") != null
								&& !listOut.getString("forwardList").equals("")) {
							ArrayList<Forward> forwardList = (ArrayList<Forward>) JSON
									.parseArray(
											listOut.getString("forwardList"),
											Forward.class);
							caseDetail1.setForwardList(forwardList);
							postDetaillviewLayout.setForwardList(forwardList);
						}
						if (listOut.getString("commentList") != null
								&& !listOut.getString("commentList").equals("")) {
							ArrayList<Comment> commentList = (ArrayList<Comment>) JSON
									.parseArray(
											listOut.getString("commentList"),
											Comment.class);
							caseDetail1.setCommentList(commentList);

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

							postDetaillviewLayout.setCommentList(commentList);
						}
						if (listOut.getString("likeList") != null
								&& !listOut.getString("likeList").equals("")) {
							ArrayList<Like> likeList = (ArrayList<Like>) JSON
									.parseArray(listOut.getString("likeList"),
											Like.class);
							caseDetail1.setLikeList(likeList);
							postDetaillviewLayout.setLikeList(likeList);
							if (listOut
									.getBooleanValue("isMineLike")) {
								Log.i("点赞成功的likelist", postDetaillviewLayout.getLikeList().toString());
							}else {
								Log.i("取消点赞的likelist", postDetaillviewLayout.getLikeList().toString());
							}
						}
						UserCard userCard = JSON.parseObject(
								listOut.getString("userCard"), UserCard.class);
						caseDetail1.setUserCard(userCard);
						PostStatis postStatis = JSON.parseObject(
								listOut.getString("postStatis"),
								PostStatis.class);
						postDetaillviewLayout.setPostStatis(postStatis);
						caseDetail1.setPostStatis(postStatis);
						caseDetail1.setPostType(listOut.getIntValue("postType"));
						CasePostDetail postDetail = JSON.parseObject(
								listOut.getString("postDetail"),
								CasePostDetail.class);
						caseDetail1.setPostDetail(postDetail);
						caseDetail1.setMineLike(listOut
								.getBooleanValue("isMineLike"));

						// 设置入activity,用于转发等入参
						String username = caseDetail1.getUserCard()
								.getUserName();
						String postText = caseDetail1.getPostDetail()
								.getPostTitle();
						String faceurl = caseDetail1.getUserCard()
								.getUserFaceUrl();
						String postType = caseDetail1.getPostType() + "";

						staticinit(postType);

						// 填时间
						String createDate = postDetail.getCreateDate();
						try {
							createDate = DisplayTimeUtil
									.displayTimeString(createDate);
						} catch (Exception e) {
							createDate = "XX";
						}
						tv_post_detailed_time.setText(createDate);

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
													CaseDetailActivity.this,
													"点赞成功!");
											loadPostDetailedData(userSeqId, postId, instance);
											postDetailBottomOperLayout.getBtn_laud().setClickable(false);
										} else {
											if (postDetaillviewLayout.getLikeList()!=null) {
												postDetaillviewLayout.getLikeList().clear();
											}
											postDetaillviewLayout.minusLike();
											ToastUtils.showMessage(
													CaseDetailActivity.this,
													"取消点赞成功!");
											loadPostDetailedData(userSeqId, postId, instance);
											postDetailBottomOperLayout.getBtn_laud().setClickable(false);
										}
										caseDetail.setMineLike(islike);
									}
								}, caseDetail1.isMineLike(), caseDetail1
										.getPostDetail().getPostLevel(),
								caseDetail1.getPostDetail().getSrcPostId(), 0);

						Message message = new Message();
						message.what = LOAD_CASEDETAIL_SUCCESS;
						message.obj = caseDetail1;
						handler.sendMessage(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	private void setStyle(DeptButton btnStyle) {
		btnStyle.setPadding(12, 10, 12, 10);
		btnStyle.setTextColor(resources.getColor(R.color.header_bg));
		btnStyle.setBackground(resources
				.getDrawable(R.drawable.flow_dept_selector));
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadPostDetailedData(userSeqId, postId, instance);
	}
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode != RESULT_OK) {
//			return;
//		}
//		switch (requestCode) {
//		case PostDetailBottomOperLayout.TO_TRANSMIT:
////			postDetaillviewLayout.setBtnId(postDetailBottomOperLayout.getBtnId());
////			PostDetailViewCommentListLayout.btnId=1;
//			loadPostDetailedData(userSeqId, postId, instance);
//			break;
//		case PostDetailBottomOperLayout.TO_COMMENT:
////			postDetaillviewLayout.setBtnId(postDetailBottomOperLayout.getBtnId());
////			PostDetailViewCommentListLayout.btnId=2;
//			loadPostDetailedData(userSeqId, postId, instance);
//			break;
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		android.util.Log.i("keyevent", event.toString());
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			notifyIndexFramentSetDataSetChange();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	private void notifyIndexFramentSetDataSetChange() {
//		Intent intent = new Intent();
//		Bundle bundle = new Bundle();
//		bundle.putInt("position", position);
//		bundle.putString("transmitCount", postDetaillviewLayout
//				.getTv_transmitCount().getText().toString());
//		bundle.putString("commentCoumt", postDetaillviewLayout
//				.getTv_commentCoumt().getText().toString());
//		bundle.putString("laudCount", postDetaillviewLayout.getTv_laudCount()
//				.getText().toString());
//		bundle.putBoolean("isLike", postDetailBottomOperLayout.isIslike());
//		intent.putExtras(bundle);
//		setResult(Constant.NOTIFY_DATA_SET_CHANGE, intent);
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
