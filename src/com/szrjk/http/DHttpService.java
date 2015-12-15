package com.szrjk.http;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szrjk.config.Constant;

/**
 * denggm on 2015/10/20.
 */
public class DHttpService
{

	public static void httpPost(HashMap<String, Object> hashMap,
			AbstractDhomeRequestCallBack abstractDhomeRequestCallBack)
	{
		//公共参数处理,
		Map BusiParams = (Map) hashMap.get("BusiParams");
		BusiParams.put("deviceType","1");//设备类型 1:安卓 2:IOS 3:web

		RequestParams params = new RequestParams();
		String reqJson = JSON.toJSONString(hashMap);
		Log.e("HttpPost", "提交报文---->" + reqJson);
		try
		{
			params.setBodyEntity(new StringEntity(reqJson, "utf-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		HttpUtils httpUtils = new HttpUtils();
		// 设置当前请求缓存
		httpUtils.configCurrentHttpCacheExpiry(10 * 1000);
		httpUtils.configTimeout(30000);


//		httpUtils.send(HttpRequest.HttpMethod.POST, Constant.RQEUEST_URL,
//				params, abstractDhomeRequestCallBack);
		new HttpThread(httpUtils,params,abstractDhomeRequestCallBack).start();
	}



	/**
	 * 貌似没啥用，我们的接口现在都是post的
	 * 
	 * @param hashMap
	 * @param abstractDhomeRequestCallBack
	 */
	public static void httpGet(HashMap<String, Object> hashMap,
			AbstractDhomeRequestCallBack abstractDhomeRequestCallBack)
	{

		RequestParams params = new RequestParams();
		String reqJson = JSON.toJSONString(params);
		Log.e("HttpPost", "提交报文---->" + reqJson);
		try
		{
			params.setBodyEntity(new StringEntity(reqJson, "utf-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		HttpUtils httpUtils = new HttpUtils();
		// 设置当前请求缓存
		httpUtils.configCurrentHttpCacheExpiry(10 * 1000);
		httpUtils.configTimeout(30000);
		httpUtils.send(HttpRequest.HttpMethod.GET, Constant.RQEUEST_URL,
				params, abstractDhomeRequestCallBack);
	}

	static class HttpThread extends Thread {
		private HttpUtils httpUtils;
		private RequestParams params;
		private AbstractDhomeRequestCallBack abstractDhomeRequestCallBack;

		HttpThread(HttpUtils httpUtils,RequestParams params, AbstractDhomeRequestCallBack abstractDhomeRequestCallBack) {
			this.httpUtils = httpUtils;
			this.params = params;
			this.abstractDhomeRequestCallBack = abstractDhomeRequestCallBack;
		}

		@Override
		public void run() {
			Log.e("httpthread",Thread.currentThread().getId()+"");
			httpUtils.send(HttpRequest.HttpMethod.POST, Constant.RQEUEST_URL,
					params, abstractDhomeRequestCallBack);
		}
	}
}
