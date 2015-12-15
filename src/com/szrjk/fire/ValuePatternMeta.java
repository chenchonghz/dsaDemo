package com.szrjk.fire;

class ValuePatternMeta extends PatternMeta<ValuePattern> {

    final LazyLoader lazyLoader;

    ValueType valueType;
    String minValue;
    String maxValue;
    String value;

    ValuePatternMeta(ValuePattern pattern, String message, int messageId, LazyLoader lazyLoader, ValueType valueType, String minValue, String maxValue, String value) {
        super(pattern, message, messageId);
        this.lazyLoader = lazyLoader;
        this.valueType = valueType;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = value;
    }

    private void setValue(String value){
        valueType = ValueType.String;
        syncValue(value);
    }

    private void setValue(long value){
        valueType = ValueType.Int;
        syncValue(value);
    }

    private void setValue(double value){
        valueType = ValueType.Float;
        syncValue(value);
    }

    private void syncValue(Object value){
        this.value = String.valueOf(value);
        this.minValue = this.value;
    }

    void performLazyLoader(){
        if (lazyLoader == null) return;
        final String stringValue = lazyLoader.loadString();
        final Long longValue = lazyLoader.loadInt();
        final Double floatValue = lazyLoader.loadFloat();
        if (stringValue != null){
            setValue(stringValue);
        }
        else if (longValue != null){
            setValue(longValue);
        }
        else if (floatValue != null){
            setValue(floatValue);
        }
    }

    String getMessage() {
        if (this.message == null) return null;
        String message = this.message;
        if (minValue != null) message = message.replace("{0}", minValue);
        if (maxValue != null) message = message.replace("{1}", maxValue);
        return message;
    }

    static ValuePatternMeta parse(ValuePattern pattern){
        return new ValuePatternMeta(pattern,
                pattern.getMessage(),
                pattern.getMessageId(),
                pattern.getLazyLoader(),
                pattern.getValueType(),
                pattern.getMinValue(),
                pattern.getMaxValue(),
                pattern.getValue());
    }

    @Override
    public String toString() {
        return "{" +
                "pattern=" + pattern.name() +
                ", messageId=" + messageId +
                ", message='" + message + '\'' +
                ", lazyLoader=" + lazyLoader +
                ", valueType=" + valueType +
                ", minValue='" + minValue + '\'' +
                ", maxValue='" + maxValue + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
