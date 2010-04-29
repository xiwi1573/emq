package com.plant.logger;

import com.icss.km.base.logger.LoggerAbstract;

public class Logger extends LoggerAbstract {

  /**
   * 构造器
   * @param name String
   */
  protected Logger(String name) {
    super(name);
  }

  /**
   * 获得日志记录器
   * @param name String
   * @return Logger
   */
  public static Logger getLogger(String name) {
    return new Logger(name);
  }

  /**
   * 获得日志记录器
   * @param clazz Class
   * @return Logger
   */
  public static Logger getLogger(Class clazz) {
    return getLogger(clazz.getName());
  }

  /**
   * 覆盖父类的debug方法，当日志输出级别为info时，不记录日志debug信息
   * @param msg Object
   */
  public void debug(Object msg) {
    if (log.isDebugEnabled()) {
      super.debug(msg);
    }
  }

  public void debug(Object msg, Throwable throwable) {
    if (log.isDebugEnabled()) {
      super.debug(msg, throwable);
    }
  }
  
}
