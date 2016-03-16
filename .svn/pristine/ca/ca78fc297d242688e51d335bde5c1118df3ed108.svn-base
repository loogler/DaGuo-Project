package com.daguo.modem.schedule;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;

/**
 * 
 * @author Bugs_rabbit
 * @function  计算时间的工具
 */
public class Util_DateDay {
	
	
	private String weekDay,SetStr;
	private int NowInt,SetInt;
	private String timeStr;
	private Time timeNow=new Time();
	
	private static String[] months = {"一月", "二月", "三月", "四月",
        "五月", "六月", "七月", "八月",
        "九月", "十月", "十一月", "十二月"};
	private static String[] months2 = {"Jan", "Feb", "Mar", "Apr",
        "May", "Jun", "Jul", "Aug",
        "Sept", "Oct", "Nov", "Dec"};
    private static String[] days1 = {"星期日", "星期一", "星期二", "星期三",
        "星期四", "星期五", "星期六"};
    private static String[] days2 = {"SUN", "MON", "TUE", "WED",
        "THU", "FRI", "SAT"};
	
	public Util_DateDay(Context context) {
		
		SharedPreferences share = context.getSharedPreferences("INIT",Context.MODE_WORLD_READABLE);
        SetStr =share.getString("SET", "0");
        timeNow.setToNow();
        timeStr=timeNow.toString();
		Util_FindDayOfYear fDayOfYear=new Util_FindDayOfYear();
        NowInt=fDayOfYear .getDayOfYear(timeNow.year, timeNow.month, timeNow.monthDay);
	    SetInt=Integer.parseInt(SetStr);
	    
	   
	}
	/**
	 * 获得"星期一"
	 * @return
	 */
    public String getDays1(){
    	return days1[timeNow.weekDay];
    }
    /**
	 *获得"MON"
	 * @return
	 */
    public String getDays2(){
    	return days2[timeNow.weekDay];
    }
    /**
	 *获得"二月"
	 * @return
	 */
    public String getMonth(){ 
    	return months[timeNow.month];
    }
    /**
	 *获得"Feb"
	 * @return
	 */
    public String getMonth2(){ 
    	return months2[timeNow.month];//
    }
    /**
	 *获得"2"
	 * @return
	 */
    public String getMonth3(){ 
    	return timeNow.month+1+"";//
    }
    /**
	 *获得"20"
	 * @return
	 */
    public String getDate(){
    	return Integer.toString(timeNow.monthDay);//
    }
    /**
	 *获得"1991"
	 * @return
	 */
    public String getYear(){
    	return Integer.toString(timeNow.year);
    }
    /**
	 *获得"k"
	 * @return
	 */
	public String  getWeedDay(){
            if (SetStr.equals("0")) {
			    weekDay ="未定义";
		    }
		    else {
			    weekDay =((NowInt-SetInt)/7+1)+"";
		    }
		return weekDay;//获得"k"
    }

	public String getCurrentTime() {
		return timeStr;
	}
}

