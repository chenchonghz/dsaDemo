package com.szrjk.fire.testers;

import android.text.TextUtils;

/**
 * 数值型
 */
public class NumericTester extends AbstractTester
{
	@Override
	public boolean test(String content)
	{
		return isNumeric(content);
	}

	public static boolean isNumeric(String input)
	{
		if (TextUtils.isEmpty(input)) { return false; }
		char[] chars = input.toCharArray();
		int sz = chars.length;
		boolean hasExp = false;
		boolean hasDecPoint = false;
		boolean allowSigns = false;
		boolean foundDigit = false;
		int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
		if (sz > start + 1)
		{
			if (chars[start] == '0' && chars[start + 1] == 'x')
			{
				int i = start + 2;
				if (i == sz) { return false; // str == "0x"
				}
				for (; i < chars.length; i++)
				{
					if ((chars[i] < '0' || chars[i] > '9')
							&& (chars[i] < 'a' || chars[i] > 'f')
							&& (chars[i] < 'A' || chars[i] > 'F')) { return false; }
				}
				return true;
			}
		}
		sz--; // don't want to loop to the last char, check it afterwords
		int i = start;
		while (i < sz || (i < sz + 1 && allowSigns && !foundDigit))
		{
			if (chars[i] >= '0' && chars[i] <= '9')
			{
				foundDigit = true;
				allowSigns = false;

			}
			else if (chars[i] == '.')
			{
				if (hasDecPoint || hasExp)
				{
					return false;
				}
				hasDecPoint = true;
			}
			else if (chars[i] == 'e' || chars[i] == 'E')
			{
				if (hasExp)
				{
					return false;
				}
				if (!foundDigit) { return false; }
				hasExp = true;
				allowSigns = true;
			}
			else if (chars[i] == '+' || chars[i] == '-')
			{
				if (!allowSigns) { return false; }
				allowSigns = false;
				foundDigit = false; // we need a digit after the E
			}
			else
			{
				return false;
			}
			i++;
		}
		if (i < chars.length)
		{
			if (chars[i] >= '0' && chars[i] <= '9')
			{
				return true;
			}
			if (chars[i] == 'e' || chars[i] == 'E')
			{
				return false;
			}
			if (!allowSigns
					&& (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) { return foundDigit; }
			if (chars[i] == 'l' || chars[i] == 'L')
			{
				return foundDigit && !hasExp;
			}
			return false;
		}
		return !allowSigns && foundDigit;
	}
}
