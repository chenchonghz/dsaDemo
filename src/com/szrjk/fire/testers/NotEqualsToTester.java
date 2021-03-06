package com.szrjk.fire.testers;

/**
 * 内容或者数值不相同
 */
public class NotEqualsToTester extends AbstractValuesTester
{
	@Override
	public boolean test(String content)
	{
		if (intValue != null)
		{
			return intValue.longValue() != Long.valueOf(content);
		}
		else if (floatValue != null)
		{
			return floatValue.doubleValue() != Double.valueOf(content);
		}
		else
		{
			return !stringValue.equals(content);
		}
	}
}
