package com.szrjk.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.LibraryGuideAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.LibraryEntity;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.HeaderView;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@ContentView(R.layout.activity_library_guide)
public class LibraryGuideActivity extends Activity implements OnClickListener{
	@ViewInject(R.id.hv_library_guide)
	private HeaderView hv_library_guide;

	@ViewInject(R.id.rly_library_medicine)
	private RelativeLayout rly_library_medicine;
	@ViewInject(R.id.iv_library_medicine)
	private ImageView iv_library_medicine;

	@ViewInject(R.id.rly_library_surgery)
	private RelativeLayout rly_library_surgery;
	@ViewInject(R.id.iv_library_surgery)
	private ImageView iv_library_surgery;

	@ViewInject(R.id.rly_library_obgyn)
	private RelativeLayout rly_library_obgyn;
	@ViewInject(R.id.iv_library_obgyn)
	private ImageView iv_library_obgyn;

	@ViewInject(R.id.rly_library_pediatrics)
	private RelativeLayout rly_library_pediatrics;
	@ViewInject(R.id.iv_library_pediatrics)
	private ImageView iv_library_pediatrics;

	@ViewInject(R.id.rly_library_otherdepartment)
	private RelativeLayout rly_library_otherdepartment;
	@ViewInject(R.id.iv_library_otherdepartment)
	private ImageView iv_library_otherdepartment;

	@ViewInject(R.id.rly_library_pharmacopoeia)
	private RelativeLayout rly_library_pharmacopoeia;
	@ViewInject(R.id.iv_library_pharmacopoeia)
	private ImageView iv_library_pharmacopoeia;

	@ViewInject(R.id.rly_library_guid)
	private RelativeLayout rly_library_guid;
	@ViewInject(R.id.iv_library_guid)
	private ImageView iv_library_guid;

	@ViewInject(R.id.lv_library_guide_child)
	private ListView lv_library_guide_child;
	private LibraryGuideActivity instance;
	private String categoryId;
	private Dialog dialog;
	 
	private List<LibraryEntity> list_head = new ArrayList<LibraryEntity>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		dialog = createDialog(instance, "加载中...");
		ViewUtils.inject(instance);
		getIntents();
		initListener();
		
		
	}



	//获得页面类型
	private void getIntents() {
		Intent intent = getIntent();
		categoryId = intent.getStringExtra(Constant.Library);
		switch (Integer.valueOf(categoryId)) {
		case 1:
			hv_library_guide.setHtext(getResources().getString(R.string.Library1));
			getList("7");
			break;
		case 2:
			hv_library_guide.setHtext(getResources().getString(R.string.Library3));
			rly_library_guid.setVisibility(View.VISIBLE);
			rly_library_pharmacopoeia.setVisibility(View.VISIBLE);
			rly_library_medicine.setVisibility(View.GONE);
			rly_library_surgery.setVisibility(View.GONE);
			rly_library_obgyn.setVisibility(View.GONE);
			rly_library_pediatrics.setVisibility(View.GONE);
			rly_library_otherdepartment.setVisibility(View.GONE);
			getList("36");
			break;

		case 3:
			hv_library_guide.setHtext(getResources().getString(R.string.Library2));
			getList("41");
			break;

		}
	}
	//请求接口获得左侧页面资料
	private boolean isFirst =true;
	//请求接口获得右边list资料
	private void getList(String id) {
		if (isFirst==true) {
			isFirst=false;
		}else{
			lv_library_guide_child.setAdapter(null);
			
			
		}
		dialog.show();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "selectDocument");
		paramMap.put("KB", "kb");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("categoryId",id);
		busiParams.put("numPerPage","10");
		busiParams.put("page","1");
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					list_head = JSON.parseArray(
							returnObj.getString("ListOut"),LibraryEntity.class);
					setadapter(list_head);
				}

			}

			public void start() {

			}

			public void loading(long total, long current, boolean isUploading) {

			}

			public void failure(HttpException exception, JSONObject jobj) {
				dialog.dismiss();
				ToastUtils.showMessage(instance, "获取失败");
			}
		});

	}
	protected void setadapter(final List<LibraryEntity> list_head2) {
		LibraryGuideAdapter adapter = new LibraryGuideAdapter(instance, list_head2);
		lv_library_guide_child.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		dialog.dismiss();
		lv_library_guide_child.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent  = new Intent(instance,DiseasesListActivity.class);
				intent.putExtra(Constant.Library, list_head2.get(position));
				startActivity(intent);
			}
		});
	}


	private List<RelativeLayout> RLYview = new ArrayList<RelativeLayout>();
	private List<ImageView> iview = new ArrayList<ImageView>();
	private void initListener() {
		rly_library_medicine.setOnClickListener(this);
		rly_library_obgyn.setOnClickListener(this);
		rly_library_surgery.setOnClickListener(this);
		rly_library_pediatrics.setOnClickListener(this);
		rly_library_otherdepartment.setOnClickListener(this);
		rly_library_pharmacopoeia.setOnClickListener(this);
		rly_library_guid.setOnClickListener(this);
		RLYview.add(rly_library_medicine);
		RLYview.add(rly_library_obgyn);
		RLYview.add(rly_library_surgery);
		RLYview.add(rly_library_pediatrics);
		RLYview.add(rly_library_otherdepartment);
		iview.add(iv_library_medicine);
		iview.add(iv_library_obgyn);
		iview.add(iv_library_surgery);
		iview.add(iv_library_pediatrics);
		iview.add(iv_library_otherdepartment);
	}
	private int Flag = 0;
	private int[] img_selected={
			R.drawable.icon_64_medicinefocus,R.drawable.icon_64_obgynfocus,
			R.drawable.icon_64_surgeryfocus,R.drawable.icon_64_pediatricsfocus,
			R.drawable.icon_64_otherdepartmentfocus
	};
	private int[] img_unselect={
			R.drawable.icon_64_medicine,R.drawable.icon_64_obgyn,
			R.drawable.icon_64_surgery,R.drawable.icon_64_pediatrics,
			R.drawable.icon_64_otherdepartment
	};
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.rly_library_medicine:
			if (Flag !=0) {
				Flag=0;
				changeColor(rly_library_medicine);
				if (categoryId.equals("1")) {
					getList("7");
				}else if (categoryId.equals("3")) {
					getList("41");
				}
				
			}
			break;
		case R.id.rly_library_obgyn:
			if (Flag!=1) {
				Flag =1;
				changeColor(rly_library_obgyn);
				if (categoryId.equals("1")) {
					getList("23");
				}else if (categoryId.equals("3")) {
					getList("57");
				}
			}
			break;
		case R.id.rly_library_surgery:
			if (Flag!=2) {
				Flag =2;
				changeColor(rly_library_surgery);
				if (categoryId.equals("1")) {
					getList("17");
				}else if (categoryId.equals("3")) {
					getList("51");
				}
			}
			break;
		case R.id.rly_library_pediatrics:
			if (Flag!=3) {
				Flag =3;
				changeColor(rly_library_pediatrics);
				if (categoryId.equals("1")) {
					getList("26");
				}else if (categoryId.equals("3")) {
					getList("60");
				}
			}
			break;
		case R.id.rly_library_otherdepartment:
			if (Flag!=4) {
				Flag =4;
				changeColor(rly_library_otherdepartment);
				if (categoryId.equals("1")) {
					getList("27");
				}else if (categoryId.equals("3")) {
					getList("61");

				}
			}
			break;
		case R.id.rly_library_guid:
			if (Flag!=5) {
				Flag =5;
				rly_library_guid.setBackgroundColor(getResources().getColor(R.color.white));
				iv_library_guid.setImageResource(R.drawable.icon_64_guidfocus);
				rly_library_pharmacopoeia.setBackgroundColor(getResources().getColor(R.color.bg_librarguide));
				iv_library_pharmacopoeia.setImageResource(R.drawable.icon_64_pharmacopoeia);
				getList("37");
			}
			break;
		case R.id.rly_library_pharmacopoeia:
			if (Flag!=6) {
				Flag =6;
				rly_library_guid.setBackgroundColor(getResources().getColor(R.color.bg_librarguide));
				iv_library_guid.setImageResource(R.drawable.icon_64_guid);
				rly_library_pharmacopoeia.setBackgroundColor(getResources().getColor(R.color.white));
				iv_library_pharmacopoeia.setImageResource(R.drawable.icon_64_pharmacopoeiafocus);
				getList("36");
			}
			break;

		}

	}



	private void changeColor(RelativeLayout rly) {
		for (int i = 0; i < RLYview.size(); i++) {
			if (RLYview.get(i).getId()==rly.getId()) {
				RLYview.get(i).setBackgroundColor(getResources().getColor(R.color.white));
				iview.get(i).setImageResource(img_selected[i]);
			}else{
				RLYview.get(i).setBackgroundColor(getResources().getColor(R.color.bg_librarguide));
				iview.get(i).setImageResource(img_unselect[i]);
			}

		}

	}
	public Dialog createDialog(Context context, String msg)
	{
		Dialog dialog = new Dialog(context, R.style.loading_dialog);
		dialog.setContentView(R.layout.loading_layout);
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		TextView textView = (TextView) dialog.findViewById(R.id.tv_msg);
		textView.setText(msg);
		return dialog;
	}

}
