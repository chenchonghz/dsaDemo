package com.szrjk.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.common.message.Log;

import java.io.File;

public class ImageLoaderUtil {
	private Context context;
	private String url;
	private ImageView imageView;
	private int nullImg;
	private int failImg;
	private static File cacheDir;
	private ImageView iv_gary;
	private ImageLoaderConfiguration imgConfig;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public ImageLoaderUtil(Context context,String url,ImageView imageView,int nullImg,int failImg){
		this.context = context;
		this.url = url;
		this.imageView = imageView;
		this.nullImg = nullImg;
		this.failImg = failImg;
		try {		
			cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/cache", true);
			setImageLoaderConfig();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("ImageLoader", e.toString());
		}
	}
	
	public ImageLoaderUtil(Context context,String url,ImageView imageView,int nullImg,int failImg,ImageView iv_gary){
		this.context = context;
		this.url = url;
		this.imageView = imageView;
		this.nullImg = nullImg;
		this.failImg = failImg;
		this.iv_gary = iv_gary;
		try {
			cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/cache", true);
			setImageLoaderConfig();		
		} catch (Exception e) {
			Log.e("ImageLoader", e.toString());
		}
	}

	public ImageLoaderUtil() {
	}

	private void setImageLoaderConfig(){
		try {
			imgConfig = new ImageLoaderConfiguration.Builder(context)
			.threadPoolSize(2) //线程池内加载的数量
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.memoryCache(new UsingFreqLimitedMemoryCache(2*1024*1024))
			.discCacheSize(50 * 1024 * 1024) 
			.tasksProcessingOrder(QueueProcessingType.LIFO)  
			.discCacheFileCount(100) //缓存的文件数量  
			.discCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
			.defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
	        .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 15 * 1000)) // connectTimeout (5 s), readTimeout (10 s)超时时间
	        .writeDebugLogs().build();
		} catch (Exception e) {
			Log.e("ImageLoader", e.toString());
		}
		
	}
	
	public void showImage(){
		try {
			ImageLoader.getInstance().init(imgConfig);
			DisplayImageOptions ops = new DisplayImageOptions.Builder()
			.showImageOnLoading(failImg)
			.showImageForEmptyUri(nullImg)
			.showImageOnFail(failImg)
			.resetViewBeforeLoading(true)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
			.build();
//			imageLoader.displayImage(url, imageView, ops);
			imageLoader.displayImage(url, imageView, ops,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					if(iv_gary != null){			
						iv_gary.setVisibility(View.GONE);
					}
					
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					if(iv_gary != null){
						iv_gary.setVisibility(View.GONE);
					}
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					// TODO Auto-generated method stub
					if(iv_gary != null){
						if(arg0 == null || arg0.isEmpty()){
							iv_gary.setVisibility(View.GONE);
						}else{
							iv_gary.setVisibility(View.VISIBLE);
						}
					}
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					if(iv_gary != null){	
						iv_gary.setVisibility(View.GONE);
					}
				}
			});
		} catch (Throwable e) {
			// TODO: handle exception
			Log.e("ImageLoader", e.toString());
		}
		
	}
	
	public void showBigImage(){
		try {
			ImageLoader.getInstance().init(imgConfig);
			DisplayImageOptions ops = new DisplayImageOptions.Builder()
			.showImageOnLoading(failImg)
			.showImageForEmptyUri(nullImg)
			.showImageOnFail(failImg)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
			.build();
			imageLoader.getInstance().displayImage(url, imageView, ops);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("ImageLoader", e.toString());
		}
		
	}
	
	

}
