package com.szrjk.fire.testers;

/**
 * 数值区间
 */
public class RangeValueTester extends AbstractValuesTester
{
	@Override
	public boolean test(String content)
	{
		final double minValue = minFloatValue;
		final double maxValue = maxFloatValue;
		final double value = Double.valueOf(content);
		return minValue <= value && value <= maxValue;
	}
}
