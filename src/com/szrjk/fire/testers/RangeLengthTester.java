package com.szrjk.fire.testers;

/**
 * 长度区间
 */
public class RangeLengthTester extends AbstractValuesTester
{
	@Override
	public boolean test(String content)
	{
		final long minLength = minIntValue;
		final long maxLength = maxIntValue;
		final long length = content.length();
		return minLength <= length && length <= maxLength;
	}
}
