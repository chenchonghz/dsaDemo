package com.szrjk.self;

import java.util.HashMap;
import java.util.List;
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
import com.szrjk.dhome.AboutYou2Activity;
import com.szrjk.dhome.MainActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.OtherHomePageInfo;
import com.szrjk.entity.TCity;
import com.szrjk.entity.TDept;
import com.szrjk.entity.THostipal;
import com.szrjk.entity.TProfessionalTitle;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.index.VSelectActivity;
import com.szrjk.index.VSelectDepartmentActivity;
import com.szrjk.index.VSelectProfessionalTitleActivity;
import com.szrjk.util.DCollectionUtils;
import com.szrjk.util.ImageLoaderUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_edit_data_doctors)
public class EditDataDoctorsActivity extends Activity {
	@ViewInject(R.id.rl_editdata_doctor)
	private RelativeLayout rl_editdata;
	@ViewInject(R.id.rl_editall_doctor)
	private RelativeLayout rl_editall; 
	@ViewInject(R.id.sv_edit_doctor)
	private ScrollView sv_edit_doctor;
	@ViewInject(R.id.tv_submit_doctor)
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
	@ViewInject(R.id.et_hospital)
	private AutoCompleteTextView et_hospital;
	@ViewInject(R.id.et_dept)
	private AutoCompleteTextView et_dept;
	@ViewInject(R.id.et_ptitle)
	private AutoCompleteTextView et_ptitle;

	private OtherHomePageInfo homePageInfo;
	private EditDataDoctorsActivity instance;
	private int RESULT_VSElECTCITY = 101;
	private UIHandler mUiHandler;

	public String[] hnameArr;

	public String[] subDeptName;

	public String[] titleName;
	private int Type  = InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_NORMAL;
	TCity tCity = new TCity();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		//加载列表数据
		loadBigData();
		initLayout();
		initListener();
	}
	private String ptitle ;
	private void initListener() {
		//		et_hospital.addTextChangedListener(mTextWatcher);
		//尝试添加 焦点转移监听
		//		et_ptitle.setOnFocusChangeListener(new OnFocusChangeListener() {
		//			
		//			@Override
		//			public void onFocusChange(View arg0, boolean arg1) {
		//				et_ptitle.setText(ptitle);
		//			}
		//		});
		//		et_ptitle.addTextChangedListener(new TextWatcher() {
		//			private CharSequence temp;
		//			private int count;
		//			public void onTextChanged(CharSequence string, int arg1, int arg2, int arg3) {
		//				count = arg3;
		//			}
		//
		//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
		//					int arg3) {
		//			}
		//
		//			public void afterTextChanged(Editable arg0) {
		//				if(count ==0){
		//					et_ptitle.showDropDown();
		//					ArrayAdapter<String >a =(ArrayAdapter<String>) et_ptitle.getAdapter();
		//					if (a!=null) {
		//						ptitle =a.getItem(0);
		//						Log.i("TAG", ptitle);
		//					}
		//
		//				}
		//			}
		//		});
		et_ptitle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				ListView listView = (ListView) parent;
				ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
				TextView textView = (TextView) view;
				et_ptitle.setFocusable(false);
				et_ptitle.setFocusableInTouchMode(false);
				et_ptitle.requestFocus();
				ptitleisFirst = true;
			}
		});
		et_dept.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				ListView listView = (ListView) parent;
				ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
				TextView textView = (TextView) view;
				et_dept.setFocusable(false);
				et_dept.setFocusableInTouchMode(false);
				et_dept.requestFocus();
				deptisFirst = true;
			}
		});
		et_hospital.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				ListView listView = (ListView) parent;
				ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
				TextView textView = (TextView) view;
				et_hospital.setFocusable(false);
				et_hospital.setFocusableInTouchMode(false);
				et_hospital.requestFocus();
				hospitalisFirst = true;
			}
		});

	}
	private void loadBigData() {
		mUiHandler = new UIHandler(); //创建uihandler
		new LoadDataThread().start();  //创建线程并启动
	}

	private class LoadDataThread extends Thread{
		@Override
		public void  run(){
			//医院/单位的下拉
			List<THostipal> hostipals = (new THostipal()).fetchHostipalList(instance);
			if (hostipals!=null) {
				String[] hostipalNames=DCollectionUtils.converFromList2(hostipals,"hospitalName");
				Message message1 = new Message();
				message1.what = UIHandler.THOSTIPAL;
				message1.obj=hostipalNames;
				mUiHandler.sendMessage(message1); //发送消息给Handler
			}

			// dept 的 下拉
			List<TDept> tDepts = (new TDept()).fetchAllList(instance);
			if(tDepts!=null){
				String[] deptNames = DCollectionUtils.converFromList2(tDepts,
						"subDeptName");
				Message message2 = new Message();
				message2.what = UIHandler.TDEPT;
				message2.obj=deptNames;
				mUiHandler.sendMessage(message2); //发送消息给Handler
			}
			// profession 的 下拉
			List<TProfessionalTitle> tProfessions = (new TProfessionalTitle()).getListFromType(instance, homePageInfo.getUserType());
			if(tProfessions!=null){
				String[] titleNameArr = DCollectionUtils.converFromList2(tProfessions,
						"titleName");
				Message message3 = new Message();
				message3.what = UIHandler.TPROFESSIONALTITLE;
				message3.obj=titleNameArr;
				mUiHandler.sendMessage(message3);
			}
		}  
	}

	private class UIHandler  extends Handler {
		static final int THOSTIPAL = 0;    //定义消息类型
		static final int TDEPT = 1;
		static final int TPROFESSIONALTITLE = 2;
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case THOSTIPAL:
				//这里对数据进行显示或做相应的处理
				hnameArr=(String[]) msg.obj;
				ArrayAdapter<String> av2 = new ArrayAdapter<String>(instance,
						R.layout.item_textview, hnameArr);
				et_hospital.setAdapter(av2);
			case TDEPT:
				subDeptName=(String[]) msg.obj;
				ArrayAdapter<String> av3 = new ArrayAdapter<String>(instance,
						R.layout.item_textview, subDeptName);
				et_dept.setAdapter(av3);
			case TPROFESSIONALTITLE:
				titleName=(String[]) msg.obj;
				ArrayAdapter<String> av4 = new ArrayAdapter<String>(instance,
						R.layout.item_textview, titleName);
				et_ptitle.setAdapter(av4);
				break;
			}

		}
	}

	private void initLayout() 
	{
		Intent intent = getIntent();
		homePageInfo = (OtherHomePageInfo) intent.getSerializableExtra(Constant.EDITINFO);
		Log.i("tag",homePageInfo.toString());
		try {
			setData();
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	//设置数据
	private void setData() throws DbException {
		try {
			ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil(
					instance, homePageInfo.getUserFaceUrl(), iv_avatar, R.drawable.icon_headfailed,R.drawable.icon_headfailed);
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
		String type = homePageInfo.getUserType();
		getType(type);
		tCity.setCityCode(homePageInfo.getCityCode());
		tCity.setProvinceCode(homePageInfo.getProvince());
		tCity.setCityName((new TCity()).getCity(instance, homePageInfo.getCityCode()));
		tCity.setProvinceName((new TCity()).getProvince(instance, homePageInfo.getProvince()));
		et_city.setText((new TCity()).getProvince(instance, homePageInfo.getProvince())+" "+(new TCity()).getCity(instance, homePageInfo.getCityCode()));

		et_hospital.setText(homePageInfo.getCompanyName());
		et_dept.setText(homePageInfo.getDeptName());
		et_ptitle.setText(homePageInfo.getProfessionalTitle());
	}
	private void getType(String type) {
		switch (Integer.valueOf(type)) {
		case 0: tv_type.setText("未知");  break;
		case 1: tv_type.setText("管理员");  break;
		case 2: tv_type.setText("医生");  break;
		case 3: tv_type.setText("学生");  break;
		case 4: tv_type.setText("药企从业人员");  break;
		case 5: tv_type.setText("媒体从业人员");  break;
		case 6: tv_type.setText("团体");  break;
		case 7: tv_type.setText("其他");  break;
		case 8: tv_type.setText("护士");  break;
		case 9: tv_type.setText("药剂师");  break;
		}
	}

	@OnClick(R.id.tv_submit_doctor)
	public void submit(View v){
		if(TextUtils.isEmpty(et_hospital.getText().toString())){
			et_hospital.setError("请输入医院名称");
			return;
		}
		DoSubmit();
	}
	//提交修改报文
	private void DoSubmit() {
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
		busiParams.put("companyName", et_hospital.getText().toString());
		busiParams.put("companyId",(new THostipal()).getKeyFromName(instance, et_hospital.getText().toString()));
		busiParams.put("deptName",et_dept.getText().toString());
		if ((new TDept()).getKeyFromName(instance, et_dept.getText().toString()).equals("")) {
			Toast.makeText(instance, "科室输入错误", Toast.LENGTH_SHORT).show();
			return;
		}else{
			busiParams.put("deptId",(new TDept()).getKeyFromName(instance, et_dept.getText().toString()));
		}
		busiParams.put("educationLevel","");
		busiParams.put("entrySchoolDate","");
		busiParams.put("mediaType","");
		if ((new TProfessionalTitle()).getKeyFromName(instance, et_ptitle.getText().toString()).equals("")) {
			Toast.makeText(instance, "职称输入错误", Toast.LENGTH_SHORT).show();
			return;
		}else{
			busiParams.put("professionalTitle",(new TProfessionalTitle()).getKeyFromName(instance, et_ptitle.getText().toString()));
		}
		busiParams.put("faceUrl",homePageInfo.getUserFaceUrl());
		busiParams.put("backgroundUrl",homePageInfo.getBackgroundUrl());
		paramMap.put("BusiParams", busiParams);
		Log.i("TAG", paramMap.toString());
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
	@OnClick(R.id.rl_type)
	public void changeType(View v){
		Toast.makeText(this, "更改类型未开放", Toast.LENGTH_SHORT).show();
	}
	@OnClick(R.id.rl_city)
	public void changeCity(View v){
		Intent intent = new Intent(this, VSelectActivity.class);
		startActivityForResult(intent, RESULT_VSElECTCITY);

	}
	private boolean hospitalisFirst =true;
	@OnClick(R.id.et_hospital)
	public void changeHospital(View v){
//		Toast.makeText(this, ""+et_hospital.isFocusable(), Toast.LENGTH_SHORT).show();
		if(hospitalisFirst==true){
			InputMethodManager  imm = (InputMethodManager) 
					et_hospital.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			et_hospital.setFocusable(true);
			et_hospital.setFocusableInTouchMode(true);
			et_hospital.requestFocus();
			et_hospital.setInputType(Type);
			hospitalisFirst = false;
		}
		et_hospital.showDropDown();
		et_hospital.setSelection(et_hospital.getText().toString().length());
		Handler handler = new Handler();
		handler.post(new Runnable() {  
			@Override  
			public void run() {  
				sv_edit_doctor.fullScroll(ScrollView.FOCUS_DOWN);  
			}  
		});
	}
	private boolean deptisFirst =true;
	@OnClick(R.id.et_dept)
	public void changeDepartment(View v){
		Intent i = new Intent(this, VSelectDepartmentActivity.class);
		startActivityForResult(i,2);
	}
	private boolean ptitleisFirst =true;
	@OnClick(R.id.et_ptitle)
	public void changepTitle(View v){
		Intent i = new Intent(this, VSelectProfessionalTitleActivity.class);
		i.putExtra("USERTYPE", homePageInfo.getUserType());
		startActivityForResult(i,3);
	}
	//获取回传的tcity
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
		if (requestCode==2) {
			if(data==null){
				return;
			}
			Bundle bundle2 = data.getExtras();
			et_dept.setText(bundle2.getString("deptName"));
		}
		if (requestCode==3) {
			if(data==null){
				return;
			}
			Bundle bundle3 = data.getExtras();
			// 返回给界面
			et_ptitle.setText(bundle3.getString("titleName"));
		}
	}
	public static void scrollToBottom(final View scroll, final View inner) {  

		Handler mHandler = new Handler();  

		mHandler.post(new Runnable() {  
			public void run() {  
				if (scroll == null || inner == null) {  
					return;  
				}  
				int offset = inner.getMeasuredHeight() - scroll.getHeight();  
				if (offset < 0) {  
					offset = 0;  
				}  

				scroll.scrollTo(0, offset);  
			}  
		});  
	}
}
