package com.szrjk.widget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;

import com.szrjk.dhome.R;

public class BootstrapEditText extends EditText
{

	private boolean roundedCorners = false;

	public BootstrapEditText(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initialise(attrs);
	}

	public BootstrapEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialise(attrs);
	}

	public BootstrapEditText(Context context)
	{
		super(context);
		initialise(null);
	}

	public static final String BOOTSTRAP_EDIT_TEXT_DEFAULT = "default";
	public static final String BOOTSTRAP_EDIT_TEXT_SUCCESS = "success";
	public static final String BOOTSTRAP_EDIT_TEXT_WARNING = "warning";
	public static final String BOOTSTRAP_EDIT_TEXT_DANGER = "danger";

	private void initialise(AttributeSet attrs)
	{

		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.BootstrapEditText);

		float fontSize = 14.0f;
		String state = "default";
		String text = "";
		String hint = "";
		boolean enabled = true;

		if (a.getString(R.styleable.BootstrapEditText_android_textSize) != null)
		{

			String xmlProvidedSize = attrs.getAttributeValue(
					"http://schemas.android.com/apk/res/android", "textSize");
			final Pattern PATTERN_FONT_SIZE = Pattern
					.compile("([0-9]+[.]?[0-9]*)sp");
			Matcher m = PATTERN_FONT_SIZE.matcher(xmlProvidedSize);

			if (m.find())
			{
				if (m.groupCount() == 1)
				{
					fontSize = Float.valueOf(m.group(1));
				}
			}
		}

		if (a.getString(R.styleable.BootstrapEditText_be_roundedCorners) != null)
		{
			roundedCorners = a.getBoolean(
					R.styleable.BootstrapEditText_be_roundedCorners, false);
		}

		if (a.getString(R.styleable.BootstrapEditText_be_state) != null)
		{
			state = a.getString(R.styleable.BootstrapEditText_be_state);
		}

		if (a.getString(R.styleable.BootstrapEditText_android_text) != null)
		{
			text = a.getString(R.styleable.BootstrapEditText_android_text);
		}

		if (a.getString(R.styleable.BootstrapEditText_android_hint) != null)
		{
			hint = a.getString(R.styleable.BootstrapEditText_android_hint);
		}

		if (a.getString(R.styleable.BootstrapEditText_android_enabled) != null)
		{
			enabled = a.getBoolean(
					R.styleable.BootstrapEditText_android_enabled, true);
		}

		this.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
		this.setText(text);
		this.setHint(hint);
		this.setEnabled(enabled);

		if (enabled)
		{
			setBackgroundDrawable(state);

		}

		a.recycle();

	}

	private void setBackgroundDrawable(String state)
	{
		if (roundedCorners)
		{
			this.setBackgroundResource(R.drawable.edittext_background_rounded);
		}
		else
		{
			this.setBackgroundResource(R.drawable.edittext_background);
		}

		if (roundedCorners)
		{

			if (state.equals(BOOTSTRAP_EDIT_TEXT_SUCCESS))
			{
				this.setBackgroundResource(R.drawable.edittext_background_rounded_success);
			}
			else if (state.equals(BOOTSTRAP_EDIT_TEXT_WARNING))
			{
				this.setBackgroundResource(R.drawable.edittext_background_rounded_warning);
			}
			else if (state.equals(BOOTSTRAP_EDIT_TEXT_DANGER))
			{
				this.setBackgroundResource(R.drawable.edittext_background_rounded_danger);
			}

		}
		else
		{

			if (state.equals(BOOTSTRAP_EDIT_TEXT_SUCCESS))
			{
				this.setBackgroundResource(R.drawable.edittext_background_success);
			}
			else if (state.equals(BOOTSTRAP_EDIT_TEXT_WARNING))
			{
				this.setBackgroundResource(R.drawable.edittext_background_warning);
			}
			else if (state.equals(BOOTSTRAP_EDIT_TEXT_DANGER))
			{
				this.setBackgroundResource(R.drawable.edittext_background_danger);
			}

		}
	}

	public void setState(String state)
	{
		setBackgroundDrawable(state);
	}

	public void setSuccess()
	{
		setBackgroundDrawable(BOOTSTRAP_EDIT_TEXT_SUCCESS);
	}

	public void setWarning()
	{
		setBackgroundDrawable(BOOTSTRAP_EDIT_TEXT_WARNING);
	}

	public void setDanger()
	{
		setBackgroundDrawable(BOOTSTRAP_EDIT_TEXT_DANGER);
	}

	public void setDefault()
	{
		setBackgroundDrawable(BOOTSTRAP_EDIT_TEXT_DEFAULT);
	}

	public void setBootstrapEditTextEnabled(boolean enabled)
	{
		this.setEnabled(enabled);
	}

}
