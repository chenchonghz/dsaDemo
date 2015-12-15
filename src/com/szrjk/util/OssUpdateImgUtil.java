package com.szrjk.util;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import android.content.Context;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.alibaba.sdk.android.oss.util.OSSToolKit;
import com.szrjk.config.Constant;
import com.szrjk.entity.PhotoType;

public class OssUpdateImgUtil
{

	private static final String accessKey = "dQZta010R0nSUjlJ"; // 实际使用时，不建议AK/SK明文保存在代码中
	private static final String secretKey = "kM9Ys2gKrv4zgKm9op4dNd1nTuavI7";
	// 发帖图片地址
	public static String feedPicFilterUrl = "http://dd-feed.digi123.cn/";
	// 头像图片地址
	public static String facePicFilterUrl = "http://dd-face.digi123.cn/";

	private static OSSService ossService = null;
	private static OSSBucket ossBucket;
	private static String ossPath = "oss-cn-shenzhen.aliyuncs.com";

	public static OSSService initOssService(Context context)
	{
		if (null == ossService)
		{
			ossService = OSSServiceProvider.getService();

			ossService.setGlobalDefaultTokenGenerator(new TokenGenerator()
			{
				@Override
				public String generateToken(String httpMethod, String md5,
						String type, String date, String ossHeaders,
						String resource)
				{

					String content = httpMethod + "\n" + md5 + "\n" + type
							+ "\n" + date + "\n" + ossHeaders + resource;
					return OSSToolKit.generateToken(accessKey, secretKey,
							content);
				}
			});
			ossService.setGlobalDefaultACL(AccessControlList.PRIVATE);
			ossService.setGlobalDefaultHostId(ossPath);
			ossService.setApplicationContext(context.getApplicationContext());

			ClientConfiguration conf = new ClientConfiguration();
			conf.setConnectTimeout(15 * 1000); // 设置建连超时时间，默认为30s
			conf.setSocketTimeout(15 * 1000); // 设置socket超时时间，默认为30s
			conf.setMaxConnections(50); // 设置全局最大并发连接数，默认为50个
			ossService.setClientConfiguration(conf);
		}
		return ossService;
	}

	/**
	 * 上传图片
	 * 
	 * @param context
	 * @param data
	 * @return
	 */
	public static void uploadPhoto(String url,final Context context, byte[] data,
			String userSeqId, PhotoType photoType, SaveCallback callback)
	{

		ossService = initOssService(context);

		if (PhotoType.Face == photoType)
		{
			ossBucket = ossService.getOssBucket(Constant.PHOTO_BUCKET_FACE);
		}
		else
		{
			ossBucket = ossService.getOssBucket(Constant.PHOTO_BUCKET_FEED);
		}

//		final String pathName = createPathName(data, userSeqId, photoType);

		OSSData ossData = ossService.getOssData(ossBucket, url);
		ossData.setData(data, "image/jpeg"); // 指定需要上传的数据和它的类型
		ossData.enableUploadCheckMd5sum(); // 开启上传MD5校验
		ossData.uploadInBackground(callback);
//		return pathName;
	}

}
