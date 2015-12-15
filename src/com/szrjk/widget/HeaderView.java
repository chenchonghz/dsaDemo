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

	private TextView htext;
	private LinearLayout lly_hv;
	private ImageView btnBack;

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

	public TextView getHtext()
	{
		return htext;
	}
	public TextView setHtext(String s){
		htext.setText(s);
		return htext;
	}
	public LinearLayout getLLy(){
		return lly_hv;
	}
}
