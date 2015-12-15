package com.szrjk.dhome;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.entity.IContextClickOper;
import com.szrjk.entity.IPhotoClickOper;
import com.szrjk.entity.OperContextClick;
import com.szrjk.util.ImageItem;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.zoom.PhotoView;
import com.szrjk.zoom.ViewPagerFixed;

@ContentView(R.layout.activity_indexgallery)
public class IndexGalleryActivity extends Activity
{
	@ViewInject(R.id.rl_glob)
	private RelativeLayout rl_glob;
	private Intent intent;
	@ViewInject(R.id.btn_send)
	private Button btn_send;
	@ViewInject(R.id.tv_context)
	private TextView tv_context;
	// 当前的位置
	private int location = 0;

	@ViewInject(R.id.tv_number)
	private TextView tv_number;

	@ViewInject(R.id.vp_pager)
	private ViewPagerFixed vp_pager;
	private MyPageAdapter adapter;
	private Resources resources;
	private Bundle bundle;
	private static List<ImageItem> tmpitems;
	List<ImageItem> iitems;
	ArrayList<String> urllist;
	private ArrayList<View> listViews = new ArrayList<View>();
	private int maxnum;
	private int id;//刚进来的时候，要显示点击的那张，并像是*/* 有3个地方改变；1、监听器滑动;2、del；3、开始
	private String[] imgs;
	private IndexGalleryActivity instance;
	private String contextText;
	private OperContextClick operContextClick;
	private String postId;
	private int postType;


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		System.out.println("进入预览界面");
		instance = this;
		resources = getResources();
		intent = getIntent();
		bundle = intent.getExtras();
		imgs = intent.getStringArrayExtra("imgs");
		id = intent.getIntExtra("position", 0);
		postId=intent.getStringExtra("postId");
		postType=intent.getIntExtra("postType",0);
		contextText = intent.getStringExtra("contextText");
		operContextClick = (OperContextClick) intent.getSerializableExtra("operInterface");
		if(contextText != null){
			tv_context.setText(contextText);
		}
		tv_context.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(operContextClick != null){
					operContextClick.clickoper(id, imgs,IndexGalleryActivity.this);
				}
			}
		});

		maxnum = imgs.length;

		vp_pager.setOnPageChangeListener(pageChangeListener);

		fillData();
		adapter = new MyPageAdapter(listViews);
		vp_pager.setAdapter(adapter);
		int margin = resources.getDimensionPixelOffset(R.dimen.album_margin);
		vp_pager.setPageMargin(margin);
		id = bundle.getInt("position", 0);
		if (id == 0) {
			tv_number.setText(  id+1	+ "/" + listViews.size() );
		}
		vp_pager.setCurrentItem(id);
		//		vp_pager.setOnClickListener(new OnClickListener() {
		//			@Override
		//			public void onClick(View v) {
		//				System.out.println("点击了");
		//				finish();
		//			}
		//		});
	}

	public String getPostId(){
		return postId;
	}
	public int getPostType(){
		return postType;
	}
	// 填充数据
	private void fillData()
	{
		{
			for (int i = 0; i < imgs.length; i++) {
				PhotoView iv = new PhotoView(this);
				try {
					ImageLoaderUtil loader = new ImageLoaderUtil(this, imgs[i], iv, R.drawable.pic_downloadfailed_bg,
							R.drawable.pic_downloadfailed_bg);
					loader.showImage();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("ImageLoader", e.toString());
				}

				listViews.add(iv);
			}
		}
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener()
	{

		public void onPageSelected(int arg0)
		{
			location = arg0;
			tv_number.setText(  location+1	+ "/" + listViews.size() );
			id = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2)
		{

		}

		public void onPageScrollStateChanged(int arg0)
		{

		}
	};

	private void initListViews(Bitmap bm)
	{
		//		if (listViews == null) listViews = new ArrayList<View>();
		//		PhotoView img = new PhotoView(this);
		//		img.setBackgroundColor(0xff000000);
		//		img.setImageBitmap(bm);
		//		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		//				LayoutParams.MATCH_PARENT));
		//		listViews.add(img);

	}


	@OnClick(R.id.ib_back)
	public void backClick(View v){
		finish();
	}

	class MyPageAdapter extends PagerAdapter
	{

		private ArrayList<View> listViews;

		private int size;

		public MyPageAdapter(ArrayList<View> listViews)
		{
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews)
		{
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount()
		{
			return size;
		}

		public int getItemPosition(Object object)
		{

			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2)
		{
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0)
		{
		}


		//		public Object instantiateItem(View arg0, int arg1)
		//		{
		//			try
		//			{
		//				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);
		//				
		//				View v = listViews.get(arg1);
		//				v.setOnClickListener(new OnClickListener() {
		//					
		//					@Override
		//					public void onClick(View v) {
		//						// TODO Auto-generated method stub
		//					System.out.println("2222222222222222222222222");	
		//					}
		//				});
		//
		//			}
		//			catch (Exception e)
		//			{
		//				e.printStackTrace();
		//			}
		//			return listViews.get(arg1 % size);
		//		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			View view = listViews.get(position);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.i("xl", "xl:arrive here.");
				}
			});
			ViewPager viewPager = (ViewPager) container;


			viewPager.addView(view);
			return listViews.get(position);
			//return super.instantiateItem(container, position);
		}

		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == arg1;
		}

	}
}
