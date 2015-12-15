package com.szrjk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * denggm on 2015/10/19.
 * DHome
 */
public class DDateUtils {

    /**
     * String -->> Calendar
     * @param datestr
     * @param format
     * @return
     * @throws ParseException
     */
    public static Calendar dformatStrToCalendar(String datestr,String format) throws ParseException {
        SimpleDateFormat sformat = new SimpleDateFormat(format);
        sformat.parse(datestr);
        return sformat.getCalendar();
    }

    /**
     * Calendar -->> String
     * @param pcalendar
     * @param format
     * @return
     */
    public static String dformatCalendarToStr(Calendar pcalendar,String format){
        SimpleDateFormat sformat = new SimpleDateFormat(format);
        sformat.setCalendar(pcalendar);
        return  sformat.format(pcalendar.getTime());
    }


    /**
     *
     * @param srcstr   20140202
     * @param oldformat   yyyyMMdd
     * @param newformat   yyyy年MM月dd日
     * @return
     * @throws ParseException
     */
    public static String dformatOldstrToNewstr(String srcstr,String oldformat,String newformat) throws ParseException {
        Calendar c = dformatStrToCalendar(srcstr,oldformat);
        return dformatCalendarToStr(c,newformat);
    }

    /**
     * 
     * @param d 传入"yyyy-MM-dd"
     * @return 返回年龄
     * @throws ParseException 解析格式异常
     */
    public static long getAge(String d) throws ParseException {
    	
    	SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		  Date date=new Date();     
		  Date mydate = myFormatter.parse(d);
		  long day=(date.getTime()-mydate.getTime())/(24*60*60*1000) + 1;
    	
		return day/365;
    	
    }

}
