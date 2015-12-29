package com.szrjk.dhome;

import java.io.File;
import java.io.IOException;

import com.lidroid.xutils.BitmapUtils;
import com.szrjk.self.more.album.AlbumGalleryActivity;
import com.szrjk.util.BitmapCompressImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class BackgroundSettingActivity extends Activity implements OnClickListener{

	private View bt1;
	private View bt2;
	private File file;
	private Uri mOutPutFileUri;
	private RelativeLayout rl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_background_setting);

		rl = (RelativeLayout)this.findViewById(R.id.rl_glo);

		bt1 = this.findViewById(R.id.button1);//拍照
		bt2 = this.findViewById(R.id.button2);//图库
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			String path = Environment.getExternalStorageDirectory().toString() + "/" +"tempimage";
			File path1 = new File(path);
			if(!path1.exists()){
				path1.mkdirs();
			}
			file = new File(path1,System.currentTimeMillis()+".jpg");
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//这里确保file生成。酷派会出现file不存在的情况
			mOutPutFileUri = Uri.fromFile(file);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
			this.startActivityForResult(intent, 100);
			
			break;
		case R.id.button2:

			Intent intent2 = new Intent();
			intent2.setClass(this, AlbumGalleryActivity.class);
			intent2.putExtra("num", 1);
			startActivityForResult(intent2, 200);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode != RESULT_OK) {
			return;
		}
		
		switch (requestCode) {
		case 100:
			
			if (!file.exists()) {
				return;
			}
			Bitmap b1 = BitmapCompressImage.getimage(file.getAbsolutePath());
//			Drawable background = DF
			Drawable drawable =new BitmapDrawable(b1);
			rl.setBackground(drawable);
		
			break;
		case 200:
			
			if (data == null) {
				
				return;
			}
			String[] arr = data.getStringArrayExtra("arr");
			Bitmap b2 = BitmapCompressImage.getimage(arr[0]);
//			Drawable background = DF
			Drawable drawable2 =new BitmapDrawable(b2);
			rl.setBackground(drawable2);
			
			break;

		}
		
		
	}

}
