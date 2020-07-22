package com.ineasnet.shanghaiguidao2.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/24.
 */

public class TimeUtil {
    /**
     * 获取时间差
     * @param time
     * @return
     */
    public static int getDayNum(String time){
        int day = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
//            Date d1 = df.parse(String.valueOf(new Date()));
            Date d2 = df.parse(time);
            long diff = System.currentTimeMillis() - d2.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);

            long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
            long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
            day = (int) days;
            System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
        }catch (Exception e){

        }
        return day;
    }
    /**
     * 获取时间差
     * @param time
     * @return
     */
    public static int getDayNum(long time){
        int day = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
//            System.currentTimeMillis();
//            Date d1 = df.parse(String.valueOf(new Date()));
            long diff = System.currentTimeMillis() - time;//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);

            long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
            long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
            day = (int) days;
            System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
        }catch (Exception e){

        }
        return day;
    }
}
