package com.szrjk.self;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.dhome.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * 全屏显示用户头像，自动缩放至最大，背景为黑
 * @author 郑斯铭
 * 10.26  暂时默认用户传入的头像为bitmap文件
 *	
 */
@ContentView(R.layout.activity_user_avatar_image_shower)
public class UserAvatarImageShower extends Activity implements OnClickListener{
@ViewInject(R.id.iv_avatar_shower)
private ImageView iv_avatar_shower;
@ViewInject(R.id.lly_user_avator_shower)
private LinearLayout lly_user_avator_shower;
private int ScreenX;


	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		initListener();
		Intent intent = getIntent();
		byte[] bs=intent.getByteArrayExtra("image");
		Bitmap bm = BitmapFactory.decodeByteArray(bs, 0, bs.length);
		getScreen();
		Bitmap res_bm =resizeBitmap(bm,ScreenX,ScreenX);
		
		iv_avatar_shower.setImageBitmap(res_bm);
	}
	private void initListener() 
	{
		lly_user_avator_shower.setOnClickListener(this);
	}
	private void getScreen() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		ScreenX = dm.widthPixels;
	}
	private Bitmap resizeBitmap(Bitmap bm, int screenX, int screenY) 
	{
		 int width = bm.getWidth();
         int height =bm.getHeight();
         int newWidth = screenX;
         int newHeight = screenY;
         float scaleWight = ((float)newWidth)/width;
         float scaleHeight = ((float)newHeight)/height;
         Matrix matrix = new Matrix();
         matrix.postScale(scaleWight, scaleHeight);
         Bitmap res = Bitmap.createBitmap(bm, 0,0,width, height, matrix, true);
         return res;
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.lly_user_avator_shower:
			finish();
			break;
		}
		
	}





	
}
