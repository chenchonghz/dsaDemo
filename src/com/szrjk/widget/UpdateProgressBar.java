package com.szrjk.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class UpdateProgressBar extends ProgressBar
{
	private String tvProgress;
	private Paint mPaint;// 画笔

	public UpdateProgressBar(Context context)
	{
		super(context);
		initPaint();
	}

	public UpdateProgressBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initPaint();
	}

	public UpdateProgressBar(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initPaint();
	}

	@Override
	public synchronized void setProgress(int progress)
	{
		super.setProgress(progress);
		setTextProgress(progress);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		Rect rect = new Rect();
		this.mPaint.getTextBounds(this.tvProgress, 0, this.tvProgress.length(),
				rect);
		int x = (getWidth() / 2) - rect.centerX();
		int y = (getHeight() / 2) - rect.centerY();
		canvas.drawText(this.tvProgress, x, y, this.mPaint);
	}

	private void initPaint()
	{
		this.mPaint = new Paint();
		this.mPaint.setAntiAlias(true);
		this.mPaint.setColor(Color.DKGRAY);
	}

	private void setTextProgress(int progress)
	{
		int i = (int) ((progress * 1.0f / this.getMax()) * 100);
		this.tvProgress = String.valueOf(i) + "%";
	}

}
