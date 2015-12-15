package com.szrjk.util;

import java.util.Random;

public class RandomUtil
{
	public static String generateCode()
	{
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 6; i++)
		{
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}
}
