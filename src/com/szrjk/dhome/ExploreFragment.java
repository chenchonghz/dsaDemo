package com.szrjk.dhome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.ExploreNewsListAdapter;
import com.szrjk.adapter.LoopViewPagerAdapter;
import com.szrjk.config.Constant;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.NewType;
import com.szrjk.entity.NewsEntity;
import com.szrjk.explore.MoreNewsActivity;
import com.szrjk.explore.NewsDetailActivity;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.self.more.CaseSharePostActivity;
import com.szrjk.self.more.ProblemHelpActivity;
import com.szrjk.util.BusiUtils;
import com.szrjk.util.DialogUtil;
import com.szrjk.util.ShowDialogUtil;
import com.szrjk.util.ToastUtils;
import com.szrjk.viewpager.LoopViewPager;
import com.szrjk.widget.IndexRecommandListView;

public class ExploreFragment extends Fragment
{
	@ViewInject(R.id.vp_imagesloop)
	private LoopViewPager loopViewPager;
	@ViewInject(R.id.tv_img_title)
	private TextView tv_img_title;
	@ViewInject(R.id.ll_dots)
	private LinearLayout ll_dots;
	@ViewInject(R.id.lv_news)
	private IndexRecommandListView lv_news;
	@ViewInject(R.id.bt_caseshare)
	private Button bt_caseShare;
	@ViewInject(R.id.bt_problemhelp)
	private Button bt_problemHelp;
	@ViewInject(R.id.sl_explore)
	private ScrollView sl_explore;
	private View news_header;
	private Button bt_more;
	private Context context;
	private Dialog dialog;
	private boolean isFirstIn = true;
	private boolean isTourist;
	private static final String LOADING_POST = "正在加载帖子";
//	private int[] imgs = new int[]{R.drawable.banner1,R.drawable.banner2,R.drawable.banner3
//			                       ,R.drawable.banner4,R.drawable.banner5};
//	private String[] titles = new String[]{"给健康带份儿礼","经纬天下，济世安邦","准妈妈的心理保健"
//			                               ,"精雕细琢下的眼部整形术","健康在于运动"};
	private List<String> indicatorTitles;
	private List<String> title_id;
	private ArrayList<NewsEntity> newsList;
	private ArrayList<NewsEntity> ggList;
	private ExploreNewsListAdapter adapter;
	private ArrayList<NewType> typeList;
	private String title;
	private String type;
	/**
	 * 定时轮播viewPager
	 */
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.HAVE_NEW_POST:
					adapter = new ExploreNewsListAdapter(context, newsList);
					lv_news.setAdapter(adapter);
				break;
			case Constant.HAVE_NEW_TYPE:
				if(typeList == null){
					typeList = new ArrayList<NewType>();		
					}
				if(typeList.isEmpty()){
					title = "热点";
					type = "RD";
					NewType newType = new NewType();
					newType.setTypeId(type);
					newType.setTypeName(title);
					typeList.add(newType);
				}
				Log.e("ExploreFragment", typeList.toString());
				break;
			case 3:
				loopViewPager.setCurrentItem(loopViewPager.getCurrentItem()+1, true);
				handler.sendEmptyMessageDelayed(3, 6000);
				break;
			case Constant.HAVE_NEW_GG:
				loopViewPager.requestFocus();
				loopViewPager.requestFocusFromTouch();
				tv_img_title.setText(ggList.get(0).getInfTitleAbstract());
				initDots();
				setAdapter();
				setGGListner();
				
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.explore_fragment, null);
		ViewUtils.inject(this, view);
		initData();
		setAdapter();
		setListener();
		return view;
	}


	protected void setGGListner() {
		// TODO Auto-generated method stub
		loopViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				updateDot(position);
				updateTitle(position);
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}


	private void setListener() {
		// TODO Auto-generated method stub
		
		bt_caseShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, CaseSharePostActivity.class);
				intent.putExtra("postType", "ALL");
				startActivity(intent);
				isFirstIn = false;
			}
		});
		
		bt_problemHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ProblemHelpActivity.class);
				intent.putExtra("postType", "ALL");
				startActivity(intent);
				isFirstIn = false;
			}
		});
		if(bt_more != null){
			bt_more.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(isTourist){
						DialogUtil.showGuestDialog(context, null);
					}else{		
						Intent intent = new Intent(context, MoreNewsActivity.class);
						if(typeList!=null){
							intent.putExtra("typeList", typeList);
						}
						startActivity(intent);
					}
					isFirstIn = false;
				}
				
			});
		}
		lv_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(isTourist){
					DialogUtil.showGuestDialog(context, null);
				}else{					
					Intent intent = new Intent(context, NewsDetailActivity.class);
					if(newsList != null && !newsList.isEmpty()){
						Log.e("Explore", "位置："+position);
						intent.putExtra("newsId", newsList.get(position-1).getInfId());
					}
					context.startActivity(intent);
				}
				isFirstIn = false;
			}
		});
	}

	protected void updateTitle(int position) {
		// TODO Auto-generated method stub
//		int currentPage = loopViewPager.getCurrentItem();
		if(ggList!=null && !ggList.isEmpty()){	
			tv_img_title.setText(ggList.get(position).getInfTitleAbstract());
		}
	}

	protected void updateDot(int position) {
		// TODO Auto-generated method stub
//		int currentPage = loopViewPager.getCurrentItem();
		for(int i = 0;i<ll_dots.getChildCount();i++){
			ll_dots.getChildAt(i).setEnabled(i == position);
		}
	}

	private void setAdapter() {
		// TODO Auto-generated method stub
		loopViewPager.setAdapter(new LoopViewPagerAdapter(context, ggList));
		if(ggList != null){
			handler.sendEmptyMessageDelayed(3, 6000);
		}
//		handler.sendEmptyMessageDelayed(3, 3000);
	}

	private void initDots() {
		// TODO Auto-generated method stub
		try {	
			for(int i = 0;i<ggList.size();i++){
				View view = new View(getContext());
				LayoutParams params = new LayoutParams(15, 15);
				if(i != 0){
					params.leftMargin = 10;	
					view.setEnabled(false);
				}
				
				view.setLayoutParams(params);
				view.setBackgroundResource(R.drawable.dot_selector);
				ll_dots.addView(view);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("ExploreFragment", e.toString());
		}
	}

	private void initData() {
		// TODO Auto-generated method stub
		context = getActivity();
		dialog = ShowDialogUtil.createDialog(context, LOADING_POST);
		news_header = View.inflate(context, R.layout.lv_news_header, null);
		bt_more = (Button)news_header.findViewById(R.id.bt_more);
		lv_news.addHeaderView(news_header);
		isTourist = BusiUtils.isguest(context);
		getGG();
		getNews();
		getTypes();
	}

	private void getGG() {
		// TODO Auto-generated method stub
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryInfAbstracts");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		busiParams.put("typeId", "GG");
		busiParams.put("count", "5");
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			
			@Override
			public void success(JSONObject jsonObject) {
				// TODO Auto-generated method stub
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					ggList = (ArrayList<NewsEntity>) JSON.parseArray(returnObj.getString("ListOut"), NewsEntity.class);
                    if(ggList == null || ggList.isEmpty()){
                    	ToastUtils.showMessage(context, "没有推荐广告");
                    }else{
                    	Log.e("Explore", "广告："+ggList.toString());
                    	handler.sendEmptyMessage(Constant.HAVE_NEW_GG);
                    }
				}else{
					ToastUtils.showMessage(context, "没有推荐广告");
				}
			}
			
			@Override
			public void start() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void loading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				// TODO Auto-generated method stub
				ToastUtils.showMessage(context, "服务器返回数据失败");
			}
		});
	}


	private void getTypes() {
		// TODO Auto-generated method stub
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryInfCol");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			
			@Override
			public void success(JSONObject jsonObject) {
				// TODO Auto-generated method stub
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					typeList = (ArrayList<NewType>)JSON.parseArray(returnObj.getString("ListOut"), NewType.class);
					handler.sendEmptyMessage(Constant.HAVE_NEW_TYPE);
				}else{
					handler.sendEmptyMessage(Constant.HAVE_NEW_TYPE);
				}
			}
			
			@Override
			public void start() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void loading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(Constant.HAVE_NEW_TYPE);
			}
		});
	}

	private void getNews() {
		// TODO Auto-generated method stub
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryInfAbstracts");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		busiParams.put("typeId", Constant.NEW_HOT);
		busiParams.put("baseLev", "0");
		busiParams.put("count", String.valueOf(3));
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
                    	ToastUtils.showMessage(context, "没有最新资讯");
                    }else{
                    	Log.e("Explore", newsList.toString());
                    	handler.sendEmptyMessage(Constant.HAVE_NEW_POST);
                    }
				}else{
					ToastUtils.showMessage(context, "没有最新资讯");
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
	
	private Runnable runnable = new Runnable() {  
		  
	    @Override  
	    public void run() {  
	        sl_explore.scrollTo(0, 0);// 改变滚动条的位置  
	    }  
	};  
	
	public void scrollToTop(){
		if(sl_explore != null){			
			handler.postDelayed(runnable, 50);
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.e("ExploreFragment", "是否第一次进入"+isFirstIn);
		if(!isFirstIn){
			getTypes();
			getNews();
		}
		handler.postDelayed(runnable, 500);
		super.onResume();
	}
}
