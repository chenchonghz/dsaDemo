package com.szrjk.message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.TMessage;
import com.szrjk.entity.UserCard;
import com.szrjk.entity.UserHomePageInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.pull.PullToRefreshBase;
import com.szrjk.pull.PullToRefreshBase.Mode;
import com.szrjk.pull.PullToRefreshBase.OnRefreshListener;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.HeaderView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.content.Intent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
@ContentView(R.layout.activity_message)
public class MessageActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.rly_message)
	private View messageView;
	@ViewInject(R.id.hv_message)
	private HeaderView hv_message;
	@ViewInject(R.id.lv_message)
	private PullToRefreshListView mPullToRefreshListView;
	@ViewInject(R.id.iv_sendImage)
	private ImageView iv_sendImage;
	@ViewInject(R.id.et_talk)
	private EditText et_talk;
	@ViewInject(R.id.iv_send)
	private ImageView iv_send;
	private MessageActivity instance;
	private ListView list_message;
	private UserCard selfUserCard = new UserCard(); 
	private UserCard objUserCard ;
	private List<MessageEntity> messages = new ArrayList<MessageEntity>();
	private MessageAdapter adapter ;
	private int BeginNum=0;
	private int EndNum=19;
	private int y;
	private TimeCount time;
	private int screenHeight = 0;
	private int keyHeight = 0;
	private String chattext;
	private long lastreflashtime=0;
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance =this;
		//		time = new TimeCount(25000, 25000);
		time = new TimeCount(25000, 10000);
		ViewUtils.inject(instance);
		mPullToRefreshListView.setMode(Mode.PULL_FROM_START);
		list_message = mPullToRefreshListView.getRefreshableView();
		hv_message.showImageLLy(R.drawable.icon_messageset_40, new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(instance, ChatSettingsActivity.class);
				intent.putExtra(Constant.USER_INFO, selfUserCard);
				startActivity(intent);
			}
		});
		//获取usercard
		Intent intent = getIntent();
		objUserCard = (UserCard) intent.getSerializableExtra("otherusercard");
		//设一个假的usercard
		if (objUserCard ==null) {
			//			setUsercard();
			ToastUtils.showMessage(instance, "此用户不存在");
		}
		//		Log.i("TAG", objUserCard.toString());
		hv_message.setHtext(objUserCard.getUserName());
		try {
			getSelfUserCard();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		//获取消息列表
		//		DisplayMetrics dm = new DisplayMetrics();
		//		getWindowManager().getDefaultDisplay().getMetrics(dm);
		//		screenHeight = dm.heightPixels;
		//		keyHeight = screenHeight/3;
		//		initListener();
		//发送消息
		//		sendMessage();
	}
	//请求获得自己的usercard
	private void getSelfUserCard() {
		final UserCard usercard = new UserCard();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryUserHomePage");
		Map<String,Object> busiParam = new HashMap<String, Object>();
		busiParam.put("userId", Constant.userInfo.getUserSeqId());
		busiParam.put("objUserId", Constant.userInfo.getUserSeqId());
		paramMap.put("BusiParams", busiParam);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj  =JSON.parseObject(
						jsonObject.getString("ErrorInfo"),ErrorInfo.class);
				if(Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					JSONObject resObj = returnObj
							.getJSONObject("ListOut");
					UserHomePageInfo userHomePageInfo = JSON.parseObject(
							resObj.toJSONString(),UserHomePageInfo.class) ;
					//数据导入
					selfUserCard.setUserSeqId(userHomePageInfo.getUserSeqId());
					selfUserCard.setUserName(userHomePageInfo.getUserName());
					selfUserCard.setUserFaceUrl(userHomePageInfo.getUserFaceUrl());
					selfUserCard.setCompanyName(userHomePageInfo.getCompanyName());
					selfUserCard.setDeptName(userHomePageInfo.getDeptName());
					selfUserCard.setProfessionalTitle(userHomePageInfo.getProfessionalTitle());
					selfUserCard.setUserLevel(userHomePageInfo.getUserLevel());
					selfUserCard.setUserType(userHomePageInfo.getUserType());
					Log.i("http", selfUserCard.toString());

					getMessage();
					DisplayMetrics dm = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(dm);
					screenHeight = dm.heightPixels;
					keyHeight = screenHeight/3;
					initListener();
				}

			}
			@Override
			public void start() {
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {
			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
			}
		});


	}

	private void initListener() {
		LinearLayout lly = hv_message.getLLy();
		lly.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				instance.finish();
				time.cancel();
			}
		});
		iv_sendImage.setOnClickListener(instance);
		iv_send.setOnClickListener(instance);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			//当两次刷新间隔小于5秒时，走一个假刷新界面
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date currentTime = null;
				try {
					currentTime = df.parse(df.format(new Date()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (lastreflashtime==0||currentTime.getTime()-lastreflashtime>5000) {
					time.cancel();
					getMessage();
					list_message.setSelectionFromTop(20,0);
					lastreflashtime = currentTime.getTime();

				}else{
//					ToastUtils.showMessage(instance, "歇一会吧");
					mPullToRefreshListView.postDelayed(new Runnable() {
						public void run() {
							   mPullToRefreshListView.onRefreshComplete();
						}
					}, 1000);

				}

			}
		});
		messageView.addOnLayoutChangeListener(new OnLayoutChangeListener() {

			@Override
			public void onLayoutChange(View v, int left, int top, int right,  
					int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
				if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){  
					//					ToastUtils.showMessage(instance, "弹出了");	
					list_message.post(new Runnable() {
						@Override
						public void run() {
							// Select the last row so it will scroll into view...
							
							if (adapter!=null&&adapter.getCount()!=0) {
								list_message.setSelectionFromTop(adapter.getCount()-1,0);
							}
						}
					});

				}else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){  

				}  

			}
		});

	}

	private void getMessage() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryCmChatHis");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userId",selfUserCard.getUserSeqId());
		busiParams.put("objUserId",objUserCard.getUserSeqId());
		busiParams.put("basePkId","0");
		busiParams.put("isNew","true");
		busiParams.put("startNum",String.valueOf(BeginNum));
		busiParams.put("endNum",String.valueOf(EndNum));
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {

			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					List<MessageEntity> list = JSON.parseArray(
							returnObj.getString("ListOut"), MessageEntity.class);
					//将第一条消息的数据放入数据库
					try {
						if (list.size()!=0) {
							//当发送者是自己时
							if (list.get(0).getSendUserCard().getUserSeqId().equals(Constant.userInfo.getUserSeqId())) {
								new TMessage().addmessage(list.get(0).getReceiveUserCard(),list.get(0).getCreateDate());
							}else{
								new TMessage().addmessage(list.get(0).getSendUserCard(),list.get(0).getCreateDate());
							}
							for(MessageEntity message : list){
								messages.add(0, message);
							}
							Log.i("TAG",messages.toString());
							BeginNum+=20;
							EndNum+=20;
							setAdapter();
						}else{
							ToastUtils.showMessage(instance, "没有更多消息了");
						}
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (mPullToRefreshListView.isRefreshing()) {
						mPullToRefreshListView.onRefreshComplete();
					}

					//					time = new TimeCount(10000, 1000);// 构造CountDownTimer对象
					time.start();
				}
			}

			@Override
			public void start() {

			}

			@Override
			public void loading(long total, long current, boolean isUploading) {

			}

			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				if (mPullToRefreshListView.isRefreshing()) {
					mPullToRefreshListView.onRefreshComplete();
				}
				time.start();
			}
		});
	}

	protected void setAdapter() {
		adapter = new MessageAdapter(instance, messages, selfUserCard, objUserCard);
		//		adapter.notifyDataSetChanged();
		list_message.setAdapter(adapter);
		//		list_message.setSelection(list_message.getBottom());
		list_message.setSelectionFromTop(messages.size(),0);

	}

	private void sendMessage() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "sendChatMessage");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		//当前用户的usercard
		Map<String, String> sendUserCard = new HashMap<String, String>();
		sendUserCard.put("deptName", selfUserCard.getDeptName());
		sendUserCard.put("userSeqId", selfUserCard.getUserSeqId());
		sendUserCard.put("userFaceUrl", selfUserCard.getUserFaceUrl());
		sendUserCard.put("userLevel", selfUserCard.getUserLevel());
		sendUserCard.put("professionalTitle", selfUserCard.getProfessionalTitle());
		sendUserCard.put("userName", selfUserCard.getUserName());
		sendUserCard.put("companyName", selfUserCard.getCompanyName());
		sendUserCard.put("userType", selfUserCard.getUserType());
		busiParams.put("sendUserCard",sendUserCard);
		//目标用户的usercard;
		Map<String, String> receiveUserCard = new HashMap<String,String>();
		receiveUserCard.put("deptName", objUserCard.getDeptName());
		receiveUserCard.put("userSeqId", objUserCard.getUserSeqId());
		receiveUserCard.put("userFaceUrl", objUserCard.getUserFaceUrl());
		receiveUserCard.put("userLevel", objUserCard.getUserLevel());
		receiveUserCard.put("professionalTitle", objUserCard.getProfessionalTitle());
		receiveUserCard.put("userName", objUserCard.getUserName());
		receiveUserCard.put("companyName", objUserCard.getCompanyName());
		receiveUserCard.put("userType", objUserCard.getUserType());
		busiParams.put("receiveUserCard",receiveUserCard);
		//聊天文字
		busiParams.put("content", chattext);
		busiParams.put("chatType", "1");
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{ 

					//					getMessage();
					MessageEntity TempMe = new MessageEntity();
					TempMe.setSendUserCard(selfUserCard);
					TempMe.setReceiveUserCard(objUserCard);
					TempMe.setContent(chattext);
					try {
						//将最新信息放进数据库
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String date =df.format(new Date());
						new TMessage().addmessage(objUserCard, date);
						TempMe.setCreateDate(date);
					} catch (DbException e) {
						e.printStackTrace();
					}
					//					messages.add(TempMe);
					//					adapter.notifyDataSetChanged();

				}
			}

			@Override
			public void start() {

			}

			@Override
			public void loading(long total, long current, boolean isUploading) {

			}

			@Override
			public void failure(HttpException exception, JSONObject jobj) {

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_sendImage:
			ToastUtils.showMessage(instance, "敬请期待");
			break;

			//发送按钮逻辑
		case R.id.iv_send:
			if (TextUtils.isEmpty(et_talk.getText()) ) {
				et_talk.setError("不能发空消息");
			}else{
				chattext = et_talk.getText().toString();
				MessageEntity TempMe = new MessageEntity();
				TempMe.setSendUserCard(selfUserCard);
				TempMe.setReceiveUserCard(objUserCard);
				TempMe.setContent(chattext);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date =df.format(new Date());
				TempMe.setCreateDate(date);
				messages.add(TempMe);
				if (adapter==null) {
					setAdapter();
				}else{
					adapter.notifyDataSetChanged();
				}
				et_talk.setText("");
				sendMessage();
				time.cancel();
				//			time = new TimeCount(10000, 1000);
				time.start();
			}
			break;
		}
	}
	class TimeCount extends CountDownTimer
	{
		public TimeCount(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish()
		{// 计时完毕时触发
			BeginNum =0;
			EndNum =19;
			messages.clear();

			getMessage();
		}

		@Override
		public void onTick(long millisUntilFinished)
		{// 计时过程显示
		}

	}
	//	@Override
	//	protected void onDestroy() {
	//		time.onFinish();
	//		finish();
	//		super.onDestroy();
	//	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			instance.finish();
			time.cancel();
		}
		return false;
	}
	//	@Override
	//	protected void onPause() {
	//		time.onFinish();
	//		Log.i("tag", "pause");
	//		super.onPause();
	//	}
	//	@Override
	//	protected void onStop() {
	//		time.onFinish();
	//		Log.i("tag", "stop");
	//		super.onStop();
	//	};
	//	protected void onResume() {
	//		time = new TimeCount(10000, 1000);
	//		time.start();
	//		super.onResume();
	//	}
}
