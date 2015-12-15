package com.szrjk.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.IPopupItemCallback;
import com.szrjk.entity.ISelectImgCallback;
import com.szrjk.entity.PhotoBucket;
import com.szrjk.entity.PhotoType;
import com.szrjk.entity.PopupItem;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.gallery.MainGalleryActivity;
import com.szrjk.widget.ListPopup;

/**
 * 选择多张
 * denggm on 2015/10/28.
 * DHome
 */
public class MultipleUploadPhotoUtils {

	// 拍照
	private static final int CAMERA_WITH_DATA = 3022;
	// 相册选择
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	private static final int DATA_CHARGE_NOTIFY = 1000;


	/**存储返回的imglist**/
	private List<ImageItem> imgItems = new ArrayList<ImageItem>();
	private String[] urlArr;

	private BaseActivity context;

	private ISelectImgCallback iSelectImgCallback;

	private int maxNum;

	private String[] arr;//图库返回的图片地址数组

	/**
	 *
	 * @param context
	 * @param lly_post  最外层的id
	 */
	public MultipleUploadPhotoUtils(BaseActivity context,LinearLayout lly_post,int maxNum,ISelectImgCallback iSelectImgCallback) {
		this.context = context;
		this.iSelectImgCallback = iSelectImgCallback;

		if(maxNum>3){
			maxNum=3;
		}
		this.maxNum = maxNum;
//		//弹框
		showPoP(lly_post);
	}



	/**
	 * 拍照
	 */
	private Uri mOutPutFileUri;
	private File file;
	
	private void doTakePicture() throws IOException {
		//        Intent intent = new Intent();
		////        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		//        Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
		//        context.startActivityForResult(getImageByCamera, CAMERA_WITH_DATA);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//文件夹aaaa
		String path = Environment.getExternalStorageDirectory().toString() + "/" +"tempimage";
		File path1 = new File(path);
		if(!path1.exists()){
			path1.mkdirs();
		}
		file = new File(path1,System.currentTimeMillis()+".jpg");
		file.createNewFile();//这里确保file生成。酷派会出现file不存在的情况
		mOutPutFileUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
		context.startActivityForResult(intent, CAMERA_WITH_DATA);
	}



	/**
	 * 相册选择多张
	 */
	private void doTakePhoto() {
		Intent intent = new Intent();
//		intent.setClass(context, AlbumActivity.class);
		intent.setClass(context, MainGalleryActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putInt(Constant.IMGNUM, maxNum);
		intent.putExtra("num", maxNum);
		//		intent.putExtras(bundle);
		context.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
	}


	public void operResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case CAMERA_WITH_DATA://拍照
			if (imgItems.size() < 9) {
				//                    Bitmap bitmapCaputre = (Bitmap) data.getExtras().get("data");
				//                    if (bitmapCaputre == null) {
				//                        return;
				//                    }
				//                    if (null != bitmapCaputre) {
				//                        String fileName = String.valueOf(System.currentTimeMillis());
				//                        FileUtils.saveBitmap(bitmapCaputre, fileName);
				//处理mOutPutFileUri中的完整图像
				ImageItem takePhoto = new ImageItem();
//				Bitmap bm = BitmapFactory.decodeFile(getImageAbsolutePath(context, mOutPutFileUri));
				
//				Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
				//根据路径获得bm、然后循环压缩《00kb
				Bitmap bm = BitmapCompressImage.getimage(file.getAbsolutePath());
				takePhoto.setBitmap(bm);
				takePhoto.setAbsPaht(file.getAbsolutePath());
				imgItems.add(takePhoto);
			} 
			break;
		case PHOTO_PICKED_WITH_DATA://相册
			//循环里面，获得bm的大小。然后大于500的压缩到少于100k，然后放回去item。add入list
//			Bundle bundle =  data.getExtras();
//			Parcelable[] imgitemArr = bundle.getParcelableArray(Constant.IMGLIST);
			arr = data.getStringArrayExtra("arr");
			for (int i=0;i<arr.length;i++){
//				ImageItem item = (ImageItem) imgitemArr[i];
				ImageItem item = new ImageItem();
				item.setAbsPaht(arr[i]);
//				Bitmap tempbp = item.getBitmap();
//				if(tempbp==null)continue;//把null的给过滤掉
				Bitmap tempbp = BitmapFactory.decodeFile(arr[i]);
				int tempsize = BitmapCompressImage.getBitmapSize(tempbp)/ 1024;
				
//				Log.i("size", getBitmapSize(tempbp)/ 1024 +"");
				if (tempsize > 500) {
//					Bitmap bm = comp(tempbp);
					Bitmap bm = BitmapCompressImage.comp(tempbp);
					item.setBitmap(bm);
					System.out.println(tempsize);
					System.out.println("大于500kb压缩图片了");
				}
				
				imgItems.add(item);
			}
//			Log.i("普通帖子图片个数", "imgItems.size:" + imgItems.size());
			break;
		}
		//上传
		Message msg = new Message();
		msg.what = DATA_CHARGE_NOTIFY;
		PhotoHandler handler = new PhotoHandler();
		handler.sendMessage(msg);
		//回调
	}
	/**
	 * 
	 * @author dlr
	 *2015-12-12 10:27:10
	 *循环上传 的是，从ImageItem取getBitmap（）时候，会出现Bitmap空bitmap = Bimp.revitionImageSize(imagePath);
	 *出现读取异常情况。现在取代读取方式。
	 */
	class PhotoHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DATA_CHARGE_NOTIFY:
				//上传所有照片
				urlArr = new String[imgItems.size()];
				Log.i("imgItems.size", ""+imgItems.size());
				for (int i=0;i<imgItems.size();i++){
					ImageItem postItems = imgItems.get(i);
					Bitmap bbb = postItems.getBitmap();
					String url =  updateFile(bbb);
					urlArr[i] = url;
				}
				iSelectImgCallback.selectImgCallback(imgItems,urlArr);
				break;
			}
		}
	}




	// 上传图片
	private String updateFile(Bitmap bitmap) {
		ByteArrayOutputStream baos = BitMapUtil.comp(bitmap);
		byte[] imgData = baos.toByteArray();

		String url = context.uploadPhoto(imgData, PhotoType.Feed, new SaveCallback() {
			@Override
			public void onProgress(String arg0, int pro, int total) {
			}
			@Override
			public void onFailure(String imageUrl, OSSException ossException) {
				//                handler.removeCallbacks(runnable);
				//                sb.append(OssUpdateImgUtil.feedPicFilterUrl + imageUrl + "|");
				// 处理图片
				new DealPhoto(imageUrl).start();
			}

			@Override
			public void onSuccess(String imageUrl) {
				//                handler.removeCallbacks(runnable);
				//                sb.append(OssUpdateImgUtil.feedPicFilterUrl + imageUrl + "|");
				//                Log.i("新的sb：", sb.toString());
				//				Log.i("gridAdapter.returnImageInfo().size()", gridAdapter.returnImageInfo().size()+"");
				// 处理图片
				new DealPhoto(imageUrl).start();
			}
		});
		return  url;
		//		pb_loading.setVisibility(View.GONE);
	}

	class DealPhoto extends Thread {
		private String pathName;

		DealPhoto(String pathName) {
			this.pathName = pathName;
		}

		@Override
		public void run() {
			dealPhoto(pathName);
		}
	}
	//
	// 处理图片
	private void dealPhoto(String pathName) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "dealPic");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		List<PhotoBucket> buckets = new ArrayList<PhotoBucket>();
		PhotoBucket bucket = new PhotoBucket();
		bucket.setBucket(Constant.PHOTO_BUCKET_FEED);
		bucket.setKey(pathName);
		bucket.setSize(Constant.FEED_DEAL_SIZE);
		buckets.add(bucket);
		busiParams.put("pics", buckets);
		paramMap.put("BusiParams", busiParams);
		context.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void start() {			}
			@Override
			public void loading(long total, long current, boolean isUploading) {			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {	
				Log.i("dealPhoto", jobj.toString());	}
			@Override
			public void success(JSONObject jsonObject) {
				Log.i("dealPhoto", jsonObject.toString());
			}
		});
	}
	/**
	 * 这里的代码备用。可以从uri中获得图片地址
	 */
	/**
	 * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
	 * @param context
	 * @param imageUri
	 * @author yaoxing
	 * @date 2014-10-12
	 */
	@TargetApi(19)
	public  String getImageAbsolutePath(Activity context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
	
	
	/**显示sendWindow**/
	private void showPoP(View v){
//		sendWindow = new PostSendPopup(instance, sendPostClick);

		List<PopupItem> pilist = new ArrayList<PopupItem>();
		PopupItem pi1 = new PopupItem();
		pi1.setItemname("拍照");//设置名称
		pi1.setColor(context.getResources().getColor(R.color.search_bg));
		pi1.setiPopupItemCallback(new IPopupItemCallback() {  //设置click动作
			@Override
			public void itemClickFunc(PopupWindow sendWindow) {
				sendWindow.dismiss();
				try {
					doTakePicture();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		PopupItem pi2 = new PopupItem();
		pi2.setItemname("从手机相册选择");
		pi2.setColor(context.getResources().getColor(R.color.search_bg));
		pi2.setiPopupItemCallback(new IPopupItemCallback() {
			@Override
			public void itemClickFunc(PopupWindow sendWindow) {
				sendWindow.dismiss();
				doTakePhoto();
			}
		});
		pilist.add(pi1);
		pilist.add(pi2);
		new ListPopup(context,pilist,v);
	}
}
