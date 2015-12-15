package com.szrjk.fire.testers;

import android.text.TextUtils;

/**
 * 纯数字
 */
public class DigitsTester extends AbstractTester
{
	@Override
	public boolean test(String content)
	{
		return TextUtils.isDigitsOnly(content);
	}
}
