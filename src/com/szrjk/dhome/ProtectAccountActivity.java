package com.szrjk.dhome;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.RegisterInfo;
import com.szrjk.fire.FireEye;
import com.szrjk.fire.Result;
import com.szrjk.fire.StaticPattern;
import com.szrjk.fire.ValuePattern;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.DesUtil;
import com.szrjk.util.DjsonUtils;
import com.szrjk.util.KeyWordUtils;
import com.szrjk.util.ToastUtils;

@ContentView(R.layout.activity_protectaccount)
public class ProtectAccountActivity extends BaseActivity implements
		OnClickListener
{

	public static final String  TAG = "ProtectAccountActivity";
	
	@ViewInject(R.id.ll_rootlayout)
	private LinearLayout ll_rootlayout;
	
	@ViewInject(R.id.sv_scroll)
	private ScrollView sv_scroll;

	@ViewInject(R.id.text_confirmpwd)
	private EditText text_confirmpwd;
	
	@ViewInject(R.id.iv_password_hiddenorshow)
	private ImageView iv_password_hiddenorshow;

	@ViewInject(R.id.text_password)
	private EditText text_password;

	@ViewInject(R.id.iv_confirmpwd_hiddenorshow)
	private ImageView iv_confirmpwd_hiddenorshow;
	
	@ViewInject(R.id.btn_continue)
	private Button btn_continue;

	private Dialog dialog = null;
	private boolean paddwordIsHidden=true;
	private boolean confirmpwdIsHidden=true;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		ViewUtils.inject(this);
		addRegisterActivitys(this);
		initLayout();
	}

	private void initLayout()
	{
//		text_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		text_password.addTextChangedListener(mTextWatcher);
		
		text_confirmpwd.addTextChangedListener(mTextWatcher);
		btn_continue.setOnClickListener(this);
		iv_password_hiddenorshow.setOnClickListener(this);
		iv_confirmpwd_hiddenorshow.setOnClickListener(this);
	}

	TextWatcher mTextWatcher = new TextWatcher() {  
		private CharSequence temp;  
		@Override  
		public void onTextChanged(CharSequence s, int start, int before, int count) {  
			temp = s;  
		}  
		@Override  
		public void beforeTextChanged(CharSequence s, int start, int count,  
				int after) {  
		}  
		@Override  
		public void afterTextChanged(Editable s) {  
			if (temp.length()>=1) {
				btn_continue.setEnabled(true);
				btn_continue.setFocusable(true);
			}else {
				btn_continue.setEnabled(false);
				btn_continue.setFocusable(false);
			}  
		}  
	};  
	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.btn_continue:
				funcclick1();
				break;
			case R.id.iv_password_hiddenorshow:
				funcclick2();
				break;
			case R.id.iv_confirmpwd_hiddenorshow:
				funcclick3();
				break;
		}
	}

	private void funcclick2() {
		KeyWordUtils.pullKeywordTop(ProtectAccountActivity.this, R.id.ll_rootlayout, R.id.btn_continue, R.id.sv_scroll);
		if (paddwordIsHidden) {
			paddwordIsHidden=false;
			text_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
			iv_password_hiddenorshow.setImageResource(R.drawable.icon_hidden);
		}else {
			paddwordIsHidden=true;
			text_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			iv_password_hiddenorshow.setImageResource(R.drawable.icon_show);
		}
	}

	private void funcclick3() {
		if (confirmpwdIsHidden) {
			//隐藏密码
			confirmpwdIsHidden=false;
			text_confirmpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
			iv_confirmpwd_hiddenorshow.setImageResource(R.drawable.icon_hidden);
		}else {
			//显示密码
			confirmpwdIsHidden=true;
			text_confirmpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			iv_confirmpwd_hiddenorshow.setImageResource(R.drawable.icon_show);
		}
	}

	private void funcclick1()
	{
		FireEye fireEye = new FireEye(this);
		fireEye.add(text_password, StaticPattern.Required.setMessage("请输入密码"));
		fireEye.add(text_password, ValuePattern.MinLength.setValue(6),
				ValuePattern.MaxLength.setValue(20));

		Result result = fireEye.test();
		if (!result.passed) { 
			
			return; }

		if (text_confirmpwd.getText().toString().equals("")) {
			// 没输入确认密码
			Toast.makeText(getApplicationContext(), "请输入确定密码!",Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!text_password.getText().toString()
				.equals(text_confirmpwd.getText().toString()))
		{
			// 两个密码不一致
			Toast.makeText(getApplicationContext(), "两次输入密码不一致，请重新输入!",Toast.LENGTH_SHORT).show();
			text_password.setText("");
			text_confirmpwd.setText("");
			return;
		}
		// 提交注册数据
		final DHomeApplication dHomeApplication = (DHomeApplication) getApplication();

		final String pwd = text_password.getText().toString();

		RegisterInfo rInfo = dHomeApplication.getRegisterInfo();

//		Log.e(TAG,DjsonUtils.bean2Json(rInfo));

		final String userAccount = rInfo.getPhone();
		String ciphertext = DesUtil.enString(pwd);
		
		rInfo.setPasswd(ciphertext);

		final String userType = rInfo.getUserType();
		Log.i("rInfo", rInfo.toString());

		// 提交
		// 调用注册接口
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "newUserRegister");
		Map<String, Object> busiParams = DjsonUtils.bean2Map(rInfo);
		paramMap.put("BusiParams", busiParams);

		// 调用接口
		httpPost(paramMap, new AbstractDhomeRequestCallBack()
		{
			@Override
			public void start()
			{
				dialog = createDialog(ProtectAccountActivity.this, "请稍候...");
				dialog.show();
			}

			@Override
			public void loading(long total, long current, boolean isUploading)
			{
//				ToastUtils
			}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject)
			{
				try {
					dialog.dismiss();
					ToastUtils.showMessage(ProtectAccountActivity.this, "" + jsonObject.getString("ErrorMessage"));
				} catch (Exception e){
					if (e instanceof SocketTimeoutException) {
						ToastUtils.showMessage(ProtectAccountActivity.this, "服务器响应超时，请返回等陆！");
						btn_continue.setEnabled(false);
					}
				}
			
			}

			@Override
			public void success(JSONObject jsonObject)
			{
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				ToastUtils.showMessage(ProtectAccountActivity.this, ""+ errorObj.getErrorMessage());
				if(!errorObj.getReturnCode().equals("0000")){
					//如果失败，则不继续
					dialog.dismiss();
					return ;
				}


				//注册成功后登陆
				LoginHelper.doLogin(userAccount, pwd, Constant.LOGIN_TYPE_PHONE, ProtectAccountActivity.this, false, new AbstractDhomeRequestCallBack() {
					@Override
					public void start() {
					}
					@Override
					public void loading(long total, long current, boolean isUploading) {
					}

					@Override
					public void failure(HttpException exception, JSONObject jsonObject) {
						dialog.dismiss();
					}
					@Override
					public void success(JSONObject jsonObject) {
						//注册完后  跳转到认证界面
						dialog.dismiss();
						Intent intent = null;
						ErrorInfo errorObj = JSON.parseObject(jsonObject.getString("ErrorInfo"), ErrorInfo.class);
						//ToastUtils.showMessage(ProtectAccountActivity.this, ""+ errorObj.getErrorMessage());
						if("2".equals(userType)||"8".equals(userType)||"9".equals(userType)){
							intent = new Intent(ProtectAccountActivity.this, DoctorActivity.class);
						} else if (userType.equals("3")) {
							intent = new Intent(ProtectAccountActivity.this, StudentActivity.class);
						} else {
							intent = new Intent(ProtectAccountActivity.this, OtherActivity.class);
						}
						startActivity(intent);
						dHomeApplication.finishRegisterActivity();
					}
				});

			}
		});

	}

}
