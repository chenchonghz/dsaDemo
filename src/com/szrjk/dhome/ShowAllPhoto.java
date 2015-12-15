package com.szrjk.dhome;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.adapter.AlbumGridViewAdapter;
import com.szrjk.config.Constant;
import com.szrjk.config.PhotoConstant;
import com.szrjk.util.ImageItem;

@ContentView(R.layout.activity_show_photo)
public class ShowAllPhoto extends BaseActivity
{
	@ViewInject(R.id.gl_list)
	private GridView gl_list;
	@ViewInject(R.id.pb_photo)
	private ProgressBar pb_photo;
	private AlbumGridViewAdapter gridImageAdapter;
	@ViewInject(R.id.btn_submit)
	private Button btn_submit;
	@ViewInject(R.id.btn_preview)
	private Button btn_preview;
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	@ViewInject(R.id.btn_cancel)
	private Button btn_cancel;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	private Intent intent;
	private Resources resources;
	private ShowAllPhoto instance;
	public static ArrayList<ImageItem> dataList = new ArrayList<ImageItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance = this;
		this.intent = getIntent();
		String folderName = intent.getStringExtra("folderName");
		if (folderName.length() > 8)
		{
			folderName = folderName.substring(0, 9) + "...";
		}
		tv_title.setText(folderName);
		init();
		initListener();
		isShowOkBt();
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			gridImageAdapter.notifyDataSetChanged();
		}
	};

	@OnClick(R.id.btn_preview)
	public void btn_previewClick(View v)
	{
//		if (PhotoConstant.postItems.size() > 0)
//		{
//			Bundle bundle = new Bundle();
//			bundle.putInt("position", 2);
//			intent.putExtras(bundle);
//			intent.setClass(instance, GalleryActivity.class);
//			startActivity(intent);
//		}
		showToast(instance, "开发中", 1);
	}

	@OnClick(R.id.btn_back)
	public void backClick(View v)
	{
		intent.setClass(instance, ImageFileActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.btn_cancel)
	public void cancelClick(View v)
	{
		// 清空选择的图片
		PhotoConstant.postItems.clear();
		intent.setClass(instance, MainActivity.class);
		startActivity(intent);
	}

	private void init()
	{
		resources = getResources();
		IntentFilter filter = new IntentFilter("data.broadcast.action");
		registerReceiver(broadcastReceiver, filter);
		pb_photo.setVisibility(View.GONE);
		gridImageAdapter = new AlbumGridViewAdapter(this, dataList,
				PhotoConstant.postItems);
		gl_list.setAdapter(gridImageAdapter);
	}

	private void initListener()
	{

		gridImageAdapter
				.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener()
				{
					public void onItemClick(final ToggleButton toggleButton,
							int position, boolean isChecked, CheckBox button)
					{
						if (PhotoConstant.postItems.size() >= Constant.maxCount
								&& isChecked)
						{
							button.setVisibility(View.GONE);
							toggleButton.setChecked(false);
							String strLimit = resources
									.getString(R.string.only_choose_num);
							createDialog(instance, strLimit);
							return;
						}

						if (isChecked)
						{
							button.setVisibility(View.VISIBLE);
							PhotoConstant.postItems.add(dataList.get(position));
							String strFinish = resources
									.getString(R.string.finish);
							btn_submit.setText(strFinish + "("
									+ PhotoConstant.postItems.size() + "/"
									+ Constant.maxCount + ")");
						}
						else
						{
							button.setVisibility(View.GONE);
							PhotoConstant.postItems.remove(dataList
									.get(position));
							String strFinish = resources
									.getString(R.string.finish);
							btn_submit.setText(strFinish + "("
									+ PhotoConstant.postItems.size() + "/"
									+ Constant.maxCount + ")");
						}
						isShowOkBt();
					}
				});

		btn_submit.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				btn_submit.setClickable(false);
				intent.setClass(instance, MainActivity.class);
				startActivity(intent);
				finish();

			}
		});

	}

	public void isShowOkBt()
	{
		if (PhotoConstant.postItems.size() > 0)
		{
			String strFinish = resources.getString(R.string.finish);
			btn_submit.setText(strFinish + "(" + PhotoConstant.postItems.size()
					+ "/" + Constant.maxCount + ")");
			btn_preview.setPressed(true);
			btn_submit.setPressed(true);
			btn_preview.setClickable(true);
			btn_submit.setClickable(true);
			btn_submit.setTextColor(Color.WHITE);
			btn_preview.setTextColor(Color.WHITE);
		}
		else
		{
			String strFinish = resources.getString(R.string.finish);
			btn_submit.setText(strFinish + "(" + PhotoConstant.postItems.size()
					+ "/" + Constant.maxCount + ")");
			btn_preview.setPressed(false);
			btn_preview.setClickable(false);
			btn_submit.setPressed(false);
			btn_submit.setClickable(false);
			btn_submit.setTextColor(Color.parseColor("#E1E0DE"));
			btn_preview.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			this.finish();
			intent.setClass(ShowAllPhoto.this, ImageFileActivity.class);
			startActivity(intent);
		}

		return false;

	}

	@Override
	protected void onRestart()
	{
		isShowOkBt();
		super.onRestart();
	}

}
