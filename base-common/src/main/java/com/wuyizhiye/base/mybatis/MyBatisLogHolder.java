package com.wuyizhiye.base.mybatis;

import org.apache.ibatis.logging.LogFactory;

/**
 * @ClassName MyBatisLogHolder
 * @Description mybatis日志工具
 * @author li.biao
 * @date 2015-4-1
 */
public class MyBatisLogHolder {
	
	/**
	 * 使用log4j
	 */
	public void useLog4JLogger(){
		LogFactory.useLog4JLogging();
	}
}
