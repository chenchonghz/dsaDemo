package com.szrjk.dhome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.IImgUrlCallback;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.self.more.MoreActivity;
import com.szrjk.util.DjsonUtils;
import com.szrjk.util.UploadPhotoUtils;
import com.szrjk.widget.AddPhotoPopup;
import com.szrjk.widget.PopWindowDeleteImage;
import com.szrjk.widget.UpdateProgressBar;

@ContentView(R.layout.activity_student)
public class StudentActivity extends BaseActivity implements OnClickListener{
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.tv_title2)
	private TextView tv_title2;
	@ViewInject(R.id.iv_back)
	private ImageView iv_back;
	@ViewInject(R.id.ll_student)
	private LinearLayout ll_student;
	String tag = "StudentActivity";
	//跳过
	@ViewInject(R.id.tv_skip)
	private TextView tv_skip;
	//身份证
	@ViewInject(R.id.bt_identity)
	private Button bt_identity;
	//工作证
	@ViewInject(R.id.bt_workpermit)
	private Button bt_workpermit;
	//完成
	@ViewInject(R.id.btnSubmit)
	private  Button btnSubmit;
	//上传进度
	@ViewInject(R.id.pb_loading)
	private UpdateProgressBar pb_loading;
	@ViewInject(R.id.lly_case_content)
	private LinearLayout lly_case_content;
	//身份证img
	@ViewInject(R.id.iv_identity)
	private ImageView iv_identity;
	//工作证img
	@ViewInject(R.id.iv_workpermit)
	private ImageView iv_workpermit;

	private Handler handler = new Handler();
	private int updateIvType = 1;
	private AddPhotoPopup menuWindow;
	private static final int CAMERA_WITH_DATA = 3022;
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	private final static int SEL_DEPT_CODE = 100;
	//	作为判断上传按钮的标记
	private Boolean identity_type = false;
	private Boolean workpermit_type = false;
	//	作为第二次点击上传按钮的标记，再次显示gone的img
	private Boolean second_i = false;
	private Boolean second_w = false;

	private ImageView imageViewCaputre;
	//用户信息
	private UserInfo userInfo;

	private StudentActivity instance;
	private String userId;

	private String identity_imgurl;
	private String identity_imgname;
	private String i_state = "no";
	private String workpermit_imgurl;
	private String workpermit_imgname;

	private String w_state = "no";
	private boolean fl;
	private Dialog alertdialog;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		alertdialog = createDialog(this, "提交中...");
		try {

			initLayout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//初始化监听
	private void initLayout() {
		//		初始化的时候，获得信息
		userInfo = Constant.userInfo;
		userId = userInfo.getUserSeqId();
		bt_identity.setOnClickListener(this);
		bt_workpermit.setOnClickListener(this);

		bt_identity.setTag("0");
		bt_workpermit.setTag("0");


		iv_identity.setOnClickListener(this);
		iv_workpermit.setOnClickListener(this);

		btnSubmit.setOnClickListener(this);
		tv_skip.setOnClickListener(this);

		uploadPhotoUtils= new UploadPhotoUtils(instance,false);

		Intent intent = this.getIntent();
		fl = intent.getBooleanExtra("authem", false);
		if (fl) {
			tv_skip.setVisibility(View.GONE);
			tv_title.setVisibility(View.GONE);
			tv_title2.setVisibility(View.VISIBLE);
			iv_back.setVisibility(View.VISIBLE);
			iv_back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		try {
			//点击上传图片选择图库，拍照
			switch (v.getId()) {

			case R.id.bt_identity:
				identity_type = true;
				//			showPoPWindow(iv_identity);
				uploadPhotoUtils.popubphoto(iv_identity, new IImgUrlCallback() {
					@Override
					public void operImgUrl(String imgurl) {
						String photoName = imgurl.substring(imgurl
								.lastIndexOf("/") + 1);
						//					switch (image_type)	{
						//					case 1:
						identity_imgurl = imgurl;
						identity_imgname = photoName;
						i_state = "ok";
						System.err.println("保存身份证图片地址，名字" + i_state);
						btnSubmit.setEnabled(true);
						System.err.println("身份证：判断有上传图，设置可点击");
						//						ToastUtils.showMessage(DoctorActivity.this, imgurl);

					}

					@Override
					public void operImgPic(Bitmap bitmap) {
						iv_identity.setImageBitmap(bitmap);

						if (second_i) {
							iv_identity.setVisibility(View.VISIBLE);
						}
						//					imageViewCaputre = iv_identity;

						// 当上传了图片，就改变按钮。设置颜色。如果删除了，就改回原来；

						bt_identity.setBackgroundColor(Color.TRANSPARENT);
						bt_identity.setText(R.string.authentication_uploading);
						bt_identity.setTextColor(instance.getResources()
								.getColor(R.color.font_cell));
						bt_identity.setClickable(false);
						btnSubmit.setEnabled(true);
						btnSubmit.setTextColor(instance.getResources()
								.getColor(R.color.green_button_selected));
						identity_type = false;
						iv_identity.setVisibility(View.VISIBLE);
						image_type = 1;

					}
				});
				bt_identity.setTag("1");
				break;
			case R.id.bt_workpermit:
				workpermit_type = true;
				//			showPoPWindow(iv_workpermit);
				//			new UploadPhotoUtils(instance).popubphoto(iv_workpermit);
				uploadPhotoUtils.popubphoto(iv_workpermit,	new IImgUrlCallback() {

					@Override
					public void operImgUrl(String imgurl) {
						// TODO Auto-generated method stub
						//					ToastUtils.showMessage(DoctorActivity.this, imgurl);
						String photoName = imgurl.substring(imgurl
								.lastIndexOf("/") + 1);
						workpermit_imgurl = imgurl;
						workpermit_imgname = photoName;
						w_state = "ok";
						btnSubmit.setEnabled(true);

					}

					@Override
					public void operImgPic(Bitmap bitmap) {
						// TODO Auto-generated method stub
						iv_workpermit.setImageBitmap(bitmap);
						if (second_w) {
							iv_workpermit.setVisibility(View.VISIBLE);
						}
						//						imageViewCaputre = iv_workpermit;
						bt_workpermit
						.setBackgroundColor(Color.TRANSPARENT);
						bt_workpermit
						.setText(R.string.authentication_uploading);
						bt_workpermit.setTextColor(instance
								.getResources().getColor(
										R.color.font_cell));
						btnSubmit.setEnabled(true);
						btnSubmit
						.setTextColor(instance
								.getResources()
								.getColor(
										R.color.green_button_selected));
						workpermit_type = false;
						iv_workpermit.setVisibility(View.VISIBLE);
						image_type = 3;
						bt_workpermit.setClickable(false);

					}
				});
				bt_workpermit.setTag("3");
				break;

			case R.id.iv_identity:
				popWindowDeleteImage_inde = new PopWindowDeleteImage(instance,
						new OnClickListener() {
					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.tv_delete_image:
							iv_identity.setVisibility(View.GONE);
							identity_type = false;
							bt_identity
							.setBackgroundResource(R.drawable.button_blue_selector);
							bt_identity
							.setText(R.string.authentication_upload);
							bt_identity
							.setTextColor(instance
									.getResources()
									.getColor(
											R.color.green_button_selected));
							second_i = true;
							bt_identity.setTag("0");
							i_state = "no";
							chekcImageState();
							bt_identity.setClickable(true);
							popWindowDeleteImage_inde.dismiss();
							break;
						}
					}
				});
				popWindowDeleteImage_inde.showAtLocation(ll_student,
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				break;
			case R.id.iv_workpermit:
				popWindowDeleteImage_work = new PopWindowDeleteImage(instance,
						new OnClickListener() {
					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.tv_delete_image:
							iv_workpermit.setVisibility(View.GONE);
							workpermit_type = false;
							bt_workpermit
							.setBackgroundResource(R.drawable.button_blue_selector);
							bt_workpermit
							.setText(R.string.authentication_upload);
							bt_workpermit
							.setTextColor(instance
									.getResources()
									.getColor(
											R.color.green_button_selected));
							second_w = true;
							bt_workpermit.setTag("0");
							w_state = "no";
							chekcImageState();
							bt_workpermit.setClickable(true);
							popWindowDeleteImage_work.dismiss();
							break;
						}
					}
				});
				popWindowDeleteImage_work.showAtLocation(ll_student,
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				break;

			case R.id.btnSubmit:
				//			btnSubmit.setTextColor(instance.getResources().getColor(R.color.white));

				uploaddata();
				break;
			case R.id.tv_skip:
				Intent i = new Intent(instance, MainActivity.class);
				startActivity(i);
				finish();
			}

			//		//判断3个图片是否上传，如果都没有，则屏蔽完成按钮

			//checkState();

			System.err.println("iden:" + i_state);
			System.err.println("work:" + w_state);

			chekcImageState();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void chekcImageState() {
		if ("ok".equals(i_state) || "ok".equals(w_state)) {
			btnSubmit.setEnabled(true);
			System.err.println("onClick判断有上传图，设置可点击");
		}else{
			btnSubmit.setEnabled(false);
			btnSubmit.setTextColor(instance.getResources().getColor(R.color.font_cell));
			System.err.println("onClick不能点击");
		}
	}

	/**
	 * 点击完成，上传图片名字，类型
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void uploaddata() {
		alertdialog.show();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "submitCertInfo");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		//临时ID
		//		busiParams.put("userSeqId", "100543");
		busiParams.put("userSeqId", userId);

		//			String identity_imgurl;
		//			String identity_imgname;
		//			
		//			String cetificater_imgurl;
		//			String cetificater_imgname;
		//			
		//			String workpermit_imgurl;
		//			String workpermit_imgname;


//		List l = new ArrayList();
		List list_id = new ArrayList();


		if (bt_identity.getTag().equals("1")) {

			Map m1 = new HashMap();

			m1.put("certType", bt_identity.getTag());
			m1.put("certId", identity_imgname);
			m1.put("certPicUrl", identity_imgurl);
			list_id.add(m1);

//			Map m11 = new HashMap();
//			m11.put("picUrl", identity_imgurl);
//			l.add(m11);

		}
		if (bt_workpermit.getTag().equals("3")) {
			Map m3 = new HashMap();
			m3.put("certType", bt_workpermit.getTag());
			m3.put("certId", workpermit_imgname);
			m3.put("certPicUrl", workpermit_imgurl);
			list_id.add(m3);

//			Map m33 = new HashMap();
//			m33.put("picUrl", workpermit_imgurl);
//			l.add(m33);
		}

		busiParams.put("certListIn", list_id);

//		busiParams.put("listCertPicIn", l);


		paramMap.put("BusiParams", busiParams);

		Log.e("log", DjsonUtils.bean2Json(paramMap));

		httpPost(paramMap, new AbstractDhomeRequestCallBack()
		{
			@Override
			public void start()
			{
			}

			@Override
			public void loading(long total, long current, boolean isUploading)
			{

			}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject)
			{
				Log.e("", "失败！");
				runOnUiThread(new  Runnable() {
					@Override
					public void run() {
						showToast(instance, "提交失败，再试试呗", 0);
						alertdialog.dismiss();
						btnSubmit.setEnabled(true);
					}
				});

			}

			@Override
			public void success(JSONObject jsonObject){
				alertdialog.dismiss();
				ErrorInfo errorObj = JSON.parseObject(jsonObject.getString("ErrorInfo"), ErrorInfo.class);

				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))	{
					JSONObject returnObj = jsonObject.getJSONObject("ErrorInfo");
					String msg = returnObj.getString("ErrorMessage");
					Log.e("returnObj", ""+returnObj);

					showToast(instance, msg,	 0);
					//Log.e("", "uploading");

					if (fl) {
						startActivity(new Intent(instance,MoreActivity.class));
						finish();
					}else{
						Intent intent = new Intent(instance,MainActivity.class);
						//需要传递参数去服务器
						startActivity(intent);
						finish();
					}

				}
			}
		});
	}


	/**
	 * 当从图库点击图片返回。获得图片并显示出来、
	 */
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != RESULT_OK) { 
			if (identity_type) identity_type = false;
			if (workpermit_type) workpermit_type = false;

			chekcImageState();
			Log.e("", "点击拍照后又返回");
			return; 
		}
		try {
			uploadPhotoUtils.imgOper(requestCode, resultCode, data);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//		switch (requestCode){
		//		//拍照返回图片
		//		case CAMERA_WITH_DATA:
		//
		//			Log.e("工作证tag", bt_workpermit.getTag()+"");
		//			Log.e("工作证状态", workpermit_type+"");
		//
		//			Log.e("身份证tag", bt_identity.getTag()+"");
		//			Log.e("身份证状态", identity_type+"");
		//
		//			System.err.println( "1".equals(bt_identity.getTag()));
		//			System.err.println("3".equals(bt_workpermit.getTag()));
		//			Bitmap bitmapCaputre = (Bitmap) data.getExtras().get("data");
		//			//发现有时候拍照点击完成之后，图片没有返回（我的手机酷派大神f1）
		//			if (bitmapCaputre == null) {
		//				showToast(instance, "出现异常，请从新拍照！", 1);
		//				return;
		//			}
		//			//做判断，分别设置位置
		//			if ( "1".equals(bt_identity.getTag()) && identity_type) {
		//				if (second_i) {
		//					iv_identity.setVisibility(View.VISIBLE);
		//				}
		//
		//				imageViewCaputre = iv_identity;
		//
		//				//当上传了图片，就改变按钮。设置颜色。如果删除了，就改回原来；
		//
		//				bt_identity.setBackgroundColor(Color.TRANSPARENT);
		//				bt_identity.setText(R.string.authentication_uploading);
		//				bt_identity.setTextColor(R.color.font_cell);
		//				btnSubmit.setEnabled(true);
		//				btnSubmit.setTextColor(instance.getResources().getColor(R.color.green_button_selected));
		//				identity_type = false;
		//				iv_identity.setVisibility(View.VISIBLE);
		//				image_type = 1;
		//			}else if ( "3".equals(bt_workpermit.getTag()) && workpermit_type) {
		//				if (second_w) {
		//					iv_workpermit.setVisibility(View.VISIBLE);
		//				}
		//				imageViewCaputre = iv_workpermit;
		//				bt_workpermit.setBackgroundColor(Color.TRANSPARENT);
		//				bt_workpermit.setText(R.string.authentication_uploading);
		//				bt_workpermit.setTextColor(R.color.font_cell);
		//				btnSubmit.setEnabled(true);
		//				btnSubmit.setTextColor(instance.getResources().getColor(R.color.green_button_selected));
		//				workpermit_type = false;
		//				iv_workpermit.setVisibility(View.VISIBLE);
		//				image_type = 3;
		//
		//				Log.e(tag, bt_workpermit.getTag()+"");
		//				Log.e(tag, image_type+"");
		//				Log.e("工作证", "工作证返回");
		//			}
		//			imageViewCaputre.setImageBitmap(bitmapCaputre);
		//			//			setProgressBarVisibility(true);
		//			//			pb_loading.setVisibility(View.VISIBLE);
		//			//			pb_loading.setProgress(0);
		//			//			handler.post(runnable);
		//			//			showToast(instance, "图片上传中。。。请稍候", Toast.LENGTH_LONG);
		//			updateFile(bitmapCaputre,image_type);
		//
		//			break;
		//		case PHOTO_PICKED_WITH_DATA:
		//			setProgressBarVisibility(true);
		//			pb_loading.setVisibility(View.VISIBLE);
		//			pb_loading.setProgress(0);
		//			handler.post(runnable);
		//			showToast(instance, "图片上传中。。。请稍候", Toast.LENGTH_LONG);
		//			Bitmap bitmap = data.getParcelableExtra("data");
		//			ImageView imageView = addImageView();
		//			imageView.setImageBitmap(bitmap);
		//
		//			imageView.setPadding(10, 10, 10, 10);
		//			lly_case_content.addView(imageView);
		//			//			updateFile(bitmap);
		//			break;
		//		}

		//		if ("ok".equals(i_state) || "ok".equals(w_state)) {
		//			btnSubmit.setEnabled(true);
		//			System.err.println("onActivityResult判断有上传图，设置可点击");
		//		}else{
		//			btnSubmit.setEnabled(false);
		//			btnSubmit.setTextColor(instance.getResources().getColor(R.color.font_cell));
		//			System.err.println("onActivityResult不能点击");
		//		}
	}

	int image_type = 0;

	//	private void updateFile(Bitmap bitmap, final int image_type)
	//	{
	//		ByteArrayOutputStream baos = BitMapUtil.comp(bitmap);
	//		byte[] imgData = baos.toByteArray();
	//		// String userSeqId = userInfo.getUserSeqId();
	//		uploadPhoto(imgData, PhotoType.Feed, new SaveCallback() {
	//			@Override
	//			public void onProgress(String arg0, int arg1, int arg2) {
	//			}
	//			@Override
	//			public void onFailure(String imgUrl, OSSException arg1) {
	//				String imageUrl  = OssUpdateImgUtil.feedPicFilterUrl+imgUrl;
	//				String photoName = imgUrl.substring(imgUrl.lastIndexOf("/")+1);
	//
	//				switch (image_type)	{
	//				case 1:
	//					identity_imgurl = imageUrl;
	//					identity_imgname = photoName;
	//					i_state = "ok";
	//					System.err.println("保存身份证图片地址，名字" + i_state);
	//					btnSubmit.setEnabled(true);
	//					System.err.println("身份证：判断有上传图，设置可点击");
	//
	//					break;
	//				case 3:
	//					workpermit_imgurl = imageUrl;
	//					workpermit_imgname = photoName;
	//					w_state = "ok";
	//					btnSubmit.setEnabled(true);
	//					break;
	//				}
	//			}
	//			@Override
	//			public void onSuccess(String imgUrl) {
	//				String imageUrl  = OssUpdateImgUtil.feedPicFilterUrl+imgUrl;
	//				String photoName = imgUrl.substring(imgUrl.lastIndexOf("/")+1);
	//
	//				switch (image_type)	{
	//				case 1:
	//					identity_imgurl = imageUrl;
	//					identity_imgname = photoName;
	//					i_state = "ok";
	//					System.err.println("保存身份证图片地址，名字" + i_state);
	//					btnSubmit.setEnabled(true);
	//					System.err.println("身份证：判断有上传图，设置可点击");
	//
	//					break;
	//				case 3:
	//					workpermit_imgurl = imageUrl;
	//					workpermit_imgname = photoName;
	//					w_state = "ok";
	//					btnSubmit.setEnabled(true);
	//					break;
	//				}
	//
	//			}
	//		});
	//		// 考虑在这里显示进度开始
	//		// 下面是isSucc成功的话，就关闭 progress bar；
	//	}


	//图片上传之后，回调获得图片的地址，图片的名字;当点击完成的时候，把这些信息都上传



	//	public void callbackUploadImg(boolean isSucc, String imgUrl, String fileName) {
	//		if (isSucc) {
	//			Log.e("isSucc", ""+isSucc);
	//			Log.e("imgUrl", ""+imgUrl);
	//			Log.e("fileName", ""+fileName);
	//			//上传完图片之后，要保存图片的URL和name
	//			if (isSucc ) {
	//				switch (image_type) {
	//				case 1:
	//					identity_imgurl = imgUrl;
	//					identity_imgname = fileName;
	//					i_state = "ok";
	//					System.err.println("保存身份证图片地址和名字，状态："+i_state);
	//					btnSubmit.setEnabled(true);
	//					System.err.println("身份证：判断有上传图，设置可点击");
	//					break;
	//				case 3:
	//					workpermit_imgurl = imgUrl;
	//					workpermit_imgname = fileName;
	//					w_state = "ok";
	//					System.err.println("保存工作证图片地址，名字"+w_state);
	//
	//
	//					if ("ok".equals(i_state) || "ok".equals(w_state)) {
	//						btnSubmit.setEnabled(true);
	//						System.err.println("onActivityResult判断有上传图，设置可点击");
	//					}else{
	//						btnSubmit.setEnabled(false);
	//						btnSubmit.setTextColor(instance.getResources().getColor(R.color.font_cell));
	//						System.err.println("onActivityResult不能点击");
	//					}
	//					break;
	//				}
	//			}
	//		}
	//
	//	};
	private PopWindowDeleteImage popWindowDeleteImage_inde;
	private PopWindowDeleteImage popWindowDeleteImage_work;

	private void checkState(){
		//判断3个图片是否上传，如果都没有，则屏蔽完成按钮
		if (!identity_type && !workpermit_type) {
			btnSubmit.setEnabled(false);
			//同时把完成的颜色变灰色
			btnSubmit.setTextColor(instance.getResources().getColor(R.color.font_cell));
		}else{
			btnSubmit.setEnabled(true);
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