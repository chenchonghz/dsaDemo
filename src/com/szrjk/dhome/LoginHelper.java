package com.szrjk.dhome;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.config.Constant;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.BusiUtils;
import com.szrjk.util.DesUtil;
import com.szrjk.util.SharePerferenceUtil;
import com.szrjk.util.ToastUtils;
import com.umeng.message.PushAgent;

import org.apache.http.conn.util.InetAddressUtils;

/**
 * denggm on 2015/10/23. DHome
 */
public class LoginHelper {

	private final static String TAG = "LoginHelper";

	/**
	 * 登陆公共接口，用户登陆成功后 -> 持久化数据
	 * 
	 * @param userAccount
	 *            用户账号，即手机号码/邮箱之类的
	 * @param pwd
	 *            明文
	 * @param loginType
	 *            Constant.LOGIN_TYPE_PHONE
	 * @param context
	 * @param userRember
	 * @param pcallback
	 *            自定久回调对象， 会在公共回调执行后执行
	 */
	public static void doLogin(final String userAccount, final String pwd,
			int loginType, final Context context, final boolean userRember,
			final AbstractDhomeRequestCallBack pcallback) {

		if (userAccount == null || pwd == null) {
			pcallback.failure(null, null);
		}

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "userLogin");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("authAccount", userAccount);
		busiParams.put("passwordType", "0");
		busiParams.put("password", DesUtil.enString(pwd));
		busiParams.put("loginType", String.valueOf(loginType));
		busiParams.put("channel", "D");
		busiParams.put("channelKey", Constant.CHANNElKEY);
		busiParams.put("deviceType", "1");
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void start() {
				pcallback.start();
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {
				pcallback.loading(total, current, isUploading);
			}

			/** 登陆失败的主要处理 **/
			@Override
			public void failure(HttpException exception, JSONObject jsonObject) {
				SharePerferenceUtil perferenceUtil = SharePerferenceUtil
						.getInstance(context, Constant.USER_INFO);
				perferenceUtil.setBooleanValue(Constant.LOGIN_STATE, false);
				perferenceUtil.setBooleanValue(Constant.USER_REMEMBER, false);
				if (null != jsonObject) {
					ToastUtils.showMessage(context,
							"" + jsonObject.getString("ErrorInfo"));
				}
				pcallback.failure(exception, jsonObject);
			}

			@Override
			public void success(JSONObject jsonObject) {
				// dialog.dismiss();
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				SharePerferenceUtil perferenceUtil = SharePerferenceUtil
						.getInstance(context, Constant.USER_INFO);

				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					Log.e(TAG, returnObj.getString("ListOut"));

					Constant.userInfo = JSON.parseObject(
							returnObj.getString("ListOut"), UserInfo.class);

					// 持久化存储
					perferenceUtil.setStringValue(Constant.USER_NAME,
							userAccount);
					perferenceUtil.setStringValue(Constant.USER_PWD, pwd);
					perferenceUtil.setStringValue(Constant.USER_SEQ_ID,
							Constant.userInfo.getUserSeqId());
					perferenceUtil.setBooleanValue(Constant.USER_REMEMBER,
							userRember);
					perferenceUtil.setBooleanValue(Constant.LOGIN_STATE, true);

					// 用户自定义方法
					pcallback.success(jsonObject);

					try {
						String usid = Constant.userInfo.getUserSeqId();
						saveClientMessage(context,usid);
					} catch (Exception e) {
						Log.i("saveClientMessage1", e.toString());
					}
				} else {
					// //这里由于有依赖，进行简单的封装,
					JSONObject ErrorInfo = new JSONObject();
					ErrorInfo.put("ReturnCode", "0006");
					ErrorInfo.put("ErrorMessage", errorObj.getErrorMessage());
					ErrorInfo.put("ErrorInfo", errorObj.getErrorMessage());
					failure(null, ErrorInfo);
				}
			}
		});
	}

	public static void saveClientMessage(Context context,String usid) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		android.view.Display display = wm.getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();

		LocationManager GpsManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		Location location = GpsManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "gatherAppInfo");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", usid);
		busiParams.put("phoneType", android.os.Build.MODEL);// 手机型号
		busiParams.put("phoneResolution", width + "*" + height);
		busiParams.put("channelType", "1");
		busiParams.put("version", BusiUtils.getVersionName(context));
		busiParams.put("systemVersion", android.os.Build.VERSION.RELEASE);// 系统版本
		busiParams.put("mac", getLocalMacAddress());// MAC地址info.getMacAddress()设备开通Wifi连接，获取到网卡的MAC地址(但是不开通wifi，这种方法获取不到Mac地址，这种方法也是网络上使用的最多的方法)
		busiParams.put("ip", GetHostIp());
		String androidId = "";
		try {
			//序列号
			androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		} catch (Exception e) {
			Log.e("error","",e);
		}
		busiParams.put("deviceId", androidId);
		String gps="";
		if (location != null) {
			gps = "精度:" + location.getAccuracy() + ",高度 :"
					+ location.getAltitude() + ",导向 :" + location.getBearing()
					+ ",速度 :" + location.getSpeed() + ", 纬度 :"
					+ location.getLatitude() + ",经度 :"
					+ location.getLongitude();
			busiParams.put("gps", gps);
		}else {
			busiParams.put("gps", gps);
		}
		//		Log.i("saveClientMessage2",
		//				"photoType " + android.os.Build.MODEL + ",phoneResolution "
		//						+ width + "*" + height + ",version "
		//						+ BusiUtils.getVersionName(context) + ",systemVersion "
		//						+ android.os.Build.VERSION.RELEASE + ",mac "
		//						+ getLocalMacAddressFromIp(context) + ",gps " + gps + ",ip "
		//						+ getLocalIpAddress() + ",deviceId " + tm.getDeviceId());
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {

			@Override
			public void success(JSONObject jsonObject) {

			}

			@Override
			public void start() {

			}

			@Override
			public void loading(long total, long current, boolean isUploading) {

			}

			@Override
			public void failure(HttpException exception, JSONObject jobj) {

			}
		});
	}

	public static String getLocalMacAddress() {
		String Mac=null;
		try{

			String path="sys/class/net/wlan0/address";
			if((new File(path)).exists())
			{
				FileInputStream fis = new FileInputStream(path);
				byte[] buffer = new byte[8192];
				int byteCount = fis.read(buffer);
				if(byteCount>0)
				{
					Mac = new String(buffer, 0, byteCount, "utf-8");
				}
			}
			if(Mac==null||Mac.length()==0)
			{
				path="sys/class/net/eth0/address";
				FileInputStream fis_name = new FileInputStream(path);
				byte[] buffer_name = new byte[8192];
				int byteCount_name = fis_name.read(buffer_name);
				if(byteCount_name>0)
				{
					Mac = new String(buffer_name, 0, byteCount_name, "utf-8");
				}
			}
			if(Mac.length()==0||Mac==null){
				return "";
			}
			return  Mac.trim();
		}catch(Exception io){
			Log.e("daming.zou**exception*", ""+io.toString(),io);
		}
		return "";
		//		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		//		WifiInfo info = wifi.getConnectionInfo();
		//		if (info.getMacAddress() != null) {
		//			return info.getMacAddress().toString();
		//		}
	}

	public static String GetHostIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(inetAddress
									.getHostAddress())) {
						if (!inetAddress.getHostAddress().toString()
								.equals("null")
								&& inetAddress.getHostAddress() != null) {
							return inetAddress.getHostAddress().toString().trim();
						}
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return "";
	}

	//	// 根据IP获取本地Mac
	//	public static String getLocalMacAddressFromIp(Context context) {
	//		String mac_s = "";
	//		try {
	//			byte[] mac;
	//			NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress
	//					.getByName(getLocalIpAddress()));
	//			mac = ne.getHardwareAddress();
	//			mac_s = byte2hex(mac);
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//
	//		return mac_s;
	//	}
	//
	//	public static String byte2hex(byte[] b) {
	//		StringBuffer hs = new StringBuffer(b.length);
	//		String stmp = "";
	//		int len = b.length;
	//		for (int n = 0; n < len; n++) {
	//			stmp = Integer.toHexString(b[n] & 0xFF);
	//			if (stmp.length() == 1)
	//				hs = hs.append("0").append(stmp);
	//			else {
	//				hs = hs.append(stmp);
	//			}
	//		}
	//		return String.valueOf(hs);
	//	}
	//
	//	public static String getLocalIpAddress() {
	//		try {
	//			for (Enumeration<NetworkInterface> en = NetworkInterface
	//					.getNetworkInterfaces(); en.hasMoreElements();) {
	//				NetworkInterface intf = en.nextElement();
	//				for (Enumeration<InetAddress> enumIpAddr = intf
	//						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	//					InetAddress inetAddress = enumIpAddr.nextElement();
	//					if (!inetAddress.isLoopbackAddress()) {
	//						return inetAddress.getHostAddress().toString();
	//					}
	//				}
	//			}
	//		} catch (SocketException ex) {
	//			Log.e("WifiPreference IpAddress", ex.toString());
	//		}
	//		return null;
	//	}
	//登出方法
	public static void Logout(Context instance){
		SharePerferenceUtil perferenceUtil = SharePerferenceUtil.getInstance(instance,
				Constant.USER_INFO);
		perferenceUtil.setBooleanValue(Constant.LOGIN_STATE, false);
		Constant.userInfo = null;//登出
		//取消推送信息
		final PushAgent pushAgent = PushAgent.getInstance(instance);
		pushAgent.disable();
	}
}
