package com.szrjk.util;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Random;

import android.content.Context;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.szrjk.config.Constant;
import com.szrjk.entity.PhotoType;
import com.szrjk.service.DemoService;

public class ImageAsynTaskUpload {
	private SharePerferenceUtil perferenceUtil;
	private Context context;
	/**
	 * 
	 * @param context 上下文
	 * @param data	图片数据流
	 * @param url	自己拼接的地址
	 * @param photoType	图片类型
	 * @param saveCallback	回调接口SaveCallback
	 * @return
	 */
	public  String uploadPhoto(Context context,byte[] data, String url,PhotoType photoType,	SaveCallback saveCallback)
	{
		this.context = context;
		perferenceUtil = SharePerferenceUtil.getInstance(context,
				Constant.USER_INFO);
		String userSeqId = Constant.userInfo!=null?Constant.userInfo.getUserSeqId():"0";
//		String url = createPathName(data, userSeqId, PhotoType.Feed);

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
//	public static String createPathName(byte[] data, String userSeqId,
	public static String createPathName( String userSeqId,
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
//			String fileName = MD5Util.MD5Encode(data);
//			sb.append(MD5Util.MD5Encode16bit(fileName
			sb.append(MD5Util.MD5Encode16bit(System.currentTimeMillis() + userSeqId));
			String str = getRandomCharAndNumr(4);
			sb.append(str + ".jpg");
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}
	/** 
	 * 获取随机字母数字组合 
	 *  
	 * @param length 
	 *            字符串长度 
	 * @return 
	 */  
	public static String getRandomCharAndNumr(Integer length) {  
	    String str = "";  
	    Random random = new Random();  
	    for (int i = 0; i < length; i++) {  
	        boolean b = random.nextBoolean();  
	        if (b) { // 字符串  
	            // int choice = random.nextBoolean() ? 65 : 97; 取得65大写字母还是97小写字母  
	            str += (char) (65 + random.nextInt(26));// 取得大写字母  
	        } else { // 数字  
	            str += String.valueOf(random.nextInt(10));  
	        }  
	    }  
	    return str;  
	}  
}
