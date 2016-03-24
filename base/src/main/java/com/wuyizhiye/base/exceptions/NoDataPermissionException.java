package com.wuyizhiye.base.exceptions;

/**
 * @ClassName NoDataPermissionException
 * @Description 描述：无数据权限异常
 * @author li.biao
 * @date 2015-4-1
 */
public class NoDataPermissionException extends RuntimeException {
	public NoDataPermissionException(String string) {
		super(string);
	}

	private static final long serialVersionUID = -4192799515291274424L;

}
