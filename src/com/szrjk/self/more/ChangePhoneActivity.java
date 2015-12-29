package com.szrjk.self.more;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.ToastUtils;

@ContentView(R.layout.activity_change_phone)
public class ChangePhoneActivity extends BaseActivity implements OnClickListener{

	protected static final int CHECK_PASSWORD_SUCCESS = 0;

	@ViewInject(R.id.et_new_phone)
	private EditText et_new_phone;

	@ViewInject(R.id.et_vcode)
	private EditText et_vcode;

	@ViewInject(R.id.btn_get_code)
	private Button btn_get_code;

	@ViewInject(R.id.btn_complete)
	private Button btn_complete;

	private ChangePhoneActivity instance;

	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		ViewUtils.inject(this);
		instance=this;
		initLayout();
	}

	private void initLayout() {
		userInfo=Constant.userInfo;
		et_new_phone.setFocusable(true);
		et_new_phone.setFocusableInTouchMode(true);
		et_new_phone.requestFocus();
		et_new_phone.setInputType(InputType.TYPE_CLASS_PHONE);
		et_vcode.setInputType(InputType.TYPE_CLASS_PHONE);
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run() 
			{
				InputMethodManager inputManager =
						(InputMethodManager)et_new_phone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(et_new_phone, 0);
			}
		},  
		998);

		et_new_phone.addTextChangedListener(mTextWatcher);
		et_vcode.addTextChangedListener(yTextWatcher);
		btn_complete.setOnClickListener(this);
		btn_get_code.setBackgroundColor(getResources().getColor(R.color.search_bg));
		btn_get_code.setTextColor(getResources().getColor(R.color.white));
		btn_get_code.setOnClickListener(this);
	}

	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer
	{
		public TimeCount(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish()
		{// 计时完毕时触发
			btn_get_code.setText("获取验证码");
			btn_get_code.setEnabled(true);
			// btn_vcode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished)
		{// 计时过程显示

			btn_get_code.setText(millisUntilFinished / 1000 + "秒");
			btn_get_code.setTextColor(getResources().getColor(R.color.font_cell));
			btn_get_code.setBackgroundColor(getResources().getColor(R.color.white));
		}
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
				btn_get_code.setEnabled(true);
				btn_get_code.setFocusable(true);
			}else {
				btn_get_code.setEnabled(false);
				btn_get_code.setFocusable(false);

			}  
		}  
	};  

	TextWatcher yTextWatcher = new TextWatcher() {  
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
				btn_complete.setEnabled(true);
				btn_complete.setFocusable(true);
			}else {
				btn_complete.setEnabled(false);
				btn_complete.setFocusable(false);
			}    
		}  
	};

	private String phone;

	private String tmpvcode;

	private long tmpvcodetime;

	private String phone2;

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{

		case R.id.btn_get_code:
			funcBtnvcode();
			et_vcode.requestFocus();
			break;
		case R.id.btn_complete:
			funcBtnContinue();
			break;
		}
	}  

	public void funcBtnvcode()
	{
		et_vcode.setText("");
		// 生成验证码
		Random r = new Random();
		String vcode = "" + r.nextInt(9) + r.nextInt(9) + r.nextInt(9)
				+ r.nextInt(9)+r.nextInt(9)+r.nextInt(9);//验证码生成
				// 保存验证码
				tmpvcode = "" + vcode;
				tmpvcodetime = new Date().getTime();// 验证码发出时间

				phone = et_new_phone.getText().toString();

				// 发送验证码 ，这里要发送短信
				//			Toast.makeText(getApplicationContext(), "验证码:" + vcode,
				//					Toast.LENGTH_LONG).show();

				// 调用短信接口入参
				HashMap<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("ServiceName", "thirdPartyAuth");
				Map<String, Object> busiParams = new HashMap<String, Object>();
				//			busiParams.put("captcha", tmpvcode);
				busiParams.put("authAccount", phone);
				busiParams.put("deviceId", "123");
				busiParams.put("busiType", "1");
				paramMap.put("BusiParams", busiParams);

				// 调用短信接口
				httpPost(paramMap, new AbstractDhomeRequestCallBack()
				{
					@Override
					public void start()
					{
					}

					@Override
					public void loading(long total, long current, boolean isUploading)
					{
					}

					@Override
					public void failure(HttpException exception, JSONObject jsonObject)
					{
					}

					@Override
					public void success(JSONObject jsonObject)
					{
						ErrorInfo errorObj = JSON.parseObject(
								jsonObject.getString("ErrorInfo"), ErrorInfo.class);
						ToastUtils.showMessage(ChangePhoneActivity.this, ""+ errorObj.getErrorMessage());
					}
				});

				// 填入重新发送 的倒计时
				TimeCount time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
				time.start();
				btn_get_code.setBackgroundColor(getResources().getColor(R.color.base_bg));

	}

	// 完成按钮动作
	public void funcBtnContinue()
	{

		phone2=et_new_phone.getText().toString();
		if (phone2.equals("")) {
			ToastUtils.showMessage(instance, "请输入新手机号码！");
			return;
		}
		if (!phone2.equals(phone)) {
			et_vcode.setText("");
			return;
		}

		// 验证码校验TODO
		String filledvcode = et_vcode.getText().toString();// 用户填的验证码
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "checkVerificationCode");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("authAccount", phone);
		busiParams.put("captcha",filledvcode);
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {

			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					changePhone();
				}else if(errorObj.getReturnCode().equals("1002")){
					ToastUtils.showMessage(instance, "验证码输入错误");
				}else if(errorObj.getReturnCode().equals("1001")){
					ToastUtils.showMessage(instance, "验证码已失效，请重新获取验证码！");
				}
			}

			@Override
			public void start() {
				dialog.show();

			}

			@Override
			public void loading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub

			}

			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				ToastUtils.showMessage(instance, "验证码错误");
				et_vcode.setText("");
				dialog.dismiss();

			}
		});
		//			long now = new Date().getTime();
		//			if (tmpvcode == null || tmpvcode.isEmpty())
		//			{
		//				// 验证码失效
		//				Toast.makeText(getApplicationContext(), "请先填写号码后发送验证码!",
		//						Toast.LENGTH_SHORT).show();
		//				return;
		//			}
		//			else if (now - tmpvcodetime > 120000)
		//			{
		//				Toast.makeText(getApplicationContext(), "验证码过期!",
		//						Toast.LENGTH_SHORT).show();
		//				return;
		//			}
		//			else if (!tmpvcode.equals(filledvcode))
		//			{
		//				Toast.makeText(getApplicationContext(), "验证码错误!",
		//						Toast.LENGTH_SHORT).show();
		//				return;
		//			}
		//			Toast.makeText(getApplicationContext(), "验证码成功!", Toast.LENGTH_SHORT)
		//					.show();

		//			changePhone();


	}

	private void changePhone() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "changePhone");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", userInfo.getUserSeqId());
		busiParams.put("newPhoneNum", phone2);
		busiParams.put("oldPhoneNum", userInfo.getPhone());
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				dialog.dismiss();
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					ToastUtils.showMessage(instance, errorObj.getErrorMessage());
					userInfo.setPhone(phone2);
					setResult(CHECK_PASSWORD_SUCCESS);
					finish();
				}
			}
			@Override
			public void start() {
			}
			@Override
			public void loading(long total, long current,
					boolean isUploading) {
			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				dialog.dismiss();
				ToastUtils.showMessage(instance, "该号码已被注册");
			}
		});
	}
}
