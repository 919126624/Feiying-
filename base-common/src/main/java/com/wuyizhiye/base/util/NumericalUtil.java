package com.wuyizhiye.base.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * @ClassName NumericalUtil
 * @Description 数值型工具类
 * @author li.biao
 * @date 2015-4-1
 */
public class NumericalUtil {

	/**
	 * 格式化 钱  显示千分位出来
	 * @param data
	 * @return
	 */
	public static String formatMoney(Double data){
		if(null == data){
			return "0";
		}
		NumberFormat format = NumberFormat.getInstance();
		return format.format(data);
	}
	
	/**
	 * 格式化 钱  显示千分位出来
	 * @param data
	 * @return
	 */
	public static String formatMoney(BigDecimal data){
		
		if(null == data){
			return "0";
		}
		return formatMoney(data.doubleValue());
	}
	
	/**
	 * 格式化 钱 取掉多余的0和.
	 * @param data
	 * @return
	 */
	public static String subZeroAndDot(Double data){
		if(null == data){
			return "0";
		}
		String str = data.toString();
		if(str.indexOf(".") > 0){  
            str = str.replaceAll("0+?$", "");//去掉多余的0  
            str = str.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return str; 
	}
	
	/**
	 * 格式化 钱 取掉多余的0和.
	 * @param data
	 * @return
	 */
	public static String subZeroAndDot(BigDecimal data){
		if(null == data){
			return "0";
		}
		return subZeroAndDot(data.doubleValue());
	}
	
}
