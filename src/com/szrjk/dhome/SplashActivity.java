package com.szrjk.dhome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.UpdataInfo;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.receiver.DHomePushService;
import com.szrjk.util.*;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_splash)
public class SplashActivity extends BaseActivity
{
	@ViewInject(R.id.lly_splash)
	private LinearLayout lly_splash;
	private Animation animation;
	private SharePerferenceUtil perferenceUtil;
	private SplashActivity instance;
	private String userName,userPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(instance);
		try {
			LoginHelper.saveClientMessage(SplashActivity.this,"0");
		} catch (Exception e) {
			Log.i("saveClientMessage1", e.toString());
		}
		playAnimation();
		ScreenInfo.getScreenWidth(instance);//获得屏幕的宽度，给裁剪图片用；或者其他用都可以了
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

	private void playAnimation()
	{
//		try {
//			perferenceUtil = SharePerferenceUtil.getInstance(this,
//				Constant.APP_INFO);
//			String s = perferenceUtil.getStringValue(Constant.ERRORMSG);
//			if(s==null||s.equals("")){
//
//			}else{
//				errormsg(s);
//				return;
//			}
//		} catch (Exception e) {
//
//		}

		animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(3000);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

				//网络判断
				if (!NetworkUtils.isNetworkConnected(instance)) {
					ToastUtils.showMessage(SplashActivity.this, "网络不通,请确认!");
					jumpLoginActivity();
					return;
				}
				//做版本判断
				getVersion(SplashActivity.this);
			}
		});
		lly_splash.setAnimation(animation);
	}


	/**
	 * 获取版本信息
	 */
	private void getVersion(Context context)
	{
		try
		{
			//ToastUtils.showMessage(SplashActivity.this,"当前版本为:"+BusiUtils.getVersionCode(SplashActivity.this)+",接口为"+Constant.RQEUEST_URL);
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ServiceName", "getVersion");
			Map<String, Object> busiParams = new HashMap<String, Object>();
			paramMap.put("BusiParams", busiParams);
			httpPost(paramMap, new AbstractDhomeRequestCallBack() {
				@Override
				public void start() {

				}

				@Override
				public void loading(long total, long current, boolean isUploading) {

				}

				@Override
				public void failure(HttpException exception, JSONObject jobj) {
					//如果失败了，直接跳到下一个界面
					jumpNextActivity();
				}

				@Override
				public void success(JSONObject jsonObject) {
					ErrorInfo errorObj = JSON.parseObject(jsonObject.getString("ErrorInfo"), ErrorInfo.class);
					JSONObject returnObj = jsonObject.getJSONObject("ReturnInfo");

					//调用接口成功后处理
					if (errorObj.getReturnCode().equals(Constant.REQUESTCODE)) {
						UpdataInfo uinfo = JSON.parseObject(returnObj.getString("ListOut"), UpdataInfo.class);
						Constant.updataInfo = uinfo;

						if (BusiUtils.getVersionCode(SplashActivity.this) < Constant.updataInfo.getVersionCode()) {
							//升级
							ToastUtils.showMessage(SplashActivity.this, "更新为:" + Constant.updataInfo.getVersionCode());
							showUpdataDialog();
						} else {
							//不升级处理
							jumpNextActivity();
						}
					}
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	/*
         * 弹出对话框通知用户更新程序
         */
	protected void showUpdataDialog()
	{
		AlertDialog.Builder builer = new AlertDialog.Builder(this);
		builer.setTitle("版本升级");
		if (BusiUtils.getVersionCode(this) <= Constant.updataInfo.getMinVersionCode())
		{
			builer.setMessage("当前应用版本" + BusiUtils.getVersionName(this)+ "过低,需升级到最新版本,才能继续使用数字医生服务！");
		}
		else{
			builer.setMessage(Constant.updataInfo.getDescription());
		}
		builer.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				//进行下载
				downLoadApk();
			}
		});
		// 如果当前版本小于等于服务器版本，强制升级
		if (BusiUtils.getVersionCode(this) <= Constant.updataInfo.getMinVersionCode())
		{
			// 当点取消按钮时进行登录
			builer.setNegativeButton("退出",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
//							SharePerferenceUtil sharePerferenceUtil = SharePerferenceUtil
//									.getInstance(SplashActivity.this,Constant.USER_INFO);
//							sharePerferenceUtil.setBooleanValue(
//									Constant.USER_RECOMMENT, false);
							finish();
							System.exit(0);
						}
					});
		}
		else{
			// 当点取消按钮时进行登录
			builer.setNegativeButton("取消",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							jumpNextActivity();
						}
					});
		}
		builer.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
								 KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK
						&& event.getRepeatCount() == 0) {
					//按返回，如果 经强制升级，则直接退出了
					if (BusiUtils.getVersionCode(SplashActivity.this) <= Constant.updataInfo.getMinVersionCode()) {
						finish();
						System.exit(0);
					}
				}
				return false;
			}
		});
		AlertDialog dialog = builer.create();
		dialog.setCancelable(false);
		dialog.show();
	}


	/*
	 * 从服务器中下载APK
	 */
	protected void downLoadApk()
	{
		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			ToastUtils.showMessage(SplashActivity.this,"SD卡不可用");
		}
		else
		{
			pd.show();
			new Thread()
			{
				@Override
				public void run()
				{
					try
					{
						File file = DownLoadManager.getFileFromServer(SplashActivity.this,
								Constant.updataInfo.getDownloadPath(), pd);
						installApk(file);
						pd.dismiss(); // 结束掉进度条对话框
					}
					catch (Exception e)
					{
						ToastUtils.showMessage(SplashActivity.this,"下载新版本失败");
					}
					finish();
					System.exit(0);
				}
			}.start();
		}
	}

	// 安装apk
	protected void installApk(File file)
	{
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}





	private void jumpNextActivity()
	{
		Intent intent = new Intent();
		if(perferenceUtil==null){
			perferenceUtil = SharePerferenceUtil.getInstance(this,
					Constant.APP_INFO);
		}
		if (!perferenceUtil.getBooleanValue(Constant.GUIDE_STATE, false))
		{
			intent.setClass(this, GuideActivity.class);
			startActivity(intent);
			finish();
		}
		else
		{
			if (perferenceUtil.getBooleanValue(Constant.LOGIN_STATE, false))
			{
				//用户不是已登陆状态
				autoLogin(Constant.LOGIN_TYPE_PHONE);
			}
			else
			{
				intent.setClass(this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}
	}

	/**
	 * 登录
	 * 
	 * @param type
	 */
	private void autoLogin(int type)
	{

		boolean userRemember = perferenceUtil.getBooleanValue(Constant.USER_REMEMBER);
		if(!userRemember){
			jumpLoginActivity();
			return;
		}
		userName = perferenceUtil.getStringValue(Constant.USER_NAME);
		userPwd = perferenceUtil.getStringValue(Constant.USER_PWD);

		LoginHelper.doLogin(userName, userPwd, type, SplashActivity.this,
				true, new AbstractDhomeRequestCallBack() {
					@Override
					public void start() {
//						dialog.show();
					}

					@Override
					public void loading(long total, long current,
										boolean isUploading) {

					}

					@Override
					public void failure(HttpException exception,
										JSONObject jsonObject) {
						//登陆后的处理，返回的信息不理
//						dialog.dismiss();
						jumpLoginActivity();
					}


					@Override
					public void success(JSONObject jsonObject) {
//						dialog.dismiss();
						jumpMainActivity();
					}
				});


//		HashMap<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("ServiceName", "userLogin");
//		Map<String, Object> busiParams = new HashMap<String, Object>();
//		userName = perferenceUtil.getStringValue(Constant.USER_NAME);
//		busiParams.put("authAccount", userName);
//		busiParams.put("passwordType", "0");
//		userPwd = perferenceUtil.getStringValue(Constant.USER_PWD);
//		busiParams.put("password", DesUtil.enString(userPwd));
//		busiParams.put("loginType", String.valueOf(type));
//		busiParams.put("channel", "D");
//		busiParams.put("channelKey", Constant.CHANNElKEY);
//		paramMap.put("BusiParams", busiParams);
//		dialog = createDialog(this, "自动登录中，请稍候...");
//		httpPost(paramMap, new AbstractDhomeRequestCallBack()
//		{
//			@Override
//			public void start()
//			{
//				dialog.show();
//			}
//
//			@Override
//			public void loading(long total, long current, boolean isUploading)
//			{
//
//			}
//
//			@Override
//			public void failure(HttpException exception, JSONObject jsonObject)
//			{
////				if(dialog!=null)dialog.dismiss();
////				ErrorInfo errorObj = JSON.parseObject(
////						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
//				if (null != jsonObject)
//				{
//					showToast(instance, jsonObject.getString("ErrorMessage"), Toast.LENGTH_SHORT);
//				}
//				perferenceUtil.setBooleanValue(Constant.LOGIN_STATE, false);
//				perferenceUtil.setBooleanValue(Constant.USER_REMEMBER, false);
//				Intent i = new Intent(SplashActivity.this,LoginActivity.class);
//				SplashActivity.this.startActivity(i);
//			}
//
//			@Override
//			public void success(JSONObject jsonObject)
//			{
//				dialog.dismiss();
//
//				ErrorInfo errorObj = JSON.parseObject(
//						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
//
//				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
//				{
//					JSONObject returnObj = jsonObject
//							.getJSONObject("ReturnInfo");
//					Constant.userInfo = JSON.parseObject(
//							returnObj.getString("ListOut"), UserInfo.class);
//					boolean flag = perferenceUtil.getBooleanValue(
//							Constant.USER_REMEMBER, false);
//					if (flag)
//					{
//						saveUserInfo(Constant.userInfo.getUserSeqId());
//					}
//					else
//					{
//						clearUserInfo(Constant.userInfo.getUserSeqId());
//					}
//					jumpMainActivity();
//				}else{
//					perferenceUtil.setBooleanValue(Constant.LOGIN_STATE, false);
//					perferenceUtil.setBooleanValue(Constant.USER_REMEMBER, false);
//					Intent i = new Intent(SplashActivity.this,LoginActivity.class);
//					SplashActivity.this.startActivity(i);
//				}
//			}
//		});
	}
	/**
	 * 清理用户信息
	 */
	private void clearUserInfo(String userSeqId)
	{
		perferenceUtil.setStringValue(Constant.USER_NAME, userName);
		perferenceUtil.setStringValue(Constant.USER_SEQ_ID, userSeqId);
		perferenceUtil.setStringValue(Constant.USER_PWD, userPwd);
		perferenceUtil.setBooleanValue(Constant.USER_REMEMBER, false);
		perferenceUtil.setBooleanValue(Constant.LOGIN_STATE, true);
	}

	/**
	 * 保存用户信息
	 */
	private void saveUserInfo(String userSeqId)
	{
		perferenceUtil.setStringValue(Constant.USER_NAME, userName);
		perferenceUtil.setStringValue(Constant.USER_PWD, userPwd);
		perferenceUtil.setStringValue(Constant.USER_SEQ_ID, userSeqId);
		perferenceUtil.setBooleanValue(Constant.USER_REMEMBER, true);
		perferenceUtil.setBooleanValue(Constant.LOGIN_STATE, true);
	}

	/**
	 * 跳转到主界面
	 */
	private void jumpMainActivity() {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		PushService.pushOnAppStart(SplashActivity.this);
		startActivity(intent);
		finish();
	}

	private void jumpLoginActivity()
	{
		Intent intent = new Intent();
		intent.setClass(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}


	@Override
	protected void onResume()
	{
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
