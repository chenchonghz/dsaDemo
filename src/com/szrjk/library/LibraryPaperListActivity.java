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
import com.szrjk.adapter.LibraryPaperAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.dhome.R.layout;
import com.szrjk.dhome.R.menu;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.PaperListInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.pull.PullToRefreshBase;
import com.szrjk.pull.PullToRefreshBase.OnRefreshListener;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.util.ToastUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_library_paper_list)
public class LibraryPaperListActivity extends Activity {
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.lv_paperlist)
	private PullToRefreshListView mPullToRefreshListView;
	private String categoryId;
	private ListView lv_paperlist; 
	private List<PaperListInfo> list_paper = new ArrayList<PaperListInfo>();
	private LibraryPaperListActivity instance;
	private LibraryPaperAdapter adapter;
	private int page =1;
	private int y = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(instance);
		getIntents();
		initData();
		getpapers(page);
		

	}

	private void getIntents() {
		Intent intent = getIntent();
		categoryId =intent.getStringExtra(Constant.Library);
		String title = intent.getStringExtra("title");
		tv_title.setText(title);
	}
	private void initData() {
		mPullToRefreshListView.setMode(com.szrjk.pull.PullToRefreshBase.Mode.PULL_FROM_END);
		//		mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(
		//				getResources().getString(R.string.pull_up_lable_text));
		//		mPullToRefreshListView.getLoadingLayoutProxy(false, true)
		//		.setReleaseLabel(
		//				getResources().getString(R.string.release_lable_text));
		//		mPullToRefreshListView.getLoadingLayoutProxy(false, true)
		//		.setRefreshingLabel(
		//				getResources()
		//				.getString(R.string.refreshing_lable_text));
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				new GetDataTask().execute();
				getpapers(page);
				y = list_paper.size();
			}
		});
		
		lv_paperlist = mPullToRefreshListView.getRefreshableView();
		lv_paperlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent(instance, DiseasesDetailedActivity.class);
				intent.putExtra("paper", list_paper.get(position-1));
				startActivity(intent);
				
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.btn_back:
					finish();
					break;

				}
			}
		});
		
	}

	private void getpapers(int page) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "selectPaperDocument");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("categoryId",categoryId);
		busiParams.put("numPerPage","10");
		busiParams.put("page",String.valueOf(page));
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					List<PaperListInfo> list =JSON.parseArray(
							returnObj.getString("ListOut"),PaperListInfo.class);
//					for(PaperListInfo paper : list){
//						list_paper.add(paper);
//					}
					if (list!=null && !list.isEmpty()) {
						list_paper.addAll(list);
						LibraryPaperListActivity.this.page++;
						setAdapter();
						if (mPullToRefreshListView.isRefreshing()) {
							mPullToRefreshListView.onRefreshComplete();
						}
						lv_paperlist.setSelectionFromTop(y,0);
					}
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

	protected void setAdapter() {
		adapter = new LibraryPaperAdapter(instance, list_paper);
		lv_paperlist.setAdapter(adapter);
		adapter.notifyDataSetChanged();
//		ToastUtils.showMessage(instance, String.valueOf(page-1));
//		lv_paperlist.setAdapter( );
		
	}
	

}
