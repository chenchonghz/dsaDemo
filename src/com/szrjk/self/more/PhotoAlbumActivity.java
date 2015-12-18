package com.szrjk.self.more;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.PhotoAlbum;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.widget.IndexGridView;

@ContentView(R.layout.activity_photo_album)
public class PhotoAlbumActivity extends BaseActivity{
	
	protected static final int GET_PHOTO_ALBUM_SUCCESS = 0;

	@ViewInject(R.id.igv_photo_album)
	private IndexGridView igv_photo_album;
	
	private PhotoAlbumActivity instance;

	private UserInfo userInfo;
	
	private ArrayList<PhotoAlbum> photoAlbumList;
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_PHOTO_ALBUM_SUCCESS:
				photoAlbumList=(ArrayList<PhotoAlbum>) msg.obj;
				setAdapter();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance=this;
		initLayout();
//		setAdapter();
	}

	private void initLayout() {
		userInfo=Constant.userInfo;
		getPhotoAlbumData();
	}

	private void setAdapter() {
//		photoAlbumList=new ArrayList<PhotoAlbum>();
//		PhotoAlbum photoAlbum=new PhotoAlbum();
//		photoAlbum.setPicUrl("http://dd-face.digi123.cn/201511/80426ba27eef6380.jpg");
//		photoAlbum.setMonth("2015年11月");
//		photoAlbum.setPicNum("12");
//		photoAlbumList.add(photoAlbum);
//		photoAlbumList.add(photoAlbum);
//		photoAlbumList.add(photoAlbum);
//		photoAlbumList.add(photoAlbum);
		PhotoAlbumAdapter photoAlbumAdapter=new PhotoAlbumAdapter(instance, photoAlbumList);
		igv_photo_album.setAdapter(photoAlbumAdapter);
		igv_photo_album.setSelector(new ColorDrawable(Color.TRANSPARENT));
		igv_photo_album.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i,
					long l) {
				Intent intent=new Intent(instance, PhotoAlbumGalleryActivity.class);
				intent.putExtra("MONTH", photoAlbumList.get(i).getMonth());
				startActivity(intent);
			}
		});
	}

	private void getPhotoAlbumData() {
		HashMap<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPhotoListByUser");
		HashMap<String, Object> busiParams=new HashMap<String, Object>();
		busiParams.put("userSeqId", userInfo.getUserSeqId());
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			
			@Override
			public void success(JSONObject jsonObject) {
				dialog.dismiss();
				ErrorInfo errorInfo=JSON.parseObject(jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorInfo.getReturnCode())) {
					JSONObject returnInfo=jsonObject.getJSONObject("ReturnInfo");
					ArrayList<PhotoAlbum> photoAlbums=(ArrayList<PhotoAlbum>) JSON.parseArray(returnInfo.getString("ListOut"), PhotoAlbum.class);
					Message message=new Message();
					message.what=GET_PHOTO_ALBUM_SUCCESS;
					message.obj=photoAlbums;
					handler.handleMessage(message);
				}
			}
			
			@Override
			public void start() {
				dialog = createDialog(instance, "拼命加载中......");
				dialog.show();
			}
			
			@Override
			public void loading(long total, long current, boolean isUploading) {
				
			}
			
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				dialog.dismiss();
			}
		});
		
	}
}
