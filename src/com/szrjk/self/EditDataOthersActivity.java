package com.szrjk.self;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.config.Constant;
import com.szrjk.dhome.MainActivity;
import com.szrjk.dhome.R;
import com.szrjk.dhome.R.id;
import com.szrjk.dhome.R.layout;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.OtherHomePageInfo;
import com.szrjk.entity.TCity;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.index.VSelectActivity;
import com.szrjk.util.DDateUtils;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.util.ToastUtils;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
@ContentView(R.layout.activity_edit_data_others)
public class EditDataOthersActivity extends Activity {
	@ViewInject(R.id.sv_edit_others)
	private ScrollView sv_edit_other;
	@ViewInject(R.id.rl_editdata_others)
	private RelativeLayout rl_editdata;
	@ViewInject(R.id.rl_editall_others)
	private RelativeLayout rl_editall; 
	@ViewInject(R.id.tv_submit_others)
	private TextView tv_submit;//提交按钮
	@ViewInject(R.id.iv_avatar)
	private ImageView iv_avatar;//头像
	@ViewInject(R.id.tv_name)
	private TextView tv_name;//姓名
	@ViewInject(R.id.rl_type)
	private RelativeLayout rly_type;
	@ViewInject(R.id.tv_type)
	private TextView tv_type;//用户类型
	@ViewInject(R.id.tv_sex)
	private TextView tv_sex;//性别
	@ViewInject(R.id.tv_birthday)
	private TextView tv_birthday;//生日
	@ViewInject(R.id.rl_city)
	private RelativeLayout rl_city;
	@ViewInject(R.id.et_city)
	private TextView et_city;
	@ViewInject(R.id.et_company)
	private EditText et_company;
	@ViewInject(R.id.et_Editclass)
	private EditText et_editclass;
	private OtherHomePageInfo homePageInfo;
	private EditDataOthersActivity instance;
	TCity tCity = new TCity();
	private int Type  = InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_NORMAL;
	private int RESULT_VSElECTCITY = 101;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance=this;
		initLayout();
	}
	private void initLayout() {
		Intent intent = getIntent();
		homePageInfo = (OtherHomePageInfo) intent.getSerializableExtra(Constant.EDITINFO);
		Log.i("tag",homePageInfo.toString());
		try {
			setData();
		} catch (DbException e) {
			e.printStackTrace();
		}

	}
	private void setData() throws DbException {
		try {
			ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil(
					instance, homePageInfo.getUserFaceUrl(), iv_avatar,
					R.drawable.icon_headfailed, R.drawable.icon_headfailed);
			imageLoaderUtil.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		tv_name.setText(homePageInfo.getUserName());
		if(homePageInfo.getSex().equals("1")){
			tv_sex.setText("男");
		}else{
			tv_sex.setText("女");
		}
		tv_birthday.setText(homePageInfo.getBirthdate());
		tv_type.setText("其他");
		tCity.setCityCode(homePageInfo.getCityCode());
		tCity.setProvinceCode(homePageInfo.getProvince());
		tCity.setCityName((new TCity()).getCity(instance, homePageInfo.getCityCode()));
		tCity.setProvinceName((new TCity()).getProvince(instance, homePageInfo.getProvince()));
		et_city.setText((new TCity()).getProvince(instance, homePageInfo.getProvince())+" "+(new TCity()).getCity(instance, homePageInfo.getCityCode()));
		et_company.setText(homePageInfo.getCompanyName());
		if (homePageInfo.getJobTitle().equals("")) {
			et_editclass.setText("无职位");
		}else{
			et_editclass.setText(homePageInfo.getJobTitle());
		}
	}
	//提交更改
	@OnClick(R.id.tv_submit_others)
	public void submit(View v){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "dealCmUserBaseInfo");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", homePageInfo.getUserSeqId());
		busiParams.put("userName", homePageInfo.getUserName());
		busiParams.put("userType",homePageInfo.getUserType());
		busiParams.put("sex", homePageInfo.getSex());
		busiParams.put("birthday", homePageInfo.getBirthdate());
		busiParams.put("province", tCity.getProvinceCode());
		busiParams.put("cityCode", tCity.getCityCode());
		busiParams.put("companyName", et_company.getText().toString());
		busiParams.put("companyId","0");
		busiParams.put("deptName","");
		busiParams.put("deptId","0");
		busiParams.put("educationLevel","");
		busiParams.put("entrySchoolDate","");
		busiParams.put("mediaType","");
		busiParams.put("professionalTitle","");
		busiParams.put("jobTitle",et_editclass.getText().toString());
		busiParams.put("faceUrl",homePageInfo.getUserFaceUrl());
		busiParams.put("backgroundUrl",homePageInfo.getBackgroundUrl());
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj  =JSON.parseObject(
						jsonObject.getString("ErrorInfo"),ErrorInfo.class);
				if(Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					Toast.makeText(instance, "修改成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(instance, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(intent);
				}
			}
			
			public void start() {
				
			}
			
			public void loading(long total, long current, boolean isUploading) {
				
			}
			
			public void failure(HttpException exception, JSONObject jobj) {
				
			}
		});
	}
	//更改用户类型
	@OnClick(R.id.rl_type)
	public void changeType(View v){
		Toast.makeText(this, "更改类型未开放", Toast.LENGTH_SHORT).show();
	}
	
	//更换城市
	@OnClick(R.id.rl_city)
	public void changeCity(View v){
		Intent intent = new Intent(this, VSelectActivity.class);
		startActivityForResult(intent, RESULT_VSElECTCITY);

	}
	//更换公司
	private boolean companyIsFirst =true;
	@OnClick(R.id.et_company)
	public void changecompany(View v){
		if(companyIsFirst==true){
			InputMethodManager  imm = (InputMethodManager) 
					et_company.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			et_company.setFocusable(true);
			et_company.setFocusableInTouchMode(true);
			et_company.requestFocus();
			et_company.setInputType(Type);
			companyIsFirst =false;
		}
		et_company.setSelection(et_company.getText().toString().length());
		Handler handler = new Handler();
		handler.post(new Runnable() {  
			@Override  
			public void run() {  
				sv_edit_other.fullScroll(ScrollView.FOCUS_DOWN);  
			}  
		});
	}
	//更换职称
	private boolean jobIsFirst =true;
	@OnClick(R.id.et_Editclass)
	public void changeJobtitle(View v){
		if(jobIsFirst==true){
			InputMethodManager  imm = (InputMethodManager) 
					et_editclass.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			et_editclass.setFocusable(true);
			et_editclass.setFocusableInTouchMode(true);
			et_editclass.requestFocus();
			et_editclass.setInputType(Type);
			jobIsFirst =false;
		}
		et_editclass.setSelection(et_editclass.getText().toString().length());
		Handler handler = new Handler();
		handler.post(new Runnable() {  
			@Override  
			public void run() {  
				sv_edit_other.fullScroll(ScrollView.FOCUS_DOWN);  
			}  
		});
	}
	//获取回传的tcity
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==101&&resultCode==11) {
			if (data==null) {
//				Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
				return;
			}else{
				Bundle bundle = data.getExtras();
				Log.i("TAG", bundle.getString("pname")+" "+bundle.getString("cname"));
				et_city.setText(bundle.getString("pname")+" "+bundle.getString("cname"));

				tCity.setProvinceName(bundle.getString("pname"));
				tCity.setProvinceCode(bundle.getString("pcode"));
				tCity.setCityName(bundle.getString("cname"));
				tCity.setCityCode(bundle.getString("ccode"));
				et_city.setTag(tCity);
				return;
			}
		}
	}
	private boolean companyisFirst =true;
	@OnClick(R.id.et_company)
	public void changeCompany(View v){
		if (companyisFirst==true) {
			InputMethodManager  imm = (InputMethodManager) 
					et_company.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			et_company.setFocusable(true);
			et_company.setFocusableInTouchMode(true);
			et_company.requestFocus();
			companyisFirst =false;
		}	
		et_company.setSelection(et_company.getText().toString().length());
		Handler handler = new Handler();
		handler.post(new Runnable() {  
			@Override  
			public void run() {  
				sv_edit_other.fullScroll(ScrollView.FOCUS_DOWN);  
			}  
		});
	}
	private boolean ClassisFirst =true;
	@OnClick(R.id.et_Editclass)
	public void changeClass(View v){
		if (ClassisFirst==true) {
			InputMethodManager  imm = (InputMethodManager) 
					et_editclass.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			et_editclass.setFocusable(true);
			et_editclass.setFocusableInTouchMode(true);
			et_editclass.requestFocus();
			ClassisFirst =false;
		}	
		et_editclass.setSelection(et_editclass.getText().toString().length());
		Handler handler = new Handler();
		handler.post(new Runnable() {  
			@Override  
			public void run() {  
				sv_edit_other.fullScroll(ScrollView.FOCUS_DOWN);  
			}  
		});
	}

}
