package com.szrjk.self;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.OtherHomePageInfo;
import com.szrjk.entity.TCity;
import com.szrjk.index.VSelectActivity;
import com.szrjk.util.ImageLoaderUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
/**
 * 
 * @author 郑斯铭
 *	编辑资料界面
 */
@ContentView(R.layout.activity_edit_data)
public class EditDataActivity extends Activity implements OnClickListener{
	@ViewInject(R.id.rl_editdata_students)
	private RelativeLayout rl_editdata;
	@ViewInject(R.id.rl_editall)
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
	//医生特有部分
	@ViewInject(R.id.rl_editdoctors)
	private RelativeLayout rl_editdoctors;
	@ViewInject(R.id.rl_editstudent)
	private RelativeLayout rl_editstudent;
	@ViewInject(R.id.rl_editothers)
	private RelativeLayout rl_editothers;
	@ViewInject(R.id.sc_edit_students)
	private ScrollView sv_edit;
	@ViewInject(R.id.rl_hospital)
	private RelativeLayout rl_hospital;
	@ViewInject(R.id.et_hospital)
	private AutoCompleteTextView et_hospital;
	@ViewInject(R.id.rl_ptitle)
	private RelativeLayout rl_ptitle;
	@ViewInject(R.id.tv_ptitle)
	private EditText et_ptitle;
	
	@ViewInject(R.id.v_doctor)
	private View v_doctor;
	private OtherHomePageInfo homePageInfo;
	final static int RESULT_VSElECTCITY = 12;
	private EditDataActivity instance;
//	private InputMethodManager imm = null;
	//	private InputMethodManager imm = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		
		//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		//获取intent
		initLayout();
		//设置监听器
		initListener();

	}
	//获取跳转intent
	private void initLayout() {
		Intent intent = getIntent();
		homePageInfo = (OtherHomePageInfo) intent.getSerializableExtra(Constant.EDITINFO);
		Log.i("TAG", homePageInfo.toString());
		setData();
	}
	//放置用户资料
	private void setData() {
		try {
			ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil(
					this, homePageInfo.getUserFaceUrl(), iv_avatar, R.drawable.icon_headfailed,R.drawable.icon_headfailed);
			imageLoaderUtil.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		tv_name.setText(homePageInfo.getUserName());
		tv_sex.setText(homePageInfo.getSex());
		tv_birthday.setText(homePageInfo.getBirthdate());
		String type = homePageInfo.getUserType();
		et_city.setText(homePageInfo.getProvince()+" "+homePageInfo.getCityCode());
		getType(type);
		initEditdata(type);
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
	//设置监听器
	private void initListener() {
		tv_submit.setOnClickListener(this);
		rly_type.setOnClickListener(this);
		rl_city.setOnClickListener(this);
		rl_hospital.setOnClickListener(this);
		rl_ptitle.setOnClickListener(this);
		//		rl_hospital.setOnClickListener(this);

	}

	//根据用户类型判断可编辑界面显示
	private void initEditdata(String type) {
		switch (Integer.valueOf(type)) {
		case 2:
			rl_editdoctors.setVisibility(View.VISIBLE);
			setDoctorsListener();
			break;
		case 3:
			rl_editstudent.setVisibility(View.VISIBLE);
			break;
		case 7:
			rl_editothers.setVisibility(View.VISIBLE);
			break;

		}
	}





	private void setDoctorsListener() {
		

	}
	//通用点击方法
	@Override
	public void onClick(View view) {
		//输入框输入方法
		int Type  = InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_NORMAL;
		//输入法弹出

		//		sv_edit.fullScroll(ScrollView.FOCUS_DOWN);
				Handler handler = new Handler();
				handler.post(new Runnable() {  
				    @Override  
				    public void run() {  
				        sv_edit.fullScroll(View.FOCUS_DOWN);  
				    }  
				});
		switch (view.getId()) {
		case R.id.tv_submit_students:
			Toast.makeText(this, "提交未开放", Toast.LENGTH_SHORT).show();
			break;
		case R.id.rl_type:
			Toast.makeText(this, "更改类型未开放", Toast.LENGTH_SHORT).show();
			break;
		case R.id.rl_city:
			Intent intent = new Intent(this, VSelectActivity.class);
			startActivityForResult(intent, RESULT_VSElECTCITY);
			break;
		case R.id.rl_hospital:
			Toast.makeText(this, "按了一下医院", Toast.LENGTH_SHORT).show();
			InputMethodManager  imm = (InputMethodManager) 
					et_hospital.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			et_hospital.setFocusable(true);
			et_hospital.setFocusableInTouchMode(true);
			et_hospital.requestFocus();
			et_hospital.setInputType(Type);
			sv_edit.fullScroll(View.FOCUS_DOWN);
			break;
		case R.id.rl_ptitle:
			Toast.makeText(this, "按了一下职称", Toast.LENGTH_SHORT).show();
			 InputMethodManager imm2 = (InputMethodManager) 
					et_ptitle.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm2.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			et_ptitle.setFocusable(true);
			et_ptitle.setFocusableInTouchMode(true);
			et_ptitle.requestFocus();
			et_ptitle.setInputType(Type);
			sv_edit.fullScroll(View.FOCUS_DOWN);
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (requestCode==12&&resultCode==11) {
			if (data==null) {
				Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
				return;
			}else{
				Toast.makeText(this, "got", Toast.LENGTH_SHORT).show();
				Bundle bundle = data.getExtras();
				Log.i("TAG", bundle.getString("pname")+" "+bundle.getString("cname"));
				et_city.setText(bundle.getString("pname")+" "+bundle.getString("cname"));
				TCity tCity = new TCity();
				tCity.setProvinceName(bundle.getString("pname"));
				tCity.setProvinceCode(bundle.getString("pcode"));
				tCity.setCityName(bundle.getString("cname"));
				tCity.setCityCode(bundle.getString("ccode"));
				et_city.setTag(tCity);
				return;
			}

		}

	}


}
