package com.szrjk.util.gallery;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.MediaColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.self.more.album.PhotoUpAlbumHelper;
import com.szrjk.self.more.album.PhotoUpImageItem;
import com.szrjk.util.gallery.ListImageDirPopupWindow.OnImageDirSelected;
/**
 * 
 * @author ldr
 *
 * 在使用gridview的时候，item布局里面用的父布局，用一个相对的，自定义的，会把宽度和高度一样，这样效果：gv里面的图片都是正方形的
 */
@ContentView(R.layout.activity_image_gallery)
public class MainGalleryActivity extends BaseActivity implements OnImageDirSelected,OnClickListener
{

	/**
	 * 存储文件夹中的图片数量
	 */
	private int mPicsSize;
	/**
	 * 图片数量最多的文件夹
	 */
	private File mImgDir;
	/**
	 * 所有的图片
	 */
	private List<String> mImgs;

	private GridView mGirdView;
	private MyAdapter mAdapter;
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashSet<String> mDirPaths = new HashSet<String>();

	/**
	 * 扫描拿到所有的图片文件夹
	 */
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

	private RelativeLayout mBottomLy;

	private TextView mChooseDir;
	private TextView mImageCount;
	int totalCount = 0;

	private int mScreenHeight;
	private TextView tv_number;//显示能选择的总是与已选择的数量

	private ListImageDirPopupWindow mListImageDirPopupWindow;

	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(android.os.Message msg)
		{

			progressDialog.dismiss();
			// 为View绑定数据
			data2View();
			// 初始化展示文件夹的popupWindw
			initListDirPopupWindw();
		}
	};


	/**
	 * 为View绑定数据
	 */
	private void data2View()
	{
		if (mImgDir == null)
		{
			Toast.makeText(getApplicationContext(), "一张图片没扫描到",
					Toast.LENGTH_SHORT).show();
			return;
		}
		mImgs = Arrays.asList(mImgDir.list());
		//把排序翻转，按时间最新的排序
		//		Collections.reverse(mImgs);
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new MyAdapter(getApplicationContext(), mImgs,
				R.layout.image_grid_item, mImgDir.getAbsolutePath(),tv_number,num,mHandler);
		mGirdView.setAdapter(mAdapter);
		mImageCount.setText(totalCount + "张");
	};

	/**
	 * 初始化展示文件夹的popupWindw
	 */
	private void initListDirPopupWindw()
	{
		mListImageDirPopupWindow = new ListImageDirPopupWindow(
				LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
				mImageFloders, LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.list_dir, null));

		mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener()
		{

			@Override
			public void onDismiss()
			{
				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		// 设置选择文件夹的回调
		mListImageDirPopupWindow.setOnImageDirSelected(this);
	}
	private int num;

	private TextView tv_comp;

	private ImageButton ib_back;

	private Dialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ViewUtils.inject(this);

		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;
		//固定一下图片个数9
		//		num = 9;
		num = this.getIntent().getIntExtra("num", 0);
		progressDialog = createDialog(this, "正在加载...");
		initView();
		getImages();

		getViewHeight();
	}
	
	

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
	 */
	private void getImages()
	{
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		// 显示进度条
		//		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
		progressDialog.show();
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{

				String firstImage = null;

				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = MainGalleryActivity.this
						.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaColumns.MIME_TYPE + "=? or "
								+ MediaColumns.MIME_TYPE + "=?",
								new String[] { "image/jpeg", "image/png" },
								MediaColumns.DATE_MODIFIED);

				int photoPathIndex = mCursor.getColumnIndexOrThrow(Media.DATA);

				Log.i("TAG", mCursor.getCount() + "");
				while (mCursor.moveToNext())
				{
					// 获取图片的路径
					//					String path = mCursor.getString(mCursor	.getColumnIndex(MediaColumns.DATA));

					if (mCursor.getString(photoPathIndex)
							.substring(
									mCursor.getString(photoPathIndex).lastIndexOf("/") + 1,
									mCursor.getString(photoPathIndex).lastIndexOf("."))
									.replaceAll(" ", "").length() <= 0) {
						Log.i(TAG, "出现了异常图片的地址："+ mCursor.getString(photoPathIndex));

					}else{
						// 获取图片的路径
						String path = mCursor.getString(photoPathIndex);

						// 拿到第一张图片的路径
						if (firstImage == null)
							firstImage = path;
						// 获取该图片的父路径名
						File parentFile = new File(path).getParentFile();
						if (parentFile == null)
							continue;
						String dirPath = parentFile.getAbsolutePath();
						ImageFloder imageFloder = null;
						// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
						if (mDirPaths.contains(dirPath))
						{
							continue;
						} else
						{
							mDirPaths.add(dirPath);
							// 初始化imageFloder
							imageFloder = new ImageFloder();
							imageFloder.setDir(dirPath);
							imageFloder.setFirstImagePath(path);
						}
						//这里跟诡异，部分图片会有问题
						if(parentFile.list()==null)continue;
						int picSize = parentFile.list(new FilenameFilter()
						{
							@Override
							public boolean accept(File dir, String filename)
							{
								if (filename.endsWith(".jpg")
										|| filename.endsWith(".png")
										|| filename.endsWith(".jpeg"))
									return true;
								return false;
							}
						}).length;
						totalCount += picSize;

						imageFloder.setCount(picSize);
						mImageFloders.add(imageFloder);

						if (picSize > mPicsSize)
						{
							mPicsSize = picSize;
							mImgDir = parentFile;
						}
					}
				}
				mCursor.close();

				// 扫描完成，辅助的HashSet也就可以释放内存了
				mDirPaths = null;

				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(0x110);

			}
		}).start();

	}

	/**
	 * 初始化View
	 */
	private void initView()
	{
		mGirdView = (GridView) findViewById(R.id.id_gridView);
		mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
		mImageCount = (TextView) findViewById(R.id.id_total_count);

		mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);

		tv_number = (TextView)findViewById(R.id.tv_number);
		tv_number.setText("0/" + num);
		ib_back = (ImageButton)findViewById(R.id.ib_back);
		tv_comp = (TextView)findViewById(R.id.tv_comp);
		tv_comp.setOnClickListener(this);
		ib_back.setOnClickListener(this);

		mGirdView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					mBottomLy.setVisibility(View.GONE);
					break;

				case MotionEvent.ACTION_UP:
					mBottomLy.setVisibility(View.VISIBLE);
					break;
				}
				return false;
			}
		});
	}

	private void initEvent()
	{
		/**
		 * 为底部的布局设置点击事件，弹出popupWindow
		 */
		mBottomLy.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mListImageDirPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
				//由于showAsDropDown，sdk 17以上可以正常使用，16未测试，但是低的有异常
				//				mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);
				//				Log.i("px", ""+px);						基于点击的控件     位置                                     x偏移 y偏移，0底部，++往上
				mListImageDirPopupWindow.showAtLocation(mBottomLy, Gravity.BOTTOM, 0, px);

				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = .3f;
				getWindow().setAttributes(lp);
			}
		});
	}

	@Override
	public void selected(ImageFloder floder)
	{

		mImgDir = new File(floder.getDir());
		mImgs = Arrays.asList(mImgDir.list(new FilenameFilter()

		{
			@Override
			public boolean accept(File dir, String filename)
			{
				if (filename.endsWith(".jpg") || filename.endsWith(".png")
						|| filename.endsWith(".jpeg"))
					return true;
				return false;
			}
		}));
		String[] t = mImgDir.list(new FilenameFilter()

		{
			@Override
			public boolean accept(File dir, String filename)
			{
				if (filename.endsWith(".jpg") || filename.endsWith(".png")
						|| filename.endsWith(".jpeg"))
					return true;
				return false;
			}
		});
		//		System.out.println(Arrays.toString(t));
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new MyAdapter(getApplicationContext(), mImgs,
				R.layout.image_grid_item, mImgDir.getAbsolutePath(),tv_number,num,mHandler);
		mGirdView.setAdapter(mAdapter);
		// mAdapter.notifyDataSetChanged();
		mImageCount.setText(floder.getCount() + "张");
		mChooseDir.setText(floder.getName());
		mListImageDirPopupWindow.dismiss();

	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.tv_comp:
				ArrayList<String> url = (ArrayList<String>) MyAdapter.mSelectedImage;
				Log.i("url", url.toString());
				if (url.size() == 0) {
					Toast.makeText(this, "没有选择图片", Toast.LENGTH_SHORT).show();
				} else {

					Intent intent = new Intent();
					String[] arr = new String[url.size()];
					for (int i = 0; i < url.size(); i++) {
						arr[i] = url.get(i);
					}
					intent.putExtra("arr", arr);
					setResult(RESULT_OK, intent);
					//清空图片
					MyAdapter.mSelectedImage.clear();
					finish();
				}
				break;

			case R.id.ib_back:
				MyAdapter.mSelectedImage.clear();
				finish();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private int px;//获得底部控件的高度
	public void getViewHeight(){
		ViewTreeObserver vtos = mBottomLy.getViewTreeObserver();    
		vtos.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
			@Override    
			public void onGlobalLayout() {  
				mBottomLy.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
				px = mBottomLy.getHeight();
				Log.i("h", px+"");
				initEvent();//pop的点击事件，获得高度之后设置 
			}    
		});
	}

}
