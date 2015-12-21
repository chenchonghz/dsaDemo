package com.szrjk.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.config.Constant;
import com.szrjk.dhome.IndexFragment;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.IPopupItemCallback;
import com.szrjk.entity.PopupItem;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.index.CaseDetailActivity;
import com.szrjk.index.PostDetailActivity;
import com.szrjk.index.PostDetailFowardActivity;
import com.szrjk.index.PostDetailFowardActivity2;
import com.szrjk.util.ToastUtils;

public class PostDetailHeaderView extends RelativeLayout {

	private TextView tv_text;
	private LinearLayout lly_hv;
	private ImageView iv_back;
	private LinearLayout ll_dotmore;
	private Context context;
	private String postId;
	private String postUserSeqId;

	public TextView gettv_text() {
		return tv_text;
	}

	public void settv_text(TextView tv_text) {
		this.tv_text = tv_text;
	}

	public LinearLayout getLly_hv() {
		return lly_hv;
	}

	public void setLly_hv(LinearLayout lly_hv) {
		this.lly_hv = lly_hv;
	}

	public ImageView getIv_back() {
		return iv_back;
	}

	public void setIv_back(ImageView iv_back) {
		this.iv_back = iv_back;
	}

	public LinearLayout getLl_dotmore() {
		return ll_dotmore;
	}

	public void setLl_dotmore(LinearLayout ll_dotmore) {
		this.ll_dotmore = ll_dotmore;
	}

	public PostDetailHeaderView(Context context) {
		super(context);
	}

	public PostDetailHeaderView(final Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.postdetail_headerview, this);
		tv_text = (TextView) findViewById(R.id.tv_header);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		ll_dotmore = (LinearLayout) findViewById(R.id.ll_dotmore);
		lly_hv = (LinearLayout) findViewById(R.id.lly_hv);

		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.PostDetailHeaderView);
		String textStr = a.getString(R.styleable.PostDetailHeaderView_text);
		a.recycle();
		tv_text.setText(textStr);
		lly_hv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				((Activity) context).onKeyDown(KeyEvent.KEYCODE_BACK,
						new KeyEvent(KeyEvent.ACTION_DOWN,
								KeyEvent.KEYCODE_BACK));
				((Activity) context).finish();
			}
		});
		ll_dotmore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// DeletePostPopup deletePostPopup = new
				// DeletePostPopup((Activity)context, null);
				// deletePostPopup.showAtLocation(PostDetailHeaderView.this,
				// Gravity.BOTTOM
				// | Gravity.CENTER_HORIZONTAL, 0, 0);
				List<PopupItem> popupItems = new ArrayList<PopupItem>();
				PopupItem popupItem = new PopupItem();
				popupItem.setItemname("删除帖子");
				popupItem
						.setColor(context.getResources().getColor(R.color.red));
				popupItem.setiPopupItemCallback(new IPopupItemCallback() {

					@Override
					public void itemClickFunc(PopupWindow sendWindow) {
						sendWindow.dismiss();
						deletePost();
					}
				});
				popupItems.add(popupItem);
				ListPopup listPopup = new ListPopup((Activity) context,
						popupItems, PostDetailHeaderView.this);
			}
		});
	}


	public void showDotmore(){
		if (postUserSeqId.equals(Constant.userInfo.getUserSeqId())) {
			ll_dotmore.setVisibility(View.VISIBLE);
		}
	}
	
	private void deletePost() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "deletePostById");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		busiParams.put("postId", postId);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				ToastUtils.showMessage(context, errorObj.getErrorMessage());
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					// ToastUtils.showMessage(context, "删除成功");
					setDelete();
					((Activity) context).onKeyDown(KeyEvent.KEYCODE_BACK,
							new KeyEvent(KeyEvent.ACTION_DOWN,
									KeyEvent.KEYCODE_BACK));
					((Activity) context).finish();
				}
			}

			@Override
			public void start() {

			}

			@Override
			public void loading(long total, long current, boolean isUploading) {

			}

			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				ToastUtils.showMessage(context, jobj.getString("ErrorMessage"));
			}
		});
	}

	public void fillData(String postId,String postUserSeqId){
		this.postId=postId;
		this.postUserSeqId=postUserSeqId;
	}

	public void setDelete() {
		if (context instanceof CaseDetailActivity) {
			((CaseDetailActivity) context).setDelete(true);
		}
		if (context instanceof PostDetailActivity) {
			((PostDetailActivity) context).setDelete(true);
		}
		if (context instanceof PostDetailFowardActivity) {
			((PostDetailFowardActivity) context).setDelete(true);
		}
		if (context instanceof PostDetailFowardActivity2) {
			((PostDetailFowardActivity2) context).setDelete(true);
		}
	}
}
