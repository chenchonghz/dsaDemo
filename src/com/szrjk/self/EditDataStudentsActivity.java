package com.szrjk.self;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.OtherHomePageInfo;
import com.szrjk.entity.TCity;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.index.VSelectActivity;
import com.szrjk.index.VSelectEducationLevelActivity;
import com.szrjk.util.DDateUtils;
import com.szrjk.util.ImageLoaderUtil;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
@ContentView(R.layout.activity_edit_data_students)
public class EditDataStudentsActivity extends Activity {
	@ViewInject(R.id.sv_edit_students)
	private ScrollView sv_students;
	@ViewInject(R.id.rl_editdata_students)
	private RelativeLayout rl_editdata;
	@ViewInject(R.id.rl_editall_students)
	private RelativeLayout rl_editall; 
	@ViewInject(R.id.tv_submit_students)
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
	@ViewInject(R.id.rl_school)
	private RelativeLayout rl_school;
	@ViewInject(R.id.et_school)
	private EditText et_school;
	@ViewInject(R.id.rl_education)
	private RelativeLayout rl_education;
	@ViewInject(R.id.tv_education)
	private TextView tv_education;
	@ViewInject(R.id.rl_major)
	private RelativeLayout rl_major;
	@ViewInject(R.id.et_major)
	private EditText et_major;
	@ViewInject(R.id.rl_time)
	private RelativeLayout rl_time;
	@ViewInject(R.id.tv_time)
	private TextView tv_time;
	private int educationID;
	private OtherHomePageInfo homePageInfo;
	private EditDataStudentsActivity instance;
	private int RESULT_VSElECTCITY = 101;
	TCity tCity = new TCity();
	@Override
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
			// TODO Auto-generated catch block
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
		tv_type.setText("学生");
		tCity.setCityCode(homePageInfo.getCityCode());
		tCity.setProvinceCode(homePageInfo.getProvince());
		tCity.setCityName((new TCity()).getCity(instance, homePageInfo.getCityCode()));
		tCity.setProvinceName((new TCity()).getProvince(instance, homePageInfo.getProvince()));
		et_city.setText((new TCity()).getProvince(instance, homePageInfo.getProvince())+" "+(new TCity()).getCity(instance, homePageInfo.getCityCode()));
		et_school.setText(homePageInfo.getCompanyName());
		tv_education.setText(homePageInfo.getEducationLevel());
		et_major.setText(homePageInfo.getDeptName());
		tv_time.setText(homePageInfo.getEntrySchoolDate());


	}
	//提交更改
	@OnClick(R.id.tv_submit_students)
	public void submit(View v){
		if(TextUtils.isEmpty(et_school.getText().toString())){
			et_school.setError("请输入学校名称");
			return;
		}
		if(TextUtils.isEmpty(et_major.getText().toString())){
			et_major.setError("请输入专业名称");
			return;
		}
		PostSubmit();
	}
	private void PostSubmit() {
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
		busiParams.put("companyName", et_school.getText().toString());
		busiParams.put("companyId","0");
		busiParams.put("deptName",et_major.getText().toString());
		busiParams.put("deptId","0");
		busiParams.put("educationLevel",String.valueOf(educationID));
		String srcdate = tv_time.getText().toString();
		Log.i("TAG", srcdate);
		try{
			srcdate = DDateUtils.dformatOldstrToNewstr(srcdate,
					"yyyy年MM月dd日", "yyyy-MM-dd");
		}
		catch (ParseException e){
		}
		busiParams.put("entrySchoolDate",srcdate);
		busiParams.put("mediaType","");
		busiParams.put("professionalTitle","");
		busiParams.put("faceUrl",homePageInfo.getUserFaceUrl());
		busiParams.put("backgroundUrl",homePageInfo.getBackgroundUrl());
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			
			@Override
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
			
			@Override
			public void start() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void loading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	//更改类型
	@OnClick(R.id.rl_type)
	public void changeType(View v){
		Toast.makeText(this, "更改类型未开放", Toast.LENGTH_SHORT).show();
	}
	//更改城市
	@OnClick(R.id.rl_city)
	public void changeCity(View v){
		Intent intent = new Intent(this, VSelectActivity.class);
		startActivityForResult(intent, RESULT_VSElECTCITY);

	}
	//更改学校
	private boolean schoolIsFirst =true;
	private int Type  = InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_NORMAL;
	@OnClick(R.id.et_school)
	public void changeschool(View v){
		if(schoolIsFirst==true){
			InputMethodManager  imm = (InputMethodManager) 
					et_school.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			et_school.setFocusable(true);
			et_school.setFocusableInTouchMode(true);
			et_school.requestFocus();
			et_school.setInputType(Type);
			schoolIsFirst =false;
		}
		et_school.setSelection(et_school.getText().toString().length());
		Handler handler = new Handler();
		handler.post(new Runnable() {  
			@Override  
			public void run() {  
				sv_students.fullScroll(View.FOCUS_DOWN);  
			}  
		});
	}
	//更改专业
	private boolean majorIsFirst =true;
	@OnClick(R.id.et_major)
	public void changemajor(View v){
		if(majorIsFirst==true){
			InputMethodManager  imm = (InputMethodManager) 
					et_major.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			et_major.setFocusable(true);
			et_major.setFocusableInTouchMode(true);
			et_major.requestFocus();
			et_major.setInputType(Type);
			majorIsFirst =false;
		}
		et_major.setSelection(et_major.getText().toString().length());
		Handler handler = new Handler();
		handler.post(new Runnable() {  
			@Override  
			public void run() {  
				sv_students.fullScroll(View.FOCUS_DOWN);  
			}  
		});
	}
	//更改学历
	@OnClick(R.id.rl_education)
	public void changeEducation(View v){
		Intent i = new Intent(this, VSelectEducationLevelActivity.class);
		startActivityForResult(i,5);
	}
	//更改入学时间
	@OnClick(R.id.rl_time)
	public void changeTime(View v){
		DatePickerDialog d;
		// 取表单上的数据
		tv_time.getText().toString();
		Calendar c = Calendar.getInstance();
		try
		{
			c = DDateUtils.dformatStrToCalendar(
					tv_time.getText().toString(), "yyyy年MM月dd日");
		}
		catch (Exception e) {}

		//创建一个 日期控件
		d = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
		{
			@Override
			public void onDateSet(DatePicker datePicker, int year,
					int monthOfYear, int dayOfMonth)
			{
				tv_time.setText(year + "年" + (monthOfYear + 1) + "月"
						+ dayOfMonth + "日");
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
		c.get(Calendar.DAY_OF_MONTH));
		DatePicker datePicker=d.getDatePicker();
		try {
			datePicker.setMinDate(new SimpleDateFormat("yyyy年MM月dd日").parse("1900-01-01").getTime());
		} catch (ParseException e) {
		}
		datePicker.setMaxDate((new Date()).getTime());
		d.show();
	}
	//获取回传的tcity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==101&&resultCode==11) {
			if (data==null) {
//				Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
				return;
			}else{
//				Toast.makeText(this, "got", Toast.LENGTH_SHORT).show();
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
		if (requestCode==5) {
			if(data==null){
				return;
			}
			Bundle bundle1 = data.getExtras();
			educationID=bundle1.getInt("index");
			// 返回给界面
			tv_education.setText(bundle1.getString("level"));
		}
	}

}
