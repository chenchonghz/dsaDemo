package com.szrjk.index;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.szrjk.adapter.PhotoGridAdapter;
import com.szrjk.config.Constant;
import com.szrjk.config.PhotoConstant;
import com.szrjk.dhome.AlbumActivity;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.GalleryActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.*;
import com.szrjk.fire.FireEye;
import com.szrjk.fire.StaticPattern;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.*;
import com.szrjk.widget.AddPhotoPopup;
import com.szrjk.widget.PostSendPopup;
import com.szrjk.widget.UpdateProgressBar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_post)
public class SendPost1Activity extends BaseActivity {

	// 发送
	@ViewInject(R.id.tv_send)
	private TextView tv_send;
	// 取消
	@ViewInject(R.id.lly_cancel)
	private LinearLayout lly_cancel;
	// 进度条
	@ViewInject(R.id.pb_loading)
	private UpdateProgressBar pb_loading;
	// 内容
	@ViewInject(R.id.et_content)
	private EditText et_content;
	// 字数提示框
	@ViewInject(R.id.tv_content)
	private TextView tv_content;
	@ViewInject(R.id.lly_post)
	private LinearLayout lly_post;
	private SendPost1Activity instance;
	private FireEye fireEye;
	private Resources resources;
	private UserInfo userInfo;
	private String content;
	// 弹出框
	private AddPhotoPopup menuWindow;
	// 拍照
	private static final int CAMERA_WITH_DATA = 3022;
	// 相册选择
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	private static final int DATA_CHARGE_NOTIFY = 1000;
	private static final int GALLERY_RESULT_TYPE = 2000;
	private Handler handler;
	// 弹出框
	private PostSendPopup sendWindow;
	// 相册容器
	@ViewInject(R.id.gv_case_list)
	private GridView gv_case_list;
	// 相册适配器
	private PhotoGridAdapter gridAdapter;
	private StringBuffer sb;

	@ViewInject(R.id.tv_addimg)
	private TextView tv_addimg;

	private ArrayList<String> imgls = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		fireEye = new FireEye(instance);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		initLayout();

	}

	private void initLayout() {
		userInfo = Constant.userInfo;
		resources = getResources();
		sb = new StringBuffer();
		handler = new PhotoHandler();
		Message msg = new Message();
		msg.what = DATA_CHARGE_NOTIFY;
		handler.sendMessage(msg);
		// 相册
		gv_case_list.setSelector(new ColorDrawable(Color.TRANSPARENT));
		//环境、 List<ImageItem> 
		System.out.println("grid里面的图片个数："+PhotoConstant.postItems.size());
		if (PhotoConstant.postItems.size() == 0) {
			gv_case_list.setVisibility(View.GONE);
		}
		gridAdapter = new PhotoGridAdapter(instance, PhotoConstant.postItems);
		gv_case_list.setAdapter(gridAdapter);

		String contentRequire = resources.getString(R.string.content_require);
		fireEye.add(et_content,
				StaticPattern.Required.setMessage(contentRequire),
				StaticPattern.Required);
		CheckTextNumber.setEditTextChangeListener(et_content, tv_content, 800);
	}

	@OnItemClick(R.id.gv_case_list)
	public void caseItemClick(AdapterView<?> adapterView, View view, int id,
			long position) {
		if (PhotoConstant.postItems.size() != 0) {
			gv_case_list.setVisibility(View.VISIBLE);
		}
		if (id == PhotoConstant.postItems.size()) {
			menuWindow = new AddPhotoPopup(instance, casePhotoClick);
			// 显示窗口
			menuWindow.showAtLocation(lly_post, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
		} else {
			System.out.println("点击了图片进入编辑gallery");
			Intent intent = new Intent(instance, GalleryActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("id", id);
			bundle.putInt(Constant.ACTIVITYENUM,
					ActivityEnum.SeedPostActivity.value());
			//把图片地址的sb传递过去
			System.out.println("帖子图片地址集合sb："+sb.toString());
			bundle.putString("urllist", sb.toString());
			intent.putExtras(bundle);
			startActivityForResult(intent, GALLERY_RESULT_TYPE);
		}
	}
	//
	@OnClick(R.id.lly_cancel)
	public void cancelClick(View v) {
		closeKeyboard();
		content = et_content.getText().toString();
		if (!"".equals(content) || PhotoConstant.caseCount > 0) {
			sendWindow = new PostSendPopup(instance, sendPostClick);
			// 显示窗口
			sendWindow.showAtLocation(lly_post, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
		}else{
			finish();
		}
	}
	//pop菜单响应
	private OnClickListener sendPostClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_save:
				sendWindow.dismiss();
				// 保存草稿
				save();
			case R.id.tv_nsave:
				sendWindow.dismiss();
				finish();
				break;
			case R.id.tv_cancel:
				sendWindow.dismiss();
				break;
			}
		}
	};
	
	//
	//	// 保存草稿
	private void save() {
		showToast(instance, "目前未开发、敬请期待哦", 0);
	}
	//
	@OnClick(R.id.tv_send)
	public void sendClick(View v) {
		switch (v.getId()) {
		case R.id.tv_send:
			content = et_content.getText().toString();

			if (content == null || content.length() == 0) {
				showToast(instance, "内容不能空！", 0);
				return;
			}

			send();
			break;
		}
	}
	//
	// 发送帖子
	private void send() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "sendNormalPost");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		//		if (sb !=null && sb.length() > 0) {
		//			picList = sb.deleteCharAt(sb.length() - 1).toString();
		//		}
		if (sb !=null && sb.length() > 0) {
			String picList = sb.toString();
			int end = picList.length()-1;
			Log.i("最后的图片picList",picList.substring(0, end) );
			busiParams.put("picList", picList);
		}else{
			busiParams.put("picList", "");
		}

		String userSeqId = userInfo.getUserSeqId();
		busiParams.put("userSeqId", userSeqId);
		busiParams.put("deviceType", "1");
		busiParams.put("content", content);

		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void start() {
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {

			}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject) {
//				ErrorInfo errorObj = JSON.parseObject(
//						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
//				Log.i("errorObj", errorObj.toString());
				runOnUiThread(new  Runnable() {
					public void run() {
						showToast(instance, "发帖失败、再试试呗", 0);
					}
				});
			}

			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					showToast(instance, "发帖成功!", Toast.LENGTH_SHORT);
					PhotoConstant.postItems.clear();
					finish();
				}else{
					//检查ErrorMessage里面的提示

					Log.i("message", errorObj.getErrorMessage());
					if (errorObj.getErrorMessage().contains("Incorrect string value")) {
						showToast(instance, "目前不支持表情发送", 0);
					}
				}
			}
		});
	}
	//
	//
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case CAMERA_WITH_DATA:
			if (PhotoConstant.postItems.size() < 9) {
				Bitmap bitmapCaputre = (Bitmap) data.getExtras().get("data");
				if (bitmapCaputre == null) {
					return;
				}
				if (null != bitmapCaputre) {
					String fileName = String
							.valueOf(System.currentTimeMillis());
					FileUtils.saveBitmap(bitmapCaputre, fileName);
					ImageItem takePhoto = new ImageItem();
					takePhoto.setBitmap(bitmapCaputre);
					takePhoto.setImageId(Integer.valueOf((int)(Math.random() * 10000)));
					PhotoConstant.postItems.add(takePhoto);
					//为什么要发消息？更新适配器显示拍照回来的图片

					Message msg = new Message();
					msg.what = DATA_CHARGE_NOTIFY;
					msg.arg1 = CAMERA_WITH_DATA;
					Log.i("相机", "发消息通知更xin ");
					handler.sendMessage(msg);
//					if (PhotoConstant.postItems.size() > 0) {
						//						pb_loading.setVisibility(View.VISIBLE);
						//						pb_loading.setProgress(0);
						//						pb_loading.setVisibility(View.VISIBLE);
//						showToast(instance, "图片上传中,请稍候...", Toast.LENGTH_LONG);
						updateFile(bitmapCaputre);
//					}
				}
			} else {
				showToast(instance, "最多只能上传" + Constant.maxCount + "图片",
						Toast.LENGTH_LONG);
			}
			break;
		case PHOTO_PICKED_WITH_DATA:
			System.out.println("图库界面关闭；获得里面返回的数据");
			Message msg = new Message();
			//	sb = new StringBuffer();
			msg.what = DATA_CHARGE_NOTIFY;
			msg.arg1 = PHOTO_PICKED_WITH_DATA;
			handler.sendMessage(msg);
			Log.i("普通帖子图片个数", "PhotoConstant.postItems.size:"+PhotoConstant.postItems.size());
			break;

		case GALLERY_RESULT_TYPE:
			//同步图片地址list
			if (data != null) {
				imgls = data.getStringArrayListExtra("newImageUrl");
				System.out.println("*********************");
				System.out.println("回传到发帖界面的图片个数："+imgls.size());
				System.out.println("回传的list："+imgls.toString());
				//回来的时候，清空sb、把最新的list拆分。然后 | 拼起来
				sb = new StringBuffer();
				for (int i = 0; i < imgls.size(); i++) {
					sb.append(imgls.get(i));
					sb.append("|");
				}
				System.out.println("新的sb：---"+sb.toString());
			}
			gridAdapter.notifyDataSetChanged();
			if (PhotoConstant.postItems.size() == 0) {
				gv_case_list.setVisibility(View.GONE);
			}
			break;
		}
		Log.i("sendPost：img", imgls.size()+"");
		if (imgls.size() == 0) {
			gv_case_list.setVisibility(View.GONE);
		}
	}
	private int pros ;
	private int totala ;

	// 上传图片
	private void updateFile(Bitmap bitmap) {
		ByteArrayOutputStream baos = BitMapUtil.comp(bitmap);
		byte[] imgData = baos.toByteArray();
		uploadPhoto(imgData, PhotoType.Feed, new SaveCallback() {
			@Override
			public void onProgress(String arg0, int pro, int total) {
				pros = pro;
				totala = total;
				//				pb_loading.setMax(total);

			}
			@Override
			public void onFailure(String imageUrl, OSSException ossException) {
				handler.removeCallbacks(runnable);
				sb.append(OssUpdateImgUtil.feedPicFilterUrl + imageUrl + "|");
				// 处理图片
				new DealPhoto(imageUrl).start();
			}
			@Override
			public void onSuccess(String imageUrl) {
				handler.removeCallbacks(runnable);
				sb.append(OssUpdateImgUtil.feedPicFilterUrl + imageUrl + "|");
				Log.i("新的sb：", sb.toString());
//				Log.i("PhotoConstant.postItems.size()", PhotoConstant.postItems.size()+"");
				// 处理图片
				new DealPhoto(imageUrl).start();

			}
		});
		//		pb_loading.setVisibility(View.GONE);
	}

	class DealPhoto extends Thread {
		private String pathName;

		DealPhoto(String pathName) {
			this.pathName = pathName;
		}

		@Override
		public void run() {
			dealPhoto(pathName);
		}
	}
	//
	// 处理图片
	private void dealPhoto(String pathName) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "dealPic");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		List<PhotoBucket> buckets = new ArrayList<PhotoBucket>();
		PhotoBucket bucket = new PhotoBucket();
		bucket.setBucket(Constant.PHOTO_BUCKET_FEED);
		bucket.setKey(pathName);
		bucket.setSize(Constant.FEED_DEAL_SIZE);
		buckets.add(bucket);
		busiParams.put("pics", buckets);
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {

			@Override
			public void start() {

			}

			@Override
			public void loading(long total, long current, boolean isUploading) {

			}

			@Override
			public void failure(HttpException exception, JSONObject jobj) {

			}

			@Override
			public void success(JSONObject jsonObject) {
				gv_case_list.setVisibility(View.VISIBLE);
			}
		});
	}
	//
	// 创建Runnable对象
	Runnable runnable = new Runnable() {
		//		int i = 0;// 用来更新bar
		@Override
		public void run() {
			//			i = pb_loading.getProgress() + 10;
			//			if (i >= 100) {
			//				i = 0;
			//			}

			//			pb_loading.setProgress(pros);
			handler.postDelayed(runnable, 10);
		}
	};
	//
	boolean isnull = false;
	class PhotoHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DATA_CHARGE_NOTIFY:
				if (PhotoConstant.postCount != PhotoConstant.postItems.size()) {
					PhotoConstant.postCount += 1;
				}
				gridAdapter.notifyDataSetChanged();


				if (msg.arg1 == PHOTO_PICKED_WITH_DATA) {
					sb = new StringBuffer();
					// 循环上传图片
//					for (int i = 0; i < PhotoConstant.postItems.size(); i++) {
//
//						Integer newid = PhotoConstant.postItems.get(i).getImageId();
//						for (int j = 0; j < idlist.size(); j++) {
//							String temp = idlist.get(j);
//							if (newid.equals(temp)) {
//								break;
//							}else{
//								isnull = true;
//							}
//						}
//					}
//					if (isnull) {
////						showToast(instance, "图片上传中,请稍候...", 0);
//						isnull = false;
//					}
//					if (idlist.size() == 0 && PhotoConstant.postItems.size() != 0 ) {
////						showToast(instance, "图片上传中,请稍候...", 0);
//					}
//					idlist = getIdList();
					for (ImageItem postItems : PhotoConstant.postItems) {
						System.out.println("上传图片！");
						updateFile(postItems.getBitmap());
					}
				}
				break;

			}
		}
	}


	//点击图片增加
	@OnClick(R.id.tv_addimg)
	public void addImg(View v){
		System.out.println("点击增加图片！");
		switch (v.getId()) {
		case R.id.tv_addimg:

			Log.i("isActive --", imm.isActive()+"");

			closeKeyboard();

			menuWindow = new AddPhotoPopup(instance, casePhotoClick);
			// 显示窗口
			menuWindow.showAtLocation(lly_post, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);


			Log.i("isActive ==", imm.isActive()+"");
			break;

		}
	}
	private OnClickListener casePhotoClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.tv_picture:// 拍照
				doTakePicture();
				break;
			case R.id.tv_photo:// 相册选择
				System.out.println("进入相册选择");
				doTakePhoto();
				break;
			}
		}
	};
	private InputMethodManager imm;

	/**
	 * 从相册选择
	 */
	private ArrayList<String > idlist = new ArrayList<String>();;
	private void doTakePhoto() {
		Intent intent = new Intent();
		intent.setClass(instance, AlbumActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(Constant.ACTIVITYENUM,
				ActivityEnum.SeedPostActivity.value());
		intent.putExtras(bundle);
		System.out.println("------发帖--------");
		idlist = getIdList();
		Log.i("IDlist", idlist.toString());
		startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
	}

	/**
	 * 拍照
	 */ 
	private void doTakePicture() {
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, CAMERA_WITH_DATA);
	}
	@Override
	public void onBackPressed() {
		//这里检查是否要保存草稿
		PhotoConstant.postItems.clear();
		finish();
	}
	private void closeKeyboard(){
		if(imm.isActive()&&getCurrentFocus()!=null){
			if (getCurrentFocus().getWindowToken()!=null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}             
		}
	}
	private ArrayList<String> getIdList(){
		for (int i = 0; i < PhotoConstant.postItems.size(); i++) {
			idlist.add(String.valueOf(PhotoConstant.postItems.get(i).getImageId()));
		}
		return idlist;
	}
}
