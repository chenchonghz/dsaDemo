package com.szrjk.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.szrjk.adapter.PhotoGridAdapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

public class PostSaveUtil {
	private BaseAdapter adapter;

	private List<ImageItem> newls = new ArrayList<ImageItem>();
	private int imgsize ;

	public  void getSaveImage(String imgurl,BaseAdapter adapter){
		this.adapter = adapter;
		new ImageAsync().execute(imgurl);
	}

	class ImageAsync extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			try {
				URL url = new URL(params[0]);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(8000);
				conn.connect();
				BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
				//				DisplayMetrics metrics = instance.getResources().getDisplayMetrics();
				//				bitmapOptions.inScreenDensity = metrics.densityDpi;
				//				bitmapOptions.inTargetDensity =  metrics.densityDpi;
				//				bitmapOptions.inDensity = DisplayMetrics.DENSITY_DEFAULT;
				Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream(), null, bitmapOptions);
				return bitmap;
			} catch (Exception e) {
				Log.i("sendpuzz", "加载图片出现异常");
				e.printStackTrace();
			}
			return null;
		}
		//这个方法是当工作线程执行完之后自动调用的方法（属于主线程）
		@Override
		protected void onPostExecute(Bitmap result) {
			try {
				//			gridAdapter.addImageList(ls);
				//完成之后，把bitmap，加入
				if (result != null) {
					//				imgsize++;
					ImageItem imageItem = new ImageItem();
					imageItem.setBitmap(result);
					newls.add(imageItem);
				}
				//这里判断如果数组个数（也即是图片个数）== 异步加载的次数、就更新适配器
				//			if (imgsize == img1.length) {
				((PhotoGridAdapter) adapter).addImageList(newls);
				adapter.notifyDataSetChanged();
				//			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
