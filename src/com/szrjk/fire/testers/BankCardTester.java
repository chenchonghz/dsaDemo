package com.szrjk.fire.testers;

/**
 * 银行卡/信用卡的卡号规则校验
 */
public class BankCardTester extends AbstractTester
{

	@Override
	public boolean test(String inputValue)
	{
		if (!testRegex("[\\d -]*", inputValue)) { return false; }
		String value = inputValue.replaceAll("\\D", "");
		final int length = value.length();
		if (13 > length || 19 < length)
		{
			return false;
		}
		else
		{
			return matchLuhn(value, length);
		}
	}

	private static boolean matchLuhn(String rawCardNumbers, int length)
	{
		char cDigit;
		int nCheck = 0, nDigit;
		boolean bEven = false;
		for (int n = length - 1; n >= 0; n--)
		{
			cDigit = rawCardNumbers.charAt(n);
			nDigit = Integer.parseInt(String.valueOf(cDigit), 10);
			if (bEven)
			{
				if ((nDigit *= 2) > 9)
				{
					nDigit -= 9;
				}
			}
			nCheck += nDigit;
			bEven = !bEven;
		}
		return (nCheck % 10) == 0;
	}
}
