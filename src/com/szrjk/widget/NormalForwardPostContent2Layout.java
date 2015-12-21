package com.szrjk.widget;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.PostDetail;
import com.szrjk.entity.UserCard;
import com.szrjk.util.DisplayTimeUtil;
import com.szrjk.util.SpannableStringUtils;


/**
 * 转发内容样式（包括名片) XXX//XXX:XXXX
 * 针对204的帖子
 */
public class NormalForwardPostContent2Layout extends RelativeLayout
{

	/****/
	private Context context;

	/**发帖人名片*/
//	private UserCard userCard;
//
//	private PostDetail postDetail;

	/****界面****/
	private LinearLayout ll_case_detail_list;

	private UserCardLayout userCardLayout;

	/****/
	private RelativeLayout rl_post_normal;


	private LinearLayout ll_normal_post_content;

	private RelativeLayout rl_post_detailed_doctorinfo;

	private TextView tv_post_detailed_time;


	public void setPostDetail(PostDetail postDetail) {
			String title1 = "";
			CharSequence postContent = postDetail.getContent();
		    String pusername = postDetail.getpUserName();
			String pcontent = postDetail.getpContent();
		    final String puserid = postDetail.getpUserSeqId();
		//时间
		//填时间
		String createDate = postDetail.getCreateDate();
		try {
			createDate = DisplayTimeUtil.displayTimeString(createDate);
		} catch (Exception e) {
			createDate = "XX";
		}
		tv_post_detailed_time.setText(createDate);


			if(pusername!=null&&!pusername.equals("")){
				//存在pusername,搞链接
				String targetstr = postContent+" //"+pusername+":"+pcontent;
				final String currentuserid = Constant.userInfo.getUserSeqId();
				int start = postContent.length();
				int stop = postContent.length()+pusername.length();
				start+=3;
				stop+=3;
				SpannableString postContent1 = SpannableStringUtils.getClickableFaceSpan(context,targetstr,start,stop,puserid,currentuserid);

				PostDetailCaseView pdcv1 = new PostDetailCaseView(context,title1,postContent1,null);
				ll_case_detail_list.addView(pdcv1);
			}else{
				ll_case_detail_list.removeAllViews();
				PostDetailCaseView pdcv1 = new PostDetailCaseView(context,title1,postContent,null);
				ll_case_detail_list.addView(pdcv1);
			}

	}

	public void setUserCard(UserCard userCard) {
		userCardLayout.setUser(userCard);
	}


//	/**
//	 * 设置背景颜色
//	 * @param colorid
//	 */
//	public void setBackgroundColor(int colorid){
//		ll_normal_post_content.setBackgroundColor(colorid);
//		rl_post_detailed_doctorinfo.setBackgroundColor(colorid);
//		tv_post_detailed_time.setBackgroundColor(colorid);
//		userCardLayout.setBackgroundColor(colorid);
//	}


	public NormalForwardPostContent2Layout(Context context)
	{
		super(context);
	}

	public NormalForwardPostContent2Layout(final Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contextView = inflater.inflate(R.layout.layout_normal_foward_post_content2, this);

		ll_case_detail_list = (LinearLayout) contextView.findViewById(R.id.ll_case_detail_list);
		userCardLayout = (UserCardLayout) contextView.findViewById(R.id.ucl_post);
		ll_normal_post_content = (LinearLayout) contextView.findViewById(R.id.ll_normal_post_content);
		rl_post_detailed_doctorinfo = (RelativeLayout) contextView.findViewById(R.id.rl_post_detailed_doctorinfo);
		tv_post_detailed_time = (TextView) contextView.findViewById(R.id.tv_post_detailed_time);
		rl_post_normal = (RelativeLayout) contextView.findViewById(R.id.rl_post_normal);
	}

}
