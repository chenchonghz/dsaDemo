package com.szrjk.dhome;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.config.Constant;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.OtherHomePageInfo;
import com.szrjk.entity.TCity;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.self.EditDataDoctorsActivity;
import com.szrjk.self.EditDataOthersActivity;
import com.szrjk.self.EditDataStudentsActivity;
import com.szrjk.self.UserAvatarImageChangerActivity;
import com.szrjk.util.DDateUtils;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.util.ToastUtils;

/**
 * 
 * @author ldr
 *
 */
@ContentView(R.layout.activity_introduce)
public class IntroduceActivity extends BaseActivity {
	private IntroduceActivity instance;
	@ViewInject(R.id.iv_other_user_background)
	private ImageView iv_other_user_background;

	@ViewInject(R.id.iv_self_avatar)
	private ImageView iv_self_avatar;


	@ViewInject(R.id.iv_back)
	private ImageView iv_back; //返回

	@ViewInject(R.id.tv_edit)
	private TextView tv_edit; //编辑资料

	@ViewInject(R.id.tv_name)
	private TextView tv_name; //名字

	@ViewInject(R.id.tv_sex)
	private TextView tv_sex; //sex

	@ViewInject(R.id.tv_age)
	private TextView tv_age; //age

	@ViewInject(R.id.tv_hospital)
	private TextView tv_hospital; //医院 

	@ViewInject(R.id.tv_departments)
	private TextView tv_departments; //科室

	@ViewInject(R.id.tv_professional_title)
	private TextView tv_professional_title; //职称

	@ViewInject(R.id.tv_school)
	private TextView tv_school; //学校

	@ViewInject(R.id.tv_education)
	private TextView tv_education; //学历 

	@ViewInject(R.id.tv_address)
	private TextView tv_address; //地址 

	@ViewInject(R.id.tv_professional)
	private TextView tv_professional; //专业

	@ViewInject(R.id.tv_admission_date)
	private TextView tv_admission_date; //入学时间 

	@ViewInject(R.id.tv_company)
	private TextView tv_company; //公司

	@ViewInject(R.id.tv_job_title)
	private TextView tv_job_title; //职位

	@ViewInject(R.id.rl_hospital)
	private RelativeLayout rl_hospital;//医院

	@ViewInject(R.id.rl_departments)
	private RelativeLayout rl_departments;//科室

	@ViewInject(R.id.rl_professional_title)
	private RelativeLayout rl_professional_title;//职称

	@ViewInject(R.id.rl_school)
	private RelativeLayout rl_school;//学校

	@ViewInject(R.id.rl_education)
	private RelativeLayout rl_education;//学历

	@ViewInject(R.id.rl_professional)
	private RelativeLayout rl_professional;//专业

	@ViewInject(R.id.rl_admission_date)
	private RelativeLayout rl_admission_date;//入学时间

	@ViewInject(R.id.rl_company)
	private RelativeLayout rl_company;//单位

	@ViewInject(R.id.rl_job_title)
	private RelativeLayout rl_job_title;//工作职位

	private String objID;

	private UserInfo userInfo;

	private String userId;
	private OtherHomePageInfo homePageInfo;

	@ViewInject(R.id.rl_glo)
	private RelativeLayout rl_glo;
	private Dialog progressDialog;
	//是否更改过
	private boolean ischange = false;
	//是否从selfactivity过来
	private boolean fromself = false;
	protected void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		initLayout();
	}

	private void initLayout() {
		Intent intent = instance.getIntent();
		fromself = intent.getBooleanExtra("self", false);
		objID = intent.getStringExtra(Constant.USER_SEQ_ID);
		userInfo = Constant.userInfo;
		userId = userInfo.getUserSeqId();
		rl_glo.setVisibility(View.INVISIBLE);//如果未加载；先隐藏
		//如果是自己。那就可以编辑自己的资料。不是
		if (userId.equals(objID)) {
			findUserInfo(userId);
			tv_edit.setVisibility(View.VISIBLE);
		}
		else{
			findUserInfo(objID);
		}

	}



	private void findUserInfo(String userId2) {
		progressDialog = createDialog(this, "加载中...");
		progressDialog.show();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryUserCards");
		Map<String,Object> busiParam = new HashMap<String, Object>();
		busiParam.put("userIds", userId2);//当前用户ID  可以用userInfo = Constant.userInfo;userId=userInfo.getUserSeqId();
		busiParam.put("idDetail", "true");
		paramMap.put("BusiParams", busiParam);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {

			@Override
			public void start() {			}
			@Override
			public void loading(long total, long current, boolean isUploading) {			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();
						ToastUtils.showMessage(instance, "加载失败，请检查网络");
						rl_glo.setVisibility(View.VISIBLE);
						tv_edit.setVisibility(View.INVISIBLE);
					}
				});
			}
			@Override
			public void success(JSONObject jsonObject) {
				progressDialog.dismiss();
				ErrorInfo errorObj  =JSON.parseObject(jsonObject.getString("ErrorInfo"),ErrorInfo.class);
				if(Constant.REQUESTCODE.equals(errorObj.getReturnCode())){

					JSONObject returnObj = jsonObject.getJSONObject("ReturnInfo");
					if (returnObj!=null) {
						JSONArray listoutobj = returnObj.getJSONArray("ListOut");
						JSONObject jsb = listoutobj.getJSONObject(0);
						homePageInfo = JSON.parseObject(jsb.toString(),OtherHomePageInfo.class);
						updateUI();
					}
				}else{
					Log.i("errorObj.getReturnCode())", errorObj.getReturnCode());
					tv_edit.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	protected void updateUI() {
		rl_glo.setVisibility(View.VISIBLE);
		String type = homePageInfo.getUserType();
		Integer t = Integer.valueOf(type);
		System.out.println(t+"");
		switch (t) {
		case 2:
		case 8:
		case 9:
			doctorInfo();
			break;
		case 3:
			studentInfo();
			break;
		case 1:
		case 4:
		case 5:
		case 6:
		case 7:
			otherInfo();
			break;
		}
	}

	private void otherInfo() {
		tv_company.setText(homePageInfo.getCompanyName());
		rl_company.setVisibility(View.VISIBLE);

		tv_job_title.setText(homePageInfo.getJobTitle());
		rl_job_title.setVisibility(View.VISIBLE);
		try {
			userbaseInfo();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void studentInfo() {
		tv_school.setText(homePageInfo.getCompanyName());
		rl_school.setVisibility(View.VISIBLE);

		tv_education.setText(homePageInfo.getEducationLevel());
		tv_education.setVisibility(View.VISIBLE);

		tv_professional.setText(homePageInfo.getProfessionalTitle());
		rl_professional.setVisibility(View.VISIBLE);

		tv_admission_date.setText(homePageInfo.getEntrySchoolDate());
		rl_admission_date.setVisibility(View.VISIBLE);
		try {
			userbaseInfo();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doctorInfo() {
		tv_hospital.setText(homePageInfo.getCompanyName());
		rl_hospital.setVisibility(View.VISIBLE);

		tv_departments.setText(homePageInfo.getDeptName());
		rl_departments.setVisibility(View.VISIBLE);

		tv_professional_title.setText(homePageInfo.getProfessionalTitle());
		rl_professional_title.setVisibility(View.VISIBLE);
		try {
			userbaseInfo();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//基本信息
	private void userbaseInfo() throws DbException{
		tv_name.setText(homePageInfo.getUserName());
		if (homePageInfo.getSex().equals("1")) {
			tv_sex.setText("男");
		}else{
			tv_sex.setText("女");

		}



		//		String day = homePageInfo.getBirthdate();
		//		System.out.println(day);
		//获得日期
		if (homePageInfo.getBirthdate() == null || homePageInfo.getBirthdate().isEmpty()) {
			System.out.println("生日没有返回");
		}else{

			String datestr = homePageInfo.getBirthdate();
			System.out.println(datestr);
			try {
				long a = DDateUtils.getAge(datestr);
				tv_age.setText(a + "岁");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		tv_address.setText((new TCity()).getProvince(instance, homePageInfo.getProvince())+" "+(new TCity()).getCity(instance, homePageInfo.getCityCode()));


		try {
			//加载图片
			BitmapUtils bitmapUtils = new BitmapUtils(instance);
			Log.i("图片地址：", homePageInfo.getBackgroundUrl());
			if(homePageInfo.getBackgroundUrl()==null||
					homePageInfo.getBackgroundUrl().isEmpty()){
				String uri_bg =getString(R.string.bg_1).toString();
				bitmapUtils.display(iv_other_user_background, uri_bg);

			}else{
				bitmapUtils.display(iv_other_user_background, homePageInfo.getBackgroundUrl());
			}
			if(homePageInfo.getUserFaceUrl()==null||
					homePageInfo.getUserFaceUrl().isEmpty()){
				iv_self_avatar.setImageResource(R.drawable.icon_headfailed);
			}else{
				String url = homePageInfo.getUserFaceUrl();
				bitmapUtils.display(iv_self_avatar, url);
			}
		} catch (Exception e) {
			System.out.println("个人简介");
			e.printStackTrace();
		}

	}
	protected void onNewIntent(Intent intent) {
		ischange = intent.getBooleanExtra("submit", false);
		findUserInfo(userId);
		super.onNewIntent(intent);
	}
	@OnClick(R.id.iv_self_avatar)
	public void onSelfAvatar(View v){
		Intent intent = new Intent(instance,UserAvatarImageChangerActivity.class);
		intent.putExtra("image", homePageInfo.getUserFaceUrl());
		intent.putExtra("code", Constant.PICTURE_OTHER_CODE);
		startActivity(intent);
	}

	@OnClick(R.id.iv_back)
	public void onBack(View v){
		if (ischange&&fromself) {
			Intent intent = new Intent(this, SelfActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);			
		}else{
			finish();
		}
	}
	@Override
	public void onBackPressed() {
		if (ischange&&fromself) {
			Intent intent = new Intent(this, SelfActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);			
		}else{
			finish();
		}
		super.onBackPressed();
	}
	@OnClick(R.id.tv_edit)
	public void onEditInfo(View v)
	{
		switch (Integer.valueOf(homePageInfo.getUserType())) {
		case 2:
		case 8:
		case 9:
			Intent intent_d = new Intent(instance,EditDataDoctorsActivity.class);
			intent_d.putExtra(Constant.EDITINFO, homePageInfo);
			startActivity(intent_d);
			break;
		case 3:
			Intent intent_s = new Intent(instance,EditDataStudentsActivity.class);
			intent_s.putExtra(Constant.EDITINFO, homePageInfo);
			startActivity(intent_s);
			break;
		case 1:
		case 4:
		case 5:
		case 6:
		case 7:
			Intent intent_o = new Intent(instance,EditDataOthersActivity.class);
			intent_o.putExtra(Constant.EDITINFO, homePageInfo);
			startActivity(intent_o);
			break;
		}

	}
}


