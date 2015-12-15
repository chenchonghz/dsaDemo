package com.szrjk.util.clip;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class ClipImageBorderView extends View
{
	/**
	 * ˮƽ������View�ı߾�
	 */
	private int mHorizontalPadding ;
	/**
	 * �߿�Ŀ�� ��λdp
	 */
	private int mBorderWidth = 1;

	private Paint mPaint;

	public ClipImageBorderView(Context context)
	{
		this(context, null);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	
		mBorderWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
						.getDisplayMetrics());
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		// ���Ʊ߿�
		mPaint.setColor(Color.parseColor("#FFFFFF"));
		mPaint.setStrokeWidth(mBorderWidth);
		mPaint.setStyle(Style.STROKE);
		//这个方法是画圆的，裁剪圆形头像用
//		canvas.drawRect(150, 150, 150, 150, mPaint);
		Log.i("mHorizontalPadding", ""+mHorizontalPadding);
		canvas.drawRect(
				mHorizontalPadding,				//左
				(getHeight()-getWidth())/2 ,		//上
				getWidth()-mHorizontalPadding, 	//右
				(getHeight()-getWidth())/2 +getWidth() ,//下
				mPaint);
//		canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()- mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);
		//可以切换裁剪圆图
//		canvas.drawCircle( getWidth()/2, getHeight()/2, getWidth()/2-mHorizontalPadding, mPaint);

	}
	/**
	 * 
	 * @param mHorizontalPadding 水平距离屏幕的边距
	 */
	public void setHorizontalPadding(int mHorizontalPadding)
	{
		this.mHorizontalPadding = mHorizontalPadding;
		
	}

}
