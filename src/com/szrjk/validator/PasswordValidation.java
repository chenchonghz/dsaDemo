package com.szrjk.validator;

import java.util.regex.Pattern;

import android.content.Context;
import android.widget.Toast;

import com.szrjk.dhome.R;

public class PasswordValidation extends ValidationExecutor
{

	@Override
	public boolean doValidate(Context context, String text)
	{
		String regex = "^[a-zA-Z]\\w{5,19}$";
		boolean result = Pattern.compile(regex).matcher(text).find();
		if (!result)
		{
			Toast.makeText(context, context.getString(R.string.pwd_error),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}
