package com.szrjk.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.config.Constant;
import com.szrjk.dhome.IndexFragment;
import com.szrjk.dhome.IndexGalleryActivity;
import com.szrjk.dhome.OtherPeopleActivity;
import com.szrjk.dhome.R;
import com.szrjk.dhome.ShowBigPicActivity;
import com.szrjk.entity.CustomLinkMovementMethod;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.IPhotoClickOper;
import com.szrjk.entity.IPullPostListCallback;
import com.szrjk.entity.InitSrcPostInterface;
import com.szrjk.entity.PostAbstractList;
import com.szrjk.entity.PostInfo;
import com.szrjk.entity.PostOtherImformationInfo;
import com.szrjk.entity.RecommendUserList;
import com.szrjk.entity.SrcPostInfo;
import com.szrjk.entity.SrcUserCard;
import com.szrjk.entity.UserCard;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.index.CaseDetailActivity;
import com.szrjk.index.PostDetailActivity;
import com.szrjk.index.PostDetailFowardActivity;
import com.szrjk.index.RepeatActivity;
import com.szrjk.self.CircleHomepageActivity;
import com.szrjk.self.SystemUserActivity;
import com.szrjk.simplifyspan.SimplifySpanBuild;
import com.szrjk.simplifyspan.other.OnClickableSpanListener;
import com.szrjk.simplifyspan.other.SpecialGravity;
import com.szrjk.simplifyspan.unit.SpecialClickableUnit;
import com.szrjk.simplifyspan.unit.SpecialImageUnit;
import com.szrjk.simplifyspan.unit.SpecialLabelUnit;
import com.szrjk.simplifyspan.unit.SpecialTextUnit;
import com.szrjk.util.BusiUtils;
import com.szrjk.util.DialogUtil;
import com.szrjk.util.DisplayTimeUtil;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.util.InitTransmitPostUtil;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.IndexGridView;
import com.szrjk.widget.TransmitTextView;
import com.szrjk.widget.IndexGridView.OnTouchInvalidPositionListener;

public class IndexListViewAdapter extends BaseAdapter implements Serializable{
	private Context context;
	private Activity mainActivity;
	private Fragment indexFragment;
	private IPullPostListCallback iPullPostListCallback;
	public  ArrayList<UserCard> userList;
	public  ArrayList<PostInfo> postList;
	private ArrayList<PostOtherImformationInfo> postOtherList;
	private ArrayList<UserCard> recommendUserList;
	private ListView recommendListView;
	private RecommendListAdapter recommend_adapter;
	private String userId;
	private Boolean isLike = false;
	private ImageLoaderUtil imageloader;
	private String lastUserId;
	private boolean isTourist;
	private int flag;
//	private String t_srcPostId;
//	private String t_postLevel;
//	private String t_postContent;
//	private static final int COMMENT_REQUEST_CODE = 100;
//	private static final int TRAMSMIT_REQUEST_CODE = 200;
	ViewHolder caseShare_Holder;
	ViewHolder2 problemHelp_Holder;
	ViewHolder3 normalPost_Holder;
	ViewHolder4 tran_normalPost_Holder;
	ViewHolder5 tran_caseShare_Holder;
	ViewHolder6 tran_problemHelp_Holder;
	ViewHolder7 recommend_user_Holder;
	ViewHolder8 viewHolder8;
	private Handler hander = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			notifyDataSetChanged();
			hander.sendEmptyMessageDelayed(0, 60000);
		};
	};

	public IndexListViewAdapter(Context context,
			Activity mainActivity, Fragment indexFragment,ArrayList<UserCard> userList, ArrayList<PostInfo> postList,
			ArrayList<PostOtherImformationInfo> postOtherList, String userId,int flag,IPullPostListCallback iPullPostListCallback) {
		this.context = context;
		this.mainActivity =  mainActivity;
		this.indexFragment = indexFragment;
		this.iPullPostListCallback = iPullPostListCallback;
		this.userId = userId;
		if(userList != null){
			this.userList = userList;
		}else{
			this.userList = new ArrayList<UserCard>();
		}
		if(postList != null){
			this.postList = postList;
		}else{
			this.postList = new ArrayList<PostInfo>();
		}
		if(postOtherList != null){
			this.postOtherList = postOtherList;
		}else{
			this.postOtherList = new ArrayList<PostOtherImformationInfo>();
		}
		this.flag = flag;
		isTourist = BusiUtils.isguest(context);
		Log.e("IndexListViewAdapter", "是否游客："+isTourist);
		hander.sendEmptyMessageDelayed(0, 60000);

	}
	public IndexListViewAdapter(Context context,
			Activity mainActivity,ArrayList<UserCard> userList, ArrayList<PostInfo> postList,
			ArrayList<PostOtherImformationInfo> postOtherList, String userId,int flag,IPullPostListCallback iPullPostListCallback) {
		this.context = context;
		this.mainActivity =  mainActivity;
		this.iPullPostListCallback = iPullPostListCallback;
		this.userId = userId;
		if(userList != null){
			this.userList = userList;
		}else{
			this.userList = new ArrayList<UserCard>();
		}
		if(postList != null){
			this.postList = postList;
		}else{
			this.postList = new ArrayList<PostInfo>();
		}
		if(postOtherList != null){
			this.postOtherList = postOtherList;
		}else{
			this.postOtherList = new ArrayList<PostOtherImformationInfo>();
		}
		this.flag = flag;
		isTourist = BusiUtils.isguest(context);
		Log.e("IndexListViewAdapter", "是否游客："+isTourist);
		hander.sendEmptyMessageDelayed(0, 60000);
	}
	

	@Override
	public int getCount() {
		return userList.size();
	}

	@Override
	public Object getItem(int position) {
		return userList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		PostInfo postInfo = postList.get(position);
		if(postInfo.getPostType().equals(Constant.CASE_SHARE)){
			return 0;
		}else if(postInfo.getPostType().equals(Constant.PROBLEM_HELP)){
			return 1;
		}else if(postInfo.getPostType().equals(Constant.NORMAL_POST)||postInfo.getPostType().equals(Constant.CIRCLE_POST)){
			return 2;
		}else if((postInfo.getPostType().equals(Constant.TRANSMIT_POST)&&postInfo.getSrcPostAbstractCard().getPostType().equals(Constant.NORMAL_POST))||(postInfo.getPostType().equals(Constant.TRANSMIT_POST)&&postInfo.getSrcPostAbstractCard().getPostType().equals(Constant.CIRCLE_POST))){
			return 3;
		}else if(postInfo.getPostType().equals(Constant.TRANSMIT_POST)&&postInfo.getSrcPostAbstractCard().getPostType().equals(Constant.CASE_SHARE)){
			return 4;
		}else if(postInfo.getPostType().equals(Constant.TRANSMIT_POST)&&postInfo.getSrcPostAbstractCard().getPostType().equals(Constant.PROBLEM_HELP)){
			return 5;
		}else if(postInfo.getPostType().equals(Constant.RECOMMEND_USER)){
			return 6;
		}else if(postInfo.getPostType().equals(Constant.TRANSMIT_POST2)){
			List<PostAbstractList> postAbList = postInfo.getPostAbstractList();
			if(postAbList != null && !postAbList.isEmpty()){
				for (PostAbstractList postAbstractList : postAbList) {
					if(postAbstractList.getPostLevel().equals("0")&&(postAbstractList.getPostAbstract().getPostType().equals(Constant.NORMAL_POST)||postAbstractList.getPostAbstract().getPostType().equals(Constant.CIRCLE_POST))){
						return 3;
					}else if(postAbstractList.getPostLevel().equals("0")&&postAbstractList.getPostAbstract().getPostType().equals(Constant.CASE_SHARE)){
						return 4;
					}else if(postAbstractList.getPostLevel().equals("0")&&postAbstractList.getPostAbstract().getPostType().equals(Constant.PROBLEM_HELP)){
						return 5;
					}
				}
			}
			return 7;
		}else{
			return 7;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 8;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Log.e("IndexViewAdapter", postOtherList.toString());
		final UserCard userInfo = userList.get(position);
		final PostInfo postInfo = postList.get(position);
		final PostOtherImformationInfo postOtherInfo = postOtherList.get(position);
		if(postOtherInfo != null){
			isLike = postOtherInfo.isMineLike();
		}
		int type = getItemViewType(position);
		Log.e("IndexViewAdapter", "帖子类型："+type);
//		ViewHolder caseShare_Holder = null;
//		ViewHolder2 problemHelp_Holder = null;
//		ViewHolder3 normalPost_Holder = null;
//		ViewHolder4 tran_normalPost_Holder = null;
//		ViewHolder5 tran_caseShare_Holder = null;
//		ViewHolder6 tran_problemHelp_Holder = null;
//		ViewHolder7 recommend_user_Holder = null;
//		ViewHolder8 viewHolder8 = null;
		if(convertView == null){
			switch (type) {
			case 0:
				convertView = View.inflate(context, R.layout.item_casesharepost, null);
				caseShare_Holder = new ViewHolder();
				Log.e("IndexViewAdapter", "病例分享帖子加载布局");
				initDoctorInfoView(convertView,caseShare_Holder);
				initPostInfoView(convertView,caseShare_Holder);
				initOtherInfoView(convertView,caseShare_Holder);
			    caseShare_Holder.ll_caseshare_post = (LinearLayout)convertView.findViewById(R.id.ll_caseshare_post);
				convertView.setTag(R.id.tag_caseshare, caseShare_Holder);
				break;
			case 1:
				convertView = View.inflate(context, R.layout.item_problemhelppost, null);
				problemHelp_Holder = new ViewHolder2();
				Log.e("IndexViewAdapter", "疑难求助帖子加载布局");
				initDoctorInfoView(convertView,problemHelp_Holder);
				initPostInfoView(convertView,problemHelp_Holder);
				initOtherInfoView(convertView,problemHelp_Holder);
				convertView.setTag(R.id.tag_problemhelp, problemHelp_Holder);
				break;
			case 2:
				convertView = View.inflate(context, R.layout.item_normalpost, null);
				normalPost_Holder = new ViewHolder3();
				Log.e("IndexViewAdapter", "普通帖子加载布局");
				initGroupView(convertView,normalPost_Holder);
				initDoctorInfoView(convertView,normalPost_Holder);
				initPostInfoView(convertView,normalPost_Holder);
				initOtherInfoView(convertView,normalPost_Holder);
				normalPost_Holder.ll_normal_post = (LinearLayout)convertView.findViewById(R.id.ll_normal_post);
				convertView.setTag(R.id.tag_normalpost, normalPost_Holder);
				break;
			case 3:
				convertView = View.inflate(context, R.layout.item_transmit_normal_post, null);
				tran_normalPost_Holder = new ViewHolder4();
				Log.e("IndexViewAdapter", "转发普通帖子加载布局");
				initDoctorInfoView(convertView, tran_normalPost_Holder);
				initPostInfoView(convertView, tran_normalPost_Holder);
				initOtherInfoView(convertView, tran_normalPost_Holder);
				initSrcGroupView(convertView,tran_normalPost_Holder);
				initSrcDoctorInfoView(convertView, tran_normalPost_Holder);
				tran_normalPost_Holder.tv_src_post_text = (TextView)convertView.findViewById(R.id.tv_src_post_text);
				tran_normalPost_Holder.ll_src_normal_post = (LinearLayout)convertView.findViewById(R.id.view_src_normal_post);
				tran_normalPost_Holder.gv_src_pic= (IndexGridView)convertView.findViewById(R.id.gv_src_pic);
				convertView.setTag(R.id.tag_transmit_normalpost, tran_normalPost_Holder);
				break;	
			case 4:
				convertView = View.inflate(context, R.layout.item_transmit_caseshare_post, null);
				tran_caseShare_Holder = new ViewHolder5();
				Log.e("IndexViewAdapter", "转发病例分享帖子加载布局");
				initDoctorInfoView(convertView, tran_caseShare_Holder);
				initPostInfoView(convertView, tran_caseShare_Holder);
				initOtherInfoView(convertView, tran_caseShare_Holder);
				initSrcDoctorInfoView(convertView, tran_caseShare_Holder);
				initSrcPostInfoView(convertView,tran_caseShare_Holder);
				convertView.setTag(R.id.tag_transmit_caseshare, tran_caseShare_Holder);
				break;
			case 5:
				convertView = View.inflate(context, R.layout.item_transmit_problemhelp_post, null);
				Log.e("IndexViewAdapter", "转发疑难求助帖子加载布局");
				tran_problemHelp_Holder = new ViewHolder6();
				initDoctorInfoView(convertView, tran_problemHelp_Holder);
				initPostInfoView(convertView, tran_problemHelp_Holder);
				initOtherInfoView(convertView, tran_problemHelp_Holder);
				initSrcDoctorInfoView(convertView, tran_problemHelp_Holder);
				initSrcPostInfoView(convertView,tran_problemHelp_Holder);
				convertView.setTag(R.id.tag_transmit_problemhelp, tran_problemHelp_Holder);
				break;
			case 6:
				convertView = View.inflate(context, R.layout.item_recommend_doctor, null);
				Log.e("IndexViewAdapter", "推荐好友加载布局");
				recommend_user_Holder = new ViewHolder7();
				recommend_user_Holder.lv_recommend_doctor = (ListView)convertView.findViewById(R.id.lv_recommend_doctor);
				recommend_user_Holder.bt_change = (Button)convertView.findViewById(R.id.bt_change);
				recommendListView = recommend_user_Holder.lv_recommend_doctor;
				convertView.setTag(R.id.tag_recommend_user, recommend_user_Holder);
				break;
			case 7:
				convertView = View.inflate(context, R.layout.item_unknow_type, null);
				viewHolder8 = new ViewHolder8();
				Log.e("IndexViewAdapter", "其他帖子加载布局");
				convertView.setTag(viewHolder8);
				break;
			}
			
		}else{
			switch (type) {
			case 0:
				caseShare_Holder = (ViewHolder)convertView.getTag(R.id.tag_caseshare);
				Log.e("IndexViewAdapter", "病例分享帖子getTag");
				Log.e("IndexViewAdapter", caseShare_Holder.toString());
				break;
			case 1:
				problemHelp_Holder = (ViewHolder2)convertView.getTag(R.id.tag_problemhelp);
				Log.e("IndexViewAdapter", "疑难求助帖子getTag");
				Log.e("IndexViewAdapter", problemHelp_Holder.toString());
				break;
			case 2:
				normalPost_Holder = (ViewHolder3)convertView.getTag(R.id.tag_normalpost);
				Log.e("IndexViewAdapter", "普通帖子getTag");
				break;
			case 3:
				tran_normalPost_Holder = (ViewHolder4)convertView.getTag(R.id.tag_transmit_normalpost);
				Log.e("IndexViewAdapter", "转发普通帖子getTag");
				break;
			case 4:
				tran_caseShare_Holder = (ViewHolder5)convertView.getTag(R.id.tag_transmit_caseshare);
				Log.e("IndexViewAdapter", "转发病例分享帖子getTag");
				break;
			case 5:
				tran_problemHelp_Holder = (ViewHolder6)convertView.getTag(R.id.tag_transmit_problemhelp);
				Log.e("IndexViewAdapter", "转发疑难求助帖子getTag");
				break;
			case 6:
				recommend_user_Holder = (ViewHolder7)convertView.getTag(R.id.tag_recommend_user);
				Log.e("IndexViewAdapter", "用户推荐getTag");
				break;
			case 7:
				viewHolder8 = (ViewHolder8)convertView.getTag();
				Log.e("IndexViewAdapter", "其他帖子getTag");
				break;
			}
		}
		switch (type) {
		case 0:
				Log.e("IndexViewAdapter", "病例分享帖子加载数据");
				if(userList.isEmpty()||postList.isEmpty()||postOtherList.isEmpty()){
					return convertView;
				}
				if(caseShare_Holder.iv_headphoto_icon != null){
					setHeadIcon(userInfo.getUserLevel(),caseShare_Holder.iv_headphoto_icon);
				}
				try {
					imageloader = new ImageLoaderUtil(context, userInfo.getUserFaceUrl(), caseShare_Holder.iv_smallPhoto, R.drawable.icon_headfailed, R.drawable.icon_headfailed);
					imageloader.showImage();
				} catch (Exception e) {
					Log.e("ImageLoader", e.toString());
				}
					
				if(userInfo.getUserName()!=null){
					caseShare_Holder.tv_doctorName.setText(userInfo.getUserName());
				}
				if(userInfo.getCompanyName()!=null){
					caseShare_Holder.tv_hospital.setText(userInfo.getCompanyName());
				}
				if(userInfo.getDeptName()!= null){
					caseShare_Holder.tv_department.setText(userInfo.getDeptName());
				}
				caseShare_Holder.tv_jobTitle.setText(userInfo.getProfessionalTitle());
				if(postInfo.getBackgroundPic()!= null){
					if(postInfo.getBackgroundPic().isEmpty()){
						caseShare_Holder.iv_gray.setVisibility(View.GONE);
					}
					try {
						imageloader = new ImageLoaderUtil(context, postInfo.getBackgroundPic(), caseShare_Holder.iv_backgroudpic, R.drawable.pic_post_bg, R.drawable.pic_downloadfailed_bg,caseShare_Holder.iv_gray);
						imageloader.showImage();
					} catch (Exception e) {
						Log.e("ImageLoader", e.toString());
					}
				}
				if(postInfo.getPostTitle()!=null){
					caseShare_Holder.tv_postTitle.setText(postInfo.getPostTitle());
				}
				if(postInfo.getCompleteRate()!=null){
					caseShare_Holder.tv_completeRate.setText("完整度"+postInfo.getCompleteRate()+"%");
				}
				if(postInfo.getCreateDate()!=null){
					try {
						caseShare_Holder.tv_time.setText(DisplayTimeUtil.displayTimeString(postInfo.getCreateDate()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(postOtherInfo.getCOMMENT_NUM()!=0){
					caseShare_Holder.tv_command.setText(String.valueOf(postOtherInfo.getCOMMENT_NUM()));
				}else{
					caseShare_Holder.tv_command.setText(context.getResources().getString(R.string.command_text));
				}
				if(postOtherInfo.getFORWARD_NUM()!=0){
					caseShare_Holder.tv_transmit.setText(""+postOtherInfo.getFORWARD_NUM());
				}else{
					caseShare_Holder.tv_transmit.setText(context.getResources().getString(R.string.transmit_text));
				}
				if(postOtherInfo.getLIKE_NUM()!=0){
					caseShare_Holder.tv_like.setText(""+postOtherInfo.getLIKE_NUM());	
				}else{
					caseShare_Holder.tv_like.setText(context.getResources().getString(R.string.like_text));
				}
				if(isLike){
					caseShare_Holder.iv_like.setImageResource(R.drawable.icon_laud_active_24);
				}else{
					caseShare_Holder.iv_like.setImageResource(R.drawable.icon_laud_24);
				}
			caseShare_Holder.ll_doctorInfo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{			
						if(userInfo.getUserSeqId().equals(Constant.userInfo.getUserSeqId())){
							skipToSelfFragment();
						}else if(userInfo.getUserType().equals("1")&& !userInfo.getUserSeqId().equals(userId)){
							skipToSystemUserActivity(userInfo.getUserSeqId());
						}else{
							if(!userInfo.getUserSeqId().equals(userId)){		
								skipToOtherPeopleActivity(userInfo.getUserSeqId());
							}
						}
					}
				}
			});
			caseShare_Holder.rl_command.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					if(isTourist){
//						DialogUtil.showGuestDialog(context, null);
//					}else{		
						skipToPostDetail(postInfo.getPostType(),postInfo.getPostId(),postInfo.getUserSeqId(),position);
//					}
				}
			});
			
			caseShare_Holder.rl_transmit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{
						skipToRepeatActivity(userInfo.getUserSeqId(),postInfo.getPostTitle(),
								userInfo.getUserFaceUrl(),postInfo.getPostId(),
								userInfo.getUserName(),postInfo.getPostType(),postInfo.getPostLevel()
								,postInfo.getSrcPostId(),position,postOtherInfo.getFORWARD_NUM(),flag);
					}
				}
			});

			caseShare_Holder.ll_caseshare_post.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					if(isTourist){
//						DialogUtil.showGuestDialog(context, null);
//					}else{	
						skipToPostDetail(postInfo.getPostType(),postInfo.getPostId(),postInfo.getUserSeqId(),position);
//					}
				}
			});
			final RelativeLayout like = caseShare_Holder.rl_like;
			like.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{			
						clickLike(position,like,postInfo.getUserSeqId(),postInfo.getPostId(),postOtherInfo.isMineLike());
					}
				}
			});
			break;
		case 1:
				Log.e("IndexViewAdapter", "疑难求助病例分享帖子加载数据");
				if(userList.isEmpty()||postList.isEmpty()||postOtherList.isEmpty()){
					return convertView;
				}
				if(problemHelp_Holder.iv_headphoto_icon != null){
					setHeadIcon(userInfo.getUserLevel(),problemHelp_Holder.iv_headphoto_icon);
				}
				if(userInfo.getUserFaceUrl()!=null){
					try {
						imageloader = new ImageLoaderUtil(context, userInfo.getUserFaceUrl(), problemHelp_Holder.iv_smallPhoto, R.drawable.icon_headfailed, R.drawable.icon_headfailed);
						imageloader.showImage();
					} catch (Exception e) {
						Log.e("ImageLoader", e.toString());
					}
				}
				if(userInfo.getUserName()!=null){
					problemHelp_Holder.tv_doctorName.setText(userInfo.getUserName());
				}
				if(userInfo.getCompanyName()!=null){
					problemHelp_Holder.tv_hospital.setText(userInfo.getCompanyName());
				}
				if(userInfo.getDeptName()!= null){
					problemHelp_Holder.tv_department.setText(userInfo.getDeptName());
				}
				problemHelp_Holder.tv_jobTitle.setText(userInfo.getProfessionalTitle());
				if(postInfo.getBackgroundPic()!= null){
					if(postInfo.getBackgroundPic().isEmpty()){
						problemHelp_Holder.iv_gray.setVisibility(View.GONE);
					}
					try {
						imageloader = new ImageLoaderUtil(context, postInfo.getBackgroundPic(), problemHelp_Holder.iv_backgroudpic, R.drawable.pic_post_bg, R.drawable.pic_downloadfailed_bg,problemHelp_Holder.iv_gray);
						imageloader.showImage();
					} catch (Exception e) {
						Log.e("ImageLoader", e.toString());
					}
				}
				if(postInfo.getPostTitle()!=null){
					problemHelp_Holder.tv_postTitle.setText(postInfo.getPostTitle());
				}
				if(postInfo.getCompleteRate()!=null){
					problemHelp_Holder.tv_completeRate.setText("完整度"+postInfo.getCompleteRate()+"%");
				}
				if(postInfo.getCreateDate()!=null){
					try {
						problemHelp_Holder.tv_time.setText(DisplayTimeUtil.displayTimeString(postInfo.getCreateDate()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(postOtherInfo!=null&&postOtherInfo.getCOMMENT_NUM()!=0){
					problemHelp_Holder.tv_command.setText(""+String.valueOf(postOtherInfo.getCOMMENT_NUM()));
				}else{
					problemHelp_Holder.tv_command.setText(context.getResources().getString(R.string.command_text));
				}
				if(postOtherInfo!=null&&postOtherInfo.getFORWARD_NUM()!=0){
					problemHelp_Holder.tv_transmit.setText(""+postOtherInfo.getFORWARD_NUM());
				}else{
					problemHelp_Holder.tv_transmit.setText(context.getResources().getString(R.string.transmit_text));
				}
				if(postOtherInfo!=null&&postOtherInfo.getLIKE_NUM()!=0){
					problemHelp_Holder.tv_like.setText(""+postOtherInfo.getLIKE_NUM());	
				}else{
					problemHelp_Holder.tv_like.setText(context.getResources().getString(R.string.like_text));
				}
				if(isLike){
					problemHelp_Holder.iv_like.setImageResource(R.drawable.icon_laud_active_24);
				}else{
					problemHelp_Holder.iv_like.setImageResource(R.drawable.icon_laud_24);
				}
			problemHelp_Holder.ll_doctorInfo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{			
						if(userInfo.getUserSeqId().equals(Constant.userInfo.getUserSeqId())){
							skipToSelfFragment();
						}else if(userInfo.getUserType().equals("1")&&!userInfo.getUserSeqId().equals(userId)){
							skipToSystemUserActivity(userInfo.getUserSeqId());
						}else{
							if(!userInfo.getUserSeqId().equals(userId)){		
								skipToOtherPeopleActivity(userInfo.getUserSeqId());
							}
						}
					}
				}
			});
			problemHelp_Holder.rl_command.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					if(isTourist){
//						DialogUtil.showGuestDialog(context, null);
//					}else{						
						skipToPostDetail(postInfo.getPostType(),postInfo.getPostId(),postInfo.getUserSeqId(),position);
//					}
				}
			});
			
			problemHelp_Holder.rl_transmit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{
						skipToRepeatActivity(userInfo.getUserSeqId(),postInfo.getPostTitle(),
								userInfo.getUserFaceUrl(),postInfo.getPostId(),
								userInfo.getUserName(),postInfo.getPostType(),postInfo.getPostLevel()
								,postInfo.getSrcPostId(),position, postOtherInfo.getFORWARD_NUM(),flag);
					}
				}
			});

			problemHelp_Holder.rl_view_post.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					if(isTourist){
//						DialogUtil.showGuestDialog(context, null);
//					}else{
						skipToPostDetail(postInfo.getPostType(),postInfo.getPostId(),postInfo.getUserSeqId(),position);
//					}
				}
			});
			final RelativeLayout like2 = problemHelp_Holder.rl_like;
			like2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{	
						clickLike(position,like2,postInfo.getUserSeqId(),postInfo.getPostId(),postOtherInfo.isMineLike());
					}
				}
			});
			break;
		case 2:
				Log.e("IndexViewAdapter", "普通帖子加载数据");
				if(userList.isEmpty()||postList.isEmpty()||postOtherList.isEmpty()){
					return convertView;
				}
				if(postInfo.getPostType().equals(Constant.CIRCLE_POST)){
					normalPost_Holder.ll_group.setVisibility(View.VISIBLE);
					normalPost_Holder.tv_group_name.setText(postInfo.getCoterieName());
				}else{
					normalPost_Holder.ll_group.setVisibility(View.GONE);
				}
				if(normalPost_Holder.iv_headphoto_icon != null){	
					setHeadIcon(userInfo.getUserLevel(),normalPost_Holder.iv_headphoto_icon);
				}
				if(userInfo.getUserFaceUrl()!=null){
					try {
						imageloader = new ImageLoaderUtil(context, userInfo.getUserFaceUrl(), normalPost_Holder.iv_smallPhoto, R.drawable.icon_headfailed, R.drawable.icon_headfailed);
						imageloader.showImage();
					} catch (Exception e) {
						Log.e("ImageLoader", e.toString());
					}
				}
				if(userInfo.getUserName()!=null){
					normalPost_Holder.tv_doctorName.setText(userInfo.getUserName());
				}
				if(userInfo.getCompanyName()!=null){
					normalPost_Holder.tv_hospital.setText(userInfo.getCompanyName());
				}
				if(userInfo.getDeptName()!= null){
					normalPost_Holder.tv_department.setText(userInfo.getDeptName());
				}
				if(userInfo.getProfessionalTitle()!= null){
					normalPost_Holder.tv_jobTitle.setText(userInfo.getProfessionalTitle());
				}
				if(postInfo.getContent()!= null){
					normalPost_Holder.tv_post_text.setText(postInfo.getContent());
				}
				if(postInfo.getCreateDate()!=null){
					try {
						normalPost_Holder.tv_time.setText(DisplayTimeUtil.displayTimeString(postInfo.getCreateDate()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(postOtherInfo!=null&&postOtherInfo.getCOMMENT_NUM()!=0){
					normalPost_Holder.tv_command.setText(""+postOtherInfo.getCOMMENT_NUM());
				}else{
					normalPost_Holder.tv_command.setText(context.getResources().getString(R.string.command_text));
				}
				if(postOtherInfo!=null&&postOtherInfo.getFORWARD_NUM()!=0){
					normalPost_Holder.tv_transmit.setText(""+postOtherInfo.getFORWARD_NUM());
				}else{
					normalPost_Holder.tv_transmit.setText(context.getResources().getString(R.string.transmit_text));
				}
				if(postOtherInfo!=null&&postOtherInfo.getLIKE_NUM()!=0){
					normalPost_Holder.tv_like.setText(""+postOtherInfo.getLIKE_NUM());	
				}else{
					normalPost_Holder.tv_like.setText(context.getResources().getString(R.string.like_text));
				}
				if(postInfo.getPicList()!=null&&!postInfo.getPicList()[0].isEmpty()){
					int screenWidth = getWindowsWidth();
					if(postInfo.getPicList().length<3){
						normalPost_Holder.gv_pic.setNumColumns(2);
					}else{
						normalPost_Holder.gv_pic.setNumColumns(3);
					}
					IndexPhotoGridViewAdapter gridViewAdapter = new IndexPhotoGridViewAdapter(context, postInfo.getPicList(),screenWidth,new IPhotoClickOper() {
						
						@Override
						public void clickoper(int position,Context context) {
							Intent intent = new Intent(context,IndexGalleryActivity.class);
							intent.putExtra("position", position);
							intent.putExtra("imgs", postInfo.getPicList());
							intent.putExtra("title", (position+1)+"/"+postInfo.getPicList().length);			
							intent.putExtra("needOper", false);
							intent.putExtra("contextText", "");
							context.startActivity(intent);
						}
					});
					normalPost_Holder.gv_pic.setAdapter(gridViewAdapter);
					normalPost_Holder.gv_pic.setVisibility(View.VISIBLE);
				}else{
					normalPost_Holder.gv_pic.setVisibility(View.GONE);
				}
				if(isLike){
					normalPost_Holder.iv_like.setImageResource(R.drawable.icon_laud_active_24);
				}else{
					normalPost_Holder.iv_like.setImageResource(R.drawable.icon_laud_24);
				}
				

				normalPost_Holder.tv_group_name.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(isTourist){
							DialogUtil.showGuestDialog(context, null);
						}else{						
							Intent intent = new Intent(context, CircleHomepageActivity.class);
							intent.putExtra(CircleHomepageActivity.intent_param_circle_id, postInfo.getCoterieId());
							context.startActivity(intent);
						}
					}
				});

			normalPost_Holder.ll_doctorInfo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{			
						if(userInfo.getUserSeqId().equals(Constant.userInfo.getUserSeqId())){
							skipToSelfFragment();
						}else if(userInfo.getUserType().equals("1")&&!userInfo.getUserSeqId().equals(userId)){
							skipToSystemUserActivity(userInfo.getUserSeqId());
						}else{
							if(!userInfo.getUserSeqId().equals(userId)){		
								skipToOtherPeopleActivity(userInfo.getUserSeqId());
							}
						}
					}
				}
			});
			final RelativeLayout transmit = normalPost_Holder.rl_transmit;
			transmit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{						
						if(postInfo.getIsOpen() == 2){
							ToastUtils.showMessage(context, "私密圈子不可转发！");
						}else{
							skipToRepeatActivity(userInfo.getUserSeqId(), postInfo.getContent(),
									userInfo.getUserFaceUrl(), postInfo.getPostId(),
									userInfo.getUserName(), postInfo.getPostType(),postInfo.getPostLevel()
									,postInfo.getSrcPostId(),position,postOtherInfo.getFORWARD_NUM(),flag);
						}
					}
				}
			});
			normalPost_Holder.rl_command.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					if(isTourist){
//						DialogUtil.showGuestDialog(context, null);
//					}else{			
						skipToPostDetail(postInfo.getPostType(),postInfo.getPostId(),postInfo.getUserSeqId(),position);
//					}
				}
			});
			normalPost_Holder.ll_view_normal_post.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					if(isTourist){
//						DialogUtil.showGuestDialog(context, null);
//					}else{				
						skipToPostDetail(postInfo.getPostType(), postInfo.getPostId(), postInfo.getUserSeqId(),position);
//					}
				}
			});
			normalPost_Holder.gv_pic.setOnTouchInvalidPositionListener(new OnTouchInvalidPositionListener() {
				
				@Override
				public boolean onTouchInvalidPosition(int motionEvent) {
					return false;
				}
			});

			final RelativeLayout like3 = normalPost_Holder.rl_like;
               like3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{	
						clickLike(position,like3,postInfo.getUserSeqId(),postInfo.getPostId(),postOtherInfo.isMineLike());
					}
				}
			});
			break;
		case 3:
				Log.e("IndexViewAdapter", "转发普通帖子加载数据");
				if(userList.isEmpty()||postList.isEmpty()||postOtherList.isEmpty()){
					return convertView;
				}
				if(tran_normalPost_Holder.iv_headphoto_icon!= null){
					setHeadIcon(userInfo.getUserLevel(),tran_normalPost_Holder.iv_headphoto_icon);
				}
				if(userInfo.getUserFaceUrl()!=null){
					try {
						imageloader = new ImageLoaderUtil(context, userInfo.getUserFaceUrl(), tran_normalPost_Holder.iv_smallPhoto, R.drawable.icon_headfailed, R.drawable.icon_headfailed);
						imageloader.showImage();
					} catch (Exception e) {
						Log.e("ImageLoader", e.toString());
					}
				}
				if(userInfo.getUserName()!=null){
					tran_normalPost_Holder.tv_doctorName.setText(userInfo.getUserName());
				}
				if(userInfo.getCompanyName()!=null){
					tran_normalPost_Holder.tv_hospital.setText(userInfo.getCompanyName());
				}
				if(userInfo.getDeptName()!= null){
					tran_normalPost_Holder.tv_department.setText(userInfo.getDeptName());
				}
				if(userInfo.getProfessionalTitle()!= null){
					tran_normalPost_Holder.tv_jobTitle.setText(userInfo.getProfessionalTitle());
				}

//				if(postInfo.getPostLevel()!=null && postInfo.getPostLevel().equals("1")){
				if(postInfo.getContent()!=null){	
					tran_normalPost_Holder.tv_post_text.setText(postInfo.getContent());
				}
				SrcPostInfo srcPostInfo = postList.get(position).getSrcPostAbstractCard();
				SrcUserCard srcUserCard = postList.get(position).getSrcUserCard();
				SpannableStringBuilder ssBuilder = new SpannableStringBuilder();
				final List<PostAbstractList> postAbstractLists = postInfo.getPostAbstractList();
				if(postInfo.getPostType().equals(Constant.TRANSMIT_POST2)){	
					// TODO Auto-generated method stub
//					boolean isTopTransmit = false;
//				Collections.reverse(postAbstractLists);
					if(postAbstractLists != null && !postAbstractLists.isEmpty()){
						for(int i = 0;i<postAbstractLists.size();i++){
							if(postAbstractLists.get(i).getPostLevel().equals("0")){
								postInfo.setSrcPostId(postAbstractLists.get(i).getPostAbstract().getPostId());
								SrcUserCard srcUser = new SrcUserCard();
								SrcPostInfo srcPost = new SrcPostInfo();
								srcUser.setUserFaceUrl(postAbstractLists.get(i).getUserCard().getUserFaceUrl());
								srcUser.setUserName(postAbstractLists.get(i).getUserCard().getUserName());
								srcPost.setContent(postAbstractLists.get(i).getPostAbstract().getContent());
								postInfo.setSrcUserCard(srcUser);
								postInfo.setSrcPostAbstractCard(srcPost);
							}else if(i == 0){
								postInfo.setPostLevel(postAbstractLists.get(i).getPostLevel());
								postInfo.setContent(postAbstractLists.get(i).getPostAbstract().getContent());
								String createDate = postAbstractLists.get(i).getPostAbstract().getCreateDate();
								if(createDate != null){
									try {
										tran_normalPost_Holder.tv_time.setText(DisplayTimeUtil.displayTimeString(createDate));
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}
						ssBuilder = InitTransmitPostUtil.initTransmitPost(context, ssBuilder, postAbstractLists,new InitSrcPostInterface() {
							
							@Override
							public void initSrcPost(Context context, UserCard userCard,
									PostInfo postInfo, String isDelete) {
								// TODO Auto-generated method stub
								if(isDelete != null && isDelete.equals("true")){
									tran_normalPost_Holder.tv_src_post_text.setText("抱歉，此帖子已被作者删除");
									tran_normalPost_Holder.tv_src_post_text.setTextColor(context.getResources().getColor(R.color.font_cell));
								}else{	
									try {
									initSrcGroupData(tran_normalPost_Holder.ll_src_group,tran_normalPost_Holder.tv_src_group_name,postInfo);
										initSrcNormalPostData(tran_normalPost_Holder.tv_src_post_text,tran_normalPost_Holder.gv_src_pic,tran_normalPost_Holder.tv_srcTime,postInfo,userCard,position);
										initSrcNormalPostListner(tran_normalPost_Holder.gv_src_pic,tran_normalPost_Holder.ll_src_normal_post,tran_normalPost_Holder.ll_srcDoctorInfo,tran_normalPost_Holder.tv_src_group_name,position,postInfo,userCard);
									} catch (Exception e1) {
										e1.printStackTrace();
									}
								}
							}
						}, iPullPostListCallback);
						
				}
					
//					for (int i = 0; i < postAbstractLists.size(); i++) {
//						if(postAbstractLists.get(i).getPostLevel().equals("0")){
//							if(postAbstractLists.get(i).getUserCard()!=null && postAbstractLists.get(i).getPostAbstract()!=null){
////								initSrcUserData(tran_normalPost_Holder.iv_srcSmallPhoto,tran_normalPost_Holder.tv_srcDoctorName,tran_normalPost_Holder.tv_srcJobTitle,
////										tran_normalPost_Holder.tv_srcHospital,tran_normalPost_Holder.tv_srcDepartment,tran_normalPost_Holder.iv_src_headphoto_icon,postAbstractLists.get(i).getUserCard());
////								t_srcPostId = postAbstractLists.get(i).getPostAbstract().getPostId();
//								postInfo.setSrcPostId(postAbstractLists.get(i).getPostAbstract().getPostId());
//								initSrcGroupData(tran_normalPost_Holder.ll_src_group,tran_normalPost_Holder.tv_src_group_name,postAbstractLists.get(i).getPostAbstract());
//								try {
//									initSrcNormalPostData(tran_normalPost_Holder.tv_src_post_text,tran_normalPost_Holder.gv_src_pic,tran_normalPost_Holder.tv_srcTime,postAbstractLists.get(i).getPostAbstract(),postAbstractLists.get(i).getUserCard(),position);
//								} catch (Exception e1) {
//									e1.printStackTrace();
//								}
//								initSrcNormalPostListner(tran_normalPost_Holder.gv_src_pic,tran_normalPost_Holder.ll_src_normal_post,tran_normalPost_Holder.ll_srcDoctorInfo,tran_normalPost_Holder.tv_src_group_name,position,postAbstractLists.get(i).getPostAbstract(),postAbstractLists.get(i).getUserCard());
//							}			
//						}else if(postAbstractLists.get(i).getPostLevel().equals(String.valueOf(postAbstractLists.size()-1))){
//							String content = postAbstractLists.get(i).getPostAbstract().getContent();
//							String createDate = postAbstractLists.get(i).getPostAbstract().getCreateDate();
////							t_postLevel = postAbstractLists.get(i).getPostLevel();
//							postInfo.setPostLevel(postAbstractLists.get(i).getPostLevel());
//							isTopTransmit = true;
//							if(content != null){
////								t_postContent = content;
//								postInfo.setContent(content);
//								SpannableString t_content = getTrasmitContent(postAbstractLists.get(i).getUserCard(),postAbstractLists.get(i).getPostAbstract(),position,postInfo,isTopTransmit);
//								ssBuilder.append(t_content);
////								tran_normalPost_Holder.tv_post_text.setText(t_content);
////								tran_normalPost_Holder.tv_post_text.setMovementMethod(LinkMovementMethod.getInstance());
//							}else{
//								postInfo.setContent("");
//								tran_normalPost_Holder.tv_post_text.setText("");
//							}
//							if(createDate != null){
//								try {
//									tran_normalPost_Holder.tv_time.setText(DisplayTimeUtil.displayTimeString(createDate));
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//							}else{
//								tran_normalPost_Holder.tv_time.setText("");
//							}
//						}else{
//							isTopTransmit = false;
//							SpannableString t_content = getTrasmitContent(postAbstractLists.get(i).getUserCard(),postAbstractLists.get(i).getPostAbstract(),position,postInfo,isTopTransmit);
//							ssBuilder.append(t_content);
//						}
//					}
					tran_normalPost_Holder.tv_post_text.setText(ssBuilder);
					tran_normalPost_Holder.tv_post_text.setMovementMethod(CustomLinkMovementMethod.getInstance());
					
				}else{
					if(postInfo.getCreateDate()!=null){
						try {
							tran_normalPost_Holder.tv_time.setText(DisplayTimeUtil.displayTimeString(postInfo.getCreateDate()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					if(srcPostInfo != null && srcUserCard != null){	
						try {
							initSrcUserData(tran_normalPost_Holder.iv_srcSmallPhoto,tran_normalPost_Holder.tv_srcDoctorName,tran_normalPost_Holder.tv_srcJobTitle,
		                			tran_normalPost_Holder.tv_srcHospital,tran_normalPost_Holder.tv_srcDepartment,tran_normalPost_Holder.iv_src_headphoto_icon,srcUserCard);
		                	initSrcGroupData(tran_normalPost_Holder.ll_src_group,tran_normalPost_Holder.tv_src_group_name,srcPostInfo);
							initSrcNormalPostData(tran_normalPost_Holder.tv_src_post_text,tran_normalPost_Holder.gv_src_pic,tran_normalPost_Holder.tv_srcTime,srcPostInfo,srcUserCard,position);
							initSrcNormalPostListner(tran_normalPost_Holder.gv_src_pic,tran_normalPost_Holder.ll_src_normal_post,tran_normalPost_Holder.ll_srcDoctorInfo,tran_normalPost_Holder.tv_src_group_name,position,srcPostInfo,srcUserCard);			
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
//				}else{
//					SpannableString content = getContentText(postInfo.getContent(),postInfo.getPContent(),postInfo.getPUserName(),postInfo.getPUserSeqId());
//					tran_normalPost_Holder.tv_post_text.setText(content);
//					tran_normalPost_Holder.tv_post_text.setMovementMethod(LinkMovementMethod.getInstance());
//				}
				if(postOtherInfo != null && postOtherInfo.getFORWARD_NUM()!=0){
					tran_normalPost_Holder.tv_transmit.setText(""+postOtherInfo.getFORWARD_NUM());
				}else{
					tran_normalPost_Holder.tv_transmit.setText(context.getResources().getString(R.string.transmit_text));
				}
				if(postInfo.getPostType().equals(Constant.TRANSMIT_POST)){	
					tran_normalPost_Holder.tv_transmit.setTextColor(context.getResources().getColor(R.color.font_disable));
				}else{
					tran_normalPost_Holder.tv_transmit.setTextColor(context.getResources().getColor(R.color.font_cell));
				}
				if(postOtherInfo != null && postOtherInfo.getCOMMENT_NUM()!=0){
					tran_normalPost_Holder.tv_command.setText(""+postOtherInfo.getCOMMENT_NUM());
				}else{
					tran_normalPost_Holder.tv_command.setText(context.getResources().getString(R.string.command_text));
				}
				if(postOtherInfo != null && postOtherInfo.getLIKE_NUM()!=0){
					tran_normalPost_Holder.tv_like.setText(""+postOtherInfo.getLIKE_NUM());	
				}else{
					tran_normalPost_Holder.tv_like.setText(context.getResources().getString(R.string.like_text));
				}
				if(isLike){
					tran_normalPost_Holder.iv_like.setImageResource(R.drawable.icon_laud_active_24);
				}else{
					tran_normalPost_Holder.iv_like.setImageResource(R.drawable.icon_laud_24);
				}
				

			tran_normalPost_Holder.ll_doctorInfo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{					
						if(userInfo.getUserSeqId().equals(Constant.userInfo.getUserSeqId())){
							skipToSelfFragment();
						}else if(userInfo.getUserType().equals("1")&&!userInfo.getUserSeqId().equals(userId)){
							skipToSystemUserActivity(userInfo.getUserSeqId());
						}else{
							if(!userInfo.getUserSeqId().equals(userId)){		
								skipToOtherPeopleActivity(userInfo.getUserSeqId());
							}
						}
					}
				}
			});
			tran_normalPost_Holder.rl_transmit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{
						if(postInfo.getPostType().equals(Constant.TRANSMIT_POST)){
							ToastUtils.showMessage(context, "多次转发功能正在开发中，敬请期待");
						}else{
							skipToRepeatActivity(userInfo.getUserSeqId(),postInfo.getSrcPostAbstractCard().getContent(),
									postInfo.getSrcUserCard().getUserFaceUrl(),postInfo.getPostId(),
									postInfo.getSrcUserCard().getUserName(),postInfo.getPostType(),postInfo.getPostLevel()
									,postInfo.getSrcPostId(),position,postOtherInfo.getFORWARD_NUM(),postAbstractLists,userInfo.getUserName(),flag);
						}		
					}
			  }
			});
			tran_normalPost_Holder.rl_command.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					if(isTourist){
//						DialogUtil.showGuestDialog(context, null);
//					}else{					
						skipToPostDetail(postInfo.getPostType(),postInfo.getPostId(),postInfo.getUserSeqId(),position);
//					}
				}
			});
			tran_normalPost_Holder.tv_post_text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					if(isTourist){
//						DialogUtil.showGuestDialog(context, null);
//					}else{
						skipToPostDetail(postInfo.getPostType(),postInfo.getPostId(),postInfo.getUserSeqId(),position);		
//					}
				}
			});
//			tran_normalPost_Holder.ll_transmit_content.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if(isTourist){
//						DialogUtil.showGuestDialog(context, null);
//					}else{						
//						skipToPostDetail(postInfo.getPostType(),postInfo.getPostId(),userInfo.getUserSeqId(),position);		
//					}
//				}
//			});
			final RelativeLayout like4 = tran_normalPost_Holder.rl_like;
            like4.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{			
						clickLike(position,like4,postInfo.getUserSeqId(),postInfo.getPostId(),postOtherInfo.isMineLike());
					}
				}
			});
			break;
		case 4:

				Log.e("IndexViewAdapter", "转发病例分享帖子加载数据");
				if(userList.isEmpty()||postList.isEmpty()||postOtherList.isEmpty()){
					return convertView;
				}
				if(tran_caseShare_Holder.iv_headphoto_icon!= null){
					setHeadIcon(userInfo.getUserLevel(),tran_caseShare_Holder.iv_headphoto_icon);
				}
				if(userInfo.getUserFaceUrl()!=null){
					imageloader = new ImageLoaderUtil(context, userInfo.getUserFaceUrl(), tran_caseShare_Holder.iv_smallPhoto, R.drawable.icon_headfailed, R.drawable.icon_headfailed);
					imageloader.showImage();
				}
				if(userInfo.getUserName()!=null){
					tran_caseShare_Holder.tv_doctorName.setText(userInfo.getUserName());
				}
				if(userInfo.getCompanyName()!=null){
					tran_caseShare_Holder.tv_hospital.setText(userInfo.getCompanyName());
				}
				if(userInfo.getDeptName()!= null){
					tran_caseShare_Holder.tv_department.setText(userInfo.getDeptName());
				}
				if(userInfo.getProfessionalTitle()!= null){
					tran_caseShare_Holder.tv_jobTitle.setText(userInfo.getProfessionalTitle());
				}
				SrcPostInfo srcPostInfo2 = postList.get(position).getSrcPostAbstractCard();
				SrcUserCard srcUserCard2 = postList.get(position).getSrcUserCard();
				SpannableStringBuilder ssBuilder2 = new SpannableStringBuilder();
				final List<PostAbstractList> caseSharePostAbstractLists = postInfo.getPostAbstractList();
				if(postInfo.getPostType().equals(Constant.TRANSMIT_POST2)){	
					if(caseSharePostAbstractLists != null && !caseSharePostAbstractLists.isEmpty()){
						for(int i = 0;i<caseSharePostAbstractLists.size();i++){
							if(caseSharePostAbstractLists.get(i).getPostLevel().equals("0")){
								postInfo.setSrcPostId(caseSharePostAbstractLists.get(i).getPostAbstract().getPostId());
								SrcUserCard srcUser = new SrcUserCard();
								SrcPostInfo srcPost = new SrcPostInfo();
								srcUser.setUserFaceUrl(caseSharePostAbstractLists.get(i).getUserCard().getUserFaceUrl());
								srcUser.setUserName(caseSharePostAbstractLists.get(i).getUserCard().getUserName());
								srcPost.setContent(caseSharePostAbstractLists.get(i).getPostAbstract().getContent());
								srcPost.setPostTitle(caseSharePostAbstractLists.get(i).getPostAbstract().getPostTitle());
								postInfo.setSrcUserCard(srcUser);
								postInfo.setSrcPostAbstractCard(srcPost);
							}else if(i == 0){
								postInfo.setPostLevel(caseSharePostAbstractLists.get(i).getPostLevel());
								postInfo.setContent(caseSharePostAbstractLists.get(i).getPostAbstract().getContent());
								String createDate = caseSharePostAbstractLists.get(i).getPostAbstract().getCreateDate();
								if(createDate != null){
									try {
										tran_caseShare_Holder.tv_time.setText(DisplayTimeUtil.displayTimeString(createDate));
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}
						ssBuilder2 = InitTransmitPostUtil.initTransmitPost(context, ssBuilder2, caseSharePostAbstractLists, new InitSrcPostInterface() {

							@Override
							public void initSrcPost(Context context, UserCard userCard,
									PostInfo postInfo, String isDelete) {
								// TODO Auto-generated method stub
								if(isDelete != null && isDelete.equals("true")){
									hintCaseShareView(tran_caseShare_Holder.ll_caseShare,tran_caseShare_Holder.fl_bg);
									tran_caseShare_Holder.tv_srcname.setText("抱歉，此帖子已被作者删除");
									tran_caseShare_Holder.tv_srcname.setTextColor(context.getResources().getColor(R.color.font_cell));
								}else{		
									try {
										initSrcPostInfoData(tran_caseShare_Holder.ll_caseShare,tran_caseShare_Holder.fl_bg,tran_caseShare_Holder.iv_src_backgroudpic,tran_caseShare_Holder.tv_src_postTitle,
												tran_caseShare_Holder.tv_src_completeRate,tran_caseShare_Holder.tv_srcTime,tran_caseShare_Holder.iv_gray,tran_caseShare_Holder.tv_srcname,postInfo,userCard,position);
										initSrcPostListner(tran_caseShare_Holder.rl_src_view_post,tran_caseShare_Holder.ll_srcDoctorInfo,position,postInfo,userCard);
									} catch (Exception e1) {
										e1.printStackTrace();
									}
								}
							}
						}, iPullPostListCallback);
						tran_caseShare_Holder.tv_post_text.setText(ssBuilder2);
						tran_caseShare_Holder.tv_post_text.setMovementMethod(CustomLinkMovementMethod.getInstance());
					}	
				}else{
					if(postInfo.getContent()!=null){	
						tran_caseShare_Holder.tv_post_text.setText(postInfo.getContent());
					}
					if(postInfo.getCreateDate()!=null){
						try {
							tran_caseShare_Holder.tv_time.setText(DisplayTimeUtil.displayTimeString(postInfo.getCreateDate()));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							Log.e("IndexListViewAdapter", e.toString());
						}
					}
					if(srcPostInfo2 != null && srcUserCard2!=null){
						if(srcPostInfo2 != null && srcUserCard2!=null){
							try {
								initSrcUserData(tran_caseShare_Holder.iv_srcSmallPhoto,tran_caseShare_Holder.tv_srcDoctorName,tran_caseShare_Holder.tv_srcJobTitle,
										tran_caseShare_Holder.tv_srcHospital,tran_caseShare_Holder.tv_srcDepartment,tran_caseShare_Holder.iv_src_headphoto_icon,srcUserCard2);		
								initSrcPostInfoData(tran_caseShare_Holder.iv_src_backgroudpic,tran_caseShare_Holder.tv_src_postTitle,
										tran_caseShare_Holder.tv_src_completeRate,tran_caseShare_Holder.tv_srcTime,tran_caseShare_Holder.iv_gray,tran_caseShare_Holder.tv_srcname,srcPostInfo2,srcUserCard2,position);
								initSrcPostListner(tran_caseShare_Holder.rl_src_view_post,tran_caseShare_Holder.ll_srcDoctorInfo,position,srcPostInfo2,srcUserCard2);		
							} catch (Exception e) {
								Log.e("IndexListViewAdapter", e.toString());
							}
						}
					}
				}
//					for (int i = 0; i < caseSharePostAbstractLists.size(); i++) {
//						if(caseSharePostAbstractLists.get(i).getPostLevel().equals("0")){
//							if(caseSharePostAbstractLists.get(i).getUserCard()!=null && caseSharePostAbstractLists.get(i).getPostAbstract()!=null){
////								initSrcUserData(tran_caseShare_Holder.iv_srcSmallPhoto,tran_caseShare_Holder.tv_srcDoctorName,tran_caseShare_Holder.tv_srcJobTitle,
////										tran_caseShare_Holder.tv_srcHospital,tran_caseShare_Holder.tv_srcDepartment,tran_caseShare_Holder.iv_src_headphoto_icon,caseSharePostAbstractLists.get(i).getUserCard());
//								try {									
//									initSrcPostInfoData(tran_caseShare_Holder.iv_src_backgroudpic,tran_caseShare_Holder.tv_src_postTitle,
//											tran_caseShare_Holder.tv_src_completeRate,tran_caseShare_Holder.tv_srcTime,tran_caseShare_Holder.iv_gray,tran_caseShare_Holder.tv_srcname,caseSharePostAbstractLists.get(i).getPostAbstract(),caseSharePostAbstractLists.get(i).getUserCard(),position);
//									
//								} catch (Exception e1) {
//									e1.printStackTrace();
//								}
//								initSrcPostListner(tran_caseShare_Holder.rl_src_view_post,tran_caseShare_Holder.ll_srcDoctorInfo,position,caseSharePostAbstractLists.get(i).getPostAbstract(),caseSharePostAbstractLists.get(i).getUserCard());
//							}			
//						}
//						if(caseSharePostAbstractLists.get(i).getPostLevel().equals(String.valueOf(caseSharePostAbstractLists.size()-1))){
//							String content = caseSharePostAbstractLists.get(i).getPostAbstract().getContent();
//							String createDate = caseSharePostAbstractLists.get(i).getPostAbstract().getCreateDate();
//							if(content != null){
//								tran_caseShare_Holder.tv_post_text.setText(content);
//							}
//							if(createDate != null){
//								try {
//									tran_caseShare_Holder.tv_time.setText(DisplayTimeUtil.displayTimeString(createDate));
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//							}
//						}
//					}
//				}

//				if(postInfo.getPostLevel()!=null && postInfo.getPostLevel().equals("1")){
				
//				}else{
//					SpannableString content = getContentText(postInfo.getContent(),postInfo.getPContent(),postInfo.getPUserName(),postInfo.getPUserSeqId());
//					tran_caseShare_Holder.tv_post_text.setText(content);
//					tran_caseShare_Holder.tv_post_text.setMovementMethod(LinkMovementMethod.getInstance());
//				}

				
				if(postOtherInfo != null &&postOtherInfo.getFORWARD_NUM()!=0){
					tran_caseShare_Holder.tv_transmit.setText(""+postOtherInfo.getFORWARD_NUM());
				}else{
					tran_caseShare_Holder.tv_transmit.setText(context.getResources().getString(R.string.transmit_text));
				}
				if(postInfo.getPostType().equals(Constant.TRANSMIT_POST)){		
					tran_caseShare_Holder.tv_transmit.setTextColor(context.getResources().getColor(R.color.font_disable));
				}else{
					tran_caseShare_Holder.tv_transmit.setTextColor(context.getResources().getColor(R.color.font_cell));
				}
				if(postOtherInfo != null &&postOtherInfo.getCOMMENT_NUM()!=0){
					tran_caseShare_Holder.tv_command.setText(""+postOtherInfo.getCOMMENT_NUM());
				}else{
					tran_caseShare_Holder.tv_command.setText(context.getResources().getString(R.string.command_text));
				}
				if(postOtherInfo != null &&postOtherInfo.getLIKE_NUM()!=0){
					tran_caseShare_Holder.tv_like.setText(""+postOtherInfo.getLIKE_NUM());	
				}else{
					tran_caseShare_Holder.tv_like.setText(context.getResources().getString(R.string.like_text));
				}
				if(isLike){
					tran_caseShare_Holder.iv_like.setImageResource(R.drawable.icon_laud_active_24);
				}else{
					tran_caseShare_Holder.iv_like.setImageResource(R.drawable.icon_laud_24);
				}
				
			tran_caseShare_Holder.ll_doctorInfo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{						
						if(userInfo.getUserSeqId().equals(Constant.userInfo.getUserSeqId())){
							skipToSelfFragment();
						}else if(userInfo.getUserType().equals("1")&&!userInfo.getUserSeqId().equals(userId)){
							skipToSystemUserActivity(userInfo.getUserSeqId());
						}else{
							if(!userInfo.getUserSeqId().equals(userId)){		
								skipToOtherPeopleActivity(userInfo.getUserSeqId());
							}
						}
					}
				}
			});
			
			
			tran_caseShare_Holder.rl_transmit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{
						if(postInfo.getPostType().equals(Constant.TRANSMIT_POST)){
							ToastUtils.showMessage(context, "多次转发功能正在开发中，敬请期待");
						}else{
							IndexFragment.POSITION = position;
							skipToRepeatActivity(userInfo.getUserSeqId(),postInfo.getSrcPostAbstractCard().getPostTitle(),
									postInfo.getSrcUserCard().getUserFaceUrl(),postInfo.getPostId(),
									postInfo.getSrcUserCard().getUserName(),postInfo.getPostType(),postInfo.getPostLevel()
									,postInfo.getSrcPostId(),position,postOtherInfo.getFORWARD_NUM(),caseSharePostAbstractLists,userInfo.getUserName(),flag);
						}				
					}
				}
			});
			tran_caseShare_Holder.rl_command.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					if(isTourist){
//						DialogUtil.showGuestDialog(context, null);
//					}else{	
						skipToPostDetail(postInfo.getPostType(),postInfo.getPostId(),postInfo.getUserSeqId(),position);
//					}
				}
			});
			tran_caseShare_Holder.tv_post_text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					if(isTourist){
//						DialogUtil.showGuestDialog(context, null);
//					}else{			
						skipToPostDetail(postInfo.getPostType(),postInfo.getPostId(),postInfo.getUserSeqId(),position);		
//					}
				}
			});
			final RelativeLayout like5 = tran_caseShare_Holder.rl_like;
            like5.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{	
						clickLike(position,like5,postInfo.getUserSeqId(),postInfo.getPostId(),postOtherInfo.isMineLike());
					}
				}
			});
			break;
		case 5:
				Log.e("IndexViewAdapter", "转发疑难求助帖子加载数据");
				if(userList.isEmpty()||postList.isEmpty()||postOtherList.isEmpty()){
					return convertView;
				}
				if(tran_problemHelp_Holder.iv_headphoto_icon!= null){
					setHeadIcon(userInfo.getUserLevel(),tran_problemHelp_Holder.iv_headphoto_icon);
				}
				if(userInfo.getUserFaceUrl()!=null){
					try {
						imageloader = new ImageLoaderUtil(context, userInfo.getUserFaceUrl(), tran_problemHelp_Holder.iv_smallPhoto, R.drawable.icon_headfailed, R.drawable.icon_headfailed);
						imageloader.showImage();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(userInfo.getUserName()!=null){
					tran_problemHelp_Holder.tv_doctorName.setText(userInfo.getUserName());
				}
				if(userInfo.getCompanyName()!=null){
					tran_problemHelp_Holder.tv_hospital.setText(userInfo.getCompanyName());
				}
				if(userInfo.getDeptName()!= null){
					tran_problemHelp_Holder.tv_department.setText(userInfo.getDeptName());
				}
				if(userInfo.getProfessionalTitle()!= null){
					tran_problemHelp_Holder.tv_jobTitle.setText(userInfo.getProfessionalTitle());
				}
				SrcPostInfo srcPostInfo3 = postList.get(position).getSrcPostAbstractCard();
				SrcUserCard srcUserCard3 = postList.get(position).getSrcUserCard();
				SpannableStringBuilder ssBuilder3 = new SpannableStringBuilder();
				final List<PostAbstractList> problemHelpPostAbstractLists = postInfo.getPostAbstractList();
				if(postInfo.getPostType().equals(Constant.TRANSMIT_POST2)){
					if(problemHelpPostAbstractLists != null && !problemHelpPostAbstractLists.isEmpty()){
						for(int i = 0;i<problemHelpPostAbstractLists.size();i++){
							if(problemHelpPostAbstractLists.get(i).getPostLevel().equals("0")){
								postInfo.setSrcPostId(problemHelpPostAbstractLists.get(i).getPostAbstract().getPostId());
								SrcUserCard srcUser = new SrcUserCard();
								SrcPostInfo srcPost = new SrcPostInfo();
								srcUser.setUserFaceUrl(problemHelpPostAbstractLists.get(i).getUserCard().getUserFaceUrl());
								srcUser.setUserName(problemHelpPostAbstractLists.get(i).getUserCard().getUserName());
								srcPost.setContent(problemHelpPostAbstractLists.get(i).getPostAbstract().getContent());
								srcPost.setPostTitle(problemHelpPostAbstractLists.get(i).getPostAbstract().getPostTitle());
								postInfo.setSrcUserCard(srcUser);
								postInfo.setSrcPostAbstractCard(srcPost);
							}else if(i == 0){
								postInfo.setPostLevel(problemHelpPostAbstractLists.get(i).getPostLevel());
								postInfo.setContent(problemHelpPostAbstractLists.get(i).getPostAbstract().getContent());
								String createDate = problemHelpPostAbstractLists.get(i).getPostAbstract().getCreateDate();
								if(createDate != null){
									try {
										tran_problemHelp_Holder.tv_time.setText(DisplayTimeUtil.displayTimeString(createDate));
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}
						ssBuilder3 = InitTransmitPostUtil.initTransmitPost(context, ssBuilder3, problemHelpPostAbstractLists,new InitSrcPostInterface() {
							@Override
							public void initSrcPost(Context context, UserCard userCard,
									PostInfo postInfo, String isDelete) {
								// TODO Auto-generated method stub
								if(isDelete != null && isDelete.equals("true")){
									hintCaseShareView(tran_problemHelp_Holder.ll_problemHelp,tran_problemHelp_Holder.fl_bg);
									tran_problemHelp_Holder.tv_srcname.setText("抱歉，此帖子已被作者删除");
									tran_problemHelp_Holder.tv_srcname.setTextColor(context.getResources().getColor(R.color.font_cell));
								}else{							
									try {
										initSrcUserData(tran_problemHelp_Holder.iv_srcSmallPhoto,tran_problemHelp_Holder.tv_srcDoctorName,tran_problemHelp_Holder.tv_srcJobTitle,
												tran_problemHelp_Holder.tv_srcHospital,tran_problemHelp_Holder.tv_srcDepartment,tran_problemHelp_Holder.iv_src_headphoto_icon,userCard);
										initSrcPostInfoData(tran_problemHelp_Holder.ll_problemHelp,tran_problemHelp_Holder.fl_bg,tran_problemHelp_Holder.iv_src_backgroudpic,tran_problemHelp_Holder.tv_src_postTitle,
												tran_problemHelp_Holder.tv_src_completeRate,tran_problemHelp_Holder.tv_srcTime,tran_problemHelp_Holder.iv_gray,tran_problemHelp_Holder.tv_srcname,postInfo,userCard,position);
										initSrcPostListner(tran_problemHelp_Holder.rl_src_view_post,tran_problemHelp_Holder.ll_srcDoctorInfo,position,postInfo,userCard);
									} catch (Exception e1) {
										e1.printStackTrace();
									}
								}
							}
						}, iPullPostListCallback);
						tran_problemHelp_Holder.tv_post_text.setText(ssBuilder3);
						tran_problemHelp_Holder.tv_post_text.setMovementMethod(CustomLinkMovementMethod.getInstance());
					}
				}else{
					if(postInfo.getContent()!=null){			
						tran_problemHelp_Holder.tv_post_text.setText(postInfo.getContent());
					}
					if(postInfo.getCreateDate()!=null){
						try {
							tran_problemHelp_Holder.tv_time.setText(DisplayTimeUtil.displayTimeString(postInfo.getCreateDate()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(srcPostInfo3!=null && srcUserCard3!=null){
						
						try {
							initSrcUserData(tran_problemHelp_Holder.iv_srcSmallPhoto, tran_problemHelp_Holder.tv_srcDoctorName, tran_problemHelp_Holder.tv_srcJobTitle,
									tran_problemHelp_Holder.tv_srcHospital, tran_problemHelp_Holder.tv_srcDepartment,tran_problemHelp_Holder.iv_src_headphoto_icon,srcUserCard3);
							initSrcPostInfoData(tran_problemHelp_Holder.iv_src_backgroudpic,tran_problemHelp_Holder.tv_src_postTitle,
									tran_problemHelp_Holder.tv_src_completeRate,tran_problemHelp_Holder.tv_srcTime,tran_problemHelp_Holder.iv_gray,tran_problemHelp_Holder.tv_srcname,srcPostInfo3,srcUserCard3,position);
							initSrcPostListner(tran_problemHelp_Holder.rl_src_view_post,tran_problemHelp_Holder.ll_srcDoctorInfo,position,srcPostInfo3,srcUserCard3);		
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
//					for (int i = 0; i < problemHelpPostAbstractLists.size(); i++) {
//						if(problemHelpPostAbstractLists.get(i).getPostLevel().equals("0")){
//							if(problemHelpPostAbstractLists.get(i).getUserCard()!=null && problemHelpPostAbstractLists.get(i).getPostAbstract()!=null){
////								initSrcUserData(tran_problemHelp_Holder.iv_srcSmallPhoto, tran_problemHelp_Holder.tv_srcDoctorName, tran_problemHelp_Holder.tv_srcJobTitle,
////										tran_problemHelp_Holder.tv_srcHospital, tran_problemHelp_Holder.tv_srcDepartment,tran_problemHelp_Holder.iv_src_headphoto_icon,problemHelpPostAbstractLists.get(i).getUserCard());
//								try {
//									initSrcPostInfoData(tran_problemHelp_Holder.iv_src_backgroudpic,tran_problemHelp_Holder.tv_src_postTitle,
//											tran_problemHelp_Holder.tv_src_completeRate,tran_problemHelp_Holder.tv_srcTime,tran_problemHelp_Holder.iv_gray,tran_problemHelp_Holder.tv_srcname,problemHelpPostAbstractLists.get(i).getPostAbstract(),problemHelpPostAbstractLists.get(i).getUserCard(),position);
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//								initSrcPostListner(tran_problemHelp_Holder.rl_src_view_post,tran_problemHelp_Holder.ll_srcDoctorInfo,position,problemHelpPostAbstractLists.get(i).getPostAbstract(),problemHelpPostAbstractLists.get(i).getUserCard());
//							}			
//						}
//						if(problemHelpPostAbstractLists.get(i).getPostLevel().equals(String.valueOf(problemHelpPostAbstractLists.size()-1))){
//							String content = problemHelpPostAbstractLists.get(i).getPostAbstract().getContent();
//							String createDate = problemHelpPostAbstractLists.get(i).getPostAbstract().getCreateDate();
//							if(content != null){
//								tran_problemHelp_Holder.tv_post_text.setText(content);
//							}
//							if(createDate != null){
//								try {
//									tran_problemHelp_Holder.tv_time.setText(DisplayTimeUtil.displayTimeString(createDate));
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//							}
//						}
//					}

				
//				if(postInfo.getPostLevel()!=null && postInfo.getPostLevel().equals("1")){
				
//				}else{
//					SpannableString content = getContentText(postInfo.getContent(),postInfo.getPContent(),postInfo.getPUserName(),postInfo.getPUserSeqId());
//					tran_problemHelp_Holder.tv_post_text.setText(content);
//					tran_problemHelp_Holder.tv_post_text.setMovementMethod(LinkMovementMethod.getInstance());
//				}
				
				
				if(postOtherInfo != null &&postOtherInfo.getFORWARD_NUM()!=0){
					tran_problemHelp_Holder.tv_transmit.setText(""+postOtherInfo.getFORWARD_NUM());
				}else{
					tran_problemHelp_Holder.tv_transmit.setText(context.getResources().getString(R.string.transmit_text));
				}
				if(postInfo.getPostType().equals(Constant.TRANSMIT_POST)){			
					tran_problemHelp_Holder.tv_transmit.setTextColor(context.getResources().getColor(R.color.font_disable));
				}else{
					tran_problemHelp_Holder.tv_transmit.setTextColor(context.getResources().getColor(R.color.font_cell));
				}
				if(postOtherInfo != null &&postOtherInfo.getCOMMENT_NUM()!=0){
					tran_problemHelp_Holder.tv_command.setText(""+postOtherInfo.getCOMMENT_NUM());
				}else{
					tran_problemHelp_Holder.tv_command.setText(context.getResources().getString(R.string.command_text));
				}
				if(postOtherInfo != null &&postOtherInfo.getLIKE_NUM()!=0){
					tran_problemHelp_Holder.tv_like.setText(""+postOtherInfo.getLIKE_NUM());	
				}else{
					tran_problemHelp_Holder.tv_like.setText(context.getResources().getString(R.string.like_text));
				}
				if(isLike){
					tran_problemHelp_Holder.iv_like.setImageResource(R.drawable.icon_laud_active_24);
				}else{
					tran_problemHelp_Holder.iv_like.setImageResource(R.drawable.icon_laud_24);
				}
				


			tran_problemHelp_Holder.ll_doctorInfo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{				
						if(userInfo.getUserSeqId().equals(Constant.userInfo.getUserSeqId())){
							skipToSelfFragment();
						}else if(userInfo.getUserType().equals("1")&&!userInfo.getUserSeqId().equals(userId)){
							skipToSystemUserActivity(userInfo.getUserSeqId());
						}else{
							if(!userInfo.getUserSeqId().equals(userId)){		
								skipToOtherPeopleActivity(userInfo.getUserSeqId());
							}
						}
					}
				}
			});
			tran_problemHelp_Holder.rl_transmit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{
						if(postInfo.getPostType().equals(Constant.TRANSMIT_POST)){
							ToastUtils.showMessage(context, "多次转发功能正在开发中，敬请期待");
						}else{
							IndexFragment.POSITION = position;
							skipToRepeatActivity(userInfo.getUserSeqId(),postInfo.getSrcPostAbstractCard().getPostTitle(),
							postInfo.getSrcUserCard().getUserFaceUrl(),postInfo.getPostId(),
							postInfo.getSrcUserCard().getUserName(),postInfo.getPostType(),postInfo.getPostLevel()
							,postInfo.getSrcPostId(),position,postOtherInfo.getFORWARD_NUM(),problemHelpPostAbstractLists,userInfo.getUserName(),flag);	
						}		
					}
				}
			});
			tran_problemHelp_Holder.rl_command.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					if(isTourist){
//						DialogUtil.showGuestDialog(context, null);
//					}else{				
						skipToPostDetail(postInfo.getPostType(),postInfo.getPostId(),postInfo.getUserSeqId(),position);
//					}
				}
			});
			tran_problemHelp_Holder.tv_post_text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					if(isTourist){
//						DialogUtil.showGuestDialog(context, null);
//					}else{
					try {	
						skipToPostDetail(postInfo.getPostType(),postInfo.getPostId(),postInfo.getUserSeqId(),position);		
					} catch (Exception e) {
					    Log.e("IndexListViewAdapter", e.toString());	
					}
//					}
				}
			});
			final RelativeLayout like6 = tran_problemHelp_Holder.rl_like;
            like6.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{						
						clickLike(position,like6,postInfo.getUserSeqId(),postInfo.getPostId(),postOtherInfo.isMineLike());
					}
				}
			});
			break;
		case 6:
			Log.e("IndexViewAdapter", "好友推荐加载数据");
			Log.e("IndexViewAdapter", "好友推荐列表:"+postInfo.getRecommendUser().toString());
			recommend_adapter = new RecommendListAdapter(context, postInfo.getRecommendUser());
			recommend_user_Holder.lv_recommend_doctor.setAdapter(recommend_adapter);
			if(postInfo.getRecommendUser()!=null && !postInfo.getRecommendUser().isEmpty()){	
				lastUserId = postInfo.getRecommendUser().get(postInfo.getRecommendUser().size()-1).getUserSeqId();
			}else{
				lastUserId = "0";
			}
			Log.e("IndexListViewAdapter", "最后的userId："+lastUserId);
			notifyDataSetChanged();
//			recommend_user_Holder.bt_change.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					getNewRecommendUser(position,postInfo);
//				}
//			});
			break;
		case 7:
			break;
		}
		return convertView;
	}
	

	protected void skipToRepeatActivity(String userSeqId, String content,
			String userFaceUrl, String postId, String srcUserName,
			String postType, String postLevel, String srcPostId, int position,
			int forward_NUM, List<PostAbstractList> postAbstractLists, String userName,int flag) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, RepeatActivity.class);
		intent.putExtra(Constant.USER_SEQ_ID, userFaceUrl);
		intent.putExtra(Constant.POST_TEXT, content);
		intent.putExtra(Constant.PIC_URL, userFaceUrl);
		intent.putExtra(Constant.POST_ID, postId);
		intent.putExtra("srcUserName", srcUserName);
		intent.putExtra(Constant.POST_TYPE, postType);
		intent.putExtra(Constant.POST_LEVEL, postLevel);
		intent.putExtra(Constant.SRC_POST_ID, srcPostId);
		intent.putExtra(Constant.POSITION, position);
		intent.putExtra(Constant.FORWARD_NUM, forward_NUM);
		intent.putExtra(Constant.USER_NAME, userName);
		Bundle bundle = new Bundle();
		bundle.putSerializable("postAbstractLists", (Serializable) postAbstractLists);
		intent.putExtra("postList", bundle);
		intent.putExtra("flag", flag);
		if(indexFragment != null){
			indexFragment.startActivityForResult(intent, 200);
		}else{
			mainActivity.startActivityForResult(intent, 200);
		}
		
	}
	protected void hintCaseShareView(LinearLayout ll_caseShare,
			FrameLayout fl_bg) {
		ll_caseShare.setVisibility(View.GONE);
		fl_bg.setVisibility(View.GONE);
	}

	private int getWindowsWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		mainActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	/**
	 * 跳转到系统主页
	 * @param userSeqId
	 */
	protected void skipToSystemUserActivity(String userSeqId) {
		Intent intent = new Intent(context, SystemUserActivity.class);
		intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
		context.startActivity(intent);
	}
	private void initSrcGroupData(LinearLayout ll_src_group,
			TextView tv_src_group_name, SrcPostInfo srcPostInfo) {
		if(srcPostInfo.getPostType().equals(Constant.CIRCLE_POST)){
			ll_src_group.setVisibility(View.VISIBLE);
			tv_src_group_name.setText(srcPostInfo.getCoterieName());
		}else{
			ll_src_group.setVisibility(View.GONE);
		}
	}
	private void initSrcGroupData(LinearLayout ll_src_group,
			TextView tv_src_group_name, PostInfo postInfo) {
		if(postInfo.getPostType().equals(Constant.CIRCLE_POST)){
			ll_src_group.setVisibility(View.VISIBLE);
			tv_src_group_name.setText(postInfo.getCoterieName());
		}else{
			ll_src_group.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 初始化转发帖子里的圈子顶部view
	 * @param convertView
	 * @param tran_normalPost_Holder
	 */
	private void initSrcGroupView(View convertView,
			ViewHolder4 tran_normalPost_Holder) {
		tran_normalPost_Holder.ll_src_group = (LinearLayout)convertView.findViewById(R.id.ll_src_group);
		tran_normalPost_Holder.tv_src_group_name = (TextView)convertView.findViewById(R.id.tv_src_group_name);
	}
	protected void getNewRecommendUser(int position, final PostInfo postInfo) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryRecommendUsers");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", userId);
		busiParams.put("baseObjUserId", lastUserId);
		busiParams.put("isNew", String.valueOf(false));
		busiParams.put("beginNum", Constant.USER_BEGIN_NUM);
		busiParams.put("endNum", Constant.USER_END_NUM);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			
			@Override
			public void success(JSONObject jsonObject) {
				recommendUserList = new ArrayList<UserCard>();
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					List<RecommendUserList> userLists = JSON.parseArray(
							returnObj.getString("ListOut"), RecommendUserList.class);
					for (RecommendUserList userList : userLists) {
						UserCard recommendUser = userList.getUserCard();
						recommendUserList.add(recommendUser);
					}
					if(!recommendUserList.isEmpty()&&recommendUserList != null){
						postInfo.setRecommendUser(recommendUserList);
						notifyDataSetChanged();
						Log.e("IndexFragmentAdapter", "新一批推荐用户："+postInfo.getRecommendUser().toString());
						lastUserId = postInfo.getRecommendUser().get(recommendUserList.size()-1).getUserSeqId();
					}else{
						ToastUtils.showMessage(context, "没有推荐用户了");
					}
				}else{
					ToastUtils.showMessage(context, "获取失败，请检查网络");
				}
			}
			
			@Override
			public void start() {
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {	
			}

			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				ToastUtils.showMessage(context, "更换失败，请检查网络");
			
			}
		});
	}

	/**
	 * 设置头像认证标记的显示
	 * @param userLevel
	 * @param iv_headphoto_icon
	 */
	private void setHeadIcon(String userLevel, ImageView iv_headphoto_icon) {
		if(userLevel.equals("11")){
			iv_headphoto_icon.setVisibility(View.VISIBLE);
		}else{
			iv_headphoto_icon.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取多层转发时转发文字
	 * @param content
	 * @param userLevel 
	 * @param userType 
	 * @param srcPostInfo 
	 * @param position 
	 * @param pContent
	 * @param pUserName
	 * @param pUserId
	 * @return
	 */
	private SpannableString getContentText(String content,
			String userName, final String userSeqId, String userLevel, final String userType, final SrcPostInfo srcPostInfo, final int position) {
		String name = userName;
		StringBuffer sb_content = new StringBuffer(name);
		int start = 0;
		int end = userName.length();
		int icon_start = userName.length();
		int icon_end = userName.length();
		Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_yellow_v_24);
		ImageSpan imgSpan = new ImageSpan(context, b, DynamicDrawableSpan.ALIGN_BASELINE);
		if(userLevel.equals("11")){
			sb_content.append(" icon");
			icon_start += 1;
			icon_end = sb_content.length();
		}
		
		if(content != null){		
			sb_content.append(":"+content);
		}
		Log.e("indexFragmentAdapter", "转发文字："+sb_content.toString());
		Log.e("indexFragmentAdapter", "开始："+start+"结束："+end);
		SpannableString spanStr = new SpannableString(sb_content);
		spanStr.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				if(userSeqId.equals(Constant.userInfo.getUserSeqId())){
					skipToSelfFragment();
				}else if(userType.equals("1")&&!userSeqId.equals(userId)){
					skipToSystemUserActivity(userSeqId);
				}else{
					if(!userSeqId.equals(userId)){		
						skipToOtherPeopleActivity(userSeqId);
					}
				}
			}
			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(false);
			}
		}, start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		spanStr.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				IndexFragment.ISSRCPOST = true;
				IndexFragment.SRC_POSEID = srcPostInfo.getPostId();
				skipToPostDetail(srcPostInfo.getPostType(),srcPostInfo.getPostId(),srcPostInfo.getUserSeqId(),position);
			}
			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(false);
			}
		}, icon_end,sb_content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		spanStr.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.link_text_color)), start, end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanStr.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.font_titleanduname)), icon_end,sb_content.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		if(userLevel.equals("11")){	
			spanStr.setSpan(imgSpan, icon_start, icon_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return spanStr;
	}
	
	private SimplifySpanBuild getContentText(TextView tv_post_text, String content, String userName,
			final String userSeqId, String userLevel, final String userType,final PostInfo postInfo, final int position) {
		SimplifySpanBuild simplifySpanBuild = new SimplifySpanBuild(context, tv_post_text);
		simplifySpanBuild.appendSpecialUnit(new SpecialTextUnit(userName, context.getResources().getColor(R.color.link_text_color)).setSpecialClickableUnit(new SpecialClickableUnit(new OnClickableSpanListener() {
			
			@Override
			public void onClick(TextView tv, String clickText) {
				// TODO Auto-generated method stub
				if(userSeqId.equals(Constant.userInfo.getUserSeqId())){
					skipToSelfFragment();
				}else if(userType.equals("1")&&!userSeqId.equals(userId)){
					skipToSystemUserActivity(userSeqId);
				}else{
					if(!userSeqId.equals(userId)){		
						skipToOtherPeopleActivity(userSeqId);
					}
				}
			}
		})));
		if(userLevel.equals("11")){
		    simplifySpanBuild.appendSpecialUnit(new SpecialTextUnit(" ")).appendSpecialUnit(new SpecialLabelUnit("火", context.getResources().getColor(R.color.transparent), 13, BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_yellow_v_24),40,40).setGravity(SpecialGravity.CENTER))
		    .appendSpecialUnit(new SpecialTextUnit(" "));
		}
		if(content != null){		
			simplifySpanBuild.appendSpecialUnit(new SpecialTextUnit(":"+content, context.getResources().getColor(R.color.font_titleanduname)).setSpecialClickableUnit(new SpecialClickableUnit(new OnClickableSpanListener() {
				
				@Override
				public void onClick(TextView tv, String clickText) {
					// TODO Auto-generated method stub
					IndexFragment.ISSRCPOST = true;
					IndexFragment.SRC_POSEID = postInfo.getPostId();
					skipToPostDetail(postInfo.getPostType(),postInfo.getPostId(),postInfo.getUserSeqId(),position);
				}
			})));
		}
		return simplifySpanBuild;
		
//		String name = userName;
//		StringBuffer sb_content = new StringBuffer(name);
//		int start = 0;
//		int end = userName.length();
//		int icon_start = userName.length();
//		int icon_end = userName.length();
//		Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_yellow_v_24);
//		ImageSpan imgSpan = new ImageSpan(context, b, DynamicDrawableSpan.ALIGN_BASELINE);
//		if(userLevel.equals("11")){
//			sb_content.append(" icon");
//			icon_start += 1;
//			icon_end = sb_content.length();
//		}
//		
//		if(content != null){		
//			sb_content.append(":"+content);
//		}
//		Log.e("indexFragmentAdapter", "转发文字："+sb_content.toString());
//		Log.e("indexFragmentAdapter", "开始："+start+"结束："+end);
//		SpannableString spanStr = new SpannableString(sb_content);
//		spanStr.setSpan(new ClickableSpan() {
//			@Override
//			public void onClick(View widget) {
//				if(userSeqId.equals(Constant.userInfo.getUserSeqId())){
//					skipToSelfFragment();
//				}else if(userType.equals("1")&&!userSeqId.equals(userId)){
//					skipToSystemUserActivity(userSeqId);
//				}else{
//					if(!userSeqId.equals(userId)){		
//						skipToOtherPeopleActivity(userSeqId);
//					}
//				}
//			}
//			@Override
//			public void updateDrawState(TextPaint ds) {
//				super.updateDrawState(ds);
//				ds.setUnderlineText(false);
//			}
//		}, start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		
//		spanStr.setSpan(new ClickableSpan() {
//			@Override
//			public void onClick(View widget) {
//				IndexFragment.ISSRCPOST = true;
//				IndexFragment.SRC_POSEID = postInfo.getPostId();
//				skipToPostDetail(postInfo.getPostType(),postInfo.getPostId(),postInfo.getUserSeqId(),position);
//			}
//			@Override
//			public void updateDrawState(TextPaint ds) {
//				super.updateDrawState(ds);
//				ds.setUnderlineText(false);
//			}
//		}, icon_end,sb_content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		
//		spanStr.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.link_text_color)), start, end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		spanStr.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.font_titleanduname)), icon_end,sb_content.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		if(userLevel.equals("11")){	
//			spanStr.setSpan(imgSpan, icon_start, icon_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
//		return null;
	}
	private SpannableString getTrasmitContent(final UserCard userCard, final PostInfo postAbstract,
			final int position, final PostInfo postInfo, boolean isTopTransmit) {
		// TODO Auto-generated method stub
		StringBuffer sb_content = new StringBuffer("");
		int start = 0;
		int end = 0;
		int icon_start = 0;
		int icon_end = 0;
		SpannableString spanStr = null;
		if(isTopTransmit == false){
			String name = userCard.getUserName();
			sb_content.append("//"+name);
			start = 2;
			end = sb_content.length();		
			Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_yellow_v_24);
			ImageSpan imgSpan = new ImageSpan(context, b, DynamicDrawableSpan.ALIGN_BASELINE);
			icon_start = sb_content.length();
			if(userCard.getUserLevel().equals("11")){
				sb_content.append(" icon");
				icon_start += 1;
				icon_end = sb_content.length();
			}
			if(postAbstract.getContent() != null){		
				sb_content.append(":"+postAbstract.getContent());
			}
			spanStr = new SpannableString(sb_content);
			spanStr.setSpan(new ClickableSpan() {
				@Override
				public void onClick(View widget) {
					if(userCard.getUserSeqId().equals(Constant.userInfo.getUserSeqId())){
						skipToSelfFragment();
					}else if(userCard.getUserType().equals("1")&&!userCard.getUserSeqId().equals(userId)){
						skipToSystemUserActivity(userCard.getUserSeqId());
					}else{
						if(!userCard.getUserSeqId().equals(userId)){		
							skipToOtherPeopleActivity(userCard.getUserSeqId());
						}
					}
				}
				@Override
				public void updateDrawState(TextPaint ds) {
					super.updateDrawState(ds);
					ds.setUnderlineText(false);
				}
			}, start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			spanStr.setSpan(new ClickableSpan() {
				@Override
				public void onClick(View widget) {
					IndexFragment.ISSRCPOST = true;
					IndexFragment.SRC_POSEID = postInfo.getPostId();
					skipToPostDetail(postAbstract.getPostType(),postAbstract.getPostId(),userCard.getUserSeqId(),position);
				}
				@Override
				public void updateDrawState(TextPaint ds) {
					super.updateDrawState(ds);
					ds.setUnderlineText(false);
				}
			}, icon_end,sb_content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			spanStr.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.font_titleanduname)), icon_end,sb_content.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);	
			spanStr.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.link_text_color)), start, end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			if(userCard.getUserLevel().equals("11")){	
				spanStr.setSpan(imgSpan, icon_start, icon_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			return spanStr;
		}else{
			if(postAbstract.getContent() != null){		
				sb_content.append(postAbstract.getContent());
				end = sb_content.length();
				icon_end = 0;
				spanStr = new SpannableString(sb_content);
				spanStr.setSpan(new ClickableSpan() {
					@Override
					public void onClick(View widget) {
						IndexFragment.ISSRCPOST = true;
						IndexFragment.SRC_POSEID = postInfo.getPostId();
						skipToPostDetail(postAbstract.getPostType(),postAbstract.getPostId(),userCard.getUserSeqId(),position);
					}
					@Override
					public void updateDrawState(TextPaint ds) {
						super.updateDrawState(ds);
						ds.setUnderlineText(false);
					}
				}, icon_end,sb_content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				spanStr.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.font_titleanduname)), icon_end,sb_content.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			return spanStr;
			
		}
	}

	/**
	 * 切换到个人主页
	 */
	protected void skipToSelfFragment() {
		iPullPostListCallback.skipToSelfFragment();
	}
	/**
	 * 初始化被转发疑难求助、病例分享帖子的控件点击监听
	 * @param rl_src_view_post
	 * @param ll_srcDoctorInfo
	 * @param position 
	 * @param srcPostInfo
	 * @param srcUserCard
	 */
	private void initSrcPostListner(RelativeLayout rl_src_view_post,
			LinearLayout ll_srcDoctorInfo,final int position, final PostInfo postInfo,
			final UserCard userCard) {
		ll_srcDoctorInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(postInfo.getUserSeqId().equals(Constant.userInfo.getUserSeqId())){
					skipToSelfFragment();
				}else if(userCard.getUserType().equals("1")&&!userCard.getUserSeqId().equals(userId)){
					skipToSystemUserActivity(userCard.getUserSeqId());
				}else{
					if(!userCard.getUserSeqId().equals(userId)){	
						skipToOtherPeopleActivity(userCard.getUserSeqId());
					}
				}
			}
		});
		rl_src_view_post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				IndexFragment.ISSRCPOST = true;
				skipToPostDetail(postInfo.getPostType(), postInfo.getPostId(), postInfo.getUserSeqId(),position);
			}
		});
	}
	private void initSrcPostListner(RelativeLayout rl_src_view_post,
			LinearLayout ll_srcDoctorInfo,final int position, final SrcPostInfo srcPostInfo,
			final SrcUserCard srcUserCard) {
		ll_srcDoctorInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isTourist){
					DialogUtil.showGuestDialog(context, null);
				}else{				
					if(srcPostInfo.getUserSeqId().equals(Constant.userInfo.getUserSeqId())){
						skipToSelfFragment();
					}else if(srcUserCard.getUserType().equals("1")&&!srcUserCard.getUserSeqId().equals(userId)){
						skipToSystemUserActivity(srcUserCard.getUserSeqId());
					}else{
						if(!srcUserCard.getUserSeqId().equals(userId)){	
							skipToOtherPeopleActivity(srcUserCard.getUserSeqId());
						}
					}
				}
			}
		});
		rl_src_view_post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if(isTourist){
//					DialogUtil.showGuestDialog(context, null);
//				}else{
				    IndexFragment.ISSRCPOST = true;
			        IndexFragment.SRC_POSEID = srcPostInfo.getPostId();
					skipToPostDetail(srcPostInfo.getPostType(), srcPostInfo.getPostId(), srcPostInfo.getUserSeqId(),position);
//				}
			}
		});
	}
	/**
	 * 初始化被转发疑难求助、病例分享帖子数据
	 * @param iv_src_backgroudpic
	 * @param tv_src_postTitle
	 * @param tv_src_completeRate
	 * @param tv_srcTime 
	 * @param iv_gray 
	 * @param tv_name 
	 * @param srcPostInfo
	 * @param position 
	 * @param srcUserCard2 
	 * @throws Exception 
	 */
	private void initSrcPostInfoData(ImageView iv_src_backgroudpic,
			TextView tv_src_postTitle, TextView tv_src_completeRate,
			TextView tv_srcTime, ImageView iv_gray, TextView tv_srcname, SrcPostInfo srcPostInfo, SrcUserCard srcUserCard, int position) throws Exception {
		SpannableString contentText = getContentText(null,srcUserCard.getUserName(),srcUserCard.getUserSeqId(),srcUserCard.getUserLevel(),srcUserCard.getUserType(),srcPostInfo,position);
		tv_srcname.setText(contentText);
		tv_srcname.setMovementMethod(LinkMovementMethod.getInstance());
		if(srcPostInfo.getBackgroundPic().isEmpty()){
			iv_gray.setVisibility(View.GONE);
		}
		imageloader = new ImageLoaderUtil(context, srcPostInfo.getBackgroundPic(), iv_src_backgroudpic, R.drawable.pic_post_bg, R.drawable.pic_downloadfailed_bg,iv_gray);
		imageloader.showImage();
		if(srcPostInfo.getPostTitle()!=null){
			tv_src_postTitle.setText(srcPostInfo.getPostTitle());
		}
		if(srcPostInfo.getCompleteRate()!=null){
			tv_src_completeRate.setText("完整度"+srcPostInfo.getCompleteRate()+"%");
		}
		if(srcPostInfo.getCreateDate()!=null){
			tv_srcTime.setText(DisplayTimeUtil.displayTimeString(srcPostInfo.getCreateDate()));
		}
	}

	private void initSrcPostInfoData(LinearLayout ll_caseShare, FrameLayout fl_bg, ImageView iv_src_backgroudpic,
			TextView tv_src_postTitle, TextView tv_src_completeRate,
			TextView tv_srcTime, ImageView iv_gray, TextView tv_srcname,
			PostInfo postInfo, UserCard userCard, int position) throws Exception {
		ll_caseShare.setVisibility(View.VISIBLE);
		fl_bg.setVisibility(View.VISIBLE);
		SimplifySpanBuild simplifySpanBuild = getContentText(tv_srcname,null,userCard.getUserName(),userCard.getUserSeqId(),userCard.getUserLevel(),userCard.getUserType(),postInfo,position);
		tv_srcname.setText(simplifySpanBuild.build());
//		SpannableString contentText = getContentText(null,userCard.getUserName(),userCard.getUserSeqId(),userCard.getUserLevel(),userCard.getUserType(),postInfo,position);
//		tv_srcname.setText(contentText);
//		tv_srcname.setMovementMethod(LinkMovementMethod.getInstance());
		if(postInfo.getBackgroundPic().isEmpty()){
			iv_gray.setVisibility(View.GONE);
		}
		imageloader = new ImageLoaderUtil(context, postInfo.getBackgroundPic(), iv_src_backgroudpic, R.drawable.pic_post_bg, R.drawable.pic_downloadfailed_bg,iv_gray);
		imageloader.showImage();
		if(postInfo.getPostTitle()!=null){
			tv_src_postTitle.setText(postInfo.getPostTitle());
		}
		if(postInfo.getCompleteRate()!=null){
			tv_src_completeRate.setText("完整度"+postInfo.getCompleteRate()+"%");
		}
		if(postInfo.getCreateDate()!=null){
			tv_srcTime.setText(DisplayTimeUtil.displayTimeString(postInfo.getCreateDate()));
		}
	}
	/**
	 * 初始化被转发疑难求助帖子信息view
	 */
	private void initSrcPostInfoView(View convertView,
			ViewHolder6 tran_problemHelp_Holder) {
		tran_problemHelp_Holder.tv_src_postTitle = (TextView)convertView.findViewById(R.id.tv_posttitle);
		tran_problemHelp_Holder.tv_src_completeRate = (TextView)convertView.findViewById(R.id.tv_completerate);
		tran_problemHelp_Holder.iv_src_backgroudpic = (ImageView)convertView.findViewById(R.id.iv_casepic);	
		tran_problemHelp_Holder.rl_src_view_post = (RelativeLayout)convertView.findViewById(R.id.view_src_problemhelp);	
		tran_problemHelp_Holder.tv_srcname = (TextView)convertView.findViewById(R.id.tv_srcname);
		tran_problemHelp_Holder.iv_gray = (ImageView)convertView.findViewById(R.id.iv_gray);
		tran_problemHelp_Holder.ll_problemHelp = (LinearLayout)convertView.findViewById(R.id.ll_problemHelp);
		tran_problemHelp_Holder.fl_bg = (FrameLayout)convertView.findViewById(R.id.fl_bg);		
	}
	/**
	 * 初始化被转发病例分享帖子信息view
	 */
	private void initSrcPostInfoView(View convertView,
			ViewHolder5 tran_caseShare_Holder) {
		tran_caseShare_Holder.tv_src_postTitle = (TextView)convertView.findViewById(R.id.tv_posttitle);
		tran_caseShare_Holder.tv_src_completeRate = (TextView)convertView.findViewById(R.id.tv_completerate);
		tran_caseShare_Holder.iv_src_backgroudpic = (ImageView)convertView.findViewById(R.id.iv_casepic);	
		tran_caseShare_Holder.rl_src_view_post = (RelativeLayout)convertView.findViewById(R.id.view_src_caseshare);
		tran_caseShare_Holder.tv_srcname = (TextView)convertView.findViewById(R.id.tv_srcname);
		tran_caseShare_Holder.iv_gray = (ImageView)convertView.findViewById(R.id.iv_gray);
		tran_caseShare_Holder.ll_caseShare = (LinearLayout)convertView.findViewById(R.id.ll_caseshare);
		tran_caseShare_Holder.fl_bg = (FrameLayout)convertView.findViewById(R.id.fl_bg);
	}
	/**
	 * 设置被转发普通帖子的各种点击监听
	 * @param gv_pic
	 * @param ll_src_normal_post
	 * @param ll_doctorInfo
	 * @param tv_src_group_name 
	 * @param srcPostInfo
	 * @param srcUserCard
	 */
	private void initSrcNormalPostListner(IndexGridView gv_pic,
			LinearLayout ll_src_normal_post, LinearLayout ll_doctorInfo, TextView tv_src_group_name,final int position, final SrcPostInfo srcPostInfo, final SrcUserCard srcUserCard) {

		gv_pic.setOnTouchInvalidPositionListener(new OnTouchInvalidPositionListener() {
			
			@Override
			public boolean onTouchInvalidPosition(int motionEvent) {
				return false;
			}
		});
		
		ll_src_normal_post.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				if(isTourist){
//					DialogUtil.showGuestDialog(context, null);
//				}else{
				    IndexFragment.ISSRCPOST = true;
				    IndexFragment.SRC_POSEID = srcPostInfo.getPostId();
					skipToPostDetail(srcPostInfo.getPostType(),srcPostInfo.getPostId(),srcPostInfo.getUserSeqId(),position);		
				}
//			}
		});
		ll_doctorInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isTourist){
					DialogUtil.showGuestDialog(context, null);
				}else{			
					if(srcUserCard.getUserSeqId().equals(Constant.userInfo.getUserSeqId())){
						skipToSelfFragment();
					}else if(srcUserCard.getUserType().equals("1")&&!srcUserCard.getUserSeqId().equals(userId)){
						skipToSystemUserActivity(srcUserCard.getUserSeqId());
					}else{
						if(!srcUserCard.getUserSeqId().equals(userId)){	
							skipToOtherPeopleActivity(srcUserCard.getUserSeqId());
						}
					}
				}
			}
		});
		tv_src_group_name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isTourist){
					DialogUtil.showGuestDialog(context, null);
				}else{				
					Intent intent = new Intent(context, CircleHomepageActivity.class);
					intent.putExtra(CircleHomepageActivity.intent_param_circle_id, srcPostInfo.getCoterieId());
					context.startActivity(intent);
				}
			}
		});
	}
	private void initSrcNormalPostListner(IndexGridView gv_pic,
			LinearLayout ll_src_normal_post, LinearLayout ll_doctorInfo, TextView tv_src_group_name,final int position, final PostInfo postInfo, final UserCard userCard) {

		gv_pic.setOnTouchInvalidPositionListener(new OnTouchInvalidPositionListener() {
			
			@Override
			public boolean onTouchInvalidPosition(int motionEvent) {
				return false;
			}
		});
		
		ll_src_normal_post.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				skipToPostDetail(postInfo.getPostType(),postInfo.getPostId(),postInfo.getUserSeqId(),position);		
			}
		});
		ll_doctorInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(userCard.getUserSeqId().equals(Constant.userInfo.getUserSeqId())){
					skipToSelfFragment();
				}else if(userCard.getUserType().equals("1")&&!userCard.getUserSeqId().equals(userId)){
					skipToSystemUserActivity(userCard.getUserSeqId());
				}else{
					if(!userCard.getUserSeqId().equals(userId)){	
						skipToOtherPeopleActivity(userCard.getUserSeqId());
					}
				}
			}
		});
		tv_src_group_name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CircleHomepageActivity.class);
				intent.putExtra(CircleHomepageActivity.intent_param_circle_id, postInfo.getCoterieId());
				context.startActivity(intent);
			}
		});
	}
	protected void SrcskipToShowBigPicActivity(SrcPostInfo srcPostInfo,
			int position) {
		Intent intent = new Intent(context, ShowBigPicActivity.class);
		intent.putExtra("imgs", srcPostInfo.getPicList());
		intent.putExtra("position", position);
		String title = (position+1)+"/"+srcPostInfo.getPicList().length;
		intent.putExtra("title", title);
		context.startActivity(intent);
	}
	/**
	 * 初始化被转发普通帖子信息
	 * @param tv_post_text
	 * @param gv_pic
	 * @param tv_time
	 * @param srcPostInfo
	 * @param srcUserCard 
	 * @param position 
	 * @throws Exception 
	 */
	private void initSrcNormalPostData(TextView tv_post_text, GridView gv_pic,
			TextView tv_time, final SrcPostInfo srcPostInfo, final SrcUserCard srcUserCard, int position) throws Exception {
		if(srcPostInfo.getContent()!=null){
			SpannableString contentText = getContentText(srcPostInfo.getContent(),srcUserCard.getUserName(),srcUserCard.getUserSeqId(),srcUserCard.getUserLevel(),srcUserCard.getUserType(),srcPostInfo,position);
			tv_post_text.setText(contentText);
			tv_post_text.setMovementMethod(LinkMovementMethod.getInstance());
		}
		if(srcPostInfo.getPicList()!=null&&!srcPostInfo.getPicList()[0].isEmpty()){
			int screenWidth = getWindowsWidth();
			if(srcPostInfo.getPicList().length<3){
				gv_pic.setNumColumns(2);
			}else{
				gv_pic.setNumColumns(3);
			}
			IndexPhotoGridViewAdapter gridViewAdapter = new IndexPhotoGridViewAdapter(context, srcPostInfo.getPicList(),screenWidth,new IPhotoClickOper() {
				
				@Override
				public void clickoper(int position,Context context) {
					Intent intent = new Intent(context,IndexGalleryActivity.class);
					intent.putExtra("position", position);
					intent.putExtra("imgs", srcPostInfo.getPicList());
					intent.putExtra("title", (position+1)+"/"+srcPostInfo.getPicList().length);
					
					intent.putExtra("needOper", false);
					
					intent.putExtra("contextText", "");
					
					context.startActivity(intent);
				}
			});
			gv_pic.setAdapter(gridViewAdapter);
			gv_pic.setVisibility(View.VISIBLE);
		}else{
			gv_pic.setVisibility(View.GONE);
		}
		if(srcPostInfo.getCreateDate()!= null){
			tv_time.setText(DisplayTimeUtil.displayTimeString(srcPostInfo.getCreateDate()));
		}
	}
	private void initSrcNormalPostData(TextView tv_post_text, GridView gv_pic,
			TextView tv_time, final PostInfo postInfo, UserCard userCard,int position) throws Exception {
		if(postInfo.getContent()!=null){
			SimplifySpanBuild simplifySpanBuild = getContentText(tv_post_text,postInfo.getContent(),userCard.getUserName(),userCard.getUserSeqId(),userCard.getUserLevel(),userCard.getUserType(),postInfo,position);
			tv_post_text.setText(simplifySpanBuild.build());
//			SpannableString contentText = getContentText(postInfo.getContent(),userCard.getUserName(),userCard.getUserSeqId(),userCard.getUserLevel(),userCard.getUserType(),postInfo,position);
//			tv_post_text.setText(contentText);
//			tv_post_text.setMovementMethod(LinkMovementMethod.getInstance());
//			tv_post_text.setText(postInfo.getContent());
		}
		if(postInfo.getPicList()!=null&&!postInfo.getPicList()[0].isEmpty()){
			int screenWidth = getWindowsWidth();
			if(postInfo.getPicList().length<3){
				gv_pic.setNumColumns(2);
			}else{
				gv_pic.setNumColumns(3);
			}
			IndexPhotoGridViewAdapter gridViewAdapter = new IndexPhotoGridViewAdapter(context, postInfo.getPicList(),screenWidth,new IPhotoClickOper() {
				
				@Override
				public void clickoper(int position,Context context) {
					Intent intent = new Intent(context,IndexGalleryActivity.class);
					intent.putExtra("position", position);
					intent.putExtra("imgs", postInfo.getPicList());
					intent.putExtra("title", (position+1)+"/"+postInfo.getPicList().length);
					
					intent.putExtra("needOper", false);
					
					intent.putExtra("contextText", "");
					
					context.startActivity(intent);
				}
			});
			gv_pic.setAdapter(gridViewAdapter);
			gv_pic.setVisibility(View.VISIBLE);
		}else{
			gv_pic.setVisibility(View.GONE);
		}
		if(postInfo.getCreateDate()!= null){
			tv_time.setText(DisplayTimeUtil.displayTimeString(postInfo.getCreateDate()));
		}
	}
	
	/**
	 * 初始化被转发帖子用户信息
	 * @param iv_smallPhoto
	 * @param tv_doctorName
	 * @param tv_jobTitle
	 * @param tv_hospital
	 * @param tv_department
	 * @param iv_src_headphoto_icon 
	 * @param tv_time
	 * @param srcUserCard
	 */
	private void initSrcUserData(ImageView iv_smallPhoto,
			TextView tv_doctorName, TextView tv_jobTitle, TextView tv_hospital,
			TextView tv_department, ImageView iv_src_headphoto_icon, SrcUserCard srcUserCard) {
		setHeadIcon(srcUserCard.getUserLevel(),iv_src_headphoto_icon);
		if(srcUserCard.getUserFaceUrl()!=null){
			try {
				imageloader = new ImageLoaderUtil(context, srcUserCard.getUserFaceUrl(), iv_smallPhoto, R.drawable.icon_headfailed, R.drawable.icon_headfailed);
				imageloader.showImage();
			} catch (Exception e) {
				Log.e("ImageLoader", e.toString());
			}
		}
		if(srcUserCard.getUserName()!=null){
			tv_doctorName.setText(srcUserCard.getUserName());
		}
		if(srcUserCard.getProfessionalTitle()!=null){
			tv_jobTitle.setText(srcUserCard.getProfessionalTitle());
		}
		if(srcUserCard.getCompanyName()!=null){
			tv_hospital.setText(srcUserCard.getCompanyName());
		}
		if(srcUserCard.getDeptName()!=null){
			tv_department.setText(srcUserCard.getDeptName());
		}
		if(srcUserCard.getCompanyName()!=null){
			tv_hospital.setText(srcUserCard.getCompanyName());
		}
		
		
	}
	private void initSrcUserData(ImageView iv_smallPhoto,
			TextView tv_doctorName, TextView tv_jobTitle, TextView tv_hospital,
			TextView tv_department, ImageView iv_src_headphoto_icon, UserCard userCard) {
		setHeadIcon(userCard.getUserLevel(),iv_src_headphoto_icon);
		if(userCard.getUserFaceUrl()!=null){
			try {
				imageloader = new ImageLoaderUtil(context, userCard.getUserFaceUrl(), iv_smallPhoto, R.drawable.icon_headfailed, R.drawable.icon_headfailed);
				imageloader.showImage();
			} catch (Exception e) {
				Log.e("ImageLoader", e.toString());
			}
		}
		if(userCard.getUserName()!=null){
			tv_doctorName.setText(userCard.getUserName());
		}
		if(userCard.getProfessionalTitle()!=null){
			tv_jobTitle.setText(userCard.getProfessionalTitle());
		}
		if(userCard.getCompanyName()!=null){
			tv_hospital.setText(userCard.getCompanyName());
		}
		if(userCard.getDeptName()!=null){
			tv_department.setText(userCard.getDeptName());
		}
		if(userCard.getCompanyName()!=null){
			tv_hospital.setText(userCard.getCompanyName());
		}
	}
	/**
	 * 点赞
	 * @param position
	 * @param like
	 * @param userSeqId
	 * @param postId
	 * @param mineLike
	 */
	protected void clickLike(int position, RelativeLayout like, String userSeqId,
			String postId, boolean mineLike) {
		ImageView i_Like = (ImageView) like.getChildAt(1);
		TextView t_like = (TextView)like.getChildAt(0);
		int like_num = postOtherList.get(position).getLIKE_NUM();
		sendPostLike(context,like,i_Like,t_like,userSeqId,postId,like_num,position,mineLike);
	}
	/**
	 * 跳转到显示大图页面
	 * @param postInfo
	 * @param position
	 */
	protected void skipToShowBigPicActivity(PostInfo postInfo, int position) {
		Intent intent = new Intent(context, ShowBigPicActivity.class);
		intent.putExtra("imgs", postInfo.getPicList());
		intent.putExtra("position", position);
		String title = (position+1)+"/"+postInfo.getPicList().length;
		intent.putExtra("title", title);
		context.startActivity(intent);
	}
	/**
	 * 根据帖子类型跳转到帖子详情
	 * @param postType
	 * @param postId
	 * @param userSeqId
	 * @param position 
	 * @param position 
	 */
	protected void skipToPostDetail(String postType, String postId,
			String userSeqId, int position) {
			if(postType.equals(Constant.CASE_SHARE)){
				Intent intent = new Intent(context, CaseDetailActivity.class);
				intent.putExtra(Constant.POST_ID, postId);
				intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
				intent.putExtra("ptype",Constant.CASE_SHARE);
				intent.putExtra("position", position);
				intent.putExtra("flag", flag);
				if(indexFragment != null){
					indexFragment.startActivityForResult(intent, 100);
				}else{
					mainActivity.startActivityForResult(intent, 100);
				}		
			}else if(postType.equals(Constant.PROBLEM_HELP)){
				Intent intent = new Intent(context, CaseDetailActivity.class);
				intent.putExtra(Constant.POST_ID, postId);
				intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
				intent.putExtra("ptype",Constant.PROBLEM_HELP);
				intent.putExtra("position", position);
				intent.putExtra("flag", flag);
				if(indexFragment != null){
					indexFragment.startActivityForResult(intent, 100);
				}else{
					mainActivity.startActivityForResult(intent, 100);
				}
			}else if(postType.equals(Constant.NORMAL_POST)||postType.equals(Constant.CIRCLE_POST)){
				Intent intent = new Intent(context, PostDetailActivity.class);
				intent.putExtra(Constant.POST_ID, postId);
				intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
				intent.putExtra("position", position);
				intent.putExtra("flag", flag);
				if(indexFragment != null){
					indexFragment.startActivityForResult(intent, 100);
				}else{
					mainActivity.startActivityForResult(intent, 100);
				}
			}else if(postType.equals(Constant.TRANSMIT_POST)||postType.equals(Constant.TRANSMIT_POST2)){
				Intent intent = new Intent(context, PostDetailFowardActivity.class);
				intent.putExtra(Constant.POST_ID, postId);
				intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
				intent.putExtra("position", position);
				intent.putExtra("flag", flag);
				if(indexFragment != null){
					indexFragment.startActivityForResult(intent, 100);
				}else{
					mainActivity.startActivityForResult(intent, 100);
				}
			}

	}
	protected void skipToRepeatActivity(String userSeqId, String postTitle,
			String userFaceUrl, String postId, String userName, String postType, String postLevel, String srcPostId, int position, int forward_num,int flag) {
		Intent intent = new Intent(context, RepeatActivity.class);
		intent.putExtra(Constant.USER_SEQ_ID, userFaceUrl);
		intent.putExtra(Constant.POST_TEXT, postTitle);
		intent.putExtra(Constant.PIC_URL, userFaceUrl);
		intent.putExtra(Constant.POST_ID, postId);
		intent.putExtra(Constant.USER_NAME, userName);
		intent.putExtra(Constant.POST_TYPE, postType);
		intent.putExtra(Constant.POST_LEVEL, postLevel);
		intent.putExtra(Constant.SRC_POST_ID, srcPostId);
		intent.putExtra(Constant.POSITION, position);
		intent.putExtra(Constant.FORWARD_NUM, forward_num);
		intent.putExtra("flag", flag);
		if(indexFragment != null){
			indexFragment.startActivityForResult(intent, 200);
		}else{
			mainActivity.startActivityForResult(intent, 200);
		}
	}
	/**
	 * 跳转到第三方主页
	 * @param userSeqId
	 */
	protected void skipToOtherPeopleActivity(String userSeqId) {
		Log.e("indexFragmentAdapter", "跳转到第三方主页");
		Intent intent = new Intent(context, OtherPeopleActivity.class);
		intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
		context.startActivity(intent);
	}
	/**
	 * 初始化评论、转发、点赞栏的view(postType:202 srcPostType:102)
	 * @param convertView
	 * @param viewHolder
	 */
	private void initOtherInfoView(View convertView, ViewHolder6 tran_problemHelp_Holder) {
		tran_problemHelp_Holder.tv_transmit = (TextView)convertView.findViewById(R.id.tv_transmit);
		tran_problemHelp_Holder.tv_command = (TextView)convertView.findViewById(R.id.tv_command);
		tran_problemHelp_Holder.tv_like = (TextView)convertView.findViewById(R.id.tv_like);
		tran_problemHelp_Holder.rl_transmit = (RelativeLayout)convertView.findViewById(R.id.rl_transmit);
		tran_problemHelp_Holder.rl_command = (RelativeLayout)convertView.findViewById(R.id.rl_command);
		tran_problemHelp_Holder.rl_like = (RelativeLayout)convertView.findViewById(R.id.rl_like);	
		tran_problemHelp_Holder.iv_like = (ImageView)convertView.findViewById(R.id.iv_like);
	}
	/**
	 * 初始化评论、转发、点赞栏的view(postType:202 srcPostType:103)
	 * @param convertView
	 * @param viewHolder
	 */
	private void initOtherInfoView(View convertView, ViewHolder5 tran_caseShare_Holder) {
		tran_caseShare_Holder.tv_transmit = (TextView)convertView.findViewById(R.id.tv_transmit);
		tran_caseShare_Holder.tv_command = (TextView)convertView.findViewById(R.id.tv_command);
		tran_caseShare_Holder.tv_like = (TextView)convertView.findViewById(R.id.tv_like);
		tran_caseShare_Holder.rl_transmit = (RelativeLayout)convertView.findViewById(R.id.rl_transmit);
		tran_caseShare_Holder.rl_command = (RelativeLayout)convertView.findViewById(R.id.rl_command);
		tran_caseShare_Holder.rl_like = (RelativeLayout)convertView.findViewById(R.id.rl_like);	
		tran_caseShare_Holder.iv_like = (ImageView)convertView.findViewById(R.id.iv_like);
	}
	/**
	 * 初始化评论、转发、点赞栏的view(postType:202 srcPostType:101)
	 * @param convertView
	 * @param viewHolder
	 */
	private void initOtherInfoView(View convertView, ViewHolder4 tran_normalPost_Holder) {
		tran_normalPost_Holder.tv_transmit = (TextView)convertView.findViewById(R.id.tv_transmit);
		tran_normalPost_Holder.tv_command = (TextView)convertView.findViewById(R.id.tv_command);
		tran_normalPost_Holder.tv_like = (TextView)convertView.findViewById(R.id.tv_like);
		tran_normalPost_Holder.rl_transmit = (RelativeLayout)convertView.findViewById(R.id.rl_transmit);
		tran_normalPost_Holder.rl_command = (RelativeLayout)convertView.findViewById(R.id.rl_command);
		tran_normalPost_Holder.rl_like = (RelativeLayout)convertView.findViewById(R.id.rl_like);	
		tran_normalPost_Holder.iv_like = (ImageView)convertView.findViewById(R.id.iv_like);
	}
	/**
	 * 初始化评论、转发、点赞栏的view(postType:101)
	 * @param convertView
	 * @param viewHolder
	 */
	private void initOtherInfoView(View convertView, ViewHolder3 normalPost_Holder) {
		normalPost_Holder.tv_transmit = (TextView)convertView.findViewById(R.id.tv_transmit);
		normalPost_Holder.tv_command = (TextView)convertView.findViewById(R.id.tv_command);
		normalPost_Holder.tv_like = (TextView)convertView.findViewById(R.id.tv_like);
		normalPost_Holder.rl_transmit = (RelativeLayout)convertView.findViewById(R.id.rl_transmit);
		normalPost_Holder.rl_command = (RelativeLayout)convertView.findViewById(R.id.rl_command);
		normalPost_Holder.rl_like = (RelativeLayout)convertView.findViewById(R.id.rl_like);	
		normalPost_Holder.iv_like = (ImageView)convertView.findViewById(R.id.iv_like);
	}
	/**
	 * 初始化帖子内容的view(postType:202 srcPostType:102)
	 * @param convertView
	 * @param viewHolder
	 */
	private void initPostInfoView(View convertView, ViewHolder6 tran_problemHelp_Holder) {
		tran_problemHelp_Holder.tv_post_text = (TransmitTextView)convertView.findViewById(R.id.tv_post_text);
	}
	/**
	 * 初始化帖子内容的view(postType:202 srcPostType:103)
	 * @param convertView
	 * @param viewHolder
	 */
	private void initPostInfoView(View convertView, ViewHolder5 tran_caseShare_Holder) {
		tran_caseShare_Holder.tv_post_text = (TransmitTextView)convertView.findViewById(R.id.tv_post_text);
	}
	/**
	 * 初始化帖子内容的view(postType:202 srcPostType:101)
	 * @param convertView
	 * @param viewHolder
	 */
	private void initPostInfoView(View convertView, ViewHolder4 tran_normalPost_Holder) {
		tran_normalPost_Holder.tv_post_text = (TransmitTextView)convertView.findViewById(R.id.tv_post_text);
		tran_normalPost_Holder.ll_transmit_content = (LinearLayout)convertView.findViewById(R.id.ll_transmit_content);
	}
	/**
	 * 初始化顶部圈子view
	 * @param convertView
	 * @param normalPost_Holder
	 */
	private void initGroupView(View convertView, ViewHolder3 normalPost_Holder) {
		normalPost_Holder.ll_group = (LinearLayout)convertView.findViewById(R.id.ll_group);
		normalPost_Holder.tv_group_name = (TextView)convertView.findViewById(R.id.tv_group_name);
	}
	/**
	 * 初始化帖子内容的view(postType:101)
	 * @param convertView
	 * @param viewHolder
	 */
	private void initPostInfoView(View convertView, ViewHolder3 normalPost_Holder) {
		normalPost_Holder.tv_post_text = (TextView)convertView.findViewById(R.id.tv_post_text);
		normalPost_Holder.ll_view_normal_post = (LinearLayout)convertView.findViewById(R.id.view_normal_post);
		normalPost_Holder.gv_pic = (IndexGridView)convertView.findViewById(R.id.gv_pic);
	}
	/**
	 * 初始化显示医生信息的view(postType:202  srcPostType:101)
	 * @param convertView
	 * @param viewHolder4
	 */
	private void initDoctorInfoView(View convertView, ViewHolder6 tran_problemHelp_Holder) {
		tran_problemHelp_Holder.iv_smallPhoto = (ImageView)convertView.findViewById(R.id.iv_smallphoto);
		tran_problemHelp_Holder.tv_doctorName = (TextView)convertView.findViewById(R.id.tv_name);
		tran_problemHelp_Holder.tv_jobTitle = (TextView)convertView.findViewById(R.id.tv_jobtitle);
		tran_problemHelp_Holder.tv_hospital = (TextView)convertView.findViewById(R.id.tv_hospital);
		tran_problemHelp_Holder.tv_department = (TextView)convertView.findViewById(R.id.tv_department);
		tran_problemHelp_Holder.tv_time = (TextView)convertView.findViewById(R.id.tv_posttime);
		tran_problemHelp_Holder.ll_doctorInfo = (LinearLayout)convertView.findViewById(R.id.view_doctorinfo);
		tran_problemHelp_Holder.iv_headphoto_icon = (ImageView)convertView.findViewById(R.id.iv_yellow_icon);
	}
	/**
	 * 初始化显示医生信息的view(postType:202  srcPostType:101)
	 * @param convertView
	 * @param viewHolder4
	 */
	private void initDoctorInfoView(View convertView, ViewHolder5 tran_caseShare_Holder) {
		tran_caseShare_Holder.iv_smallPhoto = (ImageView)convertView.findViewById(R.id.iv_smallphoto);
		tran_caseShare_Holder.tv_doctorName = (TextView)convertView.findViewById(R.id.tv_name);
		tran_caseShare_Holder.tv_jobTitle = (TextView)convertView.findViewById(R.id.tv_jobtitle);
		tran_caseShare_Holder.tv_hospital = (TextView)convertView.findViewById(R.id.tv_hospital);
		tran_caseShare_Holder.tv_department = (TextView)convertView.findViewById(R.id.tv_department);
		tran_caseShare_Holder.tv_time = (TextView)convertView.findViewById(R.id.tv_posttime);
		tran_caseShare_Holder.ll_doctorInfo = (LinearLayout)convertView.findViewById(R.id.view_doctorinfo);
		tran_caseShare_Holder.iv_headphoto_icon = (ImageView)convertView.findViewById(R.id.iv_yellow_icon);
	}
	/**
	 * 初始化显示医生信息的view(postType:202  srcPostType:101)
	 * @param convertView
	 * @param viewHolder4
	 */
	private void initDoctorInfoView(View convertView, ViewHolder4 tran_normalpost_Holder) {
		tran_normalpost_Holder.iv_smallPhoto = (ImageView)convertView.findViewById(R.id.iv_smallphoto);
		tran_normalpost_Holder.tv_doctorName = (TextView)convertView.findViewById(R.id.tv_name);
		tran_normalpost_Holder.tv_jobTitle = (TextView)convertView.findViewById(R.id.tv_jobtitle);
		tran_normalpost_Holder.tv_hospital = (TextView)convertView.findViewById(R.id.tv_hospital);
		tran_normalpost_Holder.tv_department = (TextView)convertView.findViewById(R.id.tv_department);
		tran_normalpost_Holder.tv_time = (TextView)convertView.findViewById(R.id.tv_posttime);
		tran_normalpost_Holder.ll_doctorInfo = (LinearLayout)convertView.findViewById(R.id.view_doctorinfo);
		tran_normalpost_Holder.iv_headphoto_icon = (ImageView)convertView.findViewById(R.id.iv_yellow_icon);
	}
	/**
	 * 初始化显示医生信息的view(postType:102)
	 * @param convertView
	 * @param viewHolder
	 */
	private void initDoctorInfoView(View convertView, ViewHolder3 normalPost_Holder) {
		normalPost_Holder.iv_smallPhoto = (ImageView)convertView.findViewById(R.id.iv_smallphoto);
		normalPost_Holder.tv_doctorName = (TextView)convertView.findViewById(R.id.tv_name);
		normalPost_Holder.tv_jobTitle = (TextView)convertView.findViewById(R.id.tv_jobtitle);
		normalPost_Holder.tv_hospital = (TextView)convertView.findViewById(R.id.tv_hospital);
		normalPost_Holder.tv_department = (TextView)convertView.findViewById(R.id.tv_department);
		normalPost_Holder.tv_time = (TextView)convertView.findViewById(R.id.tv_posttime);
		normalPost_Holder.ll_doctorInfo = (LinearLayout)convertView.findViewById(R.id.view_doctorinfo);
		normalPost_Holder.iv_headphoto_icon = (ImageView)convertView.findViewById(R.id.iv_yellow_icon);
	}
	/**
	 * 初始化帖子内容的view(postType:102)
	 * @param convertView
	 * @param viewHolder3
	 */
	private void initPostInfoView(View convertView, ViewHolder2 problemHelp_Holder) {
		problemHelp_Holder.tv_postTitle = (TextView)convertView.findViewById(R.id.tv_posttitle);
		problemHelp_Holder.tv_completeRate = (TextView)convertView.findViewById(R.id.tv_completerate);
		problemHelp_Holder.iv_backgroudpic = (ImageView)convertView.findViewById(R.id.iv_casepic);	
		problemHelp_Holder.rl_view_post = (RelativeLayout)convertView.findViewById(R.id.view_post);
		problemHelp_Holder.iv_gray = (ImageView)convertView.findViewById(R.id.iv_gray);
	}
	/**
	 * 初始化帖子内容的view(postType:103)
	 * @param convertView
	 * @param viewHolder3
	 */
	private void initPostInfoView(View convertView, ViewHolder caseShare_Holder) {
		caseShare_Holder.tv_postTitle = (TextView)convertView.findViewById(R.id.tv_posttitle);
		caseShare_Holder.tv_completeRate = (TextView)convertView.findViewById(R.id.tv_completerate);
		caseShare_Holder.iv_backgroudpic = (ImageView)convertView.findViewById(R.id.iv_casepic);	
		caseShare_Holder.rl_view_post = (RelativeLayout)convertView.findViewById(R.id.view_post);
		caseShare_Holder.iv_gray = (ImageView)convertView.findViewById(R.id.iv_gray);
	}
	/**
	 * 初始化评论、转发、点赞栏的view(postType:103)
	 * @param convertView
	 * @param viewHolder
	 */
	private void initOtherInfoView(View convertView, ViewHolder2 problemHelp_Holder) {
		problemHelp_Holder.tv_transmit = (TextView)convertView.findViewById(R.id.tv_transmit);
		problemHelp_Holder.tv_command = (TextView)convertView.findViewById(R.id.tv_command);
		problemHelp_Holder.tv_like = (TextView)convertView.findViewById(R.id.tv_like);
		problemHelp_Holder.rl_transmit = (RelativeLayout)convertView.findViewById(R.id.rl_transmit);
		problemHelp_Holder.rl_command = (RelativeLayout)convertView.findViewById(R.id.rl_command);
		problemHelp_Holder.rl_like = (RelativeLayout)convertView.findViewById(R.id.rl_like);
		problemHelp_Holder.iv_like = (ImageView)convertView.findViewById(R.id.iv_like);
	}
	/**
	 * 初始化评论、转发、点赞栏的view(postType:103)
	 * @param convertView
	 * @param viewHolder
	 */
	private void initOtherInfoView(View convertView, ViewHolder caseShare_Holder) {
		caseShare_Holder.tv_transmit = (TextView)convertView.findViewById(R.id.tv_transmit);
		caseShare_Holder.tv_command = (TextView)convertView.findViewById(R.id.tv_command);
		caseShare_Holder.tv_like = (TextView)convertView.findViewById(R.id.tv_like);
		caseShare_Holder.rl_transmit = (RelativeLayout)convertView.findViewById(R.id.rl_transmit);
		caseShare_Holder.rl_command = (RelativeLayout)convertView.findViewById(R.id.rl_command);
		caseShare_Holder.rl_like = (RelativeLayout)convertView.findViewById(R.id.rl_like);
		caseShare_Holder.iv_like = (ImageView)convertView.findViewById(R.id.iv_like);
	}
	/**
	 * 初始化显示医生信息的view(postType:202 srcPostType:102)
	 * @param convertView
	 * @param viewHolder3
	 */
	private void initSrcDoctorInfoView(View convertView, ViewHolder6 tran_problemHelp_Holder) {
		tran_problemHelp_Holder.iv_srcSmallPhoto = (ImageView)convertView.findViewById(R.id.iv_src_smallphoto);
		tran_problemHelp_Holder.tv_srcDoctorName = (TextView)convertView.findViewById(R.id.tv_src_name);
		tran_problemHelp_Holder.tv_srcJobTitle = (TextView)convertView.findViewById(R.id.tv_src_jobtitle);
		tran_problemHelp_Holder.tv_srcHospital = (TextView)convertView.findViewById(R.id.tv_src_hospital);
		tran_problemHelp_Holder.tv_srcDepartment = (TextView)convertView.findViewById(R.id.tv_src_department);
		tran_problemHelp_Holder.tv_srcTime = (TextView)convertView.findViewById(R.id.tv_src_posttime);
		tran_problemHelp_Holder.ll_srcDoctorInfo = (LinearLayout)convertView.findViewById(R.id.view_src_doctorinfo);
		tran_problemHelp_Holder.iv_src_headphoto_icon = (ImageView)convertView.findViewById(R.id.iv_src_yellow_icon);
	}
	/**
	 * 初始化显示医生信息的view(postType:202 srcPostType:103)
	 * @param convertView
	 * @param viewHolder3
	 */
	private void initSrcDoctorInfoView(View convertView, ViewHolder5 tran_caseShare_Holder) {
		tran_caseShare_Holder.iv_srcSmallPhoto = (ImageView)convertView.findViewById(R.id.iv_src_smallphoto);
		tran_caseShare_Holder.tv_srcDoctorName = (TextView)convertView.findViewById(R.id.tv_src_name);
		tran_caseShare_Holder.tv_srcJobTitle = (TextView)convertView.findViewById(R.id.tv_src_jobtitle);
		tran_caseShare_Holder.tv_srcHospital = (TextView)convertView.findViewById(R.id.tv_src_hospital);
		tran_caseShare_Holder.tv_srcDepartment = (TextView)convertView.findViewById(R.id.tv_src_department);
		tran_caseShare_Holder.tv_srcTime = (TextView)convertView.findViewById(R.id.tv_src_posttime);
		tran_caseShare_Holder.ll_srcDoctorInfo = (LinearLayout)convertView.findViewById(R.id.view_src_doctorinfo);
		tran_caseShare_Holder.iv_src_headphoto_icon = (ImageView)convertView.findViewById(R.id.iv_src_yellow_icon);
	}
	/**
	 * 初始化显示医生信息的view(postType:202 srcPostType:101)
	 * @param convertView
	 * @param viewHolder3
	 */
	private void initSrcDoctorInfoView(View convertView, ViewHolder4 tran_normalPost_Holder) {
		tran_normalPost_Holder.iv_srcSmallPhoto = (ImageView)convertView.findViewById(R.id.iv_src_smallphoto);
		tran_normalPost_Holder.tv_srcDoctorName = (TextView)convertView.findViewById(R.id.tv_src_name);
		tran_normalPost_Holder.tv_srcJobTitle = (TextView)convertView.findViewById(R.id.tv_src_jobtitle);
		tran_normalPost_Holder.tv_srcHospital = (TextView)convertView.findViewById(R.id.tv_src_hospital);
		tran_normalPost_Holder.tv_srcDepartment = (TextView)convertView.findViewById(R.id.tv_src_department);
		tran_normalPost_Holder.tv_srcTime = (TextView)convertView.findViewById(R.id.tv_src_posttime);
		tran_normalPost_Holder.ll_srcDoctorInfo = (LinearLayout)convertView.findViewById(R.id.view_src_doctorinfo);
		tran_normalPost_Holder.iv_src_headphoto_icon = (ImageView)convertView.findViewById(R.id.iv_src_yellow_icon);
	}
	/**
	 * 初始化显示医生信息的view(postType:103)
	 * @param convertView
	 * @param viewHolder3
	 */
	private void initDoctorInfoView(View convertView, ViewHolder2 problemHelp_Holder) {
		problemHelp_Holder.iv_smallPhoto = (ImageView)convertView.findViewById(R.id.iv_smallphoto);
		problemHelp_Holder.tv_doctorName = (TextView)convertView.findViewById(R.id.tv_name);
		problemHelp_Holder.tv_jobTitle = (TextView)convertView.findViewById(R.id.tv_jobtitle);
		problemHelp_Holder.tv_hospital = (TextView)convertView.findViewById(R.id.tv_hospital);
		problemHelp_Holder.tv_department = (TextView)convertView.findViewById(R.id.tv_department);
		problemHelp_Holder.tv_time = (TextView)convertView.findViewById(R.id.tv_posttime);
		problemHelp_Holder.ll_doctorInfo = (LinearLayout)convertView.findViewById(R.id.view_doctorinfo);
		problemHelp_Holder.iv_headphoto_icon = (ImageView)convertView.findViewById(R.id.iv_yellow_icon);
	}
	/**
	 * 初始化显示医生信息的view(postType:103)
	 * @param convertView
	 * @param viewHolder3
	 */
	private void initDoctorInfoView(View convertView, ViewHolder caseShare_Holder) {
		caseShare_Holder.iv_smallPhoto = (ImageView)convertView.findViewById(R.id.iv_smallphoto);
		caseShare_Holder.tv_doctorName = (TextView)convertView.findViewById(R.id.tv_name);
		caseShare_Holder.tv_jobTitle = (TextView)convertView.findViewById(R.id.tv_jobtitle);
		caseShare_Holder.tv_hospital = (TextView)convertView.findViewById(R.id.tv_hospital);
		caseShare_Holder.tv_department = (TextView)convertView.findViewById(R.id.tv_department);
		caseShare_Holder.tv_time = (TextView)convertView.findViewById(R.id.tv_posttime);
		caseShare_Holder.ll_doctorInfo = (LinearLayout)convertView.findViewById(R.id.view_doctorinfo);
		caseShare_Holder.iv_headphoto_icon = (ImageView)convertView.findViewById(R.id.iv_yellow_icon);
	}


	/**
	 * 点赞后发消息给服务端
	 * @param context2
	 * @param like 
	 * @param i_Like
	 * @param t_like
	 * @param postId
	 * @param userId
	 * @param like_num
	 * @param position
	 * @param isMyLike
	 */
	protected void sendPostLike(final Context context2, final RelativeLayout like, final ImageView i_Like, final TextView t_like, String srcUserId, String srcPostId, final int like_num, final int position, final boolean isMyLike) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "sendPostLike");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userId", userId);
		busiParams.put("srcUserId", srcUserId);
		busiParams.put("srcPostId", srcPostId);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			
			@Override
			public void success(JSONObject jsonObject) {
				like.setClickable(true);
				like.setFocusable(true);
				like.setFocusableInTouchMode(true);
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					if(isMyLike){
						if(like_num <= 1){
							t_like.setText(context.getResources().getString(R.string.like_text));
							postOtherList.get(position).setLIKE_NUM(0);
						}else{
							t_like.setText(""+(like_num-1));
							postOtherList.get(position).setLIKE_NUM((like_num-1));
						}
						i_Like.setImageResource(R.drawable.icon_laud_24);
						postOtherList.get(position).setMineLike(false);
						notifyDataSetChanged();
					}else{
						i_Like.setImageResource(R.drawable.icon_laud_active_24);
						t_like.setText((like_num+1)+"");
						postOtherList.get(position).setLIKE_NUM((like_num+1));
						postOtherList.get(position).setMineLike(true);
						notifyDataSetChanged();
					}
					
				}else{
					ToastUtils.showMessage(context2, "点赞失败，请检查网络");
				}
			}
			
			@Override
			public void start() {
				like.setClickable(false);
				like.setFocusable(false);
				like.setFocusableInTouchMode(false);
			}
			
			@Override
			public void loading(long total, long current, boolean isUploading) {
			}
			
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				like.setClickable(true);
				like.setFocusable(true);
				like.setFocusableInTouchMode(true);
				ToastUtils.showMessage(context2, "点赞失败，请检查网络");
			}
		});
	}
	/**
	 * 病例分享的viewHolder
	 * @author liyi
	 *
	 */
	class ViewHolder{
		ImageView iv_smallPhoto,iv_backgroudpic,iv_like,iv_headphoto_icon,iv_gray;
		TextView tv_doctorName,tv_jobTitle,tv_hospital,tv_time,tv_department,
		tv_transmit,tv_command,tv_like,tv_postTitle,tv_completeRate;
		RelativeLayout rl_transmit,rl_command,rl_like,rl_view_post;
		LinearLayout ll_doctorInfo,ll_caseshare_post;
	}
	/**
	 * 疑难求助的viewHolder
	 * @author liyi
	 *
	 */
	class ViewHolder2{
		ImageView iv_smallPhoto,iv_backgroudpic,iv_like,iv_headphoto_icon,iv_gray;
		TextView tv_doctorName,tv_jobTitle,tv_hospital,tv_time,tv_department,
		tv_transmit,tv_command,tv_like,tv_postTitle,tv_completeRate;
		RelativeLayout rl_transmit,rl_command,rl_like,rl_view_post;
		LinearLayout ll_doctorInfo;
	}
	/**
	 * 普通帖子的viewHolder
	 * @author liyi
	 *
	 */
	class ViewHolder3{
		ImageView iv_smallPhoto,iv_like,iv_headphoto_icon,iv_gray;
		TextView tv_doctorName,tv_jobTitle,tv_hospital,tv_time,tv_department,
		tv_transmit,tv_command,tv_like,tv_post_text,tv_group_name;
		RelativeLayout rl_transmit,rl_command,rl_like;
		IndexGridView gv_pic;
		LinearLayout ll_doctorInfo,ll_view_normal_post,ll_group,ll_normal_post;
	}
	/**
	 * 转发普通帖子的viewHolder
	 * @author MSI
	 *
	 */
	class ViewHolder4{
		ImageView iv_smallPhoto,iv_srcSmallPhoto,iv_like,iv_headphoto_icon,iv_src_headphoto_icon;
		TextView tv_doctorName,tv_srcDoctorName,tv_jobTitle,tv_srcJobTitle,tv_hospital,tv_srcHospital,tv_time,tv_srcTime,
		tv_department,tv_srcDepartment,tv_transmit,tv_command,tv_like,
		tv_src_post_text,tv_src_group_name;
		LinearLayout ll_doctorInfo,ll_srcDoctorInfo,ll_src_normal_post,ll_src_group,ll_transmit_content;
		RelativeLayout rl_transmit,rl_command,rl_like;
		IndexGridView gv_src_pic;
		TransmitTextView tv_post_text;
	}
	/**
	 * 转发病例分享帖子的viewHolder
	 * @author MSI
	 *
	 */
	class ViewHolder5{
		ImageView iv_smallPhoto,iv_srcSmallPhoto,iv_like,iv_src_backgroudpic,iv_headphoto_icon,iv_src_headphoto_icon,iv_gray;
		TextView tv_doctorName,tv_srcDoctorName,tv_jobTitle,tv_srcJobTitle,tv_hospital,tv_srcHospital,tv_time,tv_srcTime,
		tv_department,tv_srcDepartment,tv_transmit,tv_command,tv_like,
		tv_src_postTitle,tv_src_completeRate,tv_srcname;
		LinearLayout ll_doctorInfo,ll_srcDoctorInfo,ll_caseShare;
		RelativeLayout rl_transmit,rl_command,rl_like,rl_src_view_post;
		TransmitTextView tv_post_text;
		FrameLayout fl_bg;
	}
	/**
	 * 转发疑难求助分享帖子的viewHolder
	 * @author MSI
	 *
	 */
	class ViewHolder6{
		ImageView iv_smallPhoto,iv_srcSmallPhoto,iv_like,iv_src_backgroudpic,iv_headphoto_icon,iv_src_headphoto_icon,iv_gray;
		TextView tv_doctorName,tv_srcDoctorName,tv_jobTitle,tv_srcJobTitle,tv_hospital,tv_srcHospital,tv_time,tv_srcTime,
		tv_department,tv_srcDepartment,tv_transmit,tv_command,tv_like,
		tv_src_postTitle,tv_src_completeRate,tv_srcname;
		LinearLayout ll_doctorInfo,ll_srcDoctorInfo,ll_problemHelp;
		RelativeLayout rl_transmit,rl_command,rl_like,rl_src_view_post;
		TransmitTextView tv_post_text;
		FrameLayout fl_bg;
	}
	/**
	 * 推荐关注
	 * @author MSI
	 *
	 */
	class ViewHolder7{
		Button bt_change;
		ListView lv_recommend_doctor;
	}
	class ViewHolder8{

	}

}
