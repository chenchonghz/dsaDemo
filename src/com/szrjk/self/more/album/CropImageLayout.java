package com.szrjk.self.more.album;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

public class CropImageLayout extends RelativeLayout {

	private ZoomCropView zoomCropView;
	private CropImageBorderView mClipImageView;

	/**
	 * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
	 */
	private int mHorizontalPadding = 20;

	public CropImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		zoomCropView = new ZoomCropView(context);
		mClipImageView = new CropImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		this.addView(zoomCropView, lp);
		this.addView(mClipImageView, lp);

		// 计算padding的px
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		zoomCropView.setClipImageBorderView(mClipImageView);
		mClipImageView.setmHorizontalPadding(mHorizontalPadding);
	}

	public ZoomCropView getZoomCropView() {
		return zoomCropView;
	}

	public void setZoomCropView(ZoomCropView zoomCropView) {
		this.zoomCropView = zoomCropView;
	}

	public CropImageBorderView getmClipImageView() {
		return mClipImageView;
	}

	public void setmClipImageView(CropImageBorderView mClipImageView) {
		this.mClipImageView = mClipImageView;
	}

	public int getmHorizontalPadding() {
		return mHorizontalPadding;
	}

	/**
	 * 对外公布设置边距的方法,单位为dp
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
	}

	/**
	 * 裁切图片
	 * 
	 * @return
	 */

	public Bitmap clip() {
		return zoomCropView.clip();
	}

}
