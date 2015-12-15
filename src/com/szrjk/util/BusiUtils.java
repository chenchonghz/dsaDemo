package com.szrjk.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import com.szrjk.config.Constant;

public class BusiUtils
{
//	public static String getLocalIpAddress()
//	{
//		try
//		{
//			for (Enumeration<NetworkInterface> en = NetworkInterface
//					.getNetworkInterfaces(); en.hasMoreElements();)
//			{
//				NetworkInterface intf = en.nextElement();
//				for (Enumeration<InetAddress> enumIpAddr = intf
//						.getInetAddresses(); enumIpAddr.hasMoreElements();)
//				{
//					InetAddress inetAddress = enumIpAddr.nextElement();
//					if (!inetAddress.isLoopbackAddress()) { return inetAddress.getHostAddress().toString(); }
//				}
//			}
//		}
//		catch (SocketException ex)
//		{
//		}
//		return null;
//	}

//	/**
//	 * 创建快捷方式
//	 */
//	public static void createShoutcut(Activity context)
//	{
//		SharePerferenceUtil perferenceUtil = SharePerferenceUtil.getInstance(context,
//				Constant.APP_INFO);
//		boolean spalshCut = perferenceUtil.getBooleanValue(Constant.APP_CUT,
//				false);
//		Resources resources = context.getResources();
//		String appName = resources.getString(R.string.app_name);
//		if (!ShoutCutUtil.hasShortcut(context, appName))
//		{
//			if (!spalshCut)
//			{
//				String packageName = context.getPackageName();
//				int logo_id = context.getResources().getIdentifier(
//						"ic_launcher", "drawable", packageName);
//				ShoutCutUtil.creatShortCut(context, appName, logo_id);
//				perferenceUtil.setBooleanValue(Constant.APP_CUT, true);
//			}
//		}
//	}
//	public static String getDeviceId(Context context)
//	{
//		TelephonyManager tm = (TelephonyManager) context
//				.getSystemService(Context.TELEPHONY_SERVICE);
//		return tm.getDeviceId();
//	}
//
//	public static String getMacAdd(Context context)
//	{
//		WifiManager wifi = (WifiManager) context
//				.getSystemService(Context.WIFI_SERVICE);
//
//		WifiInfo info = wifi.getConnectionInfo();
//
//		return splitMacAdd(info.getMacAddress());
//	}
//
//	private static String splitMacAdd(String mac)
//	{
//		StringBuffer sb = new StringBuffer();
//		if (null != mac && !"".equals(mac))
//		{
//			String[] arr = mac.split(":");
//			for (int i = 0; i < arr.length; i++)
//			{
//				sb.append(arr[i]);
//			}
//		}
//		return sb.toString();
//	}

	/**
	 * 判断是否为 匿名
	 * @param context
	 * @return
	 */
	public static boolean isguest(Context context){
		String phone = Constant.userInfo.getPhone();
		if("19010000001".equals(phone)){
			//如果为匿名用户,则
			return true;
		}
		return false;
	}

	public static String getVersionName(Context context)
	{
		String version = "";
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			version = packInfo.versionName;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return version;
	}
	public static int getVersionCode(Context context)
	{
		int version = 0;
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			version = packInfo.versionCode;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return version;
	}
//
//	public static String getPhoneType(Context context)
//	{
//
//		return android.os.Build.MODEL;
//	}
//
//	public static String getSDKVersion(Context context)
//	{
//
//		return android.os.Build.VERSION.SDK;
//	}
//
//	public static String getSystemVersion(Context context)
//	{
//
//		return android.os.Build.VERSION.RELEASE;
//	}
//
//	public static String getPhoneResolution(Context context)
//	{
//
//		DisplayMetrics displayMetrics = new DisplayMetrics();
//		WindowManager windowMgr = (WindowManager) context
//				.getSystemService(Context.WINDOW_SERVICE);
//		windowMgr.getDefaultDisplay().getMetrics(displayMetrics);
//		int width = displayMetrics.widthPixels;
//		int height = displayMetrics.heightPixels;
//		return width + "*" + height;
//	}
}
