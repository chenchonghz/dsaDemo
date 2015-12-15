package com.szrjk.util.corpimageview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.dhome.R;
import com.szrjk.util.CheckImageDegree;

@ContentView(R.layout.activity_crop_image)
public class CropImageActivity extends Activity implements OnClickListener{

	public static Bitmap bp  ;

	private CropImageActivity instance;

	@ViewInject(R.id.img_crop)
	private CropImageView img_crop;

	@ViewInject(R.id.btn_submit)
	private Button btn_submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		//图片bitmap、path、uri、都可以
		Intent intent = getIntent();
		String imgPath = intent.getStringExtra("camear");
		//检查角度。然后更正竖着
		Bitmap newBit = CheckImageDegree.getNewBitmap(null,imgPath);
		Log.i("path", imgPath);
		//			Drawable mDrawable = BitmapDrawable.createFromPath(imgPath);
		Drawable mDrawable = new BitmapDrawable(newBit);
		img_crop.setDrawable(mDrawable, 200, 200);
		btn_submit.setOnClickListener(this);
		getCropViewHeight();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			//剪裁之后，获得bitmap
			Bitmap b = img_crop.getCropImage();
			//			Intent data = new Intent();
			//			data.putExtra("cropbitmap", b);
			bp = b;
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}

	}
	public static int cropViewHeight;
	private void getCropViewHeight() {
		ViewTreeObserver vtos = img_crop.getViewTreeObserver();    
		vtos.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  

			@Override    
			public void onGlobalLayout() {  
				img_crop.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
				cropViewHeight = img_crop.getHeight();
				Log.e("h", img_crop+"");
			}    
		});
		
	}
}
