package com.szrjk.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.config.Constant;
import com.szrjk.config.ConstantUser;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.CommentActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.ICallback;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.index.RepeatActivity;
import com.szrjk.util.BusiUtils;
import com.szrjk.util.DialogUtil;
import com.szrjk.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 界面底层跳转操作
 */
public class PostDetailBottomOperLayout extends RelativeLayout
{

	// 转发按钮
	private RelativeLayout btn_transmit;

	// 评论按钮
	private RelativeLayout btn_comment;

	private TextView tv_transmit;

	// 赞按钮
	private RelativeLayout btn_laud;

	private ImageView iv_like;

	private BaseActivity context;

	private String postId ;
	private String userSeqId ;
	private String postText ;
	private String faceurl ;
	private String username ;
	private String userLevel ;
	private String postType ;
	private ICallback iCallback;
	private boolean islike;

	private String postLevel;
	private String srcPostId;




	/**
	 * 初始化后，要init一下这个控件,入参均不可为空
	 * @param postId
	 * @param userSeqId
	 * @param postText
	 * @param faceurl
	 * @param username
	 * @param postType
	 * @param laudCallback
	 */
	public void initData(String postId,String userSeqId,String postText,String faceurl,String username,String userLevel,String postType,ICallback laudCallback,boolean islike,String postLevel,String srcPostId,int isOpen){
		this.postId = postId;
		this.userSeqId = userSeqId;
		this.postText = postText;
		this.faceurl = faceurl;
		this.username = username;
		this.userLevel=userLevel;
		this.postType = postType;
		this.iCallback = laudCallback;
		this.islike = islike;
		this.postLevel = postLevel;
		this.srcPostId = srcPostId;
		changeLaud(islike);


		//TODO,多次转发的，进行屏蔽
		if(postType!=null&&postType.equals(Constant.TRANSMIT_POST)){
//			btn_transmit.setBackgroundColor(Color.GRAY);
//			btn_transmit.setEnabled(false);
			tv_transmit.setTextColor(getResources().getColor(R.color.font_disable));
			btn_transmit.setEnabled(false);
			btn_transmit.setClickable(false);
		}

		//私密圈子
		if(isOpen==2){
//			btn_transmit.setBackgroundColor(Color.GRAY);
//			btn_transmit.setEnabled(false);
			tv_transmit.setTextColor(getResources().getColor(R.color.font_disable));
			btn_transmit.setEnabled(false);
			btn_transmit.setClickable(false);
		}
	}

	private void changeLaud(boolean islike){
		if(islike){
			Drawable drawable = getResources().getDrawable(R.drawable.icon_laud_active);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			iv_like.setImageDrawable(drawable);
		}else{
			Drawable drawable = getResources().getDrawable(R.drawable.icon_laud_24);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			iv_like.setImageDrawable(drawable);
		}
	}



	public PostDetailBottomOperLayout(final Context context, AttributeSet attrs)
	{
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contextView = inflater.inflate(R.layout.layout_post_detail_bottom_oper, this);
		this.context = (BaseActivity) context;

		btn_transmit = (RelativeLayout) contextView.findViewById(R.id.rl_transmit);
		btn_comment = (RelativeLayout) contextView.findViewById(R.id.rl_command);
		btn_laud = (RelativeLayout) contextView.findViewById(R.id.rl_like);
		iv_like = (ImageView) contextView.findViewById(R.id.iv_like);
		tv_transmit = (TextView) contextView.findViewById(R.id.tv_transmit);



		//转发点击
		btn_transmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (BusiUtils.isguest(context)) {
					DialogUtil.showGuestDialog(context, null);
					return;
				}
				PostDetailViewCommentListLayout.btnId=1;
				clickTransmitButton(view);
			}
		});


		//评论
		btn_comment.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (BusiUtils.isguest(context)) {
					DialogUtil.showGuestDialog(context, null);
					return;
				}
				PostDetailViewCommentListLayout.btnId=2;
				clickCommentButton(view);
			}
		});

		//点赞
		btn_laud.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (BusiUtils.isguest(context)) {
					DialogUtil.showGuestDialog(context, null);
					return;
				}
				PostDetailViewCommentListLayout.btnId=3;
				clickLaudButton(view);
			}
		});


	}


	/** 点击转发按钮 */
	public void clickTransmitButton(View view)
	{
		Intent intent = new Intent(context, RepeatActivity.class);
		intent.putExtra(Constant.POST_ID, postId);
		intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
		intent.putExtra(Constant.POST_TEXT, postText);
		intent.putExtra(Constant.PIC_URL, faceurl);
		intent.putExtra(Constant.USER_NAME, username);
		intent.putExtra(ConstantUser.USERLEVEL, userLevel);
		intent.putExtra(Constant.POST_TYPE, postType);
		intent.putExtra(Constant.POST_LEVEL, postLevel);
		intent.putExtra(Constant.SRC_POST_ID, srcPostId);
//		context.startActivity(intent);
		context.startActivityForResult(intent, PostDetailBottomOperLayout.TO_TRANSMIT);
	}


//	private TextView getLabel(String text) {
//		TextView label = new TextView(this);
//		label.setTextColor(Color.BLACK);
//		label.setGravity(Gravity.CENTER);
//		label.setPadding(100, 100,100,100);
//		label.setText(text);
//		return label;
//	}

	/**转发**/
	public final static int TO_TRANSMIT = 3011;

	/**评论**/
	public final static int TO_COMMENT = 3012;


	/** 点击评论按钮 */
	public void clickCommentButton(View view)
	{
		Intent intent = new Intent(context, CommentActivity.class);
		intent.putExtra(Constant.POST_ID, postId);
		intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
		context.startActivityForResult(intent,PostDetailBottomOperLayout.TO_COMMENT);
	}

	/** 点击赞按钮 */
	public void clickLaudButton(View view)
	{
		UserInfo userInfo=Constant.userInfo;
		String userId = userInfo.getUserSeqId();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "sendPostLike");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userId", userId);
		busiParams.put("srcUserId", userSeqId);
		busiParams.put("srcPostId", postId);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {

			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {


//					if(islike){
//						Drawable drawable = getResources().getDrawable(R.drawable.icon_laud_active);
//						drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//						iv_like.setImageDrawable(drawable);
//						iCallback.doCallback(null);
//						ToastUtils.showMessage(context, "点赞成功");
//
//					}else{
//						Drawable drawable = getResources().getDrawable(R.drawable.icon_laud_32);
//						drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//						iv_like.setImageDrawable(drawable);
//						iCallback.doCallback(null);
//						ToastUtils.showMessage(context, "取消点赞成功");
//
//					}
					islike  = !islike;
					changeLaud(islike);
					Map m = new HashMap();
					m.put("islike",islike);
					iCallback.doCallback(m);



				} else {
					ToastUtils.showMessage(context, "点赞失败，请检查网络");
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
				ToastUtils.showMessage(context, "点赞失败，请检查网络");
			}
		});
	}

	public boolean isIslike() {
		return islike;
	}

	public void setIslike(boolean islike) {
		this.islike = islike;
	}

	public RelativeLayout getBtn_laud() {
		return btn_laud;
	}

	public void setBtn_laud(RelativeLayout btn_laud) {
		this.btn_laud = btn_laud;
	}
	
}
