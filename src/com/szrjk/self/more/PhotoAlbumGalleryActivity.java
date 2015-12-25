package com.szrjk.self.more;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.IndexGalleryActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.IPhotoClickOper;
import com.szrjk.entity.OperContextClick;
import com.szrjk.entity.PhotoAlbum2;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.DDateUtils;
import com.szrjk.widget.HeaderView;
import com.szrjk.widget.IndexGridView;

@ContentView(R.layout.activity_photo_album_gallery)
public class PhotoAlbumGalleryActivity extends BaseActivity {

	protected static final int GET_PHOTO_ALBUM_LIST_SUCCESS = 0;

	@ViewInject(R.id.hv_month)
	private HeaderView hv_month;

	@ViewInject(R.id.igv_photo_album_gallery)
	private IndexGridView igv_photo_album_gallery;

	private PhotoAlbumGalleryActivity instance;

	private UserInfo userInfo;

	private ArrayList<PhotoAlbum2> photoAlbumList;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_PHOTO_ALBUM_LIST_SUCCESS:
				photoAlbumList = (ArrayList<PhotoAlbum2>) msg.obj;
				fillPostIdList();
				setAdapter();
				break;
			}
		}
	};

	private String[] picsList;
	private String[] postIdList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		initLayout();
		// setAdapter();
	}

	private void initLayout() {
		userInfo = Constant.userInfo;
		Intent intent = getIntent();
		String month = intent.getStringExtra("MONTH");
		getPhotoAlbumListByMonth(month);
		Date date = new Date();
		if (String.valueOf(date.getMonth() + 1).equals(month.subSequence(4, 6))) {
			hv_month.setHtext("本月");
		} else {
			try {
				month = DDateUtils.dformatOldstrToNewstr(month, "yyyyMM",
						"yyyy年MM月");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			hv_month.setHtext(month);
		}
	}

	private void fillPostIdList() {
		if (photoAlbumList != null && !photoAlbumList.isEmpty()) {
			postIdList = new String[photoAlbumList.size()];
		}
		for (int i = 0; i < photoAlbumList.size(); i++) {
			PhotoAlbum2 photoAlbum2 = photoAlbumList.get(i);
			String postId = photoAlbum2.getPostId();
			if (postId != null && !postId.isEmpty()) {
				postIdList[i] = postId;
			}
		}
	};

	private void setAdapter() {
		if (photoAlbumList != null && !photoAlbumList.isEmpty()) {
			picsList = new String[photoAlbumList.size()];
		}
		for (int i = 0; i < photoAlbumList.size(); i++) {
			PhotoAlbum2 photoAlbum2 = photoAlbumList.get(i);
			String picUrl = photoAlbum2.getPicUrl();
			if (picUrl != null && !picUrl.isEmpty()) {
				picsList[i] = picUrl;
			}
		}
		// picsList = new String[] {
		// "http://dd-face.digi123.cn/201511/80426ba27eef6380.jpg",
		// "http://dd-face.digi123.cn/201511/80426ba27eef6380.jpg",
		// "http://dd-face.digi123.cn/201511/80426ba27eef6380.jpg",
		// "http://dd-face.digi123.cn/201511/80426ba27eef6380.jpg",
		// "http://dd-face.digi123.cn/201511/80426ba27eef6380.jpg",
		// "http://dd-face.digi123.cn/201511/80426ba27eef6380.jpg",
		// "http://dd-face.digi123.cn/201511/80426ba27eef6380.jpg" };
		// Log.i("picsList", Arrays.toString(picsList));
		PhotoAlbumGridViewAdapter photoAlbumGridViewAdapter = new PhotoAlbumGridViewAdapter(
				instance, picsList, new IPhotoClickOper() {

					@Override
					public void clickoper(int position, Context context) {
						Intent intent = new Intent(context,
								IndexGalleryActivity.class);
						intent.putExtra("position", position);
						intent.putExtra("imgs", picsList);
						intent.putExtra("title", (position + 1) + "/"
								+ picsList.length);
						intent.putExtra("needOper", false);
						intent.putExtra("postIdList", postIdList);
						intent.putExtra("postType", photoAlbumList
								.get(position).getPostType());
						intent.putExtra("contextText", "查看原帖");
						intent.putExtra("operInterface", new OperContextClick());
						context.startActivity(intent);
					}
				});
		igv_photo_album_gallery.setAdapter(photoAlbumGridViewAdapter);
	}

	private void getPhotoAlbumListByMonth(String month) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryPhotoListByMonth");
		HashMap<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", userInfo.getUserSeqId());
		busiParams.put("queryMonth", month);
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {

			@Override
			public void success(JSONObject jsonObject) {
				dialog.dismiss();
				ErrorInfo errorInfo = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorInfo.getReturnCode())) {
					JSONObject returnInfo = jsonObject
							.getJSONObject("ReturnInfo");
					ArrayList<PhotoAlbum2> photoAlbums = (ArrayList<PhotoAlbum2>) JSON
							.parseArray(returnInfo.getString("ListOut"),
									PhotoAlbum2.class);
					Message message = new Message();
					message.what = GET_PHOTO_ALBUM_LIST_SUCCESS;
					message.obj = photoAlbums;
					handler.handleMessage(message);
				}
			}

			@Override
			public void start() {
				dialog = createDialog(instance, "拼命加载中......");
				dialog.show();
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {

			}

			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				dialog.dismiss();
			}
		});
	}
}
