package com.wuyizhiye.basedata.redis.dao;



import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName RedisDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface RedisDao {
	/**
	 * 存map对象方法
	 * @param key
	 * @param data
	 */
	void addRedisInfo(String key,Map<String,String> data);
	
	/**
	 * json方式存储
	 * @param key
	 * @param data
	 * @param maxage
	 */
	void addRedisInfo(String key,Map<String,String> data,int maxage);
	/**
	 * 二进制方式存储
	 * @param key
	 * @param data
	 * @param maxage
	 */
	void addRedisInfo(byte[] key,Map<byte[],byte[]> data,int maxage);
	
	void addRedisInfo(byte[] key,Map<byte[],byte[]> data);
	
	void  addByteRedisInfo(String key,Object o,Class<?  extends Serializable> T);
	
	void  addByteRedisListInfo(String key,List list,Class<?  extends Serializable> T);
	/**
	 * 二进制存储field数据
	 * @param key
	 * @param field
	 * @param data
	 */
	void addRedisInfo(String key,String field,byte[] data);
	
	void addRedisInfo(String key,String field,String data);
	
	void addRedisObjectInfo(String key,String field,Object data);
	
	void addRedisListInfo(String key,String field,List data);
	
	void addRedisMapInfo(String key,Map<String,Object> data);
	
	/**
	 * 存字符串方法,值为对象
	 * @param key
	 * @param data
	 */
	void addRedisInfo(String key,Object data);
	
	void addRedisInfo(String key,Object data,int maxage);
	/**
	 * json方式取出
	 * @param key
	 * @param classary
	 * @return
	 */
	public Map<String,String> get(String key);
	
	public Map<String,Object> getRedisMapIinfo(String key,Map<String,Class> classary);
	
	public String getString(String key);
	
	public String getString(String key,String field);
	
	public void removeRedisInfo(String key,String[] fields);
	
	public void removeRedisInfo(String key);
	public boolean existRedisInfo(String key);
	
	public boolean existByteRedisInfo(String key);
	
	public void expire(String key, int maxage);
	/**
	 * 二进制方式取出
	 * @param key
	 * @return
	 */
	public Map<byte[],byte[]> getByteMap(String key);
	
	public Map<byte[],byte[]> getString(byte[] key);
	
	public byte[]  getByteArray(String key,String field);
	
	public <T extends Serializable> T getByteObject(String key,Class<T> T);
	
	public <T extends Serializable> List<T> getByteList(String key,Class<T> T);
	
	public String getByteString(String key,String field);
	
	public void removeRedisByteInfo(String key,String[] fields);
	
	public void expireByte(String key, int maxage);
	
	public Set getListKey(String key);
	
	public List getListData(String key,Class c);

	public List getByteListData(String key,String fileld,Class c);
	
	public boolean useRedis();

}
