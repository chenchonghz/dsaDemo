package com.szrjk.explore;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.CommendListAdapter;
import com.szrjk.adapter.ExploreNewsListAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.NewsDetailEntity;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.ChangeWebViewFontSize;
import com.szrjk.util.ShowDialogUtil;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.HeaderView;
import com.szrjk.widget.IndexRecommandListView;
import com.szrjk.widget.InnerListView;

@ContentView(R.layout.activity_news_detail)
public class NewsDetailActivity extends Activity {
	private NewsDetailActivity instance;
	@ViewInject(R.id.wv_detail)
	private WebView wv_detail;
	@ViewInject(R.id.lv_news)
	private IndexRecommandListView lv_news;
	@ViewInject(R.id.lv_commends)
	private InnerListView lv_comments;
//	private ListView lv_commends;
	@ViewInject(R.id.rl_commend)
	private RelativeLayout rl_commend;
	@ViewInject(R.id.tv_commend_count)
	private TextView tv_commend_count;
	@ViewInject(R.id.sv_news_detail)
	private ScrollView sv_news_detail;
	@ViewInject(R.id.ib_big_comment)
	private ImageButton ib_big_comment;
	@ViewInject(R.id.ib_big_up)
	private ImageButton ib_big_up;
	@ViewInject(R.id.hv_title)
	private HeaderView hv_title;
	private String news_id;
	private Dialog dialog;
	private NewsDetailEntity newsDetails;
	private ExploreNewsListAdapter adapter;
	private CommendListAdapter commendAdapter;
	private static final String LOADING_POST = "正在加载资讯";
	private View ref_news_header;
	private boolean isFirstIn = true;
	private boolean haveMore = true;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.HAVE_NEW_POST:
				if(isFirstIn){	
					showWeb();
					isFirstIn = false;
				}else{
					tv_commend_count.setText(String.valueOf(newsDetails.getCommentCount()));
					showNewsList();
					showCommendList();
				}
				break;
			
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(instance);
		initData();
		initListner();
		
	}
	

	private void initListner() {
		// TODO Auto-generated method stub
		lv_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NewsDetailActivity.this, NewsDetailActivity.class);
				if(newsDetails.getRelInfs() != null && !newsDetails.getRelInfs().isEmpty()){
					Log.e("Explore", "位置："+position);
					intent.putExtra("newsId", newsDetails.getRelInfs().get(position-1).getInfId());
				}
				startActivity(intent);
				isFirstIn = false;
			}
		});
		rl_commend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(instance, NewsCommentActivity.class);
				intent.putExtra("infId", news_id);
				startActivity(intent);
				isFirstIn = false;
			}
		});
		wv_detail.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		ib_big_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(instance, NewsCommentActivity.class);
				intent.putExtra("infId", news_id);
				startActivity(intent);
				isFirstIn = false;
			}
		});
		
		ib_big_up.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sv_news_detail.scrollTo(0, 0);
			}
		});
		
	}

	protected void showCommendList() {
		// TODO Auto-generated method stub
		commendAdapter = new CommendListAdapter(this, newsDetails.getComments());
		lv_comments.setAdapter(commendAdapter);
		commendAdapter.notifyDataSetChanged();
		if(newsDetails.getCommentCount()>5){
			if(haveMore){
				haveMore = false;
				View view = View.inflate(instance, R.layout.item_footer_view, null);
				TextView tv_more = (TextView)view.findViewById(R.id.tv_footer_view);
				tv_more.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(instance, MoreNewsCommentsActivity.class);
						intent.putExtra("comments", newsDetails.getComments());
						intent.putExtra("infId",newsDetails.getInfId());
						startActivity(intent);
					}
				});
				lv_comments.addFooterView(view);
			}
		}
		
	}

	protected void showNewsList() {
		// TODO Auto-generated method stub
		adapter = new ExploreNewsListAdapter(instance, newsDetails.getRelInfs());
		lv_news.setAdapter(adapter);
	}

	protected void showWeb() {
		// TODO Auto-generated method stub
		String newsData = newsDetails.getInfCotent().trim();
		wv_detail.loadDataWithBaseURL(null, newsData, "text/html", "utf-8", null);
		wv_detail.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				if(newProgress == 100){
					showNewsList();
					tv_commend_count.setText(String.valueOf(newsDetails.getCommentCount()));
					showCommendList();
				}
			}
		});

//		settings.setSupportZoom(true);
//		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
	}

	private void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		news_id = intent.getStringExtra("newsId");
//		lv_commends = mPullRefreshInnerListView.getRefreshableView();
//		mPullRefreshInnerListView.setMode(Mode.PULL_UP_TO_REFRESH);
//		mPullRefreshInnerListView.getLoadingLayoutProxy(false, true).setPullLabel(
//				getResources().getString(R.string.pull_up_lable_text));
//		mPullRefreshInnerListView.getLoadingLayoutProxy(false, true)
//		.setRefreshingLabel(
//				getResources()
//				.getString(R.string.refreshing_lable_text));
//		mPullRefreshInnerListView.getLoadingLayoutProxy(false, true)
//		.setReleaseLabel(
//				getResources().getString(R.string.release_lable_text));
//		lv_comments.setParentScrollView(sv_news_detail);
//		lv_comments.setMaxHeight(965);
		dialog = ShowDialogUtil.createDialog(this, LOADING_POST);
		ref_news_header = View.inflate(this, R.layout.lv_ref_news_header, null);
		lv_news.addHeaderView(ref_news_header);
		WebSettings settings = wv_detail.getSettings(); 
			  
		// User settings          
		    
//		settings.setJavaScriptCanOpenWindowsAutomatically(true);  
//		settings.setUseWideViewPort(true);//关键点    
//		settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);        
//		settings.setDisplayZoomControls(false);  
//		settings.setJavaScriptEnabled(true); // 设置支持javascript脚本  
//		settings.setAllowFileAccess(true); // 允许访问文件   
//		settings.setSupportZoom(true); // 支持缩放  
//		  
//		  
//		  
//		settings.setLoadWithOverviewMode(true);  
//		  
//		DisplayMetrics metrics = new DisplayMetrics();  
//		  getWindowManager().getDefaultDisplay().getMetrics(metrics);  
//		  int mDensity = metrics.densityDpi;  
//		  Log.d("maomao", "densityDpi = " + mDensity);  
//		  if (mDensity == 240) {   
//			  settings.setDefaultZoom(ZoomDensity.FAR);  
//		  } else if (mDensity == 160) {  
//			  settings.setDefaultZoom(ZoomDensity.MEDIUM);  
//		  } else if(mDensity == 120) {  
//			  settings.setDefaultZoom(ZoomDensity.CLOSE);  
//		  }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){  
//			  settings.setDefaultZoom(ZoomDensity.FAR);   
//		  }else if (mDensity == DisplayMetrics.DENSITY_TV){  
//			  settings.setDefaultZoom(ZoomDensity.FAR);   
//		  }else{  
//			  settings.setDefaultZoom(ZoomDensity.MEDIUM);  
//		  }
		  settings.setDomStorageEnabled(true);
		  settings.setDatabaseEnabled(true);
		  settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		ChangeWebViewFontSize cvfs = new ChangeWebViewFontSize();
		cvfs.setFontSize(instance, settings);
//		lv_commends.setParentScrollView(sv_news_detail);
//		lv_commends.setMaxHeight(965);
		    getNewsDetail();
		}	


	private void getNewsDetail() {
		// TODO Auto-generated method stub
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryInfDetail");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("infId", news_id);
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
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
					newsDetails = JSON.parseObject(returnObj.getString("ListOut"), NewsDetailEntity.class);
//					Log.e("NewsDetail", newsDetails.getComments().toString());
					handler.sendEmptyMessage(Constant.HAVE_NEW_POST);
				}else{
					ToastUtils.showMessage(instance, "获取详情失败");
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
				if (dialog.isShowing())
				{
					dialog.dismiss();
				}
				ToastUtils.showMessage(instance, "获取详情失败，请检查网络");
			}
		});
	}
	private Runnable runnable = new Runnable() {  
		  
	    @Override  
	    public void run() {  
	    	getNewsDetail();
	    }  
	};  
	
	@Override
	protected void onResume() {
		if(!isFirstIn){	
			Log.e("NewsDetail", "我要刷新页面");
			handler.postDelayed(runnable, 800);
		}
		super.onResume();
	}
}
