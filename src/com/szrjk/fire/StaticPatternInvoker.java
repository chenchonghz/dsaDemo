package com.szrjk.fire;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.szrjk.fire.testers.*;

/**
 * 输入框的匹配模式
 * 
 */
final class StaticPatternInvoker extends
		PatternInvoker<StaticPatternMeta, StaticPattern>
{

	private static final String TAG = "StaticInvoker";

	StaticPatternInvoker(Context context, int viewId, TextView field,
			StaticPattern[] patterns)
	{
		super(context, viewId, field);
		addPatterns(patterns);
	}

	@Override
	public Result performTest()
	{
		final String value = input.getText().toString();
		// 当输入内容为空时，如果第一个校验模式不是“Required”，则跳过后面的配置。
		// 在FireEye入口时，已经确保patterns至少包含一个Pattern配置。
		if (TextUtils.isEmpty(value)
				&& !StaticPattern.Required.equals(patterns.get(0).pattern)) { return Result
				.passed(null); }
		final String inputKey = input.getClass().getSimpleName() + "@{"
				+ input.getHint() + "}";
		for (StaticPatternMeta meta : patterns)
		{
			final AbstractTester tester = findTester(meta);
			FireEyeEnv.verbose(TAG, "[v] Testing.meta: " + inputKey + "value: "
					+ value + ", tester: " + tester.getName());
			final boolean passed = tester.performTest(value);
			if (!passed) { return Result.reject(meta.message, value); }
		}
		FireEyeEnv.log(TAG, "[D] " + inputKey + " -> passed: YES, value: "
				+ value);
		return Result.passed(value);
	}

	@Override
	protected boolean onFilter(StaticPatternMeta meta, StaticPattern item)
	{
		if (StaticPattern.Required.equals(item))
		{
			this.patterns.add(0, meta);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	protected StaticPatternMeta convert(StaticPattern item)
	{
		final StaticPatternMeta meta = StaticPatternMeta.parse(item);
		meta.convertMessage(context);
		FireEyeEnv
				.verbose(TAG, "[v] Static pattern meta -> " + meta.toString());
		return meta;
	}

	private AbstractTester findTester(StaticPatternMeta meta)
	{
		switch (meta.pattern)
		{
			case BankCard:
				return new BankCardTester();
			case Digits:
				return new DigitsTester();
			case DigitsLetters:
				return new DigitsLettersTester();
			case Email:
				return new EmailTester();
			case Host:
				return new HostTester();
			case IDCard:
				return new IDCardTester();
			case IPv4:
				return new IPv4Tester();
			case Mobile:
				return new MobileTester();
			case NotBlank:
				return new NotBlankTester();
			case Numeric:
				return new NumericTester();
			case Required:
				return new RequiredTester();
			case URL:
				return new URLTester();
			case VehicleNumber:
				return new VehicleNumberTester();
			case VIN:
				return new VINTester();
			case MIX:
				return new MixTester();
			case ISCHINESE:
				return new ChineseTester();
			case ONLYCHINESEORMIX:
				return new ChineseMixTester();
			default:
				return new AbstractTester()
				{
					@Override
					public boolean test(String content)
					{
						return false;
					}
				};
		}
	}
}
