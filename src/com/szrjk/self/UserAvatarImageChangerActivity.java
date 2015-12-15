package com.szrjk.self;



import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.widget.HeaderView;

import android.os.Bundle;
import android.provider.Contacts;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
/**
 * 用户点击自己头像显示大图界面
 * @author 郑斯铭
 *
 */
@ContentView(R.layout.activity_user_avatar_image_changer)
public class UserAvatarImageChangerActivity extends Activity implements OnClickListener{
	@ViewInject(R.id.hv_user_avatar_changer)
	private HeaderView hv_avatar;
	@ViewInject(R.id.iv_avatar_changer)
	private ImageView iv_avatar_changer;
	@ViewInject(R.id.lly_user_avator_changer)
	private LinearLayout lly_user_avator_changer;
	@ViewInject(R.id.rly_user_avatar_changer)
	private RelativeLayout rly_user_avator_changer;
	@ViewInject(R.id.btn_user_avatar_change)
	private Button btn_user_avatar_change;


		protected void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			ViewUtils.inject(this);
			//加载监听器
			initListener();
			//获得图片url
			String Url=getIntents();
			//获得屏幕宽度
			int ScreenX=getScreen();
			//,返回bitmap格式，放入控件
			setPicture(Url,ScreenX);
			
		}
		private String getIntents() {
			Intent intent = getIntent();
			String Code = intent.getStringExtra("code");
			if(Code.equals(Constant.PICTURE_FACE_CODE)){
//				Log.i("IMG", "来自头像");
				
			}else if (Code.equals(Constant.PICTURE_OTHER_CODE)) {
//				Log.i("IMG", "来自其他");
				rly_user_avator_changer.setVisibility(View.GONE);
				hv_avatar.setVisibility(View.GONE);
				 
			}
			return intent.getStringExtra("image");
			
		}
		private void setPicture(String url, int screenX) {
			BitmapUtils bitmapUtils = new BitmapUtils(this);
//			BitmapDisplayConfig bdc = new BitmapDisplayConfig();
//			BitmapSize size = new BitmapSize(screenX, screenX);
//			bdc.setBitmapMaxSize(size);
			bitmapUtils.display(iv_avatar_changer, url);
		}
		private void initListener() 
		{
			lly_user_avator_changer.setOnClickListener(this);
			btn_user_avatar_change.setOnClickListener(this);
		}
		private int getScreen() {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			int Screenwidth = dm.widthPixels;
			int Screenheight = dm.heightPixels;
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(Screenwidth, Screenheight);
			iv_avatar_changer.setLayoutParams(param);
			return Screenwidth;
		}
		//点击线性布局黑色界面退出 ，点击btn更换头像
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.lly_user_avator_changer:
				finish();
				break;
			case R.id.btn_user_avatar_change:
//				Toast.makeText(this, "更改头像", Toast.LENGTH_SHORT).show();
				break;
			}
			
		}





		
	}