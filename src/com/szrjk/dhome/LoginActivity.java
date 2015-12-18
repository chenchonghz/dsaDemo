package com.szrjk.dhome;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.entity.IPopupItemCallback;
import com.szrjk.entity.PopupItem;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.SharePerferenceUtil;
import com.szrjk.widget.ListPopup;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements OnClickListener
{
	@ViewInject(R.id.et_name)
	private EditText et_name;
	@ViewInject(R.id.et_pwd)
	private EditText et_pwd;
	@ViewInject(R.id.cb_save)
	private CheckBox cb_save;
	@ViewInject(R.id.tv_forget)
	private TextView tv_forget;

	@ViewInject(R.id.tv_guest)
	private TextView tv_guest;

	@ViewInject(R.id.btnReg)
	private Button btn_rg;
	@ViewInject(R.id.btnLogin)
	private Button btn_lg;
	@ViewInject(R.id.iv_wx)
	private ImageView iv_wx;
	@ViewInject(R.id.iv_wb)
	private ImageView iv_wb;
	@ViewInject(R.id.iv_qq)
	private ImageView iv_qq;


	//版本切换按钮
	@ViewInject(R.id.tv_url)
	private TextView tv_url;
	@ViewInject(R.id.lly_login)
	private LinearLayout lly_login;
	//
	private String name, pwd;
	private LoginActivity instance;
	private SharePerferenceUtil perferenceUtil;
	private Dialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		initLayout();
	}

	private void initLayout()
	{
		perferenceUtil = SharePerferenceUtil.getInstance(instance,
				Constant.USER_INFO);
		dialog = createDialog(this, "登录中，请稍候...");
		btn_rg.setOnClickListener(this);
		btn_lg.setOnClickListener(this);
		tv_forget.setOnClickListener(this);
		tv_guest.setOnClickListener(this);
		boolean falg = perferenceUtil.getBooleanValue(Constant.USER_REMEMBER,
				false);
		if (falg)
		{
			et_name.setText(perferenceUtil.getStringValue(Constant.USER_NAME,
					""));
			et_pwd.setText(perferenceUtil.getStringValue(Constant.USER_PWD, ""));
		}
		else
		{
			et_name.setText("");
			et_pwd.setText("");
		}
		//版本信息
		tv_url.setText(Constant.RQEUEST_URL);
		tv_url.setOnClickListener(this);
	}

	private void shakeAnimation(View view)
	{
		Animation animation = AnimationUtils
				.loadAnimation(this, R.anim.shake_x);
		view.startAnimation(animation);
	}

	/**
	 * 检验表单输入
	 *
	 * @return
	 */
	private boolean checkInput()
	{
		boolean flag = true;
		name = et_name.getText().toString();
		pwd = et_pwd.getText().toString();
		if (null == name || "".equals(name) || "".equals(name.trim()))
		{
			shakeAnimation(et_name);
			flag = false;
		}
		if (null == pwd || "".equals(pwd))
		{
			shakeAnimation(et_pwd);
			flag = false;
		}
		return flag;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btnReg:
				// 跳转到注册界面
				jumpRegisterActivity();
				break;
			case R.id.tv_forget:
				// 跳转到找回密码界面
				jumpForgetActivity();
				break;
			case R.id.btnLogin:
				if (checkInput())
				{
					login(Constant.LOGIN_TYPE_PHONE);
				}
				break;
			case R.id.tv_url:
				showchange(lly_login);
//				DialogUtil.showGuestDialog(instance, null);
//				List<DialogItem> item = new ArrayList<DialogItem>();
//				DialogItem pi1 = new DialogItem();
//				pi1.setItemText("开发环境");//设置名称
//				pi1.setColor(this.getResources().getColor(R.color.search_bg));
//				pi1.setDialogItemCallback(new DialogItemCallback() {
//					
//					@Override
//					public void DialogitemClick() {
//						Constant.RQEUEST_URL = "http://192.168.1.71:8888/qry";
//						tv_url.setText(Constant.RQEUEST_URL);
//						ToastUtils.showMessage(instance, "开发环境");
//					}
//				});
//				DialogItem pi2 = new DialogItem();
//				pi2.setItemText("测试环境");//设置名称
//				pi2.setColor(this.getResources().getColor(R.color.search_bg));
//				pi2.setDialogItemCallback(new DialogItemCallback() {
//					
//					@Override
//					public void DialogitemClick() {
//						Constant.RQEUEST_URL = "http://192.168.1.82:8888/qry";
//						tv_url.setText(Constant.RQEUEST_URL);
//						ToastUtils.showMessage(instance, "测试环境");
//					}
//				});
//				DialogItem pi3= new DialogItem();
//				pi3.setItemText("UAT环境");//设置名称
//				pi3.setColor(this.getResources().getColor(R.color.search_bg));
//				pi3.setDialogItemCallback(new DialogItemCallback() {
//					public void DialogitemClick() {
//						Constant.RQEUEST_URL = "http://112.74.109.173:8888/qry";
//						tv_url.setText(Constant.RQEUEST_URL);
//						ToastUtils.showMessage(instance, "UAT环境");
//					}
//				});
//				item.add(pi1);item.add(pi2);item.add(pi3);
//				CustomListDialog listdialog = new CustomListDialog(instance, item);
//				listdialog.setCanceledOnTouchOutside(true);
//				listdialog.show();
				break;
			case R.id.tv_guest:
				guestLogin();
				break;
		}
	}

	/**
	 * 游客登陆
	 */
	private void guestLogin(){
//		sendWindow = new PostSendPopup(instance, sendPostClick);
		LoginHelper.doLogin("19010000001", "digi123", Constant.LOGIN_TYPE_PHONE, LoginActivity.this,
				false, new AbstractDhomeRequestCallBack() {
					@Override
					public void start() {
						dialog.show();
					}

					@Override
					public void loading(long total, long current,
										boolean isUploading) {

					}

					@Override
					public void failure(HttpException exception,
										JSONObject jsonObject) {
						dialog.dismiss();
					}

					@Override
					public void success(JSONObject jsonObject) {
						dialog.dismiss();
						jumpMainActivity();
					}
				});


	}


	private ListPopup window;
	private void showchange(View v){
//		sendWindow = new PostSendPopup(instance, sendPostClick);

		List<PopupItem> pilist = new ArrayList<PopupItem>();

		PopupItem pi1 = new PopupItem();
		pi1.setItemname("开发环境");//设置名称
		pi1.setColor(this.getResources().getColor(R.color.search_bg));
		pi1.setiPopupItemCallback(new IPopupItemCallback() {  //设置click动作
			@Override
			public void itemClickFunc(PopupWindow sendWindow) {
				sendWindow.dismiss();
				Constant.RQEUEST_URL = "http://192.168.1.71:8888/qry";
				tv_url.setText(Constant.RQEUEST_URL);
			}
		});
		pilist.add(pi1);
		PopupItem pi2 = new PopupItem();
		pi2.setItemname("测试环境");//设置名称
		pi2.setColor(this.getResources().getColor(R.color.search_bg));
		pi2.setiPopupItemCallback(new IPopupItemCallback() {  //设置click动作
			@Override
			public void itemClickFunc(PopupWindow sendWindow) {
				sendWindow.dismiss();
				Constant.RQEUEST_URL = "http://192.168.1.82:8888/qry";
				tv_url.setText(Constant.RQEUEST_URL);
			}
		});
		pilist.add(pi2);
		PopupItem pi3 = new PopupItem();
		pi3.setItemname("UAT环境");//设置名称
		pi3.setColor(this.getResources().getColor(R.color.search_bg));
		pi3.setiPopupItemCallback(new IPopupItemCallback() {  //设置click动作
			@Override
			public void itemClickFunc(PopupWindow sendWindow) {
				sendWindow.dismiss();
				Constant.RQEUEST_URL = "http://112.74.109.173:8888/qry";
				tv_url.setText(Constant.RQEUEST_URL);
			}
		});
		pilist.add(pi3);
		PopupItem pi4 = new PopupItem();
		pi4.setItemname("错误报告");//设置名称
		pi4.setColor(this.getResources().getColor(R.color.search_bg));
		pi4.setiPopupItemCallback(new IPopupItemCallback() {  //设置click动作
			@Override
			public void itemClickFunc(PopupWindow sendWindow) {
				sendWindow.dismiss();
				try {
					perferenceUtil = SharePerferenceUtil.getInstance(instance,
						Constant.APP_INFO);
					String s = perferenceUtil.getStringValue(Constant.ERRORMSG);
					if(s==null||s.equals("")){
		
					}else{
						errormsg(s);
						return;
					}
				} catch (Exception e) {
		
				}
			}
		});
		pilist.add(pi4);
		window = new ListPopup(this,pilist,v);
	}
	public void errormsg(String msg){
		if(msg==null||msg.equals("")){
			return;
		}
		Intent intent = new Intent(this, ActErrorReport.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("error", msg);
		intent.putExtra("by", "uehandler");
		startActivity(intent);
	}

	/**
	 * 跳转到注册界面
	 */
	private void jumpRegisterActivity()
	{
		Intent intent = new Intent();
		intent.setClass(this, Register1Activity.class);
		startActivity(intent);
	}

	/**
	 * 跳转到忘记密码界面
	 */
	private void jumpForgetActivity()
	{
		Intent intent = new Intent();
		intent.setClass(this, ForgetActivity.class);
		startActivity(intent);
	}

	/**
	 * 登录
	 *
	 * @param type
	 */
	private void login(int type)
	{

		LoginHelper.doLogin(name, pwd, type, LoginActivity.this,
				cb_save.isChecked(), new AbstractDhomeRequestCallBack() {
					@Override
					public void start() {
						dialog.show();
					}

					@Override
					public void loading(long total, long current,
										boolean isUploading) {

					}

					@Override
					public void failure(HttpException exception,
										JSONObject jsonObject) {
						dialog.dismiss();
					}

					@Override
					public void success(JSONObject jsonObject) {
						dialog.dismiss();
						jumpMainActivity();
					}
				});
	}

	/**
	 * 跳转到登录界面
	 */
	private void jumpMainActivity()
	{
		String uid = Constant.userInfo.getUserSeqId();
//		ToastUtils.showMessage(LoginActivity.this, "" + uid);
		PushService.pushOnAppStart(LoginActivity.this);
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
