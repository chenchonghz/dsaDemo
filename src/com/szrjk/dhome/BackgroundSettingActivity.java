package com.szrjk.dhome;

import java.io.File;
import java.io.IOException;

import com.lidroid.xutils.BitmapUtils;
import com.szrjk.self.more.album.AlbumGalleryActivity;
import com.szrjk.util.BitmapCompressImage;
import com.szrjk.util.PictureLoader;

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

public class BackgroundSettingActivity extends BaseActivity implements OnClickListener{

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
			file = PictureLoader.getCamera(this);
			break;
		case R.id.button2:
			PictureLoader.getAlbum(this, 1);
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
		case PictureLoader.CAMERA:
			
			if (!file.exists()) {
				return;
			}
			Bitmap b1 = BitmapCompressImage.getimage(file.getAbsolutePath());
			Drawable drawable =new BitmapDrawable(b1);
			rl.setBackground(drawable);
		
			break;
		case PictureLoader.Album:
			if (data == null) {
				return;
			}
			String[] arr = PictureLoader.getIntentData(data);
			
			Bitmap b2 = BitmapCompressImage.getimage(arr[0]);
//			Drawable background = DF
			Drawable drawable2 =new BitmapDrawable(b2);
			rl.setBackground(drawable2);
			
			break;

		}
		
		
	}

}
