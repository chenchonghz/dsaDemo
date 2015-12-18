package com.szrjk.dhome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.adapter.FolderAdapter;
import com.szrjk.config.PhotoConstant;

@ContentView(R.layout.activity_image_file)
public class ImageFileActivity extends Activity
{
	private FolderAdapter folderAdapter;
	@ViewInject(R.id.btn_cancel)
	private Button bt_cancel;
	@ViewInject(R.id.gl_list)
	private GridView gl_list;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	private ImageFileActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance  = this;
		ViewUtils.inject(this);
		folderAdapter = new FolderAdapter(this);
		gl_list.setAdapter(folderAdapter);
	}

	@OnClick(R.id.btn_cancel)
	public void cancelClick(View v)
	{
		// 清空选择的图片
		PhotoConstant.postItems.clear();
		Intent intent = new Intent();
		intent.setClass(instance, MainActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent intent = new Intent();
			intent.setClass(instance, MainActivity.class);
			startActivity(intent);
		}
		return true;
	}

}
