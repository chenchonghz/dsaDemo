package com.szrjk.fire.testers;

import android.text.TextUtils;

/**
 * 不是任何空值
 */
public class NotBlankTester extends AbstractTester
{
	@Override
	public boolean test(String content)
	{
		final boolean empty = TextUtils.isEmpty(content);
		return !empty && !testRegex("^\\s*$", content);
	}
}
