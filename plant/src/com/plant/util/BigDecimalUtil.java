package com.plant.util;

import java.math.BigDecimal;

/**
 * <p>Title: java.math.BigDecimal工具类</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 中软国际（昆明）信息技术有限公司</p>
 * @author fengr
 * @version 1.0
 */
public class BigDecimalUtil {

  public BigDecimalUtil() {
  }

  /**
   * 加法
   * @param first BigDecimal
   * @param last BigDecimal
   * @return BigDecimal
   */
  public static BigDecimal add(BigDecimal first, BigDecimal last) {
    return add(first, last, "##.00");
  }

  /**
   * 格式化加法
   * @param first BigDecimal
   * @param last BigDecimal
   * @param format String ##.00 两位小数，不足补零
   * @return BigDecimal
   */
  public static BigDecimal add(BigDecimal first, BigDecimal last, String format) {
    BigDecimal result = new BigDecimal("0.00");
    if (first == null) {
      first = new BigDecimal("0.00");
    }
    if (last == null) {
      last = new BigDecimal("0.00");
    }
    result = first.add(last);
    result = new BigDecimal(NumberFormatUtil.numberFormat(result, format));
    return result;
  }


  /**
   * 减法
   * @param first BigDecimal
   * @param last BigDecimal
   * @return BigDecimal
   */
  public static BigDecimal subtract(BigDecimal first, BigDecimal last) {
    return subtract(first, last, "##.00");
  }

  /**
   * 格式化减法
   * @param first BigDecimal
   * @param last BigDecimal
   * @param format String ##.00 两位小数，不足补零
   * @return BigDecimal
   */
  public static BigDecimal subtract(BigDecimal first, BigDecimal last,
                                    String format) {
    BigDecimal result = new BigDecimal("0.00");
    if (first == null) {
      first = new BigDecimal("0.00");
    }
    if (last == null) {
      last = new BigDecimal("0.00");
    }
    result = first.subtract(last);
    result = new BigDecimal(NumberFormatUtil.numberFormat(result, format));
    return result;
  }

  /**
   * 乘法
   * @param first BigDecimal
   * @param last BigDecimal
   * @return BigDecimal
   */
  public static BigDecimal multiply(BigDecimal first, BigDecimal last) {
    return multiply(first, last, "##.00");
  }

  /**
   * 格式化乘法
   * @param first BigDecimal
   * @param last BigDecimal
   * @param format String ##.00 两位小数，不足补零
   * @return BigDecimal
   */
  public static BigDecimal multiply(BigDecimal first, BigDecimal last,
                                    String format) {
    BigDecimal result = new BigDecimal("0.00");
    if (first == null) {
      first = new BigDecimal("0.00");
    }
    if (last == null) {
      last = new BigDecimal("0.00");
    }
    result = first.multiply(last);
    result = new BigDecimal(NumberFormatUtil.numberFormat(result, format));
    return result;
  }


  /**
   * 除法
   * @param first BigDecimal
   * @param last BigDecimal
   * @param scale int
   * @param roundingMode int
   * @return BigDecimal
   */
  public static BigDecimal divide(BigDecimal first, BigDecimal last, int scale,
                                  int roundingMode) {
    BigDecimal result = new BigDecimal("0.00");
    if (first == null) {
      first = new BigDecimal("0.00");
    }
    if (last == null) {
      last = new BigDecimal("0.00");
    }

    if (first.floatValue() == 0 && last.floatValue() == 0) {
      result = new BigDecimal("0.00");
    }

    if (first.floatValue() != 0 && last.floatValue() == 0) {
      result = new BigDecimal("1.00");
    }

    if (last.floatValue() != 0) {
      result = first.divide(last, scale, roundingMode);
    }
    return result;
  }

  /**
   * 计算百分率，不含%号
   * @param dividend BigDecimal 被除数
   * @param divisor BigDecimal 除数
   * @param scale int 小数点位数
   * @param roundingMode int
   * @return BigDecimal
   */
  public static BigDecimal getPercent(BigDecimal dividend, BigDecimal divisor,
                                      int scale, int roundingMode) {
    BigDecimal result = new BigDecimal("0.00");
    if (divisor == null || divisor.doubleValue() == 0.00)
      return result;
    result = multiply(divide(dividend, divisor, scale + 2, roundingMode), new BigDecimal(100));
    return result;
  }

  /**
   * 字符串校验
   * @param value BigDecimal
   * @return boolean
   */
  public static boolean isAvalid(BigDecimal value) {
    String t_value = String.valueOf(value);
    if (t_value == null || t_value.equals("") || t_value.equals("null")) {
      return false;
    } else {
      return true;
    }
  }

}
