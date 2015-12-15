package com.szrjk.self.more;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.DHomeApplication;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.RegisterInfo;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.DesUtil;
import com.szrjk.util.ToastUtils;

@ContentView(R.layout.activity_change_password)
public class ChangePasswordActivity extends BaseActivity{
	
	@ViewInject(R.id.et_old_password)
	private EditText et_old_password;
	
	@ViewInject(R.id.iv_oldpassword_hiddenorshow)
	private ImageView iv_oldpassword_hiddenorshow;
	
	@ViewInject(R.id.et_new_password)
	private EditText et_new_password;
	
	@ViewInject(R.id.iv_newpassword_hiddenorshow)
	private ImageView iv_newpassword_hiddenorshow;
	
	@ViewInject(R.id.et_new_password_confirm)
	private EditText et_new_password_confirm;
	
	@ViewInject(R.id.iv_newpasswordconfirm_hiddenorshow)
	private ImageView iv_newpasswordconfirm_hiddenorshow;
	
	@ViewInject(R.id.tv_commit)
	private TextView tv_commit;
	
	private boolean oldPasswordIsHidden=true;
	private boolean newPasswordIsHidden=true;
	private boolean newPasswordConfirmIsHidden=true;

	private UserInfo userInfo;
	
	private ChangePasswordActivity instance;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance=this;
		userInfo=Constant.userInfo;
		DHomeApplication dHomeApplication=(DHomeApplication) getApplication();
	}

	@OnClick(R.id.iv_oldpassword_hiddenorshow)
	public void passwordHiddenClick(View view){
		if (oldPasswordIsHidden) {
			//显示密码
			oldPasswordIsHidden=false;
			et_old_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			iv_oldpassword_hiddenorshow.setImageResource(R.drawable.icon_show);
		}else {
			//隐藏密码
			oldPasswordIsHidden=true;
			et_old_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
			iv_oldpassword_hiddenorshow.setImageResource(R.drawable.icon_hidden);
		}
	}
	
	@OnClick(R.id.iv_newpassword_hiddenorshow)
	public void newPasswordHiddenClick(View view){
		if (newPasswordIsHidden) {
			//显示密码
			newPasswordIsHidden=false;
			et_new_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			iv_newpassword_hiddenorshow.setImageResource(R.drawable.icon_show);
		}else {
			//隐藏密码
			newPasswordIsHidden=true;
			et_new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
			iv_newpassword_hiddenorshow.setImageResource(R.drawable.icon_hidden);
		}
	}
	
	@OnClick(R.id.iv_newpasswordconfirm_hiddenorshow)
	public void newPasswordConfirmHiddenClick(View view){
		if (newPasswordConfirmIsHidden) {
			//显示密码
			newPasswordConfirmIsHidden=false;
			et_new_password_confirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			iv_newpasswordconfirm_hiddenorshow.setImageResource(R.drawable.icon_show);
		}else {
			//隐藏密码
			newPasswordConfirmIsHidden=true;
			et_new_password_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
			iv_newpasswordconfirm_hiddenorshow.setImageResource(R.drawable.icon_hidden);
		}
	}
	@OnClick(R.id.tv_commit)
	public void commitClick(View view){
		changePassword();
	}
	private void changePassword() {
		String oldPassword = et_old_password.getText().toString();
		if (oldPassword.equals("")) {
			ToastUtils.showMessage(instance, "请输入旧密码！");
			return;
		}else if (oldPassword.length()<6) {
			ToastUtils.showMessage(instance, "密码要6-20位数！");
			return;
		}
		String newPassword = et_new_password.getText().toString();
		if (newPassword.equals("")) {
			ToastUtils.showMessage(instance, "请输入新密码！");
			return;
		}else if (newPassword.length()<6) {
			ToastUtils.showMessage(instance, "密码要6-20位数！");
			return;
		}
		String newPasswordConfirm = et_new_password_confirm.getText().toString();
		if (newPasswordConfirm.equals("")) {
			ToastUtils.showMessage(instance, "请确认新密码！");
			return;
		}else if (newPasswordConfirm.length()<6) {
			ToastUtils.showMessage(instance, "密码要6-20位数！");
			return;
		}else if (!newPasswordConfirm.equals(newPassword)) {
			ToastUtils.showMessage(instance, "新密码和确认新密码不一致，请重新输入！");
			et_new_password.setText("");
			et_new_password_confirm.setText("");
			return;
		}
			
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "updateUserPass");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("account", userInfo.getPhone());
		busiParams.put("password", DesUtil.enString(oldPassword));
		busiParams.put("newPass", DesUtil.enString(newPassword));
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					ToastUtils.showMessage(instance, errorObj.getErrorMessage());
					instance.finish();
				} 
			}
			public void start() {
			}
			public void loading(long total, long current, boolean isUploading) {
			}
			public void failure(HttpException exception, JSONObject jobj) {
				ToastUtils.showMessage(instance, jobj.getString("ErrorMessage"));
			}
		});
	}
}
