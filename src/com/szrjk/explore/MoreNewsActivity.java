package com.szrjk.explore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.TabPageIndicatorAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.NewType;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.widget.AddNewsTypePopup2;
import com.szrjk.widget.ColumnHorizontalScrollView;
import com.szrjk.widget.HeaderView;



@ContentView(R.layout.activity_more_news)
public class MoreNewsActivity extends FragmentActivity {
	private MoreNewsActivity instance;
	@ViewInject(R.id.mColumnHorizontalScrollView)
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	@ViewInject(R.id.mRadioGroup_content)
	private LinearLayout mRadioGroup_content;
	@ViewInject(R.id.rl_column)
	//
	private RelativeLayout rl_column;
	@ViewInject(R.id.shade_left)
	public ImageView shade_left;
	@ViewInject(R.id.shade_right)
	public ImageView shade_right;
	@ViewInject(R.id.vp_new)
	private ViewPager viewPager;
	@ViewInject(R.id.bt_more_type)
	private Button bt_more_type;
	@ViewInject(R.id.ll_indicator)
	private LinearLayout ll_indicator;
	@ViewInject(R.id.bt_more_type_close)
	private Button bt_more_type_close;
	@ViewInject(R.id.ll_my_news)
	private LinearLayout ll_my_news;
	@ViewInject(R.id.hv_header)
	private HeaderView hv_header;
	@ViewInject(R.id.ll_more_news_activity)
	private LinearLayout ll_more_news_activity;
	@ViewInject(R.id.fl_more_type)
	private FrameLayout fl_more_type;
	private TabPageIndicatorAdapter mAdapter;
	private ArrayList<NewType> titles;
	private ArrayList<String> titles_id;
	private ArrayList<String> indicatorTitle;
	private ArrayList<NewsFragment> fragments = new ArrayList<NewsFragment>();
	private AddNewsTypePopup2 addNewsTypePopup;
	private int columnSelectIndex = 0;
	private int mScreenWidth = 0;
	private int mItemWidth = 0;
	private String title;
	private NewType nType;
	private boolean popUpShowing;
	private int headerHeight;
	private int indicatorHeight;
	private int mScreenHeight;
	private int popupHeight;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.HAVE_NEW_TYPE:
				Bundle bundle = msg.getData();
				titles_id = bundle.getStringArrayList("title_id");
				indicatorTitle = bundle.getStringArrayList("title");
				if(titles_id.isEmpty()){
					titles_id.add("RD");
				}
				if(indicatorTitle.isEmpty()){
					indicatorTitle.add("热点");
				}
				Log.e("MoreNews", titles_id.toString());
				setTypeView();	
				break;
			}
		};
	};
	


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		initData();
		initListner();
	}

	private void initListner() {
		bt_more_type.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				try {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(instance, TestActivity.class);
//				startActivity(intent);
				viewPager.setCurrentItem(0);
				int w = View.MeasureSpec.makeMeasureSpec(0,
		                View.MeasureSpec.UNSPECIFIED);
		        int h = View.MeasureSpec.makeMeasureSpec(0,
		                View.MeasureSpec.UNSPECIFIED);
		        mColumnHorizontalScrollView.measure(w, h);
		        int height = mColumnHorizontalScrollView.getMeasuredHeight();
		        int width = mColumnHorizontalScrollView.getMeasuredWidth();
//				addNewsTypePopup.showPopup(popupHeight);
		        addNewsTypePopup = new AddNewsTypePopup2(instance, titles_id, indicatorTitle, ll_indicator,bt_more_type,bt_more_type_close,ll_my_news,mColumnHorizontalScrollView,mAdapter,viewPager,handler,width);
		        if(Constant.CURRENTAPIVERSION < 19){ 	
		        	addNewsTypePopup.showAtLocation(bt_more_type, Gravity.NO_GRAVITY, 0, 0);
		        }else{
		        	addNewsTypePopup.showAsDropDown(ll_indicator, 0, 0, Gravity.CENTER_HORIZONTAL);
		        }
				
				ObjectAnimator animator = ObjectAnimator.ofFloat(bt_more_type, "rotation", 0f, 180f);
				animator.setDuration(300);  
				animator.start();
				ObjectAnimator gone = ObjectAnimator.ofFloat(mColumnHorizontalScrollView, "alpha", 1f, 0f);
				gone.setDuration(300);  
				gone.start();
				ll_my_news.setVisibility(View.VISIBLE);
				ObjectAnimator in = ObjectAnimator.ofFloat(ll_my_news, "translationX", width, 0);  
				ObjectAnimator show = ObjectAnimator.ofFloat(ll_my_news, "alpha", 0f, 1f);
				AnimatorSet animSet = new AnimatorSet();
				animSet.play(in).with(show);
				animSet.setDuration(300);  
				animSet.start();
//				bt_more_type_close.setVisibility(View.VISIBLE);
//				bt_more_type.setVisibility(View.GONE);
//				mColumnHorizontalScrollView.setVisibility(View.GONE);
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("MoreNewsActivity", e.toString());
				}
			}
		});
	}


	private void initData() {
		// TODO Auto-generated method stub
		indicatorTitle = new ArrayList<String>();
		titles_id = new ArrayList<String>();
		getTpyes();
//		Intent intent = getIntent();
//		titles = (ArrayList<NewType>) intent.getSerializableExtra("typeList");
//		if(titles == null || titles.isEmpty()){
//			titles_id.add("RD");
//			indicatorTitle.add("热点");
//		}else{
//			for (NewType i : titles) {
//				String type = i.getTypeId();
//				titles_id.add(type);
//				String title = i.getTypeName();
//				indicatorTitle.add(title);
//			}
//		}
		mScreenWidth = getWindowsWidth();
		mItemWidth = (mScreenWidth / 4)-20;
//		setTypeView();
		

	}
	
	private void getTpyes() {
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
					titles = (ArrayList<NewType>)JSON.parseArray(returnObj.getString("ListOut"), NewType.class);
					if(titles == null || titles.isEmpty()){
						titles_id.add("RD");
						indicatorTitle.add("热点");
					}else{
						for (NewType i : titles) {
							String type = i.getTypeId();
							titles_id.add(type);
							String title = i.getTypeName();
							indicatorTitle.add(title);
						}
					}
					setTypeView();
				}else{
					titles = new ArrayList<NewType>();
					titles_id.add("RD");
					indicatorTitle.add("热点");
					setTypeView();
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
				titles = new ArrayList<NewType>();
				titles_id.add("RD");
				indicatorTitle.add("热点");
				setTypeView();
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus){
			mScreenHeight = getWindwosHeight();
			headerHeight = getHeaderHeight();
			indicatorHeight = getIndicatorHeight();
			popupHeight = mScreenHeight - headerHeight - indicatorHeight;
		}
	}

	private int getIndicatorHeight() {
		// TODO Auto-generated method stub
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		ll_indicator.measure(w, h);
		Log.e("MoreNews", "指示栏高度："+ll_indicator.getMeasuredHeight());
		return ll_indicator.getMeasuredHeight();
	}

	private int getHeaderHeight() {
		// TODO Auto-generated method stub
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		hv_header.measure(w, h);
		Log.e("MoreNews", "标题栏高度："+hv_header.getMeasuredHeight());
		return hv_header.getMeasuredHeight();
	}

	private int getWindwosHeight() {
		// TODO Auto-generated method stub
		 Rect outRect = new Rect();  
	        this.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);    
	        Log.e("MoreNews", "屏幕高度："+outRect.height());
		return outRect.height(); 
	}

	public void setTypeView() {
		// TODO Auto-generated method stub
		initTabColumn();
		initFragment();
	}


	private void initFragment() {
		// TODO Auto-generated method stub
		fragments.clear();//清空
		int count =  indicatorTitle.size();
		for(int i = 0; i< count;i++){
			Bundle args = new Bundle();
			Log.e("MoreNews", "标题id:"+titles_id.get(i));
			args.putString("titles_id", titles_id.get(i));
			NewsFragment fragment = new NewsFragment();
			fragment.setArguments(args);
			fragments.add(fragment);
		}
		mAdapter = new TabPageIndicatorAdapter(getSupportFragmentManager(), fragments);
		viewPager.setAdapter(mAdapter);
		viewPager.setOnPageChangeListener(pageListener);
		
	}
	
	/** 
	 *  ViewPager切换监听方法
	 * */
	public OnPageChangeListener pageListener= new OnPageChangeListener(){

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			viewPager.setCurrentItem(position);
			selectTab(position);
		}
	};

	private void initTabColumn() {
		// TODO Auto-generated method stub
		mRadioGroup_content.removeAllViews();
		int count =  indicatorTitle.size();
		mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, bt_more_type, rl_column);
		for(int i = 0; i< count; i++){
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth , LayoutParams.MATCH_PARENT);
//			params.leftMargin = 5;
//			params.rightMargin = 5;
//			TextView localTextView = (TextView) mInflater.inflate(R.layout.column_radio_item, null);
			TextView columnTextView = new TextView(this);
			columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
			columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 20, 5, 20);
			columnTextView.setId(i);
			columnTextView.setText(indicatorTitle.get(i));
//			columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
			if(columnSelectIndex == i){
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
			          for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
				          View localView = mRadioGroup_content.getChildAt(i);
				          if (localView != v)
				        	  localView.setSelected(false);
				          else{
				        	  localView.setSelected(true);
				        	  viewPager.setCurrentItem(i);
				          }
			          }
				}
			});
			mRadioGroup_content.addView(columnTextView, i ,params);
		}
	}
	
	/** 
	 *  选择的Column里面的Tab
	 * */
	private void selectTab(int tab_postion) {
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			// rg_nav_content.getParent()).smoothScrollTo(i2, 0);
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
			// mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
			// mItemWidth , 0);
		}
		//判断是否选中
		for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}
	
	

	private int getWindowsWidth() {
		// TODO Auto-generated method stub
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	private void getNewType() {
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
					titles = (ArrayList<NewType>)JSON.parseArray(returnObj.getString("ListOut"), NewType.class);
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
//		if(addNewsTypePopup != null){
//			if(addNewsTypePopup.isShowing()){
//				popUpShowing = true;
//			}else{
//				popUpShowing = false;
//			}
//		}
		super.onResume();
	}
}
