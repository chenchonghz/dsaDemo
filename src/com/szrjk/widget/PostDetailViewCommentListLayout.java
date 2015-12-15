package com.szrjk.widget;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szrjk.adapter.PostCommentAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.Comment;
import com.szrjk.entity.Forward;
import com.szrjk.entity.Like;
import com.szrjk.entity.PostStatis;
import com.szrjk.index.MoreCommentActivity;
import com.szrjk.index.MoreForwardActivity;
import com.szrjk.index.MoreLikeActivity;
import com.szrjk.util.SetListViewHeightUtils;

/**
 * 评论列表组件
 */
public class PostDetailViewCommentListLayout extends RelativeLayout {

	protected static String TAG = PostDetailViewCommentListLayout.class
			.getCanonicalName();

	private List<Forward> forwardList;
	private List<Comment> commentList;
	private List<Like> likeList;

	private Context context;

	private TextView tv_selectTransmit;
	// 转发标签
	private TextView tv_transmitTab;
	// 转发数
	private TextView tv_transmitCount;

	private TextView tv_selectComment;
	// 评论标签
	private TextView tv_commentTab;
	// 评论数
	private TextView tv_commentCoumt;

	private TextView tv_selectLaud;
	// 赞标签
	private TextView tv_laudTab;
	// 赞数
	private TextView tv_laudCount;
	// 没有转发、评论、赞
	private TextView tv_commentNone;
	// 评论容器
	private NoScrollListView lv_comment;
	// 选中转发
	private RelativeLayout rl_transmit;
	// 选中评论
	private RelativeLayout rl_comment;
	// 选中赞
	private RelativeLayout rl_laud;

	private PostStatis postStatis;
	private PostCommentAdapter postCommentAdapter;
	private View contexView;
	private PHandler handler = new PHandler();
	private int btnId;

	public int getBtnId() {
		return btnId;
	}

	public void setBtnId(int btnId) {
		this.btnId = btnId;
	}

	public PostStatis getPostStatis() {
		return postStatis;
	}

	public void setPostStatis(PostStatis postStatis) {
		this.postStatis = postStatis;
		setNum(postStatis.getFORWARD_NUM(), postStatis.getCOMMENT_NUM(),
				postStatis.getLIKE_NUM());
	}

	public PostDetailViewCommentListLayout(Context context) {
		super(context);
	}

	public PostDetailViewCommentListLayout(final Context context,
			AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contexView = inflater.inflate(R.layout.layout_post_detail_comment_list,
				this);

		rl_transmit = (RelativeLayout) contexView
				.findViewById(R.id.rl_post_detailed_transmit);
		rl_comment = (RelativeLayout) contexView
				.findViewById(R.id.rl_post_detailed_comment);
		rl_laud = (RelativeLayout) contexView
				.findViewById(R.id.rl_post_detailed_laud);
		tv_selectTransmit = (TextView) contexView
				.findViewById(R.id.tv_post_detailed_selecttransmit);
		tv_transmitTab = (TextView) contexView
				.findViewById(R.id.tv_post_detailed_transmittab);
		tv_transmitCount = (TextView) contexView
				.findViewById(R.id.tv_post_detailed_transmitcount);

		tv_selectComment = (TextView) contexView
				.findViewById(R.id.tv_post_detailed_selectcomment);
		tv_commentTab = (TextView) contexView
				.findViewById(R.id.tv_post_detailed_commenttab);
		tv_commentCoumt = (TextView) contexView
				.findViewById(R.id.tv_post_detailed_commentcount);

		tv_selectLaud = (TextView) contexView
				.findViewById(R.id.tv_post_detailed_selectlaud);
		tv_laudTab = (TextView) contexView
				.findViewById(R.id.tv_post_detailed_laudtab);
		tv_laudCount = (TextView) contexView
				.findViewById(R.id.tv_post_detailed_laudcount);
		tv_commentNone = (TextView) contexView
				.findViewById(R.id.tv_post_detailed_commentnone);
		lv_comment = (NoScrollListView) contexView
				.findViewById(R.id.lv_post_detailed_comment);

		clickForward(contexView);
		clickLike(contexView);
		clickComment(contexView);

		clickCommentTable();
		// setData(context, commentList);
		

		this.context = context;
	}

	// class PThread extends Thread {
	// private Context pcontext;
	// private List ppostComments;
	//
	// PThread(Context context, List postComments) {
	// this.pcontext = context;
	// this.ppostComments = postComments;
	// }
	//
	// @Override
	// public void run() {
	// setData(pcontext, ppostComments);
	// }
	// }

	private static final int DATA_CHARGE_NOTIFY_COMMENT = 1000;
	private static final int DATA_CHARGE_NOTIFY_FORWARD = 1001;
	private static final int DATA_CHARGE_NOTIFY_PRAISE = 1002;

	class PHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DATA_CHARGE_NOTIFY_COMMENT:
				// 上传所有照片
				// urlArr = new String[imgItems.size()];
				// for (int i=0;i<imgItems.size();i++){
				// ImageItem postItems = imgItems.get(i);
				// String url = updateFile(postItems.getBitmap());
				// urlArr[i] = url;
				// }
				// iSelectImgCallback.selectImgCallback(imgItems,urlArr);

				setData(context, commentList);
				break;
			case DATA_CHARGE_NOTIFY_FORWARD:
				setData(context, forwardList);
				break;
			case DATA_CHARGE_NOTIFY_PRAISE:
				setData(context, likeList);
				break;
			}
		}
	}

	public void addLike() {
		int i = Integer.parseInt(tv_laudCount.getText().toString());
		tv_laudCount.setText((i + 1) + "");
		// postCommentAdapter.notifyDataSetChanged();
	}

	public void minusLike() {
		int i = Integer.parseInt(tv_laudCount.getText().toString());
		tv_laudCount.setText((i - 1) + "");
		// postCommentAdapter.notifyDataSetChanged();
	}

	public void setNum(int forwardnum, int commentnum, int likenum) {
		tv_transmitCount.setText(String.valueOf(forwardnum));
		tv_commentCoumt.setText(String.valueOf(commentnum));
		tv_laudCount.setText(String.valueOf(likenum));
		// clickLike(contexView);
	}

	int tabId;

	/** 点击转发标签 */
	// @OnClick(R.id.rl_post_detailed_transmit)
	public void clickForward(View view) {
		rl_transmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				clickForwardTable();
				// ArrayList<Forward> forwards = postDetail.getForwardList();
				// setData(context, forwardList);
				// new PThread(context,forwardList).start();
			}
		});
	}
	private void clickForwardTable() {
		tabId = 1;
		rl_transmit.setSelected(true);
		oneOutThree(rl_transmit, rl_comment, rl_laud);
		setSelected();
		
		Message msg = new Message();
		msg.what = DATA_CHARGE_NOTIFY_FORWARD;
		handler.sendMessage(msg);
	}

	/** 点击评论标签 */
	// @OnClick(R.id.rl_post_detailed_comment)
	public void clickComment(View view) {
		rl_comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				clickCommentTable();
				// setData(context, commentList);
				// new PThread(context,commentList).start();

			}
		});
	}
	
	private void clickCommentTable() {
		tabId = 2;
		rl_comment.setSelected(true);
		oneOutThree(rl_comment, rl_transmit, rl_laud);
		setSelected();

		Message msg = new Message();
		msg.what = DATA_CHARGE_NOTIFY_COMMENT;
		handler.sendMessage(msg);
	}

	/** 点击赞标签 */
	// @OnClick(R.id.rl_post_detailed_laud)
	public void clickLike(View view) {

		rl_laud.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				clickLikeTable();
				// setData(context, likeList);
				// new PThread(context,likeList).start();
				
			}
		});
	}

	private void clickLikeTable() {
		tabId = 3;
		rl_laud.setSelected(true);
		oneOutThree(rl_laud, rl_transmit, rl_comment);
		setSelected();
		
		Message msg = new Message();
		msg.what = DATA_CHARGE_NOTIFY_PRAISE;
		handler.sendMessage(msg);
	}

	private void setSelected() {
		multiSelect(rl_transmit, tv_transmitTab, tv_transmitCount,
				tv_selectTransmit);
		multiSelect(rl_comment, tv_commentTab, tv_commentCoumt,
				tv_selectComment);
		multiSelect(rl_laud, tv_laudTab, tv_laudCount, tv_selectLaud);
	}

	private void multiSelect(View view1, View view2, View view3, View view4) {
		if (view1.isSelected()) {
			view2.setSelected(true);
			view3.setSelected(true);
			view4.setSelected(true);
		} else {
			view2.setSelected(false);
			view3.setSelected(false);
			view4.setSelected(false);
		}
	}

	private void oneOutThree(View view1, View view2, View view3) {
		if (view1.isSelected()) {
			view2.setSelected(false);
			view3.setSelected(false);
		}
		if (view2.isSelected()) {
			view1.setSelected(false);
			view3.setSelected(false);
		}
		if (view3.isSelected()) {
			view1.setSelected(false);
			view2.setSelected(false);
		}
	}

	View mFooterView = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> void setData(Context context, List<T> postComments) {

		try {
			if (postComments != null && !postComments.isEmpty()) {
				tv_commentNone.setVisibility(View.GONE);
				lv_comment.setVisibility(View.VISIBLE);
				// Log.e(TAG, DjsonUtils.bean2Json(postComments));
				postCommentAdapter = new PostCommentAdapter(context,
						postComments, tabId, false);
				if (postComments.size() > 5) {
					if (lv_comment.getFooterViewsCount() == 0) {
						LayoutInflater layoutInflater = (LayoutInflater) context
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						mFooterView = layoutInflater.inflate(
								R.layout.item_footer_view, null);
						mFooterView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							jumpActivity();
						}
					});
						lv_comment.addFooterView(mFooterView);
					}
				} else {
					//每次总是先remove掉FooterView
					if (mFooterView != null)
						lv_comment.removeFooterView(mFooterView);
				}
				lv_comment.setAdapter(postCommentAdapter);
				

				if (postCommentAdapter != null) {
					postCommentAdapter.notifyDataSetChanged();
				}

				SetListViewHeightUtils.setListViewHeight(lv_comment);
			} else {
				tv_commentNone.setVisibility(View.VISIBLE);
				lv_comment.setVisibility(View.GONE);
				if (tabId == 1) {
					tv_commentNone.setText(getResources().getString(
							R.string.post_detailed_tv_forwardnone));
				}
				if (tabId == 2) {
					tv_commentNone.setText(getResources().getString(
							R.string.post_detailed_tv_commentnone));
				}
				if (tabId == 3) {
					tv_commentNone.setText(getResources().getString(
							R.string.post_detailed_tv_likenone));
				}
			}

		} catch (Exception e) {
			Log.e(TAG, "", e);
		}

	}
	
	private void jumpActivity() {
		Intent intent = null;
		if (tabId == 1) {
			Forward forward = forwardList.get(0);
			intent = new Intent(
					PostDetailViewCommentListLayout.this.context,
					MoreForwardActivity.class);
			intent.putExtra(Constant.PCOMMENT_ID,
					forward.getForwardInfo()
							.getPostId());
			intent.putExtra(Constant.POST_ID, forward
					.getForwardInfo().getSrcPostId());
		}
		if (tabId == 2) {
			Comment comment = commentList.get(0);
			intent = new Intent(
					PostDetailViewCommentListLayout.this.context,
					MoreCommentActivity.class);
			// 当前评论的id
			intent.putExtra(Constant.PCOMMENT_ID,
					comment.getCommentInfo()
							.getPostId());
			intent.putExtra(Constant.POST_ID, comment
					.getCommentInfo().getSrcPostId());
		}
		if (tabId == 3) {
			Like like = likeList.get(0);
			intent = new Intent(
					PostDetailViewCommentListLayout.this.context,
					MoreLikeActivity.class);
			intent.putExtra(Constant.PCOMMENT_ID, like
					.getLikeInfo().getPostId());
			intent.putExtra(Constant.POST_ID, like
					.getLikeInfo().getSrcPostId());
		}
		PostDetailViewCommentListLayout.this.context
				.startActivity(intent);
	}

	public List<Forward> getForwardList() {
		return forwardList;
	}

	public void setForwardList(List<Forward> forwardList) {
		this.forwardList = forwardList;
		if (btnId==1) {
			rl_transmit.performClick();
		}
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
		if (btnId==2) {
			rl_comment.performClick();
		}
	}

	public List<Like> getLikeList() {
		return likeList;
	}

	public void setLikeList(List<Like> likeList) {
		this.likeList = likeList;
		if (btnId==3) {
			rl_laud.performClick();
		}
	}

	public TextView getTv_transmitCount() {
		return tv_transmitCount;
	}

	public void setTv_transmitCount(TextView tv_transmitCount) {
		this.tv_transmitCount = tv_transmitCount;
	}

	public TextView getTv_commentCoumt() {
		return tv_commentCoumt;
	}

	public void setTv_commentCoumt(TextView tv_commentCoumt) {
		this.tv_commentCoumt = tv_commentCoumt;
	}

	public TextView getTv_laudCount() {
		return tv_laudCount;
	}

	public void setTv_laudCount(TextView tv_laudCount) {
		this.tv_laudCount = tv_laudCount;
	}
}
