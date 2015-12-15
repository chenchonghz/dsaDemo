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
import com.szrjk.adapter.LibraryPaperListAdaper;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.dhome.R.layout;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.LibraryEntity;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.widget.HeaderView;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
@ContentView(R.layout.activity_library_paper)
public class LibraryPaperActivity extends BaseActivity {
@ViewInject(R.id.hv_library_paper)
private HeaderView hv_paper;
@ViewInject(R.id.lv_library_paper)
private ListView lv_paper;
private LibraryPaperActivity instance;
private Dialog dialog;
private List<LibraryEntity> list_head = new ArrayList<LibraryEntity>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		dialog = createDialog(instance, "加载中...");
		getdocuments();
		initListener();
	}



	private void getdocuments() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "selectDocument");
		paramMap.put("KB", "kb");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("categoryId","4");
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
			public void start() {dialog.show();
			}
			public void loading(long total, long current, boolean isUploading) {
			}
			public void failure(HttpException exception, JSONObject jobj) {
				dialog.dismiss();
			}
		});
	}

	protected void setadapter(List<LibraryEntity> list_head) {
		LibraryPaperListAdaper adapter = new LibraryPaperListAdaper(instance, list_head);
		lv_paper.setAdapter(adapter);
		dialog.dismiss();
	}
	private void initListener() {
		lv_paper.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent(instance, LibraryPaperListActivity.class);
				intent.putExtra(Constant.Library, list_head.get(position).getId());
				intent.putExtra("title", list_head.get(position).getName());
				startActivity(intent);
				Log.i("TAG",list_head.get(position).getId() );
			}
		});
		
	}
}
