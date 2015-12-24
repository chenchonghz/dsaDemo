package com.szrjk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.szrjk.config.ConstantUser;
import com.szrjk.dhome.DHomeApplication;
import com.szrjk.dhome.MainActivity;
import com.szrjk.dhome.SelfActivity;

public class ChangePortraitBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String userfaceUrl = intent.getStringExtra("userfaceUrl");
		ConstantUser.getUserInfo().setUserFaceUrl(userfaceUrl);
		MainActivity mainActivity=((DHomeApplication) context
				.getApplicationContext()).mainActivity;
		mainActivity.setDataChange();
		SelfActivity selfActivity = ((DHomeApplication) context
				.getApplicationContext()).selfActivity;
		selfActivity.getUserHpInfo(ConstantUser.getUserInfo().getUserSeqId());
	}
}
