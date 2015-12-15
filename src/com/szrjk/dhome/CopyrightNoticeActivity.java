package com.szrjk.dhome;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.activity_copyright_notice)
public class CopyrightNoticeActivity extends BaseActivity{
	
	@ViewInject(R.id.iv_back)
	private ImageView iv_back;
	
	private CopyrightNoticeActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance=this;
	}
	
	@OnClick(R.id.iv_back)
	public void backClick(View view){
		instance.finish();
	}
	
}
