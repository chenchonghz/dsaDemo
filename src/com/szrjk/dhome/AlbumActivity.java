package com.szrjk.dhome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.adapter.AlbumGridViewAdapter;
import com.szrjk.config.Constant;
import com.szrjk.index.SendPostActivity;
import com.szrjk.util.AlbumHelper;
import com.szrjk.util.ImageBucket;
import com.szrjk.util.ImageItem;
/**
 * 
 * @author Administrator
 *这里的数据来源、从缩略图、相册、媒体数据库获得数据、具体获得。么看懂。╮(╯▽╰)╭   init（）；
 */
@ContentView(R.layout.activity_album)
public class AlbumActivity extends BaseActivity
{
	// 列表
	@ViewInject(R.id.gv_list)
	private GridView gv_list;
	// 无内容提示
	@ViewInject(R.id.tv_album)
	private TextView tv_album;
	private AlbumGridViewAdapter viewAdapter;//适配器
	// 完成
	@ViewInject(R.id.btn_submit)
	private Button btn_submit;
	// 取消
	@ViewInject(R.id.bt_back)
	private Button bt_back;
	private Intent intent;
	// 预览
	@ViewInject(R.id.btn_preview)
	private Button btn_preview;
	private AlbumActivity instance;
	private ArrayList<ImageItem> dataList;
	private AlbumHelper helper;
	public static List<ImageBucket> contentList;
	public static Bitmap bitmap;
	private Resources resources;
	private String strFinish;
	private Bundle bundle;
	List<ImageItem> imgItems = new ArrayList<ImageItem>();
	private int maxnum;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(instance);
		intent = getIntent();
		bundle = intent.getExtras();
		//最大值
		maxnum = bundle.getInt(Constant.IMGNUM);

		//		registerBroadCast();
		init();
		// 这个函数主要用来控制预览和完成按钮的状态
		//		isShowOkBt(); 超出可选图片张数

	}

	// 注册广播
	//	private void registerBroadCast()
	//	{
	//		IntentFilter filter = new IntentFilter("data.broadcast.action");
	//		registerReceiver(broadcastReceiver, filter);
	//	}
	//
	//	BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	//	{
	//		@Override
	//		public void onReceive(Context context, Intent intent)
	//		{
	//			viewAdapter.notifyDataSetChanged();
	//		}
	//	};

	@OnClick(R.id.btn_preview)
	public void previewClick(View v)
	{
		//		Bundle bundle = new Bundle();
		//		bundle.putInt(Constant.ACTIVITYENUM, ACTIVITY);
		//		intent.putExtras(bundle);
		//		intent.setClass(instance, GalleryActivity.class);
		//		if (imgItems.size() > 0)
		//		{
		//			startActivity(intent);
		//			finish();
		//		}
	}

	@OnClick(R.id.btn_submit)
	public void sendClick(View v)
	{
		//		jumpBackActivity();
	}

	private void jumpBackActivity()
	{
		System.out.println("点击完成");
		intent.setClass(instance, SendPostActivity.class);
		Bundle bundle = new Bundle();
		//			bundle.put(Constant.IMGLIST, imgItems);
		Parcelable[] imgitemArr =  imgItems.toArray(new Parcelable[imgItems.size()]);
		bundle.putParcelableArray(Constant.IMGLIST,imgitemArr);

		intent.putExtras(bundle);
		setResult(Activity.RESULT_OK, intent);

		Log.i("图库AlbumActivity", "----点击完成。回到帖子--");
		finish();
	}

	@OnClick(R.id.btn_cancel)
	public void cancelClick(View v)
	{
		jumpBackActivity();
	}

	// 初始化，给一些对象赋值
	private void init()
	{
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		resources = getResources();
		bitmap = BitmapFactory.decodeResource(resources,
				R.drawable.album_no_pictures);
		contentList = helper.getImagesBucketList(false);
		dataList = new ArrayList<ImageItem>();

		for (int i = 0; i < contentList.size(); i++)
		{
			dataList.addAll(contentList.get(i).imageList);
		}
		Collections.sort(dataList);
		Collections.reverse(dataList);

		viewAdapter = new AlbumGridViewAdapter(this, dataList,	imgItems);
		btn_submit.setText( imgItems.size()	+ "/" + maxnum );

		gv_list.setAdapter(viewAdapter);
		gv_list.setEmptyView(tv_album);
		strFinish = resources.getString(R.string.finish);
		viewAdapter
		.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener()
		{

			@Override
			public void onItemClick(final ToggleButton toggleButton,
					int position, boolean isChecked, CheckBox chooseBt)
			{
				//如果大于等于9、就设置不可再点击
				if (imgItems.size() >= maxnum)
				{
					//								toggleButton.setChecked(false);
					//								toggleButton.setVisibility(View.GONE);
					//这里处理。多于9张之后，再点击新的，就设置check = false 把刚刚点击的移除就可以了
					chooseBt.setChecked(false);
					if (!removeOneData(dataList.get(position)))	{
						String strLimit = resources.getString(R.string.only_choose_num);
						showToast(instance, strLimit,Toast.LENGTH_SHORT);
					}
					return;
				}
				if (isChecked)
				{
					//								chooseBt.setVisibility(View.VISIBLE);
					//								toggleButton.setVisibility(View.GONE);
					imgItems.add(dataList.get(position));
				}
				else
				{
					imgItems.remove(dataList.get(position));
					//								chooseBt.setVisibility(View.GONE);
					//								toggleButton.setVisibility(View.GONE);
				}
				btn_submit.setText( imgItems.size()	+ "/" + maxnum );

				//						isShowOkBt();
			}
		});
	}


	private boolean removeOneData(ImageItem imageItem)
	{
		if (imgItems.contains(imageItem))
		{
			imgItems.remove(imageItem);
			btn_submit.setText( imgItems.size()	+ "/" + maxnum );

			return true;
		}
		return false;
	}

	public void isShowOkBt()
	{
		if (imgItems.size() > 0)
		{
			selButton();
		}
		else
		{
			noSelButton();
		}
		btn_submit.setText( imgItems.size()
				+ "/" + maxnum );

	}

	private void noSelButton()
	{
		//		btn_preview.setPressed(false);
		//		btn_preview.setClickable(false);
		//		btn_submit.setPressed(false);
		//		btn_submit.setClickable(false);
		//		btn_submit.setTextColor(Color.parseColor("#E1E0DE"));
		//		btn_preview.setTextColor(Color.parseColor("#E1E0DE"));
	}

	private void selButton()
	{
		//		btn_preview.setPressed(true);
		//		btn_submit.setPressed(true);
		////		btn_preview.setClickable(true);
		//		btn_submit.setClickable(true);
		//		btn_submit.setTextColor(Color.WHITE);
		//		btn_preview.setTextColor(Color.WHITE);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			//			jumpBackActivity();
			finish();
		}
		return false;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//		unregisterReceiver(broadcastReceiver);

	}
	@Override
	protected void onRestart()
	{
		//		isShowOkBt();
		super.onRestart();
	}


	@OnClick(R.id.bt_back)
	public void onBack(View v){
		finish();
	}
}
