package com.szrjk.fire.testers;

/**
 * 最小长度
 */
public class MinLengthTester extends AbstractValuesTester
{
	@Override
	public boolean test(String content)
	{
		final long minLength = intValue;
		return content.length() >= minLength;
	}
}
