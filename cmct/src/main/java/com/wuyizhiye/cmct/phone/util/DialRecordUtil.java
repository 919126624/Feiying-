package com.wuyizhiye.cmct.phone.util;

import java.util.Date;

import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;

/**
 * @ClassName DialRecordUtil
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public class DialRecordUtil {
	/**
	 * 得到当月表名
	 */
	public static String getSuffix(Date date){
		String suffix = "";
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date);	
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH)+1;
//		suffix += "_"+year+"_"+month;
		suffix += "_"+DateUtil.convertDateToStr(date,"yyyy_MM");
		return suffix;
	}
	
	public static String getSuffix(){
		return getSuffix(new Date());
	}
	
	/**
	 * @param showMonth 2014-12
	 * @return
	 */
	public static String getSuffix(String showMonth){
		if(StringUtils.isEmpty(showMonth)){
			return "";
		}
		return "_"+showMonth.replace("-", "_");
	}
}
