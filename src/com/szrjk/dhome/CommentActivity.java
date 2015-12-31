package com.szrjk.dhome;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.CheckTextNumber;
import com.szrjk.util.ShowDialogUtil;
import com.szrjk.widget.BootstrapEditText;
import com.szrjk.widget.HeaderView;
import com.szrjk.widget.PopWindowComment;

import java.util.HashMap;
import java.util.Map;
/**
 * @author ldr、
 * time:2015-12-31 14:44:45
 */
@ContentView(R.layout.activity_comment)
public class CommentActivity extends BaseActivity {

	@ViewInject(R.id.hv_comment)
	private HeaderView hv_comment;
	@ViewInject(R.id.ll_comment)
	private LinearLayout ll_comment;
	private Dialog dialog;
	private CommentActivity instance;
	//返回
	@ViewInject(R.id.iv_back)
	private ImageView iv_back;
	//发布
	@ViewInject(R.id.tv_issue)
	private TextView tv_issue;
	//内容
	@ViewInject(R.id.et_content)
	private BootstrapEditText et_content;
	
	@ViewInject(R.id.tv_content)
	private TextView tv_content;
	
	private Intent intent_data;
	private String post_id;
	private String user_seq_id;
	private UserInfo userInfo;
	private String userId;
	private String pCommentId;
	private String commentLevel;
	private String content;
	private PopWindowComment popWindowComment;
	private InputMethodManager imm;

	@Override
	protected void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		initLayout();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	private void initLayout() {
		//跳转过来的时候要获得用户信息（这里需要用户ID）\
		userInfo = Constant.userInfo;
		userId = userInfo.getUserSeqId();
		//获得传递过来的帖子ID，层级数据
		intent_data = instance.getIntent();
		post_id = intent_data.getStringExtra(Constant.POST_ID);
		pCommentId = intent_data.getStringExtra(Constant.PCOMMENT_ID);
		if (pCommentId == null) {
			System.out.println("pCommentId空");
		}else{
			Log.i("pCommentId", pCommentId);
			commentLevel = intent_data.getStringExtra(Constant.COMMENT_LEVEL);
		}
		
		//这种情况是首页跳过来；无传数据
		if (commentLevel == null) {
			commentLevel = "0" ;
			Log.i("commentLevel", commentLevel);
		}
		//检查字数
		CheckTextNumber.setEditTextChangeListener(et_content, tv_content, 500);
		setHeader();
	}

	private void setHeader() {
		//返回
		hv_comment.setBtnBackOnClick(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});

		//确定
		hv_comment.showTextBtn("发布", new OnClickListener() {
			@Override
			public void onClick(View view) {
				//检测是否输入了内容。如果没问题就提交服务器、销毁ac
				checkData();
			}
		});
	}

	private void checkData() {
		content = et_content.getText().toString().trim();
		if (!TextUtils.isEmpty(content)) {
			if (content.length()>500) {
				showToast(instance, "你输入的内容不能超过500字", 0);
			}else{
				commentData(content);
			}
		}else{
			showToast(instance, "你输入的内容是空的。", 1);
		}
	}
	private void commentData(String content) {
		dialog = ShowDialogUtil.createDialog(instance, "正在提交...");
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "sendPostComment");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		//临时ID
		busiParams.put("userSeqId", userId);
		//		"pCommentId": "",   //父评论ID, 层级为1时,传空即可
		
		busiParams.put("pCommentId", pCommentId);

		//		"commentLevel": "1",  //直接对帖子评论,传1, 对评论的评论则根据评论的层级传>1
		busiParams.put("commentLevel", String.valueOf(Integer.valueOf(commentLevel)+1));
		busiParams.put("content", content);
		busiParams.put("postId", post_id);
		busiParams.put("deviceType", "1");
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void start() {	
				
			}
			@Override
			public void loading(long total, long current, boolean isUploading) {			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {	
				showToast(instance, "评论失败", 0);
			}
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(jsonObject.getString("ErrorInfo"), ErrorInfo.class);

				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))	{
					JSONObject returnObj = jsonObject.getJSONObject("ErrorInfo");
					String msg = returnObj.getString("ErrorMessage");
					Log.e("returnObj", ""+returnObj);
					showToast(instance, msg,	 0);
					dialog.cancel();
					//回到首页，刷新一次；把评论的数目+1
					//或者回到帖子详情哪里
					Intent intent = new Intent();
					setResult(Activity.RESULT_OK, intent);
					instance.finish();
				}
			}
		});
	}
	@Override
	public void onBackPressed()	{
		closeKeyboard();
		content = et_content.getText().toString().trim();
		if (content.length() != 0) {
			popWindowComment = new PopWindowComment(instance, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.tv_comment:
						
						instance.finish();
						
						break;
					}
				}
			});
			popWindowComment.showAtLocation(ll_comment, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
		}else{
			finish();
		}
	}
	private void closeKeyboard() {
		if (imm.isActive() && getCurrentFocus() != null) {
			if (getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
}
