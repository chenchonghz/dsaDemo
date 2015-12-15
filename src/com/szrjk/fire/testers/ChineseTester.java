package com.szrjk.fire.testers;

/**
 * 只能是中文  用来演示
 */
public class ChineseTester extends AbstractTester
{

	static final String P_REGEX = "^([\\u4E00-\\u9FA5])*$";

	@Override
	public boolean test(String content)
	{
		return testRegex(P_REGEX, content);
	}
}
