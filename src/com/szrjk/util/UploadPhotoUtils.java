package com.szrjk.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.IImgUrlCallback;
import com.szrjk.entity.PhotoType;
import com.szrjk.http.InterfaceComm;
import com.szrjk.util.clip.ClipActivity;
import com.szrjk.util.gallery.MainGalleryActivity;
import com.szrjk.widget.AddPhotoPopup;

/**
 * denggm on 2015/10/28.
 * DHome
 */
public class UploadPhotoUtils {

	public static final int CAMERA_WITH_DATA = 3022;//拍照
	public static final int PHOTO_PICKED_WITH_DATA = 3023;//照片中选取
	public static final int REQUESTCODE_CUTTING = 3024;//剪裁照片
	public static final int IMAGE_CROP = 3025;//剪裁照片


	protected BaseActivity context;
	protected AddPhotoPopup menuWindow;

	protected  IImgUrlCallback imgUrlCallback;
	private boolean isClip;
	private Dialog alertdialog;

	public UploadPhotoUtils(BaseActivity pactivity,boolean isClip) {
		this.context = pactivity;
		this.isClip=isClip;
	}

	//上传图片
	private void updateFile(Bitmap bitmap) {
		ImageUploadUtil imageUploadUtil = new ImageUploadUtil();
		try {
			alertdialog = context.createDialog(context, "上传图片中...");
			ByteArrayOutputStream baos = BitMapUtil.comp(bitmap);
			byte[] imgData = baos.toByteArray();
//			String url =context.uploadPhoto(imgData, PhotoType.Face, new SaveCallback() {
			imageUploadUtil.uploadPhoto(context,imgData, PhotoType.Face, new SaveCallback() {
				@Override
				public void onSuccess(String s) {
					String imgUrl = OssUpdateImgUtil.facePicFilterUrl + s;
					//                Log.e(TAG, imgUrl);
					//上传完url之后处理   返回给ac，registerInfo实体保存头像地址

					imgUrlCallback.operImgUrl(imgUrl);
					context.runOnUiThread(new Runnable() {
						
						@SuppressWarnings("static-access")
						@Override
						public void run() {
							alertdialog.dismiss();
							BaseActivity.showToast(context, "上传成功", 0);
							
						}
					});
					
//					Log.i("Thread", ""+Thread.currentThread().getName());
					//好像是通知阿里云，处理图片
					InterfaceComm.dealPhoto(s, PhotoType.Face);
				}

				@Override
				public void onProgress(String s, int i, int i1) {
				}

				@Override
				public void onFailure(String s, OSSException e) {
					Log.i("updateFile--onFailure", e.toString());
					context.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							alertdialog.dismiss();
							ToastUtils.showMessage(context, "图片上传失败,请删除后再上传");
						}
					});
				}
			});
		} catch (Exception e) {
			Log.i("updateFile", "出现异常----");
			e.printStackTrace();
		}finally{
			//回收
			imageUploadUtil = null;
			ClipActivity.cbitmap = null;
			file.delete();
			Log.i("", "回收资源");
		}
	}

	/**
	 * 在拍照后，处理图片
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void imgOper(int requestCode,int resultCode,Intent data){

		try {
			
			switch(requestCode)
			{
			case UploadPhotoUtils.CAMERA_WITH_DATA:// 调用相机拍照
				//startPhotoZoom(data.getData());

				//跳转截图
				if (isClip) {
//					Intent intent = new Intent(context,CropImageActivity.class);
					Intent intent = new Intent(context,ClipActivity.class);
					if (file == null) {
						Log.i("file", "file 临时保存图片对象空"); 
						Toast.makeText(context, "储存失败，请再试试", 0).show();
						return;
					}else{
						intent.putExtra("camear", file.getAbsolutePath());
						context.startActivityForResult(intent, IMAGE_CROP);
					}
				}else{
					//返回的时候缩略图
					//酷派手机，拍照返回时候，会出现data空
					//				if (data == null) {
					//					Log.i("Intent:data", "Intent:data  无返回");
					//					return;
					//				}
					//				intent.putExtra("camear", file.getAbsolutePath());

					//Bitmap bitmapCaputre = BitmapFactory.decodeFile(file.getAbsolutePath());
					//由于启动拍照的时候，已经指定了一个file保存拍照后的图片；这里就不用返回的data（是缩略图）、file是原图（保持清晰）
					
					Bitmap bitmapCaputre = BitmapCompressImage.getimage(file.getAbsolutePath());
					//Bitmap bitmapCaputre=(Bitmap)data.getExtras().get("data");
					imgUrlCallback.operImgPic(bitmapCaputre);
					updateFile(bitmapCaputre);
				}

				break;
			case UploadPhotoUtils.PHOTO_PICKED_WITH_DATA:// 直接从相册获取
				//			try {
				//                    startPhotoZoom(data.getData());
				//                } catch (NullPointerException e) {
				//                    e.printStackTrace();// 用户点击取消操作
				//                }
				if (data == null) {
					Log.i("PHOTO_PICKED_WITH_DATA", "直接从相册获取返回的数据空了");
					return ;
				}
//				Uri selectedImage=data.getData();
//				String[]filePathColumn={MediaStore.Images.Media.DATA};
//				Cursor cursor=context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//				cursor.moveToFirst();
//				int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
//				//获得图片的地址 
//				String picturePath=cursor.getString(columnIndex);
//				cursor.close();
				String[] arr = data.getStringArrayExtra("arr");
				String picturePath = arr[0];
				//Log.i("picturePath", picturePath);
				//这里会出现有路径，但是无法读取的问题。为了之后的操作，先读取一次
				try {
					Bitmap tbp = BitmapCompressImage.getimage(picturePath);
					if (tbp == null) {
						Log.i("picturePath", "picturePath  读取异常");
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				//跳转截图
				if (isClip) {
//					Intent intentPhoto = new Intent(context,CropImageActivity.class);
					Intent intentPhoto = new Intent(context,ClipActivity.class);
					intentPhoto.putExtra("camear", picturePath);
					context.startActivityForResult(intentPhoto, IMAGE_CROP);
				}else{
//					Bitmap b= BitmapFactory.decodeFile(picturePath);
					Bitmap b= BitmapCompressImage.getimage(picturePath);
					//设置返回的图片显示出来
					imgUrlCallback.operImgPic(b);
					updateFile(b);
				}

				break;
			case IMAGE_CROP:// 取得裁剪后的图片
				//            	Bundle c = data.getExtras();
				//            	Bitmap bit = (Bitmap)c.get("cropbitmap");
				//图像过大时候，不能用bundle传输
//				Bitmap bit = CropImageActivity.bp;
				Bitmap bit = ClipActivity.cbitmap;
				if (bit == null) {
					System.err.println("Bitmap is null ----------------");
					ToastUtils.showMessage(context, "操作有误，裁剪失败");
					return;
				}
				//设置返回的图片显示出来
				ToastUtils.showMessage(context, "正在上传头像...");
				imgUrlCallback.operImgPic(bit);
				updateFile(bit);
				ClipActivity.cbitmap = null;//清除静态数据
				break;

			}
		} catch (Exception e) {
			Log.i("onActivityResult", "数据有异常");
			e.printStackTrace();
		}
	}


	/**
	 * 弹框,
	 * @param img_reg1faceimg
	 */
	public void popubphoto(ImageView img_reg1faceimg,IImgUrlCallback imgUrlCallback) {
		try {
			// 原界面显示窗口
			menuWindow = new AddPhotoPopup(context, casePhotoClick);
			menuWindow.showAtLocation(img_reg1faceimg, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			this.imgUrlCallback = imgUrlCallback;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	private View.OnClickListener casePhotoClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				menuWindow.dismiss();
				switch (v.getId()) {
				case R.id.tv_picture:
					//拍照
					doTakePicture();
					break;
				case R.id.tv_photo:
					//图库
					doTakePhoto();
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};


	/**
	 * 选择照片
	 */
	private void doTakePhoto() {
		try {
//			Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//			context.startActivityForResult(i, PHOTO_PICKED_WITH_DATA);
			Intent intent = new Intent(context,MainGalleryActivity.class);
			intent.putExtra("num", 1);
			context.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (Exception e) {
			Log.i("", "进入图库");
			e.printStackTrace();
		}
	}

	/**
	 * 拍照
	 */
	private void doTakePicture() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			//            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			//            String caseFileName = timeStampFormat.format(new Date());
			//            String filePath = Environment.getExternalStorageDirectory() + File.separator + caseFileName + ".jpg";
			// 跳转到拍照界面
			//            jumpCaptureActivity(filePath);
			Log.i("手机厂商", ""+android.os.Build.MANUFACTURER);
//			12-11 10:14:28.509: I/手机厂商(26319): YuLong

			Log.i("手机型号", ""+android.os.Build.MODEL);
//			12-11 10:14:28.509: I/手机型号(26319): Coolpad 8297-T01

			jumpCamera();
		} else {
			ToastUtils.showMessage(context, "请插入SD卡");
		}
	}

	/**
	 * 拍照
	 */
	private File file;//保存临时拍照文件的file
	private void jumpCamera() {
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//文件夹tempimage
			String path = Environment.getExternalStorageDirectory().toString()+ "/" + "tempimage".trim();
//			File tempcache = context.getExternalCacheDir();
//			String path = tempcache.getAbsolutePath();
			File path1 = new File(path);
			//如果不存在目录就创建
			if (!path1.exists()) {
				path1.mkdirs();
			}
		
			file = new File(path1, System.currentTimeMillis() + ".jpg");
			file.createNewFile();//酷派手机大神f1，有时候，file无法生成。原因未知；但是加入createNewFile方法即可
			if (!file.exists()) {
				Log.i("", "保存图片的临时文件生成失败");
				//由于保存图片的文件无法生成，调用拍照之后，拍照的图片无法保存。抛错；
				ToastUtils.showMessage(context, "生成图片失败", 0);
				return;
			}
			Uri mOutPutFileUri = Uri.fromFile(file);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
			context.startActivityForResult(intent,UploadPhotoUtils.CAMERA_WITH_DATA);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
