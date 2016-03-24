package com.wuyizhiye.framework.redis;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wuyizhiye.basedata.redis.dao.RedisDao;
import com.wuyizhiye.basedata.redis.serialize.TransCoderUtil;
import com.wuyizhiye.basedata.util.RedisHolder;

/**
 * @ClassName RedisSessionUtil
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
public class RedisSessionUtil {
	private static final ThreadLocal<Map<String,Object>> ctx = new ThreadLocal<Map<String,Object>>();
	private static final ThreadLocal<String> clientPrimaryKey = new ThreadLocal<String>();
	private static final ThreadLocal<Map<String,Object>> updatectx = new ThreadLocal<Map<String,Object>>();
	private static Logger Log=Logger.getLogger(RedisSessionUtil.class); 
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
	
	public static Map<String,Object> getUpdateContext(){
		return updatectx.get();
	}
	
	public static void setUpdateContext(Map<String,Object> context){
		updatectx.set(context);
	}
	
	public static void saveContext(String primaryKey,Map<String,Object> context){
		RedisDao client = RedisHolder.getRedisClient();
		client.addRedisInfo(TransCoderUtil.serialString(primaryKey),
				LoginInfoUtil.toMap(context));
	}

	@SuppressWarnings("unchecked")
	public static Map<String,Object> getContextFromRedis(String clientPrimaryKey) {
		long begin = System.currentTimeMillis();
		RedisDao client = RedisHolder.getRedisClient();
		Map<byte[], byte[]> redisMap = client.getByteMap(clientPrimaryKey);
		Map<String,Object> ctx = LoginInfoUtil.toInfo(redisMap);
		long time = System.currentTimeMillis() - begin;
		BigDecimal second = new BigDecimal(time).divide(new BigDecimal("1000.000")).setScale(3);
		Log.error("redis取出人员信息耗时:"+second+"秒");
		return ctx;
	}
	
	public static void removeContextFromRedis(String clientPrimaryKey){
		RedisDao client = RedisHolder.getRedisClient();
		client.expireByte(clientPrimaryKey, 0);
	}
	
	/**
	 * 系统是否使用memcache
	 * @return
	 */
	public static boolean usingRedis(){
		return RedisHolder.getRedisClient().useRedis();
	}
	
	public static void saveContext(String primaryKey,Map<String,Object> context,int invalidtime){
		RedisDao client = RedisHolder.getRedisClient();
		client.addRedisInfo(TransCoderUtil.serialString(primaryKey),
				LoginInfoUtil.toMap(context),invalidtime);
	}
}
