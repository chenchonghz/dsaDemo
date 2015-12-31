package com.szrjk.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.dhome.SelfActivity;
import com.szrjk.entity.ICallback;
import com.szrjk.entity.IPullPostListCallback;
import com.szrjk.entity.PostAbstractList;
import com.szrjk.entity.PostDetail;
import com.szrjk.entity.UserCard;
import com.szrjk.index.CaseDetailActivity;
import com.szrjk.index.PostDetailActivity;
import com.szrjk.self.CircleHomepageActivity;
import com.szrjk.simplifyspan.SimplifySpanBuild;
import com.szrjk.util.DisplayTimeUtil;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.util.UserNameJumpUtil;

import java.util.Map;


/**
 * 帖子 转发的内容样式（包括名片),里面自己区分 普通，病历分享
 */
public class PostContentForwardLayout extends RelativeLayout
{

	/****/
	private Context context;

	/**发帖人名片*/
//	private UserCard userCard;
//
//	private PostDetail postDetail;

	/****界面****/
	private LinearLayout ll_case_detail_list;

//	private UserCardLayout userCardLayout;

	/**普通贴子的 转发layout**/
	private RelativeLayout rl_post_foward_normal;


	private LinearLayout ll_normal_post_content;
//	private RelativeLayout rl_post_detailed_doctorinfo;
//	private TextView tv_post_detailed_time;
	private LinearLayout ll_post_content_left;
	/***以下为转发时，case 类型的样式***/
	private RelativeLayout rl_post_foward_case;
	private TextView tv_uname;
	private ImageView iv_casepic;
	private TextView tv_posttitle;
	private TextView tv_caseshare;
	private TextView tv_completerate;

	PostDetailCaseView pdcv1;

	PostAbstractList ppostAbstractInfo;
	LinearLayout ll_group;

	TextView tv_group_name;

	public void setUserCard(UserCard userCard) {
//		userCardLayout.setUser(userCard);
	}

	public void setPostDetail(PostAbstractList postAbstractInfo) {

		String postType = postAbstractInfo.getPostAbstract().getPostType();
		this.ppostAbstractInfo = postAbstractInfo;

		ll_case_detail_list.removeAllViews();
		if(postAbstractInfo.getIsDelete()!=null&&postAbstractInfo.getIsDelete().equals("true")){
			//已经被删除了
			rl_post_foward_normal.setVisibility(View.VISIBLE);

			String title1 = "";
			String postContent = "抱歉，此帖子已被作者删除";
			pdcv1 = new PostDetailCaseView(context,title1,postContent,null);
			ll_case_detail_list.addView(pdcv1);
			return;
		}

		final String postid = postAbstractInfo.getPostAbstract().getPostId();
		final String userSeqId = postAbstractInfo.getPostAbstract().getUserSeqId();

		if(postType.equals(Constant.NORMAL_POST)){
			//普通贴子
			rl_post_foward_normal.setVisibility(View.VISIBLE);

			String title1 = "";
			String postContent = postAbstractInfo.getPostAbstract().getContent();
			String caseUrl = postAbstractInfo.getPostAbstract().getPicListstr();

			pdcv1 = new PostDetailCaseView(context, title1, postAbstractInfo, postContent, caseUrl);
			ll_case_detail_list.addView(pdcv1);

		}else if(postType.equals(Constant.CIRCLE_POST)){
			//圈子帖子
			rl_post_foward_normal.setVisibility(View.VISIBLE);
			String title1 = "";
			String postContent = postAbstractInfo.getPostAbstract().getContent();
			String caseUrl = postAbstractInfo.getPostAbstract().getPicListstr();

			pdcv1 = new PostDetailCaseView(context, title1, postAbstractInfo, postContent, caseUrl);
			ll_case_detail_list.addView(pdcv1);

			ll_group.setVisibility(View.VISIBLE);
			tv_group_name.setText(postAbstractInfo.getPostAbstract().getCoterieName());
			tv_group_name.setBackgroundColor(context.getResources().getColor(R.color.base_bg));
			
			//点击圈子 名称的跳转
			final String coterieid = postAbstractInfo.getPostAbstract().getCoterieId();
			tv_group_name.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(context, CircleHomepageActivity.class);
					intent.putExtra(CircleHomepageActivity.intent_param_circle_id, coterieid);
					context.startActivity(intent);
				}
			});
		}else{
			//病例分享
			rl_post_foward_case.setVisibility(View.VISIBLE);
			String postUserName = postAbstractInfo.getUserCard().getUserName();
			String userLevel = postAbstractInfo.getUserCard().getUserLevel();
			String userType = postAbstractInfo.getUserCard().getUserType();
			
			SimplifySpanBuild simplifySpanBuild = UserNameJumpUtil.getContentText(context, tv_uname, null, postUserName, userSeqId, userLevel, userType, postAbstractInfo.getPostAbstract(), 0, new IPullPostListCallback() {
				@Override
				public void skipToSelfFragment() {
					Intent intent=new Intent(context, SelfActivity.class);
					context.startActivity(intent);
				}
			});
			tv_uname.setText(simplifySpanBuild.build());
			String picurl = postAbstractInfo.getPostAbstract().getBackgroundPic();
			String posttitle = postAbstractInfo.getPostAbstract().getPostTitle();
			final String caseShare = postType.equals(Constant.CASE_SHARE)?"病例分享":"疑难求助";

			if(postType.equals(Constant.CASE_SHARE)){
				ll_post_content_left.setBackgroundColor(getResources().getColor(R.color.post_type_caseshare));
			}else{
				ll_post_content_left.setBackgroundColor(getResources().getColor(R.color.post_type_problemhelp));
			}

			String completerate = postAbstractInfo.getPostAbstract().getCompleteRate();

			/**设值**/
			try {
				ImageLoaderUtil imageloader = new ImageLoaderUtil(context, picurl, iv_casepic, R.drawable.pic_post_bg, R.drawable.pic_downloadfailed_bg);
				imageloader.showImage();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("ImageLoader", e.toString());
			}
			tv_posttitle.setText(posttitle);
			tv_caseshare.setText(caseShare);
			tv_completerate.setText("完整度"+completerate+"%");

			//病例分享等绑定动作
			iv_casepic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					//跳到
					Intent intent = new Intent(context, CaseDetailActivity.class);
					intent.putExtra(Constant.POST_ID, postid);
					intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
//					intent.putExtra("ptype", Constant.CASE_SHARE);
					context.startActivity(intent);
				}
			});
		}

		//填时间
//		String createDate = postAbstractInfo.getPostAbstract().getCreateDate();
//		try {
//			createDate = DisplayTimeUtil.displayTimeString(createDate);
//		} catch (Exception e) {
//			createDate = "XX";
//		}
//		tv_post_detailed_time.setText(createDate);
	}


	/**
	 * 设置背景颜色
	 * @param colorid
	 */
	@Override
	public void setBackgroundColor(int colorid){
//		ll_normal_post_content.setBackgroundColor(colorid);
//		rl_post_detailed_doctorinfo.setBackgroundColor(colorid);
//		tv_post_detailed_time.setBackgroundColor(colorid);
		ll_group.setBackgroundColor(colorid);
//		userCardLayout.setBackgroundColor(colorid);

		if(pdcv1!=null){
			pdcv1.contentClick( new ICallback() {
				@Override
				public void doCallback(Map m) {
					final String postid = ppostAbstractInfo.getPostAbstract().getPostId();
					final String userSeqId = ppostAbstractInfo.getPostAbstract().getUserSeqId();

					Intent intent = new Intent(context, PostDetailActivity.class);
					intent.putExtra(Constant.POST_ID, postid);
					intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
					context.startActivity(intent);
				}
			});
		}
	}


	public PostContentForwardLayout(Context context)
	{
		super(context);
	}

	public PostContentForwardLayout(final Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contextView = inflater.inflate(R.layout.layout_post_forward_content, this);

		ll_case_detail_list = (LinearLayout) contextView.findViewById(R.id.ll_case_detail_list);
//		userCardLayout = (UserCardLayout) contextView.findViewById(R.id.ucl_post);
		ll_normal_post_content = (LinearLayout) contextView.findViewById(R.id.ll_normal_post_content);
//		rl_post_detailed_doctorinfo = (RelativeLayout) contextView.findViewById(R.id.rl_post_detailed_doctorinfo);
//		tv_post_detailed_time = (TextView) contextView.findViewById(R.id.tv_post_detailed_time);

		rl_post_foward_normal = (RelativeLayout) contextView.findViewById(R.id.rl_post_foward_normal);
		rl_post_foward_case = (RelativeLayout) contextView.findViewById(R.id.rl_post_foward_case);


		tv_uname = (TextView) contextView.findViewById(R.id.tv_uname);
		iv_casepic = (ImageView) contextView.findViewById(R.id.iv_casepic);
		tv_posttitle = (TextView) contextView.findViewById(R.id.tv_posttitle);
		tv_caseshare = (TextView) contextView.findViewById(R.id.tv_caseshare);
		tv_completerate = (TextView) contextView.findViewById(R.id.tv_completerate);

		ll_post_content_left = (LinearLayout) contextView.findViewById(R.id.ll_post_content_left);

		ll_group = (LinearLayout) contextView.findViewById(R.id.ll_group);
		tv_group_name = (TextView) contextView.findViewById(R.id.tv_group_name);
		ll_group.setVisibility(View.GONE);

	}

}
