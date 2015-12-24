package com.szrjk.self;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.Coterie;
import com.szrjk.entity.DeptEntity;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.IImgUrlCallback;
import com.szrjk.entity.CircleType;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.util.ToastUtils;
import com.szrjk.util.UploadPhotoUtils;

@ContentView(R.layout.activity_create_circle)
public class CreateCircleActivity extends BaseActivity implements OnClickListener{

	protected static final int EDIT_DATA_SUCCESS = 2;

	@ViewInject(R.id.iv_back)
	private ImageView iv_back;
	
	@ViewInject(R.id.tv_coterieTitle)
	private TextView tv_coterieTitle;
	
	//完成
	@ViewInject(R.id.tv_complete)
	private TextView tv_complete;
	
	@ViewInject(R.id.tv_change_face)
	private TextView tv_change_face;
	
	@ViewInject(R.id.iv_face)
	private ImageView iv_face;
	
	@ViewInject(R.id.et_coterieName)
	private EditText et_coterieName;
	
	@ViewInject(R.id.et_coterieDesc)
	private EditText et_coterieDesc;
	
	@ViewInject(R.id.tv_number)
	private TextView tv_number;
	private int length;
	@ViewInject(R.id.tv_nothing)
	private TextView tv_nothing;
	@ViewInject(R.id.tv_1)
	private TextView tv_neike;
	@ViewInject(R.id.tv_2)
	private TextView tv_waike;
	@ViewInject(R.id.tv_3)
	private TextView tv_fuke;
	@ViewInject(R.id.tv_4)
	private TextView tv_erke;
	@ViewInject(R.id.tv_5)
	private TextView tv_more;
	
	@ViewInject(R.id.rb_single)
	private RadioButton rb_single;
	@ViewInject(R.id.rb_group)
	private RadioButton rb_group;
	
	@ViewInject(R.id.rb_public)
	private RadioButton rb_public;
	
	@ViewInject(R.id.rb_private)
	private RadioButton rb_private;
	
	private CreateCircleActivity instance;
	private UserInfo userInfo;
	private String coterieFaceUrl;

	private Coterie coterie;

	private InputMethodManager imm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		initLayout();
		coterie=(Coterie) getIntent().getSerializableExtra("COTERIE");
		if (coterie!=null) {
			editCoterie(coterie);
		}
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
	}


	private void editCoterie(Coterie coterie) {
		tv_coterieTitle.setText("编辑资料");
		tv_complete.setText("确定");
		BitmapUtils bitmapUtils = new BitmapUtils(instance);
		//获得头像地址
		coterieFaceUrl= coterie.getCoterieFaceUrl();
		//Log.i("旧的头像地址", coterieFaceUrl);
		bitmapUtils.display(iv_face, coterieFaceUrl);
		et_coterieName.setText(coterie.getCoterieName());
		et_coterieDesc.setText(coterie.getCoterieDesc());
		String isOpen=coterie.getIsOpen();
		if (isOpen.equals("1")) {
			rb_public.setChecked(true);
		}else {
			rb_private.setChecked(true);
		}
		if (coterie.getCoterieType().equals("1")) {
			rb_single.setChecked(true);
		}else {
			rb_group.setChecked(true);
		}
		
		tv_nothing.setTextColor(instance.getResources().getColor(R.color.font_tabletitle));
		tv_nothing.setBackground(instance.getResources().getDrawable(R.drawable.flow_text_selector));
		if (coterie.getPropList().size()==0) {
			select(tv_nothing);dept_id=null;
		}else{
			switch (Integer.valueOf(coterie.getPropList().get(0).getPropertyId())) {
			case 10:select(tv_neike);dept_id="10";break;
			case 11:select(tv_waike);dept_id="11";break;
			case 12:select(tv_fuke);dept_id="12";break;
			case 13:select(tv_erke);dept_id="13";break;
			case 14:select(tv_more);dept_id="14";break;
		}
		}
	}

	private void initLayout() {
		userInfo = Constant.userInfo;
		findUserFace();
		et_coterieName.addTextChangedListener(yTextWatcher);
		et_coterieDesc.addTextChangedListener(mTextWatcher);
		tv_nothing.setOnClickListener(this);
		tv_neike.setOnClickListener(this);
		tv_waike.setOnClickListener(this);
		tv_erke.setOnClickListener(this);
		tv_fuke.setOnClickListener(this);
		tv_more.setOnClickListener(this);
	}

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
			length=temp.length();
			if (length==8) {
				ToastUtils.showMessage(instance, "圈子名称最多只能输入8个字符！");
			}
		}  
	};  
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
            length=temp.length();
            tv_number.setText(length+"/200");
        }  
    };  
	@OnClick(R.id.iv_back)
	public void backClick(View view){
		finish();
	}
	//科室选择类型
//	@OnClick(R.id.tv_nothing)
//	public void choosenothing(View view){
//		tv_nothing.setTextColor(instance.getResources().getColor(R.color.header_bg));
//		tv_nothing.setBackground(instance.getResources().getDrawable(R.drawable.flow_dept_selector));
//	}
	
	@OnClick(R.id.tv_complete)
	public void completeClick(View view){
		String userSeqId = userInfo.getUserSeqId();
		String coterieName = et_coterieName.getText().toString();
		String coterieDesc = et_coterieDesc.getText().toString();
		String isOpen=null;
		String coterieType = null;
		//圈子类型,1个人,2机构/组织
		if (rb_single.isChecked()) {
			coterieType = "1";
		}
		if (rb_group.isChecked()) {
			coterieType = "2";
		}
		//圈子是否开放，1开放，2不开放
		if (rb_public.isChecked()) {
			isOpen="1";
		}
		if (rb_private.isChecked()) {
			isOpen="2";
		}
		if (coterieName.equals("")) {
			ToastUtils.showMessage(instance, "请输入圈子名称！");
			//Toast.makeText(instance, "请输入圈子名称！", Toast.LENGTH_LONG).show();
			return;
		}
		if (coterieDesc.equals("")) {
			ToastUtils.showMessage(instance, "请输入圈子说明！");
			//Toast.makeText(instance, "请输入圈子说明！", Toast.LENGTH_LONG).show();
			return;
		}
		if (isOpen==null) {
			ToastUtils.showMessage(instance, "请选择圈子权限！");
			return;
		}
		if (tv_complete.getText().toString().equals("完成")) {
			maintainCoterie("A",userSeqId,"",coterieFaceUrl,coterieName,coterieDesc,isOpen,coterieType);
		}
		if (tv_complete.getText().toString().equals("确定")) {
			maintainCoterie("U", userSeqId, coterie.getCoterieId(), coterieFaceUrl, coterieName, coterieDesc, isOpen,coterieType);
		}
	}
	
	private void maintainCoterie(String operateType,String userSeqId, String coterieId,String coterieFaceUrl2,
			String coterieName, String coterieDesc, String isOpen,String coterieType) {
		List<CircleType> propList = new ArrayList<CircleType>();
		if (dept_id==null) {
			propList = null;
		}else{
			CircleType property = new CircleType("1", dept_id);
			propList.add(property);
		}
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "maintainCoterieInfo");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("operateType", operateType);
		Map<String, Object> coterieInfo = new HashMap<String, Object>();
		coterieInfo.put("userSeqId", userSeqId);
		coterieInfo.put("coterieId", coterieId);
		coterieInfo.put("coterieName", coterieName);
		coterieInfo.put("coterieDesc", coterieDesc);
		coterieInfo.put("coterieFaceUrl", coterieFaceUrl);
		coterieInfo.put("isOpen", isOpen);
		coterieInfo.put("coterieType", coterieType);
		coterieInfo.put("propList", propList);
		busiParams.put("coterieInfo", coterieInfo);
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
//					Coterie coterie1= JSON.parseObject(jsonObject.getString("ReturnInfo"), Coterie.class);
					String coterieId = returnObj.getString("coterieId");
					if (tv_complete.getText().toString().equals("完成")) {
						Toast.makeText(instance, "创建圈子成功！", Toast.LENGTH_LONG).show();
						Intent intent=new Intent(instance, CircleInviteFirendActivity.class);
						intent.putExtra(Constant.CIRCLE, coterieId);
						intent.putExtra("Create", "YES");
						startActivity(intent);
						finish();
					}
					if (tv_complete.getText().toString().equals("确定")) {
						Toast.makeText(instance, "编辑资料成功！", Toast.LENGTH_LONG).show();
						setResult(EDIT_DATA_SUCCESS);
						Intent intent=new Intent(instance, CircleIntroductionActivity.class);
						intent.putExtra(Constant.CIRCLE, coterieId);
						startActivity(intent);
						finish();
					}
					//Log.i("circleInfo", circleInfo.toString());
				}
			}
			@Override
			public void start() {
			}
			@Override
			public void loading(long total, long current, boolean isUploading) {
			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				ToastUtils.showMessage(instance, jobj.getString("ErrorMessage"));
			}
		});
	}

	@OnClick(R.id.tv_change_face)
	public void changeFaceClick(View view){
		closeKeyboard();
		uploadPhotoUtils=new UploadPhotoUtils(instance,true);
		uploadPhotoUtils.popubphoto(iv_face, new IImgUrlCallback() {
			@Override
			public void operImgUrl(String imgurl) {
				coterieFaceUrl=imgurl;
				//Log.i("新的头像地址", coterieFaceUrl);
			}
			@Override
			public void operImgPic(Bitmap bitmap) {
				iv_face.setImageBitmap(bitmap);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode!=RESULT_OK) {
			return;
		}
		try {
			uploadPhotoUtils.imgOper(requestCode,resultCode,data);
			
		} catch (Exception e) {
			e.printStackTrace();
			showToast(instance, "处理图片出错，请再尝试", 0);
		}
	}
	private void findUserFace() {
		//获得头像地址
		if (userInfo != null) {
			coterieFaceUrl= userInfo.getUserFaceUrl();
			ImageLoaderUtil imageLoaderUtil=new ImageLoaderUtil(instance, coterieFaceUrl, iv_face, R.drawable.header, R.drawable.header);
			imageLoaderUtil.showImage();
		}else{
			iv_face.setImageResource(R.drawable.header);
			Log.i("userInfo.getUserFaceUrl()", "userInfo.getUserFaceUrl()	是空");
		}

	}
	private void closeKeyboard(){
		if(imm.isActive()&&getCurrentFocus()!=null){
			if (getCurrentFocus().getWindowToken()!=null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
	private int dept_view_id = R.id.tv_nothing;
	private String dept_name = "无";
	private String dept_id=null;
	@Override
	//科室选择（单选模式）
	public void onClick(View view) {
		if (view.getId()==dept_view_id) {
			Log.i("dept", dept_name);
		}else{
			//第一次判断更改至非选中色
			switch (dept_view_id) {
			case R.id.tv_nothing:unselect(tv_nothing);break;
			case R.id.tv_1:unselect(tv_neike);break;
			case R.id.tv_2:unselect(tv_waike);break;
			case R.id.tv_3:unselect(tv_fuke);break;
			case R.id.tv_4:unselect(tv_erke);break;
			case R.id.tv_5:unselect(tv_more);break;
			}
			//更改选中色
			switch (view.getId()) {
			case R.id.tv_nothing:select(tv_nothing);dept_id=null;break;
			case R.id.tv_1:select(tv_neike);dept_id="10";break;
			case R.id.tv_2:select(tv_waike);dept_id="11";break;
			case R.id.tv_3:select(tv_fuke);dept_id="12";break;
			case R.id.tv_4:select(tv_erke);dept_id="13";break;
			case R.id.tv_5:select(tv_more);dept_id="14";break;
			}
			
		}
		
	}


	private void select(TextView tv) {
		tv.setTextColor(instance.getResources().getColor(R.color.header_bg));
		tv.setBackground(instance.getResources().getDrawable(R.drawable.flow_dept_selector));
		dept_view_id = tv.getId();
		dept_name = tv.getText().toString();
		Log.i("dept", dept_view_id+dept_name);
		
	}


	private void unselect(TextView tv) {
		tv.setTextColor(instance.getResources().getColor(R.color.font_tabletitle));
		tv.setBackground(instance.getResources().getDrawable(R.drawable.flow_text_selector));
	}
}
