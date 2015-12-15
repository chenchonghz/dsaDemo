package com.szrjk.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.szrjk.entity.MessageItem;
import com.szrjk.entity.MsgItem;
import com.szrjk.entity.RemindEvent;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;
import de.greenrobot.event.EventBus;
import org.android.agoo.client.BaseConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class DHomePushService extends UmengBaseIntentService
{
	@Override
	protected void onMessage(Context context, Intent intent)
	{
		super.onMessage(context, intent);
		String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
		try
		{
			Log.i("message", message);
			UMessage msg = new UMessage(new JSONObject(message));
			Log.i("msg", message.toString());
			Log.i("msg.custom", msg.custom);
			String s = msg.custom;
			Log.i("s", s);
			MsgItem messageItem =JSON.parseObject(s, MsgItem.class);

			Log.e("messageItem", JSON.toJSONString(messageItem));
			//{"message":"用户申请加入圈子","id":3,"declaringClass":"com.szjk.dhome.type.PushMessageType","alert":"用户申请加入圈子"}
			//{"message":"用户加关注","id":1,"declaringClass":"com.szjk.dhome.type.PushMessageType","alert":"用户加关注"}//



//			int type = messageItem.getType();
			int type =messageItem.getId();
			switch (type)
			{
				case 0://
					//setMsgTips(Constant.SYS_MSG);
					break;
				case 1://用户加关注
					//setMsgTips(Constant.FOCUS_MSG);

					break;
				case 2:// 加好友
//					setMsgTips(Constant.CIRCLE_MSG);
					EventBus.getDefault().post(new RemindEvent(2));
					break;
				case 3:// 用户申请加入圈子
//					setMsgTips(Constant.FRIEND_MSG);
					EventBus.getDefault().post(new RemindEvent(3));
					break;
				case 4:// 邀请用户加入圈子
					//setMsgTips(Constant.FORWARD_MSG);
					EventBus.getDefault().post(new RemindEvent(3));
					break;
				case 5:// 发私信
					//setMsgTips(Constant.COMMENT_MSG);
					EventBus.getDefault().post(new RemindEvent(1));
					break;
				case 6://
					//setMsgTips(Constant.PRAISE_MSG);
					break;
				case 7://
					//setMsgTips(Constant.CALENDAR_MSG);
					break;
				case 8://
					//setMsgTips(Constant.ATTENTION_MSG);
					break;
				case 9://
//					setMsgTips(Constant.PERSON_MSG);
					break;
				case 10://
//					setMsgTips(Constant.INVITE_MSG);
					break;
			}

		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 *
	 * 
	 * @param msgType
	 */
//	private void setMsgTips(String msgType)
//	{
//		SharePerferenceUtil sharePerferenceUtil = SharePerferenceUtil
//				.getInstance(getApplicationContext(), Constant.MESSAGE_INFO);
//		int msgCount = sharePerferenceUtil.getIntValue(msgType, 0);
//		sharePerferenceUtil.setIntValue(msgType, ++msgCount);
//	}
}
