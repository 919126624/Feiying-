package com.wuyizhiye.basedata.util;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.wuyizhiye.base.spring.ApplicationContextAware;

/**
 * @ClassName RedisUtil
 * @Description redis 工具类
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="redisUtil")
public class RedisUtil {
	private static Logger logger =Logger.getLogger(RedisUtil.class);
	    /**       
	     * 获取数据库连接        
	     * @return conn        
	     */       
	    public Jedis getConnection() {  
	       //循环三次获取
	    	Jedis jedis=null;            
	        try {   
	        	int i=0;
	        	do{        		
	            jedis=getJedisPool().getResource();   
	            i++;
	        	}while(jedis==null&&i<3);
	        } catch (Exception e) {                
	            logger.error("", e);            
	        }            
	        return jedis;        
	    }     
	      
	    /**        
	     * 关闭数据库连接        
	     * @param conn        
	     */       
	    public void closeConnection(Jedis jedis) {            
	        if (null != jedis) {                
	            try {                    
	            	getJedisPool().returnResource(jedis);                
	            } catch (Exception e) {  
	                    logger.error("", e);                
	            }            
	        }        
	    }    
	     
	    public JedisPool getJedisPool(){
	    	return ApplicationContextAware.getApplicationContext().getBean(JedisPool.class);
	    }
	    
	    public  boolean useRedis(){
	    	return ApplicationContextAware.getApplicationContext().containsBean("jedisPool");
	    }
}
