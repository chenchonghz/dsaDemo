package com.szrjk.message;

import android.app.Activity;
import android.os.Bundle;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.szrjk.dhome.R;
/**
 * 
 * @author ldr
 * 2015-12-25 13:44:52
 * 聊天消息设置
 */
@ContentView(R.layout.activity_chat_settings)
public class ChatSettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
	}


}
