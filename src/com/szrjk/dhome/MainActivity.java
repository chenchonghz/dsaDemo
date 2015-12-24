package com.szrjk.dhome;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.entity.IPopupItemCallback;
import com.szrjk.entity.PopupItem;
import com.szrjk.entity.RemindEvent;
import com.szrjk.index.SendCaseActivity;
import com.szrjk.index.SendPostActivity;
import com.szrjk.index.SendPuzzleActivity;
import com.szrjk.search.SearchMainActivity;
import com.szrjk.util.BusiUtils;
import com.szrjk.util.DialogUtil;
import com.szrjk.util.SharePerferenceUtil;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.ListPopup;
import com.szrjk.widget.TabPhotoPopup;

import de.greenrobot.event.EventBus;

@ContentView(R.layout.activity_main)
public class MainActivity extends FragmentActivity implements OnClickListener , OnTouchListener
{
	@ViewInject(R.id.rl_main)
	private RelativeLayout rl_main;
	//首页
	private IndexFragment indexFragment;
	//发现
	private ExploreFragment2 exploreFragment;
	//知识库
	private LibraryFragment libraryFragment;
	//我
//	private SelfFragment selfFragment;
	private MoreFragment moreFragment;
//	@ViewInject(R.id.iv_search)
//	private ImageView iv_search;
	@ViewInject(R.id.fl_index)
	private FrameLayout indexLayout;
	@ViewInject(R.id.fl_explore)
	private FrameLayout exploreLayout;
	@ViewInject(R.id.fl_library)
	private FrameLayout libraryLayout;
	@ViewInject(R.id.fl_self)
	private FrameLayout selfLayout;
	@ViewInject(R.id.iv_index)
	private ImageView ivIndex;
	@ViewInject(R.id.iv_explore)
	private ImageView ivExplore;
	@ViewInject(R.id.iv_library)
	private ImageView ivLibrary;
	@ViewInject(R.id.iv_self)
	private ImageView ivSelf;
	@ViewInject(R.id.iv_action_bg)
	private ImageView ivActionBg;
	@ViewInject(R.id.iv_action)
	private ImageView ivAction;
	@ViewInject(R.id.tv_index)
	private TextView tv_index;
	@ViewInject(R.id.tv_explore)
	private TextView tv_explore;
	@ViewInject(R.id.tv_library)
	private TextView tv_library;
	@ViewInject(R.id.tv_self)
	private TextView tv_self;
	//"我的"页面红点提示
	@ViewInject(R.id.iv_remind_self)
	private ImageView iv_remind_self;
	private TabPhotoPopup menuWindow;
	public static MainActivity instance;
	private SharePerferenceUtil perferenceUtil;
	private boolean isSkipToSelf;
	private boolean isTourist;
	private Fragment lastfragment ;
	private int Clickposition =0;
	private List<Fragment> list = new ArrayList<Fragment>();
	private boolean First = true;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance = this;
		list.add(indexFragment);
		list.add(exploreFragment);
		list.add(libraryFragment);
		list.add(moreFragment);
		ViewUtils.inject(this);
		DHomeApplication.mainActivity=this;
		initData();
		initListener();
		clickIndex();
//		EventBus.getDefault().register(this);
		getSharedPreference();
	}
	public static void finishMain(){
		instance.finish();
	}
	//检查持久化存储
	private void getSharedPreference() {
		SharedPreferences remind = getSharedPreferences("remind", Context.MODE_PRIVATE);
		String userid = remind.getString("UserID", "newUser");
		if(userid.equals("newUser")){
//			Log.i("TAG", "新用户");
		}else if(userid.equals(Constant.userInfo.getUserSeqId())){
			boolean havemessage  = remind.getBoolean("haveMessage", false);
			boolean havefriend  = remind.getBoolean("haveFriend", false);
			boolean havecricle = remind.getBoolean("haveCircle", false);
			if (havecricle||havemessage||havefriend) {
				EventBus.getDefault().post(new RemindEvent(4));
//				Log.i("TAG", "post");
			}
//			Log.i("TAG", havemessage+"+"+havefriend+"+"+havecricle+"检查完毕");
		}else{
//			Log.i("TAG", "更换了用户");
		}

	}

	private void initData() {
		// TODO Auto-generated method stub
		isTourist = BusiUtils.isguest(instance);
		Intent intent = getIntent();
		isSkipToSelf = intent.getBooleanExtra("isSkipToSelf", false);
		Log.e("MainActivity", "是否进入个人主页"+isSkipToSelf);
	}

	private void initListener()
	{
		perferenceUtil = SharePerferenceUtil.getInstance(instance,
				Constant.USER_INFO);
		//		indexLayout.setOnClickListener(this);
		exploreLayout.setOnClickListener(this);
		libraryLayout.setOnClickListener(this);
		selfLayout.setOnClickListener(this);
		ivActionBg.setOnClickListener(this);
		ivAction.setOnClickListener(this);
//		iv_search.setOnClickListener(this);
		indexLayout.setOnTouchListener(this);

	}


	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		//		case R.id.fl_index:
		//			clickIndex();
		//			Clickposition =0;
		//			break;
		case R.id.fl_explore:
			clickExplore();
			Clickposition =1;
			break;
		case R.id.fl_library:
			clickLibrary();
			Clickposition =2;
			break;
		case R.id.fl_self:
			if(isTourist){
				DialogUtil.showGuestDialog(instance, null);
			}else{			
				clickSelf();
			}
			Clickposition =3;
			break;
		case R.id.iv_action_bg:
			if(isTourist){
				DialogUtil.showGuestDialog(instance, null);
			}else{				
				menuWindow = new TabPhotoPopup(this, itemsOnClick);
				// 显示窗口
				menuWindow.showAtLocation(ivActionBg, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);
			}
			break;
		case R.id.iv_action:
			if(isTourist){
				DialogUtil.showGuestDialog(instance, null);
			}else{				
				menuWindow = new TabPhotoPopup(this, itemsOnClick);
				// 显示窗口
				menuWindow.showAtLocation(ivActionBg, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);
			}
			break;
		case R.id.iv_search:
			if(isTourist){
				DialogUtil.showGuestDialog(instance, null);
			}else{			
				Intent intent = new Intent(instance, SearchMainActivity.class);
				startActivity(intent);
				break;
			}
		}
	}

	private OnClickListener itemsOnClick = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.lly_post:
				jumpPostActivity();
				menuWindow.dismiss();
				break;
			case R.id.lly_share:
				jumpCaseActivity();
				menuWindow.dismiss();
				break;
			case R.id.lly_help:
				jumpPuzzleActivity();
				menuWindow.dismiss();
				break;
			case R.id.lly_cancel:
				menuWindow.dismiss();
				break;
			}
		}
	};

	// 跳转到发帖界面
	private void jumpPostActivity()
	{
		Intent intent = new Intent();
		intent.setClass(instance, SendPostActivity.class);
		startActivity(intent);
	}

	// 跳转到发病例分享界面
	private void jumpCaseActivity()
	{
		Intent intent = new Intent();
		intent.setClass(instance, SendCaseActivity.class);
		startActivity(intent);
	}

	// 跳转到发疑难求助界面
	private void jumpPuzzleActivity()
	{
		Intent intent = new Intent();
		intent.setClass(instance, SendPuzzleActivity.class);
		startActivity(intent);
	}
	private boolean indexFirst = true;
	private boolean exploreFirst = true;
	private boolean libraryFirst = true;
	private boolean selfFirst = true;
	private void clickIndex()
	{
		if (indexFragment == null) {
			indexFragment = new IndexFragment();
		}
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		if (indexFirst) {
			fragmentTransaction.add(R.id.fl_content, indexFragment);
			indexFirst = false;
		}else{
			fragmentTransaction.hide(lastfragment);
			fragmentTransaction.show(indexFragment);
		}
		// 事务管理提交
		fragmentTransaction.commit();
		// 改变选中状态
		indexLayout.setSelected(true);
		ivIndex.setSelected(true);
		tv_index.setTextColor(getResources().getColor(R.color.link_text_color));
		tv_explore.setTextColor(Color.BLACK);
		tv_library.setTextColor(Color.BLACK);
		tv_self.setTextColor(Color.BLACK);

		exploreLayout.setSelected(false);
		ivExplore.setSelected(false);

		libraryLayout.setSelected(false);
		ivLibrary.setSelected(false);

		selfLayout.setSelected(false);
		ivSelf.setSelected(false);
		lastfragment = indexFragment;
	}

	private void clickExplore()
	{
		if (exploreFragment==null) {
			exploreFragment = new ExploreFragment2();
		}

		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		fragmentTransaction.hide(lastfragment);
		if (exploreFirst) {
			fragmentTransaction.add(R.id.fl_content, exploreFragment);
			exploreFirst = false;
			//			fristFragment = exploreFragment;
		}else{
			//			fragmentTransaction.hide(indexFragment);
			fragmentTransaction.show(exploreFragment);
			//			fristFragment = exploreFragment;
		}
		fragmentTransaction.commit();
//		exploreFragment.scrollToTop();
		indexLayout.setSelected(false);
		ivIndex.setSelected(false);
		tv_explore.setTextColor(getResources().getColor(R.color.link_text_color));
		tv_index.setTextColor(Color.BLACK);
		tv_library.setTextColor(Color.BLACK);
		tv_self.setTextColor(Color.BLACK);

		exploreLayout.setSelected(true);
		ivExplore.setSelected(true);

		libraryLayout.setSelected(false);
		ivLibrary.setSelected(false);

		selfLayout.setSelected(false);
		ivSelf.setSelected(false);
		lastfragment = exploreFragment;
	}

	@Override
	public void onBackPressed()
	{
		showPop();
	}

	private void clickLibrary()
	{
		if (libraryFragment ==null) {
			libraryFragment = new LibraryFragment();
		}
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		//隐藏上一个fragment
		fragmentTransaction.hide(lastfragment);
		//如果是第一次点击，添加此fragment，若否，则从隐藏中显示
		if (libraryFirst) {
			fragmentTransaction.add(R.id.fl_content, libraryFragment);
			libraryFirst = false;
		}else{
			fragmentTransaction.show(libraryFragment);
		}
		fragmentTransaction.commit();
		indexLayout.setSelected(false);
		ivIndex.setSelected(false);
		tv_library.setTextColor(getResources().getColor(R.color.link_text_color));
		tv_explore.setTextColor(Color.BLACK);
		tv_index.setTextColor(Color.BLACK);
		tv_self.setTextColor(Color.BLACK);

		exploreLayout.setSelected(false);
		ivExplore.setSelected(false);

		libraryLayout.setSelected(true);
		ivLibrary.setSelected(true);

		selfLayout.setSelected(false);
		ivSelf.setSelected(false);
		lastfragment = libraryFragment;


	}

	public void clickSelf()
	{
		if (moreFragment == null) {
			moreFragment = new MoreFragment();
		}
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		//隐藏上一个fragment
		fragmentTransaction.hide(lastfragment);
		//如果是第一次点击，添加此fragment，若否，则从隐藏中显示
		if (selfFirst) {
			fragmentTransaction.add(R.id.fl_content, moreFragment,"selfFragment");
			selfFirst = false;
		}else{
			fragmentTransaction.show(moreFragment);
			setDataChange();
		}
		fragmentTransaction.commit();
		indexLayout.setSelected(false);
		ivIndex.setSelected(false);
		tv_self.setTextColor(getResources().getColor(R.color.link_text_color));
		tv_explore.setTextColor(Color.BLACK);
		tv_library.setTextColor(Color.BLACK);
		tv_index.setTextColor(Color.BLACK);

		exploreLayout.setSelected(false);
		ivExplore.setSelected(false);

		libraryLayout.setSelected(false);
		ivLibrary.setSelected(false);

		selfLayout.setSelected(true);
		ivSelf.setSelected(true);
		lastfragment = moreFragment ;

	}
	public void setDataChange() {
		if (moreFragment!=null) {
			moreFragment.setPortrait();
			moreFragment.queryFriendFollowFans();
			moreFragment.queryMineCount();
		}
	}

	public static MainActivity getMainActivity(){
		return instance;

	}

	public void skipToSelfFragment(){
		if (moreFragment==null) {
			moreFragment = new MoreFragment();
		}
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		//		fragmentTransaction.replace(R.id.fl_content, selfFragment,"selfFragment");
		//隐藏上一个fragment
		fragmentTransaction.hide(lastfragment);
		//如果是第一次点击，添加此fragment，若否，则从隐藏中显示
		if (selfFirst) {
			fragmentTransaction.add(R.id.fl_content, moreFragment,"selfFragment");
			selfFirst = false;
		}else{
			fragmentTransaction.show(moreFragment);
		}
		fragmentTransaction.commit();
		indexLayout.setSelected(false);
		ivIndex.setSelected(false);

		exploreLayout.setSelected(false);
		ivExplore.setSelected(false);

		libraryLayout.setSelected(false);
		ivLibrary.setSelected(false);

		selfLayout.setSelected(true);
		ivSelf.setSelected(true);
		tv_self.setTextColor(getResources().getColor(R.color.link_text_color));
		tv_explore.setTextColor(Color.BLACK);
		tv_library.setTextColor(Color.BLACK);
		tv_index.setTextColor(Color.BLACK);
		lastfragment = moreFragment;
		Clickposition =3;
	}

//	protected void onNewIntent(Intent intent) {
//		//		String url =intent.getStringExtra("bg");
//		//		BitmapUtils bitmapUtils = new BitmapUtils(this);
//		//		bitmapUtils.display(getSupportFragmentManager().findFragmentByTag("selfFragment")
//		//				.getView().findViewById(R.id.iv_self_user_background), url);
//		SelfFragment selffragment = (SelfFragment) getSupportFragmentManager().findFragmentByTag("selfFragment");
//		selffragment.getUserHpInfo(Constant.userInfo.getUserSeqId());
//		try {
//			selffragment.initViews(selffragment.userHomePageInfo);
//		} catch (DbException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		super.onNewIntent(intent);
//	}
	class TimeCount extends CountDownTimer
	{
		public TimeCount(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish()
		{// 计时完毕时触发
			count = 0;  
			firClick = 0;  
			secClick = 0;  
		}

		@Override
		public void onTick(long millisUntilFinished)
		{// 计时过程显示

		}
	}

	public static void delete(File file) {  
		if (file.isFile()) {  
			file.delete();  
			return;  
		}  
		//如果是文件夹
		if(file.isDirectory()){  
			File[] childFiles = file.listFiles();  
			if (childFiles == null || childFiles.length == 0) {  
				file.delete();  
				return;  
			}  
			//循环删除里面的子文件
			for (int i = 0; i < childFiles.length; i++) {  
				delete(childFiles[i]);  
			}  
			//最后删除文件夹
			file.delete();  
		}  
	}
	/**显示sendWindow**/
	private void showPop(){
		//		sendWindow = new PostSendPopup(instance, sendPostClick);

		List<PopupItem> pilist = new ArrayList<PopupItem>();
		PopupItem pi1 = new PopupItem();
		pi1.setItemname("退出");//设置名称
		pi1.setColor(this.getResources().getColor(R.color.search_bg));
		pi1.setiPopupItemCallback(new IPopupItemCallback() {  //设置click动作
			@Override
			public void itemClickFunc(PopupWindow popupWindow) {
				// 设置登出状态
				perferenceUtil.setBooleanValue(Constant.LOGIN_STATE, false);
				perferenceUtil.setStringValue(Constant.USER_SEQ_ID, "");
				// 点击“确认”后的操作
				//获得临时文件夹路径
				String path = Environment.getExternalStorageDirectory().toString() + "/" +"tempimage";
				//				Log.i("path", path);
				File path1 = new File(path);
				delete(path1);
				System.exit(0);
			}
		});
		pilist.add(pi1);
		new ListPopup(instance,pilist,rl_main);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(isSkipToSelf){
			skipToSelfFragment();
			isSkipToSelf = false;
		}
		super.onResume();
	}
	int count = 0;
	long firClick = 0,secClick=0;
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if(MotionEvent.ACTION_DOWN == event.getAction()){
			indexLayout.setBackgroundResource(R.color.pop_pressed);
			if (Clickposition!= 0) {
				clickIndex();
				Clickposition =0;
				count = 0;
				firClick = 0;secClick=0;
			}else if(Clickposition==0){
				count++;
//				Log.i("time", ""+count);
				TimeCount time = new TimeCount(600, 100);
				if(count == 1){  
					firClick = System.currentTimeMillis();
					time.start();

				}else if (count == 2){  
					secClick = System.currentTimeMillis();  
					if(secClick - firClick < 500){  
						//双击事件  
						//						ToastUtils.showMessage(instance, "双击");
						indexFragment.mPullRefreshListView.setRefreshing();
						indexFragment.getNewPosts(Constant.userInfo.getUserSeqId(), 
								indexFragment.mMaxPostId, true,
								Constant.POST_BEGIN_NUM, Constant.POST_END_NUM);
						time.cancel();
					}  
					count = 0;  
					firClick = 0;  
					secClick = 0;  
				}  
			}  
		}else if (MotionEvent.ACTION_UP == event.getAction()) {
			indexLayout.setBackgroundResource(R.color.white);
		}
		return true;
	}
//	public void onEventMainThread(RemindEvent event){
//		SharedPreferences remind = getSharedPreferences("remind", Context.MODE_PRIVATE);
//		Editor editor = remind.edit();
//		editor.putString("UserID", Constant.userInfo.getUserSeqId());
//		switch (event.getRemindMessage()) {
//		//有新消息
//		case 1: selfFragment.iv_remind_message.setVisibility(View.VISIBLE);
//		selfFragment.haveMessage =true;
//		editor.putBoolean("haveMessage", selfFragment.haveMessage);
//		break;
//		case 11:selfFragment.iv_remind_message.setVisibility(View.GONE);
//		selfFragment.haveMessage = false;
//		editor.putBoolean("haveMessage", selfFragment.haveMessage);
//		break;
//		//有新好友
//		case 2:selfFragment.iv_remind_friend.setVisibility(View.VISIBLE);
//		selfFragment.haveFriend =true;
//		editor.putBoolean("haveFirend", selfFragment.haveFriend);
//		break;
//		//好友信息处理完毕
//		case 21:selfFragment.iv_remind_friend.setVisibility(View.GONE);
//		selfFragment.haveFriend = false;
//		editor.putBoolean("haveFirend", selfFragment.haveFriend);
//		break;
//		//有新圈子
//		case 3:selfFragment.iv_remind_circle.setVisibility(View.VISIBLE);
//		selfFragment.haveCircle =true;
//		editor.putBoolean("haveCircle", selfFragment.haveCircle);
//		break;
//		case 31:selfFragment.iv_remind_circle.setVisibility(View.GONE);
//		selfFragment.haveCircle = false;
//		editor.putBoolean("haveCircle", selfFragment.haveCircle);
//		break;
//		case 4:iv_remind_self.setVisibility(View.VISIBLE);break;
//		case 9:Log.i("push", "event接受全员推送");
//			ToastUtils.showMessage(instance, event.getRemindString());break;
//		}
//		if (selfFragment.haveMessage||selfFragment.haveFriend||selfFragment.haveCircle) {
//			iv_remind_self.setVisibility(View.VISIBLE);
//		}else{
//			if (First) {
//				First = false;
//			}else{
//				iv_remind_self.setVisibility(View.GONE);
//			}
//		}
		//写入持久化
//		Log.i("TAG", "state"+":"+selfFragment.haveMessage+"+"+selfFragment.haveFriend
//				+"+"+selfFragment.haveCircle);
//		editor.commit();
		
//		Log.i("TAG", "remind"+":"+remind.getBoolean("haveMessage", false)+"+"
//				+remind.getBoolean("haveFirend", false)
//				+"+"+remind.getBoolean("haveCircle", false));
//	}
	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	//检查持久化存储
	public void fragment2getSharedPreference() {
		SharedPreferences remind = getSharedPreferences("remind", Context.MODE_PRIVATE);
		String userid = remind.getString("UserID", "newUser");
		if(userid.equals("newUser")){
//			Log.i("TAG", "新用户");
		}else if(userid.equals(Constant.userInfo.getUserSeqId())){
			boolean havemessage  = remind.getBoolean("haveMessage", false);
			if (havemessage) {
				EventBus.getDefault().post(new RemindEvent(1));
			}
			boolean havefriend  = remind.getBoolean("haveFriend", false);
			if (havefriend) {
				EventBus.getDefault().post(new RemindEvent(2));
			}
			boolean havecricle = remind.getBoolean("haveCircle", false);
			if (havecricle) {
				EventBus.getDefault().post(new RemindEvent(3));
//				Log.i("TAG", "post");
			}
//			Log.i("TAG", havemessage+"+"+havefriend+"+"+havecricle+"检查完毕");
		}else{
//			Log.i("TAG", "更换了用户");
			SharePerferenceUtil shareUtilCase = SharePerferenceUtil.getInstance(instance,Constant.ISAVECASE);
			SharePerferenceUtil shareUtilPuzz = SharePerferenceUtil.getInstance(instance,Constant.ISAVEPUZZ);
			shareUtilCase.setBooleanValue(Constant.ISAVECASE, false);
			shareUtilPuzz.setBooleanValue(Constant.ISAVEPUZZ, false);
			shareUtilCase.setStringValue(Constant.SAVECASE, "");
			shareUtilPuzz.setStringValue(Constant.SAVEPUZZ, "");
		}

	}

}
