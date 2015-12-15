package com.szrjk.self.more;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;

@ContentView(R.layout.activity_mine_get_like)
public class MineGetLikeActivity extends BaseActivity{
	
	@ViewInject(R.id.iv_back)
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
	}
	
	@OnClick(R.id.iv_back)
	public void backClick(View view){
		finish();
	}
}
