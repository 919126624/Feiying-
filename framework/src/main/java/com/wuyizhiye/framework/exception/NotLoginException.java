package com.wuyizhiye.framework.exception;

/**
 * @ClassName NotLoginException
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
public class NotLoginException extends RuntimeException {
	private static final long serialVersionUID = 3248791301850058776L;
	public NotLoginException(String msg){
		super(msg);
	}
}
