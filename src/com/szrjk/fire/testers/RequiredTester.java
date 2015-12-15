package com.szrjk.fire.testers;

import android.text.TextUtils;

/**
 * 非空
 */
public class RequiredTester extends AbstractTester
{
	@Override
	public boolean test(String content)
	{
		return !TextUtils.isEmpty(content);
	}
}
