package com.kwe.kweplus.util;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;


/**
 * 日期时间工具类
 * 
 * @author 康豪杰
 * 
 */
public final class CalendarUtils {
	public static void main(String[] args) {
		String str ="2019-06-17||2019-06-24";
		System.out.println(str.substring(0,str.indexOf("||"))+" 00:00:00");
		System.out.println(str.substring(str.indexOf("||")+2)+" 00:00:00");
		//System.out.println(str.split("||")[3]+" 00:00:00");
		//System.out.println(CalendarUtils.getParseDate(CalendarUtils.Y_M_DHMS_LONG,str.split("||")[0]+" 00:00:00"));
	}

	public static final String YMDHMS_LONG = "yyyyMMddHHmmss";
	public static final String YMDHMS_SHORT = "yyMMddHHmmss";
	public static final String YMD_LONG = "yyyyMMdd";
	public static final String YMD_SHORT = "yyMMdd";
	public static final String Y_M_DHMS_LONG = "yyyy-MM-dd HH:mm:ss";
	public static final String Y_M_DHMS_SHORT = "yy-MM-dd HH:mm:ss";
	public static final String Y_M_D_LONG = "yyyy-MM-dd";
	public static final String Y_M_D_SHORT = "yy-MM-dd";
	public static final String YBMBD_LONG = "yyyy/MM/dd";
	public static final String YBMBD_SHORT = "yy/MM/dd";


	public static final String TIME_FORMAT="HHmmss";

	/**
	 * 得到格式化后的日期
	 * 
	 * @param fmt
	 * @param dtstr
	 * @return
	 */
	public static Date getParseDate(String fmt, String dtstr) {
		if (!MyUtils.isNotEmpty(fmt)) {
			throw new RuntimeException("fmt is not null");
		}
		try {
			return new SimpleDateFormat(fmt).parse(dtstr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 得到格式化后的日期字符串
	 * 
	 * @param fmt
	 * @param dt
	 * @return
	 */
	public static String getFormatDate(String fmt, Date dt) {
		if (!MyUtils.isNotEmpty(fmt)) {
			throw new RuntimeException("fmt is not null");
		}
		if (dt == null) {
			return new SimpleDateFormat(fmt).format(new Date());
		}
		return new SimpleDateFormat(fmt).format(dt);
	}

	/**
	 * 根据指定日期修改年份
	 * 
	 * @param dt
	 * @param amount
	 *            正数代表增加多少年，负数代表减少多少年
	 * @return
	 */
	public static Date mergeYear(Date dt, int amount) {
		Calendar cl = Calendar.getInstance();
		if (dt != null) {
			cl.setTime(dt);
		}
		cl.add(Calendar.YEAR, amount);
		return cl.getTime();
	}

	/**
	 * 在指定日期上面对月份进行增减
	 * 
	 * @param dt
	 * @param amount
	 *            正数代表增加，负数代表减少
	 * @return
	 */
	public static Date mergeMonth(Date dt, int amount) {
		Calendar cl = Calendar.getInstance();
		if (dt != null) {
			cl.setTime(dt);
		}
		cl.add(Calendar.MONTH, amount);
		return cl.getTime();
	}

	/**
	 * 在指定日期上面对天数进行增减
	 * 
	 * @param dt
	 * @param amount
	 *            正数代表增加 ，负数代表减少
	 * @return
	 */
	public static Date mergeDate(Date dt, int amount) {
		Calendar cl = Calendar.getInstance();
		if (dt != null) {
			cl.setTime(dt);
		}
		cl.add(Calendar.DATE, amount);
		return cl.getTime();
	}

	/**
	 * 在指定日期上面对小时进行增减
	 * 
	 * @param dt
	 * @param amount
	 *            正数代表增加 ，负数代表减少
	 * @return
	 */
	public static Date mergeHour(Date dt, int amount) {
		Calendar cl = Calendar.getInstance();
		if (dt != null) {
			cl.setTime(dt);
		}
		cl.add(Calendar.HOUR, amount);
		return cl.getTime();
	}

	/**
	 * 在指定日期上面对秒进行增减
	 * 
	 * @param dt
	 * @param amount
	 *            正数代表增加 ，负数代表减少
	 * @return
	 */
	public static Date mergeSecond(Date dt, int amount) {
		Calendar cl = Calendar.getInstance();
		if (dt != null) {
			cl.setTime(dt);
		}
		cl.add(Calendar.SECOND, amount);
		return cl.getTime();
	}

	/**
	 * 在指定日期上面对分钟进行增减
	 * 
	 * @param dt
	 * @param amount
	 *            正数代表增加 ，负数代表减少
	 * @return
	 */
	public static Date mergeMinute(Date dt, int amount) {
		Calendar cl = Calendar.getInstance();
		if (dt != null) {
			cl.setTime(dt);
		}
		cl.add(Calendar.MINUTE, amount);
		return cl.getTime();
	}

	/***
	 * 计算两个时间段的分钟间隔
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Long calcMinuteInterval(Date startDate,Date endDate){
		Long millionSeconds = startDate.getTime()-endDate.getTime();
		return Math.abs(millionSeconds/1000/60);
	}

	/**
	 * 比较日期大小.
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static String compareDate(Date date1,Date date2){
		if(date1.before(date2)) return "<";
		else if(date1.after(date2)) return ">";
		else
			return "=";
	}

	public static Timestamp getDateOnZero(Date date){
		String zeroStr = getFormatDate("yyyy-MM-dd",date)+" 00:00:00";
		return new Timestamp(getParseDate("yyyy-MM-dd HH:mm:ss",zeroStr).getTime());
	}

	public static Timestamp getDateOnLast(Date date){
		String zeroStr = getFormatDate("yyyy-MM-dd",date)+" 23:59:59";
		return new Timestamp(getParseDate("yyyy-MM-dd HH:mm:ss",zeroStr).getTime());
	}

	public static LocalDateTime getLocalDateTime(Date date){
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		return LocalDateTime.ofInstant(instant, zone);
	}

	public static Date localDateTimeToUdate(LocalDateTime localDateTime) {
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDateTime.atZone(zone).toInstant();
		return Date.from(instant);
	}
}
