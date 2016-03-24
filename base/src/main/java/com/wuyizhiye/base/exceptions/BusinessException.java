package com.wuyizhiye.base.exceptions;

/**
 * @ClassName BusinessException
 * @Description 业务 异常
 * @author li.biao
 * @date 2015-4-1
 */
public class BusinessException extends RuntimeException {
	
	private static final long serialVersionUID = -4167599513341274424L;
	
	public BusinessException(String string) {
		super(string);
	}
	
}
