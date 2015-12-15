package com.szrjk.http;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szrjk.config.Constant;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.util.ToastUtils;

/**
 * 
 * 接口调用，回调类 DHome
 */
public abstract class AbstractDhomeRequestCallBack extends
		RequestCallBack<String>
{

	@Override
	public void onStart()
	{
		// 动画之类的效果
		start();
	}

	public abstract void start();

	@Override
	public void onLoading(long total, long current, boolean isUploading)
	{
		loading(total, current, isUploading);
	}

	public abstract void loading(long total, long current, boolean isUploading);

	@Override
	public void onFailure(HttpException exception, String str)
	{
		Log.e("HttpPost", "返回报文---->" + str,exception);
		//这里由于有依赖，进行简单的封装,
		JSONObject ErrorInfo = new JSONObject();
		ErrorInfo.put("ReturnCode","0006");
		ErrorInfo.put("ErrorMessage", "网络不通,请确认!");
		ErrorInfo.put("ErrorInfo", "网络不通,请确认!");
		failure(exception, ErrorInfo);
	}

	public abstract void failure(HttpException exception, JSONObject jobj);

	@Override
	public void onSuccess(com.lidroid.xutils.http.ResponseInfo<String> info)
	{
		try {
			JSONObject jsonObject = JSON.parseObject(info.result);
			Log.e("HttpPost", "返回报文---->" + jsonObject.toJSONString());
			// 用户自定义的业务处理
			Log.e("httpthread_result", Thread.currentThread().getId() + "");
//		new HttpResultThread(jsonObject).start();
			ErrorInfo errorObj = JSON.parseObject(
					jsonObject.getString("ErrorInfo"), ErrorInfo.class);
			String returncode = errorObj.getReturnCode();
			if(returncode!=null&&!returncode.equals(Constant.REQUESTCODE)){
				JSONObject ErrorInfo = new JSONObject();
				ErrorInfo.put("ReturnCode",errorObj.getReturnCode());
				ErrorInfo.put("ErrorMessage", errorObj.getErrorMessage());
				ErrorInfo.put("ErrorInfo", errorObj.getErrorMessage());
				failure(new HttpException("接口返回"+errorObj.getReturnCode()), ErrorInfo);
			}else{
				success(jsonObject);
			}
		} catch (Exception e) {
			Log.i("TAG","频繁请求",e);
		}

	}

	/**
	 * 成功后的回调
	 * 
	 * @param jsonObject
	 */
	public abstract void success(JSONObject jsonObject);
//
//	 class HttpResultThread extends Thread {
//		private JSONObject jsonObject;
//
//		 HttpResultThread(JSONObject jsonObject) {
//			this.jsonObject = jsonObject;
//		}
//
//		@Override
//		public void run() {
//			Log.e("httpthread_result", Thread.currentThread().getId() + "");
//			success(jsonObject);
//		}
//	}

}
