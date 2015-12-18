package com.szrjk.self;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.UserBackgroundGridViewAdapter;
import com.szrjk.dhome.R;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.app.Activity;
import android.content.Intent;
/**
 * 用户背景墙选择界面（小图浏览）
 * @author 郑斯铭
 *
 */

@ContentView(R.layout.activity_user_background_select)
public class UserBackgroundSelectActivity extends Activity {
@ViewInject(R.id.gv_user_bg_select)
private GridView gv_user_bg_select;
private String[]img_bg={
		"http://dd-feed.oss-cn-shenzhen.aliyuncs.com/wall/b2e72544322a8cae.png",
		"http://dd-feed.oss-cn-shenzhen.aliyuncs.com/wall/16670f07c7324083.png",
		"http://dd-feed.oss-cn-shenzhen.aliyuncs.com/wall/5b54ab23e6779ab5.png",
		"http://dd-feed.oss-cn-shenzhen.aliyuncs.com/wall/a9669b2ae9c908b9.png",
		"http://dd-feed.oss-cn-shenzhen.aliyuncs.com/wall/7a395de6504ed23e.png",
		"http://dd-feed.oss-cn-shenzhen.aliyuncs.com/wall/26d380b15264bd18.png",
		"http://dd-feed.oss-cn-shenzhen.aliyuncs.com/wall/60e46fd0530d72fe.png",
		"http://dd-feed.oss-cn-shenzhen.aliyuncs.com/wall/d34ddd1ca73b6b32.png",
		"http://dd-feed.oss-cn-shenzhen.aliyuncs.com/wall/bf0fe5982e66407a.png"
};



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		UserBackgroundGridViewAdapter adapter = 
				new UserBackgroundGridViewAdapter(this.getApplication(), img_bg);
		gv_user_bg_select.setAdapter(adapter);
		gv_user_bg_select.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(UserBackgroundSelectActivity.this,UserBackgroundShower.class);
				intent.putExtra("position", position);
				intent.putExtra("imgs", img_bg);
				intent.putExtra("title", (position+1)+"/"+img_bg.length);
				startActivity(intent);
				
			}
		});
	}

	
}
