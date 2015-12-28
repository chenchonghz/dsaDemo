package com.szrjk.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.CheckTextNumber;
import com.szrjk.util.ImageItem;
import com.szrjk.util.MultipleUploadPhotoUtils;
import com.szrjk.util.OssUpdateImgUtil;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.HeaderView;
import com.szrjk.widget.IndexGridView;
import com.szrjk.widget.PostSendPopup;
import com.szrjk.widget.UpdateProgressBar;

@ContentView(R.layout.activity_post)
public class SendPostActivity extends BaseActivity {
	@ViewInject(R.id.hv_post)
	private HeaderView hv_post;
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
	private SendPostActivity instance;
	private UserInfo userInfo;
	private String content;
	private InputMethodManager imm;
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
	private String ls;
	//地址集合
	private ArrayList<String> urlList = new ArrayList<String>();
	private TextView sub;
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
		try {
			dialog = createDialog(this, "发送中，请稍候...");
			userInfo = Constant.userInfo;
			// 相册
			gv_case_list.setSelector(new ColorDrawable(Color.TRANSPARENT));
			gv_case_list.setVisibility(View.VISIBLE);
			gridAdapter = new PhotoGridAdapter(instance,
					new ArrayList<ImageItem>());
			gv_case_list.setAdapter(gridAdapter);
			if (gridAdapter.returnImageInfo().size() == 0) {
				gv_case_list.setVisibility(View.GONE);
			}
			//检查剩余字数用的
			CheckTextNumber.setEditTextChangeListener(et_content, tv_content,800);
			et_content.setOnTouchListener(et_ls);
			//		instance.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

			//			hv_post.showBackBtn(text, onClickListener)
			sub = hv_post.getTextBtn();
			hv_post.showTextBtn("发布", new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						closeKeyboard();
						//防止多次点击发帖
						sub.setClickable(false);
						content = et_content.getText().toString().trim();
						if (content == null || content.length() == 0) {
							showToast(instance, "内容不能空！", 0);
							sub	.setClickable(true);
							return;
						}
						//			dialog.setCancelable(false);
						dialog.show();
						send();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			//			hv_post.setHtext(s)
			//			hv_post.showImageLLy(resId, onClickListener)
			//			hv_post.getTextBtn()

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private ArrayList<String> absList= new ArrayList<String>();
	/**
	 * 调用拍照
	 */
	public void callSelectImg(){

		try {
			int maxNum = 9 - gridAdapter.returnImageInfo().size();
			if (maxNum == 0) {
				ToastUtils.showMessage(this, "照片最多9张");
			}
			multipleUploadPhotoUtils = new MultipleUploadPhotoUtils(
					SendPostActivity.this, lly_post, maxNum,
					new ISelectImgCallback() {
						@Override
						public void selectImgCallback(List<ImageItem> imgList,
								String[] urlarr) {

							//		ToastUtils.showMessage(SendPostActivity.this, imgList.size()+""+ DjsonUtils.bean2Json(urlarr));
							gridAdapter.addImageList(imgList);
							gridAdapter.notifyDataSetChanged();
							for (int i = 0; i < urlarr.length; i++) {
								urlList.add(OssUpdateImgUtil.feedPicFilterUrl + urlarr[i]);
							}
							Log.i("图片地址", urlList.toString());
							if (urlList.isEmpty()) {
								gv_case_list.setVisibility(View.GONE);
							} else {
								gv_case_list.setVisibility(View.VISIBLE);
							}
							//获得图片的绝对地址
							for (int j = 0; j < imgList.size(); j++) {
								absList.add(imgList.get(j).getAbsPaht());
							}
							multipleUploadPhotoUtils = null;
						}
					});
		} catch (Exception e) {
			Log.i("callSelectImg", "callSelectImg异常----");
			e.printStackTrace();
		}
	}

	@OnItemClick(R.id.gv_case_list)
	public void caseItemClick(AdapterView<?> adapterView, View view, int num,
			long position) {
		try {
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
				bundle.putStringArrayList("absList", absList);
				//GalleryActivity.filltmpitems(gridAdapter.returnImageInfo());
				intent.putExtras(bundle);
				startActivityForResult(intent, GALLERY_RESULT_TYPE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 发送帖子
	private void send() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		//如果传递过来了圈子ID、就切换接口
		if (circle_id != null) {
			paramMap.put("ServiceName", "sendCoteriePost");
		}else{
			paramMap.put("ServiceName", "sendNormalPost");
		}
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
		busiParams.put("picList", ls);
		String userSeqId = userInfo.getUserSeqId();
		busiParams.put("userSeqId", userSeqId);
		busiParams.put("deviceType", "1");
		busiParams.put("content", content);

		//发圈子帖子、如果有圈子ID，就增加入参
		if (circle_id != null) {
			busiParams.put("coterieId", circle_id);
		}

		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void start() {
				//	dialog.setCancelable(false);
				//	dialog.show();
				//	tv_send.setClickable(false);
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {

				Log.i("total", total+"");
				Log.i("current", current+"");
				Log.i("isUploading", isUploading+"");
			}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject) {
				final String err = jsonObject.toString();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dialog.dismiss();
						showToast(instance, "发帖失败、再试试呗", 0);
						sub.setClickable(true);
						if (err.contains("Incorrect string value")) {
							showToast(instance, "目前不支持表情发送", 0);
						}
					}
				});
			}

			@Override
			public void success(JSONObject jsonObject) {
				dialog.dismiss();
				sub.setClickable(true);
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					showToast(instance, "发帖成功!", Toast.LENGTH_SHORT);
					gridAdapter.returnImageInfo().clear();
					finish();
				} else {
					//检查ErrorMessage里面的提示

					Log.i("message", errorObj.getErrorMessage());
					if (errorObj.getErrorMessage().contains("Incorrect string value")) {

						sub.setClickable(true);
					}
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode != RESULT_OK) {
				return;
			}
			switch (requestCode) {
			case PHOTO_PICKED_WITH_DATA:
				multipleUploadPhotoUtils.operResult(requestCode, resultCode,data);
				break;

			case CAMERA_WITH_DATA:
				multipleUploadPhotoUtils.operResult(requestCode, resultCode,data);
				break;
			case GALLERY_RESULT_TYPE:
				//同步图片地址list
				if (data != null) {
					//ArrayList<String>
					urlList = data.getStringArrayListExtra("urllist");
					absList = data.getStringArrayListExtra("absList");
					//					gridAdapter.setImageList(GalleryActivity.gettmpitems());
					gridAdapter.addStringUrl(absList);
					gridAdapter.notifyDataSetChanged();
				}
				break;
			}
			if (gridAdapter.returnImageInfo().isEmpty()) {
				gv_case_list.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//点击图片增加
	@OnClick(R.id.tv_addimg)
	public void addImg(View v){
		try {
			//		System.out.println("点击增加图片！");
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		//这里检查是否要保存草稿
		//		checkContent();
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
	
	
	class ImageBrocast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String ac = intent.getAction();
			
			if ("失败".equals(ac)) {
				
			}else{
				
			}
			
		}
	}
	
	
	
	/**
	 * 这里的3个重写方法：由于打开了拍照，低ram的手机会回收发帖这个Activity。
	 * 当回收之后，会执行onCreate方法里面的检查草稿方法。导致把草稿恢复覆盖当前编辑的内容
	 * @param newConfig
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		Log.i("onConfigurationChanged", "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i("onRestoreInstanceState", "onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("onSaveInstanceState", "onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}
}
