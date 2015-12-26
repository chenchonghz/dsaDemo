package com.szrjk.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.szrjk.dhome.R;
import com.szrjk.dhome.SelfActivity;

public class NotificationUtil {

	/**
	 * 
	 * @param context 当前界面context
	 * @param cls 传入需要跳转的Activity
	 * @param NotificationTitle 通知标题
	 * @param NotificationText	通知正文
	 * @param NotificationsmallIcon_resID 通知图标
	 * @param IsGoing	true：通知出现在"正在进行"栏，false:通知出现在"通知"栏
	 * @param autocancel	true:通知点击消失
	 * @param soundOpen	true:开启声音
	 */
	public static void showNotification(Context context,Class<?> cls,String NotificationTitle,String NotificationText,
			int NotificationsmallIcon_resID,boolean IsGoing,boolean autocancel,boolean soundOpen){
		//1.得到NotificationManager  
		NotificationManager nm=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); 
		//设置通知栏跳转目标
		Intent intent = new Intent(context, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pi= PendingIntent.getActivity(context, 0, intent, 0);  
		//用builder设置Notification
		Notification n = new Notification.Builder(context)
		.setContentTitle(NotificationTitle)
		.setContentText(NotificationText)
		.setSmallIcon(NotificationsmallIcon_resID)
		.build();
		n.contentIntent = pi;
		//		此条将通知放在”正在进行“栏，否则在通知栏
		if (IsGoing) {
			n.flags =Notification.FLAG_ONGOING_EVENT;
		}
		//单击通知，通知消失
		if (autocancel) {
			n.flags |= Notification.FLAG_AUTO_CANCEL;
		}
		//通知声音
		if (soundOpen) {
			n.defaults = Notification.DEFAULT_SOUND;   
		}
		//启用通知
		nm.notify(1, n); 
	}
}
