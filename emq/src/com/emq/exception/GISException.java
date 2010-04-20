package com.emq.exception;

/**
 * 系统基础的异常对象，系统中所有的新异常类应从此类继承。
 * @author guqiong
 * @created 2009-9-21 
 */
public class GISException extends Exception {
  
	private static final long serialVersionUID = -8312044984940955231L;

	public GISException() {
		super();
	}

	public GISException(String message) {
		super(message);
	}

	public GISException(String message, Throwable cause) {
		super(message, cause);
	}

	public GISException(Throwable cause) {
		super(cause);
	}
}
