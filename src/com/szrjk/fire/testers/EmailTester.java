package com.szrjk.fire.testers;

/**
 * 邮件地址
 */
public class EmailTester extends AbstractTester
{

	static final String EMAIL_REGEX = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+"
			+ "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+"
			+ "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";

	@Override
	public boolean test(String content)
	{
		// 全部转换成小写来优化正则匹配性能
		return testRegex(EMAIL_REGEX, content.toLowerCase());
	}
}
