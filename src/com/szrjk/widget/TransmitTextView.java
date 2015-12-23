package com.szrjk.widget;

import java.util.Arrays;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class TransmitTextView extends TextView{
	public boolean linkHit;

	public TransmitTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public TransmitTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TransmitTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean performClick() {
		// TODO Auto-generated method stub
		if(linkHit){
			return true;
		}
		return super.performClick();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		linkHit = false;
		return super.onTouchEvent(event);
	}
	
	
	
}
