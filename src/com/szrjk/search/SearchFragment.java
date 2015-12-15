package com.szrjk.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.adapter.CoterieMemberListAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.SearchEntity;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.library.DiseasesDetailedActivity;
import com.szrjk.pull.ILoadingLayout;
import com.szrjk.pull.PullToRefreshBase;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.pull.PullToRefreshBase.OnRefreshListener;
import com.szrjk.search.SearchPagerFragment.SearchPagerAdapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class SearchFragment extends Fragment{
	

	private SearchMoreActivity instance;
	private PullToRefreshListView mPullToRefreshListView;
	private ListView lv_paper;
	private List<SearchEntity> list_paper = new ArrayList<SearchEntity>();
	private int y;
	private int num;
	private Dialog dialog;
	private String keyword;
	private SearchFragmentAdapter adapter;
	private String searchType;
	private TextView tv_none_result;
	protected boolean isFrist=true;
	
	
	
	
	public SearchFragment(String searchType) {
		super();
		this.searchType=searchType;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,Bundle savedInstanceState) {
		
		 instance = (SearchMoreActivity) getActivity();
			View v = inflater.inflate(R.layout.fragment_search,null);
			mPullToRefreshListView = (PullToRefreshListView) v.findViewById(R.id.lv_paperlist);
			tv_none_result = (TextView) v.findViewById(R.id.tv_none_result);
			mPullToRefreshListView.setMode(com.szrjk.pull.PullToRefreshBase.Mode.PULL_FROM_END);
			lv_paper= mPullToRefreshListView.getRefreshableView();
			//从searchMoreActivity页面拿到搜索keyword
			Log.i("TAG", keyword+"paper");
			//设定搜索显示的初始id 
			num=0;
			PostData();
			initListener();
			return v;
	}
	
	public void refreshSearch(){
		list_paper.clear();
		PostData();
		num=0;
		isFrist=true;
	}
	
	private void PostData() {
		keyword = instance.getkeyword();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "searchAll");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("channel","H");
		busiParams.put("searchType", searchType);
		busiParams.put("searchWord",keyword);
		busiParams.put("baseRecordId",String.valueOf(num));
		busiParams.put("pageSize","10");
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					List<SearchEntity> list = JSON.parseArray(
							returnObj.getString("ListOut"),SearchEntity.class);
					if (list!=null && !list.isEmpty()) {
						list_paper.addAll(list);
						num +=10;
						Log.i("TAG",num+"search");
						Log.i("TAG",list_paper.toString());
						setAdapter();
						if (mPullToRefreshListView.isRefreshing()) {
							mPullToRefreshListView.onRefreshComplete();
						}
						lv_paper.setSelectionFromTop(y,0);
					}

					if (isFrist==true) {
						if (list==null||list.isEmpty()) {
							tv_none_result.setVisibility(View.VISIBLE);
							mPullToRefreshListView.setVisibility(View.GONE);
						}
						isFrist = false;
					}else{
						tv_none_result.setVisibility(View.GONE);
						mPullToRefreshListView.setVisibility(View.VISIBLE);
						if (list==null||list.isEmpty()) {
							ILoadingLayout startLabels = mPullToRefreshListView  
									.getLoadingLayoutProxy(false, true); //为true,false返回设置下拉的ILoadingLayout；为false,true返回设置上拉的。
							startLabels.setPullLabel("没有更多了！");// 刚下拉时，显示的提示  
							startLabels.setRefreshingLabel("没有更多了！");// 刷新时  
							startLabels.setReleaseLabel("没有更多了！");// 下来达到一定距离时，显示的提示 
						}
						if (mPullToRefreshListView.isRefreshing()) {
							mPullToRefreshListView.onRefreshComplete();
						}
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
		adapter = new SearchFragmentAdapter(instance, list_paper,searchType);
		lv_paper.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
	}
	//设置监听
	private void initListener() {
		//上拉加载监听器
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			//上拉加载方法
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				//				new GetDataTask().execute();
				//发送请求得到新项目报文
				PostData();
				//保存加载的位置
				y = list_paper.size();
			}
		});
		lv_paper.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(instance, DiseasesDetailedActivity.class);
				intent.putExtra("searchID", list_paper.get(arg2-1).getRecordId());
				intent.putExtra("searchTitle", list_paper.get(arg2-1).getTitle());
				startActivity(intent);
				
			}
		});
	}
}
