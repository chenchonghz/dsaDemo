package com.szrjk.message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.dhome.AddressBookActivity;
import com.szrjk.dhome.R;
/**
 * 
 * @author ldr
 * 2015-12-25 13:44:52
 * 聊天消息设置
 */
@ContentView(R.layout.activity_chat_settings)
public class ChatSettingsActivity extends Activity {

	@ViewInject(R.id.bt_chatclean)
	private Button bt_chatclean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		
		bt_chatclean.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				startActivity(new Intent(ChatSettingsActivity.this,AddressBookActivity.class));
				
			}
		});
	}


}
