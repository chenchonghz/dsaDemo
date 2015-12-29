package com.szrjk.util;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.szrjk.dhome.BaseActivity;
import com.szrjk.self.more.album.AlbumGalleryActivity;

public class PictureLoader {
	
	public static final int CAMERA = 1001;
	public static final int Album = 1002;
	/**
	 * 
	 * @param context  必须是baActivity 类型
	 * @return			返回拍照之后的图片
	 */
	public static File getCamera(BaseActivity context){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String path = Environment.getExternalStorageDirectory().toString() + "/" +"tempimage";
		File path1 = new File(path);
		if(!path1.exists()){
			path1.mkdirs();
		}
		File file = new File(path1,System.currentTimeMillis()+".jpg");
		try {
			file.createNewFile();//这里确保file生成。酷派会出现file不存在的情况
		} catch (IOException e) {
			e.printStackTrace();
		}
		Uri mOutPutFileUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
		context.startActivityForResult(intent, CAMERA);
		return file;
	}
	/**
	 * 
	 * @param context 必须是baActivity 类型
	 * @param num 	  你要获得多少张图片
	 */
	public static void getAlbum(BaseActivity context,int num){
		Intent intent2 = new Intent();
		intent2.setClass(context, AlbumGalleryActivity.class);
		intent2.putExtra("num", num);
		context.startActivityForResult(intent2, Album);
	}
	/**
	 * 
	 * @param intent 通过相册返回的intent
	 * @return 			根据key，获得一个string【】
	 */
	public static String[] getIntentData(Intent intent){
		
		if (intent == null) {
			return null;
		}
		
		return intent.getStringArrayExtra("arr");
		
	}
	
}
