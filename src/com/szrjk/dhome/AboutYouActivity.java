package com.szrjk.dhome;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.entity.BusiException;
import com.szrjk.entity.IImgUrlCallback;
import com.szrjk.entity.RegisterInfo;
import com.szrjk.fire.FireEye;
import com.szrjk.fire.Result;
import com.szrjk.fire.StaticPattern;
import com.szrjk.util.DDateUtils;
import com.szrjk.util.ToastUtils;
import com.szrjk.util.UploadPhotoUtils;
import com.szrjk.util.corpimageview.CropImageActivity;

@ContentView(R.layout.activity_aboutyou)
public class AboutYouActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "AboutYouActivity";

	@ViewInject(R.id.ll_about_you)
	private LinearLayout ll_about_you;
	@ViewInject(R.id.text_pdate)
	private EditText text_pdate;
	@ViewInject(R.id.text_sex)
	private EditText text_sex;
	@ViewInject(R.id.text_pname)
	private EditText text_pname;

	@ViewInject(R.id.img_reg1faceimg)
	private ImageView img_reg1faceimg;

	@ViewInject(R.id.btn_continue)
	private Button btn_continue;
	@ViewInject(R.id.lly_case_content)
	private LinearLayout lly_case_content;

	private RegisterInfo registerInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		ViewUtils.inject(this);
		addRegisterActivitys(this);
		initLayout();

		//
		DHomeApplication dHomeApplication = (DHomeApplication) getApplication();
		registerInfo = dHomeApplication.getRegisterInfo();
		// 默认用户图片
		registerInfo
				.setFaceUrl("http://dd-face.oss-cn-shenzhen.aliyuncs.com/face/icon_headportraitM.png");

	}

	private void initLayout() {
		text_pname.addTextChangedListener(mTextWatcher);
		text_pdate.addTextChangedListener(yTextWatcher);
		text_sex.addTextChangedListener(pTextWatcher);
		ll_about_you.setOnClickListener(this);
		text_pname.setOnClickListener(this);
		text_pdate.setOnClickListener(this);
		text_sex.setOnClickListener(this);
		btn_continue.setOnClickListener(this);
		img_reg1faceimg.setOnClickListener(this);

		// 点击出现弹出框，不弹出键盘
		text_pdate.setFocusable(false);
		text_sex.setFocusable(false);

		BitmapUtils bitmapUtils = new BitmapUtils(this);
		// 加载网络图片,默认图片
		bitmapUtils
				.display(img_reg1faceimg,
						"http://dd-face.oss-cn-shenzhen.aliyuncs.com/face/icon_headportraitM.png");
		// http://dd-face.oss-cn-shenzhen.aliyuncs.com/face/icon_headportraitM.png
		// 是否需要剪裁图片
		uploadPhotoUtils = new UploadPhotoUtils(AboutYouActivity.this, true);

	}

	TextWatcher mTextWatcher = new TextWatcher() {
		private CharSequence temp;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (temp.length() >= 1) {
				btn_continue.setEnabled(true);
				btn_continue.setFocusable(true);
			} else {
				btn_continue.setEnabled(false);
				btn_continue.setFocusable(false);
			}
			if (s.length() >= 8) {
				Toast.makeText(AboutYouActivity.this, "最多只能输入8个字符",
						Toast.LENGTH_LONG).show();
			}
		}
	};
	TextWatcher yTextWatcher = new TextWatcher() {
		private CharSequence temp;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (temp.length() >= 1) {
				btn_continue.setEnabled(true);
				btn_continue.setFocusable(true);
			} else {
				btn_continue.setEnabled(false);
				btn_continue.setFocusable(false);
			}
		}
	};
	
	TextWatcher pTextWatcher = new TextWatcher() {
		private CharSequence temp;
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			temp = s;
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (temp.length() >= 1) {
				btn_continue.setEnabled(true);
				btn_continue.setFocusable(true);
			} else {
				btn_continue.setEnabled(false);
				btn_continue.setFocusable(false);
			}
		}
	};

	@Override
	public void onClick(View view) {
		// 三个按钮都同一个动作，这个应该有更好的方式来处理的
		try {
			switch (view.getId()) {

			case R.id.ll_about_you:
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				text_pname.clearFocus();
				text_pname.setFocusable(false);
				break;
			case R.id.text_pname:
				text_pname.setFocusable(true);
				text_pname.setFocusableInTouchMode(true);
				text_pname.requestFocus();
				String tempName = text_pname.getText().toString();
				text_pname.setSelection(tempName.length());
				InputMethodManager inputManager = (InputMethodManager) text_pname
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(text_pname, 0);
				break;
			case R.id.text_pdate:
				// 点击日期弹出日期控件
				text_pname.clearFocus();
				text_pname.setFocusable(false);
				funcclickpdate();
				break;
			case R.id.text_sex:
				// 选择性别
				text_pname.clearFocus();
				text_pname.setFocusable(false);
				funcclickpsex();
				break;
			case R.id.btn_continue:
				funcclickcontinue();
				break;
			case R.id.img_reg1faceimg:
				InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm1.hideSoftInputFromWindow(view.getWindowToken(), 0);
				text_pname.clearFocus();
				text_pname.setFocusable(false);
				uploadPhotoUtils.popubphoto(img_reg1faceimg,
						new IImgUrlCallback() {
							@Override
							public void operImgUrl(String imgurl) {
								DHomeApplication dHomeApplication = (DHomeApplication) AboutYouActivity.this
										.getApplication();
								RegisterInfo registerInfo = dHomeApplication
										.getRegisterInfo();
								registerInfo.setFaceUrl(imgurl);
							}

							@Override
							public void operImgPic(Bitmap bitmap) {
								img_reg1faceimg.setImageBitmap(bitmap);
							}
						});

				break;
			}
		} catch (BusiException e) {
			ToastUtils.showMessage(this, e.getMessage());
		}
	}

	// private void popubphoto(){
	//
	// // uploadPhotoUtils.popubphoto(img_reg1faceimg);
	// }

	private void funcclickpdate() {
		DatePickerDialog d;
		// 取表单上的数据
		text_pdate.getText().toString();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR,1980);
		c.set(Calendar.MONTH,0);
		c.set(Calendar.DAY_OF_MONTH,1);
		try {
			c = DDateUtils.dformatStrToCalendar(
					text_pdate.getText().toString(), "yyyy年MM月dd日");
		} catch (Exception e) {
		}

		// 创建一个 日期控件
		d = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker datePicker, int year,
							int monthOfYear, int dayOfMonth) {
						text_pdate.setText(year + "年" + (monthOfYear + 1) + "月"
								+ dayOfMonth + "日");
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		DatePicker datePicker = d.getDatePicker();
		try {
			datePicker.setMinDate(new SimpleDateFormat("yyyy年MM月dd日").parse(
					"1900-01-01").getTime());
		} catch (ParseException e) {
		}
		datePicker.setMaxDate((new Date()).getTime());
		d.show();

	}

	/*
	 * @Override protected void onActivityResult(int requestCode, int
	 * resultCode, Intent data) { if (resultCode != RESULT_OK) { return; }
	 * switch (requestCode) { case UploadPhotoUtils.CAMERA_WITH_DATA: Bitmap
	 * bitmapCaputre = (Bitmap) data.getExtras().get("data");
	 * 
	 * // ImageView imageViewCaputre = addImageView(); //
	 * imageViewCaputre.setImageBitmap(bitmapCaputre); //
	 * imageViewCaputre.getLayoutParams().height=img_reg1faceimg.getHeight(); //
	 * imageViewCaputre.getLayoutParams().width=img_reg1faceimg.getWidth(); //
	 * imageViewCaputre.setOnClickListener(new OnClickListener() { // @Override
	 * // public void onClick(View view) { // popubphoto(); // } // }); ////
	 * imageViewCaputre.setPadding(10, 10, 10, 10); // //
	 * lly_case_content.removeAllViews(); //
	 * lly_case_content.addView(imageViewCaputre);
	 * img_reg1faceimg.setImageBitmap(bitmapCaputre); updateFile(bitmapCaputre);
	 * break; case UploadPhotoUtils.PHOTO_PICKED_WITH_DATA:
	 * 
	 * Uri selectedImage = data.getData(); String[] filePathColumn = {
	 * MediaStore.Images.Media.DATA };
	 * 
	 * Cursor cursor = getContentResolver().query(selectedImage, filePathColumn,
	 * null, null, null); cursor.moveToFirst();
	 * 
	 * int columnIndex = cursor.getColumnIndex(filePathColumn[0]); String
	 * picturePath = cursor.getString(columnIndex); cursor.close();
	 * 
	 * Bitmap b = BitmapFactory.decodeFile(picturePath); // ImageView imageView
	 * = addImageView(); // imageView.setImageBitmap(b); // //
	 * imageView.getLayoutParams().height=img_reg1faceimg.getHeight(); //
	 * imageView.getLayoutParams().width=img_reg1faceimg.getWidth(); //
	 * imageView.setOnClickListener(new OnClickListener() { // @Override //
	 * public void onClick(View view) { // popubphoto(); // } // }); // //
	 * lly_case_content.removeAllViews(); //
	 * lly_case_content.addView(imageView);
	 * 
	 * img_reg1faceimg.setImageBitmap(b); updateFile(b); break;
	 * 
	 * } }
	 */

	// /**
	// * 添加图片
	// *
	// * @return
	// */
	// private ImageView addImageView()
	// {
	// ImageView imageView = new ImageView(AboutYouActivity.this);
	// ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(150, 150);
	// imageView.setLayoutParams(params);
	// return imageView;
	// }

	private void funcclickpsex() throws BusiException {
		AlertDialog.Builder b = new AlertDialog.Builder(this);

		DHomeApplication dHomeApplication = (DHomeApplication) getApplication();
		String[] sexstrArr = dHomeApplication
				.getAdapterArr(DHomeApplication.constant_sex_val);
		b.setTitle("性别")
				.setSingleChoiceItems(sexstrArr,
						text_sex.getText().toString().equals("女") ? 1 : 0,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {
								BitmapUtils bitmapUtils = new BitmapUtils(
										AboutYouActivity.this);
								if (registerInfo
										.getFaceUrl()
										.equals("http://dd-face.oss-cn-shenzhen.aliyuncs.com/face/icon_headportraitM.png")
										|| registerInfo
												.getFaceUrl()
												.equals("http://dd-face.oss-cn-shenzhen.aliyuncs.com/face/icon_headportraitW.png")) {
									if (i == 0) {
										// 加载网络图片,默认图片
										bitmapUtils
												.display(img_reg1faceimg,
														"http://dd-face.oss-cn-shenzhen.aliyuncs.com/face/icon_headportraitM.png");
										registerInfo
												.setFaceUrl("http://dd-face.oss-cn-shenzhen.aliyuncs.com/face/icon_headportraitM.png");
									}
									if (i == 1) {
										// 加载网络图片,默认图片
										bitmapUtils
												.display(img_reg1faceimg,
														"http://dd-face.oss-cn-shenzhen.aliyuncs.com/face/icon_headportraitW.png");
										registerInfo
												.setFaceUrl("http://dd-face.oss-cn-shenzhen.aliyuncs.com/face/icon_headportraitW.png");
									}

								}
								if (i == 0) {
									text_sex.setText("男");
								}
								if (i == 1) {
									text_sex.setText("女");
								}
								if (i == 2) {
									String sex = text_sex.getText().toString();
									if (sex.equals("")) {
										text_sex.setHint("性别");
									}
								}

								dialogInterface.dismiss();
							}
						}).show();

	}

	private void funcclickcontinue() throws BusiException {
		FireEye fireEye = new FireEye(this);
		fireEye.add(text_pname, StaticPattern.Required.setMessage("请输入名称      "),
				StaticPattern.ONLYCHINESEORMIX);
//		fireEye.add(text_pdate, StaticPattern.Required.setMessage("请输入出生日期"));
//		fireEye.add(text_sex, StaticPattern.Required.setMessage("请输入性别"));

		Result result = fireEye.test();
		if (!result.passed) {
			return;
		}
		if (text_pdate.getText().toString().equals("")) {
			ToastUtils.showMessage(AboutYouActivity.this, "请输入出生日期");
			return;
		}
		if (text_sex.getText().toString().equals("")) {
			ToastUtils.showMessage(AboutYouActivity.this, "请输入性别");
			return;
		}

		// 校验和保存值
		DHomeApplication dHomeApplication = (DHomeApplication) getApplication();
		RegisterInfo registerInfo = dHomeApplication.getRegisterInfo();

		registerInfo.setUserName(text_pname.getText().toString());

		String srcBirthdate = text_pdate.getText().toString();
		try {
			srcBirthdate = DDateUtils.dformatOldstrToNewstr(srcBirthdate,
					"yyyy年MM月dd日", "yyyy-MM-dd");
		} catch (ParseException e) {
			ToastUtils.showMessage(this, "日期解析错误?");
		}
		registerInfo.setBirthdate(srcBirthdate);

		// 取性别
		String val = (String) dHomeApplication.getVal(
				DHomeApplication.constant_sex_val, text_sex.getText()
						.toString());
		registerInfo.setSex(val);

		// ToastUtils.showMessage(AboutYouActivity.this,
		// DjsonUtils.bean2Json(registerInfo));
		Intent i = new Intent(AboutYouActivity.this, AboutYou2Activity.class);
		startActivity(i);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		// 图片处理
		uploadPhotoUtils.imgOper(requestCode, resultCode, data);

		// switch(requestCode)
		// {
		// case UploadPhotoUtils.CAMERA_WITH_DATA:
		// Bitmap bitmapCaputre=(Bitmap)data.getExtras().get("data");
		// currentImageView.setImageBitmap(bitmapCaputre);
		// updateFile(bitmapCaputre);
		// break;
		// case UploadPhotoUtils.PHOTO_PICKED_WITH_DATA:
		// Uri selectedImage=data.getData();
		// String[]filePathColumn={MediaStore.Images.Media.DATA};
		// Cursor
		// cursor=getContentResolver().query(selectedImage,filePathColumn,null,null,null);
		// cursor.moveToFirst();
		// int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
		// String picturePath=cursor.getString(columnIndex);
		// cursor.close();
		// Bitmap b= BitmapFactory.decodeFile(picturePath);
		//
		// currentImageView.setImageBitmap(b);
		// updateFile(b);
		// break;
		// }
	}

	// /**上传阿里云后的回调**/
	// @Override
	// public void callbackUploadImg(final boolean isSucc, final String imgUrl,
	// final String fileName,String pathName){
	//
	// }
	@Override
	protected void onDestroy() {
		super.onDestroy();
		CropImageActivity.bp = null;
	}
}
