package com.szrjk.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szrjk.config.Constant;
import com.szrjk.config.ConstantUser;
import com.szrjk.dhome.CommentActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.CommentInfo;
import com.szrjk.entity.MyPostComments;
import com.szrjk.entity.PostAbstractList;
import com.szrjk.entity.PostInfo;
import com.szrjk.entity.UserCard;
import com.szrjk.index.CaseDetailActivity;
import com.szrjk.index.PostDetailActivity;
import com.szrjk.index.PostDetailFowardActivity2;
import com.szrjk.util.DisplayTimeUtil;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.widget.UserCardLayout;

public class MyCommentListAdapter extends BaseAdapter {
	private Context context;
	private List<MyPostComments> myPostCommentsList;
	private LayoutInflater inflater;
	private static int TYPE=1;

	public MyCommentListAdapter(Context context,
			List<MyPostComments> myPostCommentsList) {
		this.context = context;
		this.myPostCommentsList = myPostCommentsList;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return myPostCommentsList.size();
	}

	@Override
	public Object getItem(int position) {
		return myPostCommentsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_my_comment_list, null);
			holder = new ViewHolder();
			holder.ll_firstLayer = (LinearLayout) convertView
					.findViewById(R.id.ll_firstLayer);
			holder.rl_secondLayer = (RelativeLayout) convertView
					.findViewById(R.id.rl_secondLayer);
			holder.rl_postInfo = (RelativeLayout) convertView
					.findViewById(R.id.rl_postInfo);
			holder.rl_postContent = (RelativeLayout) convertView
					.findViewById(R.id.rl_postContent);
			holder.ucl_userCardLayout = (UserCardLayout) convertView
					.findViewById(R.id.ucl_userCardLayout);
			holder.btn_reply = (Button) convertView
					.findViewById(R.id.btn_reply);
			holder.tv_commentcontent1 = (TextView) convertView
					.findViewById(R.id.tv_commentcontent1);
			holder.tv_commentcontent2 = (TextView) convertView
					.findViewById(R.id.tv_commentcontent2);
			holder.iv_portrait = (ImageView) convertView
					.findViewById(R.id.iv_portrait);
			holder.iv_vip = (ImageView) convertView.findViewById(R.id.iv_vip);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_postcontent = (TextView) convertView
					.findViewById(R.id.tv_postcontent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyPostComments myPostComments = myPostCommentsList.get(position);
		List<PostAbstractList> postAbstractList = myPostComments.getAbstractInfo().getPostAbstractList();
		final PostInfo abstractInfo;
		if (postAbstractList!=null&&!postAbstractList.isEmpty()) {
			abstractInfo=postAbstractList.get(postAbstractList.size()-1).getPostAbstract();
		}else{
			abstractInfo = myPostComments.getAbstractInfo();
		}
		UserCard userCard = myPostComments.getUserCard();
		final UserCard userCard_FirstLayer = myPostComments
				.getUserCard_FirstLayer();
		UserCard userCard_SecondLayer = myPostComments
				.getUserCard_SecondLayer();
		final CommentInfo commentInfo_FirstLayer = myPostComments
				.getCommentInfo_FirstLayer();
		CommentInfo commentInfo_SecondLayer = myPostComments
				.getCommentInfo_SecondLayer();
		if (commentInfo_SecondLayer == null
				|| commentInfo_SecondLayer.equals("")) {
			holder.tv_commentcontent1.setText(commentInfo_FirstLayer
					.getContent());
			holder.tv_commentcontent2.setVisibility(View.GONE);
			holder.rl_postContent.setBackgroundColor(context.getResources()
					.getColor(R.color.base_bg));
			holder.rl_postInfo.setBackgroundColor(context.getResources()
					.getColor(R.color.base_bg2));
		} else {
			holder.tv_commentcontent1.setText("回复"
					+ userCard_SecondLayer.getUserName() + ":"
					+ commentInfo_FirstLayer.getContent());
			holder.tv_commentcontent2.setText(userCard_SecondLayer
					.getUserName()
					+ "回复"
					+ userCard_FirstLayer.getUserName()
					+ ":" + commentInfo_SecondLayer.getContent());
			holder.rl_secondLayer.setBackgroundColor(context.getResources()
					.getColor(R.color.base_bg));
			holder.rl_postContent.setBackgroundColor(context.getResources()
					.getColor(R.color.base_bg2));
		}
		holder.ucl_userCardLayout.setUser(userCard_FirstLayer);
		try {
			holder.tv_time.setText(DisplayTimeUtil
					.displayTimeString(commentInfo_FirstLayer.getCreateDate()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		holder.tv_name.setText(userCard.getUserName());
		holder.tv_postcontent.setText(abstractInfo.getContent());
		ImageLoaderUtil imageLoaderUtil = null;
		String url = null;
		if (abstractInfo != null && !abstractInfo.equals("")) {
			if (abstractInfo.getPicListstr() != null
					&& !abstractInfo.getPicListstr().isEmpty()) {
				String[] picList = abstractInfo.getPicList();
				url = picList[0];
				setTYPE(2);
			} else {
				url = ConstantUser.getUserInfo().getUserFaceUrl();
				if (!ConstantUser.getUserInfo().getUserLevel().equals("11")) {
					setTYPE(2);
				}
			}
		}
		imageLoaderUtil = new ImageLoaderUtil(context, url, holder.iv_portrait,
				R.drawable.pic_downloadfailed_bg,
				R.drawable.pic_downloadfailed_bg);
		imageLoaderUtil.showImage();
		
		if (getTYPE()==2) {
			holder.iv_vip.setVisibility(View.GONE);
		}
		holder.btn_reply.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(context, CommentActivity.class);
				intent.putExtra(Constant.POST_ID,
						commentInfo_FirstLayer.getSrcPostId());
				intent.putExtra(Constant.USER_SEQ_ID,
						userCard_FirstLayer.getUserSeqId());
				intent.putExtra(Constant.PCOMMENT_ID,
						commentInfo_FirstLayer.getPostId());
				intent.putExtra(Constant.COMMENT_LEVEL,
						commentInfo_FirstLayer.getLevel());
				((Activity) context).startActivity(intent);
			}
		});
		holder.rl_postContent.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				String postType = abstractInfo.getPostType();
				String postId = abstractInfo.getPostId();
				Intent intent=null;
				switch (Integer.parseInt(postType)) {
				case 101:
					intent = new Intent(context, PostDetailActivity.class);
					intent.putExtra(Constant.USER_SEQ_ID, Constant.userInfo.getUserSeqId());
					intent.putExtra(Constant.POST_ID, postId);
					break;
				case 102:
					intent = new Intent(context, CaseDetailActivity.class);
					intent.putExtra(Constant.USER_SEQ_ID, Constant.userInfo.getUserSeqId());
					intent.putExtra(Constant.POST_ID, postId);
					break;
				case 103:
					intent = new Intent(context, CaseDetailActivity.class);
					intent.putExtra(Constant.USER_SEQ_ID, Constant.userInfo.getUserSeqId());
					intent.putExtra(Constant.POST_ID, postId);
					break;
				case 104:
					intent = new Intent(context, PostDetailActivity.class);
					intent.putExtra(Constant.USER_SEQ_ID, Constant.userInfo.getUserSeqId());
					intent.putExtra(Constant.POST_ID, postId);
					break;
				case 202:
					intent = new Intent(context, PostDetailFowardActivity2.class);
					intent.putExtra(Constant.USER_SEQ_ID, Constant.userInfo.getUserSeqId());
					intent.putExtra(Constant.POST_ID, postId);
					break;
				case 204:
					intent = new Intent(context, PostDetailFowardActivity2.class);
					intent.putExtra(Constant.USER_SEQ_ID, Constant.userInfo.getUserSeqId());
					intent.putExtra(Constant.POST_ID, postId);
					break;
				}
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	public int getTYPE() {
		return TYPE;
	}

	public void setTYPE(int tYPE) {
		TYPE = tYPE;
	}

	private class ViewHolder {
		private LinearLayout ll_firstLayer;
		private RelativeLayout rl_secondLayer;
		private RelativeLayout rl_postInfo;
		private RelativeLayout rl_postContent;
		private UserCardLayout ucl_userCardLayout;
		private Button btn_reply;
		private TextView tv_commentcontent1;
		private TextView tv_commentcontent2;
		private ImageView iv_portrait;
		private ImageView iv_vip;
		private TextView tv_time;
		private TextView tv_name;
		private TextView tv_postcontent;
	}
}
