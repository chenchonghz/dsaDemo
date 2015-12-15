package com.szrjk.service;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.entity.IImgUrlCallback;
import com.szrjk.entity.PhotoType;
import com.szrjk.http.InterfaceComm;
import com.szrjk.util.ImageUploadUtil;
import com.szrjk.util.OssUpdateImgUtil;
import com.szrjk.util.ToastUtils;

import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class PictureUploadService extends IntentService {
	private IImgUrlCallback imgUrlCallback;
	private volatile Looper mServiceLooper;
	private volatile ServiceHandler mServiceHandler;
	private BaseActivity Context;
	private String mName;
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			onHandleIntent((Intent)msg.obj);
			stopSelf(msg.arg1);
		}
	}
	public PictureUploadService(String name) {
		super(name);
		mName = name;
	}
	@Override
	public void onStart(Intent intent, int startId) {
		 Message msg = mServiceHandler.obtainMessage();
	        msg.arg1 = startId;
	        msg.obj = intent;
	        mServiceHandler.sendMessage(msg);
		super.onStart(intent, startId);
	}
	@Override
	public void onCreate() {
		HandlerThread thread = new HandlerThread("IntentService[" + mName + "]");
		thread.start();

		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		super.onCreate();
	}
	@Override
	public void onDestroy() {
		mServiceLooper.quit();
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		System.out.println("onStart");  
        try {  
            Thread.sleep(10000);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
        System.out.println("睡眠结束");  
//		IUE = (ImageUploadEntity) intent.getSerializableExtra("Image");
//		ImageUploadUtil util = new ImageUploadUtil();
//		util.uploadPhoto(Context, IUE.getImageData(), PhotoType.valueOf(IUE.getType()) ,new SaveCallback() {
//
//			@Override
//			public void onProgress(String arg0, int arg1, int arg2) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onFailure(String s, OSSException e) {
//				Log.i("updateFile--onFailure", e.toString());
//				//				PictureUploadService.this.runOnUiThread(new Runnable() {
//				//					
//				//					@Override
//				//					public void run() {
//				//						ToastUtils.showMessage(Context, "图片上传失败,请删除后再上传");
//				//					}
//				//				});
//
//			}
//
//			@Override
//			public void onSuccess(String s) {
//
//				String imgUrl = OssUpdateImgUtil.facePicFilterUrl + s;
//				//                Log.e(TAG, imgUrl);
//				//上传完url之后处理   返回给ac，registerInfo实体保存头像地址
//
//				imgUrlCallback.operImgUrl(imgUrl);
//				//				 Context.runOnUiThread(new Runnable() {
//				//					
//				//					@SuppressWarnings("static-access")
//				//					@Override
//				//					public void run() {
//				//						Context.showToast(Context, "上传成功", 0);
//				//						
//				//					}
//				//				});
//
//				Log.i("Thread", ""+Thread.currentThread().getName());
//				//好像是通知阿里云，处理图片
//				InterfaceComm.dealPhoto(s, PhotoType.Face);
//
//			}
//		});
//
	}
}

