package com.szrjk.fire.testers;

import java.util.HashMap;
import java.util.Locale;

/**
 * 17位车架号。
 * 
 */
public class VINTester extends AbstractTester
{

	static final String VIN_REGEX = "[\\dA-HJ-NPR-Z]{17}";
	static final char[] CHECK_DIGIT =
	{
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'X'
	};
	static final int[] WEIGHT =
	{
			8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2
	};
	static final HashMap<Character, Integer> VALUES = new HashMap<Character, Integer>(
			17);

	static
	{
		for (int i = 0; i < 10; i++)
		{
			VALUES.put((char) (i + '0'), i);
		}
		VALUES.put('A', 1);
		VALUES.put('B', 2);
		VALUES.put('C', 3);
		VALUES.put('D', 4);
		VALUES.put('E', 5);
		VALUES.put('F', 6);
		VALUES.put('G', 7);
		VALUES.put('H', 8);
		VALUES.put('J', 1);
		VALUES.put('K', 2);
		VALUES.put('L', 3);
		VALUES.put('M', 4);
		VALUES.put('N', 5);
		VALUES.put('P', 7);
		VALUES.put('R', 9);
		VALUES.put('S', 2);
		VALUES.put('T', 3);
		VALUES.put('U', 4);
		VALUES.put('V', 5);
		VALUES.put('W', 6);
		VALUES.put('X', 7);
		VALUES.put('Y', 8);
		VALUES.put('Z', 9);
	}

	@Override
	protected boolean test(String content)
	{
		final String vin = content.toUpperCase(Locale.US);
		return testRegex(VIN_REGEX, vin) && testCheckDigit(vin);
	}

	private boolean testCheckDigit(String vin)
	{
		char[] array = vin.toCharArray();
		int sum = 0;
		for (int i = 0; i < 17; i++)
		{
			sum += VALUES.get(array[i]) * WEIGHT[i];
		}
		int mode = sum % 11;
		return CHECK_DIGIT[mode] == array[8];
	}

}
