package com.szrjk.dhome;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.util.BitmapCompressImage;
import com.szrjk.util.ImageItem;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.zoom.PhotoView;
import com.szrjk.zoom.ViewPagerFixed;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_gallery)
public class GalleryActivity extends Activity
{
	private Intent intent;
	@ViewInject(R.id.btn_send)
	private Button btn_send;
	@ViewInject(R.id.btn_del)
	private ImageButton btn_del;
	// 当前的位置
	private int location = 0;

	@ViewInject(R.id.tv_number)
	private TextView tv_number;

	@ViewInject(R.id.vp_pager)
	private ViewPagerFixed vp_pager;
	private MyPageAdapter adapter;
	private Resources resources;
	private GalleryActivity instance;
	//	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	//	public List<String> drr = new ArrayList<String>();
	//	public List<String> del = new ArrayList<String>();
	private String strFinish;
	private Bundle bundle;
	//	RelativeLayout photo_relativeLayout;
	//	private String urladdress;
	//	private ArrayList<String> imglist;
	//	private ArrayList<String> imglist_pu_case;
	//	private ArrayList<String> imglist_pu_check;


	//private static List<ImageItem> tmpitems;


	public static List<ImageItem> iitems;
	ArrayList<String> urllist;
	private ArrayList<View> listViews ;
	private int maxnum;
	private int id;//刚进来的时候，要显示点击的那张，并像是*/* 有3个地方改变；1、监听器滑动;2、del；3、开始
	private ArrayList<String> absList;

	/**由于 intend 不能传输大量的数据（>1M),这里用静态来做 临时的 items**/
//	public static void filltmpitems(List<ImageItem> tmpitems1){
//		tmpitems = tmpitems1;
//	}
//
	public static List<ImageItem> gettmpitems(){
		return iitems;
	}


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		resources = getResources();
		intent = getIntent();
		bundle = intent.getExtras();

		//普通帖子
		//		{
		//
		//			urladdress = intent.getStringExtra("urllist");
		//			if (urladdress != null) {
		//				System.out.println("-------------------------------");
		//				System.out.println("编辑界面接收："+urladdress);
		//
		//				String ua[] = urladdress.split("\\|");
		//
		//				System.out.println("拆分之后的数组："+Arrays.toString(ua));
		//
		//				imglist= new ArrayList<String>(Arrays.asList(ua));
		//				System.out.println("总图片："+imglist.size());
		//				System.out.println("变成list："+imglist.toString());
		//			}else{
		//				System.out.println("urladdress==null");
		//			}
		//			System.out.println("-------------------------------");
		//		}

		urllist = bundle.getStringArrayList("urllist");
		absList = bundle.getStringArrayList("absList");
		
		//		List<ImageItem> iitems  =  bundle.getParcelableArrayList("imagelist");
//		this.iitems = tmpitems;
//		tmpitems = null;
		maxnum = urllist.size();

		
		//		ACTIVITY = bundle.getInt(Constant.ACTIVITYENUM);

		// 为发送按钮设置文字
		vp_pager.setOnPageChangeListener(pageChangeListener);
		fillData();
		isShowOkBt();
		adapter = new MyPageAdapter(listViews);
		vp_pager.setAdapter(adapter);
		int margin = resources.getDimensionPixelOffset(R.dimen.album_margin);
		vp_pager.setPageMargin(margin);
		id = bundle.getInt("id", 0);//在外面点击了哪一张，就显示current
		if (id == 0) {
			tv_number.setText(  id+1	+ "/" + listViews.size() );
		}
		vp_pager.setCurrentItem(id);
	}

	// 填充数据
	private void fillData()
	{
		
//		iitems = new ArrayList<ImageItem>();
//		for (int i = 0; i < absList.size(); i++)
//		{
////			initListViews(iitems.get(i).getBitmap()); 
//			//根据路径，获取对应的图片
//			Bitmap b = BitmapCompressImage.getimage(absList.get(i));
//			//构造显示的view
//			initListViews(b); 
//			ImageItem item = new ImageItem();
//			item.setAbsPaht(absList.get(i));
//			iitems.add(item);
//		}
		listViews = new ArrayList<View>();
		for (int i = 0; i < absList.size(); i++) {
			PhotoView iv = new PhotoView(this);
			try {
//				ImageLoaderUtil loader = new ImageLoaderUtil(this, absList.get(i), iv, R.drawable.pic_downloadfailed_bg,
//						R.drawable.pic_downloadfailed_bg);
//				loader.showImage();
				BitmapUtils bitmapUtils = new BitmapUtils(instance);
				bitmapUtils.display(iv, absList.get(i));
			} catch (Exception e) {
				Log.e("ImageLoader", e.toString());
			}
			listViews.add(iv);
		}
		
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener()
	{

		@Override
		public void onPageSelected(int arg0)
		{
			location = arg0;
			tv_number.setText(  location+1	+ "/" + listViews.size() );
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{

		}

		@Override
		public void onPageScrollStateChanged(int arg0)
		{

		}
	};

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

	@OnClick(R.id.btn_del)
	public void delClick(View v)
	{
		
//		iitems.remove(location);
		urllist.remove(location);
		absList.remove(location);
		System.out.println("删除下标："+location);
		//没删除一张图片，就对比一次list
		//				iitems.remove(location);
		//				System.out.println("剩余的图片"+imglist.toString());
		vp_pager.removeAllViews();
		listViews.remove(location);
		if (absList.size() == 0){
			listViews.clear();
			jumpBackActivity();
			return;
		}
		adapter.setListViews(listViews);

		tv_number.setText(  location+1	+ "/" + listViews.size() );
		adapter.notifyDataSetChanged();
	}

	@OnClick(R.id.ib_back)
	public void backClick(View v){
		jumpBackActivity();
	}

	@OnClick(R.id.btn_send)
	public void sendClick(View v)
	{
		jumpBackActivity();
	}

	private void jumpBackActivity()
	{
		//		if (ACTIVITY == ActivityEnum.SeedPostActivity.value())
		//		{
		//			intent.setClass(instance, SendPostActivity.class);
		Intent ipost = new Intent();
		Bundle b = new Bundle();
		b.putStringArrayList("urllist", urllist);
		b.putStringArrayList("absList", absList);
		
//		tmpitems = iitems;
		ipost.putExtras(b);
		setResult(RESULT_OK, ipost);
		finish();
		
	}


	public void isShowOkBt()
	{
		if (absList.size() > 0)
		{
			strFinish = resources.getString(R.string.finish);
			btn_send.setText(strFinish + "(" + absList.size()
					+ "/" + maxnum + ")");
			btn_send.setPressed(true);
			btn_send.setClickable(true);
			btn_send.setTextColor(Color.WHITE);
		}
		else
		{
			btn_send.setPressed(false);
			btn_send.setClickable(false);
			btn_send.setTextColor(Color.parseColor("#E1E0DE"));
		}
		if (absList.size() > 0 ) {
			tv_number.setText(  id	+ "/" + absList.size() );
		}
	}

	/**
	 * 监听返回按钮
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		jumpBackActivity();
		return false;
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

		@Override
		public int getCount()
		{
			return size;
		}

		@Override
		public int getItemPosition(Object object)
		{
			return POSITION_NONE;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2)
		{
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		@Override
		public void finishUpdate(View arg0)
		{
		}

		@Override
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

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == arg1;
		}

	}
}
