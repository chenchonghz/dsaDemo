package com.szrjk.fire;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.szrjk.fire.testers.AbstractValuesTester;
import com.szrjk.fire.testers.EqualsToTester;
import com.szrjk.fire.testers.MaxLengthTester;
import com.szrjk.fire.testers.MaxValueTester;
import com.szrjk.fire.testers.MinLengthTester;
import com.szrjk.fire.testers.MinValueTester;
import com.szrjk.fire.testers.NotEqualsToTester;
import com.szrjk.fire.testers.RangeLengthTester;
import com.szrjk.fire.testers.RangeValueTester;
import com.szrjk.fire.testers.RequiredValueTester;

/**
 * 输入框的匹配模式
 *
 */
final class ValuePatternInvoker extends PatternInvoker<ValuePatternMeta, ValuePattern> {

    private static final String TAG = "ValueInvoker";

    ValuePatternInvoker(Context context, int viewId, TextView field, ValuePattern... patterns) {
        super(context, viewId, field);
        addPatterns(patterns);
    }

    @Override
    public Result performTest(){
        final String value = input.getText().toString();
        // 当输入内容为空时，如果第一个校验模式不是“Required”，则跳过后面的配置。
        final ValuePatternMeta first = patterns.get(0);
        if (TextUtils.isEmpty(value) && !ValuePattern.Required.equals(first.pattern)){
            return Result.passed(null);
        }
        final String inputKey = input.getClass().getSimpleName() + "@{" + input.getHint() + "}";
        for (ValuePatternMeta meta : patterns){
            meta.performLazyLoader();
            final AbstractValuesTester tester = findTester(meta);
            final boolean passed = tester.performTest(value);
            if (!passed){
                FireEyeEnv.verbose(TAG,
                        "[v] Performing: passed: NO, " + inputKey + "value: " + value + ", message: " + meta.message + ", tester: " + tester.getName());
                // 如果校验器发生异常，取异常消息返回
                String message = tester.getExceptionMessage();
                if (message == null) message = meta.getMessage();
                return Result.reject(message, value);
            } else{
                FireEyeEnv.verbose(TAG,
                        "[v] Performing: passed: YES, " + inputKey + "value: " + value + ", message: " + meta.message + ", tester: " + tester.getName());
            }
        }
        FireEyeEnv.log(TAG, "[D] " + inputKey + " -> passed: YES, value: " + value);
        return Result.passed(value);
    }

    @Override
    protected boolean onFilter(ValuePatternMeta meta, ValuePattern item) {
        if (ValuePattern.Required.equals(item)){
            this.patterns.add(0, meta);
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected ValuePatternMeta convert(ValuePattern item) {
        final ValuePatternMeta meta = ValuePatternMeta.parse(item);
        meta.convertMessage(context);
        FireEyeEnv.log(TAG, "[D] Value pattern meta -> " + meta.toString());
        return meta;
    }

    private AbstractValuesTester findTester(ValuePatternMeta meta){
        switch (meta.pattern){
            case EqualsTo:
                EqualsToTester equalsToTester = new EqualsToTester();
                switch (meta.valueType){
                    case Float:
                        equalsToTester.setFloatValue(Double.valueOf(meta.value));
                        break;
                    case Int:
                        equalsToTester.setIntValue(Long.valueOf(meta.value));
                        break;
                    case String:
                        equalsToTester.setStringValue(meta.value);
                        break;
                }
                return equalsToTester;
            case NotEqualsTo:
                NotEqualsToTester notEqualsToTester = new NotEqualsToTester();
                switch (meta.valueType){
                    case Float:
                        notEqualsToTester.setFloatValue(Double.valueOf(meta.value));
                        break;
                    case Int:
                        notEqualsToTester.setIntValue(Long.valueOf(meta.value));
                        break;
                    case String:
                        notEqualsToTester.setStringValue(meta.value);
                        break;
                }
                return notEqualsToTester;
            case MinLength:
                MinLengthTester minLengthTester = new MinLengthTester();
                minLengthTester.setIntValue(Long.parseLong(meta.value));
                return minLengthTester;
            case MaxLength:
                MaxLengthTester maxLengthTester = new MaxLengthTester();
                maxLengthTester.setIntValue(Long.parseLong(meta.value));
                return maxLengthTester;
            case MinValue:
                MinValueTester minValueTester = new MinValueTester();
                minValueTester.setFloatValue(Double.valueOf(meta.value));
                return minValueTester;
            case MaxValue:
                MaxValueTester maxValueTester = new MaxValueTester();
                maxValueTester.setFloatValue(Double.valueOf(meta.value));
                return maxValueTester;
            case RangeLength:
                RangeLengthTester rangeLengthTester = new RangeLengthTester();
                rangeLengthTester.setMinIntValue(Long.parseLong(meta.minValue));
                rangeLengthTester.setMaxIntValue(Long.parseLong(meta.maxValue));
                return rangeLengthTester;
            case RangeValue:
                RangeValueTester rangeValueTester = new RangeValueTester();
                rangeValueTester.setMinFloatValue(Double.valueOf(meta.minValue));
                rangeValueTester.setMaxFloatValue(Double.valueOf(meta.maxValue));
                return rangeValueTester;
            case Required:
                return new RequiredValueTester();
            default:
                return new RequiredValueTester(){
                    @Override
                    public boolean test(String content) {
                        return false;
                    }
                };
        }
    }

}
