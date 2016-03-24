package com.wuyizhiye.framework.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wuyizhiye.base.util.DateUtil;


public class Constant {
	
	/**
	 * 返回结果状态KEY
	 */
	public static final String RESULT_STATUS_KEY = "STATE" ;
	
	/**
	 * 返回结果消息KEY
	 */
	public static final String RESULT_MSG_KEY = "MSG" ;
	
	/**
	 * 返回结果状态：SUCCESS
	 */
	public static final String RESULT_STATUS_SUCCESS = "SUCCESS" ;
	
	/**
	 * 返回结果状态：FAIl
	 */
	public static final String RESULT_STATUS_FAIl = "FAIl" ;

	/**
	 * 数据源参数KEY
	 */
	public static final String DATABASETYPE = "DataBaseType" ;
	
	/**
	 * 数据源:MYSQL
	 */
	public static final String DATABASETYPE_MYSQL = "MYSQL" ;
	
	/**
	 * 数据源:SQLSERVER
	 */
	public static final String DATABASETYPE_SQLSERVER = "SQLSERVER" ;
	
	/**
	 * 数据源:ORACLE
	 */
	public static final String DATABASETYPE_ORACLE = "ORACLE" ;
	
	/**
	 *  生成简易编码（前缀+当前年月日时分秒+随机长度）
	 * @param pfix 前缀
	 * @param len 尾数随机长度
	 * @return
	 */
	public static String getSimpleNumber(String pfix,int len){
		String billNumber = "";
		//生成日期前缀
		String nowDateStr = DateUtil.dateConvertStr(DateUtil.getCurDate(),DateUtil.GENERAL_FORMHMS);
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(nowDateStr);
		String dateStr = m.replaceAll("").trim();
		billNumber += dateStr ;
		return pfix + billNumber + getRandomNum(len) ;
	}
	
	/**
	 * 生成固定位随机数
	 * @param pwd_len
	 * @return
	 */
	public static String getRandomNum(int pwd_len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，
			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}
	
}
