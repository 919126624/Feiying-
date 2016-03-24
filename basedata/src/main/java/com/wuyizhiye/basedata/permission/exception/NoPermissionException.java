package com.wuyizhiye.basedata.permission.exception;

/**
 * @ClassName NoPermissionException
 * @Description TODO
 * @author li.biao
 * @date 2015-4-8
 */
public class NoPermissionException extends RuntimeException {
	private static final long serialVersionUID = 8128505227692384918L;
	public NoPermissionException(String msg){
		super(msg);
	}
}
