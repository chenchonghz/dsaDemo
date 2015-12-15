package com.szrjk.util;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import com.szrjk.config.Constant;
import com.szrjk.dhome.OtherPeopleActivity;
import com.szrjk.dhome.R;
import com.szrjk.dhome.Register2Activity;

/**
 * denggm on 2015/11/7.
 * DHome
 */
public class SpannableStringUtils {


    //设置超链接文字
    public static SpannableString getClickableSpan(Context context, String srcstr, int start, int stop, ClickableSpan cspan) {
        int link1start = start;
        int link1stop = stop;

        SpannableString spanStr = new SpannableString(srcstr);
        //设置下划线文字
        //spanStr.setSpan(new UnderlineSpan(), link1start, link1stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //设置文字的单击事件
        spanStr.setSpan(cspan, link1start, link1stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的前景色
        spanStr.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.search_bg)), link1start, link1stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }


    public static SpannableString getClickableFaceSpan(final Context context, String srcstr, int start, int stop, final String puserid, final String currentuserid) {

        SpannableString postContent1 = SpannableStringUtils.getClickableSpan(context, srcstr, start, stop, new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (currentuserid != null && currentuserid.equals(puserid)) {
                    //相同的用户
                    return;
                } else {
                    Intent i = new Intent(context, OtherPeopleActivity.class);
                    i.putExtra(Constant.USER_SEQ_ID, puserid);
                    context.startActivity(i);
                }
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        });
        return postContent1;
    }

}
