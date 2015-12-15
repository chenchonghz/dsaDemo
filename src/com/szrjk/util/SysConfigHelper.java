package com.szrjk.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.szrjk.dhome.R;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class SysConfigHelper
{

//	private final static String TAG = SysConfigHelper.class.getName();
//	private static String serverUrl;
//	private static String imHost;
//	private static String registerUrl;
//	private static String loginUrl;
//	private static String indexUrl;
//	private static String forgetUrl;
//	private static HashMap<Integer, String> resMap = new HashMap<Integer, String>();
//	public static boolean isTest = false;
//	public static boolean isUAT = false;
//
//	static
//	{
//		initDhomeProperties();
//	}
//
//	public static boolean isUAT()
//	{
//		return isTest;
//	}
//
//	public static boolean isTest()
//	{
//		return isUAT;
//	}
//
//	public static String channelKey()
//	{
//		if (isTest())
//		{
//			return "CurrentModeIsTest";
//		}
//		else
//		{
//			return "awfjoi(&%*werfkk52364frwjp4";
//		}
//	}
//
//	/**
//	 * 降低代码复杂度，避免每添加一个配置项，都要更新SysConfigHelper类
//	 *
//	 * @param context
//	 * @param resId
//	 * @return
//	 */
//	public static String getFullHref(Context context, int resId)
//	{
//
//		Integer resKey = Integer.valueOf(resId);
//		if (resMap.containsKey(resKey)) { return resMap.get(resKey); }
//
//		String href = context.getResources().getString(resId);
//		href = getFullHref(context, href);
//		resMap.put(resKey, href);
//
//		return href;
//	}
//
//	public static String getFullHref(Context context, String href)
//	{
//		if (href == null)
//		{
//			href = "";
//		}
//		else
//		{
//			href = getServerUrl(context) + href;
//		}
//
//		return href;
//	}
//
////	public static String getServerUrl(Context context)
////	{
////
////		if (serverUrl == null || "".equals(serverUrl))
////		{
////			serverUrl = context.getResources().getString(
////					R.string.web_server_url);
////		}
////
////		return serverUrl;
////	}
//
//	public static String getImHost(Context context)
//	{
//
//		if (imHost == null || "".equals(imHost))
//		{
//			imHost = context.getResources().getString(R.string.im_server_host);
//		}
//
//		return imHost;
//	}
//
//	public static String getRegisterUrl(Context context)
//	{
//
//		if (registerUrl == null || "".equals(registerUrl))
//		{
//			registerUrl = context.getResources().getString(
//					R.string.register_url);
//			registerUrl = getServerUrl(context) + registerUrl;
//		}
//
//		return registerUrl;
//	}
//
//	public static String getForgetUrl(Context context)
//	{
//
//		if (forgetUrl == null || "".equals(forgetUrl))
//		{
//			forgetUrl = context.getResources().getString(R.string.forget_pwd);
//			forgetUrl = getServerUrl(context) + forgetUrl;
//		}
//
//		return forgetUrl;
//	}
//
//	public static String getLoginUrl(Context context)
//	{
//
//		if (loginUrl == null || "".equals(loginUrl))
//		{
//			loginUrl = context.getResources().getString(R.string.login_url);
//			loginUrl = getServerUrl(context) + loginUrl;
//
//		}
//
//		return loginUrl;
//	}
//
//	public static String getIndexUrl(Context context)
//	{
//
//		if (indexUrl == null || "".equals(indexUrl))
//		{
//			indexUrl = context.getResources().getString(R.string.index_url);
//			indexUrl = getServerUrl(context) + indexUrl;
//		}
//
//		return indexUrl;
//	}
//
//	public static void initDhomeProperties()
//	{
//		File extFile = null;
//		InputStream is = null;
//		// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
//		if (Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED))
//		{
//			try
//			{
//				// confi.properties非运行时必须文件，无sdcard时，不生成即可
//				File dir = new File(Environment.getExternalStorageDirectory(),
//						"dhome");
//				if (!dir.exists())
//				{
//					boolean b = dir.mkdir();
//					if (!b) return;
//				}
//
//				extFile = new File(dir, "config.properties.ext");
//				File file = new File(dir, "config.properties");
//
//				if (!extFile.exists() && !file.exists())
//				{
//					boolean b = extFile.createNewFile();
//					if (b)
//					{
//						FileOutputStream fos = new FileOutputStream(extFile);
//						OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
//								fos, "gbk");
//						BufferedWriter bw = new BufferedWriter(
//								outputStreamWriter);
//
//						String str = "#配置服务地址";
//						bw.write(str);
//						bw.newLine();
//						str = "serverUrl=";
//						bw.write(str);
//						bw.newLine();
//						str = "imHost=";
//						bw.write(str);
//						bw.newLine();
//
//						bw.close();
//						outputStreamWriter.close();
//						fos.close();
//					}
//				}
//
//				if (file.exists())
//				{
//					Properties prop = new Properties();
//					is = new FileInputStream(file);
//					prop.load(is);
//
//					serverUrl = prop.getProperty("serverUrl");
//					imHost = prop.getProperty("imHost");
//					String uatFlag = prop.getProperty("isUAT");
//					isTest = true;
//
//					if (serverUrl != null && uatFlag != null)
//					{
//						if (uatFlag.equalsIgnoreCase("true"))
//						{
//							isUAT = true;
//							// UAT模式等同于正式版
//							isTest = false;
//						}
//					}
//				}
//
//			}
//			catch (Exception e)
//			{
//				Log.e(TAG, e.getMessage());
//			}
//			finally
//			{
//				if (is != null)
//				{
//					try
//					{
//						is.close();
//					}
//					catch (IOException e)
//					{
//						Log.e(TAG, e.getMessage());
//					}
//				}
//			}
//		}
//	}

	public static String getDiskCacheDir(Context context)
	{
		String cachePath = "/SDCard/Android/data/com.szrjk.dhome/cache";
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable())
		{
			File file = context.getExternalCacheDir();
			if (null != file)
			{
				cachePath = file.getPath();
			}
		}
		else
		{
			File file = context.getCacheDir();
			if (null != file)
			{
				cachePath = file.getPath();
			}
		}
		return cachePath;
	}
}
