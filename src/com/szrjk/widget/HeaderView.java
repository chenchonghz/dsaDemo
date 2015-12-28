package com.szrjk.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szrjk.dhome.R;

public class HeaderView extends RelativeLayout
{
	//headerview标题文字
	private TextView htext;
	//headerview左侧返回键布局对象
	private LinearLayout lly_hv;
	private ImageView btnBack;
	//headerview右侧图标按钮布局对象
	private LinearLayout lly_image;
	private ImageView btn_image;
	//headerview右侧文字按钮对象
	private TextView btn_text;
	//headerview左侧文字按钮对象
	private TextView btn_back;
	
	
	public ImageView getBtnBack()
	{
		return btnBack;
	}

	public HeaderView(Context context)
	{
		super(context);
	}

	public HeaderView(final Context context, AttributeSet attrs)
	{
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.headerview, this);
		htext = (TextView) findViewById(R.id.headerview_text_id);
		btnBack = (ImageView) findViewById(R.id.btn_back);
		lly_hv =(LinearLayout) findViewById(R.id.lly_hv);
		lly_image = (LinearLayout) findViewById(R.id.lly_Image);
		btn_image = (ImageView) findViewById(R.id.btn_Image);
		btn_text = (TextView) findViewById(R.id.btn_text);
		btn_back = (TextView) findViewById(R.id.btn_tv_back);
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.HeaderView);
		String htextstr = a.getString(R.styleable.HeaderView_htextstr);
		htext.setText(htextstr);

		lly_hv.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				((Activity) context).finish();
			}
		});
	}
	//获得headerview的标题文字对象
	public TextView getHtext()
	{
		return htext;
	}
	//设置headerview的标题文字
	public TextView setHtext(String s){
		htext.setText(s);
		return htext;
	}
	//获得headerview左侧返回键布局对象
	public LinearLayout getLLy(){
		return lly_hv;
	}
	//设置headerview左侧返回键布局对象的按键逻辑
	public void setBtnBackOnClick(OnClickListener onClickListener){
		lly_hv.setOnClickListener(onClickListener);
	}
	//显示headerview右侧图标按钮布局对象，并设置其显示图片与点击逻辑
	public void showImageLLy(int resId,OnClickListener onClickListener){
		lly_image.setVisibility(View.VISIBLE);
		btn_image.setImageResource(resId);
		lly_image.setOnClickListener(onClickListener);
	}
	//获得headerview左侧图标按钮布局对象
	public LinearLayout getImageBtn(){
		return lly_image;
	}
	//获得headerview左侧文字按钮布局对象
	public TextView getTextBtn(){
		return btn_text;
	}
	//获得headerview左侧文字按钮布局对象
		public TextView getBackBtn(){
			return btn_back;
		}
	//显示headerview右侧文字按钮对象，并设置其显示文字与点击逻辑
	public void showTextBtn(String text,OnClickListener onClickListener){
		btn_text.setVisibility(View.VISIBLE);
		btn_text.setText(text);
		btn_text.setOnClickListener(onClickListener);
	}
	//显示headerview左侧文字按钮对象，并设置其显示文字与点击逻辑，并且隐藏原图标按钮
	public void showBackBtn(String text,OnClickListener onClickListener){
		lly_hv.setVisibility(View.GONE);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setText(text);
		btn_back.setOnClickListener(onClickListener);
	}
}
