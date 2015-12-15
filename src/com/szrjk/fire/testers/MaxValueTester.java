package com.szrjk.fire.testers;

/**
 * 最大伟
 */
public class MaxValueTester extends AbstractValuesTester
{
	@Override
	public boolean test(String content)
	{
		final double maxValue = floatValue;
		return Double.valueOf(content) <= maxValue;
	}

}
