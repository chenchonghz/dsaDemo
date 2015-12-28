package com.szrjk.dhome;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.RelativeLayout;
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
import com.szrjk.util.ToastUtils;

@ContentView(R.layout.activity_register2)
public class Register2Activity extends BaseActivity implements OnClickListener
{
	@ViewInject(R.id.rl_root)
	private RelativeLayout rl_root;

	@ViewInject(R.id.sv_scroll)
	private ScrollView sv_scroll;

	@ViewInject(R.id.btn_vcode)
	private Button btn_vcode;

	@ViewInject(R.id.btn_continue)
	private Button btn_continue;

	@ViewInject(R.id.text_phonenum)
	private EditText text_phonenum;

	@ViewInject(R.id.text_vcode)
	private EditText text_vcode;

	/**
	 * 验证码,存储
	 */
	//	private static String tmpvcode;
	//	private static long tmpvcodetime;

	Resources resources;

	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		ViewUtils.inject(this);

		addRegisterActivitys(this);
		text_phonenum.setFocusable(true);
		text_phonenum.setFocusableInTouchMode(true);
		text_phonenum.requestFocus();
		text_phonenum.setInputType(InputType.TYPE_CLASS_PHONE);
		text_vcode.setInputType(InputType.TYPE_CLASS_PHONE);
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run() 
			{
				InputMethodManager inputManager =
						(InputMethodManager)text_phonenum.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(text_phonenum, 0);
			}
		},  
		998);
		initLayout();
	}

	private void initLayout()
	{
		text_phonenum.addTextChangedListener(mTextWatcher);
		text_vcode.addTextChangedListener(yTextWatcher);
		btn_continue.setOnClickListener(this);
		btn_vcode.setOnClickListener(this);

	}

	@Override
	public void onClick(View view)
	{
		// 三个按钮都同一个动作，这个应该有更好的方式来处理的
		switch (view.getId())
		{

		case R.id.btn_vcode:
			funcBtnvcode();
			text_vcode.requestFocus();
			break;
		case R.id.btn_continue:
			funcBtnContinue();
			break;
		}
	}

	// 继续按钮动作
	public void funcBtnContinue()
	{
		// 设置验证规则
		FireEye fireEye = new FireEye(this);
		fireEye.add(text_vcode, StaticPattern.Required.setMessage("请输入验证码"),
				StaticPattern.Digits);
		fireEye.add(text_vcode, ValuePattern.MaxLength.setValue(6),
				ValuePattern.MinLength.setValue(6));
		Result result = fireEye.test();
		if (!result.passed) { return; }

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "checkVerificationCode");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("authAccount", phone);
		busiParams.put("captcha", text_vcode.getText().toString());
		paramMap.put("BusiParams", busiParams);

		// 调用短信接口
		httpPost(paramMap, new AbstractDhomeRequestCallBack()
		{
			@Override
			public void start()
			{
				dialog.show();
			}

			@Override
			public void loading(long total, long current, boolean isUploading)
			{
			}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject)
			{
				//				ErrorInfo.put("ErrorInfo", str);
				ToastUtils.showMessage(Register2Activity.this,jsonObject.getString("ErrorInfo"));
				dialog.dismiss();
			}

			@Override
			public void success(JSONObject jsonObject)
			{
				//				ErrorInfo errorObj = JSON.parseObject(
				//						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				//				ToastUtils.showMessage(Register2Activity.this, ""+ errorObj.getErrorMessage());
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					dialog.dismiss();
					// 保存数据,手机号码
					DHomeApplication dHomeApplication = (DHomeApplication) getApplication();
					RegisterInfo registerInfo = dHomeApplication.getRegisterInfo();
					registerInfo.setPhone(text_phonenum.getText().toString());

					// 跳转
					Intent intent1 = new Intent(Register2Activity.this,
							AboutYouActivity.class);
					startActivity(intent1);
				}else if(errorObj.getReturnCode().equals("0001")){
					ToastUtils.showMessage(Register2Activity.this, "验证码输入错误");
				}
			}
		});


		//		String phone2=text_phonenum.getText().toString();
		//		if (!phone2.equals(phone)) {
		//			text_vcode.setText("");
		//			return;
		//		}

		// 验证码校验TODO
		//		String filledvcode = text_vcode.getText().toString();// 用户填的验证码
		//		long now = new Date().getTime();
		//		if (tmpvcode == null || tmpvcode.isEmpty())
		//		{
		//			// 验证码失效
		//			Toast.makeText(getApplicationContext(), "请先填写号码后发送验证码!",
		//					Toast.LENGTH_SHORT).show();
		//			return;
		//		}
		//		else if (now - tmpvcodetime > 120000)
		//		{
		//			Toast.makeText(getApplicationContext(), "验证码过期!",
		//					Toast.LENGTH_SHORT).show();
		//			return;
		//		}
		//		else if (!tmpvcode.equals(filledvcode))
		//		{
		//			Toast.makeText(getApplicationContext(), "验证码错误!",
		//					Toast.LENGTH_SHORT).show();
		//			return;
		//		}
		//		Toast.makeText(getApplicationContext(), "验证码成功!", Toast.LENGTH_SHORT)
		//				.show();

		//		// 保存数据,手机号码
		//		DHomeApplication dHomeApplication = (DHomeApplication) getApplication();
		//		RegisterInfo registerInfo = dHomeApplication.getRegisterInfo();
		//		registerInfo.setPhone(text_phonenum.getText().toString());
		//
		//		// 跳转
		//		Intent intent1 = new Intent(Register2Activity.this,
		//				AboutYouActivity.class);
		//		startActivity(intent1);

	}

	public void funcBtnvcode()
	{
		// 设置验证规则
		FireEye fireEye = new FireEye(this);
		fireEye.add(text_phonenum,
				StaticPattern.Required.setMessage("手机号码不可为空"),
				StaticPattern.Mobile);
		Result result = fireEye.test();
		if (!result.passed) { return; }

		// 生成验证码
		//		Random r = new Random();
		//		String vcode = "" + r.nextInt(9) + r.nextInt(9) + r.nextInt(9)
		//				+ r.nextInt(9)+r.nextInt(9)+r.nextInt(9);//验证码生成
		//		// 保存验证码
		//		tmpvcode = "" + vcode;
		//		tmpvcodetime = new Date().getTime();// 验证码发出时间

		phone = text_phonenum.getText().toString();

		// 发送验证码 ，这里要发送短信
		//Toast.makeText(getApplicationContext(), "验证码:" + vcode,
		//		Toast.LENGTH_LONG).show();

		// 调用短信接口入参
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "thirdPartyAuth");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		//		busiParams.put("captcha", tmpvcode);
		busiParams.put("busiType", "1");
		busiParams.put("deviceId", "123");//????
		busiParams.put("authAccount", phone);
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
				//				ErrorInfo.put("ErrorInfo", str);
				ToastUtils.showMessage(Register2Activity.this,jsonObject.getString("ErrorInfo"));
			}

			@Override
			public void success(JSONObject jsonObject)
			{

				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				ToastUtils.showMessage(Register2Activity.this, ""+ errorObj.getErrorMessage());
			}
		});

		// 填入重新发送 的倒计时
		TimeCount time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
		time.start();
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
			btn_vcode.setText("发送验证码");
			btn_vcode.setEnabled(true);
			// btn_vcode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished)
		{// 计时过程显示
			btn_vcode.setEnabled(false);
			btn_vcode.setText(millisUntilFinished / 1000 + "秒");
			btn_vcode.setTextColor(getResources().getColor(R.color.font_cell));
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
				btn_vcode.setEnabled(true);
				btn_vcode.setFocusable(true);
			}else {
				btn_vcode.setEnabled(false);
				btn_vcode.setFocusable(false);
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
				btn_continue.setEnabled(true);
				btn_continue.setFocusable(true);
			}else {
				btn_continue.setEnabled(false);
				btn_continue.setFocusable(false);
			}    
		}  
	};  
}
