package com.szrjk.self.more.album;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.self.more.album.PhotoUpAlbumHelper.GetAlbumList;
import com.szrjk.util.AlbumHelper;

@ContentView(R.layout.activity_album_gallery)
public class AlbumGalleryActivity extends BaseActivity {
	
	@ViewInject(R.id.ll_backtoalbum)
	private LinearLayout ll_backtoalbum;
	@ViewInject(R.id.tv_cancel)
	private TextView tv_cancel;
	// 列表
	@ViewInject(R.id.gv_list)
	private GridView gv_list;
	// 无内容提示
	@ViewInject(R.id.tv_album)
	private TextView tv_album;
	private AlbumGalleryAdapter viewAdapter;// 适配器
	private AlbumGalleryActivity instance;
	private ArrayList<PhotoUpImageItem> dataList;
	private AlbumHelper helper;
	private PhotoUpAlbumHelper photoUpAlbumHelper;
	public static List<PhotoUpImageBucket> contentList;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(instance);
		loadData();
		
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
		
		Log.i("dataList[0]", dataList.get(0).toString());

		viewAdapter = new AlbumGalleryAdapter(instance, dataList);

		gv_list.setAdapter(viewAdapter);
		gv_list.setEmptyView(tv_album);
		gv_list.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gv_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				Intent intent=new Intent(instance,CropPictureActivity.class);
				intent.putExtra("IMAGEPATH", dataList.get(i).getImagePath());
				startActivity(intent);
			}
		});
		ll_backtoalbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(instance, AlbumsActivity.class);
//				intent.putExtra("recentphoto", dataList.get(0).getImagePath());
//				intent.putExtra("count", dataList.size());
				AlbumsActivity.recentphoto=dataList.get(0).getImagePath();
				AlbumsActivity.count=dataList.size();
				startActivity(intent);
				finish();
			}
		});
		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}
	
	private void loadData(){
        photoUpAlbumHelper = PhotoUpAlbumHelper.getHelper();//创建异步线程实例
        photoUpAlbumHelper.init(AlbumGalleryActivity.this);//初始化实例
        //回调接口，创建匿名内部对象，实现接口中的方法，获取到PhotoUpAlbumHelper的接口GetAlbumList所传递的数据
        photoUpAlbumHelper.setGetAlbumList(new GetAlbumList() {
            @Override
            public void getAlbumList(List<PhotoUpImageBucket> list) {
            	contentList = list;
            	init();
            	viewAdapter.notifyDataSetChanged();//更新视图
            }
        });
        photoUpAlbumHelper.execute(false);//异步线程执行
	}
    
}
