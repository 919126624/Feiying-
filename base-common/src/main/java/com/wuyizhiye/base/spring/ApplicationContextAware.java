package com.wuyizhiye.base.spring;

import org.springframework.beans.BeansException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @ClassName ApplicationContextAware
 * @Description spring上下文获取类
 * @author li.biao
 * @date 2015-4-1
 */
@Lazy(value=false)
@Component
public class ApplicationContextAware implements
		org.springframework.context.ApplicationContextAware {
	private static ApplicationContext ctx;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ApplicationContextAware.ctx = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext(){
		return ctx;
	}
}
