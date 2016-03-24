package com.wuyizhiye.base.util;

/**
 * @ClassName DJCons
 * @Description 辅助类，包括所有常数定义
 * @author li.biao
 * @date 2015-4-1
 */
public final class DJCons {

	/**
	 * <p>Title: DJCons.java</p>  
	 * <p>Description: 构造函数</p>
	 */
	public DJCons() {

	}

	/**
	 * 数据库类型定义 SQL SERVER
	 * */
	public static final byte DB_SQL = 0;
	
	/**
	 * 数据库类型定义 ORACLE
	 * */
	public static final byte DB_ORA = 1;
	
	/**
	 * 数据库类型定义 DB2
	 * */
	public static final byte DB_DB2 = 2;
	
	/**
	 * 数据库类型定义 MYSQL
	 * */
	public static final byte DB_MYSQL = 3;

	/**
	 * 显示格式
	 */
	public static final String YSS_DATEFORMAT = "yyyy-MM-dd";

	// 其它常量
	public static final String ERROR_TO_CLIENT = "A401";// 通过response发给客户端异常消息时用来setHeader
	public static final String CONTENT_TEXT = "text/html; charset=GBK";
	
}
