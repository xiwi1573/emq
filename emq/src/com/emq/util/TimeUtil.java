package com.emq.util;

import java.text.*;
import java.util.*;
import com.emq.exception.UtilException;

public class TimeUtil {

  /**
   * 将Date类型日期转化成String类型"任意"格式
   * java.sql.Date,java.sql.Timestamp类型是java.util.Date类型的子类
   * @param date Date
   * @param format String
   *               "2003-01-01"格式
   *               "yyyy年M月d日"
   *               "yyyy-MM-dd HH:mm:ss"格式
   * @return String
   */
  public static String dateToString(java.util.Date date, String format) {
    if (date == null || format == null) {
      return null;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    String str = sdf.format(date);
    return str;
  }

  /**
   * 将String类型日期转化成java.utl.Date类型"2003-01-01"格式
   * @param str String String类型日期
   * @param format String 时间格式
   * @return Date
   * @throws UtilException
   */
  public static java.util.Date stringToUtilDate(String str, String format) throws
          UtilException {
    if (str == null || format == null) {
      return null;
    }

    SimpleDateFormat sdf = new SimpleDateFormat(format);
    java.util.Date date = null;
    try {
      date = sdf.parse(str);
    } catch (ParseException pe) {
      throw new UtilException(pe.getMessage());
    }
    return date;
  }

  /**
   * 将String类型日期转化成java.sql.Date类型"2003-01-01"格式
   * @param str String String类型日期
   * @param format String 时间格式
   * @return Date
   * @throws UtilException
   */
  public static java.sql.Date stringToSqlDate(String str, String format) throws
          UtilException {
    if (str == null || format == null) {
      return null;
    }

    SimpleDateFormat sdf = new SimpleDateFormat(format);
    java.util.Date date = null;
    try {
      date = sdf.parse(str);
    } catch (ParseException pe) {
      throw new UtilException(pe.getMessage());
    }
    return new java.sql.Date(date.getTime());
  }

  /**
   * 将String类型日期转化成java.sql.Date类型"2003-01-01"格式
   * @param str String String类型日期
   * @param format String 时间格式
   * @return Timestamp
   * @throws UtilException
   */
  public static java.sql.Timestamp stringToTimestamp(String str, String format) throws
          UtilException {
    if (str == null || format == null) {
      return null;
    }

    SimpleDateFormat sdf = new SimpleDateFormat(format);
    java.util.Date date = null;
    try {
      date = sdf.parse(str);
    } catch (ParseException pe) {
      throw new UtilException(pe.getMessage());
    }
    return new java.sql.Timestamp(date.getTime());
  }

  /**
   * 将java.util.Date日期转化成java.sql.Date类型
   * @param date Date
   * @return 格式化后的java.sql.Date
   */
  public static java.sql.Date toSqlDate(Date date) {
    if (date == null) {
      return null;
    }
    return new java.sql.Date(date.getTime());
  }

  /**
   * 将时间字符串转化为时间格式 string to string
   * @param str String String类型日期
   * @param oldformat String
   * @param newformat String
   * @return String
   * @throws UtilException
   */
  public static String toDateString(String str, String oldformat,
                                    String newformat) throws UtilException {
    return dateToString(stringToUtilDate(str, oldformat), newformat);
  }

  /**
   * 将日历转化为日期
   * @param calendar Calendar
   * @return Date
   */
  public static java.util.Date converToDate(java.util.Calendar calendar) {
    return calendar.getTime();
  }

  /**
   * 将日期转化为日历
   * @param date Date
   * @return Calendar
   */
  public static java.util.Calendar converToCalendar(java.util.Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar;
  }

  /**
   * 求得从某天开始，过了几年几月几日几时几分几秒后，日期是多少
   * 几年几月几日几时几分几秒可以为负数
   * @param date Date
   * @param year int
   * @param month int
   * @param day int
   * @param hour int
   * @param min int
   * @param sec int
   * @return Date
   */
  public static java.util.Date modifyDate(java.util.Date date, int year,
                                          int month, int day, int hour,
                                          int min, int sec) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.YEAR, year);
    cal.add(Calendar.MONTH, month);
    cal.add(Calendar.DATE, day);
    cal.add(Calendar.HOUR, hour);
    cal.add(Calendar.MINUTE, min);
    cal.add(Calendar.SECOND, sec);

    return cal.getTime();

  }

  /**
   * 取得当前日期时间
   * @param i int
   *        1:year
   *        2:month
   *        3:day
   * @return int
   */
  public static int getCurTime(int i) {
    if (i == 1) {
      return java.util.Calendar.getInstance().get(Calendar.YEAR);
    } else if (i == 2) {
      return java.util.Calendar.getInstance().get(Calendar.MONTH) + 1;
    } else if (i == 3) {
      return java.util.Calendar.getInstance().get(Calendar.DATE);
    }
    return 0;
  }

  /**
   * 判断String类型日期是否在当前时间之后
   * @param time String String类型日期
   * @param format String 时间格式
   * @return boolean
   * @throws UtilException
   */
  public static boolean isAfterCurrentTime(String time, String format) throws
          UtilException {
    java.util.Date date = stringToUtilDate(time, format);
    if (new java.util.Date().after(date)) {
      return false;
    }
    return true;
  }

  /**
   * 获取某年某月的天数，如获取2006年2月份天数
   * @param yy String 年
   * @param mm String 月
   * @return int
   */
  public static int getMonthDays(String yy, String mm) {
    Calendar cal = Calendar.getInstance();
    int y = Integer.parseInt(yy);
    int m = Integer.parseInt(mm);
    cal.set(y, m - 1, 1);
    return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  /**
   * 获取当前月份前推interval个月
   * @param year int 年
   * @param month int 月
   * @param interval int 间隔
   * @return List
   */
  public static List getTimeObjectList(int year, int month, int interval) {
    List timeObjectList = new ArrayList();
    if (interval > month) { // 月份大于间隔
      for (int i = 12 - (interval - month) + 1; i <= 12; i++) {
        TimeObject obj = new TimeObject();
        obj.setYear(String.valueOf(year - 1));
        obj.setMonth(String.valueOf(i));
        timeObjectList.add(obj);
      }

      for (int i = 1; i <= month; i++) {
        TimeObject obj = new TimeObject();
        obj.setYear(String.valueOf(year));
        obj.setMonth(String.valueOf(i));
        timeObjectList.add(obj);
      }
    } else { // 间隔大于月份
      for (int i = (month - interval + 1); i <= month; i++) {
        TimeObject obj = new TimeObject();
        obj.setYear(String.valueOf(year));
        obj.setMonth(String.valueOf(i));
        timeObjectList.add(obj);
      }
    }
    return timeObjectList;
  }

  /**
   * 根据月份获取季度值
   * @param month int
   * @return int
   * @throws UtilException
   */
  public static int getQuarter(int month) throws UtilException {
    if (month <= 0 && month > 12)
      throw new UtilException("请传入正确的月份值，正确的月份值为:1-12，当前月份参数值为:" + month);
    if (month > 0 && month < 4)
      return 1;
    else if (month > 3 && month < 7)
      return 2;
    else if (month > 6 && month < 10)
      return 3;
    else
      return 4;
  }

  /**
   * 创建年时间格式，如2006
   * @param year String
   * @return String
   */
  public static String createYear(String year) {
    return createTime(year, "", "", 1);
  }

  /**
   * 创建年月时间格式，如:2006-12
   * @param year String
   * @param month String
   * @return String
   */
  public static String createYearMonth(String year, String month) {
    return createTime(year, month, "", 2);
  }

  /**
   * 创建年月日时间格式，如:2006-06-01
   * @param year String
   * @param month String
   * @param day String
   * @return String
   */
  public static String createYearMonthDay(String year, String month, String day) {
    return createTime(year, month, day, 3);
  }

  /**
   * 根据参数构造时间字符串
   * @param year String 年
   * @param month String 月
   * @param day String 日
   * @param type int 1:年 2:年月 3:年月日
   * @return String
   */
  public static String createTime(String year, String month, String day,
                                  int type) {
    String time = "";
    if (month != null && !"".equals(month) && Integer.parseInt(month) < 10 &&
        !month.startsWith("0")) {
      month = "0" + month;
    }
    if (day != null && !"".equals(day) && Integer.parseInt(day) < 10 &&
        !day.startsWith("0"))
      day = "0" + day;
    switch (type) {
    case 1: { // 年
      time = year;
      break;
    }
    case 2: { // 月
      time = year + "-" + month;
      break;
    }
    case 3: { // 日
      time = year + "-" + month + "-" + day;
      break;
    }
    default: {
    }
    }
    return time;
  }

  /**
   * 获取趋势图展现标题
   * @param timeType String 时间类型，枚举（year:年;month:月）
   * @param startY String 起始年
   * @param startM String 起始月
   * @param endY String 结束年
   * @param endM String 结束月
   * @return String
   */
  public static String createTimeTitle(String timeType, String startY, String startM,
                                String endY, String endM) {
    StringBuffer timeTitle = new StringBuffer();
    if (timeType.equals("year")) { // 年
      if (Integer.parseInt(startY) == Integer.parseInt(endY)) {
        timeTitle.append(startY).append("年");
      } else {
        timeTitle.append(startY).append("年");
        timeTitle.append("-");
        timeTitle.append(endY).append("年");
      }
      return timeTitle.toString();
    } else if (timeType.equals("month")) { // 月
      if (Integer.parseInt(startY) == Integer.parseInt(endY)) {
        timeTitle.append(startY).append("年");
        timeTitle.append(startM).append("月");
        timeTitle.append("-");
        timeTitle.append(endM).append("月");
      } else {
        timeTitle.append(startY).append("年");
        timeTitle.append(startM).append("月");
        timeTitle.append("-");
        timeTitle.append(endY).append("年");
        timeTitle.append(endM).append("月");
      }
      return timeTitle.toString();
    }
    return "";
  }

  public static void main(String[] args) {
    String time = "2005-12-30 00:00:00.00000";
    String format = "yyyy-MM-dd hh:mm:ss";
    System.out.println(TimeUtil.dateToString(new java.util.Date(), format));
  }
}
