package com.szrjk.dhome;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.config.Constant;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.fire.FireEye;
import com.szrjk.fire.Result;
import com.szrjk.fire.StaticPattern;
import com.szrjk.fire.TextViewLoader;
import com.szrjk.fire.ValuePattern;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.DesUtil;
import com.szrjk.util.MD5Util;
import com.szrjk.util.RandomUtil;
import com.szrjk.util.ToastUtils;
import com.szrjk.validator.AccountValidation;
import com.szrjk.validator.CodeValidation;
import com.szrjk.validator.EditTextValidator;
import com.szrjk.validator.PasswordValidation;
import com.szrjk.validator.ValidationModel;

@ContentView(R.layout.activity_forget)
public class ForgetActivity extends BaseActivity
{
	@ViewInject(R.id.rl_editall)
	private RelativeLayout rl_editall; 
	@ViewInject(R.id.et_phone)
	private EditText et_phone;
	@ViewInject(R.id.et_code)
	private EditText et_code;
	@ViewInject(R.id.et_pwd)
	private EditText et_pwd;
	@ViewInject(R.id.et_repwd)
	private EditText et_repwd;
	@ViewInject(R.id.btnCode)
	private Button btnCode;
	@ViewInject(R.id.btnSubmit)
	private Button btnSubmit;
	private EditTextValidator textValidator;
	@ViewInject(R.id.lly_content)
	private LinearLayout lly_content;
	@ViewInject(R.id.iv_password_hiddenorshow)
	private ImageView iv_pwd_hiddenorshow;
	@ViewInject(R.id.iv_repwd_hiddenorshow)
	private ImageView iv_repwd_hiddenorshow;
	private boolean pwdishidden;
	private boolean repwdishidden;
	private ForgetActivity instance;
	private FireEye fireEye;
	private Resources resources;
	private String phone, pwd, code,re_pwd;
	private String captcha;
	private TimeCount time;
	private String  error = "0006";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		fireEye = new FireEye(instance);
		initLayout();
	}

	private void initLayout()
	{	
		resources = getResources();
		fireEye.add(et_phone,
				StaticPattern.Required.setMessage("手机号码不可为空"),
				StaticPattern.Mobile);
		textValidator = new EditTextValidator(instance);
		textValidator.setButton(btnCode);
		textValidator
		.add(new ValidationModel(et_phone, new AccountValidation()));
		textValidator.execute();
		pwdishidden =true;
		repwdishidden = true;
	}

	@OnClick(R.id.btnCode)
	//发送验证码
	public void btnCode(View v)
	{
		FireEye fireEye = new FireEye(instance);
		fireEye.add(et_phone,
				StaticPattern.Required.setMessage("手机号码不可为空"),
				StaticPattern.Mobile);
		Result result = fireEye.test();
		phone = et_phone.getText().toString();
		if (result.passed)
		{
			captcha = RandomUtil.generateCode();
			sendCode();
			//			ToastUtils.showMessage(instance, captcha);
			time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
			time.start();
		}
	}

	@OnClick(R.id.iv_password_hiddenorshow)
	public void pwdshow(View v){
		if (pwdishidden) {
			pwdishidden=false;
			et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			iv_pwd_hiddenorshow.setImageResource(R.drawable.icon_show);
		}else {
			pwdishidden=true;
			et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
			iv_pwd_hiddenorshow.setImageResource(R.drawable.icon_hidden);
		}
	}
	@OnClick(R.id.iv_repwd_hiddenorshow)
	public void repwdshow(View v){
		if (repwdishidden) {
			repwdishidden=false;
			et_repwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			iv_repwd_hiddenorshow.setImageResource(R.drawable.icon_show);
		}else {
			repwdishidden=true;
			et_repwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
			iv_repwd_hiddenorshow.setImageResource(R.drawable.icon_hidden);
		}
	}

	@OnClick(R.id.btnSubmit)
	public void btnSubmit(View v)
	{
		FireEye fireEye = new FireEye(instance);
		fireEye.add(et_phone,
				StaticPattern.Required.setMessage("手机号码不可为空"),
				StaticPattern.Mobile);
		final Result result = fireEye.test();
		if (!result.passed) {
			return;
		}


		/*if(TextUtils.isEmpty(et_pwd.getText().toString())){
			et_pwd.setError("密码不能为空");
			return;
		}else{
			if(TextUtils.isEmpty(et_repwd.getText().toString())){
				et_repwd.setError("密码不能为空");
				return;
			}else{
				pwd = et_pwd.getText().toString();
				re_pwd = et_repwd.getText().toString();
			}
		}*/
		FireEye fireEye1 = new FireEye(instance);
		fireEye1.add(et_pwd,
				StaticPattern.Required.setMessage("密码不能为空"),
				StaticPattern.DigitsLetters);
		fireEye1.add(et_repwd,
				StaticPattern.Required.setMessage("密码不能为空"),
				StaticPattern.DigitsLetters);
		if (!fireEye1.test().passed) {
		}
		code = et_code.getText().toString();
		pwd = et_pwd.getText().toString();
		re_pwd = et_repwd.getText().toString();
		//接口认证验证码
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "checkVerificationCode");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("authAccount", phone);
		busiParams.put("captcha", code);
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					if (result.passed&&pwd.equals(re_pwd))
					{
						resetPwd();
					}else if(!pwd.equals(re_pwd)){
						ToastUtils.showMessage(instance, "两次密码不一致");
						dialog.dismiss();
						//et_repwd.setError("两次密码不一致");
						et_pwd.setText("");
						et_repwd.setText("");
					}
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
				et_code.setText("");
				dialog.dismiss();
			}
		});
		//		if (result.passed&&code.equals(captcha)&&pwd.equals(re_pwd))
		//		{
		//			resetPwd();
		//		}else if(!code.equals(captcha)){
		//			ToastUtils.showMessage(instance, "验证码错误");
		//			et_code.setText("");
		//			//et_code.setError("验证码错误");
		//		}else if(!pwd.equals(re_pwd)){
		//			ToastUtils.showMessage(instance, "两次密码不一致");
		//			//et_repwd.setError("两次密码不一致");
		//			et_pwd.setText("");
		//			et_repwd.setText("");
		//		}


	}

	/**
	 * 重置密码
	 */
	private void resetPwd()
	{

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "resetUserPwd");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		String strPwd = DesUtil.enString(pwd);
		busiParams.put("newpass", strPwd);
		busiParams.put("account", phone);
		busiParams.put("captcha", code);
		String token;
		try
		{
			token = MD5Util
					.MD5Encode16bit(phone + strPwd + Constant.PRIVATEKEY);
			busiParams.put("token", token);
			paramMap.put("BusiParams", busiParams);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		final Dialog dialog = createDialog(this, "提交中，请稍候...");
		httpPost(paramMap, new AbstractDhomeRequestCallBack()
		{

			@Override
			public void success(JSONObject jsonObject)
			{

				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				ToastUtils.showMessage(instance, errorObj.getErrorMessage());
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					//showToast(instance, "找回密码成功", Toast.LENGTH_SHORT);
					finish();
				}else {
					dialog.dismiss();
				}
			}

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
				dialog.dismiss();
				JSONObject errorObj = jsonObject.getJSONObject("ErrorInfo");
				if (null != errorObj)
				{
					showToast(instance, errorObj.getString("ErrorMessage"),
							Toast.LENGTH_SHORT);
				}
			}
		});
	}

	/**
	 * 发送验证码
	 */
	private void sendCode()
	{
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "thirdPartyAuth");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		//		busiParams.put("captcha", captcha);
		busiParams.put("authAccount", phone);
		busiParams.put("deviceId", "123");
		busiParams.put("busiType", "2");
		paramMap.put("BusiParams", busiParams);
		//		final Dialog dialog = createDialog(this, "发送中，请稍候...");
		httpPost(paramMap, new AbstractDhomeRequestCallBack()
		{

			@Override
			public void success(JSONObject jsonObject)
			{
				dialog.dismiss();
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				Log.i("TAG", errorObj.toString());
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					ToastUtils.showMessage(instance, ""+ errorObj.getErrorMessage());
					fireEye.add(et_pwd, ValuePattern.MinValue.setValue(6));
					fireEye.add(et_repwd, ValuePattern.MinValue.setValue(6));
					fireEye.add(et_code, ValuePattern.MinValue.setValue(6));
					String reset = resources.getString(R.string.reset_pwd);
					fireEye.add(et_repwd, ValuePattern.Required
							.setMessage(reset), ValuePattern.EqualsTo
							.lazy(new TextViewLoader(et_pwd)));
					textValidator = new EditTextValidator(instance);
					textValidator.setButton(btnSubmit);
					textValidator.add(new ValidationModel(et_phone,
							new AccountValidation()));
					textValidator.add(new ValidationModel(et_code,
							new CodeValidation()));
					textValidator.add(new ValidationModel(et_pwd,
							new PasswordValidation()));
					textValidator.add(new ValidationModel(et_repwd,
							new PasswordValidation()));
					textValidator.execute();
				}
				//				else if(error.equals(errorObj.get("ReturnCode"))){
				//					ToastUtils.showMessage(instance, "此用户不存在，请重新输入");
				//					time.onFinish();
				//				}
			}

			@Override
			public void start()
			{
				//				dialog.show();
			}

			@Override
			public void loading(long total, long current, boolean isUploading)
			{

			}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject)
			{
				dialog.dismiss();
				String errorObj = jsonObject.getString("ErrorInfo");//这是string
				if (null != errorObj)
				{
					//					showToast(instance, errorObj.getString("ErrorMessage"),
					//							Toast.LENGTH_SHORT);
					ToastUtils.showMessage(instance, errorObj);
					time.onFinish();
				}
			}
		});
	}
	class TimeCount extends CountDownTimer
	{
		public TimeCount(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish()
		{// 计时完毕时触发
			btnCode.setText("发送验证码");
			btnCode.setEnabled(true);
			btnCode.setTextColor(getResources().getColor(R.color.white));
			btnCode.setBackgroundColor(getResources().getColor(R.color.global_main));
			// btn_vcode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished)
		{// 计时过程显示
			btnCode.setEnabled(false);
			btnCode.setText(millisUntilFinished / 1000 + "秒");
			btnCode.setTextColor(getResources().getColor(R.color.font_cell));
			btnCode.setBackgroundColor(getResources().getColor(R.color.base_bg));
		}
	}
}
