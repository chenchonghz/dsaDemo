package com.szrjk.util;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import android.content.Context;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.szrjk.config.Constant;
import com.szrjk.entity.PhotoType;

public class ImageUploadUtil {
	private SharePerferenceUtil perferenceUtil;
	private Context context;

	public  String uploadPhoto(Context context,byte[] data, PhotoType photoType,	SaveCallback saveCallback)
	{
		this.context = context;
		perferenceUtil = SharePerferenceUtil.getInstance(context,
				Constant.USER_INFO);
		String userSeqId = Constant.userInfo!=null?Constant.userInfo.getUserSeqId():"0";
		String url = createPathName(data, userSeqId, PhotoType.Feed);

		new AliyunThread(url,data,userSeqId,photoType,saveCallback).start();

		return url;
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
				OssUpdateImgUtil.uploadPhoto(url,context, data, userSeqId, photoType,
						saveCallback);
			}
			else
			{
				OssUpdateImgUtil.uploadPhoto(url,context, data, "0", photoType,
						saveCallback);
			}
		}
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
}
