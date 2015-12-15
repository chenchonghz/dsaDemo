package com.szrjk.util;

import android.app.ProgressDialog;
import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadManager {

	/**
	 * 从服务器下载apk
	 * @param path
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public static File getFileFromServer(Context context, String path, ProgressDialog pd) throws Exception {

		URL url = new URL(path);
		HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		//获取到文件的大小
		pd.setMax(conn.getContentLength());
		InputStream is = conn.getInputStream();
		String filePath = SysConfigHelper.getDiskCacheDir(context) + "updata.apk";
		File file = new File(filePath);
		FileOutputStream fos = new FileOutputStream(file);
		BufferedInputStream bis = new BufferedInputStream(is);
		byte[] buffer = new byte[1024];
		int len ;
		int total=0;
		while((len =bis.read(buffer))!=-1){
			fos.write(buffer, 0, len);
			total+= len;
			//获取当前下载量
			pd.setProgress(total);
		}
		fos.close();
		bis.close();
		is.close();
		return file;
	}
}
