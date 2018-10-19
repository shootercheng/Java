package com.scd.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author shootercheng
 * @date 2018/10/18.
 */
public class DataType {

    public static int count = 0;

    public static Long convertLongByte(Long max){

        long val = max / 1024;
        if(val > 1024){
            count ++;
            return convertLongByte(val);
        }else {
            return val;
        }
    }

    public static Date getDateBefore(Date date, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)-day);
        return calendar.getTime();
    }

    public static Date getChangeYearMonthDay(Date date, int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        calendar.add(Calendar.MONTH, month);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    public static void main(String[] args) throws ParseException{
        Long maxl = Long.MAX_VALUE;
        long val = maxl / 1024;
        System.out.println(val);
        System.out.println(convertLongByte(maxl));
        System.out.println(count);
        Long max = 7L * 1024 * 1024 * 1024 * 1024 * 1024;
        // bytes    kb
        System.out.println(max);
        long endTime = DateUtil.getPreGranularityTime(System.currentTimeMillis(), DateUtil.ONE_DAY_INTERVAL);
        System.out.println(DateUtil.formatMillisecondstoString(endTime, DateUtil.YYYY_MM_DD_HH_MM_SS));
        //22天    23天    24天     25天错误，为啥？？？？       ---> 一段经典的问题
//        long daylong = endTime - DateUtil.ONE_DAY_INTERVAL * 1000 * (30 - 5);
        long daylong = endTime - DateUtil.ONE_DAY_INTERVAL * 1000 * (30 - 6);
        daylong = daylong - DateUtil.ONE_DAY_INTERVAL * 1000 * 6;
        System.out.println(DateUtil.formatMillisecondstoString(daylong,DateUtil.YYYY_MM_DD_HH_MM_SS));
        Date date = DateUtil.convertMillisecondstoDate(DateUtil.getDayStartTime(System.currentTimeMillis()));
        Date dateBefore = getDateBefore(date, 210);
        Date dateChange = getChangeYearMonthDay(date, 0, 0, -210);
        System.out.println(DateUtil.formatDatetoString(dateBefore, DateUtil.YYYY_MM_DD_HH_MM_SS));
        System.out.println(DateUtil.formatDatetoString(dateChange, DateUtil.YYYY_MM_DD_HH_MM_SS));
        Date dateMonth = getChangeYearMonthDay(date, 0 , -30, 0);
        System.out.println(DateUtil.formatDatetoString(dateMonth, DateUtil.YYYY_MM_DD_HH_MM_SS));
    }
}
