package com.szrjk.simplifyspan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.szrjk.simplifyspan.customspan.CustomAbsoluteSizeSpan;
import com.szrjk.simplifyspan.customspan.CustomClickableSpan;
import com.szrjk.simplifyspan.customspan.CustomImageSpan;
import com.szrjk.simplifyspan.customspan.CustomLabelSpan;
import com.szrjk.simplifyspan.other.CustomLinkMovementMethod;
import com.szrjk.simplifyspan.other.OnClickStateChangeListener;
import com.szrjk.simplifyspan.other.SpecialConvertMode;
import com.szrjk.simplifyspan.unit.BaseSpecialUnit;
import com.szrjk.simplifyspan.unit.SpecialClickableUnit;
import com.szrjk.simplifyspan.unit.SpecialImageUnit;
import com.szrjk.simplifyspan.unit.SpecialLabelUnit;
import com.szrjk.simplifyspan.unit.SpecialRawSpanUnit;
import com.szrjk.simplifyspan.unit.SpecialTextUnit;

/**
 * SimplifySpan Build
 * Created by iWgang on 15/12/2.
 * https://github.com/iwgang/SimplifySpan
 */
public class SimplifySpanBuild {
    private List<BaseSpecialUnit> mFinalSpecialUnit;
    private List<BaseSpecialUnit> mBeforeSpecialUnit;
    private StringBuilder mStringBuilder;
    private StringBuilder mBeforeStringBuilder;
    private Context mContext;
    private TextView mTextView;
    private Map<SpecialClickableUnit, PositionInfo> mBeforeCacheSpecialClickableUnitMap = new HashMap<SpecialClickableUnit, PositionInfo>();
    private Map<SpecialClickableUnit, PositionInfo> mCacheSpecialClickableUnitMap = new HashMap<SpecialClickableUnit, PositionInfo>();

    private StringBuilder mNormalSizeText;

    public SimplifySpanBuild(Context context, TextView textView) {
        this(context, textView, null);
    }

    public SimplifySpanBuild(Context context, TextView textView, String initializeNormalText) {
        init(context, textView, initializeNormalText);
    }

    public SimplifySpanBuild(Context context, TextView textView, String initializeNormalText, BaseSpecialUnit... normalSpecialUnits) {
        init(context, textView, initializeNormalText, normalSpecialUnits);
    }

    private void init(Context context, TextView textView, String initializeNormalText, BaseSpecialUnit... normalSpecialUnits) {
        mStringBuilder = new StringBuilder(TextUtils.isEmpty(initializeNormalText) ? "" : initializeNormalText);
        mBeforeStringBuilder = new StringBuilder("");
        mNormalSizeText = new StringBuilder("");
        mFinalSpecialUnit = new ArrayList<BaseSpecialUnit>();
        mBeforeSpecialUnit = new ArrayList<BaseSpecialUnit>();
        this.mContext = context;
        this.mTextView = textView;

        if (!TextUtils.isEmpty(initializeNormalText)) {
            if (null != normalSpecialUnits && normalSpecialUnits.length > 0) {
                buildNormalSpecialUnits(false, 0, initializeNormalText, normalSpecialUnits);
            } else {
                mNormalSizeText.append(initializeNormalText);
            }
        }
    }

    private void buildNormalSpecialUnits(boolean isAddToBeforeSpecialUnit, int initStartPos, String finalText, BaseSpecialUnit... normalSpecialUnits) {
        Map<String, Boolean> delTextTagMap = new HashMap<String, Boolean>();
        List<Integer> otherSpecialTextStartPos;
        for (BaseSpecialUnit st : normalSpecialUnits) {
            String specialText = st.getSpecialText();

            if (TextUtils.isEmpty(specialText) || !finalText.contains(specialText)) continue;

            int specialTextLength = specialText.length();
            switch (st.getConvertMode()) {
                case SpecialConvertMode.ONLY_FIRST:
                    st.setStartPoss(new int[]{initStartPos + finalText.indexOf(specialText)});
                    break;
                case SpecialConvertMode.ONLY_LAST:
                    st.setStartPoss(new int[]{initStartPos + finalText.lastIndexOf(specialText)});
                    break;
                case SpecialConvertMode.ALL:
                    int firstSpecialTextStartPos = finalText.indexOf(specialText);
                    otherSpecialTextStartPos = new ArrayList<Integer>();
                    otherSpecialTextStartPos.add(firstSpecialTextStartPos);
                    int previousSpecialTextStartPos = firstSpecialTextStartPos + specialTextLength;
                    boolean hasNext = true;
                    while (hasNext) {
                        int nextSpecialTextStartPos = finalText.indexOf(specialText, previousSpecialTextStartPos);
                        if (nextSpecialTextStartPos != -1) {
                            otherSpecialTextStartPos.add(nextSpecialTextStartPos);
                            previousSpecialTextStartPos = nextSpecialTextStartPos + specialTextLength;
                        } else {
                            hasNext = false;
                        }
                    }

                    int[] startPos = new int[otherSpecialTextStartPos.size()];
                    for (int i = 0; i < otherSpecialTextStartPos.size(); i++) {
                        startPos[i] = initStartPos + otherSpecialTextStartPos.get(i);
                    }
                    st.setStartPoss(startPos);
                    break;
            }

            int[] startPossTemp = st.getStartPoss();
            if (null == startPossTemp || startPossTemp.length == 0) continue ;

            // excluding content
            if (st instanceof SpecialTextUnit) {
                SpecialTextUnit specialTextUnit = (SpecialTextUnit)st;

                if (specialTextUnit.getTextSize() > 0) {
                    if (startPossTemp.length > 1) {
                        delTextTagMap.put(specialText, true);
                    } else {
                        delTextTagMap.put(specialText, false);
                    }
                }
            } else if (st instanceof SpecialImageUnit || st instanceof SpecialLabelUnit) {
                if (startPossTemp.length > 1) {
                    delTextTagMap.put(specialText, true);
                } else {
                    delTextTagMap.put(specialText, false);
                }
            }
        }

        if (!delTextTagMap.isEmpty()) {
            for (Map.Entry<String, Boolean> pm : delTextTagMap.entrySet()) {
                if (pm.getValue()) {
                    // delete all
                    finalText = finalText.replaceAll(pm.getKey(), "");
                } else {
                    // del first
                    finalText = finalText.replace(pm.getKey(), "");
                }
            }
        }


        if (isAddToBeforeSpecialUnit) {
            mNormalSizeText.insert(0, finalText);
            mBeforeSpecialUnit.addAll(Arrays.asList(normalSpecialUnits));
        } else {
            mNormalSizeText.append(finalText);
            mFinalSpecialUnit.addAll(Arrays.asList(normalSpecialUnits));
        }
    }

    /**
     * append SpecialUnit
     * @param specialUnit SpecialUnit (Not support convertMode)
     * @return SimplifySpanBuild
     */
    public SimplifySpanBuild appendSpecialUnit(BaseSpecialUnit specialUnit) {
        if (null == specialUnit) return this;

        String specialText = specialUnit.getSpecialText();
        if (TextUtils.isEmpty(specialText)) return this;

        // process start pos
        specialUnit.setStartPoss(new int[]{mStringBuilder.length()});
        mStringBuilder.append(specialText);
        mFinalSpecialUnit.add(specialUnit);
        return this;
    }

    /**
     * append normal text
     * @param text                normal text
     * @param normalSpecialUnits  [Optional] normal SpecialUnit list (support convertMode)
     * @return SimplifySpanBuild
     */
    public SimplifySpanBuild appendNormalText(String text, BaseSpecialUnit... normalSpecialUnits) {
        if (TextUtils.isEmpty(text)) return this;

        if (null != normalSpecialUnits && normalSpecialUnits.length > 0) {
            buildNormalSpecialUnits(false, mStringBuilder.length(), text, normalSpecialUnits);
        } else {
            mNormalSizeText.append(text);
        }

        mStringBuilder.append(text);
        return this;
    }

    /**
     * append SpecialUnit to first (Behind the existing BeforeContent)
     * @param specialUnit  (Not support convertMode)
     * @return SimplifySpanBuild
     */
    public SimplifySpanBuild appendSpecialUnitToFirst(BaseSpecialUnit specialUnit) {
        if (null == specialUnit) return this;

        String specialText = specialUnit.getSpecialText();
        if (TextUtils.isEmpty(specialText)) return this;

        // process start pos
        int curBeforeFirstPos = mBeforeStringBuilder.length();
        specialUnit.setStartPoss(new int[]{curBeforeFirstPos});
        mBeforeStringBuilder.insert(curBeforeFirstPos, specialText);
        mBeforeSpecialUnit.add(specialUnit);
        return this;
    }

    /**
     * append normal text to first (Behind the existing BeforeContent)
     * @param text                normal text
     * @param normalSpecialUnits  [Optional] normal SpecialUnit list (support convertMode)
     * @return SimplifySpanBuild
     */
    public SimplifySpanBuild appendNormalTextToFirst(String text, BaseSpecialUnit... normalSpecialUnits) {
        if (TextUtils.isEmpty(text)) return this;

        if (null != normalSpecialUnits && normalSpecialUnits.length > 0) {
            buildNormalSpecialUnits(true, mBeforeStringBuilder.length(), text, normalSpecialUnits);
        } else {
            mNormalSizeText.append(text);
        }

        mBeforeStringBuilder.append(text);
        return this;
    }

    /**
     * append multi clickable SpecialUnit or String
     * @param specialClickableUnit SpecialClickableUnit
     * @param specialUnitOrStrings Unit Or String
     * @return
     */
    public SimplifySpanBuild appendMultiClickableSpecialUnit(SpecialClickableUnit specialClickableUnit, Object... specialUnitOrStrings) {
        processMultiClickableSpecialUnit(false, specialClickableUnit, specialUnitOrStrings);
        return this;
    }

    /**
     * append multi clickable SpecialUnit or String to first (Behind the existing BeforeContent)
     * @param specialClickableUnit SpecialClickableUnit
     * @param specialUnitOrStrings Unit Or String
     * @return
     */
    public SimplifySpanBuild appendMultiClickableSpecialUnitToFirst(SpecialClickableUnit specialClickableUnit, Object... specialUnitOrStrings) {
        processMultiClickableSpecialUnit(true, specialClickableUnit, specialUnitOrStrings);
        return this;
    }

    /**
     * Build
     * @return SpannableStringBuilder
     */
    public SpannableStringBuilder build() {
        if (mBeforeStringBuilder.length() > 0) {
            mStringBuilder.insert(0, mBeforeStringBuilder);

            // reset SpecialUnit start pos
            if (!mFinalSpecialUnit.isEmpty()) {
                for (BaseSpecialUnit specialUnit : mFinalSpecialUnit) {
                    int[] tempStartPoss = specialUnit.getStartPoss();

                    if (null == tempStartPoss || tempStartPoss.length == 0) continue ;

                    for (int i = 0; i < tempStartPoss.length; i++) {
                        int oldStartPos = tempStartPoss[i];
                        tempStartPoss[i] = oldStartPos + mBeforeStringBuilder.length();
                    }
                }
            }

            // reset Cache SpecialClickableUnit start pos
            if (!mCacheSpecialClickableUnitMap.isEmpty()) {
                for (Map.Entry<SpecialClickableUnit, PositionInfo> cm : mCacheSpecialClickableUnitMap.entrySet()) {
                    cm.getValue().startPos += mBeforeStringBuilder.length();
                }
            }
        }

        if (!mBeforeCacheSpecialClickableUnitMap.isEmpty()) {
            mCacheSpecialClickableUnitMap.putAll(mBeforeCacheSpecialClickableUnitMap);
        }

        if (!mBeforeSpecialUnit.isEmpty()) {
            mFinalSpecialUnit.addAll(mBeforeSpecialUnit);
        }

        if (mStringBuilder.length() == 0) return null;
        if (mFinalSpecialUnit.isEmpty()) return new SpannableStringBuilder(mStringBuilder.toString());

        if (mNormalSizeText.length() == 0) mNormalSizeText.append(mStringBuilder);

        String normalSizeText = mNormalSizeText.toString();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(mStringBuilder);
        boolean isInitClickListener = false;
        for (BaseSpecialUnit st : mFinalSpecialUnit) {
            String specialText = st.getSpecialText();
            int[] startPoss = st.getStartPoss();

            if (TextUtils.isEmpty(specialText) || null == startPoss || startPoss.length == 0) continue;

            int specialTextLength = specialText.length();
            if (st instanceof SpecialTextUnit) {
                // text span
                SpecialTextUnit specialTextUnit = (SpecialTextUnit)st;

                final SpecialClickableUnit internalSpecialClickableUnit = specialTextUnit.getSpecialClickableUnit();
                if (null != internalSpecialClickableUnit) {
                    if (internalSpecialClickableUnit.getNormalTextColor() == 0) {
                        internalSpecialClickableUnit.setNormalTextColor(specialTextUnit.getSpecialTextColor());
                    }
                    if (internalSpecialClickableUnit.getNormalBgColor() == 0) {
                        internalSpecialClickableUnit.setNormalBgColor(specialTextUnit.getSpecialTextBackgroundColor());
                    }
                }
                for (int startPos : startPoss) {
                    // Set Text Color
                    if (specialTextUnit.getSpecialTextColor() != 0) {
                        spannableStringBuilder.setSpan(new ForegroundColorSpan(specialTextUnit.getSpecialTextColor()), startPos, startPos + specialTextLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    // Set Text Background Color
                    if (specialTextUnit.getSpecialTextBackgroundColor() != 0 && null == internalSpecialClickableUnit) {
                        spannableStringBuilder.setSpan(new BackgroundColorSpan(specialTextUnit.getSpecialTextBackgroundColor()), startPos, startPos + specialTextLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    // Add Underline
                    if (specialTextUnit.isShowUnderline()) {
                        spannableStringBuilder.setSpan(new UnderlineSpan(), startPos, startPos + specialTextLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    // Add StrikeThrough
                    if (specialTextUnit.isShowStrikeThrough()) {
                        spannableStringBuilder.setSpan(new StrikethroughSpan(), startPos, startPos + specialTextLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    // Set Text Bold
                    if (specialTextUnit.isTextBold()) {
                        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), startPos, startPos + specialTextLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    // Set Text Size
                    if (specialTextUnit.getTextSize() > 0) {
                        spannableStringBuilder.setSpan(new CustomAbsoluteSizeSpan(normalSizeText, specialTextUnit.getSpecialText(), Math.round(specialTextUnit.getTextSize()), mTextView, specialTextUnit.getGravity()), startPos, startPos + specialTextLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    // set clickable
                    if (null != internalSpecialClickableUnit) {
                        if (!isInitClickListener) {
                            isInitClickListener = true;
                            mTextView.setMovementMethod(CustomLinkMovementMethod.getInstance());
                        }
                        spannableStringBuilder.setSpan(new CustomClickableSpan(internalSpecialClickableUnit), startPos, startPos + specialTextLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            } else if (st instanceof SpecialImageUnit) {
                // image Span
                SpecialImageUnit specialImageUnit = (SpecialImageUnit)st;
                Bitmap bitmap = specialImageUnit.getBitmap();
                int imgWidth = specialImageUnit.getWidth();
                int imgHeight = specialImageUnit.getHeight();
                if (imgWidth > 0 && imgHeight > 0) {
                    int bitWidth = bitmap.getWidth();
                    int bitHeight = bitmap.getHeight();

                    if (imgWidth < bitWidth && imgHeight < bitHeight) {
                        Bitmap newBitmap = ThumbnailUtils.extractThumbnail(bitmap, imgWidth, imgHeight);
                        if (null != newBitmap) {
                            bitmap.recycle();
                            specialImageUnit.setBitmap(newBitmap);
                        }
                    }
                }

                for (int startPos : startPoss) {
                    CustomImageSpan curCustomImageSpan = new CustomImageSpan(mContext, normalSizeText, specialImageUnit);
                    spannableStringBuilder.setSpan(curCustomImageSpan, startPos, startPos + specialTextLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    if (specialImageUnit.isClickable()) {
                        addClickStateChangeListeners(startPos, startPos + specialTextLength, curCustomImageSpan);
                    }
                }
            } else if (st instanceof SpecialLabelUnit) {
                // label span
                SpecialLabelUnit specialLabelUnit = (SpecialLabelUnit)st;
                specialLabelUnit.convertLabelTextSize(sp2px(mContext, specialLabelUnit.getLabelTextSize()));

                for (int startPos : startPoss) {
                    CustomLabelSpan curCustomLabelSpan = new CustomLabelSpan(normalSizeText, specialLabelUnit);
                    spannableStringBuilder.setSpan(curCustomLabelSpan, startPos, startPos + specialTextLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    if (specialLabelUnit.isClickable()) {
                        addClickStateChangeListeners(startPos, startPos + specialTextLength, curCustomLabelSpan);
                    }
                }
            } else if (st instanceof SpecialClickableUnit) {
                // clickable span
                SpecialClickableUnit specialClickableUnit = (SpecialClickableUnit)st;

                if (!isInitClickListener) {
                    isInitClickListener = true;
                    mTextView.setMovementMethod(CustomLinkMovementMethod.getInstance());
                }

                int startPos = startPoss[0];
                spannableStringBuilder.setSpan(new CustomClickableSpan(specialClickableUnit), startPos, startPos + specialTextLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }  else if (st instanceof SpecialRawSpanUnit) {
                // raw span
                SpecialRawSpanUnit specialRawSpanUnit = (SpecialRawSpanUnit)st;

                int startPos = startPoss[0];
                spannableStringBuilder.setSpan(specialRawSpanUnit.getSpanObj(), startPos, startPos + specialTextLength, specialRawSpanUnit.getFlags());

                // Temporarily unable to support all
            }
        }

        return spannableStringBuilder;
    }

    private void processMultiClickableSpecialUnit(boolean isToFirst, SpecialClickableUnit specialClickableUnit, Object... specialUnitOrStrings) {
        if (null == specialClickableUnit || null == specialUnitOrStrings || specialUnitOrStrings.length == 0) return ;

        StringBuilder tempStrBuild = new StringBuilder();
        int baseStartPos;
        if (isToFirst) {
            baseStartPos = mBeforeStringBuilder.length();
        } else {
            baseStartPos = mStringBuilder.length();
        }
        for (Object su : specialUnitOrStrings) {
            if (su instanceof SpecialTextUnit) {
                SpecialTextUnit specialTextUnit = (SpecialTextUnit)su;

                String specialText = specialTextUnit.getSpecialText();
                if (TextUtils.isEmpty(specialText)) continue ;

                specialTextUnit.setSpecialClickableUnit(null);

                // process start pos
                specialTextUnit.setStartPoss(new int[]{baseStartPos + tempStrBuild.length()});
                if (isToFirst) {
                    mBeforeSpecialUnit.add(specialTextUnit);
                } else {
                    mFinalSpecialUnit.add(specialTextUnit);
                }
                tempStrBuild.append(specialText);
            } else if (su instanceof SpecialImageUnit) {
                SpecialImageUnit specialImageUnit = (SpecialImageUnit)su;

                String specialText = specialImageUnit.getSpecialText();
                if (TextUtils.isEmpty(specialText)) continue ;

                specialImageUnit.setClickable(true);
                if (specialImageUnit.getBgColor() == 0 && specialClickableUnit.getNormalBgColor() != 0) {
                    specialImageUnit.setBgColor(specialClickableUnit.getNormalBgColor());
                }

                // process start pos
                specialImageUnit.setStartPoss(new int[]{baseStartPos + tempStrBuild.length()});
                if (isToFirst) {
                    mBeforeSpecialUnit.add(specialImageUnit);
                } else {
                    mFinalSpecialUnit.add(specialImageUnit);
                }
                tempStrBuild.append(specialText);
            } else if (su instanceof SpecialLabelUnit) {
                SpecialLabelUnit specialLabelUnit = (SpecialLabelUnit)su;

                String specialText = specialLabelUnit.getSpecialText();
                if (TextUtils.isEmpty(specialText)) continue ;

                specialLabelUnit.setClickable(true);
                if (specialLabelUnit.getBgColor() == 0 && specialClickableUnit.getNormalBgColor() != 0) {
                    specialLabelUnit.setBgColor(specialClickableUnit.getNormalBgColor());
                }

                // process start pos
                specialLabelUnit.setStartPoss(new int[]{baseStartPos + tempStrBuild.length()});
                if (isToFirst) {
                    mBeforeSpecialUnit.add(specialLabelUnit);
                } else {
                    mFinalSpecialUnit.add(specialLabelUnit);
                }
                tempStrBuild.append(specialText);
            } else if (su instanceof String) {
                tempStrBuild.append(su.toString());
            }
        }

        String finalStr = tempStrBuild.toString();
        specialClickableUnit.setSpecialText(finalStr);
        specialClickableUnit.setStartPoss(new int[]{baseStartPos});
        PositionInfo positionInfo = new PositionInfo(baseStartPos, finalStr.length());
        if (isToFirst) {
            mBeforeStringBuilder.insert(baseStartPos, finalStr);
            mBeforeSpecialUnit.add(specialClickableUnit);
            mBeforeCacheSpecialClickableUnitMap.put(specialClickableUnit, positionInfo);
        } else {
            mStringBuilder.append(finalStr);
            mFinalSpecialUnit.add(specialClickableUnit);
            mCacheSpecialClickableUnitMap.put(specialClickableUnit, positionInfo);
        }
    }

    private void addClickStateChangeListeners(int startPos, int endPos, OnClickStateChangeListener onClickStateChangeListener) {
        if (mCacheSpecialClickableUnitMap.isEmpty()) return ;

        for (Map.Entry<SpecialClickableUnit, PositionInfo> cm : mCacheSpecialClickableUnitMap.entrySet()) {
            PositionInfo curPositionInfo = cm.getValue();
            int curStartPos = curPositionInfo.startPos;
            int curEndPos = curStartPos + curPositionInfo.textLength;
            if (startPos >= curStartPos && endPos <= curEndPos) {
                SpecialClickableUnit curCacheSpecialClickableUnit = cm.getKey();
                List<OnClickStateChangeListener> onClickStateChangeListeners = curCacheSpecialClickableUnit.getOnClickStateChangeListeners();
                if (null == onClickStateChangeListeners) {
                    onClickStateChangeListeners = new ArrayList<OnClickStateChangeListener>();
                    curCacheSpecialClickableUnit.setOnClickStateChangeListeners(onClickStateChangeListeners);
                }
                onClickStateChangeListeners.add(onClickStateChangeListener);
                break ;
            }
        }
    }

    public static float sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return spValue * scale;
    }

    static class PositionInfo {
        int startPos;
        int textLength;

        public PositionInfo(int startPos, int textLength) {
            this.startPos = startPos;
            this.textLength = textLength;
        }
    }

}
