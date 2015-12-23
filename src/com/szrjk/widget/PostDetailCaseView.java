package com.szrjk.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szrjk.adapter.IndexPhotoGridViewAdapter;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.IndexGalleryActivity;
import com.szrjk.dhome.R;
import com.szrjk.dhome.SelfActivity;
import com.szrjk.entity.ICallback;
import com.szrjk.entity.IPhotoClickOper;
import com.szrjk.entity.IPullPostListCallback;
import com.szrjk.entity.PostAbstractList;
import com.szrjk.simplifyspan.SimplifySpanBuild;
import com.szrjk.util.UserNameJumpUtil;

/**
 * 病例分享,疑难杂症 的title,content和gridview,的简单封装
 */
public class PostDetailCaseView extends RelativeLayout {

	private TextView tvDetailTitle;
	private TextView tvDetailedContent;
	private GridView gvDetailPic;

	public void contentClick(final ICallback iCallback) {
		tvDetailedContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				iCallback.doCallback(null);
			}
		});
	}

	/**
	 * 
	 * @param context
	 * @param title
	 * @param content
	 * @param gridImgArrStr
	 *            图片的列表str http://xxxxxx|http://sldjofdasjf|....
	 */
	public PostDetailCaseView(Context context, String title,
			CharSequence content, String gridImgArrStr) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		boolean hideTitle = false;
		if (title == null) {
			return;
		} else if (title.equals("")) {
			// 如果为空串,隐藏title
			inflater.inflate(R.layout.post_detail_case_view, this);
			hideTitle = true;
		} else {
			inflater.inflate(R.layout.post_detail_case_view, this);
		}

		tvDetailTitle = (TextView) findViewById(R.id.tv_post_detailed_case);
		if (hideTitle) {
			// 隐藏title
			tvDetailTitle.setVisibility(View.GONE);
		}
		tvDetailedContent = (TextView) findViewById(R.id.tv_post_detailed_casecontent);
		gvDetailPic = (GridView) findViewById(R.id.gv_post_detailed_case);

		tvDetailTitle.setText(title);

		SharedPreferences mySharedPreferences = context.getSharedPreferences(
				"text_size", Context.MODE_PRIVATE);
		// 用putString的方法保存数据
		String size = mySharedPreferences.getString("size", "");
		float textSize = tvDetailedContent.getTextSize();
		double weight = 1;
		if (size == null) {
		} else if (size.equals("small")) {
		} else if (size.equals("middle")) {
			weight = 1.5;
		} else if (size.equals("large")) {
			weight = 2.0;
		}
		textSize *= weight;
		tvDetailedContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

		tvDetailedContent.setText(content);

		// 设置该句使文本的超连接起作用
		tvDetailedContent.setMovementMethod(LinkMovementMethod.getInstance());

		if (gridImgArrStr != null && !gridImgArrStr.equals("")) {
			final String[] gridImgArr = gridImgArrStr.split("\\|");
			if (gridImgArr != null) {
				BaseActivity b = (BaseActivity) context;
				int screenWidth = getWindowsWidth(b);
				if (gridImgArr.length < 3) {
					gvDetailPic.setNumColumns(2);
				} else {
					gvDetailPic.setNumColumns(3);
				}
				gvDetailPic.setAdapter(new IndexPhotoGridViewAdapter(context,
						gridImgArr, screenWidth, new IPhotoClickOper() {

							@Override
							public void clickoper(int position, Context context) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(context,
										IndexGalleryActivity.class);
								intent.putExtra("position", position);
								intent.putExtra("imgs", gridImgArr);
								intent.putExtra("title", (position + 1) + "/"
										+ gridImgArr.length);
								intent.putExtra("needOper", false);
								intent.putExtra("contextText", "");
								context.startActivity(intent);
							}
						}));
			} else {
				gvDetailPic.setVisibility(View.GONE);
			}
		} else {
			gvDetailPic.setVisibility(View.GONE);
		}

	}
	/**
	 * @param context
	 * @param title
	 * @param content
	 * @param gridImgArrStr
	 *            图片的列表str http://xxxxxx|http://sldjofdasjf|....
	 */
	public PostDetailCaseView(final Context context, String title,PostAbstractList postAbstractInfo,
			CharSequence content, String gridImgArrStr) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		boolean hideTitle = false;
		if (title == null) {
			return;
		} else if (title.equals("")) {
			// 如果为空串,隐藏title
			inflater.inflate(R.layout.post_detail_case_view, this);
			hideTitle = true;
		} else {
			inflater.inflate(R.layout.post_detail_case_view, this);
		}
		
		tvDetailTitle = (TextView) findViewById(R.id.tv_post_detailed_case);
		if (hideTitle) {
			// 隐藏title
			tvDetailTitle.setVisibility(View.GONE);
		}
		tvDetailedContent = (TextView) findViewById(R.id.tv_post_detailed_casecontent);
		gvDetailPic = (GridView) findViewById(R.id.gv_post_detailed_case);
		
		tvDetailTitle.setText(title);
		
		SharedPreferences mySharedPreferences = context.getSharedPreferences(
				"text_size", Context.MODE_PRIVATE);
		// 用putString的方法保存数据
		String size = mySharedPreferences.getString("size", "");
		float textSize = tvDetailedContent.getTextSize();
		double weight = 1;
		if (size == null) {
		} else if (size.equals("small")) {
		} else if (size.equals("middle")) {
			weight = 1.5;
		} else if (size.equals("large")) {
			weight = 2.0;
		}
		textSize *= weight;
		tvDetailedContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		
		String postUserName = postAbstractInfo.getUserCard().getUserName();
		String userLevel = postAbstractInfo.getUserCard().getUserLevel();
		String userType = postAbstractInfo.getUserCard().getUserType();
		String userSeqId = postAbstractInfo.getPostAbstract().getUserSeqId();
		
		SimplifySpanBuild simplifySpanBuild = UserNameJumpUtil.getContentText(context, tvDetailedContent, (String)content, postUserName, userSeqId, userLevel, userType, postAbstractInfo.getPostAbstract(), 0, new IPullPostListCallback() {
			@Override
			public void skipToSelfFragment() {
				Intent intent=new Intent(context, SelfActivity.class);
				context.startActivity(intent);
			}
		});
		tvDetailedContent.setText(simplifySpanBuild.build());
//		tvDetailedContent.setText(postUserName+":"+content);
		
		// 设置该句使文本的超连接起作用
//		tvDetailedContent.setMovementMethod(LinkMovementMethod.getInstance());
		
		if (gridImgArrStr != null && !gridImgArrStr.equals("")) {
			final String[] gridImgArr = gridImgArrStr.split("\\|");
			if (gridImgArr != null) {
				BaseActivity b = (BaseActivity) context;
				int screenWidth = getWindowsWidth(b);
				if (gridImgArr.length < 3) {
					gvDetailPic.setNumColumns(2);
				} else {
					gvDetailPic.setNumColumns(3);
				}
				gvDetailPic.setAdapter(new IndexPhotoGridViewAdapter(context,
						gridImgArr, screenWidth, new IPhotoClickOper() {
					
					@Override
					public void clickoper(int position, Context context) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context,
								IndexGalleryActivity.class);
						intent.putExtra("position", position);
						intent.putExtra("imgs", gridImgArr);
						intent.putExtra("title", (position + 1) + "/"
								+ gridImgArr.length);
						intent.putExtra("needOper", false);
						intent.putExtra("contextText", "");
						context.startActivity(intent);
					}
				}));
			} else {
				gvDetailPic.setVisibility(View.GONE);
			}
		} else {
			gvDetailPic.setVisibility(View.GONE);
		}
		
	}

	private int getWindowsWidth(BaseActivity b) {
		// TODO Auto-generated method stub
		DisplayMetrics dm = new DisplayMetrics();
		b.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

}
