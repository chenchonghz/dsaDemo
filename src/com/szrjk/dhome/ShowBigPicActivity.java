package com.szrjk.dhome;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szrjk.dhome.R;
import com.szrjk.util.BitMapUtil;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.widget.HeaderView;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * 帖子大图浏览，使用ViewPager展示，可左右滑动
 * @author liyi
 * 
 */
@ContentView(R.layout.activity_index_bigpic_shower)
public class ShowBigPicActivity extends Activity{
	@ViewInject(R.id.vp_user_background_changer)
	private ViewPager vp_user_bg;
	@ViewInject(R.id.btn_user_background_change)
	private Button btn_user_bg_change;
	@ViewInject(R.id.hv_user_background_changer)
	private HeaderView hv_user_background;
	private String[]imgs ;
	private int position;
	private String title;
	private ArrayList<ImageView> viewContainter = new ArrayList<ImageView>();
	private ImageLoaderUtil imageLoader;
//	private ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		//获取intent信息
		getIntents();
		//设置默认title
		hv_user_background.setHtext(title);
		//设置ViewPager
		setViewPager();
		//设置ViewPager适配器
		setAdapter();
		//设置ViewPager监听器
		initListener();
		
		
	}
	private void setViewPager() {
		for (int i = 0; i < imgs.length; i++) {
			ImageView iv = new ImageView(this);
			try {
				imageLoader = new ImageLoaderUtil(this, imgs[i], iv, R.drawable.pic_downloadfailed_bg, R.drawable.pic_downloadfailed_bg);
				imageLoader.showBigImage();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("ImageLoader", e.toString());
			}
			viewContainter.add(iv);
		}
		
	}
	private void initListener() {
		vp_user_bg.addOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int position) {
				hv_user_background.setHtext((position+1)+"/"+imgs.length);
			}
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
	}
	private void setAdapter() {
		UserBackgroundViewPagerAdapter adapter = 
				new UserBackgroundViewPagerAdapter();
		vp_user_bg.setAdapter(adapter);
		vp_user_bg.setCurrentItem(position);
		
	}
	private void getIntents() {
		Intent intent = getIntent();
		imgs = intent.getStringArrayExtra("imgs");
		position = intent.getIntExtra("position", 0);
		title= intent.getStringExtra("title");
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageLoader loader = ImageLoader.getInstance();
		loader.destroy();
	}

	public class UserBackgroundViewPagerAdapter extends PagerAdapter{

		public int getCount() {
			return viewContainter.size();
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(viewContainter.get(position));  
		}
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(viewContainter.get(position));
			return viewContainter.get(position);
		}
	}
}
