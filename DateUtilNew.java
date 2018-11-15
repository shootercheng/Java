package com.scd.util;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
	

/**
 *
 * @author scd
 *
 */
public class DateUtil {

	private static final Logger logger = Logger.getLogger("lavasoft");

	private static final int secondsAnHour = 3600;
	//时间粒度	单位：秒(s)
	public static final int FIVE_MIN_INTERVAL = 300;
	public static final int FIFTEEN_MIN_INTERVAL = 900;
	public static final int THIRTY_MIN_INTERVAL = 1800;
	public static final int ONE_DAY_INTERVAL = 86400;
	public static final int SEVEN_DAY_INTERVAL = 604800;
	public static final int THIRTY_DAY_INTERVAL = 2592000;
	//时间点个数
	public static final int MULTIPLYING_RATE = 30;
	//时间显示格式
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	//某一天 00:00:00时间
	public static final String TIME_FORMAT_ZERO = "yyyy-MM-dd 00:00:00";
	//某一月 yyyy-MM-00 00:00:00
	public static final String TIME_FORMATDAY_ZERO = "yyyy-MM-00 00:00:00";

	public static final String HH_MM = "HH:mm";

	public static final String MM_DD = "MM-dd";




	/**
	 * 获取指定时间的下一个时间粒度整数倍的时间点
	 * @param milliseconds	时间毫秒数(ms)
	 * @param intervalsec	时间粒度秒(s)
	 * @return the number of milliseconds
	 */
	public static long getNextGranularityTime(long milliseconds, int intervalsec) {
		long retLong = 0;
		Calendar cal = Calendar.getInstance();
		if (milliseconds <= 0 || intervalsec <= 0) {
			return cal.getTimeInMillis();
		}
		if (intervalsec >= secondsAnHour) {
			long remainder = milliseconds % (secondsAnHour * 1000);
			if (remainder == 0) {
				return milliseconds +secondsAnHour * 1000;
			} else {
				return milliseconds - remainder + secondsAnHour * 1000;
			}
		} else {
			int rawOffset = cal.getTimeZone().getRawOffset();
			retLong = ((milliseconds + rawOffset) / (intervalsec * 1000) + 1) * intervalsec * 1000;
			return retLong - rawOffset;
		}
	}

	/**
	 * 获取指定时间的上一个时间粒度整数倍的时间点
	 * @param milliseconds	时间毫秒数(ms)
	 * @param intervalsec	时间粒度秒(s)
	 * @return the number of milliseconds
	 */
	public static long getPreGranularityTime(long milliseconds, int intervalsec) throws ParseException{
		long retLong = 0;
		Calendar cal = Calendar.getInstance();
		if (milliseconds <= 0 || intervalsec <= 0) {
			return cal.getTimeInMillis();
		}
		if (intervalsec >= secondsAnHour) {
//			return milliseconds - milliseconds % (secondsAnHour * 1000);
//			return milliseconds - intervalsec * 1000;
			 if(intervalsec < THIRTY_DAY_INTERVAL) {
				 return getDayStartTime(milliseconds);
			 }else{
				 return getMonthSatrtTime(milliseconds);
			 }
		} else {
			int rawOffset = cal.getTimeZone().getRawOffset();
			retLong = ((milliseconds + rawOffset) / (intervalsec * 1000) + 1) * intervalsec * 1000 - intervalsec * 1000;
			return retLong - rawOffset;
		}
	}

	public static Date convertMillisecondstoDate(long milliseconds){
		Date date = new Date(milliseconds);
		return date;
	}

	public static long convertDatetoMillisecond(Date date){
		return date.getTime();
	}

	public static String formatDatetoString(Date date, String pattern){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

	public static Long getStartTime(long endtime, int intervalsec, int rate){
		long startTime = 0L;
		if(intervalsec < ONE_DAY_INTERVAL ) {
			startTime = endtime - intervalsec * 1000 * (rate - 1);
		}else if(intervalsec == ONE_DAY_INTERVAL){
			startTime = convertDatetoMillisecond(getChangeYearMonthDay(convertMillisecondstoDate(endtime), 0, 0, -(rate-1)));
		}else if(intervalsec == SEVEN_DAY_INTERVAL){
			startTime = convertDatetoMillisecond(getChangeYearMonthDay(convertMillisecondstoDate(endtime), 0, 0, -(rate-1)*7));
		}else if(intervalsec == THIRTY_DAY_INTERVAL){
			startTime = convertDatetoMillisecond(getChangeYearMonthDay(convertMillisecondstoDate(endtime), 0, -(rate - 1), 0));
		}
		return startTime;
	}
	
	public static Long getStartTimeNew(long endtime, int intervalsec, int rate){
		long startTime = 0L;
		if(intervalsec < THIRTY_DAY_INTERVAL){
			startTime = endtime - intervalsec * 1000L * (rate -1);
		}else{
			startTime = convertDatetoMillisecond(getChangeYearMonthDay(convertMillisecondstoDate(endtime), 0, -(rate - 1), 0));
		}
		return startTime;
	}

	/**
	 * 获取粒度横坐标时间点
	 * @param endtime
	 * @param intervalsec
	 * @param rate
	 * @return
	 */
	public static List<Long> getTimes(long endtime, int intervalsec, int rate){
		long startTime = getStartTime(endtime, intervalsec, rate);
		List<Long> timeslist = new ArrayList<Long>();
		timeslist.add(startTime);
		//月粒度需要单独计算
		if(intervalsec < THIRTY_DAY_INTERVAL) {
			long preNode = startTime;
			for (int i = 1; i < rate - 1; i++) {
				long curNode = preNode + intervalsec * 1000;
				timeslist.add(curNode);
				preNode = curNode;
			}
		}else if(intervalsec == THIRTY_DAY_INTERVAL){
			Date startdate = convertMillisecondstoDate(startTime);
			Date preNode = startdate;
			for(int i = 1; i < rate - 1; i++){
				Date curNode = getChangeYearMonthDay(preNode, 0, 1, 0);
				long curDateL = convertDatetoMillisecond(curNode);
				timeslist.add(curDateL);
				preNode = curNode;
			}
		}
		timeslist.add(endtime);
		return timeslist;
	}

	public static List<String> getTimeSegments(long endtime, int intervalsec, int rate){
		long startTime = getStartTime(endtime, intervalsec, rate);
		List<String> timesegs = new ArrayList<String>();
		if(intervalsec < THIRTY_DAY_INTERVAL) {
			long preNode = startTime;
			for (int i = 1; i < rate + 1; i++) {
				Long curNode = preNode + intervalsec * 1000;
				String timeseg = preNode + "_" + curNode;
				timesegs.add(timeseg);
				preNode = curNode;
			}
		}else if(intervalsec == THIRTY_DAY_INTERVAL){
			Date startdate = convertMillisecondstoDate(startTime);
			Date preNode = startdate;
			for(int i = 1; i < rate + 1; i++){
				Date curNode = getChangeYearMonthDay(preNode, 0, 1, 0);
//				String preNodestr = convertDatetoMillisecond(preNode);
//				String curNodestr = convertDatetoMillisecond(curNode);
				String timeseg = convertDatetoMillisecond(preNode) + "_" + convertDatetoMillisecond(curNode);
				timesegs.add(timeseg);
				preNode = curNode;
			}
		}
		return timesegs;
	}



	public static String formatMillisecondstoString(long milliseconds, String pattern) {
		Date date = convertMillisecondstoDate(milliseconds);
		return formatDatetoString(date, pattern);
	}

	public static List<Point> initPoint(List<Long> times) {
		List<Point> points = new ArrayList<Point>();
		for(int i = 0; i < times.size(); i++) {
			Point point = new Point();
			point.setTime(times.get(i));
			point.setFilesize(new BigInteger("0"));
			points.add(point);
		}
		return points;
	}
	
	public static Map<Long, BigInteger> initMapPoint(List<Long> times){
		Map<Long, BigInteger> mapPoint = new HashMap<Long, BigInteger>();
		for(Long time : times){
			mapPoint.put(time, new BigInteger("0"));
		}
		return mapPoint;
	}
	
	public static void sortMap(List<Map.Entry<Long, BigInteger>> listMaps){
		Collections.sort(listMaps, new Comparator<Map.Entry<Long, BigInteger>>() {
			public int compare(Map.Entry<Long, BigInteger> o1, Map.Entry<Long, BigInteger> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
	}

	public static List<Map<String, Long>> createRemoteData() throws ParseException{
		List<Map<String, Long>> listMap = new ArrayList<Map<String,Long>>();
		String[] times = {"2018-10-18 07:10:00","2018-10-17 11:55:00",
				"2018-10-19 11:36:00","2018-10-19 11:02:00"};
		String[] filesizes = {"1","1","1","1"};
		for(int i = 0; i < 4; i++) {
			Map<String,Long> map = new HashMap<String,Long>();
			String strtime = times[i];
			Date date = parseStringtoDate(strtime, YYYY_MM_DD_HH_MM_SS);
			map.put("time", convertDatetoMillisecond(date));
			map.put("filesize", Long.valueOf(filesizes[i]));
			listMap.add(map);
		}
		return listMap;
	}

	public static Date parseStringtoDate(String sdate, String pattern) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.parse(sdate);
	}

	/**
	 * 查询日志时间在那一个时间段，属于那个点
	 * @param milliseconds
	 * @param timesegms
	 * @return
	 */
	public static long findLogsegtime(long milliseconds, List<String> timesegms) {
		for(String timeseg : timesegms) {
			String[] timesArray = timeseg.split("_");
			if(timesArray.length == 2) {
				long start = Long.valueOf(timesArray[0]);
				long end = Long.valueOf(timesArray[1]);
				if(milliseconds >= start && milliseconds < end) {
					return start;
				}
			}
		}
		return 0L;
	}

	public static Point findPoint(long milliseconds, List<Point> points) {
		for(Point point : points) {
			if(point.getTime().longValue() == milliseconds) {
				return point;
			}
		}
		return null;
	}


	/**
	 * 获取某一天开始时间
	 * @param milliseconds
	 * @return Date
	 */
	public static Long getDayStartTime(long milliseconds) throws  ParseException {
		 String dayStart = formatMillisecondstoString(milliseconds, TIME_FORMAT_ZERO);
//		 logger.info("--------one day start time str----------"+dayStart);
		 Date date = parseStringtoDate(dayStart, TIME_FORMAT_ZERO);
//		 logger.info("--------one day start time date------------"+date);
		 return convertDatetoMillisecond(date);
	}

	public static Long getMonthSatrtTime(long milliseconds) throws ParseException{
		String monthStart = formatMillisecondstoString(milliseconds, TIME_FORMATDAY_ZERO);
//		 logger.info("--------one day start time str----------"+dayStart);
		Date date = parseStringtoDate(monthStart, TIME_FORMATDAY_ZERO);
//		 logger.info("--------one day start time date------------"+date);
		return convertDatetoMillisecond(date);
	}

	/**
	 * 获取某一天之前时间多少天
	 * @param date
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date getChangeYearMonthDay(Date date, int year, int month, int day){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, year);
		calendar.add(Calendar.MONTH, month);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

	public static void main(String[] args) throws ParseException {
		System.out.println(formatDatetoString(new Date(),  YYYY_MM_DD_HH_MM_SS));
		Long endTime = getPreGranularityTime(System.currentTimeMillis(),THIRTY_DAY_INTERVAL);
		long startTime = getStartTime(endTime, THIRTY_DAY_INTERVAL, MULTIPLYING_RATE);
		System.out.println(formatMillisecondstoString(endTime, YYYY_MM_DD_HH_MM_SS));
//		Date date = convertMillisecondstoDate(endTime);
//		System.out.println(formatDatetoString(date, YYYY_MM_DD_HH_MM_SS));
//		Long startTime = getStartTime(endTime, FIVE_MIN_INTERVAL, MULTIPLYING_RATE);
//		Date sDate = convertMillisecondstoDate(startTime);
//		System.out.println(formatDatetoString(sDate, YYYY_MM_DD_HH_MM_SS));
		List<Long> timeslist = getTimes(endTime, THIRTY_DAY_INTERVAL, MULTIPLYING_RATE);
		for(Long time : timeslist) {
			System.out.println(formatMillisecondstoString(time, YYYY_MM_DD_HH_MM_SS));
		}
		System.out.println("----------横坐标轴时间段-------------");
		List<String> timeSegs = getTimeSegments(endTime, THIRTY_DAY_INTERVAL, MULTIPLYING_RATE);
		for(String stimes : timeSegs) {
//			System.out.println(stimes);
			String[] timeArray = stimes.split("_");
			if(timeArray.length == 2) {
				String start = formatMillisecondstoString(Long.valueOf(timeArray[0]), YYYY_MM_DD_HH_MM_SS);
				String end = formatMillisecondstoString(Long.valueOf(timeArray[1]), YYYY_MM_DD_HH_MM_SS);
				System.out.println(start + "--TO--" + end);
			}
		}
//		List<Point> points = initPoint(timeslist);
//		String teStr = "2018-10-17 22:10:00";
//		System.out.println(parseStringtoDate(teStr,YYYY_MM_DD_HH_MM_SS));
//		List<Map<String, Long>> remoteList = createRemoteData();
//		for(int i = 0; i < remoteList.size(); i++) {
//			Map<String, Long> map = remoteList.get(i);
//			long logtime = map.get("time");
//			long filesize = map.get("filesize");
//			long starttime = findLogsegtime(logtime, timeSegs);
//			if(starttime != 0L) {
//				Point point = findPoint(starttime, points);
//				if(point != null) {
//					BigInteger pfilesize = point.getFilesize();
//					point.setFilesize(pfilesize.add(new BigInteger(String.valueOf(filesize))));
//				}
//			}
//		}
//		for(Point point : points) {
//			System.out.println(point);
////			long milliseconds = point.getTime();
////			System.out.println(formatMillisecondstoString(milliseconds, HH_MM) + "--" + point.getFilesize());
//		}
		// new
		Map<Long, BigInteger> mapPoint = initMapPoint(timeslist);
		List<Map<String, Long>> remoteList = createRemoteData();
		for(int i = 0; i < remoteList.size(); i++) {
			Map<String, Long> map = remoteList.get(i);
			long logtime = map.get("time");
			long filesize = map.get("filesize");
			if(logtime < startTime || logtime > endTime){
				continue;
			}
			long axeTime = findLogsegtime(logtime, timeSegs);
			if(axeTime != 0L) {
				BigInteger oldValue = mapPoint.get(axeTime);
				mapPoint.put(axeTime, oldValue.add(new BigInteger(String.valueOf(filesize))));
			}
		}
		List<Map.Entry<Long, BigInteger>> pointList = new ArrayList<Map.Entry<Long, BigInteger>>(mapPoint.entrySet());
		for(Map.Entry<Long, BigInteger> entry : pointList){
			System.out.println("time:" + entry.getKey() + ",filesize:" + entry.getValue());
		}
		sortMap(pointList);
		for(Map.Entry<Long, BigInteger> entry : pointList){
			System.out.println("time:" + entry.getKey() + ",filesize:" + entry.getValue());
		}
	}
}
