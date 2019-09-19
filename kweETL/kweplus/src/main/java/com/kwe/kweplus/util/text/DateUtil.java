package com.kwe.kweplus.util.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期相关的操作
 * @author Dawei
 *  
 */

public class DateUtil {

	private final static Logger log = LoggerFactory.getLogger(DateUtil.class);
	public static final long ONE_DAY=86400;//一天的秒数
	/**
	 * 当天的开始时间
	 * @return
	 */
	public static long startOfTodDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date.getTime()/1000;
	}
	  /**
	   * 当天的结束时间
	   * @return
	   */
	  public static long endOfTodDay() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	    Date date=calendar.getTime();
	    return date.getTime()/1000;
	  }
	  /**
	   * 昨天的开始时间
	   * @return
	   */
	  public static long startOfyesterday() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.add(Calendar.DATE, -1);
	    calendar.set(Calendar.MILLISECOND, 0);
	    Date date=calendar.getTime();
	    return date.getTime()/1000;
	  }
	  /**
	   * 昨天的结束时间
	   * @return
	   */
	  public static long endOfyesterday() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	    calendar.add(Calendar.DATE, -1);
	    Date date=calendar.getTime();
	    return date.getTime()/1000;
	  }
	  /**
	   * 某天的开始时间
	   * @param dayUntilNow 距今多少天以前
	   * @return 时间戳
	   */
	  public static long startOfSomeDay(int dayUntilNow){
		  Calendar calendar = Calendar.getInstance();
		  calendar.set(Calendar.HOUR_OF_DAY, 0);
		  calendar.set(Calendar.MINUTE, 0);
		  calendar.set(Calendar.SECOND, 0);
		  calendar.set(Calendar.MILLISECOND, 0);
		  calendar.add(Calendar.DATE, -dayUntilNow);
		  Date date=calendar.getTime();
		  return date.getTime()/1000;
	  }
	/**
	 * 某天的結束时间
	 * @param dayUntilNow 距今多少天以前
	 * @return 时间戳
	 */
	public static long endOfSomeDay(int dayUntilNow){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		calendar.add(Calendar.DATE, -dayUntilNow);
		Date date=calendar.getTime();
		return date.getTime()/1000;
	}
	  
	 /**
	  *  某天的年月日
	  * @param dayUntilNow 距今多少天以前
	  * @return 年月日map  key为  year month day
	  */
	 public static Map<String,Object> getYearMonthAndDay(int dayUntilNow){
		 Map<String,Object> map=new HashMap<String,Object>();
		 Calendar calendar=Calendar.getInstance();
		  calendar.set(Calendar.HOUR_OF_DAY, 0);
		  calendar.set(Calendar.MINUTE, 0);
		  calendar.set(Calendar.SECOND, 0);
		  calendar.set(Calendar.MILLISECOND, 0);
		  calendar.add(Calendar.DATE, -dayUntilNow);
		  map.put("year", calendar.get(Calendar.YEAR));
		  map.put("month", calendar.get(Calendar.MONTH)+1);
		  map.put("day", calendar.get(Calendar.DAY_OF_MONTH));
		  return map;
	 }

	/**
	 * 计算两个时间的间隔天数
	 * @param fDate 小的
	 * @param oDate 大的
	 * @return
	 */
	 public static int daysOfTwo(Date fDate, Date oDate){
		 Calendar aCalendar = Calendar.getInstance();

		 aCalendar.setTime(fDate);
		 int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

		 if (oDate == null){
			 aCalendar.setTime(new Date());
		 } else {
			 aCalendar.setTime(oDate);
		 }
		 int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

		 return day2 - day1;
	 }
	/**
	 * 将一个字符串转换成日期格式
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date toDate(String date, String pattern) {
		if((""+date).equals("")){
			return null;
		}
		if(pattern == null){
			pattern = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date newDate = new Date();
		try {
			newDate = sdf.parse(date);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return newDate;
	}
	
	/**
	 * 把日期转换成字符串型
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String toString(Date date, String pattern){
		if(date == null){
			return "";
		}
		if(pattern == null){
			pattern = "yyyy-MM-dd";
		}
		String dateString = "";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			dateString = sdf.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dateString;
	}
	
	public static String toString(Long time,String pattern){
		if(time>0){
			if(time.toString().length()==10){
				time = time*1000;
			}
			Date date = new Date(time);
			String str  = DateUtil.toString(date, pattern);
			return str;
		}
		return "";
	}

	
	
	/**
	 * 获取上个月的开始结束时间
	 * @return
	 */
	public static Long[] getLastMonth() {
		   // 取得系统当前时间
		   Calendar cal = Calendar.getInstance();
		   int year = cal.get(Calendar.YEAR);
		   int month = cal.get(Calendar.MONTH) + 1;
		   
		   // 取得系统当前时间所在月第一天时间对象
		   cal.set(Calendar.DAY_OF_MONTH, 1);
		   
		   // 日期减一,取得上月最后一天时间对象
		   cal.add(Calendar.DAY_OF_MONTH, -1);
		   
		   // 输出上月最后一天日期
		   int day = cal.get(Calendar.DAY_OF_MONTH);

		   String months = "";
		   String days = "";

		   if (month > 1) {
		    month--;
		   } else {
		    year--;
		    month = 12;
		   }
		   if (!(String.valueOf(month).length() > 1)) {
		    months = "0" + month;
		   } else {
		    months = String.valueOf(month);
		   }
		   if (!(String.valueOf(day).length() > 1)) {
		    days = "0" + day;
		   } else {
		    days = String.valueOf(day);
		   }
		   String firstDay = "" + year + "-" + months + "-01";
		   String lastDay = "" + year + "-" + months + "-" + days;

		   Long[] lastMonth = new Long[2];
		   lastMonth[0] =DateUtil.getDateline(firstDay);
		   lastMonth[1] = DateUtil.getDateline(lastDay);

		 //  //System.out.println(lastMonth[0] + "||" + lastMonth[1]);
		   return lastMonth;
		}
	
	
	/**
	 * 获取当月的开始结束时间
	 * @return
	 */
	public static Long[] getCurrentMonth() {
		   // 取得系统当前时间
		   Calendar cal = Calendar.getInstance();
		   int year = cal.get(Calendar.YEAR);
		   int month = cal.get(Calendar.MONTH)+1 ;
		   // 输出下月第一天日期
		   int notMonth = cal.get(Calendar.MONTH)+2 ;
		   // 取得系统当前时间所在月第一天时间对象
		   cal.set(Calendar.DAY_OF_MONTH, 1);
		   
		   // 日期减一,取得上月最后一天时间对象
		   cal.add(Calendar.DAY_OF_MONTH, -1);
		   
		  

		   String months = "";
		   String nextMonths = "";


		   if (!(String.valueOf(month).length() > 1)) {
		    months = "0" + month;
		   } else {
		    months = String.valueOf(month);
		   }
		   if (!(String.valueOf(notMonth).length() > 1)) {
			   nextMonths = "0" + notMonth;
		   } else {
			   nextMonths = String.valueOf(notMonth);
		   }
		   String firstDay = "" + year + "-" + months + "-01";
		   String lastDay=	""+year+"-"+nextMonths+"-01";
		   Long[] currentMonth = new Long[2]; 
		   currentMonth[0] =DateUtil.getDateline(firstDay);
		   currentMonth[1] = DateUtil.getDateline(lastDay);

		 //  //System.out.println(lastMonth[0] + "||" + lastMonth[1]);
		   return currentMonth;
		}
		
 
	public static long getDateline(){
		return System.currentTimeMillis()/1000;
	}
	public static long getDateline(String date){
		return (long)(toDate(date, "yyyy-MM-dd").getTime()/1000);
	}
	public static long getDateHaveHour(String date){
		return (long)(toDate(date, "yyyy-MM-dd HH").getTime()/1000);
	}
	public static long getDateline(String date,String pattern){
		return (long)(toDate(date, pattern).getTime()/1000);
	}
	public static Date getCurUTCDate()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
		return calendar.getTime();
	}
	

	/**
	 2  * 将java.util.Date对象转化为String字符串
	 3  * @param date
	 4  *            要格式的java.util.Date对象
	 5  * @param strFormat
	 6  *            输出的String字符串格式的限定（如："yyyy-MM-dd HH:mm:ss"）
	 7  * @return 表示日期的字符串
	 8  */
 public static String dateToStr(Date date, String strFormat) {
		    SimpleDateFormat sf = new SimpleDateFormat(strFormat);
		    String str = sf.format(date);
		    return str; }

	/**
	 * 将String字符串转换为java.util.Date格式日期
	 * @param strDate
	 *            表示日期的字符串
	 * @param dateFormat
	 *            传入字符串的日期表示格式（如："yyyy-MM-dd HH:mm:ss"）
	 * @return java.util.Date类型日期对象（如果转换失败则返回null）
	 */
	public static Date strToUtilDate(String strDate, String dateFormat) {
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
		Date date = null;
		try {
			date = sf.parse(strDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 将java.sql.Timestamp对象转化为String字符串
	 * @param time
	 *            要格式的java.sql.Timestamp对象
	 * @param strFormat
	 *            输出的String字符串格式的限定（如："yyyy-MM-dd HH:mm:ss"）
	 * @return 表示日期的字符串
	 */
	public static String dateToStr(java.sql.Timestamp time, String strFormat) {
		DateFormat df = new SimpleDateFormat(strFormat);
		String str = df.format(time);
		return str;
	}
	/**
	 * 将String字符串转换为java.sql.Timestamp格式日期,用于数据库保存
	 * @param strDate
	 *            表示日期的字符串
	 * @param dateFormat
	 *            传入字符串的日期表示格式（如："yyyy-MM-dd HH:mm:ss"）
	 * @return java.sql.Timestamp类型日期对象（如果转换失败则返回null）
	 */
	public static java.sql.Timestamp strToSqlDate(String strDate, String dateFormat) {
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
		Date date = null;
		try {
			date = sf.parse(strDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		java.sql.Timestamp dateSQL = new java.sql.Timestamp(date.getTime());
		return dateSQL;
	}
	/**
	 * 将java.util.Date对象转化为java.sql.Timestamp对象
	 *
	 * @param date
	 *            要转化的java.util.Date对象
	 * @return 转化后的java.sql.Timestamp对象
	 */
	public static java.sql.Timestamp dateToTime(Date date) {
		return new java.sql.Timestamp(date.getTime());
	}
	/**
	 * 将java.sql.Timestamp对象转化为java.util.Date对象
	 *
	 * @param time
	 *            要转化的java.sql.Timestamp对象
	 * @return 转化后的java.util.Date对象
	 */
	public static Date timeToDate(java.sql.Timestamp time) {
		return time;
	}


	public static boolean isInFiveMinutes(Date curr_date,String dateStr){
		DateFormat df = new SimpleDateFormat("yyMMddhhmmss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date one;
		try{
			one = df.parse(dateStr);
			long time1 = one.getTime();
			long time2 = curr_date.getTime();
			long diff ;
			if(time1<time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			long minutes = diff / (1000 * 60);
			if(minutes<=5){
				return true;
			}
		}catch (Exception e){

		}
		return false;
	}

	public static void main(String args[]){
  System.out.println(isInFiveMinutes(new Date(),"190320083829"));
	}


}
