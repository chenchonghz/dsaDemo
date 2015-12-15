package com.szrjk.self.more;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.DesUtil;
import com.szrjk.util.ToastUtils;

/**身份验证*/
@ContentView(R.layout.activity_authentication)
public class AuthenticationActivity extends BaseActivity{
	@ViewInject(R.id.et_old_password)
	private EditText et_old_password;
	
	@ViewInject(R.id.iv_oldpassword_hiddenorshow)
	private ImageView iv_oldpassword_hiddenorshow;
	
	@ViewInject(R.id.btn_continue)
	private Button btn_continue;
	
	private boolean oldPasswordIsHidden=true;
	
	private AuthenticationActivity instance;

	private UserInfo userInfo;
	
	private boolean isOK;
	
	private static final int CHANGE_PHONE_SUCCESS=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance=this;
		initLayout();
	}

	private void initLayout() {
		userInfo=Constant.userInfo;
		et_old_password.addTextChangedListener(watcher);
	}
	
	TextWatcher watcher=new TextWatcher() {
		private CharSequence temp;
		@Override
		public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
			temp=s;
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			
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

	private static final int CHECK_PASSWORD_SUCCESS=0;
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CHECK_PASSWORD_SUCCESS:
				isOK=(Boolean) msg.obj;
				//Log.i("isOk2", isOK+""+System.currentTimeMillis());
				break;
			}
		};
	};
	@OnClick(R.id.rl_home)
	public void homeClick(View view){
		finish();
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
	@OnClick(R.id.btn_continue)
	public void continueClick(View view){
		String oldPassword = et_old_password.getText().toString();
		//String userPassword=userInfo.getPassword();
		if (oldPassword.equals("")) {
			ToastUtils.showMessage(this, "请输入旧密码！");
			return;
		}else if (oldPassword.length()<6) {
			ToastUtils.showMessage(this, "密码必须为6位以上的数字加字母组合！");
			return;
		}
		checkPassword(oldPassword);
		new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(200);
					runOnUiThread(new Runnable() {
						

						@Override
						public void run() {
							if(isOK){
								//Log.i("isOk3", isOK+""+System.currentTimeMillis());
								Intent intent=new Intent(instance,ChangePhoneActivity.class);
								startActivityForResult(intent, CHANGE_PHONE_SUCCESS);
							}else {
								ToastUtils.showMessage(instance, "您所输入的密码与旧密码不符！");
								et_old_password.setText("");
							}
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
		
	}
	
	public void checkPassword(String oldPassword){
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "checkPassword");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", userInfo.getUserSeqId());
		busiParams.put("password", DesUtil.enString(oldPassword));
		busiParams.put("phoneNum", userInfo.getPhone());
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					JSONObject returnInfo=jsonObject.getJSONObject("ReturnInfo");
					boolean isOk = Boolean.parseBoolean(returnInfo.getString("isOk"));
					//Log.i("isOk1", isOk+""+System.currentTimeMillis());
					Message message=new Message();
					message.what=CHECK_PASSWORD_SUCCESS;
					message.obj=isOk;
					handler.sendMessage(message);
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
				
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CHANGE_PHONE_SUCCESS:
			setResult(CHANGE_PHONE_SUCCESS);
			finish();
			break;
		}
	}
}
