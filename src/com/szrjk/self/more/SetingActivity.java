package com.szrjk.self.more;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.LoginActivity;
import com.szrjk.dhome.LoginHelper;
import com.szrjk.dhome.MainActivity;
import com.szrjk.dhome.R;
import com.szrjk.dhome.SelfActivity;
import com.szrjk.entity.IPopupItemCallback;
import com.szrjk.entity.PopupItem;
import com.szrjk.entity.UserInfo;
import com.szrjk.self.UserBackgroundSelectActivity;
import com.szrjk.util.FileUtils;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.ListPopup;
import com.szrjk.widget.TextSizePopup;

@ContentView(R.layout.activity_seting)
public class SetingActivity extends BaseActivity {

	@ViewInject(R.id.ll_seting)
	private LinearLayout ll_seting;

	@ViewInject(R.id.rl_change_hportrait)
	private RelativeLayout rl_change_hportrait;

	@ViewInject(R.id.rl_change_background)
	private RelativeLayout rl_change_background;

	@ViewInject(R.id.rl_change_password)
	private RelativeLayout rl_change_password;

	@ViewInject(R.id.rl_change_phonenumber)
	private RelativeLayout rl_change_phonenumber;

	@ViewInject(R.id.tv_phoneNumber)
	private TextView tv_phoneNumber;

	@ViewInject(R.id.rl_clear_cache)
	private RelativeLayout rl_clear_cache;

	@ViewInject(R.id.tv_cacheSize)
	private TextView tv_cacheSize;

	@ViewInject(R.id.rl_text_size)
	private RelativeLayout rl_text_size;
	@ViewInject(R.id.tv_size)
	private TextView tv_size;

	@ViewInject(R.id.rl_log_out)
	private RelativeLayout rl_log_out;

	private SetingActivity instance;

	private UserInfo userInfo;

	private File cacheDir=null;

	private TextSizePopup textSizePopup;

	private static final int CHANGE_PORTRAIT_SUCCESS=0;

	private static final int CHANGE_PHONE_SUCCESS = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		cacheDir=StorageUtils.getOwnCacheDirectory(instance, "imageloader/cache", true);
		ViewUtils.inject(this);
		initLayout();
	}

	private void initLayout() {
		userInfo = Constant.userInfo;
		tv_phoneNumber.setText(userInfo.getPhone());
		String cacheSize=getCache();
		Log.i("cacheSize", cacheSize + "");
		tv_cacheSize.setText(cacheSize);



		SharedPreferences mySharedPreferences= this.getSharedPreferences("text_size",
				Context.MODE_PRIVATE);
		//用putString的方法保存数据
		String size = mySharedPreferences.getString("size","");
		if(size==null){
		}else if(size.equals("small")){
			tv_size.setText("小");
		}else if(size.equals("middle")){
			tv_size.setText("中");
		}else if(size.equals("large")){
			tv_size.setText("大");
		}



	}

	public String getCache() {
		String cache=null;
		try {

			double cacheSize=DataCleanManager.getFolderSize(getCacheDir())+DataCleanManager.getFolderSize(getFilesDir())+DataCleanManager.getFolderSize(new File(FileUtils.SDPATH))+DataCleanManager.getFolderSize(cacheDir);
			cache = DataCleanManager.getFormatSize(cacheSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cache;
	}

	@OnClick(R.id.rl_change_hportrait)
	public void changePortraitClick(View v) {
		ToastUtils.showMessage(instance, "等待需求更改");
		send();
		//		Intent intent = new Intent(instance, ChangePortraitActivity.class);
		//		Bundle bundle = new Bundle();
		//		// 把图片地址的urlList传递过去
		//		bundle.putString("userfaceUrl", userInfo.getUserFaceUrl());
		//		intent.putExtras(bundle);
		//		startActivityForResult(intent, CHANGE_PORTRAIT_SUCCESS);
	}

	private void send() {
		//1.得到NotificationManager  
		NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); 
		//设置通知栏跳转目标
		Intent intent = new Intent(this, SelfActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pi= PendingIntent.getActivity(this, 0, intent, 0);  
		//用builder设置Notification
		Notification n = new Notification.Builder(instance)
		.setContentTitle("国明周五请吃饭")
		.setContentText("周五下班走起")
		.setSmallIcon(R.drawable.icon_messages)
		.build();
		n.contentIntent = pi;
		//此条将通知放在”正在进行“栏，否则在通知栏
//		n.flags =Notification.FLAG_ONGOING_EVENT;
		//单击通知，通知消失
		n.flags |= Notification.FLAG_AUTO_CANCEL;
		//通知声音
		n.defaults = Notification.DEFAULT_SOUND;   
		//启用通知
		nm.notify(1, n); 


	}

	@OnClick(R.id.rl_change_background)
	public void changeBackgroundClick(View v) {
		ToastUtils.showMessage(instance, "等待需求更改");
		//		Intent intent = new Intent(instance, UserBackgroundSelectActivity.class);
		//		startActivity(intent);
	}

	@OnClick(R.id.rl_change_password)
	public void changePasswordClick(View v) {
		Intent intent = new Intent(instance, ChangePasswordActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.rl_change_phonenumber)
	public void changePhoneNumClick(View v) {
		Intent intent = new Intent(instance, AuthenticationActivity.class);
		startActivityForResult(intent, CHANGE_PHONE_SUCCESS);
	}

	@OnClick(R.id.rl_clear_cache)
	public void clearCacheNumClick(View v) {
		clearCache();
	}

	public void clearCache() {
		DataCleanManager.cleanInternalCache(getApplication());
		DataCleanManager.cleanExternalCache(getApplication());
		DataCleanManager.cleanFiles(getApplication());
		DataCleanManager.deleteFolderFile(FileUtils.SDPATH, true);
		ImageLoader.getInstance().clearDiskCache();
		tv_cacheSize.setText(getCache());
		ToastUtils.showMessage(instance, "清除缓存成功！");
		Log.i("cacheSize1", getCache()+"");
	}

	@OnClick(R.id.rl_text_size)
	public void textSizeClick(View v) {
		//		textSizePopup = new TextSizePopup(instance, itemsOnClick);
		//		textSizePopup.showAtLocation(ll_seting, Gravity.BOTTOM
		//				| Gravity.CENTER_HORIZONTAL, 0, 0);
		showPoP(ll_seting);
	}

	private OnClickListener itemsOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_large:

				//系统的
				//				 Intent intent = new Intent("/");
				//				 ComponentName cn = new ComponentName("com.android.settings",
				//				 "com.android.settings.Display");
				//				 intent.setComponent(cn);
				//				 startActivityForResult(intent, 1);
				break;
			case R.id.tv_medium:

				textSizePopup.dismiss();
				break;
			case R.id.tv_small:

				textSizePopup.dismiss();
				break;
			case R.id.tv_logout:
				jumpLoginActivity();
				break;

			}
		}
	};

	private ListPopup window;

	@OnClick(R.id.rl_log_out)
	public void logOutClick(View v) {
		//		LogoutPopup logoutPopup = new LogoutPopup(instance, itemsOnClick);
		//		logoutPopup.showAtLocation(ll_seting, Gravity.BOTTOM
		//				| Gravity.CENTER_HORIZONTAL, 0, 0);
		showPoExit(ll_seting);
	}

	private void jumpLoginActivity() {
		//		SharePerferenceUtil perferenceUtil = SharePerferenceUtil.getInstance(instance,
		//				Constant.USER_INFO);
		//注销用户信息和登陆状态
		//		perferenceUtil.setBooleanValue(Constant.LOGIN_STATE, false);
		//		Constant.userInfo = null;//登出
		//
		//		//取消推送信息
		//		final PushAgent pushAgent = PushAgent.getInstance(SetingActivity.this);
		//		pushAgent.disable();
		LoginHelper.Logout(instance);
		Intent intent = new Intent();
		//跳到登陆界面
		intent.setClass(instance, LoginActivity.class);
		startActivity(intent);
		//关闭界面
		instance.finish();
		//		MoreActivity.instance.finish();
		MainActivity.instance.finish();
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CHANGE_PORTRAIT_SUCCESS:
			if (data==null) {
				break;
			}
			Bundle bundle=data.getExtras();
			if (bundle.getBoolean("CHANGE_PORTRAIT_SUCCESS")) {
				Intent intent=new Intent();
				intent.putExtras(bundle);
				setResult(CHANGE_PORTRAIT_SUCCESS,intent);
				instance.finish();
			}
			break;
		case CHANGE_PHONE_SUCCESS:
			tv_phoneNumber.setText(userInfo.getPhone());
			break;
		}
	}


	//设置字体大小
	public void changeTextSize(String size){
		//实例化SharedPreferences对象（第一步） 
		SharedPreferences mySharedPreferences= getSharedPreferences("text_size", 
				Context.MODE_PRIVATE); 
		//实例化SharedPreferences.Editor对象（第二步） 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		//用putString的方法保存数据 
		editor.putString("size", size);
		//提交当前数据
		editor.commit(); 
		//使用toast信息提示框提示成功写入数据 
		Toast.makeText(this, "调整字号成功！"+ "  " , Toast.LENGTH_LONG).show();
	}


	/**显示sendWindow**/
	private void showPoP(View v){
		//		sendWindow = new PostSendPopup(instance, sendPostClick);

		List<PopupItem> pilist = new ArrayList<PopupItem>();
		PopupItem pi1 = new PopupItem();
		pi1.setItemname("大");//设置名称
		pi1.setColor(this.getResources().getColor(R.color.search_bg));
		pi1.setiPopupItemCallback(new IPopupItemCallback() {  //设置click动作
			@Override
			public void itemClickFunc(PopupWindow sendWindow) {
				sendWindow.dismiss();
				tv_size.setText("大");
				changeTextSize("large");

			}
		});
		PopupItem pi2 = new PopupItem();
		pi2.setItemname("中");
		pi2.setColor(this.getResources().getColor(R.color.search_bg));
		pi2.setiPopupItemCallback(new IPopupItemCallback() {
			@Override
			public void itemClickFunc(PopupWindow sendWindow) {
				sendWindow.dismiss();
				tv_size.setText("中");
				changeTextSize("middle");
			}
		});
		PopupItem pi3 = new PopupItem();
		pi3.setItemname("小");
		pi3.setColor(this.getResources().getColor(R.color.search_bg));
		pi3.setiPopupItemCallback(new IPopupItemCallback() {
			@Override
			public void itemClickFunc(PopupWindow sendWindow) {
				sendWindow.dismiss();

				tv_size.setText("小");
				changeTextSize("small");
			}
		});
		pilist.add(pi1);
		pilist.add(pi2);
		pilist.add(pi3);
		window = new ListPopup(this,pilist,v);
	}
	/**显示sendWindow**/
	private void showPoExit(View v){
		//		sendWindow = new PostSendPopup(instance, sendPostClick);

		List<PopupItem> pilist = new ArrayList<PopupItem>();
		PopupItem pi1 = new PopupItem();
		pi1.setItemname("确认退出");//设置名称
		pi1.setColor(this.getResources().getColor(R.color.search_bg));
		pi1.setiPopupItemCallback(new IPopupItemCallback() {  //设置click动作
			@Override
			public void itemClickFunc(PopupWindow sendWindow) {
				sendWindow.dismiss();
				jumpLoginActivity();
			}
		});
		pilist.add(pi1);
		window = new ListPopup(this,pilist,v);
	}
}
