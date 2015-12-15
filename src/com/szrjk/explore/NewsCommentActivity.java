package com.szrjk.explore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.IPopupItemCallback;
import com.szrjk.entity.PopupItem;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.CheckTextNumber;
import com.szrjk.widget.BootstrapEditText;
import com.szrjk.widget.ListPopup;

@ContentView(R.layout.activity_news_comment)
public class NewsCommentActivity extends BaseActivity {
	
	@ViewInject(R.id.rl_max)
	private RelativeLayout rl_max;
	private NewsCommentActivity instance;
	//返回
	@ViewInject(R.id.iv_back)
	private ImageView iv_back;
	//发布
	@ViewInject(R.id.tv_issue)
	private TextView tv_issue;
	//内容
	@ViewInject(R.id.et_content)
	private BootstrapEditText et_content;
	private String content;
	private String infId;
	@ViewInject(R.id.tv_content)
	private TextView tv_content;
	private InputMethodManager imm;
	private Dialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(instance);
		initViewData();
		initListner();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		progressDialog = createDialog(this, "发送中，请稍候...");
	}

	private void initListner() {
		//回退监听
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				content = et_content.getText().toString().trim();
				if (content.length() != 0) {
					closeKeyboard();
						showPop();
				}else{
					finish();
				}
			}
		});
		tv_issue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkData();
			}
		});
	}

	protected void checkData() {
		content = et_content.getText().toString().trim();
		if (!TextUtils.isEmpty(content)) {
			if (content.length()>500) {
				showToast(instance, "你输入的内容不能超过500字", 0);
			}else{
				tv_issue.setClickable(false);
				progressDialog.show();
				commentData(content);
			}
		}else{
			showToast(instance, "你输入的内容是空的。", 0);
		}
	}

	private void commentData(String content) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "addInfComment");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("infId", infId);
		busiParams.put("userId", Constant.userInfo.getUserSeqId());
		busiParams.put("commentContent", content);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {

			@Override
			public void success(JSONObject jsonObject) {
				progressDialog.dismiss();
				tv_issue.setClickable(true);
				ErrorInfo errorObj = JSON.parseObject(jsonObject.getString("ErrorInfo"), ErrorInfo.class);

				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))	{
					showToast(instance, errorObj.getErrorMessage(), 0);
					instance.finish();
				}else{
					showToast(instance, "评论失败", 0);
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
				progressDialog.dismiss();
				tv_issue.setClickable(true);
				showToast(instance, "评论失败,请检查网络", 0);
			}
		});
	}

	private void initViewData() {
		Intent intent = getIntent();
		infId = intent.getStringExtra("infId");
		//检查字数
		CheckTextNumber.setEditTextChangeListener(et_content, tv_content, 500);
	}
	private void closeKeyboard() {
		if (imm.isActive() && getCurrentFocus() != null) {
			if (getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
	/**显示sendWindow**/
	private void showPop() {
		//		sendWindow = new PostSendPopup(instance, sendPostClick);

		List<PopupItem> pilist = new ArrayList<PopupItem>();
		PopupItem pi1 = new PopupItem();
		pi1.setItemname("放弃评论");//设置名称
		pi1.setColor(this.getResources().getColor(R.color.red));
		pi1.setiPopupItemCallback(new IPopupItemCallback() { //设置click动作
			@Override
			public void itemClickFunc(PopupWindow popupWindow) {
				// 点击“确认”后的操作
				finish(); 
			}
		});
		pilist.add(pi1);
		new ListPopup(instance, pilist, rl_max);
	}

	@Override
	public void onBackPressed() {
		content = et_content.getText().toString().trim();
		if (content.length() != 0) {
			closeKeyboard();
				showPop();
		}else{
			finish();
		}
	}
}
