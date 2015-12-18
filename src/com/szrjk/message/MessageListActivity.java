package com.szrjk.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.TMessage;
import com.szrjk.entity.UserCard;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.widget.HeaderView;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

@ContentView(R.layout.activity_message_list)
public class MessageListActivity extends Activity {
	@ViewInject(R.id.hv_message)
	private HeaderView hv_message;
	@ViewInject(R.id.lv_messagelist)
	private ListView lv_message;
	private MessageListActivity instance;
	private TimeCount time;
	private List<MessageListEntity> allmessage = new ArrayList<MessageListEntity>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(instance);
		getOldMessage();
		getNewMessage();
		initListener();
		
	}
	private void getOldMessage() {
		try {
			List<TMessage> oldList = new TMessage().getlist(instance,Constant.userInfo.getUserSeqId());
			if (oldList!=null&&!oldList.isEmpty()) {

				for(TMessage tm: oldList){
					MessageListEntity  mle = new MessageListEntity();
					UserCard uc = new UserCard();
					uc.setUserSeqId(tm.getSelfUserId());
					mle.setReceiveUserCard(uc);
					UserCard objuUc = new UserCard();
					objuUc.setUserSeqId(tm.getObjUserid());
					objuUc.setUserName(tm.getObjUsername());
					objuUc.setCompanyName(tm.getObjUserHospital());
					objuUc.setDeptName(tm.getObjUserdept());
					objuUc.setProfessionalTitle(tm.getObjUserPtitle());
					objuUc.setUserFaceUrl(tm.getObjUserfaceurl());
					objuUc.setUserLevel(tm.getObjUserfacelevel());
					mle.setSendUserCard(objuUc);
					mle.setCreateDate(tm.getLasttime());
					allmessage.add(mle);
				}
			}
			//			Log.i("TAG", oldList.size()+"和"+allmessage.size());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	//设置监听
	private void initListener() {
		LinearLayout lly = hv_message.getLLy();
		lly.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				instance.finish();
				time.cancel();				
			}
		});
		lv_message.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ImageView unread = (ImageView) arg1.findViewById(R.id.iv_unread);
				unread.setVisibility(View.GONE);
				Intent intent = new Intent(instance, MessageActivity.class);
				intent.putExtra("otherusercard",allmessage.get(arg2).getSendUserCard());
				startActivity(intent);
				
			}
		});

	}
	private void getNewMessage() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryCmChatUnread");
		Map<String,Object> busiParam = new HashMap<String, Object>();
		busiParam.put("receiveUserId", Constant.userInfo.getUserSeqId());
		busiParam.put("basePkId", "0");
		busiParam.put("chatType","1");
		paramMap.put("BusiParams", busiParam);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {

			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					List<MessageListEntity> list = JSON.parseArray(
							returnObj.getString("ListOut"), MessageListEntity.class);
					try {
						new TMessage().addmessage(list);
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					for (int i = 0; i < list.size(); i++) {
						int a = 0;
						for (int j = 0; j <allmessage.size(); j++) {
							if (allmessage.get(j).getSendUserCard().getUserSeqId()
									.equals(list.get(i).getSendUserCard().getUserSeqId())) 
							{
								allmessage.remove(j);
								allmessage.add(0,list.get(i));
								a=1;
								break;
							}
						}
						if (a==0) {
							allmessage.add(0, list.get(i));
						}
					}

					MessageListAdapter adapter = new MessageListAdapter(instance, allmessage,list.size());
					lv_message.setAdapter(adapter);
					time = new TimeCount(10000, 1000);
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
			}
		});

	}
	class TimeCount extends CountDownTimer
	{
		public TimeCount(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish()
		{
			allmessage = new ArrayList<MessageListEntity>();
			getOldMessage();
			getNewMessage();
		}

		@Override
		public void onTick(long millisUntilFinished)
		{// 计时过程显示
		}
	}
	@Override
	protected void onDestroy() {
		time.cancel();
		finish();
		super.onDestroy();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			instance.finish();
			time.cancel();
		}
		return false;
	}
}
