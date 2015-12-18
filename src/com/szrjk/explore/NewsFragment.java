package com.szrjk.explore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.adapter.ExploreMoreNewsListAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.NewsEntity;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.ShowDialogUtil;
import com.szrjk.util.ToastUtils;

public class NewsFragment extends Fragment{
    private ListView lv_news;
	private String title_id;
	private ExploreMoreNewsListAdapter adapter;
	private Dialog dialog;
	private Context context;
	private static final String LOADING_POST = "正在加载帖子";
	private ArrayList<NewsEntity> newsList;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.HAVE_NEW_POST:
						// TODO Auto-generated method stub
						adapter = new ExploreMoreNewsListAdapter(context, newsList);
						lv_news.setAdapter(adapter);		
				break;
			}
		};
	};
	
//	public NewsFragment(String title_id) {
//		// TODO Auto-generated constructor stub
//		this.title_id = title_id;
//	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle mBundle = getArguments();
		title_id = mBundle.getString("titles_id");
		Log.e("Explore", "帖子类型："+title_id);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View contextView = inflater.inflate(R.layout.news_fragment, container,false);
		initData(contextView);
		initListner();
		return contextView;
	}

	private void initListner() {
		// TODO Auto-generated method stub
		lv_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, NewsDetailActivity.class);
				if(newsList != null && !newsList.isEmpty()){
					Log.e("Explore", "位置："+position);
					intent.putExtra("newsId", newsList.get(position).getInfId());
				}
				context.startActivity(intent);
			}
		});
	}
	
	

	@SuppressLint("NewApi")
	private void initData(View contextView) {
		// TODO Auto-generated method stub
		context = getActivity();
		lv_news = (ListView)contextView.findViewById(R.id.lv_news);
//		Bundle mBundle = getArguments();
//		news_type = mBundle.getString("titles_id");
//		Log.e("Explore", "帖子类型："+title_id);
		dialog = ShowDialogUtil.createDialog(context, LOADING_POST);
		getNews();
	}

	private void getNews() {
		// TODO Auto-generated method stub
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryInfAbstracts");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		busiParams.put("typeId", title_id);
		busiParams.put("baseLev", "0");
		busiParams.put("count", String.valueOf(20));
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			
			@Override
			public void success(JSONObject jsonObject) {
				// TODO Auto-generated method stub
				if (dialog.isShowing())
				{
					dialog.dismiss();
				}
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					newsList = (ArrayList<NewsEntity>) JSON.parseArray(returnObj.getString("ListOut"), NewsEntity.class);
                    if(newsList == null || newsList.isEmpty()){
                    	
                    }else{
                    	Log.e("Explore", newsList.toString());
                    	handler.sendEmptyMessage(Constant.HAVE_NEW_POST);
                    }
				}
					
			}
			
			@Override
			public void start() {
				// TODO Auto-generated method stub
				dialog.show();
			}
			
			@Override
			public void loading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				// TODO Auto-generated method stub
				if (dialog!=null&&dialog.isShowing())
				{
					dialog.dismiss();
					ToastUtils.showMessage(context, "服务器返回数据失败，请检查网络");
				}
			}
		});
	}
}
