package com.szrjk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.szrjk.util.NetworkUtils;

public class NetCheckReceiver extends BroadcastReceiver
{
	// android 中网络变化时所发的Intent的名字
	private static final String netACTION = "android.net.conn.CONNECTIVITY_CHANGE";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals(netACTION))
		{
			if (!NetworkUtils.isWifiConnected(context)
					&& !NetworkUtils.isMobileConnected(context))
			{
			}
			else
			{

			}
		}
	}
}
