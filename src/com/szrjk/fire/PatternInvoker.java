package com.szrjk.fire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.widget.TextView;

/**
 * 输入框的匹配模式
 *
 */
abstract class PatternInvoker<Patt, Meta> {

    protected final List<Patt> patterns = new ArrayList<Patt>();
    protected final Context context;

    public final TextView input;
    public final int viewId;

    protected PatternInvoker(Context context, int viewId, TextView input) {
        this.context = context;
        this.input = input;
        this.viewId = viewId;
    }

    /**
     * 添加匹配模式列表
     * @param patterns 匹配模式列表
     */
    public final void addPatterns(Meta[] patterns){
        for (Meta item : Arrays.asList(patterns)){
            final Patt pattern = convert(item);
            if (!onFilter(pattern, item)){
                this.patterns.add(pattern);
            }
        }
    }

    public final String dump(){
        StringBuilder buf = new StringBuilder(input.toString());
        buf.append('@').append(input.getHint()).append(':').append(input.getText());
        buf.append("\n -> patterns:\n");
        for (Patt pattern : patterns){
            buf.append("\t").append(pattern.toString()).append(" ,\n");
        }
        return buf.toString();
    }

    /**
     * 校验输入
     * @return 校验结果
     */
    public abstract Result performTest();

    /**
     * 配置项过滤
     * @param pattern 转换后的储存项
     * @param item 配置项目
     * @return 如果配置项手动排序，则返回True。否则返回False。
     */
    protected abstract boolean onFilter(Patt pattern, Meta item);

    /**
     * 将配置项 Meta 转换成储存项 P
     */
    protected abstract Patt convert(Meta item);
}
