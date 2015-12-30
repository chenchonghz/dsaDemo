package com.szrjk.self.more.album;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.self.more.album.PhotoUpAlbumHelper.GetAlbumList;
import com.szrjk.util.AlbumHelper;
import com.szrjk.widget.PictureListPopup;
/**
 * 
 * @author Administrator
 * 新图库
 */
@ContentView(R.layout.activity_album_gallery)
public class AlbumGalleryActivity extends BaseActivity {

	@ViewInject(R.id.id_bottom_ly)
	private RelativeLayout id_bottom_ly;
	@ViewInject(R.id.tv_name)
	private TextView tv_name;
	@ViewInject(R.id.ll_backtoalbum)
	private LinearLayout ll_backtoalbum;
	@ViewInject(R.id.tv_comp)
	private TextView tv_comp;
	// 列表
	@ViewInject(R.id.gv_list)
	private GridView gv_list;
	// 无内容提示
	@ViewInject(R.id.tv_album)
	private TextView tv_album;
	private AlbumGalleryAdapter viewAdapter;// 适配器
	private AlbumGalleryActivity instance;
	private ArrayList<PhotoUpImageItem> dataList;
	private PhotoUpAlbumHelper photoUpAlbumHelper;
	public static List<PhotoUpImageBucket> contentList;
	private int num;
	private Dialog progressDialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(instance);
		loadData();
		num = this.getIntent().getIntExtra("num", 0);
		//		num = 9;
		tv_comp.setText("完成" + "("+AlbumGalleryAdapter.mSelectedImage.size() + "/" + num + ")");
	}

	// 初始化，给一些对象赋值
	private void init() {
		dataList = new ArrayList<PhotoUpImageItem>();

		for (int i = 0; i < contentList.size(); i++) {
			dataList.addAll(contentList.get(i).getImageList());
			//			for (int j = 0; j < contentList.get(i).getImageList().size(); j++) {
			//				Log.i("imageid", contentList.get(i).getImageList().get(j).getImageId());
			//				Log.i("imagepath", contentList.get(i).getImageList().get(j).getImagePath());
			//			}
		}
		Collections.sort(dataList);
		Collections.reverse(dataList);

		//		Log.i("dataList[0]", dataList.get(0).toString());

		viewAdapter = new AlbumGalleryAdapter(instance, dataList,tv_comp,num);

		gv_list.setAdapter(viewAdapter);
		gv_list.setEmptyView(tv_album);
		gv_list.setSelector(new ColorDrawable(Color.TRANSPARENT));
		//不能使用
		//		gv_list.setOnItemClickListener(new OnItemClickListener() {
		//			@Override
		//			public void onItemClick(AdapterView<?> adapterView, View view,
		//					int i, long l) {
		//				Intent intent=new Intent(instance,CropPictureActivity.class);
		//				intent.putExtra("IMAGEPATH", dataList.get(i).getImagePath());
		//				startActivity(intent);
		//			}
		//		});
		//		ll_backtoalbum.setOnClickListener(new OnClickListener() {
		//			@Override
		//			public void onClick(View view) {
		//				Intent intent=new Intent(instance, AlbumsActivity.class);
		//				//				intent.putExtra("recentphoto", dataList.get(0).getImagePath());
		//				//				intent.putExtra("count", dataList.size());
		//				AlbumsActivity.recentphoto=dataList.get(0).getImagePath();
		//				Log.i("测试1", ""+dataList.get(0).getImagePath());
		//				Log.i("测试2", "");
		//				AlbumsActivity.count=dataList.size();
		//				Log.i("测试3", ""+dataList.size());
		//				intent.putExtra("num", num);
		//				startActivity(intent);
		//				finish();
		//			}
		//		});
		tv_comp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ArrayList<String> url = (ArrayList<String>) AlbumGalleryAdapter.mSelectedImage;
				Log.i("url", url.toString());
				if (url.size() == 0) {
					Toast.makeText(AlbumGalleryActivity.this, "没有选择图片", Toast.LENGTH_SHORT).show();
				} else {

					Intent intent = new Intent();
					String[] arr = new String[url.size()];
					for (int i = 0; i < url.size(); i++) {
						arr[i] = url.get(i);
					}
					intent.putExtra("arr", arr);
					setResult(RESULT_OK, intent);
					//清空图片
					AlbumGalleryAdapter.mSelectedImage.clear();
					finish();
				}
			}
		});
		//点击相册
		ll_backtoalbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				PictureListPopup popup = new PictureListPopup(instance, ll_backtoalbum, contentList,mHandler);
//				popup.showAtLocation(ll_backtoalbum, Gravity.BOTTOM, 0, -200);
				finish();
			}
		});
		getViewHeight();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			AlbumGalleryAdapter.mSelectedImage.clear();
		}
		return false;
	}
	//扫描图片缩略图，获得图片ID，绝对路径
	private void loadData(){
		progressDialog = createDialog(this, "加载中...");
		progressDialog.show();
		photoUpAlbumHelper = PhotoUpAlbumHelper.getHelper();//创建异步线程实例
		photoUpAlbumHelper.init(AlbumGalleryActivity.this);//初始化实例
		//回调接口，创建匿名内部对象，实现接口中的方法，获取到PhotoUpAlbumHelper的接口GetAlbumList所传递的数据
		photoUpAlbumHelper.setGetAlbumList(new GetAlbumList() {
			@Override
			public void getAlbumList(List<PhotoUpImageBucket> list) {
				contentList = list;
				Log.i("contentList", contentList.toString());
				progressDialog.cancel();
				init();
				viewAdapter.notifyDataSetChanged();//更新视图
			}
		});
		photoUpAlbumHelper.execute(false);//异步线程执行
	}

	//点击pop返回之后，切换相册列表
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(android.os.Message msg)		{
			switch (msg.what) {
			case 0:
				viewAdapter = new AlbumGalleryAdapter(instance, dataList,tv_comp,num);
				gv_list.setAdapter(viewAdapter);
				gv_list.setEmptyView(tv_album);
				gv_list.setSelector(new ColorDrawable(Color.TRANSPARENT));
				tv_name.setText("最近照片");
				break;
			case 1:
				PhotoUpImageBucket  data = (PhotoUpImageBucket)msg.obj;
				tv_name.setText(data.getBucketName());
				viewAdapter = new AlbumGalleryAdapter(AlbumGalleryActivity.this,data.getImageList(),tv_comp,num);
				gv_list.setAdapter(viewAdapter);
				gv_list.setEmptyView(tv_album);
				gv_list.setSelector(new ColorDrawable(Color.TRANSPARENT));
				break;
			}
		}
	};

	public PhotoUpImageBucket getHeadItem(){
		PhotoUpImageBucket photoUpImageBucket=new PhotoUpImageBucket();
		photoUpImageBucket.setBucketName("最近照片");
		photoUpImageBucket.setCount(dataList.size());
		ArrayList<PhotoUpImageItem> imageList = new ArrayList<PhotoUpImageItem>();
		PhotoUpImageItem photoUpImageItem = new PhotoUpImageItem();
		photoUpImageItem.setImagePath(dataList.get(0).getImagePath());
		imageList.add(0, photoUpImageItem);
		photoUpImageBucket.setImageList(imageList);
		return photoUpImageBucket;
	}

	private int px;//获得底部控件的高度
	public void getViewHeight(){
		ViewTreeObserver vtos = id_bottom_ly.getViewTreeObserver();    
		vtos.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
			@Override    
			public void onGlobalLayout() {  
				id_bottom_ly.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
				px = id_bottom_ly.getHeight();
				Log.i("h", px+"");
				initEvent();//pop的点击事件，获得高度之后设置 
			}    
		});
	}

	private PictureListPopup popup;
	protected void initEvent() {
		//由于showAsDropDown，sdk 17以上可以正常使用，16未测试，但是低的有异常
		//				mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);
		//基于点击的控件     位置                                     x偏移 y偏移，0底部，++往上
		id_bottom_ly.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popup = new PictureListPopup(instance, id_bottom_ly, contentList,mHandler);
				popup.setAnimationStyle(R.style.anim_popup_dir);
				//		popup.showAtLocation(ll_backtoalbum, Gravity.BOTTOM, 0, -200);
				popup.showAtLocation(id_bottom_ly, Gravity.BOTTOM, 0, px);
				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = .3f;
				getWindow().setAttributes(lp);
				
				
				popup.setOnDismissListener(new OnDismissListener()		{
					@Override
					public void onDismiss()
					{
						// 设置背景颜色变亮
						WindowManager.LayoutParams lp = getWindow().getAttributes();
						lp.alpha = 1.0f;
						getWindow().setAttributes(lp);
					}
				});
			}
		});
		
	}
}
