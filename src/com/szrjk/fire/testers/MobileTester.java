package com.szrjk.fire.testers;

/**
 * 手机号
 */
public class MobileTester extends AbstractTester
{

	static final String PHONE_REGEX = "^(\\+?\\d{2}-?)?(1[34578])\\d{9}$";

	@Override
	public boolean test(String content)
	{
		return testRegex(PHONE_REGEX, content);
	}
}
