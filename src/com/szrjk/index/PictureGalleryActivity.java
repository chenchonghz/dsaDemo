package com.szrjk.index;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.dhome.R;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.zoom.PhotoView;
import com.szrjk.zoom.ViewPagerFixed;

@ContentView(R.layout.activity_picture_gallery)
public class PictureGalleryActivity extends Activity {

	private Intent intent;
	private Bundle bundle;
	@ViewInject(R.id.vp_pager)
	private ViewPagerFixed vp_pager;
	private ArrayList<View> listViews = null;
	private PictureGralleryAdapter pictureGralleryAdapter;
	private Resources resources;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		intent=getIntent();
		resources = getResources();
		bundle=intent.getExtras();
		int id = bundle.getInt("id", 0);
		String[] picUrls=bundle.getStringArray("picUrls");
		fillPicture(picUrls);
		pictureGralleryAdapter=new PictureGralleryAdapter(listViews);
		vp_pager.setAdapter(pictureGralleryAdapter);
		vp_pager.setOnPageChangeListener(pageChangeListener);
		int margin = resources.getDimensionPixelOffset(R.dimen.album_margin);
		vp_pager.setPageMargin(margin);
		vp_pager.setCurrentItem(id);
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener()
	{

		private int location;

		public void onPageSelected(int position)
		{
			location = position;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2)
		{

		}

		public void onPageScrollStateChanged(int arg0)
		{

		}
	};
	private ImageLoaderUtil imageLoader;
	private void fillPicture(String[] picUrls) {
		try {
			if (picUrls!=null) {
				for (int i = 0; i < picUrls.length; i++) {
					initListViews(picUrls[i]);
				}
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initListViews(Bitmap bm)
	{
		if (listViews == null) listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		listViews.add(img);
	}

	private void initListViews(String picUrl)
	{
		if (listViews == null) listViews = new ArrayList<View>();
		ImageView iv = new ImageView(this);
		
		iv.setBackgroundColor(0xff000000);
		iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		try {
			imageLoader = new ImageLoaderUtil(this, picUrl, iv, R.drawable.pic_downloadfailed_bg, R.drawable.pic_downloadfailed_bg);
			imageLoader.showBigImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		listViews.add(iv);
	}
	class PictureGralleryAdapter extends PagerAdapter
	{

		private ArrayList<View> listViews;

		private int size;

		public PictureGralleryAdapter(ArrayList<View> listViews)
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

		public Object instantiateItem(View arg0, int arg1)
		{
			try
			{
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			}
			catch (Exception e)
			{
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == arg1;
		}

	}
}
