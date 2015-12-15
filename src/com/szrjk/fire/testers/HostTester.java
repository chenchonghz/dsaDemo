package com.szrjk.fire.testers;

/**
 * 主机地址
 */
public class HostTester extends AbstractTester
{

	static final String HOST_REGEX = "^([a-z0-9]([a-z0-9\\-]{0,65}[a-z0-9])?\\.)+[a-z]{2,6}$";

	@Override
	public boolean test(String content)
	{
		// 全部转换成小写来优化正则匹配性能
		final String host = content.toLowerCase();
		return testRegex(IPv4Tester.IPV4_REGEX, host)
				|| testRegex(HOST_REGEX, host);
	}
}
