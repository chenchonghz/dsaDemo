package com.szrjk.service;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.szrjk.entity.PhotoType;
import com.szrjk.http.InterfaceComm;
import com.szrjk.util.ImageUploadUtil;
import com.szrjk.util.OssUpdateImgUtil;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class DemoService extends IntentService{
	public DemoService() {
		super("demo");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		Log.i("TAG", "startService");
		ImageUploadUtil util = new ImageUploadUtil();
//		ImageUploadEntity iue = (ImageUploadEntity) arg0.getSerializableExtra("image");
//		final BaseActivity context = (BaseActivity) arg0.getSerializableExtra("context");
//		final IImgUrlCallback imgUrlCallback = iue.getImaUrlCallback();
		byte[] imgData = arg0.getByteArrayExtra("data");
		
		util.uploadPhoto(this,imgData, PhotoType.Face, new SaveCallback() {
			@Override
			public void onSuccess(String s) {
				String imgUrl = OssUpdateImgUtil.facePicFilterUrl + s;
				//                Log.e(TAG, imgUrl);
				//上传完url之后处理   返回给ac，registerInfo实体保存头像地址

//				imgUrlCallback.operImgUrl(imgUrl);
//				context.runOnUiThread(new Runnable() {
//					
//					@SuppressWarnings("static-access")
//					@Override
//					public void run() {
//						context.showToast(context, "上传成功", 0);
//						
//					}
//				});
				Log.i("TAG", imgUrl);
				Log.i("Thread", ""+Thread.currentThread().getName());
				//好像是通知阿里云，处理图片
				InterfaceComm.dealPhoto(s, PhotoType.Face);
			}

			@Override
			public void onProgress(String s, int i, int i1) {
			}

			@Override
			public void onFailure(String s, OSSException e) {
				Log.i("updateFile--onFailure", e.toString());
				Log.i("TAG", e.toString());
//				context.runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						ToastUtils.showMessage(context, "图片上传失败,请删除后再上传");
//					}
//				});
			}
		});
		
	}

}
