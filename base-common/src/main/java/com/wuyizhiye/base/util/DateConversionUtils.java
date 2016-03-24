package com.wuyizhiye.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.wuyizhiye.base.util.StringUtils;

/**
 * 这个类需要合并处理
 * @ClassName DateConversionUtils
 * @Description 完成指定日期的转换；此转换适用用于接口中；如果其他用处请使用DateUtils工具类；
 * @author li.biao
 * @date 2015-4-1
 */
public class DateConversionUtils {
	
	/**
	 * @Description 转换yyyyMMddHHmmss格式的时间字符成Date类型
	 * @param dateStr  传入一个标准的时间字符串;eg:20141107172800
	 * @return 返回一个标准的日期格式;eg:2014-11-7 17:28:00
	 * @throws ParseException
	 */
	public static Date conversionStrToDate(String dateStr)
			throws ParseException {
		Date date = null;
		if (StringUtils.isEmpty(dateStr)) {
			date = null;
		} else {
			if (dateStr.length() != 14) {// 如果不是标准的14位日期格式抛出异常
				date = null;
				throw new RuntimeException(
						"please input standard date:eg 20140101000000");
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				date = sdf.parse(dateStr);
			}
		}
		return date;
	}

	/**
	 * @Description 获取指定时间
	 * @param dTime
	 * @return
	 * @throws ParseException
	 */
	public static Date getStartDate(String dTime) throws ParseException {
		Date date = null;
		if (StringUtils.isEmpty(dTime)) {
			date = null;
		} else {
			if (dTime.length() != 8) {// 如果不是标准的8位日期格式抛出异常
				date = null;
				throw new RuntimeException(
						"please input standard date:eg 20140101");
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				date = sdf.parse(dTime);
			}
		}
		return date;
	}

	/**
	 * @Description 获取指定时间的后一天
	 * @param dTime
	 * @return
	 * @throws ParseException
	 */
	public static Date getEndDate(String dTime) throws ParseException {
		Date date = null;
		if (StringUtils.isEmpty(dTime)) {
			date = null;
		} else {
			if (dTime.length() != 8) {// 如果不是标准的8位日期格式抛出异常
				date = null;
				throw new RuntimeException(
						"please input standard date:eg 20140101");
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				date = sdf.parse(dTime);
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(date.getTime());
				calendar.add(Calendar.DATE, 1);
				date = new Date(calendar.getTimeInMillis());
//				System.out.println(sdf.format(date));
			}
		}
		return date;
	}
	
	public static void main(String[] args) throws ParseException {
		getEndDate("20141110");
	}
}
