package com.szrjk.util.clip;

import java.io.File;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;

/**
 * 
 * @author ldr
 * 这个由于可能被回收；（拍照占用ram），不能横竖屏，去拍照的时候，要保存状态）
 */
public class ClipActivity extends BaseActivity{
	private ClipImageLayout mClipImageLayout;
	private String path;
	private Dialog alertdialog;
	public static Bitmap cbitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);//必须
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clipimage);
		initLayout();
		//这里不能少
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
			alertdialog = createDialog(this, "加载中...");
			alertdialog.show();
//			path = getIntent().getStringExtra("path");
			Intent ipath = this.getIntent();
			path = ipath.getStringExtra("camear");
//			Log.i("裁剪界面", path);
			if (TextUtils.isEmpty(path) ) {
				Log.i("剪", "路径空");
				Toast.makeText(this, "加载图片失败", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!(new File(path).exists())) {
				Log.i("剪", "file不存在");
				Toast.makeText(this, "加载图片失败", 0).show();
				return;
			}
			Bitmap bitmap = ImageTools.convertToBitmap(path, 600, 600);//?
			if (bitmap == null) {
				Toast.makeText(this, "加载图片失败", Toast.LENGTH_SHORT).show();
				return;
			}
			mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
			//		mClipImageLayout.setHorizontalPadding(0);
			mClipImageLayout.setBitmap(bitmap);
			alertdialog.dismiss();
			((Button) findViewById(R.id.id_action_clip))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							alertdialog.show();
							new Thread(new Runnable() {
								@Override
								public void run() {
									Bitmap bp = mClipImageLayout.clip();
									cbitmap = bp;
//									String path = Environment
//											.getExternalStorageDirectory()
//											+ "/ClipHeadPhoto/cache/"
//											+ System.currentTimeMillis()
//											+ ".png";
//									ImageTools.savePhotoToSDCard(bitmap, path);
									alertdialog.dismiss();
//									Intent intent = new Intent();
//									intent.putExtra("path", path);
									setResult(RESULT_OK);
									finish();
								}
							}).start();
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initLayout() {
		findViewById(R.id.tv_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}

	/**
	 * 这里的3个重写方法：由于打开了拍照，低ram的手机会回收发帖这个Activity。
	 * 当回收之后，会执行onCreate方法里面的检查草稿方法。导致把草稿恢复覆盖当前编辑的内容
	 * @param newConfig
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		Log.i("onConfigurationChanged", "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i("onRestoreInstanceState", "onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("onSaveInstanceState", "onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}
}
