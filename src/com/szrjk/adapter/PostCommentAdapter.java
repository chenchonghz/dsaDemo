package com.szrjk.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.CommentActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.Comment;
import com.szrjk.entity.Forward;
import com.szrjk.entity.Like;
import com.szrjk.util.BusiUtils;
import com.szrjk.util.DialogUtil;
import com.szrjk.util.DisplayTimeUtil;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.util.SpannableStringUtils;
import com.szrjk.widget.PostDetailBottomOperLayout;
import com.szrjk.widget.UserCardLayout;

/**
 * 帖子详情中 ，评论等的列表
 * @param <T>
 */
public class PostCommentAdapter<T> extends BaseAdapter {

	private static String TAG = PostCommentAdapter.class.getCanonicalName();
	private BaseActivity context;
	private List<T> postComments;
	private LayoutInflater inflater;
	private int tabId;
	private boolean isMore;
	private ImageLoaderUtil imageLoaderUtil;

	public  PostCommentAdapter(Context context,List<T> postComments,int tabId, boolean isMore) {
		this.context= (BaseActivity) context;
		this.postComments=postComments;
		this.inflater=LayoutInflater.from(context);
		this.tabId=tabId;
		this.isMore=isMore;
	}
	@Override
	public int getCount() {
		if (isMore) {
			return postComments.size();
		}
		return postComments.size()<=5?postComments.size():5;
	}

	@Override
	public Object getItem(int position) {
		return postComments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder=null;
		if (convertView==null) {
				convertView=inflater.inflate(R.layout.item_post_comment, null);
				holder=new ViewHolder();
				holder.userCardLayout = (UserCardLayout) convertView.findViewById(R.id.request_usercard);
				holder.tv_time=(TextView)convertView.findViewById(R.id.tv_item_post_comment_time);
				holder.iv_comment=(ImageView)convertView.findViewById(R.id.iv_item_post_comment_comment);
				holder.tv_content=(TextView)convertView.findViewById(R.id.tv_item_post_comment_content);
				convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		Object object=postComments.get(position);
		//转发
		try{
			if (tabId==1) {
					Forward forward =(Forward) object;
//					String userFaceUrl = forward.getUserCard().getUserFaceUrl();
					holder.userCardLayout.setUser(forward.getUserCard());

					holder.tv_time.setText(DisplayTimeUtil.displayTimeString(forward.getForwardInfo().getCreateDate()));

					if (forward.getForwardInfo().getContent()!=null) {
						holder.tv_content.setText(forward.getForwardInfo().getContent()+"//"+ forward.getForwardInfo().getSrcUserCard().getUserName()+":"+ forward.getForwardInfo().getSrcPostAbstractCard().getContent());
					}
					holder.tv_content.setText(forward.getForwardInfo().getContent());
			}
		//评论
			if (tabId==2) {
					final Comment comment=(Comment) object;
//					String userFaceUrl = comment.getUserCard().getUserFaceUrl();
					holder.userCardLayout.setUser(comment.getUserCard());
					holder.tv_time.setText(DisplayTimeUtil.displayTimeString(comment.getCommentInfo().getCreateDate()));


					CharSequence postContent1 = "";

					if(comment.getCommentInfo().getpUserCard()!=null){
						//存在评论的评论
						final String puserid = comment.getCommentInfo().getpUserCard().getUserSeqId();
						final String currentuserid = Constant.userInfo.getUserSeqId();
						final String content = comment.getCommentInfo().getContent();
						final String pusername = comment.getCommentInfo().getpUserCard().getUserName();
						String targetstr = "回复"+pusername+":"+content;
						int start = 2;
						int stop = start+pusername.length();
						postContent1 =SpannableStringUtils.getClickableFaceSpan(context, targetstr, start, stop, puserid, currentuserid);
					}else{
						//不存在评论的评论,
						postContent1 = comment.getCommentInfo().getContent();
					}

					holder.tv_content.setText(postContent1);
					//设置该句使文本的超连接起作用
					holder.tv_content.setMovementMethod(LinkMovementMethod.getInstance());

					holder.iv_comment.setVisibility(View.VISIBLE);
					holder.iv_comment.setImageResource(R.drawable.icon_comment);
					holder.iv_comment.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							if (BusiUtils.isguest(context)) {
								DialogUtil.showGuestDialog(context, null);
								return;
							}
							Intent intent=new Intent(context,CommentActivity.class);
							intent.putExtra(Constant.POST_ID, comment.getCommentInfo().getSrcPostId());
							intent.putExtra(Constant.USER_SEQ_ID, comment.getUserCard().getUserSeqId());
							intent.putExtra(Constant.PCOMMENT_ID, comment.getCommentInfo().getPostId());
							intent.putExtra(Constant.COMMENT_LEVEL, comment.getCommentInfo().getLevel());
//							context.startActivity(intent);
							context.startActivityForResult(intent, PostDetailBottomOperLayout.TO_COMMENT);
					}
				});
			}
			//赞
			if (tabId==3) {

					Like like=(Like) object;
					String userFaceUrl = like.getUserCard().getUserFaceUrl();
					holder.userCardLayout.setUser(like.getUserCard());
					holder.tv_content.setVisibility(View.GONE);
				return convertView;
			}
		} catch (Exception e) {
			Log.e(TAG,"",e);
		}
		return convertView;
	}

	class ViewHolder{
		private UserCardLayout userCardLayout;
		private TextView tv_time;
		private ImageView iv_comment;
		private TextView tv_content;

	}


}
