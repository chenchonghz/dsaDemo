package com.szrjk.dhome;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.entity.RegisterInfo;
import com.szrjk.widget.HeaderView;

@ContentView(R.layout.activity_register1)
public class Register1Activity extends BaseActivity implements OnClickListener
{

	final static String TAG = "Register1Activity";
	@ViewInject(R.id.hv_register)
	private HeaderView hv_register;
	@ViewInject(R.id.btn_doctor)
	private Button btn_doctor;

	@ViewInject(R.id.btn_student)
	private Button btn_student;
	@ViewInject(R.id.btn_other)
	private Button btn_other;

	@ViewInject(R.id.textView3)
	private TextView textView3;

	@ViewInject(R.id.btn_pharmacist)
	private Button btn_pharmacist;

	@ViewInject(R.id.btn_nurse)
	private Button btn_nurse;

	private LinearLayout lly_hv;
	private Register1Activity instance;
	private boolean IsVisitor;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		Intent intent = getIntent();
		IsVisitor = intent.getBooleanExtra("Visitor",false);
		addRegisterActivitys(this);
		initLayout();
	}


	private void initLayout()
	{
		textView3.setText(getClickableSpan());
		//设置该句使文本的超连接起作用
		textView3.setMovementMethod(LinkMovementMethod.getInstance());
		lly_hv = hv_register.getLLy();
		btn_doctor.setOnClickListener(this);
		btn_student.setOnClickListener(this);
		btn_other.setOnClickListener(this);
		btn_nurse.setOnClickListener(this);
		btn_pharmacist.setOnClickListener(this);
		lly_hv.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				goLogin();
			}
		});
	}


	@Override
	public void onClick(View view) {
		switch (view.getId())
		{
			case R.id.btn_doctor:
				funcblick(2,btn_doctor);
				break;
			case R.id.btn_student:
				funcblick(3,btn_student);
				break;
			case R.id.btn_other:
				funcblick(7,btn_other);
				break;
			case R.id.btn_pharmacist:
				funcblick(9,btn_pharmacist);
				break;
			case R.id.btn_nurse:
				funcblick(8,btn_nurse);
				break;
		}
	}

	private void funcblick(int userType,Button button){
		//保存变量
		DHomeApplication dHomeApplication = (DHomeApplication) getApplication();
		RegisterInfo registerInfo = dHomeApplication.getRegisterInfo();
		registerInfo.setUserType(userType+"");
		
		button.setPressed(true);
		//跳转 TODO
		Intent intent2 = new Intent(Register1Activity.this,Register2Activity.class);
		startActivity(intent2);
	}

	 @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goLogin();
		}
		return false;
	}
	//设置超链接文字
	private SpannableString getClickableSpan(){
		int link1start = 22;
		int link1stop = 34;
		
		int link2start=35;
		int link2stop=41;
		
		int link3start=42;
		int link3stop=48;

		SpannableString spanStr = new SpannableString("感谢您使用数字医生，注册即表明您已阅读并同意《数字医生平台服务协议》、《法律声明》、《版权声明》，在使用过程中您可能会收到来自数字医生的短信通知。");
		//设置下划线文字
		//spanStr.setSpan(new UnderlineSpan(), link1start, link1stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		//设置文字的单击事件
		spanStr.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				//TODO
				startActivity(new Intent(Register1Activity.this, DigitalDoctorPlatformServiceContractActivity.class));
			}
			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(false);
			}
		}, link1start, link1stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanStr.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				//TODO
				startActivity(new Intent(Register1Activity.this, DigitalDoctorTermsActivity.class));
			}
			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(false);
			}
		}, link2start, link2stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanStr.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				//TODO
				startActivity(new Intent(Register1Activity.this, CopyrightNoticeActivity.class));
			}
			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(false);
			}
		}, link3start, link3stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		//设置文字的前景色
		spanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.search_bg)), link1start, link1stop,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.search_bg)), link2start, link2stop,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.search_bg)), link3start, link3stop,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		 //设置字体背景色  
		spanStr.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.base_bg)), link1start, link1stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanStr.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.base_bg)), link2start, link2stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanStr.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.base_bg)), link3start, link3stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanStr;
	}
	private void goLogin(){
		if (IsVisitor) {
			Intent intent = new Intent(instance, LoginActivity.class);
			startActivity(intent);
			instance.finish();
		}else{
			instance.finish();
		}
	}
}
