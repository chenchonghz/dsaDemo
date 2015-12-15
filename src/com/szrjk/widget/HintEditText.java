package com.szrjk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szrjk.dhome.R;

public class HintEditText extends LinearLayout implements TextWatcher
{
	private EditText et_title;
	private TextView tv_title;
	private int maxLength = 0;
	private LinearLayout lly_content;

	public HintEditText(Context context)
	{
		super(context);
		initUI(context);
	}

	public HintEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.HintEditText);
		maxLength = typedArray.getInt(R.styleable.HintEditText_maxLength, 0);
		initUI(context);
		tv_title.setText("还可以输入" + maxLength + "字");
		et_title.setFilters(new InputFilter[]
		{
			new InputFilter.LengthFilter(maxLength)
		});
		et_title.addTextChangedListener(this);
	}

	public HintEditText(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initUI(context);
	}

	public void initUI(Context context)
	{

		lly_content = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.hint_edit_text, this, true);

		et_title = (EditText) lly_content.findViewById(R.id.et_title);

		tv_title = (TextView) lly_content.findViewById(R.id.tv_title);

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		tv_title.setHint("还可以输入" + (maxLength - s.toString().length()) + "字");
	}

	@Override
	public void afterTextChanged(Editable s)
	{

	}

}
