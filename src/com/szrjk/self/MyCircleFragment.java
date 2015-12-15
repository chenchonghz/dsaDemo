package com.szrjk.self;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.adapter.CircleListAdapter;
import com.szrjk.adapter.MyFriendListAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.CircleInfo;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.FriendList;
import com.szrjk.entity.UserHomePageInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.ToastUtils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class MyCircleFragment extends Fragment {

	private UserHomePageInfo homePageInfo;
	private View v;
	private ImageView ib_more;

	//创建圈子
	private RelativeLayout rl_create_circle;

	//热圈子
	private ListView lv_hot_circle;

	//我的圈子
	public ListView lv_my_circle;
	private CircleActivity instance;
	private List<CircleInfo> list;
	private List<CircleInfo> hotlist; //保存热门圈子的所有列表
	private List<CircleInfo> hotlist_show = new ArrayList<CircleInfo>() ;//保存热门圈子的显示列表 （头3项）
	private Dialog dialog;
	
	public MyCircleFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_circle, container, false);
		instance  = (CircleActivity)getActivity();
		homePageInfo  = instance.getUserInfo();
		dialog = createDialog(getActivity(), "加载中...");
		findView();
		setListener();
		getCircles();
		getHotCircles();
		return v;
	}
	private void getHotCircles() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "getHotCoterie");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId",Constant.userInfo.getUserSeqId());
		busiParams.put("beginNum",Integer.valueOf(0));
		busiParams.put("endNum",Integer.valueOf(20));
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					if (returnObj==null) {
						Toast.makeText(instance, "还没有圈子", Toast.LENGTH_SHORT).show();
					}else{
						hotlist=JSON.parseArray(
								returnObj.getString("ListOut"), CircleInfo.class);
						
						getHotCircle2Show(hotlist);
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

	//请求用户的圈子报文
	public void getCircles() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryUserRelativeCoterie");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId",Constant.userInfo.getUserSeqId());
		busiParams.put("beginNum",Integer.valueOf(0));
		busiParams.put("endNum",Integer.valueOf(500));
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			public void success(JSONObject jsonObject) {
				dialog.dismiss();
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					if (returnObj==null) {
						Toast.makeText(instance, "还没有圈子", Toast.LENGTH_SHORT).show();
					}else{
						list=JSON.parseArray(
								returnObj.getString("coterieList"), CircleInfo.class);
						
						setCircleAdapter(list);
					}
				}
			}

			public void start() {dialog.show();
			}

			public void loading(long total, long current, boolean isUploading) {

			}

			public void failure(HttpException exception, JSONObject jobj) {
				dialog.dismiss();
				ToastUtils.showMessage(getActivity(), "获取失败");
			}
		});
	}
	
	//设置"我的圈子"listview的adpater
	protected void setCircleAdapter(List<CircleInfo> list2) {
		CircleListAdapter adapter = new CircleListAdapter(list2, instance);
		lv_my_circle.setAdapter(adapter);
		setListViewHeightBasedOnChildren(lv_my_circle);
		
		
	}
	//获得显示的热门圈子列表
	private void getHotCircle2Show(List<CircleInfo> list){
		for (int i = 0; list.size()>3?i<3:i<list.size(); i++) {
			hotlist_show.add(list.get(i));
			
		}
		CircleListAdapter adapter = new CircleListAdapter(hotlist_show, instance);
		lv_hot_circle.setAdapter(adapter);
		setListViewHeightBasedOnChildren(lv_hot_circle);
	}
	
	
	
	private void findView() {
		rl_create_circle = (RelativeLayout)v.findViewById(R.id.rl_create_circle);
		lv_my_circle = (ListView) v.findViewById(R.id.lv_my_circle);
		lv_hot_circle = (ListView) v.findViewById(R.id.lv_hot_circle);
		ib_more =(ImageView) v.findViewById(R.id.ib_more);
	}

	private void setListener() {
		rl_create_circle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),CreateCircleActivity.class);
				i.putExtra("userinfo", homePageInfo);
				startActivity(i);
			}
		});
		
		lv_my_circle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				CircleInfo circleInfo = list.get(position);
				Intent intent = new Intent(instance, CircleHomepageActivity.class);
				intent.putExtra("circle", circleInfo);
				startActivity(intent);
				
			}
		});
		lv_hot_circle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				CircleInfo circleInfo = hotlist_show.get(position);
				Intent intent = new Intent(instance, CircleHomepageActivity.class);
				intent.putExtra(Constant.CIRCLE, circleInfo);
				startActivity(intent);
			}
		});
		ib_more.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				if (hotlist.size()==0) {
					ToastUtils.showMessage(instance, "未有热门圈子");
				}else{
					Intent intent = new Intent(instance, HotCircleActivity.class);
					intent.putExtra(Constant.CIRCLE, (Serializable)hotlist);
					startActivity(intent);
					
				}
			}
		});
		
		

	}
	public static void setListViewHeightBasedOnChildren(ListView listView) {
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null) {
	     return;
	    }
	    int totalHeight = 0;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	     View listItem = listAdapter.getView(i, null, listView);
	     listItem.measure(0, 0);
	     totalHeight += listItem.getMeasuredHeight();
	    }
	   
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    
	    params.height = totalHeight
	      + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
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
