package com.szrjk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.szrjk.dhome.R;
import com.szrjk.entity.PostDetail;
import com.szrjk.entity.UserCard;
import com.szrjk.util.DisplayTimeUtil;


/**
 * 普通帖子 内容样式（包括名片)
 */
public class NormalPostContentLayout extends RelativeLayout
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

			//普通贴子
			String title1 = "";
			String postContent = postDetail.getContent();
			String caseUrl = postDetail.getPicList();

			PostDetailCaseView pdcv1 = new PostDetailCaseView(context,title1,postContent,caseUrl);
			ll_case_detail_list.addView(pdcv1);

		String date = postDetail.getCreateDate();
		try {
			date=DisplayTimeUtil.displayTimeString(date);
		} catch (Exception e) {
			date="";
		}
		tv_post_detailed_time.setText(date);
	}

	public void setUserCard(UserCard userCard) {
		userCardLayout.setUser(userCard);
	}


	/**
	 * 设置背景颜色
	 * @param colorid
	 */
	public void setBackgroundColor(int colorid){
		ll_normal_post_content.setBackgroundColor(colorid);
		rl_post_detailed_doctorinfo.setBackgroundColor(colorid);
		tv_post_detailed_time.setBackgroundColor(colorid);
		userCardLayout.setBackgroundColor(colorid);
	}


	public NormalPostContentLayout(Context context)
	{
		super(context);
	}

	public NormalPostContentLayout(final Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contextView = inflater.inflate(R.layout.layout_normal_post_content, this);

		ll_case_detail_list = (LinearLayout) contextView.findViewById(R.id.ll_case_detail_list);
		userCardLayout = (UserCardLayout) contextView.findViewById(R.id.ucl_post);
		ll_normal_post_content = (LinearLayout) contextView.findViewById(R.id.ll_normal_post_content);
		rl_post_detailed_doctorinfo = (RelativeLayout) contextView.findViewById(R.id.rl_post_detailed_doctorinfo);
		tv_post_detailed_time = (TextView) contextView.findViewById(R.id.tv_post_detailed_time);

		rl_post_normal = (RelativeLayout) contextView.findViewById(R.id.rl_post_normal);


	}

}
