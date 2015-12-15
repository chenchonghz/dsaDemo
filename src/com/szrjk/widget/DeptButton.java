package com.szrjk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.Checkable;

public class DeptButton extends Button implements Checkable
{
	private boolean mChecked;
	private static final int[] CHECKED_STATE_LIST = new int[]
	{
		android.R.attr.state_checked
	};

	private boolean mBroadcasting;

	private OnCheckedChangeListener mOnCheckedChangeListener;
	private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

	public DeptButton(Context context)
	{
		super(context);
	}

	public DeptButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public DeptButton(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected int[] onCreateDrawableState(int extraSpace)
	{
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if (mChecked)
		{
			mergeDrawableStates(drawableState, CHECKED_STATE_LIST);
		}
		return drawableState;
	}

	@Override
	public void setChecked(boolean checked)
	{
		if (mChecked != checked)
		{
			mChecked = checked;
			refreshDrawableState();

			if (mBroadcasting) { return; }

			mBroadcasting = true;
			if (mOnCheckedChangeListener != null)
			{
				mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
			}
			if (mOnCheckedChangeWidgetListener != null)
			{
				mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked);
			}
		}
	}

	@Override
	public boolean isChecked()
	{
		return mChecked;
	}

	@Override
	public void toggle()
	{
		setChecked(!mChecked);
	}

	@Override
	public boolean performClick()
	{
		toggle();
		return super.performClick();
	}

	void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener)
	{
		mOnCheckedChangeWidgetListener = listener;
	}

	public interface OnCheckedChangeListener
	{
		void onCheckedChanged(DeptButton buttonView, boolean isChecked);
	}

}
