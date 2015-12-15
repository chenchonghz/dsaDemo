package com.szrjk.fire.testers;

/**
 * 中国民用车辆号牌
 */
public class VehicleNumberTester extends AbstractTester
{

	static final String VEHICLE_REGEX = "^[京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼川贵云藏陕甘青宁新渝]?[A-Z][A-HJ-NP-Z0-9学挂港澳练]{5}$";

	@Override
	public boolean test(String content)
	{
		// 全部转换成大写来优化正则匹配性能
		final String number = content.toUpperCase();
		return testRegex(VEHICLE_REGEX, number);
	}
}
