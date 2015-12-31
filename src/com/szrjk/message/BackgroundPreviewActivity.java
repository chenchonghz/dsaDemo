package com.szrjk.message;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.TMessage;
import com.szrjk.entity.UserCard;
import com.szrjk.widget.HeaderView;
/**
 * @author ldr
 * 2015-12-30 15:23:35
 * description：用户选择图片之后的界面预览
 * 传入参数：path
 */
@ContentView(R.layout.activity_background_preview)
public class BackgroundPreviewActivity extends BaseActivity {

	@ViewInject(R.id.hv_user_avatar_changer)
	private HeaderView hv_user_avatar_changer;
	@ViewInject(R.id.iv_portrait)
	private ImageView iv_portrait;

	@ViewInject(R.id.btn_change_portrait)
	private Button btn_change_portrait;

	private BackgroundPreviewActivity instance;
	private Intent intent;
	@ViewInject(R.id.rl_img)
	private RelativeLayout rl_img;
	private int rl_img_height;
	private String path;
	private UserCard obj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		hv_user_avatar_changer.setHtext("背景图预览");
		instance = this;
		intent = getIntent();
		path = intent.getStringExtra("path");
		obj = (UserCard)intent.getSerializableExtra("obj");
		// Log.i("userfaceUrl", userfaceUrl);
		// 过得包裹图片的高度、再获得屏幕的宽度、再计算放在中间
		try {
			getRelaHeight();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		// Log.i("hhhhh", (rl_img_height - screenWidth)/2 + "");
		// 用相对布局的高度 - 屏幕宽度 除以2.设置一个marginTop
		//lp.topMargin = (rl_img_height - screenWidth) / 2;
		iv_portrait.setLayoutParams(lp);
		BitmapUtils utils = new BitmapUtils(instance);
		utils.display(iv_portrait, path);
	}

	@OnClick(R.id.bt_apply)
	public void applyClick(View view) {
		if (path == null) {
			Log.i("","path is null");
			return;
		}
		try {
			new TMessage().addBackground(obj, path);
		} catch (DbException e) {
			e.printStackTrace();
		}
		
		Intent intent = new Intent(instance, MessageActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	// Activity里用户操作的所有数据全部需要保存
	// Activity中第一次初始化时获取到的变量也需要保存
	// 用户登陆后所有拥有的相关权限也需要进行处理保存
	// 数据恢复时要考虑当前Activity引用的其他Activity或Application里面的变量的再次初始化
	// 所有自定义对象都最好能被序列化，否则无法进行状态保存
	// 尽量少用static类型的变量
	// 有些服务能不在Application中初始化，最好不要在Application中初始化。
	// 在Application中定义的变量最后都在Application中进行初始化创建，不要部分在Application进行初始化，部分在其他的Activity里进行初始化
	
	
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
