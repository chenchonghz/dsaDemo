package com.szrjk.fire.testers;

/**
 * 只能abc,用来演示
 */
public class MixTester extends AbstractTester
{

	static final String MIX_REGEX = "^[abc]$";

	@Override
	public boolean test(String content)
	{
		return testRegex(MIX_REGEX, content);
	}
}
