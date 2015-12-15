package com.szrjk.dhome;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.entity.BusiException;
import com.szrjk.entity.RegisterInfo;
import com.szrjk.entity.TCity;
import com.szrjk.entity.TDept;
import com.szrjk.entity.THostipal;
import com.szrjk.entity.TProfessionalTitle;
import com.szrjk.fire.FireEye;
import com.szrjk.fire.Result;
import com.szrjk.fire.StaticPattern;
import com.szrjk.index.VSelectActivity;
import com.szrjk.index.VSelectDepartmentActivity;
import com.szrjk.index.VSelectEducationLevelActivity;
import com.szrjk.index.VSelectProfessionalTitleActivity;
import com.szrjk.util.DCollectionUtils;
import com.szrjk.util.ToastUtils;

@ContentView(R.layout.activity_aboutyou2)
public class AboutYou2Activity extends BaseActivity implements OnClickListener
{

	final static int RESULT_VSElECTCITY = 1;
	final static int RESULT_VSEDUCATIONLEVEL = 0;
	final static int RESULT_DEPARTMENT = 2;
	public static final int TPROFESSIONALTITLE = 4;

	@ViewInject(R.id.text_parea)
	private EditText text_parea;

	@ViewInject(R.id.text_hospital)
	private AutoCompleteTextView text_hospital;

	@ViewInject(R.id.text_dept)
	private AutoCompleteTextView text_dept;

	@ViewInject(R.id.text_professionaltitle)
	private AutoCompleteTextView text_professionaltitle;

	@ViewInject(R.id.text_jobtitle)
	private EditText text_jobtitle;
	
	@ViewInject(R.id.btn_continue)
	private Button btn_continue;

	@ViewInject(R.id.text_school)
	private AutoCompleteTextView text_school;

	@ViewInject(R.id.text_major)
	private AutoCompleteTextView text_major;

	@ViewInject(R.id.text_edubg)
	private EditText text_edubg;
	
	private UIHandler mUiHandler;

	public String[] hnameArr;

	public String[] subDeptName;

	public String[] titleName;

	private String userType;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		ViewUtils.inject(this);
		addRegisterActivitys(this);
		initLayout();
		loadBigData();
	}

	private void loadBigData(){
		mUiHandler = new UIHandler(); //创建uihandler
		new LoadDataThread().start();  //创建线程并启动
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
					ArrayAdapter<String> av2 = new ArrayAdapter<String>(AboutYou2Activity.this,
							R.layout.item_auto_complete_textview,R.id.tv_text, hnameArr);
					text_hospital.setAdapter(av2);
			/*case TDEPT:
				subDeptName=(String[]) msg.obj;
					ArrayAdapter<String> av3 = new ArrayAdapter<String>(AboutYou2Activity.this,
							R.layout.item_auto_complete_textview,R.id.tv_text, subDeptName);
					text_dept.setAdapter(av3);*/
			/*case TPROFESSIONALTITLE:
				titleName=(String[]) msg.obj;
					ArrayAdapter<String> av4 = new ArrayAdapter<String>(AboutYou2Activity.this,
							R.layout.item_auto_complete_textview,R.id.tv_text, titleName);
					text_professionaltitle.setAdapter(av4);
				break;*/
			}
			
		}
	}


	private class LoadDataThread extends Thread{
		@Override
		public void  run(){
			//医院/单位的下拉
			List<THostipal> hostipals = (new THostipal()).fetchHostipalList(AboutYou2Activity.this);
			if (hostipals!=null) {
				String[] hostipalNames=DCollectionUtils.converFromList2(hostipals,"hospitalName");
				Message message1 = new Message();
				message1.what = UIHandler.THOSTIPAL;
				message1.obj=hostipalNames;
				mUiHandler.sendMessage(message1); //发送消息给Handler
			}
			
			// dept 的 下拉
			/*List<TDept> tDepts = (new TDept()).fetchAllList(AboutYou2Activity.this);
			if(tDepts!=null){
				String[] deptNames = DCollectionUtils.converFromList2(tDepts,
						"subDeptName");
			Message message2 = new Message();
			message2.what = UIHandler.TDEPT;
			message2.obj=deptNames;
			mUiHandler.sendMessage(message2); 
			}*/
			// profession 的 下拉
			/*List<TProfessionalTitle> tProfessions = (new TProfessionalTitle()).getListFromType(AboutYou2Activity.this, userType);
			if(tProfessions!=null){
				String[] titleNameArr = DCollectionUtils.converFromList2(tProfessions,
						"titleName");
			Message message3 = new Message();
			message3.what = UIHandler.TPROFESSIONALTITLE;
			message3.obj=titleNameArr;
			mUiHandler.sendMessage(message3);
			}*/
		}  
	}

	private void initLayout()
	{
		
		text_parea.addTextChangedListener(mTextWatcher);
		text_dept.addTextChangedListener(mTextWatcher);
		text_professionaltitle.addTextChangedListener(mTextWatcher);
		text_hospital.addTextChangedListener(mTextWatcher);
		text_school.addTextChangedListener(mTextWatcher);
		text_jobtitle.addTextChangedListener(yTextWatcher);
		text_major.addTextChangedListener(yTextWatcher);

		text_dept.setOnClickListener(this);
		text_hospital.setOnClickListener(this);
		text_edubg.setOnClickListener(this);
		text_parea.setOnClickListener(this);
		text_professionaltitle.setOnClickListener(this);
		btn_continue.setOnClickListener(this);

		text_parea.setFocusable(false);
		text_hospital.setFocusable(false);
		text_edubg.setFocusable(false);
		text_dept.setFocusable(false);
		text_professionaltitle.setFocusable(false);



		//不同的职称，不同的处理
		DHomeApplication dHomeApplication = (DHomeApplication) getApplication();
		userType = dHomeApplication.getRegisterInfo().getUserType();
		if("2".equals(userType)||"8".equals(userType)||"9".equals(userType)){
			//医生
			text_school.setVisibility(View.GONE);
			text_edubg.setVisibility(View.GONE);
			text_major.setVisibility(View.GONE);
			text_jobtitle.setVisibility(View.GONE);
		}else if("3".equals(userType)){
			//学生
			text_hospital.setVisibility(View.GONE);
			text_dept.setVisibility(View.GONE);
			text_professionaltitle.setVisibility(View.GONE);
			text_jobtitle.setVisibility(View.GONE);
		}else{
			//其它
			text_school.setVisibility(View.GONE);
			text_edubg.setVisibility(View.GONE);
			text_major.setVisibility(View.GONE);
			text_dept.setVisibility(View.GONE);
			text_professionaltitle.setVisibility(View.GONE);
			text_hospital.setHint("单位");
			
		}
	}

	TextWatcher mTextWatcher = new TextWatcher() {  
		private CharSequence temp;  
		@Override  
		public void onTextChanged(CharSequence s, int start, int before, int count) {  
			temp = s;  
		}  
		@Override  
		public void beforeTextChanged(CharSequence s, int start, int count,  
				int after) {  
		}  
		@Override  
		public void afterTextChanged(Editable s) {  
			if (temp.length()>=1) {
				btn_continue.setEnabled(true);
				btn_continue.setFocusable(true);
			}else {
				btn_continue.setEnabled(false);
				btn_continue.setFocusable(false);
			}  
			
			if (temp.length()==16) {
				ToastUtils.showMessage(AboutYou2Activity.this, "此选项所能输入的长度最大为16");
			}
		}  
	};  
	TextWatcher yTextWatcher = new TextWatcher() {  
		private CharSequence temp;  
		@Override  
		public void onTextChanged(CharSequence s, int start, int before, int count) {  
			temp = s;  
		}  
		@Override  
		public void beforeTextChanged(CharSequence s, int start, int count,  
				int after) {  
		}  
		@Override  
		public void afterTextChanged(Editable s) {  
			if (temp.length()>=1) {
				btn_continue.setEnabled(true);
				btn_continue.setFocusable(true);
			}else {
				btn_continue.setEnabled(false);
				btn_continue.setFocusable(false);
			}  
			
			if (temp.length()==6) {
				ToastUtils.showMessage(AboutYou2Activity.this, "此选项所能输入的长度最大为6");
			}
			
		}  
	};
	private int index;  
	@Override
	public void onClick(View view)
	{
		try
		{
			switch (view.getId())
			{
			case R.id.text_parea:
				text_hospital.clearFocus();
				text_hospital.setFocusable(false);
				// 地区点击
				funcclick1();
				break;
			case R.id.text_hospital:
				text_hospital.setFocusable(true);
				text_hospital.setFocusableInTouchMode(true);
				text_hospital.requestFocus();
				String tempName=text_hospital.getText().toString();
				text_hospital.setSelection(tempName.length());
				InputMethodManager inputManager =
   	                 (InputMethodManager)text_hospital.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
   	             inputManager.showSoftInput(text_hospital, 0);				
   	             // 医院点击
				
				funcclick2();
				break;
			case R.id.text_dept:
				text_hospital.clearFocus();
				text_hospital.setFocusable(false);
				// 科室点击
				funcclick3();
				break;
			case R.id.btn_continue:
				text_hospital.clearFocus();
				text_hospital.setFocusable(false);
				funcclick4();
				break;
			case R.id.text_professionaltitle:
				text_hospital.clearFocus();
				text_hospital.setFocusable(false);
				// 职称点击
				funcclick6();
				break;
			case R.id.text_edubg:
				// 学历点击
				funcclick5();
				break;
			}
		}
		catch (BusiException e)
		{
			ToastUtils.showMessage(this, e.getMessage());
		}
	}

	private void funcclick6() {
		Intent i = new Intent(AboutYou2Activity.this, VSelectProfessionalTitleActivity.class);
		i.putExtra("USERTYPE", userType);
		startActivityForResult(i,TPROFESSIONALTITLE);
	}

	private void funcclick5() {
		Intent i = new Intent(AboutYou2Activity.this, VSelectEducationLevelActivity.class);
		startActivityForResult(i,RESULT_VSEDUCATIONLEVEL);
	}

	private void funcclick1()
	{
		Intent i = new Intent(AboutYou2Activity.this, VSelectActivity.class);
		startActivityForResult(i, RESULT_VSElECTCITY);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		case RESULT_VSElECTCITY:
			if(data==null){
				break;
			}
			Bundle bundle = data.getExtras();
			// 返回给界面
			text_parea.setText(bundle.getString("pname")+" "+bundle.getString("cname"));
			TCity tCity = new TCity();
			tCity.setProvinceName(bundle.getString("pname"));
			tCity.setProvinceCode(bundle.getString("pcode"));
			tCity.setCityName(bundle.getString("cname"));
			tCity.setCityCode(bundle.getString("ccode"));
			text_parea.setTag(tCity);
			break;
		case RESULT_VSEDUCATIONLEVEL:
			if(data==null){
				break;
			}
			Bundle bundle1 = data.getExtras();
			index=bundle1.getInt("index");
			// 返回给界面
			text_edubg.setText(bundle1.getString("level"));
			break;
		case RESULT_DEPARTMENT:
			if(data==null){
				break;
			}
			Bundle bundle2 = data.getExtras();
			// 返回给界面
			text_dept.setText(bundle2.getString("deptName"));
			break;
		case TPROFESSIONALTITLE:
			if(data==null){
				break;
			}
			Bundle bundle3 = data.getExtras();
			// 返回给界面
			text_professionaltitle.setText(bundle3.getString("titleName"));
			break;
		}

	}

	private void funcclick2()
	{
	}

	private void funcclick3()
	{
		Intent i = new Intent(AboutYou2Activity.this, VSelectDepartmentActivity.class);
		startActivityForResult(i,RESULT_DEPARTMENT);
	}

	private void funcclick4() throws BusiException
	{
		FireEye fireEye = new FireEye(this);

		//不同的职称，不同的处理
		DHomeApplication dHomeApplication = (DHomeApplication) getApplication();
		RegisterInfo registerInfo = dHomeApplication.getRegisterInfo();
		String userType = registerInfo.getUserType();
		fireEye.add(text_parea, StaticPattern.Required.setMessage("请输入所在地"));
		//验证
		if("2".equals(userType)||"8".equals(userType)||"9".equals(userType)){
			//医生
			fireEye.add(text_hospital, StaticPattern.Required.setMessage("请输入医院"));
			fireEye.add(text_dept, StaticPattern.Required.setMessage("请输入科室"));
			fireEye.add(text_professionaltitle, StaticPattern.Required.setMessage("请输入职称"));
		}else if("3".equals(userType)){
			//学生
			fireEye.add(text_school, StaticPattern.Required.setMessage("请输入学校"));
			fireEye.add(text_edubg, StaticPattern.Required.setMessage("请输入学历"));
			fireEye.add(text_major, StaticPattern.Required.setMessage("请输入专业"));
		}else{
			//其它
			fireEye.add(text_hospital, StaticPattern.Required.setMessage("请输入单位"));
			fireEye.add(text_jobtitle, StaticPattern.Required.setMessage("请输入职位"));
		}



		Result result = fireEye.test();
		if (!result.passed) { return; }

		// 地区
		TCity ptcity = (TCity) text_parea.getTag();
		String hospitalstr = text_hospital.getText().toString();
		String hospitalval = (new THostipal()).getKeyFromName(this, hospitalstr);
		String deptstr = text_dept.getText().toString();
		String deptval = (new TDept()).getKeyFromName(this, deptstr);
		// 职称
		String professionalTitlestr = text_professionaltitle.getText().toString();
		String professionalTitleval=new TProfessionalTitle().getKeyFromName(AboutYou2Activity.this, professionalTitlestr);
		String school = text_school.getText().toString();
		String educationLev=text_edubg.getText().toString();
		String major = text_major.getText().toString();
		//职位
		String jobTitle=text_jobtitle.getText().toString();

		//提交时
		if("2".equals(userType)||"8".equals(userType)||"9".equals(userType)){
			//医生
			if (professionalTitleval.equals("")) {
				professionalTitleval="0";
			}
			registerInfo.setProfessionalTitle(professionalTitleval);
			if (deptval.equals("")) {
				deptval="0";
			}
			registerInfo.setDeptId(deptval);
			registerInfo.setDeptName(deptstr);
			if (hospitalval.equals("")) {
				hospitalval="0";
			}
			registerInfo.setCompanyId(hospitalval);
			registerInfo.setCompanyName(hospitalstr);
		}else if("3".equals(userType)){
			//学生
			registerInfo.setCompanyName(school); 
			registerInfo.setEducationLev(index);
			registerInfo.setDeptName(major);

		}else{
			//其它
			registerInfo.setCompanyName(hospitalstr);
			registerInfo.setJobTitle(jobTitle);
			registerInfo.setDeptId("0");
			registerInfo.setDeptName("");
		}

		// 地区的处理
		registerInfo.setProvince(ptcity.getProvinceCode());
		registerInfo.setCity(ptcity.getCityCode());

		Intent i = new Intent(AboutYou2Activity.this,
				ProtectAccountActivity.class);
		startActivity(i);
	}
}
