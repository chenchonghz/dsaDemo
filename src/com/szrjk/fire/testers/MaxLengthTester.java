package com.szrjk.fire.testers;

/**
 * 最大长度
 */
public class MaxLengthTester extends AbstractValuesTester
{
	@Override
	public boolean test(String content)
	{
		final long maxLength = intValue;
		return content.length() <= maxLength;
	}
}
