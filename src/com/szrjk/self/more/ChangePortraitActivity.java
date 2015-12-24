package com.szrjk.self.more;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.IImgUrlCallback;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.util.ToastUtils;
import com.szrjk.util.UploadPhotoUtils;
import com.szrjk.util.corpimageview.CropImageActivity;
import com.szrjk.widget.HeaderView;

@ContentView(R.layout.activity_change_portrait)
public class ChangePortraitActivity extends BaseActivity {

	@ViewInject(R.id.hv_user_avatar_changer)
	private HeaderView hv_user_avatar_changer;
	@ViewInject(R.id.iv_portrait)
	private ImageView iv_portrait;

	@ViewInject(R.id.btn_change_portrait)
	private Button btn_change_portrait;

	private ChangePortraitActivity instance;
	private Intent intent;
	private Bundle bundle;

	private UploadPhotoUtils uploadPhotoUtils;

	private UserInfo userInfo;

	private static final int CHANGE_PORTRAIT_SUCCESS = 0;

	@ViewInject(R.id.rl_img)
	private RelativeLayout rl_img;
	private int rl_img_height;
	private byte[] clipBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		hv_user_avatar_changer.setHtext("个人头像");
		instance = this;
		intent = getIntent();
		bundle = intent.getExtras();
		userfaceUrl = bundle.getString("userfaceUrl");
		clipBitmap = bundle.getByteArray("CLIPBITMAP");

		// Log.i("userfaceUrl", userfaceUrl);
		// 过得包裹图片的高度、再获得屏幕的宽度、再计算放在中间
		getRelaHeight();
		userInfo = Constant.userInfo;
	}

	private void initImageView() {
		iv_portrait.setBackgroundColor(0xff000000);
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		Log.i("screenWidth", screenWidth + "");
		// Log.i("screenHeigh", screenHeigh+"");
		// int screenWidth=getWindowManager().getDefaultDisplay().getWidth();
		// iv_portrait.setLayoutParams(new
		// LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.WRAP_CONTENT));
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				screenWidth, screenWidth);

		// Log.i("hhhhh", (rl_img_height - screenWidth)/2 + "");
		// 用相对布局的高度 - 屏幕宽度 除以2.设置一个marginTop
		lp.topMargin = (rl_img_height - screenWidth) / 2;
		iv_portrait.setLayoutParams(lp);
		if (userfaceUrl != null) {
			ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil(instance,
					userfaceUrl, iv_portrait, R.drawable.header,
					R.drawable.header);
			imageLoaderUtil.showImage();
		} else {
			if (clipBitmap != null) {
				Bitmap bitmap = BitmapFactory.decodeByteArray(clipBitmap, 0,
						clipBitmap.length);
				if (bitmap != null) {
					iv_portrait.setImageBitmap(bitmap);
				}
			} else {
				iv_portrait.setImageResource(R.drawable.header);
			}
		}
	}

	private String userfaceUrl;

	@OnClick(R.id.btn_change_portrait)
	public void changePortraitClick(View view) {
		try {
			uploadPhotoUtils = new UploadPhotoUtils(instance, true);
			uploadPhotoUtils.popubphoto(iv_portrait, new IImgUrlCallback() {
				@Override
				public void operImgUrl(String imgurl) {
					userfaceUrl = imgurl;
					changePortrait();
					// Log.i("新的头像地址", coterieFaceUrl);
				}
				@Override
				public void operImgPic(Bitmap bitmap) {
//					iv_portrait.setImageBitmap(bitmap);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void changePortrait() {
		// 调用注册接口
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "dealUserUIElement");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", userInfo.getUserSeqId());
		busiParams.put("faceUrl", userfaceUrl);
		busiParams.put("backgroundUrl", "");
		paramMap.put("BusiParams", busiParams);
		// 调用接口
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				Intent intent = new Intent();
				intent.setAction("CHANGE_PORTRAIT_SUCCESS");
				intent.putExtra("CHANGE_PORTRAIT_SUCCESS", true);
				intent.putExtra("userfaceUrl", userfaceUrl);
				instance.sendBroadcast(intent);
//				ToastUtils.showMessage(instance, "更改头像成功！");
//				userInfo.setUserFaceUrl(userfaceUrl);
			}

			@Override
			public void start() {
			}

			@Override
			public void loading(long total, long current,
					boolean isUploading) {
			}

			@Override
			public void failure(HttpException exception, final JSONObject jobj) {
				runOnUiThread(new  Runnable() {
					@Override
					public void run() {
						ToastUtils.showMessage(instance,jobj.getString("ReturnInfo"));
					}
				});
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		try {
			if (resultCode != RESULT_OK) {
				return;
			}
			Log.i("onActivityResult", "进入图片处理工具");
			uploadPhotoUtils.imgOper(requestCode, resultCode, data);
//		} catch (Exception e) {
//			showToast(instance, "拍照异常，请重试", 0);
//			Constant.userInfo = userInfo2;
//			e.printStackTrace();
//		}
	}

	// Activity里用户操作的所有数据全部需要保存
	// Activity中第一次初始化时获取到的变量也需要保存
	// 用户登陆后所有拥有的相关权限也需要进行处理保存
	// 数据恢复时要考虑当前Activity引用的其他Activity或Application里面的变量的再次初始化
	// 所有自定义对象都最好能被序列化，否则无法进行状态保存
	// 尽量少用static类型的变量
	// 有些服务能不在Application中初始化，最好不要在Application中初始化。
	// 在Application中定义的变量最后都在Application中进行初始化创建，不要部分在Application进行初始化，部分在其他的Activity里进行初始化
	@Override
	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
		outState.putSerializable("userInfo", userInfo);
		outState.putString("userfaceUrl", userfaceUrl);
		outState.putByteArray("clipBitmap", clipBitmap);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		super.onRestoreInstanceState(savedInstanceState);
		userInfo = (UserInfo) savedInstanceState.getSerializable("userInfo");
		userfaceUrl = savedInstanceState.getString("userfaceUrl");
		clipBitmap = savedInstanceState.getByteArray("clipBitmap");
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		Log.i("onConfigurationChanged", "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}
	@Override
	protected void onDestroy() {
		CropImageActivity.bp = null;
		super.onDestroy();
	}

	private void getRelaHeight() {
		ViewTreeObserver vto2 = rl_img.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				rl_img.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				rl_img_height = rl_img.getHeight();
				Log.i("rl_img--h", rl_img_height + "");
				// 顺序不能乱、这里必须有高度、
				initImageView();
			}
		});
	}
}
