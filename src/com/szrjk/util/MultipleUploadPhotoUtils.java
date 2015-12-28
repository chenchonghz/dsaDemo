package com.szrjk.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.IPopupItemCallback;
import com.szrjk.entity.ISelectImgCallback;
import com.szrjk.entity.PhotoType;
import com.szrjk.entity.PopupItem;
import com.szrjk.self.more.album.AlbumGalleryActivity;
import com.szrjk.service.DemoService;
import com.szrjk.widget.ListPopup;

/**
 * 选择多张
 * denggm on 2015/10/28.
 * 
 * 最后修改时间 2015-12-28 16:21:07
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
//		intent.setClass(context, MainGalleryActivity.class);
		intent.setClass(context, AlbumGalleryActivity.class);
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
			if (data == null) {
				ToastUtils.showMessage(context, "异常");
				return;
			}
			arr = data.getStringArrayExtra("arr");
			for (int i=0;i<arr.length;i++){
//				ImageItem item = (ImageItem) imgitemArr[i];
				ImageItem item = new ImageItem();
				item.setAbsPaht(arr[i]);
//				Bitmap tempbp = item.getBitmap();
//				if(tempbp==null)continue;//把null的给过滤掉
//				Bitmap tempbp = BitmapFactory.decodeFile(arr[i]);
//				int tempsize = BitmapCompressImage.getBitmapSize(tempbp)/ 1024;
//				
////				Log.i("size", getBitmapSize(tempbp)/ 1024 +"");
//				if (tempsize > 500) {
////					Bitmap bm = comp(tempbp);
//					Bitmap bm = BitmapCompressImage.comp(tempbp);
//					item.setBitmap(bm);
//					System.out.println(tempsize);
//					System.out.println("大于500kb压缩图片了");
//				}
				
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
				
				//先生成图片
				for (int i = 0; i < urlArr.length; i++) {
					String userSeqId = Constant.userInfo!=null?Constant.userInfo.getUserSeqId():"0";
					urlArr[i] = ImageAsynTaskUpload.createPathName(userSeqId, PhotoType.Feed);
				}
				Log.i("拼接后的图片---", Arrays.toString(urlArr));
				//先回调，更新UI
				iSelectImgCallback.selectImgCallback(imgItems,urlArr);
				
				
				for (int i=0;i<imgItems.size();i++){
					ImageItem postItems = imgItems.get(i);
					new CompAsynTask().execute(postItems.getAbsPaht(),urlArr[i]);
//					Bitmap bbb = postItems.getBitmap();
//					String url =  
//					urlArr[i] = url;
				}
				//目前没做失败处理 
				//iSelectImgCallback.selectImgCallback(imgItems,urlArr);
				break;
			}
		}
	}
	
	/**
	 * @author l
	 * 这个异步任务；主要是压缩图片，压缩完成之后onPostExecute方法，把压缩之后的byte流
	 * 和对应的阿里云地址传给服务。服务创建队列，一个一个上传图片
	 */
	class CompAsynTask extends AsyncTask<String, Void, Bitmap> {
		private String up ;//上传图片用的地址
		@Override
		protected Bitmap doInBackground(String... params) {
			String p = params[0];//图片绝对地址
			this.up = params[1]; //服务器图片的保存地址
			Bitmap tempbp = BitmapFactory.decodeFile(p);
			//int tempsize = BitmapCompressImage.getBitmapSize(tempbp)/ 1024;
			//		Log.i("size", getBitmapSize(tempbp)/ 1024 +"");
			//			Bitmap bm = comp(tempbp);
			Bitmap bm = BitmapCompressImage.comp(tempbp);
			return bm;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			//updateFile(result, up);
			//压缩完之后，上传图片
			Log.i("正在上传图片：", up);
			Intent intent = new Intent(context,DemoService.class);
			byte[] bo = BitMapUtil.comp(result).toByteArray();
			
			intent.putExtra("postbp", bo);
			intent.putExtra("path", up);
			context.startService(intent);
		}
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
