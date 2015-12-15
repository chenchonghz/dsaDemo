package com.szrjk.dhome;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.config.Constant;
import com.szrjk.entity.PhotoType;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.*;
import com.szrjk.widget.AddPhotoPopup;
import com.szrjk.widget.ListPopup;
import com.szrjk.widget.PostSendPopup;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity
{

	protected String TAG = this.getClass().getCanonicalName();
	private BaseActivity instance;
	SharePerferenceUtil perferenceUtil;

	protected  Dialog dialog ;

	/**
	 * 在拍照时使用，当前图的设置，其它地方请不要使用!!
	 */
	protected  UploadPhotoUtils uploadPhotoUtils;

	//拍照时使用
	protected AddPhotoPopup menuWindow;

	// 多选的弹出框
	public AddPhotoPopup multiplemenuWindow;

	// window 的弹出框
	protected ListPopup sendWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance = this;
		initData();
		dialog= createDialog(this, "请稍候...");
	}

	private void initData()
	{
		perferenceUtil = SharePerferenceUtil.getInstance(instance,
				Constant.USER_INFO);
	}

	/**
	 * 得到自定义的Dialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public Dialog createDialog(Context context, String msg)
	{
		Dialog dialog = new Dialog(context, R.style.loading_dialog);
		dialog.setContentView(R.layout.loading_layout);
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		TextView textView = (TextView) dialog.findViewById(R.id.tv_msg);
		textView.setText(msg);
		return dialog;
	}

	/**
	 * 得到自定义的Dialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static void showToast(Context context, String msg, int duration)
	{
		Toast toast = Toast.makeText(context, msg, duration);
		toast.show();
	}

	public void httpPost(HashMap<String, Object> params,
			AbstractDhomeRequestCallBack abstractDhomeRequestCallBack)
	{
		if (NetworkUtils.isNetworkConnected(instance))
		{
			DHttpService.httpPost(params, abstractDhomeRequestCallBack);
		}
		else
		{
			ToastUtils.showMessage(instance,"网络不通,请确认!!");
			//这里由于有依赖，进行简单的封装,
			JSONObject ErrorInfo1 = new JSONObject();
			ErrorInfo1.put("ReturnCode","0006");
			ErrorInfo1.put("ErrorMessage", "网络不通,请确认!");
			ErrorInfo1.put("ErrorInfo", "网络不通,请确认!");
			abstractDhomeRequestCallBack.failure(new HttpException(), ErrorInfo1);
//			jumpNetworkActivity();
		}
	}



	public void httpGet(HashMap<String, Object> params,
			AbstractDhomeRequestCallBack abstractDhomeRequestCallBack)
	{
		if (NetworkUtils.isNetworkConnected(instance))
		{
			DHttpService.httpGet(params, abstractDhomeRequestCallBack);
		}
		else
		{
			jumpNetworkActivity();
		}
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

	// 设置网络连接
	private void jumpNetworkActivity() {
		Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
		startActivity(intent);
	}
	public static String createPathName(byte[] data, String userSeqId,
										PhotoType photoType)
	{
		StringBuffer sb = new StringBuffer();
		try
		{

			if (photoType == PhotoType.Face)
			{
				// 头像目录
				sb.append("face");
				sb.append(File.separator);
			}
			else
			{
				Calendar calendar = Calendar.getInstance();
				// 按月份分目录
				int month = calendar.get(Calendar.MONTH) + 1;
				String monthStr = month + "";
				if (month < 10)
				{
					monthStr = "0" + month;
				}
				sb.append(calendar.get(Calendar.YEAR));
				sb.append(monthStr);
				sb.append(File.separator);
			}
			String fileName = MD5Util.MD5Encode(data);
			sb.append(MD5Util.MD5Encode16bit(fileName
					+ System.currentTimeMillis() + userSeqId));
			sb.append(".jpg");
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}

	public String uploadPhoto(byte[] data, PhotoType photoType,
			SaveCallback saveCallback)
	{

		String userSeqId = Constant.userInfo!=null?Constant.userInfo.getUserSeqId():"0";
		String url = createPathName(data, userSeqId, PhotoType.Feed);

		new AliyunThread(url,data,userSeqId,photoType,saveCallback).start();

		return url;
	}
	
	public void addRegisterActivitys(Activity activity){
		DHomeApplication.activityList.add(activity);
	}

	class AliyunThread extends Thread {
		private String url;
		private byte[] data;
		private String userSeqId;
		private PhotoType photoType;
		private SaveCallback saveCallback;


		AliyunThread(String url,byte[] data,String userSeqId,PhotoType photoType, SaveCallback callback) {
			this.url = url;
			this.data = data;
			this.userSeqId = userSeqId;
			this.photoType = photoType;
			this.saveCallback = callback;
		}

		@Override
		public void run() {

			if (perferenceUtil.getBooleanValue(Constant.LOGIN_STATE, false))
			{
//			String userSeqId = Constant.userInfo!=null?Constant.userInfo.getUserSeqId():"0";
//			String userSeqId = Constant.userInfo.getUserSeqId();
				OssUpdateImgUtil.uploadPhoto(url,instance, data, userSeqId, photoType,
						saveCallback);
			}
			else
			{
				OssUpdateImgUtil.uploadPhoto(url,instance, data, "0", photoType,
						saveCallback);
			}
		}
	}
}
