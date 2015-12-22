package com.szrjk.self;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.MainActivity;
import com.szrjk.dhome.R;
import com.szrjk.dhome.SelfActivity;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.widget.DragImageView;
import com.szrjk.widget.HeaderView;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * 用户背景墙大图浏览，使用ViewPager展示，可左右滑动
 * @author 郑斯铭
 * 图片需要压缩处理显示，未完成
 */
@ContentView(R.layout.activity_user_background_shower)
public class UserBackgroundShower extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.vp_user_background_changer)
	private ViewPager vp_user_bg;
	@ViewInject(R.id.btn_user_background_change)
	private Button btn_user_bg_change;
	@ViewInject(R.id.hv_user_background_changer)
	private HeaderView hv_user_background;
	private String[]imgs ;
	private int position;
	private String title;
	private String Url;
	private boolean needOper;
	private ArrayList<DragImageView> viewContainter = new ArrayList<DragImageView>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		//获取intent信息
		getIntents();
		//压缩图片
		//		imagereduce();
		//设置默认title
		hv_user_background.setHtext(title);
		//设置ViewPager
		try {
			setViewPager();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//设置ViewPager适配器
		setAdapter();
		//设置ViewPager监听器
		initListener();


	}
	private void setViewPager() throws Exception {
		for (int i = 0; i < imgs.length; i++) {
			//			ImageView iv = new ImageView(this);
			//			BitmapUtils bitmapUtils = new BitmapUtils(this);
			//			bitmapUtils.display(iv, imgs[i]);
			//			ImageLoaderUtil loader = new ImageLoaderUtil(this, imgs[i], iv, R.drawable.pic_downloadfailed_bg,
			//					R.drawable.pic_downloadfailed_bg);
			DragImageView iv = new DragImageView(this);
			ImageLoaderUtil loader = new ImageLoaderUtil(this, imgs[i], iv, R.drawable.pic_downloadfailed_bg,
					R.drawable.pic_downloadfailed_bg);
			loader.showImage();
			
			viewContainter.add(iv);
		}
	}
	private void initListener() {
		btn_user_bg_change.setOnClickListener(this);
		vp_user_bg.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				Log.i("TAG","position="+position);
				Url=imgs[position];
				hv_user_background.setHtext((position+1)+"/"+imgs.length);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}
			@Override
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
		imgs =intent.getStringArrayExtra("imgs");
		position = intent.getIntExtra("position", 0);
		title= intent.getStringExtra("title");
		needOper =intent.getBooleanExtra("needOper",true);
		//如果不需要提交，则隐藏掉
		if(!needOper){
			btn_user_bg_change.setVisibility(View.GONE);
		}
		Url = imgs[position];
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_user_background_change:
			//发送请求更改背景墙地址
			sendPost(Url);
			
			//			Log.i("TAG","bg_change"+position_now);
			//			Intent intent = new Intent(this, SelfFragment.class);
			//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			//			Bitmap bm = bitmaps.get(position_now);
			//			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			//			byte[] bs = baos.toByteArray();
			//			Log.i("TAG", bs.toString());
			//            intent.putExtra("img", bs);
			//            
			//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//            startActivity(intent);
			break;

		}

	}
	private void sendChangebg() {
		
		Intent intent = new Intent(this, SelfActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		//		intent.putExtra("bg", url);
		startActivity(intent);

	}


	private void sendPost(String url) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "dealUserUIElement");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		busiParams.put("backgroundUrl",url);
		paramMap.put("BusiParams",busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if(Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					Toast.makeText(UserBackgroundShower.this, "设置成功", Toast.LENGTH_SHORT).show();
					sendChangebg();
				}else{
					Toast.makeText(UserBackgroundShower.this, "设置失败", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void start() {
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {

			}

			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				// TODO Auto-generated method stub

			}
		});
	}

	//ViewPager适配器
	public class UserBackgroundViewPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return viewContainter.size();
		}

		@Override
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
	public static Bitmap getBitMBitmap(String urlpath) throws IOException {
		Bitmap map = null;
		URL url = new URL(urlpath);
		URLConnection conn = url.openConnection();
		conn.connect();
		InputStream in;
		in = conn.getInputStream();
		map = BitmapFactory.decodeStream(in);
		// TODO Auto-generated catch block
		return map;
	}
}
