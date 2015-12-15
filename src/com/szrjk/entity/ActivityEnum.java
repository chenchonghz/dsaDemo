package com.szrjk.entity;

public enum ActivityEnum
{
	SeedPostActivity(1), SeedCaseActivity1(2), SeedCaseActivity2(3), SeedCaseActivity3(
			4), SeedCaseActivity4(5), SeedPuzzleActivity1(6), SeedPuzzleActivity2(
			7),SeedPuzzleActivity3(8);

	private int value = 0;

	private ActivityEnum(int value)
	{
		this.value = value;
	}

	public static ActivityEnum valueOf(int value)
	{
		switch (value)
		{
			case 1:
				return SeedPostActivity;
			case 2:
				return SeedCaseActivity1;
			case 3:
				return SeedCaseActivity2;
			case 4:
				return SeedCaseActivity3;
			case 5:
				return SeedCaseActivity4;
			case 6:
				return SeedPuzzleActivity1;
			case 7:
				return SeedPuzzleActivity2;
			case 8:
				return SeedPuzzleActivity3;
			default:
				return null;
		}
	}

	public int value()
	{
		return this.value;
	}
}
