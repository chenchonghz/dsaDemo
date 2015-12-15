package com.szrjk.util.corpimageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.szrjk.config.Constant;

public class FloatDrawable extends Drawable {

	private Context mContext;
	private int offset = 50;
	private Paint mLinePaint = new Paint();
	private Paint mLinePaint2 = new Paint();
	//�����Ļ���
	private int sw = Constant.screenWidth;
	
	{
		Log.i("sw", ""+sw);
		
		mLinePaint.setARGB(200, 50, 50, 50);
		mLinePaint.setStrokeWidth(1F);
		mLinePaint.setStyle(Paint.Style.STROKE);
		mLinePaint.setAntiAlias(true);
		mLinePaint.setColor(Color.WHITE);
		//
		mLinePaint2.setARGB(200, 50, 50, 50);
		mLinePaint2.setStrokeWidth(7F);
		mLinePaint2.setStyle(Paint.Style.STROKE);
		mLinePaint2.setAntiAlias(true);
		mLinePaint2.setColor(Color.WHITE);
	}

	public FloatDrawable(Context context) {
		super();
		this.mContext = context;

	}

	public int getBorderWidth() {
		return dipTopx(mContext, offset);//���dip���������ֵ���������õ�
	}

	public int getBorderHeight() {
		return dipTopx(mContext, offset);
	}

	@Override
	public void draw(Canvas canvas) {

		int left = getBounds().left;
		int top = getBounds().top;
		int right = getBounds().right;
		int bottom = getBounds().bottom;

		Rect mRect = new Rect(
				left + dipTopx(mContext, offset) / 2, 
				top  + dipTopx(mContext, offset) / 2, 
				right  - dipTopx(mContext, offset) / 2, 
				bottom - dipTopx(mContext, offset) / 2);
		//��Ĭ�ϵ�ѡ���
		canvas.drawRect(mRect, mLinePaint);
		//���ĸ��ǵ��ĸ��ֹսǡ�Ҳ���ǰ�������
		canvas.drawLine((
				left + dipTopx(mContext, offset) / 2 - 3.5f), 
				top  + dipTopx(mContext, offset) / 2,
				left + dipTopx(mContext, offset) - 8f,
				top + dipTopx(mContext, offset) / 2, mLinePaint2);


		canvas.drawLine(left + dipTopx(mContext, offset) / 2,
				top + dipTopx(mContext, offset) / 2,
				left + dipTopx(mContext, offset) / 2,
				top + dipTopx(mContext, offset) / 2 + 30, mLinePaint2);


		canvas.drawLine(right - dipTopx(mContext, offset) + 8f,
				top + dipTopx(mContext, offset) / 2,
				right - dipTopx(mContext, offset) / 2,
				top + dipTopx(mContext, offset) / 2, mLinePaint2);


		canvas.drawLine(right - dipTopx(mContext, offset) / 2,
				top + dipTopx(mContext, offset) / 2 - 3.5f,
				right - dipTopx(mContext, offset) / 2,
				top + dipTopx(mContext, offset) / 2 + 30, mLinePaint2);


		canvas.drawLine((left + dipTopx(mContext, offset) / 2 - 3.5f), bottom
				- dipTopx(mContext, offset) / 2,
				left + dipTopx(mContext, offset) - 8f,
				bottom - dipTopx(mContext, offset) / 2, mLinePaint2);


		canvas.drawLine((left + dipTopx(mContext, offset) / 2), bottom
				- dipTopx(mContext, offset) / 2,
				(left + dipTopx(mContext, offset) / 2),
				bottom - dipTopx(mContext, offset) / 2 - 30f, mLinePaint2);


		canvas.drawLine((right - dipTopx(mContext, offset) + 8f), bottom
				- dipTopx(mContext, offset) / 2,
				right - dipTopx(mContext, offset) / 2,
				bottom - dipTopx(mContext, offset) / 2, mLinePaint2);


		canvas.drawLine((right - dipTopx(mContext, offset) / 2), bottom
				- dipTopx(mContext, offset) / 2 - 30f,
				right - dipTopx(mContext, offset) / 2,
				bottom - dipTopx(mContext, offset) / 2 + 3.5f, mLinePaint2);

	}

	@Override
	public void setBounds(Rect bounds) {
		Log.i("bounds.left", ""+bounds.left);
		Log.i("dipTopx(mContext, offset) / 2", ""+dipTopx(mContext, offset) / 2);

		int templeft = bounds.left;
		if (bounds.left < 0) {
			bounds.left = 0;
			int newright = bounds.right - templeft;
			bounds.right = newright;
		}
		int tempright = bounds.right;
		if (bounds.right > sw) {
			bounds.right = sw;
			//������
			int gap = tempright - sw;
			//��߽߱��� + ���
			int temleft = bounds.left - gap;
			//�����߱߾಻���ڽ�����
			if (temleft>=0) {
				bounds.left = temleft;
			}else{
				//���+ ��೬������Ļ��ߣ���ֱ�Ӹ�ֵ0.����Ļ���
				bounds.left =0;
			}
		}
		
		if (Math.abs(bounds.left - bounds.right) <= 150) {
//			Log.i("", "������");
//			return;
		}
		super.setBounds(new 
				Rect(

						bounds.left - dipTopx(mContext, offset) / 2,
						bounds.top - dipTopx(mContext, offset) / 2,
						bounds.right   + dipTopx(mContext, offset) / 2, 
						bounds.bottom + dipTopx(mContext, offset) / 2));
	}

	@Override
	public void setAlpha(int alpha) {

	}

	@Override
	public void setColorFilter(ColorFilter cf) {

	}

	@Override
	public int getOpacity() {
		return 0;
	}

	public int dipTopx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
