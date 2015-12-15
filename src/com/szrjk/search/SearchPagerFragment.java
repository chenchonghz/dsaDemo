package com.szrjk.search;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import u.aly.l;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
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
import com.szrjk.util.DDateUtils;
import com.szrjk.util.MxgsaTagHandler;
import com.szrjk.util.ToastUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SearchPagerFragment extends Fragment{
	private SearchMoreActivity instance;
	private PullToRefreshListView mPullToRefreshListView;
	private ListView lv_paper;
	private List<SearchEntity> list_paper = new ArrayList<SearchEntity>();
	private int y;
	private TextView tv_nothing;
	private int num;
	private Dialog dialog;
	private String keyword;
	private boolean isFrist = true;
	private SearchPagerAdapter adapter;

	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		instance = (SearchMoreActivity) getActivity();
		View v = inflater.inflate(R.layout.fragment_search_paper,null);
		mPullToRefreshListView = (PullToRefreshListView) v.findViewById(R.id.lv_paperlist);
		mPullToRefreshListView.setMode(com.szrjk.pull.PullToRefreshBase.Mode.PULL_FROM_END);
		lv_paper= mPullToRefreshListView.getRefreshableView();
		tv_nothing = (TextView) v.findViewById(R.id.tv_search);
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
		busiParams.put("searchType", "04");
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
					tv_nothing.setVisibility(View.GONE);
					mPullToRefreshListView.setVisibility(View.VISIBLE);
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
		adapter = new SearchPagerAdapter(instance, list_paper);
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
	//适配器
	public class SearchPagerAdapter extends BaseAdapter {
		private Context mContext;
		private List<SearchEntity> list;
		private LayoutInflater mInflater;

		public SearchPagerAdapter(Context mContext, List<SearchEntity> list) {
			super();
			this.mContext = mContext;
			this.list = list;
			this.mInflater = LayoutInflater.from(mContext);

		}

		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertview, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertview==null) {
				viewHolder = new ViewHolder();
				convertview = mInflater.inflate(R.layout.item_search_paper, null);
				viewHolder.tv_title = (TextView) convertview.findViewById(R.id.tv_search_title);
				viewHolder.tv_author = (TextView) convertview.findViewById(R.id.tv_search_author);
				viewHolder.tv_date = (TextView) convertview.findViewById(R.id.tv_search_date);
				convertview.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertview.getTag();
			}
			viewHolder.tv_title.setText(Html.fromHtml(list.get(position).getTitle(), null, new MxgsaTagHandler(instance)));
			viewHolder.tv_author.setText(list.get(position).getAuthor());
			String updateTime = list.get(position).getUpdateTime();
			try {
				updateTime=DDateUtils.dformatOldstrToNewstr(updateTime, "yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日 HH:mm");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			viewHolder.tv_date.setText(updateTime);
			return convertview;
		}

		private class ViewHolder{
			private TextView tv_title;
			private TextView tv_author;
			private TextView tv_date;
		}
	}
}
