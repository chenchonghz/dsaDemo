package com.szrjk.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.config.Constant;
import com.szrjk.entity.PhotoBucket;
import com.szrjk.entity.PhotoType;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.ImageAsynTaskUpload;

public class DemoService extends IntentService{
	private DemoService instance;
	public DemoService() {
		super("demo");
	}

	@Override
	protected void onHandleIntent(Intent data) {
		instance = this;
		Log.i("", "进入>--服务上传图片");
		byte[] bo = data.getByteArrayExtra("postbp");
		String path = data.getStringExtra("path");

		ImageAsynTaskUpload util = new ImageAsynTaskUpload();
		util.uploadPhoto(instance, bo, path, PhotoType.Feed,new SaveCallback() {
			@Override
			public void onProgress(String arg0, int pro, int total) {
			}
			@Override
			public void onFailure(String imageUrl, OSSException ossException) {
				//                handler.removeCallbacks(runnable);
				//                sb.append(OssUpdateImgUtil.feedPicFilterUrl + imageUrl + "|");
				// 处理图片
				//上传失败？如果突然断网，就会停止所以异步的上传，
				Log.i("上传发帖图片失败", ossException.toString());
				new DealPhoto(imageUrl).start();
			}

			@Override
			public void onSuccess(String imageUrl) {
				// 处理图片
				Log.i("", "上传图片成功>--");
				new DealPhoto(imageUrl).start();
			}
		});
		//		return  url;
		//		pb_loading.setVisibility(View.GONE);
	}
	class DealPhoto extends Thread {
		private String pathName;

		DealPhoto(String pathName) {
			this.pathName = pathName;
		}

		@Override
		public void run() {
			dealPhoto(pathName);
		}

		// 处理图片
		private void dealPhoto(String pathName) {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ServiceName", "dealPic");
			Map<String, Object> busiParams = new HashMap<String, Object>();
			List<PhotoBucket> buckets = new ArrayList<PhotoBucket>();
			PhotoBucket bucket = new PhotoBucket();
			bucket.setBucket(Constant.PHOTO_BUCKET_FEED);
			bucket.setKey(pathName);
			bucket.setSize(Constant.FEED_DEAL_SIZE);
			buckets.add(bucket);
			busiParams.put("pics", buckets);
			paramMap.put("BusiParams", busiParams);
			DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
				@Override
				public void start() {			}
				@Override
				public void loading(long total, long current, boolean isUploading) {			}
				@Override
				public void failure(HttpException exception, JSONObject jobj) {	
					Log.i("dealPhoto", jobj.toString());	}
				@Override
				public void success(JSONObject jsonObject) {
					Log.i("dealPhoto", "阿里云裁剪图片成功");
				}
			});
		}
	}
}
