package com.wuyizhiye.base.exceptions;

/**
 * @ClassName LicenseExceedException
 * @Description license 超出限制异常
 * @author li.biao
 * @date 2015-4-1
 */
public class LicenseExceedException extends RuntimeException {
	public LicenseExceedException(String string) {
		super(string);
	}

	private static final long serialVersionUID = -4192799515291274424L;

}
