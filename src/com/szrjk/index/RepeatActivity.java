package com.szrjk.index;

import java.util.HashMap;
import java.util.Map;

import u.aly.l;
import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.IndexFragment;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.CheckTextNumber;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.widget.CustomDialog;
import com.szrjk.widget.CustomDialog.ConfrimButtonListener;

//转发界面
@ContentView(R.layout.activity_repeat)
public class RepeatActivity extends BaseActivity implements OnClickListener {
	private RepeatActivity instance;
	@ViewInject(R.id.lly_back)
	private LinearLayout lly_back;
	@ViewInject(R.id.btn_back)
	private Button btback;
	@ViewInject(R.id.tv_repeat_send)
	private TextView tvSendRepeat;
	@ViewInject(R.id.et_repeat)
	private EditText etRepeat;
	@ViewInject(R.id.iv_avatar_repeat)
	private ImageView iv_avatar_repeat;
	@ViewInject(R.id.tv_Id_repeat)
	private TextView tv_name_repeat;
	@ViewInject(R.id.tv_text_repeat)
	private TextView tv_text_repeat;
	@ViewInject(R.id.tv_repeat_num)
	private TextView tv_repeat_num;
	
	@ViewInject(R.id.tv_repeat_num_all)
	private TextView tv_repeat_num_all;
	private final int num_all = 500;
	// 转发帖子内容string
	private String note_text;
	private String repeat_text;
	private String avatar_url;
	private String user_seq_id;
	private String post_id;
	private String post_level;
	private String user_name;
	private String post_type;
	private String src_post_id;
	private int position;
	private int forward_num;
	private Dialog dialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		// 跳转接收数据(用户头像、用户id、帖子大意)；
		getintent();
		// 将传入数据放入控件；
		putinview();
		// 设置listener
		initListener();
		dialog = createDialog(this, "发送中，请稍候...");
	}

	private void getintent() {
		// 解析传入intent赋值
		// 需要传入
		// 被转发用户id（Constant.USER_SEQ_ID）备用
		// 被转发用户名（ Constant.USER_NAME ）
		// 被转发用户的头像url (Constant.PIC_URL）
		// 被转发帖子正文 (Constant.POST_TEXT）
		// 原帖的postID （Constant.POST_ID）
		// 如不是转发原帖
		// 传入次级转发贴的（ pPostId ）
		// 转发层级 （postlevel ）
		//

		Intent intent = getIntent();
		note_text = intent.getStringExtra(Constant.POST_TEXT);
		avatar_url = intent.getStringExtra(Constant.PIC_URL);
		user_seq_id = intent.getStringExtra(Constant.USER_SEQ_ID);
		post_id = intent.getStringExtra(Constant.POST_ID);
		user_name = intent.getStringExtra(Constant.USER_NAME);
		post_type = intent.getStringExtra(Constant.POST_TYPE);
		src_post_id = intent.getStringExtra(Constant.SRC_POST_ID);
		position = intent.getIntExtra(Constant.POSITION, 0);
		forward_num = intent.getIntExtra(Constant.FORWARD_NUM, 0);
		// post_level:如果转发原帖，原帖传入的为空，则在此处设为1，如果原帖传入有值，则+1;
		String level = intent.getStringExtra(Constant.POST_LEVEL);
		if (level == null) {
			post_level = "1";
		} else {
			post_level = String.valueOf(Integer.valueOf(level) + 1);
		}

		// uesr_avatar = BitmapFactory.decodeByteArray(byte_user_avatar, 0,
		// byte_user_avatar.length);
	}

	private void putinview() {
		try {
			ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil(this, avatar_url,
					iv_avatar_repeat, R.drawable.icon_headfailed,
					R.drawable.icon_headfailed);
			imageLoaderUtil.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		tv_name_repeat.setText(user_name);
		tv_text_repeat.setText(note_text);
	}

	private void initListener() {
		tvSendRepeat.setOnClickListener(this);
		// 转发文字输入框的字数监听
		CheckTextNumber.setEditTextChangeListener(etRepeat, tv_repeat_num_all, 140);
//		etRepeat.addTextChangedListener(new TextWatcher() {
//			private CharSequence temp;
//			private int selectionStart;
//			private int selectionEnd;
//
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				temp = s;
//			}
//
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				int number = num_all - s.length();
//				tv_repeat_num.setText("" + number);
//				selectionStart = etRepeat.getSelectionStart();
//				selectionEnd = etRepeat.getSelectionEnd();
//				// System.out.println("start="+selectionStart+",end="+selectionEnd);
//				if (temp.length() > num_all) {
//					s.delete(selectionStart - 1, selectionEnd);
//					int tempSelection = selectionStart;
//					etRepeat.setText(s);
//					etRepeat.setSelection(tempSelection);
//				}
//			}
//		});
		lly_back.setOnClickListener(instance);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		// 回退按钮逻辑
//		case R.id.btn_back:
//			onBackPressed();
//			break;
		case R.id.lly_back:
//			onBackPressed();
			instance.finish();
			break;
		// 转发发送逻辑
		case R.id.tv_repeat_send:
			// 获取转发发送文字并intent发送，跳转回上一界面，并结束此界面
			repeat_text = etRepeat.getText().toString().trim();
			if (TextUtils.isEmpty(repeat_text)) {
				repeat_text = "转发";
			}
			// //转发帖子请求
			sendRepeat();
			break;
		}

	}

	private void sendRepeat() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "sendPostForward");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		busiParams.put("postLevel", post_level);
		busiParams.put("content", repeat_text);
		busiParams.put("deviceType", 1);
		switch (Integer.valueOf(post_level)) {
		case 1:
			busiParams.put("pPostId", post_id);
			busiParams.put("srcPostId", post_id);
			break;
		default:
			busiParams.put("pPostId", post_id);
			busiParams.put("srcPostId", src_post_id);
			break;
		}
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			public void success(JSONObject jsonObject) {
				dialog.dismiss();
				tvSendRepeat.setClickable(true);
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					Toast.makeText(RepeatActivity.this, "转发成功",
							Toast.LENGTH_SHORT).show();
//					Intent intent = new Intent();
//					intent.putExtra(Constant.POSITION, position);
//					intent.putExtra(Constant.FORWARD_NUM, forward_num + 1);
//					setResult(Constant.FORWARD_RESULTCODE, intent);
					IndexFragment.FORWARD_NUM = forward_num+1;
					finish();
				} else {
					Toast.makeText(RepeatActivity.this, "转发失败",
							Toast.LENGTH_SHORT).show();
				}

			}

			public void start() {
				dialog.show();
				dialog.setCancelable(false);
				tvSendRepeat.setClickable(false);
			
			}

			public void loading(long total, long current, boolean isUploading) {
			}

			public void failure(HttpException exception, JSONObject jobj) {
				String err =jobj.toString();
				if (err.contains("Incorrect string value")) {
					runOnUiThread(new Runnable() {
						public void run() {
							dialog.dismiss();
							tvSendRepeat.setClickable(true);
							showToast(instance, "目前不支持表情发送", 0);
						}
					});
				}else{
					runOnUiThread(new Runnable() {
						public void run() {
							dialog.dismiss();
							tvSendRepeat.setClickable(true);
							showToast(instance, "转发失败，再试试呗", 0);
						}
					});
				}
			}
		});
	}

//	public void onBackPressed() {
//		Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle("确认放弃转发吗？");
//		builder.setIcon(android.R.drawable.ic_dialog_info);
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				instance.finish();
//			}
//		});
//		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//			}
//		});
//		builder.setCancelable(false);
//		builder.show();
//		repeat_text = etRepeat.getText().toString().trim();
//		if (TextUtils.isEmpty(repeat_text)) {
//			instance.finish();
//		}else{
//			CustomDialog dialog = new CustomDialog(instance, "确认放弃转发吗？", "确定", "取消", 
//					new ConfrimButtonListener() {
//				public void confrim() {
//					instance.finish();
//				}
//			});
//			dialog.setCanceledOnTouchOutside(true);
//			dialog.show();
//		}
//	}
//	public void cancel(){
//		CustomDialog dialog = new CustomDialog(instance, "确认放弃转发吗？", "确定", "取消", 
//				new ConfrimButtonListener() {
//					public void confrim() {
//						instance.finish();
//					}
//				});
//		dialog.setCanceledOnTouchOutside(true);
//		dialog.show();
//	}
}
