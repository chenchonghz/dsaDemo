package com.szrjk.fire.testers;

import android.text.TextUtils;

/**
 * 非空
 */
public class RequiredValueTester extends AbstractValuesTester
{
	@Override
	public boolean test(String content)
	{
		return !TextUtils.isEmpty(content);
	}
}
