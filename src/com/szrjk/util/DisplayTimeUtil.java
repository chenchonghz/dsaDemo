package com.szrjk.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DisplayTimeUtil {


	public static String displayTimeString(String time) throws Exception{
		String displayTime = null;
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		Date postTime = fmt.parse(time);
//		Log.e("TIME", "post时间："+postTime.toString());
		Date currentTime = fmt.parse(fmt.format(new Date()));
		Calendar current = Calendar.getInstance();
		Calendar c_postTime = Calendar.getInstance();
        Calendar today = Calendar.getInstance();    
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));  
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));  
        today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));  
        today.set( Calendar.HOUR_OF_DAY, 0);  
        today.set( Calendar.MINUTE, 0);  
        today.set(Calendar.SECOND, 0);
        current.setTime(currentTime);
        c_postTime.setTime(postTime);
        if(c_postTime.after(today)){
        	long current_time = currentTime.getTime();
        	long post_time = postTime.getTime();
        	long min = (current_time - post_time)/60000;
        	System.out.println("分钟："+min);
        	if(min>=5&&min<=60){
        		displayTime = min+"分钟前";
        	}else if(min<5){
        		displayTime = "刚刚";
        	}else{
        		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        		displayTime = format.format(postTime);
        	}
        	return displayTime;

        }else{
        	Calendar year = Calendar.getInstance();
        	year.set(Calendar.YEAR, current.get(Calendar.YEAR));
        	year.set(Calendar.MONTH, 1);
        	year.set(Calendar.DAY_OF_MONTH, 1);
        	year.set(Calendar.HOUR_OF_DAY, 0);
        	year.set( Calendar.MINUTE, 0);  
            year.set(Calendar.SECOND, 0);
            if(c_postTime.after(year)){
            	SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
            	displayTime = format.format(postTime);
            }else{
            	SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            	displayTime = format.format(postTime);
            }
        	return displayTime;
        }	
	}
	
	public static String displayDetailTimeString(String time) throws Exception{
		String displayTime = null;
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		Date postTime = fmt.parse(time);
//		Log.e("TIME", "post时间："+postTime.toString());
		Date currentTime = fmt.parse(fmt.format(new Date()));
		Calendar current = Calendar.getInstance();
		Calendar c_postTime = Calendar.getInstance();
        Calendar today = Calendar.getInstance();    
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));  
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));  
        today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));  
        today.set( Calendar.HOUR_OF_DAY, 0);  
        today.set( Calendar.MINUTE, 0);  
        today.set(Calendar.SECOND, 0);
        current.setTime(currentTime);
        c_postTime.setTime(postTime);
        if(c_postTime.after(today)){
        	long current_time = currentTime.getTime();
        	long post_time = postTime.getTime();
        	long min = (current_time - post_time)/60000;
        	System.out.println("分钟："+min);
        	if(min>=5&&min<=60){
        		displayTime = min+"分钟前";
        	}else if(min<5){
        		displayTime = "刚刚";
        	}else{
        		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        		displayTime = format.format(postTime);
        	}
        	return displayTime;

        }else{
        	Calendar year = Calendar.getInstance();
        	year.set(Calendar.YEAR, current.get(Calendar.YEAR));
        	year.set(Calendar.MONTH, 1);
        	year.set(Calendar.DAY_OF_MONTH, 1);
        	year.set(Calendar.HOUR_OF_DAY, 0);
        	year.set( Calendar.MINUTE, 0);  
            year.set(Calendar.SECOND, 0);
            if(c_postTime.after(year)){
            	SimpleDateFormat format = new SimpleDateFormat("MM月dd日  HH:mm");
            	displayTime = format.format(postTime);
            }else{
            	SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
            	displayTime = format.format(postTime);
            }
        	return displayTime;
        }	
	}
	
	public static String displayDateString(String time) throws Exception{
		String displayDate = null;
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		Date newsTime = fmt.parse(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		displayDate = format.format(newsTime);
		return displayDate;
	}
	public static String displayTimeStringt2message(String time) throws Exception{
		String displayTime = null;
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		Date postTime = fmt.parse(time);
//		Log.e("TIME", "post时间："+postTime.toString());
		Date currentTime = fmt.parse(fmt.format(new Date()));
		Calendar current = Calendar.getInstance();
		Calendar c_postTime = Calendar.getInstance();
        Calendar today = Calendar.getInstance();    
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));  
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));  
        today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));  
        today.set( Calendar.HOUR_OF_DAY, 0);  
        today.set( Calendar.MINUTE, 0);  
        today.set(Calendar.SECOND, 0);
        current.setTime(currentTime);
        c_postTime.setTime(postTime);
        if(c_postTime.after(today)){
        	long current_time = currentTime.getTime();
        	long post_time = postTime.getTime();
        	long min = (current_time - post_time)/60000;
        	System.out.println("分钟："+min);
        	if(min>=5&&min<=60){
        		displayTime = min+"分钟前";
        	}else if(min<5){
        		displayTime = "刚刚";
        	}else{
        		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        		displayTime = format.format(postTime);
        	}
        	return displayTime;

        }else{
        	Calendar year = Calendar.getInstance();
        	year.set(Calendar.YEAR, current.get(Calendar.YEAR));
        	year.set(Calendar.MONTH, 1);
        	year.set(Calendar.DAY_OF_MONTH, 1);
        	year.set(Calendar.HOUR_OF_DAY, 0);
        	year.set( Calendar.MINUTE, 0);  
            year.set(Calendar.SECOND, 0);
            if(c_postTime.after(year)){
            	SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
            	displayTime = format.format(postTime);
            }else{
            	SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            	displayTime = format.format(postTime);
            }
        	return displayTime;
        }	
	}
}
