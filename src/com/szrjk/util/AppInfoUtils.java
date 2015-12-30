package com.szrjk.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;
import org.apache.http.conn.util.InetAddressUtils;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * denggm on 2015/12/29.
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


    /**
     * �õ�ip��ַ����ip����ʱ����������ip
     * @return
     */
    public static String GetHostIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(inetAddress
                            .getHostAddress())) {
                        if (!inetAddress.getHostAddress().toString()
                                .equals("null")
                                && inetAddress.getHostAddress() != null) {
                            return inetAddress.getHostAddress().toString().trim();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("error", "",ex);
        }
        return "";
    }

    /**
     * ��ȡ gps�������������ȡ���ˣ�����""
     * @param context
     * @return
     */
    public static String gpsdesc(Context context){
        String gpsdesc = "";
        try {
            LocationManager GpsManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
            Location location = GpsManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                gpsdesc = "����:" + location.getAccuracy() + ",�߶� :"
                        + location.getAltitude() + ",���� :" + location.getBearing()
                        + ",�ٶ� :" + location.getSpeed() + ", γ�� :"
                        + location.getLatitude() + ",���� :"
                        + location.getLongitude();
            }
        } catch (Exception e) {
            Log.e("error","",e);
        }
        return gpsdesc;
    }


    /**
     * ��ȡmac��ַ,�����ȡ���ˣ����� ""
     * @return
     */
    public static String getLocalMacAddress() {
        String Mac=null;
        try{

            String path="sys/class/net/wlan0/address";
            if((new File(path)).exists())
            {
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer = new byte[8192];
                int byteCount = fis.read(buffer);
                if(byteCount>0)
                {
                    Mac = new String(buffer, 0, byteCount, "utf-8");
                }
            }
            if(Mac==null||Mac.length()==0)
            {
                path="sys/class/net/eth0/address";
                FileInputStream fis_name = new FileInputStream(path);
                byte[] buffer_name = new byte[8192];
                int byteCount_name = fis_name.read(buffer_name);
                if(byteCount_name>0)
                {
                    Mac = new String(buffer_name, 0, byteCount_name, "utf-8");
                }
            }
            if(Mac.length()==0||Mac==null){
                return "";
            }
            return  Mac.trim();
        }catch(Exception io){
            Log.e("daming.zou**exception*", ""+io.toString(),io);
        }
        return "";
        //		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //		WifiInfo info = wifi.getConnectionInfo();
        //		if (info.getMacAddress() != null) {
        //			return info.getMacAddress().toString();
        //		}
    }

}
