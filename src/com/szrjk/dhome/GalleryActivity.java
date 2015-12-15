package com.szrjk.dhome;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.util.ImageItem;
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
	private Button btn_del;
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


	private static List<ImageItem> tmpitems;


	List<ImageItem> iitems;
	ArrayList<String> urllist;
	private ArrayList<View> listViews ;
	private int maxnum;
	private int id;//刚进来的时候，要显示点击的那张，并像是*/* 有3个地方改变；1、监听器滑动;2、del；3、开始

	/**由于 intend 不能传输大量的数据（>1M),这里用静态来做 临时的 items**/
	public static void filltmpitems(List<ImageItem> tmpitems1){
		tmpitems = tmpitems1;
	}

	public static List<ImageItem> gettmpitems(){
		return tmpitems;
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

		ArrayList<String> urllist = bundle.getStringArrayList("urllist");
		//		List<ImageItem> iitems  =  bundle.getParcelableArrayList("imagelist");
		this.urllist = urllist;
		this.iitems = tmpitems;
		tmpitems = null;
		maxnum = urllist.size();

		isShowOkBt();
		//		ACTIVITY = bundle.getInt(Constant.ACTIVITYENUM);

		// 为发送按钮设置文字
		vp_pager.setOnPageChangeListener(pageChangeListener);
		fillData();
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
		for (int i = 0; i < iitems.size(); i++)
		{
			initListViews(iitems.get(i).getBitmap());
		}
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener()
	{

		public void onPageSelected(int arg0)
		{
			location = arg0;
			tv_number.setText(  location+1	+ "/" + listViews.size() );
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
		//当剩余一个的时候，没有移除imgURL。返回的时候出现剩余一张图片地址的情况！
		//		if (listViews.size() == 1)
		//		{
		//			if (ACTIVITY == ActivityEnum.SeedPostActivity.value())
		//			{
		//				iitems.clear();
		//				PhotoConstant.postCount = 0;
		//
		//
		//				System.out.println("删除下标："+location);
		//				//没删除一张图片，就对比一次list
		//				imglist.remove(location);
		//				System.out.println("剩余的图片"+imglist.toString());
		//
		//
		//				strFinish = resources.getString(R.string.finish);
		//				btn_send.setText(strFinish + "("
		//						+ iitems.size() + "/"
		//						+ Constant.maxCount + ")");
		//				Intent intent = new Intent("data.broadcast.action");
		//				//发广播通知预览图库界面更新（被勾选的）猜测、
		//				sendBroadcast(intent);
		//
		//				Intent ipost = new Intent();
		//				Bundle b = new Bundle();
		//				b.putStringArrayList("newImageUrl", imglist);
		//
		//				ipost.putExtras(b);
		//				setResult(RESULT_OK, ipost);
		//
		//
		////				setResult(RESULT_OK);
		//				finish();
		//			}
		//			else if (ACTIVITY == ActivityEnum.SeedCaseActivity1.value())
		//			{
		//				PhotoConstant.caseItems.clear();
		//				PhotoConstant.caseCount = 0;
		//				strFinish = resources.getString(R.string.finish);
		//				btn_send.setText(strFinish + "("
		//						+ PhotoConstant.caseItems.size() + "/"
		//						+ Constant.maxCount + ")");
		//				Intent intent = new Intent("data.broadcast.action");
		//				sendBroadcast(intent);
		//				setResult(RESULT_OK);
		//				finish();
		//			}
		//			else if (ACTIVITY == ActivityEnum.SeedCaseActivity2.value())
		//			{
		//				PhotoConstant.checkItems.clear();
		//				PhotoConstant.checkCount = 0;
		//				strFinish = resources.getString(R.string.finish);
		//				btn_send.setText(strFinish + "("
		//						+ PhotoConstant.checkItems.size() + "/"
		//						+ Constant.maxCount + ")");
		//				Intent intent = new Intent("data.broadcast.action");
		//				sendBroadcast(intent);
		//				setResult(RESULT_OK);
		//				finish();
		//			}
		//			else if (ACTIVITY == ActivityEnum.SeedCaseActivity3.value())
		//			{
		//				PhotoConstant.treatItems.clear();
		//				PhotoConstant.treatCount = 0;
		//				strFinish = resources.getString(R.string.finish);
		//				btn_send.setText(strFinish + "("
		//						+ PhotoConstant.treatItems.size() + "/"
		//						+ Constant.maxCount + ")");
		//				Intent intent = new Intent("data.broadcast.action");
		//				sendBroadcast(intent);
		//				setResult(RESULT_OK);
		//				finish();
		//			}
		//			else if (ACTIVITY == ActivityEnum.SeedCaseActivity4.value())
		//			{
		//				PhotoConstant.visitItems.clear();
		//				PhotoConstant.visitCount = 0;
		//				strFinish = resources.getString(R.string.finish);
		//				btn_send.setText(strFinish + "("
		//						+ PhotoConstant.visitItems.size() + "/"
		//						+ Constant.maxCount + ")");
		//				Intent intent = new Intent("data.broadcast.action");
		//				sendBroadcast(intent);
		//				setResult(RESULT_OK);
		//				finish();
		//			}
		//			else if (ACTIVITY == ActivityEnum.SeedPuzzleActivity1.value())
		//			{
		//				PhotoConstant.puzzleCases.clear();
		//				PhotoConstant.puzzleCaseCount = 0;
		//				strFinish = resources.getString(R.string.finish);
		//				btn_send.setText(strFinish + "("
		//						+ PhotoConstant.puzzleCases.size() + "/"
		//						+ Constant.maxCount + ")");
		//				Intent intent = new Intent("data.broadcast.action");
		//				sendBroadcast(intent);
		//				setResult(RESULT_OK);
		//				finish();
		//			}
		//			else if (ACTIVITY == ActivityEnum.SeedPuzzleActivity2.value())
		//			{
		//				PhotoConstant.puzzleChecks.clear();
		//				PhotoConstant.puzzleCheckCount = 0;
		//				strFinish = resources.getString(R.string.finish);
		//				btn_send.setText(strFinish + "("
		//						+ PhotoConstant.puzzleChecks.size() + "/"
		//						+ Constant.maxCount + ")");
		//				Intent intent = new Intent("data.broadcast.action");
		//				sendBroadcast(intent);
		//				setResult(RESULT_OK);
		//				finish();
		//			}
		//		}
		//		else
		//		{
		//			if (ACTIVITY == ActivityEnum.SeedPostActivity.value())
		//			{
		iitems.remove(location);
		urllist.remove(location);
		//				PhotoConstant.postCount--;

		

		System.out.println("删除下标："+location);
		//没删除一张图片，就对比一次list
		//				iitems.remove(location);
		//				System.out.println("剩余的图片"+imglist.toString());


		vp_pager.removeAllViews();
		listViews.remove(location);
		if (iitems.size() == 0){
			listViews.clear();
			jumpBackActivity();
			return;
		}
		adapter.setListViews(listViews);

		tv_number.setText(  location+1	+ "/" + listViews.size() );
		adapter.notifyDataSetChanged();
		//			}
		//			else if (ACTIVITY == ActivityEnum.SeedCaseActivity1.value())
		//			{
		//				PhotoConstant.caseItems.remove(location);
		//				PhotoConstant.caseCount--;
		//				vp_pager.removeAllViews();
		//				listViews.remove(location);
		//				adapter.setListViews(listViews);
		//				strFinish = resources.getString(R.string.finish);
		//				btn_send.setText(strFinish + "("
		//						+ PhotoConstant.caseItems.size() + "/"
		//						+ Constant.maxCount + ")");
		//			}
		//			else if (ACTIVITY == ActivityEnum.SeedCaseActivity2.value())
		//			{
		//				PhotoConstant.checkItems.remove(location);
		//				PhotoConstant.checkCount--;
		//				vp_pager.removeAllViews();
		//				listViews.remove(location);
		//				adapter.setListViews(listViews);
		//				strFinish = resources.getString(R.string.finish);
		//				btn_send.setText(strFinish + "("
		//						+ PhotoConstant.checkItems.size() + "/"
		//						+ Constant.maxCount + ")");
		//			}
		//			else if (ACTIVITY == ActivityEnum.SeedCaseActivity3.value())
		//			{
		//				PhotoConstant.treatItems.remove(location);
		//				PhotoConstant.treatCount--;
		//				vp_pager.removeAllViews();
		//				listViews.remove(location);
		//				adapter.setListViews(listViews);
		//				strFinish = resources.getString(R.string.finish);
		//				btn_send.setText(strFinish + "("
		//						+ PhotoConstant.treatItems.size() + "/"
		//						+ Constant.maxCount + ")");
		//			}
		//			else if (ACTIVITY == ActivityEnum.SeedCaseActivity4.value())
		//			{
		//				PhotoConstant.visitItems.remove(location);
		//				PhotoConstant.visitCount--;
		//				vp_pager.removeAllViews();
		//				listViews.remove(location);
		//				adapter.setListViews(listViews);
		//				strFinish = resources.getString(R.string.finish);
		//				btn_send.setText(strFinish + "("
		//						+ PhotoConstant.visitItems.size() + "/"
		//						+ Constant.maxCount + ")");
		//			}
		//			else if (ACTIVITY == ActivityEnum.SeedPuzzleActivity1.value())
		//			{
		//				PhotoConstant.puzzleCases.remove(location);
		//				PhotoConstant.puzzleCaseCount--;
		//				vp_pager.removeAllViews();
		//				listViews.remove(location);
		//
		//				//没删除一张图片，就对比一次list
		//				imglist.remove(location);
		//				System.out.println("剩余的图片"+imglist.toString());
		//
		//				adapter.setListViews(listViews);
		//				strFinish = resources.getString(R.string.finish);
		//				btn_send.setText(strFinish + "("
		//						+ PhotoConstant.puzzleCases.size() + "/"
		//						+ Constant.maxCount + ")");
		//			}
		//			else if (ACTIVITY == ActivityEnum.SeedPuzzleActivity2.value())
		//			{
		//				PhotoConstant.puzzleChecks.remove(location);
		//				PhotoConstant.puzzleCheckCount--;
		//				vp_pager.removeAllViews();
		//				listViews.remove(location);
		//
		//				//没删除一张图片，就对比一次list
		//				imglist.remove(location);
		//				System.out.println("剩余的图片"+imglist.toString());
		//
		//				adapter.setListViews(listViews);
		//				strFinish = resources.getString(R.string.finish);
		//				btn_send.setText(strFinish + "("
		//						+ PhotoConstant.puzzleChecks.size() + "/"
		//						+ Constant.maxCount + ")");
		//			}
		//		}
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
		tmpitems = iitems;
		ipost.putExtras(b);
		setResult(RESULT_OK, ipost);
		finish();
		//		}
		//		if (ACTIVITY == ActivityEnum.SeedCaseActivity1.value()
		//				|| ACTIVITY == ActivityEnum.SeedCaseActivity2.value()
		//				|| ACTIVITY == ActivityEnum.SeedCaseActivity3.value()
		//				|| ACTIVITY == ActivityEnum.SeedCaseActivity4.value())
		//		{
		//			intent.setClass(instance, SendCaseActivity.class);
		//			startActivity(intent);
		//		}
		//		if (ACTIVITY == ActivityEnum.SeedPuzzleActivity1.value()
		//				|| ACTIVITY == ActivityEnum.SeedPuzzleActivity2.value())
		//		{
		//			//			intent.setClass(instance, SendPuzzleActivity.class);
		//			//			startActivity(intent);
		//			Intent ipost = new Intent();
		//			Bundle b = new Bundle();
		//			b.putStringArrayList("newImageUrl", imglist);
		//
		//			ipost.putExtras(b);
		//			setResult(RESULT_OK, ipost);
		//			finish();
		//		}
		//		finish();
	}


	public void isShowOkBt()
	{
		if (iitems.size() > 0)
		{
			strFinish = resources.getString(R.string.finish);
			btn_send.setText(strFinish + "(" + iitems.size()
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
		if (iitems.size() > 0 ) {
			tv_number.setText(  id	+ "/" + iitems.size() );
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
	//	@Override
	//	public boolean onKeyDown(int keyCode, KeyEvent event)
	//	{
	//
	//		if (keyCode == KeyEvent.KEYCODE_BACK)
	//		{
	//			jumpBackActivity();
	//		}
	//		return true;
	//	}

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
