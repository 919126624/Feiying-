package com.wuyizhiye.basedata.util;

import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.basedata.redis.dao.RedisDao;

/**
 * @ClassName RedisHolder
 * @Description redis 替代session操作类
 * @author li.biao
 * @date 2015-4-2
 */
public class RedisHolder {
	
	public static RedisDao getRedisClient(){
		return ApplicationContextAware.getApplicationContext().getBean(RedisDao.class);
	}

}
