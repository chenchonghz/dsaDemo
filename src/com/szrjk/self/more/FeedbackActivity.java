package com.szrjk.self.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.szrjk.adapter.PhotoGridAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.GalleryActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.ISelectImgCallback;
import com.szrjk.entity.UserCard;
import com.szrjk.entity.UserInfo;
import com.szrjk.fire.FireEye;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.CheckTextNumber;
import com.szrjk.util.DjsonUtils;
import com.szrjk.util.ImageItem;
import com.szrjk.util.MultipleUploadPhotoUtils;
import com.szrjk.util.OssUpdateImgUtil;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.IndexGridView;
import com.szrjk.widget.PostSendPopup;
import com.szrjk.widget.UpdateProgressBar;

@ContentView(R.layout.activity_feedback)
public class FeedbackActivity extends BaseActivity {

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
	private FeedbackActivity instance;
	private UserInfo userInfo;
	private String content;
	private InputMethodManager imm;

	private Handler handler;

	protected PostSendPopup sendWindow;

	private MultipleUploadPhotoUtils multipleUploadPhotoUtils;

	private static final int GALLERY_RESULT_TYPE = 2000;

	// 相册容器
	@ViewInject(R.id.gv_case_list)
	private IndexGridView gv_case_list;
	// 相册适配器
	private PhotoGridAdapter gridAdapter;

	@ViewInject(R.id.tv_addimg)
	private TextView tv_addimg;
	private Dialog dialog;
	private String circle_id;

	// 拍照
	private static final int CAMERA_WITH_DATA = 3022;
	// 相册选择
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	private static final int DATA_CHARGE_NOTIFY = 1000;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		Intent post_type = instance.getIntent();
		if (post_type != null) {
			circle_id = post_type.getStringExtra(Constant.CIRCLE);
		}
		initLayout();
	}

	private void initLayout() {
		dialog = createDialog(this, "发送中，请稍候...");
		userInfo = Constant.userInfo;
		// 相册
		gv_case_list.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gv_case_list.setVisibility(View.VISIBLE);
		gridAdapter = new PhotoGridAdapter(instance, new ArrayList<ImageItem>());
		gv_case_list.setAdapter(gridAdapter);
		//		if (gridAdapter.returnImageInfo().size() == 0) {
		//			gv_case_list.setVisibility(View.GONE);
		//		}

		CheckTextNumber.setEditTextChangeListener(et_content, tv_content, 800);

		et_content.setOnTouchListener(et_ls);
		instance.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

	}

	/**
	 * 调用拍照
	 */
	//地址集合
	private ArrayList<String> urlList = new ArrayList<String>();

	public void callSelectImg(){

		int maxNum = 9-gridAdapter.returnImageInfo().size();
		if(maxNum==0){
			ToastUtils.showMessage(this,"照片够了");
		}
		multipleUploadPhotoUtils= new MultipleUploadPhotoUtils(FeedbackActivity.this, lly_post,maxNum, new ISelectImgCallback() {
			@Override
			public void selectImgCallback(List<ImageItem> imgList,String[] urlarr) {

				//				ToastUtils.showMessage(SendPostActivity.this, imgList.size()+""+ DjsonUtils.bean2Json(urlarr));
				gridAdapter.addImageList(imgList);
				gridAdapter.notifyDataSetChanged();
				for (int i = 0; i < urlarr.length; i++) {
					urlList.add(OssUpdateImgUtil.feedPicFilterUrl + urlarr[i]);
				}
				Log.i("图片地址", urlList.toString());
				//				if (urlList.isEmpty()) {
				//					gv_case_list.setVisibility(View.GONE);
				//				}else{
				//					gv_case_list.setVisibility(View.VISIBLE);
				//				}
				multipleUploadPhotoUtils = null;
			}
		});
	}

	@OnItemClick(R.id.gv_case_list)
	public void caseItemClick(AdapterView<?> adapterView, View view, int num,
			long position) {
		//关闭软盘
		closeKeyboard();
		if (num == gridAdapter.returnImageInfo().size()) {
			//增加相片
			callSelectImg();
		} else {
			System.out.println("点击了图片进入编辑gallery");
			Intent intent = new Intent(instance, GalleryActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("id", num);
			//把图片地址的urlList传递过去
			bundle.putStringArrayList("urllist", urlList);
			GalleryActivity.filltmpitems(gridAdapter.returnImageInfo());
			intent.putExtras(bundle);
			startActivityForResult(intent, GALLERY_RESULT_TYPE);
		}
	}
	//
	@OnClick(R.id.lly_cancel)
	public void cancelClick(View v) {
		//		checkContent();
		finish();
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
	private String ls;

	//
	//	// 保存草稿
	private void save() {
		showToast(instance, "目前未开发、敬请期待哦", 0);
	}
	//
	@OnClick(R.id.tv_send)
	public void sendClick(View v) {
		closeKeyboard();
		tv_send.setClickable(false);
		switch (v.getId()) {
		case R.id.tv_send:
			content = et_content.getText().toString();
			if (content == null || content.length() == 0) {
				showToast(instance, "内容不能空！", 0);
				tv_send.setClickable(true);
				return;
			}
//			dialog.setCancelable(false);
			dialog.show();
			send();
			break;
		}
	}
	//
	// 发送帖子
	private void send() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "sendSuggestionMessage");
		Map<String, Object> busiParams = new HashMap<String, Object>();

		if (urlList.size() > 0) {

			StringBuffer stringBuffer = new StringBuffer();
			for (int i = 0; i < urlList.size(); i++) {
				stringBuffer.append(urlList.get(i));
				stringBuffer.append("|");
			}
			String st = stringBuffer.toString();
			ls = st.substring(0,st.length() - 1);
		}
//		busiParams.put("picList", ls);
		String userSeqId = userInfo.getUserSeqId();
		UserCard userCard=new UserCard();
		userCard.setUserName(userInfo.getUserName());
		userCard.setUserFaceUrl(userInfo.getUserFaceUrl());
		userCard.setUserSeqId(userSeqId);
		userCard.setDeptName(userInfo.getDeptName());
		userCard.setUserLevel(userInfo.getUserLevel());
		userCard.setProfessionalTitle(userInfo.getProfessionalTitle());
		userCard.setCompanyName(userInfo.getCompanyName());
		userCard.setUserType(userInfo.getUserType());
		Map<String, Object> sendUserCard =new HashMap<String, Object>();
		sendUserCard.put("deptName", userCard.getDeptName());
		sendUserCard.put("userSeqId", userCard.getUserSeqId());
		sendUserCard.put("userFaceUrl", userCard.getUserFaceUrl());
		sendUserCard.put("userLevel", userCard.getUserLevel());
		sendUserCard.put("professionalTitle", userCard.getProfessionalTitle());
		sendUserCard.put("companyName", userCard.getCompanyName());
		sendUserCard.put("userType", userCard.getUserType());

		busiParams.put("sendUserCard", sendUserCard);
//		busiParams.put("deviceType", "1");
		busiParams.put("content", content);

		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void start() {
//				dialog.setCancelable(false);
//				dialog.show();
//				tv_send.setClickable(false);
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {

				Log.i("total", total+"");
				Log.i("current", current+"");
				Log.i("isUploading", isUploading+"");
			}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject) {
				runOnUiThread(new Runnable() {
					public void run() {
						dialog.dismiss();
						showToast(instance, "提交失败、再试试呗", 0);
						tv_send.setClickable(true);
					}
				});
			}

			@Override
			public void success(JSONObject jsonObject) {
				dialog.dismiss();
				tv_send.setClickable(true);
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					showToast(instance, "提交成功!", Toast.LENGTH_SHORT);
					gridAdapter.returnImageInfo().clear();
					finish();
				} else {
					//检查ErrorMessage里面的提示

					Log.i("message", errorObj.getErrorMessage());
					if (errorObj.getErrorMessage().contains("Incorrect string value")) {
						showToast(instance, "目前不支持表情发送", 0);
						tv_send.setClickable(true);
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
		case PHOTO_PICKED_WITH_DATA:
			multipleUploadPhotoUtils.operResult(requestCode,resultCode,data);
			break;

		case CAMERA_WITH_DATA:
			multipleUploadPhotoUtils.operResult(requestCode,resultCode,data);
			break;
		case GALLERY_RESULT_TYPE:
			//同步图片地址list
			if (data != null) {
				//ArrayList<String>
				urlList = data.getStringArrayListExtra("urllist");
				gridAdapter.setImageList(GalleryActivity.gettmpitems());
				gridAdapter.notifyDataSetChanged();
			}
			break;
		}
		//		if (gridAdapter.returnImageInfo().isEmpty()) {
		//			gv_case_list.setVisibility(View.GONE);
		//		}
	}
	// 创建Runnable对象
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			handler.postDelayed(runnable, 10);
		}
	};

	//点击图片增加
	@OnClick(R.id.tv_addimg)
	public void addImg(View v){
		System.out.println("点击增加图片！");
		switch (v.getId()) {
		case R.id.tv_addimg:

			closeKeyboard();
			if (gridAdapter.returnImageInfo().size() == 9) {
				showToast(instance, "最多9张图片", 0);
				return;
			}	
			callSelectImg();
			break;

		}
	}

	@Override
	public void onBackPressed() {
		//这里检查是否要保存草稿
		//		checkContent();
//		dialog.setCancelable(true);
		dialog.dismiss();
		finish();

	}
	private void closeKeyboard(){
		if(imm.isActive()&&getCurrentFocus()!=null){
			if (getCurrentFocus().getWindowToken()!=null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
	//	检查保存草稿用的
	private void checkContent(){
		String scontext = et_content.getText().toString();
		if (scontext.length() > 0 || gridAdapter.returnImageInfo().size() > 0) {
			closeKeyboard();
			sendWindow = new PostSendPopup(instance, sendPostClick);
			// 显示窗口
			sendWindow.showAtLocation(lly_post, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
		}else{
			gridAdapter.returnImageInfo().clear();
			finish();
		}
	}
	private View.OnTouchListener et_ls = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.getParent().requestDisallowInterceptTouchEvent(false);
			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
				v.getParent().requestDisallowInterceptTouchEvent(false);
			}
			return false;
		}
	};
}
