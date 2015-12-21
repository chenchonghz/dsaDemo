package com.szrjk.self.more.album;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.dhome.R;
import com.szrjk.self.more.ChangePortraitActivity;
import com.szrjk.zoom.PhotoView;

@ContentView(R.layout.activity_crop_picture)
public class CropPictureActivity extends Activity {
	
	@ViewInject(R.id.ll_backtoalbum)
	private LinearLayout ll_backtoalbum;

	@ViewInject(R.id.rl_container)
	private RelativeLayout rl_container;

	@ViewInject(R.id.cil_clipImageLayout)
	private CropImageLayout cil_clipImageLayout;
	
	@ViewInject(R.id.btn_confirm)
	private Button btn_confirm;
	
	private ZoomCropView pv_crop_picture;
	
	private CropPictureActivity instance;
	
	private Intent intent;

	private int widthOfScreen;

	private int heightOfScreen;

	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		pv_crop_picture=cil_clipImageLayout.getZoomCropView();
		intent = getIntent();
		String imagepath = intent.getStringExtra("IMAGEPATH");
		widthOfScreen=getWindowManager().getDefaultDisplay().getWidth();
        heightOfScreen=getWindowManager().getDefaultDisplay().getHeight();
		setPicture(imagepath);
		setListeners();
	}

	private void setListeners() {
		ll_backtoalbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		btn_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Bitmap clipBitmap = pv_crop_picture.clip();
				Intent intent = new Intent(CropPictureActivity.this,
						ChangePortraitActivity.class);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				clipBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] datas = baos.toByteArray();
				intent.putExtra("CLIPBITMAP", datas);
				startActivity(intent);
			}
		});
	}

	private void setPicture(String imagepath) {
		try {
			if (imagepath != null) {
				bitmap = decodeSampledBitmapFromFile(imagepath, widthOfScreen, heightOfScreen);
				
				if(readPictureDegree(imagepath)!=0){
	        		bitmap=rotaingImageBitmap(readPictureDegree(imagepath), bitmap);
	        	}
				initViews(bitmap);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initViews(Bitmap bm) {
		pv_crop_picture.setBackgroundColor(0xff000000);
		pv_crop_picture.setImageBitmap(bm);
		pv_crop_picture.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	/**
     * 根据传入的宽和高，计算出合适的inSampleSize值
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {  
        // 源图片的高度和宽度  
        final int height = options.outHeight;  
        final int width = options.outWidth;  
        int inSampleSize = 1;  
        if (height > reqHeight || width > reqWidth) {  
            // 计算出实际宽高和目标宽高的比率  
            final float heightRatio = (float) height / (float) reqHeight;  
            final float widthRatio = (float) width / (float) reqWidth;  
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高  
            // 一定都会大于等于目标的宽和高。  
            inSampleSize = (int) Math.ceil(heightRatio < widthRatio ? heightRatio : widthRatio);  
        }  
        return inSampleSize;  
    }
    
    /**
     * 从文件中加载图片并压缩成指定大小
     * 先通过BitmapFactory.decodeStream方法，创建出一个bitmap，
     * 再调用上述方法将其设为ImageView的 source。decodeStream最大的秘密在
     * 于其直接调用JNI>>nativeDecodeAsset()来完成decode，无需再使用java层的createBitmap，
     * 从而节省了java层的空间
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws FileNotFoundException 
     */
    public static Bitmap decodeSampledBitmapFromFile(String pathName,  
            int reqWidth, int reqHeight){  
		// 加载位图
    	BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(
					new File(pathName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		// 为位图设置100K的缓存
		options.inTempStorage = new byte[100 * 1024];
		// 设置位图颜色显示优化方式
		// ALPHA_8：每个像素占用1byte内存（8位）
		// ARGB_4444:每个像素占用2byte内存（16位）
		// ARGB_8888:每个像素占用4byte内存（32位）
		// RGB_565:每个像素占用2byte内存（16位）
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		// 设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
		options.inPurgeable = true;
		// 设置解码位图的尺寸信息
		options.inInputShareable = true;
		return BitmapFactory.decodeStream(bis, null, options);
	}
    
    /**
  	 * 返回按比例缩减后的bitmap
  	 * @param bmp
  	 * @param width
  	 * @param height
  	 * @return
  	 */
  	public static Bitmap picZoom(Bitmap bmp, int width, int height) {
		int bmpWidth = bmp.getWidth();
		int bmpHeght = bmp.getHeight();
		//等比例自动缩放图片适应控件
		float f1=(float)bmpWidth/width;
		float f2=(float)bmpHeght/height;
		float scale=1f;
		if(f1>1||f2>1){
			//放大
			scale=f1<f2?f2:f1;
		}else if(f1<1||f2<1){
			//缩小
			scale=f1<f2?f1:f2;
		}
  		Matrix matrix = new Matrix();
  		matrix.postScale(scale,scale);
  		return Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, true);
  	}
  	
  	/**
     * 旋转图片
     * @param angle
     * @param mBitmap
     * @return
     */
	public static Bitmap rotaingImageBitmap(int angle, Bitmap mBitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		Bitmap b = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
		if(mBitmap!=null&&!mBitmap.isRecycled()){
			mBitmap.recycle();
			mBitmap=null;
		}
		return b;
	}
	
	 /**
     * 读取图片旋转角度
     * 三星手机会旋转图片
     * @param imagePath
     * @return
     */
	public static int readPictureDegree(String imagePath) {
		int imageDegree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(imagePath);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				imageDegree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				imageDegree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				imageDegree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageDegree;
	}
  	
  	@Override
	protected void onDestroy() {
		super.onDestroy();
		//及时回收
		if(bitmap!=null&&bitmap.isRecycled()){
			bitmap.recycle();
			bitmap=null;
		}
		System.gc();
	}
}
