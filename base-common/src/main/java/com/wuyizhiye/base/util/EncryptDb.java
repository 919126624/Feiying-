package com.wuyizhiye.base.util;

import org.apache.log4j.Logger;

/**
 * 数据源  加密 
 * @author Administrator
 *
 */
public class EncryptDb {
	private static Logger logger=Logger.getLogger(EncryptDb.class);
	
	public static void main(String[] args) {
		try {
			String test = com.alibaba.druid.filter.config.ConfigTools.encrypt("jdbc:mysql://dev:3306/ebsite_test?useUnicode=true&amp;characterEncoding=utf8");
//			System.out.println("===================");
//			System.out.println(test);
//			System.out.println("===================");
		} catch (Exception e) {
//			e.printStackTrace();
			logger.error("",e);
		}
	}

}
