package com.wuyizhiye.basedata.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.Logger;

import com.danga.MemCached.MemCachedClient;
import com.wuyizhiye.base.spring.ApplicationContextAware;

/**
 * @ClassName MemcachedUtils
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class MemcachedUtils {
	public static final String CLIENT_PRIMARY_KEY = "_MEMCACHED__CLIENT_PRIMARY_KEY";
	private static final ThreadLocal<Map<String,Object>> ctx = new ThreadLocal<Map<String,Object>>();
	private static final ThreadLocal<String> clientPrimaryKey = new ThreadLocal<String>();
	private static final int MAX_AGE = 30;
	private static Logger Log=Logger.getLogger(MemcachedUtils.class); 
	public static MemCachedClient getClientInstance(){
		MemCachedClient client = ApplicationContextAware.getApplicationContext().getBean(MemCachedClient.class);
		return client;
	}
	
	public static Map<String,Object> getContext(){
		return ctx.get();
	}
	
	public static String getClientPrimaryKey(){
		return clientPrimaryKey.get();
	}
	
	public static void setClientPrimaryKey(String primaryKey){
		clientPrimaryKey.set(primaryKey);
	}
	
	public static void setContext(Map<String,Object> context){
		ctx.set(context);
	}
	
	public static void saveContext(String primaryKey,Map<String,Object> context){
		MemCachedClient client = MemcachedUtils.getClientInstance();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, MAX_AGE);//默认有效期为30分钟
		client.set(primaryKey, context, cal.getTime());
	}

	@SuppressWarnings("unchecked")
	public static Map<String,Object> getContextFromMemcache(String clientPrimaryKey) {
		long begin = System.currentTimeMillis();
		MemCachedClient client = MemcachedUtils.getClientInstance();
		Map<String,Object> ctx = (Map<String,Object>) client.get(clientPrimaryKey);
		Long time = System.currentTimeMillis() - begin;
		BigDecimal second = new BigDecimal(time).divide(new BigDecimal("1000.000")).setScale(3);
		Log.error("memcache取出人员信息耗时:"+second+"秒");
		return ctx;
	}
	
	public static void removeContextFromMemcache(String clientPrimaryKey){
		MemCachedClient client = MemcachedUtils.getClientInstance();
		client.delete(clientPrimaryKey);
	}
	
	/**
	 * 系统是否使用memcache
	 * @return
	 */
	public static boolean usingMemcache(){
		return ApplicationContextAware.getApplicationContext().containsBean("memcachedClient");
	}
	
}
