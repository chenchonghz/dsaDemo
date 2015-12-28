package com.szrjk.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.szrjk.adapter.PhotoGridAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.GalleryActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ActivityEnum;
import com.szrjk.entity.CasePuzzPostEntity;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.IPopupItemCallback;
import com.szrjk.entity.ISelectImgCallback;
import com.szrjk.entity.PopupItem;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.CheckTextNumber;
import com.szrjk.util.DisplayUtil;
import com.szrjk.util.DjsonUtils;
import com.szrjk.util.ImageItem;
import com.szrjk.util.MultipleUploadPhotoUtils;
import com.szrjk.util.OssUpdateImgUtil;
import com.szrjk.util.PostSaveUtil;
import com.szrjk.util.SharePerferenceUtil;
import com.szrjk.util.ToastUtils;
import com.szrjk.widget.AddPhotoPopup;
import com.szrjk.widget.DeptButton;
import com.szrjk.widget.FlowDeptLayout;
import com.szrjk.widget.HeaderView;
import com.szrjk.widget.IndexGridView;
import com.szrjk.widget.ListPopup;
import com.szrjk.widget.PostSendPopup;
import com.szrjk.widget.UpdateProgressBar;

@ContentView(R.layout.activity_puzzle)
public class SendPuzzleActivity extends BaseActivity {

	@ViewInject(R.id.tv_case_length)
	private TextView tv_case_length;

	@ViewInject(R.id.tv_check_length)
	private TextView tv_check_length;

	// 标题
	@ViewInject(R.id.et_title)
	private EditText et_title;
	// 标题字数提示
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	// 选择科室
	@ViewInject(R.id.lly_dept)
	private LinearLayout lly_dept;
	private SendPuzzleActivity instance;
	private final static int SEL_DEPT_CODE = 100;
	private final static int GALLERY_RESULT_TYPE1 = 2001;
	private final static int GALLERY_RESULT_TYPE2 = 2002;
	// 发送
	@ViewInject(R.id.hv_puzz)
	private HeaderView hv_puzz;

	// 取消
	@ViewInject(R.id.lly_cancel)
	private LinearLayout lly_cancel;
	// 主诉、病史
	@ViewInject(R.id.tv_case)
	private TextView tv_case;
	// 查体、辅查
	@ViewInject(R.id.tv_check)
	private TextView tv_check;
	// 主诉、病史布局
	@ViewInject(R.id.lly_case)
	private LinearLayout lly_case;
	// 主诉、病史内容
	@ViewInject(R.id.et_case)
	private EditText et_case;
	// 查体、辅查布局
	@ViewInject(R.id.lly_check)
	private LinearLayout lly_check;
	@ViewInject(R.id.tv_treat)
	private TextView tv_treat;
	// 查体、辅查内容
	@ViewInject(R.id.et_check)
	private EditText et_check;
	@ViewInject(R.id.et_treat)
	private EditText et_treat;
	@ViewInject(R.id.lly_treat)
	private LinearLayout lly_treat;
	@ViewInject(R.id.lly_post)
	private LinearLayout lly_post;
	@ViewInject(R.id.tv_treat_length)
	private TextView tv_treat_length;
	private Resources resources;
	private String title, strCase, strCheck, strTreat, deptIds, deptValues;
	private UserInfo userInfo;
	private int completeRate = 0;
	private static final int CAMERA_WITH_DATA = 3022;
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	private static final int GALLERY_RESULT_TYPE3 = 2003;
	// 进度条
	@ViewInject(R.id.pb_loading)
	private UpdateProgressBar pb_loading;
	// 相册容器
	@ViewInject(R.id.gv_case_list)
	private IndexGridView gv_case_list;
	// 相册容器
	@ViewInject(R.id.gv_check_list)
	private IndexGridView gv_check_list;
	// 相册容器
	// 诊断、治疗图片
	@ViewInject(R.id.gv_treat_list)
	private IndexGridView gv_treat_list;

	@ViewInject(R.id.fl_content)
	private FlowDeptLayout fl_content;

	@ViewInject(R.id.tv_dept)
	private TextView tv_dept;
	// 相册适配器
	private PhotoGridAdapter gridAdapter;
	// 相册适配器
	private PhotoGridAdapter checkAdapter;

	private InputMethodManager imm;

	private PhotoGridAdapter treatAdapter;

	private Dialog dialog;

	private SharePerferenceUtil shareUtil;

	//这个是科室的高度
	private int px ;

	private String ls2 ="";

	private String ls3 ="";

	private String ls1 ="";

	// 点击图片增加按钮的时候;或者点击了上传的图片的时候的响应
	private ArrayList<String> urlList1 = new ArrayList<String>();
	private ArrayList<String> urlList2 = new ArrayList<String>();
	private ArrayList<String> urlList3 = new ArrayList<String>();
	private MultipleUploadPhotoUtils multipleUploadPhotoUtils1;
	private MultipleUploadPhotoUtils multipleUploadPhotoUtils2;
	private MultipleUploadPhotoUtils multipleUploadPhotoUtils3;

	private ArrayList<String> absList1= new ArrayList<String>();
	private ArrayList<String> absList2= new ArrayList<String>();
	private ArrayList<String> absList3= new ArrayList<String>();

	private TextView hv_sub;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		initLayout();
		dialog = createDialog(this, "发送中，请稍候...");


	}

	private void initLayout() {
		try {
			userInfo = Constant.userInfo;
			resources = getResources();
			shareUtil = SharePerferenceUtil.getInstance(instance,
					Constant.USER_INFO);
			// 相册
			gv_case_list.setSelector(new ColorDrawable(Color.TRANSPARENT));
			gridAdapter = new PhotoGridAdapter(instance,
					new ArrayList<ImageItem>());
			gv_case_list.setAdapter(gridAdapter);

			// 相册
			gv_check_list.setSelector(new ColorDrawable(Color.TRANSPARENT));
			checkAdapter = new PhotoGridAdapter(instance,
					new ArrayList<ImageItem>());
			gv_check_list.setAdapter(checkAdapter);

			gv_treat_list.setSelector(new ColorDrawable(Color.TRANSPARENT));
			treatAdapter = new PhotoGridAdapter(instance,
					new ArrayList<ImageItem>());
			gv_treat_list.setAdapter(treatAdapter);

			// 检查edittext 里面的字数，动态显示剩余个数
			CheckTextNumber.setEditTextChangeListener(et_title, tv_title, 30);

			CheckTextNumber.setEditTextChangeListener(et_case, tv_case_length,
					800);
			CheckTextNumber.setEditTextChangeListener(et_check,
					tv_check_length, 800);
			CheckTextNumber.setEditTextChangeListener(et_treat,
					tv_treat_length, 800);

			et_case.setOnTouchListener(et_ls);
			et_check.setOnTouchListener(et_ls);
			et_treat.setOnTouchListener(et_ls);
			//		instance.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			//检查是否之前有草稿

			hv_puzz.setBtnBackOnClick(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						closeKeyboard();
						if (checkSave()) {
							shareUtil.setBooleanValue(Constant.ISAVEPUZZ, false);
							finish();
						} else {
							showPop();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			//点击提交
			hv_sub = hv_puzz.getTextBtn();
			hv_puzz.showTextBtn("发布", new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						closeKeyboard();
						hv_sub.setClickable(false);
						if (checkInput()) {
							//				dialog.setCancelable(false);
							dialog.show();
							send();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			checkSaveContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// 校验表单输入
	private boolean checkInput() {
		// 获得控件里面的内容
		getEditTextString();
		if (title == null || title.length() == 0) {
			showToast(instance, "标题不能空！", 0);
			hv_sub.setClickable(true);
			return false;
		}
		if (deptIds == null) {
			showToast(instance, "请选择科室！", 0);
			hv_sub.setClickable(true);
			return false;
		}

		return true;
	}

	// 发送帖子
	private void send() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "sendPuzzlePost");
		Map<String, Object> busiParams = new HashMap<String, Object>();

		checkImageList();

		countCompleteRate();
		busiParams.put("userSeqId", userInfo.getUserSeqId());
		busiParams.put("deviceType", "1");
		busiParams.put("deptList", deptIds);
		busiParams.put("postTitle", title);
		busiParams.put("completeRate", String.valueOf(completeRate));

		busiParams.put("chapterTitle1", "主诉、病史");
		busiParams.put("content1", strCase);
		busiParams.put("picList1", ls1);

		busiParams.put("chapterTitle2", "查体、辅查");
		busiParams.put("content2", strCheck);
		busiParams.put("picList2", ls2);

		busiParams.put("chapterTitle3", "诊断、治疗");
		busiParams.put("content3", strTreat);
		busiParams.put("picList3", ls3);

		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void start() {
				//				dialog.setCancelable(false);
				//				dialog.show();
				//				tv_send.setClickable(false);
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {

			}

			@Override
			public void failure(HttpException exception, JSONObject jsonObject) {
				final String err = jsonObject.toString();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dialog.dismiss();
						showToast(instance, "发帖失败、再试试呗", 0);
						hv_sub.setClickable(true);
						if (err.contains("Incorrect string value")) {
							showToast(instance, "目前不支持表情发送", 0);
						}
					}
				});
			}

			@Override
			public void success(JSONObject jsonObject) {
				dialog.dismiss();
				hv_sub.setClickable(true);
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);

				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					showToast(instance, "发帖成功!", Toast.LENGTH_SHORT);
					gridAdapter.returnImageInfo().clear();
					//下次进来就没有草稿
					shareUtil.setBooleanValue(Constant.ISAVEPUZZ, false);
					finish();
				} else {
					//检查ErrorMessage里面的提示

					Log.i("message", errorObj.getErrorMessage());
					if (errorObj.getErrorMessage().contains("Incorrect string value")) {
						showToast(instance, "目前不支持表情发送", 0);
						dialog.dismiss();
						hv_sub.setClickable(true);
					}
				}
			}
		});

	}

	/**
	 * 检查完整%比
	 */
	private void countCompleteRate() {
		if (title.length() > 0) {
			completeRate = 10;
		}
		if (deptIds.length() > 0) {
			completeRate += 30;
		}

		if (strCase.length() > 0) {
			completeRate += 20;
		}else if (ls1 != null) {
			if (ls1.length() > 0) {
				completeRate += 20;
			}
		}

		if (strCheck.length() > 0) {
			completeRate += 20;
		}else if (ls2 != null) {
			if (ls2.length() > 0) {
				completeRate += 20;
			}
		}

		if (strTreat.length() > 0) {
			completeRate += 20;
		}else if (ls3 != null) {
			if (ls3.length() > 0) {
				completeRate += 20;
			}
		}

	}

	// 跳转部门选择界面
	@OnClick(R.id.lly_dept)
	public void deptClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.lly_dept:
				jumpDeptActivity();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 点击病史按钮
	@OnClick(R.id.tv_case)
	public void caseClick(View v) {
		try {
			findFocusable(et_case);
			switch (v.getId()) {
			case R.id.tv_case:
				lly_case.setVisibility(View.VISIBLE);
				lly_check.setVisibility(View.GONE);
				lly_treat.setVisibility(View.GONE);
				int color = resources.getColor(R.color.search_bg);
				float fontSize = resources
						.getDimension(R.dimen.font_size_small);
				int size = DisplayUtil.px2sp(instance, fontSize);
				tv_case.setTextColor(color);
				color = resources.getColor(R.color.white);
				tv_case.setBackgroundColor(color);
				tv_case.setTextSize(size);
				color = resources.getColor(R.color.font_cell);
				tv_check.setTextColor(color);
				tv_treat.setTextColor(color);
				fontSize = resources.getDimension(R.dimen.font_size_msmall);
				size = DisplayUtil.px2sp(instance, fontSize);
				color = resources.getColor(R.color.base_bg);
				tv_check.setTextSize(size);
				tv_treat.setTextSize(size);
				tv_check.setBackgroundColor(color);

				tv_treat.setBackgroundColor(color);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 检查按钮
	@OnClick(R.id.tv_check)
	public void checkClick(View v) {
		try {
			findFocusable(et_check);
			switch (v.getId()) {
			case R.id.tv_check:
				lly_check.setVisibility(View.VISIBLE);
				lly_case.setVisibility(View.GONE);
				lly_treat.setVisibility(View.GONE);
				int color = resources.getColor(R.color.search_bg);
				float fontSize = resources
						.getDimension(R.dimen.font_size_small);
				int size = DisplayUtil.px2sp(instance, fontSize);
				tv_check.setTextColor(color);
				tv_check.setTextSize(size);
				color = resources.getColor(R.color.white);
				tv_check.setBackgroundColor(color);
				color = resources.getColor(R.color.font_cell);
				tv_case.setTextColor(color);
				tv_treat.setTextColor(color);
				fontSize = resources.getDimension(R.dimen.font_size_msmall);
				size = DisplayUtil.px2sp(instance, fontSize);
				color = resources.getColor(R.color.base_bg);
				tv_case.setTextSize(size);
				tv_treat.setTextSize(size);
				tv_case.setBackgroundColor(color);

				tv_treat.setBackgroundColor(color);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnClick(R.id.tv_treat)
	public void treatClick(View v) {
		try {
			findFocusable(et_treat);
			switch (v.getId()) {
			case R.id.tv_treat:
				lly_check.setVisibility(View.GONE);
				lly_case.setVisibility(View.GONE);
				lly_treat.setVisibility(View.VISIBLE);
				// lly_visit.setVisibility(View.GONE);
				float fontSize = resources
						.getDimension(R.dimen.font_size_small);
				int size = DisplayUtil.px2sp(instance, fontSize);
				int color = resources.getColor(R.color.search_bg);
				tv_treat.setTextColor(color);
				color = resources.getColor(R.color.white);
				tv_treat.setBackgroundColor(color);
				tv_treat.setTextSize(size);
				color = resources.getColor(R.color.font_cell);
				fontSize = resources.getDimension(R.dimen.font_size_msmall);
				size = DisplayUtil.px2sp(instance, fontSize);
				tv_case.setTextColor(color);
				tv_check.setTextColor(color);
				tv_case.setTextSize(size);
				tv_check.setTextSize(size);
				color = resources.getColor(R.color.base_bg);
				tv_check.setBackgroundColor(color);
				tv_case.setBackgroundColor(color);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnItemClick(R.id.gv_case_list)
	public void caseItemClick(AdapterView<?> adapterView, View view, int num,
			long position) {
		try {
			if (num == gridAdapter.returnImageInfo().size()) {
				int maxNum = 9 - gridAdapter.returnImageInfo().size();
				if (maxNum == 0) {
					ToastUtils.showMessage(this, "照片最多9张");
				}
				multipleUploadPhotoUtils1 = new MultipleUploadPhotoUtils(
						instance, lly_post, maxNum, new ISelectImgCallback() {
							@Override
							public void selectImgCallback(
									List<ImageItem> imgList, String[] urlarr) {

								// ToastUtils.showMessage(instance,
								// imgList.size()+""+ DjsonUtils.bean2Json(urlarr));
								gridAdapter.addImageList(imgList);
								gridAdapter.notifyDataSetChanged();
								for (int i = 0; i < urlarr.length; i++) {
									urlList1.add(OssUpdateImgUtil.feedPicFilterUrl
											+ urlarr[i]);
								}
								for (int j = 0; j < imgList.size(); j++) {
									absList1.add(imgList.get(j).getAbsPaht());
								}
								Log.i("图片地址", urlList1.toString());
								multipleUploadPhotoUtils1 = null;
							}
						});
			} else {
				System.out.println("点击了图片进入编辑gallery");
				Intent intent = new Intent(instance, GalleryActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("id", num);
				//把图片地址的urlList传递过去
				bundle.putStringArrayList("urllist", urlList1);
				bundle.putStringArrayList("absList", absList1);
				//				GalleryActivity.filltmpitems(gridAdapter.returnImageInfo());
				intent.putExtras(bundle);
				startActivityForResult(intent, GALLERY_RESULT_TYPE1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnItemClick(R.id.gv_check_list)
	public void checkItemClick(AdapterView<?> adapterView, View view, int num,
			long position) {
		try {
			if (num == checkAdapter.returnImageInfo().size()) {
				int maxNum = 9 - checkAdapter.returnImageInfo().size();
				if (maxNum == 0) {
					ToastUtils.showMessage(this, "照片最多9张");
				}
				multipleUploadPhotoUtils2 = new MultipleUploadPhotoUtils(
						instance, lly_post, maxNum, new ISelectImgCallback() {
							@Override
							public void selectImgCallback(
									List<ImageItem> imgList, String[] urlarr) {
								// ToastUtils.showMessage(instance,
								// imgList.size()+""+ DjsonUtils.bean2Json(urlarr));
								checkAdapter.addImageList(imgList);
								checkAdapter.notifyDataSetChanged();
								for (int i = 0; i < urlarr.length; i++) {
									urlList2.add(OssUpdateImgUtil.feedPicFilterUrl
											+ urlarr[i]);
								}
								for (int j = 0; j < imgList.size(); j++) {
									absList2.add(imgList.get(j).getAbsPaht());
								}
								Log.i("图片地址2", urlList2.toString());
								multipleUploadPhotoUtils2 = null;
							}
						});
			} else {
				System.out.println("点击了图片进入编辑gallery");
				Intent intent = new Intent(instance, GalleryActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("id", num);
				//把图片地址的urlList传递过去
				bundle.putStringArrayList("urllist", urlList2);
				bundle.putStringArrayList("absList", absList2);
				//				GalleryActivity.filltmpitems(checkAdapter.returnImageInfo());
				intent.putExtras(bundle);
				startActivityForResult(intent, GALLERY_RESULT_TYPE2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnItemClick(R.id.gv_treat_list)
	public void treatItemClick(AdapterView<?> adapterView, View view, int num,
			long position) {
		try {
			if (num == treatAdapter.returnImageInfo().size()) {
				int maxNum = 9 - treatAdapter.returnImageInfo().size();
				if (maxNum == 0) {
					ToastUtils.showMessage(this, "照片最多9张");
				}
				multipleUploadPhotoUtils3 = new MultipleUploadPhotoUtils(
						instance, lly_post, maxNum, new ISelectImgCallback() {
							@Override
							public void selectImgCallback(
									List<ImageItem> imgList, String[] urlarr) {
								// ToastUtils.showMessage(instance,
								// imgList.size()+""+ DjsonUtils.bean2Json(urlarr));
								treatAdapter.addImageList(imgList);
								treatAdapter.notifyDataSetChanged();
								for (int i = 0; i < urlarr.length; i++) {
									urlList3.add(OssUpdateImgUtil.feedPicFilterUrl
											+ urlarr[i]);
								}
								for (int j = 0; j < imgList.size(); j++) {
									absList3.add(imgList.get(j).getAbsPaht());
								}
								Log.i("图片地址2", urlList3.toString());
								multipleUploadPhotoUtils3 = null;
							}
						});
			} else {
				System.out.println("点击了图片进入编辑gallery");
				Intent intent = new Intent(instance, GalleryActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("id", num);
				//把图片地址的urlList传递过去
				bundle.putStringArrayList("urllist", urlList3);
				bundle.putStringArrayList("absList", absList3);
				//				GalleryActivity.filltmpitems(treatAdapter.returnImageInfo());
				intent.putExtras(bundle);
				startActivityForResult(intent, GALLERY_RESULT_TYPE3);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 跳转到部门选择
	private void jumpDeptActivity() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt(Constant.ACTIVITYENUM,
				ActivityEnum.SeedPuzzleActivity1.value());
		bundle.putString("DeptIds", deptIds);
		bundle.putString("DeptValues", deptValues);
		intent.putExtras(bundle);
		intent.setClass(instance, DepartmentGVActivity.class);
		startActivityForResult(intent, SEL_DEPT_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode != RESULT_OK) {
				return;
			}
			switch (requestCode) {
			case SEL_DEPT_CODE:

				if (data.getExtras() == null) {
					return;
				}
				Bundle bundle = data.getExtras();
				deptIds = bundle.getString("DeptIds");
				deptValues = bundle.getString("DeptValues");
				px = bundle.getInt("height", 0);
				fl_content.removeAllViews();
				if (null != deptIds) {
					addDeptment(deptIds, deptValues);
					lly_dept.setBackground(instance.getResources().getDrawable(
							R.color.white));
				} else {
					tv_dept.setVisibility(View.VISIBLE);
					lly_dept.setBackground(instance.getResources().getDrawable(
							R.color.white));
				}
				break;
			case CAMERA_WITH_DATA:
				if (multipleUploadPhotoUtils1 != null) {
					multipleUploadPhotoUtils1.operResult(requestCode,
							resultCode, data);
				}

				if (multipleUploadPhotoUtils2 != null) {
					multipleUploadPhotoUtils2.operResult(requestCode,
							resultCode, data);
				}

				if (multipleUploadPhotoUtils3 != null) {
					multipleUploadPhotoUtils3.operResult(requestCode,
							resultCode, data);
				}
				break;
			case PHOTO_PICKED_WITH_DATA:

				if (multipleUploadPhotoUtils1 != null) {
					multipleUploadPhotoUtils1.operResult(requestCode,
							resultCode, data);
				}

				if (multipleUploadPhotoUtils2 != null) {
					multipleUploadPhotoUtils2.operResult(requestCode,
							resultCode, data);
				}

				if (multipleUploadPhotoUtils3 != null) {
					multipleUploadPhotoUtils3.operResult(requestCode,
							resultCode, data);
				}

				break;

			case GALLERY_RESULT_TYPE1:
				//同步图片地址list
				if (data != null) {
					//ArrayList<String>
					urlList1 = data.getStringArrayListExtra("urllist");
					Log.i("urlList1", urlList1.toString());
					//					gridAdapter.setImageList(GalleryActivity.gettmpitems());
					absList1 = data.getStringArrayListExtra("absList");
					gridAdapter.addStringUrl(absList1);
					gridAdapter.notifyDataSetChanged();
				}
				break;
			case GALLERY_RESULT_TYPE2:
				//同步图片地址list
				if (data != null) {
					//ArrayList<String>
					urlList2 = data.getStringArrayListExtra("urllist");
					Log.i("urlList2", urlList2.toString());
					//					checkAdapter.setImageList(GalleryActivity.gettmpitems());
					absList2 = data.getStringArrayListExtra("absList");
					gridAdapter.addStringUrl(absList2);
					checkAdapter.notifyDataSetChanged();
				}
				break;
			case GALLERY_RESULT_TYPE3:
				//同步图片地址list
				if (data != null) {
					//ArrayList<String>
					urlList3 = data.getStringArrayListExtra("urllist");
					Log.i("urlList3", urlList3.toString());
					//					treatAdapter.setImageList(GalleryActivity.gettmpitems());
					absList3 = data.getStringArrayListExtra("absList");
					gridAdapter.addStringUrl(absList3);
					treatAdapter.notifyDataSetChanged();
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addDeptment(String ids, String values) {
		String[] strIds = ids.split(";");
		String[] strValues = values.split(";");
		int i = 0;
		for (String id : strIds) {
			int txtColor = resources.getColor(R.color.black);
			DeptButton btn = new DeptButton(this);
			btn.setTextColor(txtColor);
			btn.setGravity(Gravity.CENTER);
			//设置样式
			setStyle(btn);
			btn.setText(strValues[i]);
			btn.setTag(id);
			i++;
			fl_content.addView(btn);
		}
		if (i > 0) {
			tv_dept.setVisibility(View.GONE);
		}
	}

	private void setStyle(DeptButton btnStyle) {
		btnStyle.setHeight(px);
		btnStyle.setPadding(12, 10, 12, 10);
		btnStyle.setTextColor(resources.getColor(R.color.header_bg));
		btnStyle.setBackground(resources.getDrawable(R.drawable.flow_dept_selector));
	}


	@Override
	public void onBackPressed() {
		try {
			//		showToast(instance, "草稿功能后续开发", 0);
			closeKeyboard();
			if (checkSave()) {
				dialog.dismiss();
				shareUtil.setBooleanValue(Constant.ISAVEPUZZ, false);
				finish();
			} else {
				showPop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// edit主动获得焦点
	private void findFocusable(EditText et_case2) {
		et_case2.setFocusable(true);
		et_case2.setFocusableInTouchMode(true);
		et_case2.requestFocus();
		et_case2.findFocus();
	}
	//隐藏软盘
	private void closeKeyboard() {
		if (imm.isActive() && getCurrentFocus() != null) {
			if (getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
	//当点击三个标签的时候，整个界面往上移动
	private View.OnTouchListener et_ls = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.getParent().requestDisallowInterceptTouchEvent(false);
			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
				v.getParent().requestDisallowInterceptTouchEvent(false);
			}
			return false;
		}
	};

	private String[] img1;
	private String[] img2;
	private String[] img3;
	private boolean checkSave() {
		checkImageList();
		String s1 = et_title.getText().toString().trim();
		String s2 = deptIds;
		String s3 = et_case.getText().toString().trim();
		String s4 = et_check.getText().toString().trim();
		String s5 = et_treat.getText().toString().trim();
		if (s1 != null) {
			if (s1.length()> 0) {
				return false;
			}
		}
		if (s2 != null) {
			if (s2.length()> 0) {
				return false;
			}
		}
		if (s3 != null) {
			if (s3.length()> 0) {
				return false;
			}
		}
		if (s4 != null) {
			if (s4.length()> 0) {
				return false;
			}
		}
		if (s5 != null) {
			if (s5.length()> 0) {
				return false;
			}
		}
		if (ls1 != null) {
			if (ls1.length() > 0) {
				return false;
			}
		}
		if (ls1 != null) {
			if (ls1.length() > 0) {
				return false;
			}
		}
		if (ls2 != null) {
			if (ls2.length() > 0) {
				return false;
			}
		}
		if (ls3 != null) {
			if (ls3.length() > 0) {
				return false;
			}
		}
		return true;
	}
	private void checkImageList() {
		if (urlList1.size() > 0) {
			StringBuffer stringBuffer1 = new StringBuffer();
			for (int i = 0; i < urlList1.size(); i++) {
				stringBuffer1.append(urlList1.get(i));
				stringBuffer1.append("|");
			}
			String st1 = stringBuffer1.toString();
			ls1 = st1.substring(0, st1.length() - 1);
		}

		if (urlList2.size() > 0) {
			StringBuffer stringBuffer2 = new StringBuffer();
			for (int i = 0; i < urlList2.size(); i++) {
				stringBuffer2.append(urlList2.get(i));
				stringBuffer2.append("|");
			}
			String st2 = stringBuffer2.toString();
			ls2 = st2.substring(0, st2.length() - 1);
		}

		if (urlList3.size() > 0) {
			StringBuffer stringBuffer3 = new StringBuffer();
			for (int i = 0; i < urlList3.size(); i++) {
				stringBuffer3.append(urlList3.get(i));
				stringBuffer3.append("|");
			}
			String st3 = stringBuffer3.toString();
			ls3 = st3.substring(0, st3.length() - 1);
		}
	}

	/**显示sendWindow**/
	private void showPop(){
		try {
			//		sendWindow = new PostSendPopup(instance, sendPostClick);

			List<PopupItem> pilist = new ArrayList<PopupItem>();
			PopupItem pi1 = new PopupItem();
			pi1.setItemname("保存");//设置名称
			pi1.setColor(resources.getColor(R.color.search_bg));
			pi1.setiPopupItemCallback(new IPopupItemCallback() { //设置click动作
				@Override
				public void itemClickFunc(PopupWindow popupWindow) {
					//				ToastUtils.showMessage(instance, "后续开发");
					//这里保存标题、科室、高度、多个标签的图片URL
					//				shareUtil.set
					saveContent();
					//				instance.finish();
					//				DjsonUtils.json2Bean(jsonStr, ToastUtils.class);
				}
			});
			PopupItem pi2 = new PopupItem();
			pi2.setItemname("放弃");
			pi2.setColor(resources.getColor(R.color.search_bg));
			pi2.setiPopupItemCallback(new IPopupItemCallback() {
				@Override
				public void itemClickFunc(PopupWindow popupWindow) {
					//				ToastUtils.showMessage(instance, "放弃");
					shareUtil.setBooleanValue(Constant.ISAVEPUZZ, false);
					instance.finish();
				}
			});
			pilist.add(pi1);
			pilist.add(pi2);
			new ListPopup(instance, pilist, lly_post);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void getEditTextString()	{
		title = et_title.getText().toString().trim();
		strCase = et_case.getText().toString().trim();
		strCheck = et_check.getText().toString().trim();
		strTreat = et_treat.getText().toString().trim();
	}
	//保存内容
	private void saveContent(){
		try {
			//准备保存到xml里面的字符串
			String jsonStr = null;
			//准备保存实体
			CasePuzzPostEntity entity = new CasePuzzPostEntity();
			//获得控件里面的内容
			getEditTextString();
			entity.setTitle(title);
			entity.setDeptId(deptIds);
			entity.setDeptValue(deptValues);
			entity.setCaseContent(strCase);
			entity.setCheckContent(strCheck);
			entity.setTreatContent(strTreat);
			entity.setCaseImg(ls1);
			//		Log.i("save:ls1", ls1);
			entity.setCheckImg(ls2);
			entity.setTreatImg(ls3);
			entity.setPx(px);
			//		DjsonUtils.json2Bean(jsonStr, CasePuzzPostEntity.class);
			jsonStr = DjsonUtils.bean2Json(entity);
			Log.i("保存的内容", jsonStr);
			shareUtil.setStringValue(Constant.SAVEPUZZ, jsonStr);
			shareUtil.setBooleanValue(Constant.ISAVEPUZZ, true);
			Log.i("", "草稿已经保存到xml");
			showToast(instance, "已保存草稿", 0);
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//检查是否有保存草稿
	private void checkSaveContent(){
		try {
			boolean issave = shareUtil.getBooleanValue(Constant.ISAVEPUZZ,
					false);
			if (issave) {
				showToast(instance, "正在恢复草稿", 0);
				String saveStr = shareUtil.getStringValue(Constant.SAVEPUZZ);
				CasePuzzPostEntity entity = DjsonUtils.json2Bean(saveStr,
						CasePuzzPostEntity.class);
				//去标题
				if (entity.getTitle() != null) {
					et_title.setText(entity.getTitle());
				}
				//病史内容
				if (entity.getCaseContent() != null) {
					et_case.setText(entity.getCaseContent());
				}
				//检查内容
				if (entity.getCheckContent() != null) {
					et_check.setText(entity.getCheckContent());
				}
				//诊断
				if (entity.getTreatContent() != null) {
					et_treat.setText(entity.getTreatContent());
				}
				//科室
				if (entity.getPx() > 0) {
					px = entity.getPx();
					deptIds = entity.getDeptId();
					deptValues = entity.getDeptValue();
					addDeptment(deptIds, deptValues);
				}
				setSaveImage(entity.getCaseImg(), img1, urlList1, gridAdapter);
				setSaveImage(entity.getCheckImg(), img2, urlList2, checkAdapter);
				setSaveImage(entity.getTreatImg(), img3, urlList3, treatAdapter);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param s			保存图片的整个string
	 * @param arr		由s拆分的图片URL数组
	 * @param ils		原来保存图片URL的list
	 * @param adapter	显示图片对应的适配器
	 */
	private void setSaveImage(String s,String[] arr,ArrayList<String> ils ,BaseAdapter adapter){
		try {
			if (s == null) {
				Log.i("", "没有图片");
				return;
			}
			//图片标签
			if (s.length() > 0) {
				arr = s.split("\\|");
				for (int i = 0; i < arr.length; i++) {
					ils.add(arr[i]);
				}
				for (int i = 0; i < arr.length; i++) {
					PostSaveUtil postSaveUtil = new PostSaveUtil();
					postSaveUtil.getSaveImage(arr[i],adapter);
				}
			}
		} catch (Exception e) {
			Log.i("sendpuzz", "异步任务加载图片出现异常");
			e.printStackTrace();
		}
	}
	/**
	 * 这里的3个重写方法：由于打开了拍照，低ram的手机会回收发帖这个Activity。
	 * 当回收之后，会执行onCreate方法里面的检查草稿方法。导致把草稿恢复覆盖当前编辑的内容
	 * @param newConfig
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		Log.i("onConfigurationChanged", "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i("onRestoreInstanceState", "onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("onSaveInstanceState", "onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}
}
