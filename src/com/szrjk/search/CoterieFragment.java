package com.szrjk.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.CircleInfo;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.SearchEntity;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.pull.ILoadingLayout;
import com.szrjk.pull.PullToRefreshBase;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.pull.PullToRefreshBase.OnRefreshListener;
import com.szrjk.self.CircleHomepageActivity;
import com.szrjk.util.ToastUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Browser.SearchColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CoterieFragment extends Fragment{
	private SearchMoreActivity instance;
	private PullToRefreshListView mPullToRefreshListView;
	private ListView lv_circle;
	private List<SearchEntity> list_circle = new ArrayList<SearchEntity>();
	private TextView tv_nothing;
	private int y;
	private int num;
	private Dialog dialog;
	private CircleInfo circleInfo;
	private String keyword;
	private CoterieFragmentAdapter adapter;
	private boolean isFrist = true;
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,Bundle savedInstanceState) {
		instance = (SearchMoreActivity) getActivity();
		View v = inflater.inflate(R.layout.fragment_coterie,null);
		mPullToRefreshListView = (PullToRefreshListView) v.findViewById(R.id.lv_circlelist);
		mPullToRefreshListView.setMode(com.szrjk.pull.PullToRefreshBase.Mode.PULL_FROM_END);
		lv_circle= mPullToRefreshListView.getRefreshableView();
		tv_nothing = (TextView) v.findViewById(R.id.tv_search);
		//从searchMoreActivity页面拿到搜索keyword

		num=0;
		PostData();
		initListener();
		return v;
	}

	public void refreshSearch(){
		list_circle.clear();
		PostData();
		num=0;
		isFrist=true;
	}

	private void initListener() {
		//上拉加载监听器
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			//上拉加载方法
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				//				new GetDataTask().execute();
				//发送请求得到新项目报文
				PostData();
				//保存加载的位置
				y = list_circle.size();
			}
		});
		lv_circle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				dialog = createDialog(getActivity(), "客官你别急，玩命加载中...");
//				HashMap<String, Object> paramMap = new HashMap<String, Object>();
//				paramMap.put("ServiceName", "queryCoterieById");
//				Map<String, Object> busiParams = new HashMap<String, Object>();
//				busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
//				busiParams.put("coterieId",list_circle.get(arg2-1).getRecordId());
//				busiParams.put("memberLimitCount", "0");
//				paramMap.put("BusiParams", busiParams);
//				DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
//					public void success(JSONObject jsonObject) {
//						ErrorInfo errorObj = JSON.parseObject(
//								jsonObject.getString("ErrorInfo"), ErrorInfo.class);
//						if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
//						{
//							JSONObject returnObj = jsonObject
//									.getJSONObject("ReturnInfo");
//							circleInfo = JSON.parseObject(returnObj.getString("ListOut"), CircleInfo.class);
//							Log.i("TAG", circleInfo.toString());
//							Intent intent = new Intent(instance, CircleHomepageActivity.class);
//							intent.putExtra(Constant.CIRCLE, circleInfo);
//							dialog.dismiss();
//							startActivity(intent);
//						}else if ("0006".equals(errorObj.getReturnCode())) {
//							ToastUtils.showMessage(instance, "圈子已失效");
//							dialog.dismiss();
//
//						}
//					}
//
//					public void start() {dialog.show();
//
//					}
//
//					public void loading(long total, long current, boolean isUploading) {
//
//					}
//
//					public void failure(HttpException exception, JSONObject jobj) {
//						dialog.dismiss();
//					}
//				});
				Intent intent = new Intent(instance, CircleHomepageActivity.class);
				intent.putExtra(CircleHomepageActivity.intent_param_circle_id,list_circle.get(arg2-1).getRecordId());
				startActivity(intent);

			}
		});

	}
	private void PostData() {
		keyword = instance.getkeyword();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "searchAll");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("channel","H");
		busiParams.put("searchType", "12");
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
						list_circle.addAll(list);
						num +=10;
						Log.i("TAG",num+"search");
						Log.i("TAG",list_circle.toString());
						setAdapter();
						if (mPullToRefreshListView.isRefreshing()) {
							mPullToRefreshListView.onRefreshComplete();
						}
						lv_circle.setSelectionFromTop(y,0);
					}
					Log.i("isFrist", isFrist+"");
					if (isFrist==true) {
						if (list==null||list.isEmpty()) {
							tv_nothing.setVisibility(View.VISIBLE);
							mPullToRefreshListView.setVisibility(View.GONE);
						}
						isFrist = false;
					}else{
						tv_nothing.setVisibility(View.GONE);
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
		adapter = new CoterieFragmentAdapter(instance, list_circle);
		lv_circle.setAdapter(adapter);
		adapter.notifyDataSetChanged();
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
