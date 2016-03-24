package com.wuyizhiye.base.exceptions;

/**
 * @ClassName WrongReqException
 * @Description license 异常
 * @author li.biao
 * @date 2015-4-1
 */
public class WrongReqException extends RuntimeException {
	public WrongReqException(String string) {
		super(string);
	}

	private static final long serialVersionUID = -4192799515291274424L;

}
