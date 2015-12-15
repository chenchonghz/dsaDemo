package com.szrjk.util.corpimageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.szrjk.config.Constant;
import com.szrjk.dhome.MainActivity;

public class CropImageView extends View {

	private static int sw = Constant.screenWidth;
	

	// ��touch��Ҫ�õ��ĵ㣬
	private float mX_1 = 0;
	private float mY_1 = 0;
	// �����¼��ж�
	private final int STATUS_SINGLE = 1;
	private final int STATUS_MULTI_START = 2;
	private final int STATUS_MULTI_TOUCHING = 3;
	// ��ǰ״̬
	private int mStatus = STATUS_SINGLE;
	// Ĭ�ϲü��Ŀ��
	private int cropWidth;
	private int cropHeight;
	// ����Drawable���ĸ���
	private final int EDGE_LT = 1;
	private final int EDGE_RT = 2;
	private final int EDGE_LB = 3;
	private final int EDGE_RB = 4;
	private final int EDGE_MOVE_IN = 5;
	private final int EDGE_MOVE_OUT = 6;
	private final int EDGE_NONE = 7;

	public int currentEdge = EDGE_NONE;

	protected float oriRationWH = 0;
	protected final float maxZoomOut = 5.0f;
	protected final float minZoomIn = 0.333333f;

	protected Drawable mDrawable;
	protected FloatDrawable mFloatDrawable;

	protected Rect mDrawableSrc = new Rect();// ͼƬRect�任ʱ��Rect
	protected Rect mDrawableDst = new Rect();// ͼƬRect
	protected Rect mDrawableFloat = new Rect();// �����Rect
	protected boolean isFrist = true;
	private boolean isTouchInSquare = true;

	protected Context mContext;
	private Rect tempDrawableFloat;

	public CropImageView(Context context) {
		super(context);
		init(context);
	}

	public CropImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i("crop", ""+sw);
		init(context);
	}

	public CropImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.i("crop", ""+sw);
		init(context);

	}

	@SuppressLint("NewApi")
	private void init(Context context) {
		this.mContext = context;
		try {
			if (android.os.Build.VERSION.SDK_INT >= 11) {
				this.setLayerType(LAYER_TYPE_SOFTWARE, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mFloatDrawable = new FloatDrawable(context);
	}

	public void setDrawable(Drawable mDrawable, int cropWidth, int cropHeight) {
		this.mDrawable = mDrawable;
		this.cropWidth = cropWidth;
		this.cropHeight = cropHeight;
		this.isFrist = true;
		invalidate();
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getPointerCount() > 1) {
			if (mStatus == STATUS_SINGLE) {
				mStatus = STATUS_MULTI_START;
			} else if (mStatus == STATUS_MULTI_START) {
				mStatus = STATUS_MULTI_TOUCHING;
			}
		} else {
			if (mStatus == STATUS_MULTI_START
					|| mStatus == STATUS_MULTI_TOUCHING) {
				mX_1 = event.getX();
				mY_1 = event.getY();
			}

			mStatus = STATUS_SINGLE;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mX_1 = event.getX();
			mY_1 = event.getY();
			currentEdge = getTouch((int) mX_1, (int) mY_1);
			isTouchInSquare = mDrawableFloat.contains((int) event.getX(),
					(int) event.getY());

			break;

		case MotionEvent.ACTION_UP:
			checkBounds();
			break;

		case MotionEvent.ACTION_POINTER_UP:
			currentEdge = EDGE_NONE;
			break;

		case MotionEvent.ACTION_MOVE:
			if (mStatus == STATUS_MULTI_TOUCHING) {

			} else if (mStatus == STATUS_SINGLE) {
				int dx = (int) (event.getX() - mX_1);
				//				Log.i("dx", ""+dx);
				int dy = (int) (event.getY() - mY_1);
				//				Log.i("dy", ""+dy);

				mX_1 = event.getX();
				mY_1 = event.getY();
				// ����õ�����һ���ǣ����ұ任Rect
				if (!(dx == 0 && dy == 0)) {
					switch (currentEdge) {
					case EDGE_LT://����
						Log.i("le", ""+mDrawableFloat.left);
						Log.i("ri", mDrawableFloat.right+"");
						
						//�ұ�����Ļ���720
						//��߲�����Ļ���0
						//��С��һ��ֵ��ʱ��ֹͣ��С
						if (mDrawableFloat.right == sw) {
							if (mDrawableFloat.left != 0) {
								//����������ֵ
								
								mDrawableFloat.set(
										mDrawableFloat.left + dx,

										mDrawableFloat.top + dx,
										mDrawableFloat.right - dx,
										mDrawableFloat.bottom -dx
										);
							}else{

								mDrawableFloat.set(
										mDrawableFloat.left + dx,

										mDrawableFloat.top ,
										mDrawableFloat.right - dx,
										mDrawableFloat.bottom 
										);
							}
						}else{

							mDrawableFloat.set(
									mDrawableFloat.left + dx,

									mDrawableFloat.top + dx,
									mDrawableFloat.right - dx,
									mDrawableFloat.bottom -dx
									);
						
						}
//						if (Math.abs(mDrawableFloat.left - mDrawableFloat.right) <= 150) {
							
//						}
						break;

					case EDGE_RT://���Ͻ�
						//�ұ�720sw��������߻�����0��ʱ��.��������
						if (mDrawableFloat.right == sw ) {
							if (mDrawableFloat.right ==sw && mDrawableFloat.left !=0) {
								mDrawableFloat.set(
										mDrawableFloat.left -dx,
										mDrawableFloat.top - dx,
										mDrawableFloat.right  + dx, 
										mDrawableFloat.bottom +dx);
							}else{


								mDrawableFloat.set(
										mDrawableFloat.left -dx,
										mDrawableFloat.top ,
										mDrawableFloat.right  + dx, 
										mDrawableFloat.bottom );
							}
						}else{

							mDrawableFloat.set(
									mDrawableFloat.left -dx,
									mDrawableFloat.top - dx,
									mDrawableFloat.right  + dx, 
									mDrawableFloat.bottom +dx);
						}

						break;

					case EDGE_LB://����
						if (mDrawableFloat.right == sw) {
							if (mDrawableFloat.left != 0) {
								mDrawableFloat.set(
										mDrawableFloat.left + dx,
										mDrawableFloat.top +dx,
										mDrawableFloat.right -dx,
										mDrawableFloat.bottom - dx
										);
							}else{

								mDrawableFloat.set(
										mDrawableFloat.left + dx,
										mDrawableFloat.top ,
										mDrawableFloat.right -dx,
										mDrawableFloat.bottom 
										);
							}
						}else{

							mDrawableFloat.set(
									mDrawableFloat.left + dx,
									mDrawableFloat.top +dx,
									mDrawableFloat.right -dx,
									mDrawableFloat.bottom - dx
									);
						}
						break;

					case EDGE_RB://���½�
						//ͬ������
						if (mDrawableFloat.right == sw) {
							if (mDrawableFloat.right == sw && mDrawableFloat.left !=0 ) {
								mDrawableFloat.set(
										mDrawableFloat.left -dx,
										mDrawableFloat.top -dx, 
										mDrawableFloat.right + dx,
										mDrawableFloat.bottom + dx
										);
							}else{

								mDrawableFloat.set(
										mDrawableFloat.left -dx,
										mDrawableFloat.top , 
										mDrawableFloat.right + dx,
										mDrawableFloat.bottom 
										);
							}
						}else{

							mDrawableFloat.set(
									mDrawableFloat.left -dx,
									mDrawableFloat.top -dx, 
									mDrawableFloat.right + dx,
									mDrawableFloat.bottom + dx
									);
						}
						
						break;

					case EDGE_MOVE_IN:
						if (isTouchInSquare) {
							mDrawableFloat.offset((int) dx, (int) dy);
						}
						Log.e("mDrawableFloat", ""+mDrawableFloat.bottom);
						Log.e("mDrawableFloat", ""+mDrawableFloat.top);
						break;

					case EDGE_MOVE_OUT:
						//���ﲻ֪��Ϊʲôû�д���
						break;
					}
//					Log.i("zzzzzzzz", ""+mDrawableFloat.left);
//					Log.i("yyyyyyy", ""+mDrawableFloat.right);
//					if (Math.abs(mDrawableFloat.left - mDrawableFloat.right) <= 200) {
//						System.out.println("200 ������");
//						mDrawableFloat.left = 
//						mDrawableFloat.set(
//								mDrawableFloat.left ,
//								mDrawableFloat.top , 
//								mDrawableFloat.right ,
//								mDrawableFloat.bottom 
//								);
//					}
					mDrawableFloat.sort();
					invalidate();
				}
			}
			break;
//		case MotionEvent.ACTION_CANCEL:
//			checkBounds();
////			invalidate();
//			break;
//		case MotionEvent.ACTION_OUTSIDE:
//			checkBounds();
//			break;
		}

		return true;
	}

	// ��ݳ��������ж��Ǵ�����Rect��һ����
	public int getTouch(int eventX, int eventY) {
		if (mFloatDrawable.getBounds().left <= eventX
				&& eventX < (mFloatDrawable.getBounds().left + mFloatDrawable
						.getBorderWidth())
						&& mFloatDrawable.getBounds().top <= eventY
						&& eventY < (mFloatDrawable.getBounds().top + mFloatDrawable
								.getBorderHeight())) {
			return EDGE_LT;


		} else if ((mFloatDrawable.getBounds().right - mFloatDrawable
				.getBorderWidth()) <= eventX
				&& eventX < mFloatDrawable.getBounds().right
				&& mFloatDrawable.getBounds().top <= eventY
				&& eventY < (mFloatDrawable.getBounds().top + mFloatDrawable
						.getBorderHeight())) {
			return EDGE_RT;


		} else if (mFloatDrawable.getBounds().left <= eventX
				&& eventX < (mFloatDrawable.getBounds().left + mFloatDrawable
						.getBorderWidth())
						&& (mFloatDrawable.getBounds().bottom - mFloatDrawable
								.getBorderHeight()) <= eventY
								&& eventY < mFloatDrawable.getBounds().bottom) {
			return EDGE_LB;


		} else if ((mFloatDrawable.getBounds().right - mFloatDrawable
				.getBorderWidth()) <= eventX
				&& eventX < mFloatDrawable.getBounds().right
				&& (mFloatDrawable.getBounds().bottom - mFloatDrawable
						.getBorderHeight()) <= eventY
						&& eventY < mFloatDrawable.getBounds().bottom) {
			return EDGE_RB;


		} else if (mFloatDrawable.getBounds().contains(eventX, eventY)) {
			return EDGE_MOVE_IN;
		}
		return EDGE_MOVE_OUT;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (mDrawable == null) {
			return;
		}

		if (mDrawable.getIntrinsicWidth() == 0
				|| mDrawable.getIntrinsicHeight() == 0) {
			return;
		}

		configureBounds();
		// �ڻ����ϻ�ͼƬ
		mDrawable.draw(canvas);
		canvas.save();

		// �ڻ����ϻ�����FloatDrawable,Region.Op.DIFFERENCE�Ǳ�ʾRect�����Ĳ���
		canvas.clipRect(mDrawableFloat, Region.Op.DIFFERENCE);
		// �ڽ����Ĳ����ϻ��ϻ�ɫ�������
		canvas.drawColor(Color.parseColor("#a0000000"));
		canvas.restore();
		// ������
		mFloatDrawable.draw(canvas);
	}

	protected void configureBounds() {
		// configureBounds��onDraw�����е���
		// isFirst��Ŀ���������mDrawableSrc��mDrawableFloatֻ��ʼ��һ�Σ�
		// ֮��ı仯�Ǹ��touch�¼����仯�ģ�����ÿ��ִ�����¶�mDrawableSrc��mDrawableFloat��������
		if (isFrist) {
			oriRationWH = ((float) mDrawable.getIntrinsicWidth()) / ((float) mDrawable.getIntrinsicHeight());

			Log.i("Width()", mDrawable.getIntrinsicWidth()+"");

			Log.i("Height()", mDrawable.getIntrinsicHeight()+"");

			System.out.println(oriRationWH);

			final float scale = mContext.getResources().getDisplayMetrics().density;
			int w = Math.min(getWidth(), (int) (mDrawable.getIntrinsicWidth()
					* scale + 0.5f));
			int h = (int) (w / oriRationWH);

			int left = (getWidth() - w) / 2;
			int top = (getHeight() - h) / 2;
			int right = left + w;
			int bottom = top + h;

			mDrawableSrc.set(left, top, right, bottom);
			mDrawableDst.set(mDrawableSrc);

			int floatWidth = dipTopx(mContext, cropWidth);
			int floatHeight = dipTopx(mContext, cropHeight);

			if (floatWidth > getWidth()) {
				floatWidth = getWidth();
				floatHeight = cropHeight * floatWidth / cropWidth;
			}

			if (floatHeight > getHeight()) {
				floatHeight = getHeight();
				floatWidth = cropWidth * floatHeight / cropHeight;
			}

			int floatLeft = (getWidth() - floatWidth) / 2;
			int floatTop = (getHeight() - floatHeight) / 2;
			//	   mDrawableFloat.set(floatLeft, floatTop, floatLeft + floatWidth, floatTop + floatHeight);
			mDrawableFloat.set(floatLeft, floatTop, floatLeft + floatWidth,   floatTop + floatHeight);

			isFrist = false;
		}

		mDrawable.setBounds(mDrawableDst);
		mFloatDrawable.setBounds(mDrawableFloat);
	}

	// ��up�¼��е����˸÷�����Ŀ���Ǽ���Ƿ�Ѹ����ϳ�����Ļ
	protected void checkBounds() {
		int newLeft = mDrawableFloat.left;
		int newTop = mDrawableFloat.top;

		Log.i("newLeft", ""+newLeft);
		Log.i("newTop", ""+newTop);
		boolean isChange = false;
		if (mDrawableFloat.left < getLeft()) {
			newLeft = getLeft();
			isChange = true;
		}

		if (mDrawableFloat.top < getTop()) {
			newTop = getTop();
			isChange = true;
		}

		if (mDrawableFloat.right > getRight()) {
			newLeft = getRight() - mDrawableFloat.width();
			isChange = true;
		}

		if (mDrawableFloat.bottom > getBottom()) {
			newTop = getBottom() - mDrawableFloat.height();
			isChange = true;
		}

		//���ü��߶�
		if (mDrawableFloat.height() > sw) {
			System.out.println("�߶ȴ�����Ļ���");
			int tt = mDrawableFloat.top;
			int tb = mDrawableFloat.bottom;
			Log.i("th", ""+tt);
			Log.i("tw", ""+tb);
			int xx = ((tb - tt) - sw) / 2;
			mDrawableFloat.top += xx;
			mDrawableFloat.bottom -= xx;
			isChange = true;
		} 
		
		
		if (mDrawableFloat.width() != mDrawableFloat.height()) {
			System.out.println("�����");
			int wd =( mDrawableFloat.height() - mDrawableFloat.width()) / 2;
			mDrawableFloat.left -= wd;
			mDrawableFloat.right += wd;
			isChange = true;
		}
		//�������ֵ
		if (mDrawableFloat.width() <= 50 ) {
			
		}
		
		System.out.println("�����Ŀ�ȡ�---"+ mDrawableFloat.width());
		System.out.println("�����ĸ߶ȡ�---"+ mDrawableFloat.height());
		mDrawableFloat.offsetTo(newLeft, newTop);
		if (isChange) {
			invalidate();
		}
		
	}

	// ����ͼƬ�Ĳü�����ν�Ĳü����Ǹ��Drawable���µ�����ڻ����ϴ���һ���µ�ͼƬ
	public Bitmap getCropImage() {
		if (mDrawableFloat.top < 0 || mDrawableFloat.bottom > CropImageActivity.cropViewHeight) {
			Log.i("", "�����߽�");
			Log.i("mDrawableFloat.top", ""+mDrawableFloat.top);
			Log.i("mDrawableFloat.bottom", ""+mDrawableFloat.bottom);
			Log.e("ch", ""+CropImageActivity.cropViewHeight);
		}else{
			
		
		Bitmap tmpBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.RGB_565);
		Canvas canvas = new Canvas(tmpBitmap);
		mDrawable.draw(canvas);

		Matrix matrix = new Matrix();
		float scale = (float) (mDrawableSrc.width()) 
				/ (float) (mDrawableDst.width());
		matrix.postScale(scale, scale);

		Bitmap ret = Bitmap.createBitmap(tmpBitmap, mDrawableFloat.left,
				mDrawableFloat.top, mDrawableFloat.width(),
				mDrawableFloat.height(), matrix, true);
		tmpBitmap.recycle();
		tmpBitmap = null;

		return ret;
		}
		return null;
	}

	public int dipTopx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
