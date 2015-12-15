package com.szrjk.fire.testers;

/**
 * 最小值
 */
public class MinValueTester extends AbstractValuesTester
{
	@Override
	public boolean test(String content)
	{
		final double minValue = floatValue;
		return minValue <= Double.valueOf(content);
	}
}
