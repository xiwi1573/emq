package com.plant.advice;

import java.lang.reflect.Method;
import org.aopalliance.intercept.*;
import com.plant.logger.Logger;


public class LogAroundAdvice implements MethodInterceptor {

  /**
   * 日志记录器
   */
  protected static final Logger log = Logger.getLogger(LogAroundAdvice.class);

  /**
   * 构造器
   */
  public LogAroundAdvice() {
  }

  /**
   * invoke
   * @param methodInvocation MethodInvocation
   * @return Object
   * @throws Throwable
   */
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    Method method = methodInvocation.getMethod();
    log.info("开始调用方法:" + method.getName());
    Object obj = methodInvocation.proceed();
    log.info("调用方法:" + method.getName() + "结束！");
    return obj;
  }
}
