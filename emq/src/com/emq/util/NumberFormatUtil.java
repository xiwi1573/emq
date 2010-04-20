package com.emq.util;

/**
 * 数据格式化
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 中软国际（昆明）信息技术有限公司</p>
 *
 * @author chenzhg
 * @version 1.0
 */
import java.text.*;
import java.util.*;
import java.math.*;

import org.apache.log4j.*;

public class NumberFormatUtil {

  private static Logger log = Logger.getLogger(NumberFormatUtil.class);

  public NumberFormatUtil() {
  }

  /**
   * 将给定的数字按给定的形式输出
   * @param d double
   * @param pattern String
   *       #:表示有数字则输出数字，没有则空，如果输出位数多于＃的位数，
   *          则超长输入
   *       0:有数字则输出数字，没有补0
   *          对于小数，有几个＃或0，就保留几位的小数；
   *       例如：  "###.00" -->表示输出的数值保留两位小数，不足两位的
   *                          补0，多于两位的四舍五入
   *              "###.0#" -->表示输出的数值可以保留一位或两位小数；
   *                               整数显示为有一位小数，一位或两位小数
   *                               的按原样显示，多于两位的四舍五入；
   *              "###" --->表示为整数，小数部分四舍五入
   *              ".###" -->12.234显示为.234
   *              "#,###.0#"  -->表示整数每隔3位加一个"，";
   * @param l Locale
   * @return String
   */
  public static String formatNumber(double d, String pattern, Locale l) {
    String s = "";
    try {
      DecimalFormat nf = (DecimalFormat) NumberFormat.getInstance(l);
      nf.applyPattern(pattern);
      s = nf.format(d);

    } catch (Exception e) {
      e.printStackTrace();
      log.debug(" formatNumber is error!");
    }
    return s;

  }

  /**
   * 按缺省的区域输出数字形式
   * @param d double
   * @param pattern String
   * @return String
   */
  public static String formatNumber(double d, String pattern) {
    return formatNumber(d, pattern, Locale.getDefault());
  }

  /**
   * 格式化货币
   * @param d double
   * @param pattern String
   *        "\u00A4#,###.00" :显示为 ￥1，234，234.10
   * @param l Locale
   * @return String
   */
  public static String formatCurrency(double d, String pattern, Locale l) {
    String s = "";
    try {
      DecimalFormat nf = (DecimalFormat) NumberFormat.getCurrencyInstance(l);
      nf.applyPattern(pattern);
      s = nf.format(d);

    } catch (Exception e) {
      e.printStackTrace();
      log.debug(" formatNumber is error!");
    }
    return s;

  }

  /**
   * 使用默认区域的指定方式显示货币
   * @param d double
   * @param pattern String
   * @return String
   */
  public static String formatCurrency(double d, String pattern) {
    return formatCurrency(d, pattern, Locale.getDefault());
  }

  /**
   * 使用默认方式显示货币：
   *     例如:￥12,345.46  默认保留2位小数，四舍五入
   * @param d double
   * @return String
   */
  public static String formatCurrency(double d) {
    String s = "";
    try {
      DecimalFormat nf = (DecimalFormat) NumberFormat.getCurrencyInstance();
      s = nf.format(d);

    } catch (Exception e) {
      e.printStackTrace();
      log.debug(" formatNumber is error!");
    }
    return s;

  }

  /**
   * 按指定区域格式化百分数
   * @param d double
   * @param pattern String "##,##.000%"-->不要忘记“%”
   * @param l Locale
   * @return String
   */
  public static String formatPercent(double d, String pattern, Locale l) {
    String s = "";
    try {
      DecimalFormat df = (DecimalFormat) NumberFormat.getPercentInstance(l);
      df.applyPattern(pattern);
      s = df.format(d);
    } catch (Exception e) {
      log.debug("formatPercent is error!");
      log.debug(e);
    }
    return s;
  }

  /**
   * 使用默认区域格式化百分数
   * @param d double
   * @param pattern String
   * @return String
   */
  public static String formatPercent(double d, String pattern) {
    return formatPercent(d, pattern, Locale.getDefault());
  }

  /**
   * 格式化百分数
   * @param d double
   * @return String
   */
  public static String formatPercent(double d) {
    String s = "";
    try {
      DecimalFormat df = (DecimalFormat) NumberFormat.getPercentInstance();
      s = df.format(d);
    } catch (Exception e) {
      log.debug("formatPercent is error!");
      log.debug(e);
    }
    return s;
  }

  /**
   * 输出数字的格式,如:1,234,567.89
   * @param bd BigDecimal 要格式华的数字
   * @param format String 格式 "###,##0"
   * @return String
   */
  public static String numberFormat(BigDecimal bd, String format) {

    if (bd == null ||"0".equals(bd.toString())) {
      return "";
    }
    //System.out.println("format = " + format);
    if(format.indexOf(".")!=-1){
      int index = format.indexOf(".");
      String sign = format.substring(index-1,index);
      String prefix = format.substring(0,index-1);
      String suffix = format.substring(index+1,format.length());
      if(!sign.equals("0")){
        sign = "0";
        format = prefix + sign + "." + suffix;
      }
    }
    //System.out.println("format = " + format);


    DecimalFormat bf = new DecimalFormat(format);
    return bf.format(bd);
  }

  /**
   * 格式化BigDecimal
   * @param bd BigDecimal
   * @param format String
   * @return BigDecimal
   */
  public static BigDecimal formatBigDecimal(BigDecimal bd, String format) {
    if (bd == null || "0".equals(bd.toString())) {
      return new BigDecimal(0.00);
    }

    String var = numberFormat(bd, format);
    return new BigDecimal(var);
  }


  public static void main(String[] args) {
    BigDecimal value = new BigDecimal("0.456");
    String s = numberFormat(value, "###.00");
    System.out.println(s);
  }

}
