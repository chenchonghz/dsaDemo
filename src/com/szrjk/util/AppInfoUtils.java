package com.szrjk.util;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import com.szrjk.config.Constant;

/**
 * denggm on 2015/12/29.
 * DHome
 * ��ȡһЩ �ֻ���Ϣ
 */
public class AppInfoUtils {


    /**
     * �õ� deviceid,���ʧ�ܷ���  ��0��
     * @param context
     * @return
     */
    public static String fetchDeviceId(Context context){
        String androidId = "0";
        try {
            //���к�
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            Log.e("error", "", e);
        }
        return androidId;
    }

}
