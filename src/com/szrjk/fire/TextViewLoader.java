package com.szrjk.fire;

import android.widget.TextView;

/**
 * TextView Text value Loader
 * 
 */
public class TextViewLoader implements LazyLoader
{

	private final TextView mTextView;

	public TextViewLoader(TextView textView)
	{
		mTextView = textView;
	}

	@Override
	public Long loadInt()
	{
		return null;
	}

	@Override
	public Double loadFloat()
	{
		return null;
	}

	@Override
	public String loadString()
	{
		return String.valueOf(mTextView.getText());
	}
}
