package com.wuyizhiye.basedata.redis.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.redis.serialize.TransCoderUtil;
import com.wuyizhiye.basedata.util.RedisUtil;

/**
 * @ClassName RedisDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="redisDao")
public class RedisDaoImpl  implements
		RedisDao {

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public void addRedisInfo(String key, Map<String, String> data) {
		addRedisInfo(key,data,-1);
	}

	@Override
	public Map<String, String> get(String key) {
		if(!redisUtil.useRedis()) return new HashMap<String,String>();
		Jedis jedis = redisUtil.getConnection();	
		Map<String, String> result= jedis.hgetAll(key);
		redisUtil.closeConnection(jedis);
		return result;
	}
	
	@Override
	public void addRedisInfo(String key,Object obj){
		this.addRedisInfo(key,obj,-1);
	}
	@Override
	public void addRedisInfo(String key,Object obj,int maxage){
		if(!redisUtil.useRedis()) return;
		Jedis jedis = redisUtil.getConnection();
		String data = "";
		if(JSONUtils.isArray(obj.getClass())){
			data = JSONArray.fromObject(obj).toString();	
		}else if(JSONUtils.isObject(obj)){
			data = JSONObject.fromObject(obj).toString();	
		}else{
			data = obj.toString();
		}
		jedis.set(key, data);
		if(maxage>0)
			jedis.expire(key, maxage*60);	
		redisUtil.closeConnection(jedis);
	}
	
	@Override
	public String getString(String key) {
		if(!redisUtil.useRedis()) return null;
		Jedis jedis = redisUtil.getConnection();
		String result =  jedis.get(key);
		redisUtil.closeConnection(jedis);
		return result;
	}

	@Override
	public void addRedisInfo(String key, String field, String data) {
		if(!redisUtil.useRedis()) return;
		Jedis jedis = redisUtil.getConnection();
		jedis.hset(key, field, data);
		redisUtil.closeConnection(jedis);
	}

	@Override
	public String getString(String key, String field) {
		if(!redisUtil.useRedis()) return null;
		Jedis jedis = redisUtil.getConnection();
		String result =   jedis.hget(key, field);
		redisUtil.closeConnection(jedis);
		return result;
	}

	@Override
	public void removeRedisInfo(String key,String[] fields) {
		if(!redisUtil.useRedis()) return ;
		Jedis jedis = redisUtil.getConnection();
		jedis.hdel(key, fields);
		redisUtil.closeConnection(jedis);
	}

	@Override
	public boolean existRedisInfo(String key) {
		if(!redisUtil.useRedis()) return false;
		Jedis jedis = redisUtil.getConnection();	
		boolean b = jedis.exists(key);
		redisUtil.closeConnection(jedis);
		return b;
	}

	@Override
	public void removeRedisInfo(String key) {
		if(!redisUtil.useRedis()) return ;
		Jedis jedis = redisUtil.getConnection();
		jedis.del(key);
		redisUtil.closeConnection(jedis);
	}
	@Override
	public void addRedisInfo(String key, Map<String, String> data, int maxage) {
		if(!redisUtil.useRedis()) return ;
		
		Jedis jedis = redisUtil.getConnection();
		jedis.hmset(key, data);
		if(maxage>0)
			jedis.expire(key, maxage*60);	
		redisUtil.closeConnection(jedis);
	}

	@Override
	public void addRedisObjectInfo(String key, String field, Object data) {
		if(!redisUtil.useRedis()) return ;
		
		Jedis jedis = redisUtil.getConnection();
		jedis.hset(key, field, JSONObject.fromObject(data).toString());
		redisUtil.closeConnection(jedis);
	}

	@Override
	public void addRedisListInfo(String key, String field, List data) {
		if(!redisUtil.useRedis()) return ;
		
		Jedis jedis = redisUtil.getConnection();
		jedis.hset(key, field, JSONArray.fromObject(data).toString());	
		redisUtil.closeConnection(jedis);
	}

	@Override
	public void addRedisMapInfo(String key, Map<String, Object> data) {
		if(!redisUtil.useRedis()) return ;
		Jedis jedis = redisUtil.getConnection();
		Set<String> keyset = data.keySet();
		Iterator<String> ite = keyset.iterator();
		while(ite.hasNext()){
			String field = ite.next();
			Object obj = data.get(field);
			if(null==obj) continue;
			if(obj.getClass().equals(String.class)){
				jedis.hset(key, field,obj.toString());
			}else if(JSONUtils.isArray(obj.getClass())){
				jedis.hset(key, field, JSONArray.fromObject(obj).toString());	
			}else{
				jedis.hset(key, field, JSONObject.fromObject(obj).toString());	
			}
		}
		redisUtil.closeConnection(jedis);
	}

	@Override
	public Map<String, Object> getRedisMapIinfo(String key,Map<String,Class> classary) {
		if(!redisUtil.useRedis()) return new HashMap<String, Object>();
		Jedis jedis = redisUtil.getConnection();	
		Map<String, Object> result = new HashMap<String,Object>();
		Map<String, String> data =  jedis.hgetAll(key);
		Set<String> keyset = data.keySet();
		Iterator<String> ite = keyset.iterator();
		while(ite.hasNext()){
			String field = ite.next();
			String jsondata = data.get(field);
			if(jsondata.startsWith("[")){
				result.put(field, JSONArray.toCollection(JSONArray.fromObject(jsondata),classary.get(field)));
			}else if(jsondata.startsWith("{")){
				result.put(field, JSONObject.toBean(JSONObject.fromObject(jsondata),classary.get(field)));
			}else{
				result.put(field,jsondata);
			}
		}
		redisUtil.closeConnection(jedis);
		return result;
	}

	@Override
	public void expire(String key, int maxage) {
		if(!redisUtil.useRedis()) return;
		Jedis jedis = redisUtil.getConnection();	
		jedis.expire(key, maxage*60);	
		redisUtil.closeConnection(jedis);
	}

	@Override
	public void addRedisInfo(byte[] key, Map<byte[], byte[]> data, int maxage) {
		if(!redisUtil.useRedis()) return;
		Jedis jedis = redisUtil.getConnection();
		jedis.hmset(key, data);
		if(maxage>0)
			jedis.expire(key, maxage*60);	
		redisUtil.closeConnection(jedis);
		
	}
	@Override
	public  void  addByteRedisInfo(String key,Object o,Class<? extends Serializable> T){
		if(!redisUtil.useRedis()) return;
		Jedis jedis = redisUtil.getConnection();
		jedis.set(TransCoderUtil.serialString(key), TransCoderUtil.serial(o, T));
		redisUtil.closeConnection(jedis);
	}
	@Override
	public  void addByteRedisListInfo(String key,List list,Class<?  extends Serializable> T){
		if(!redisUtil.useRedis()) return;
		Jedis jedis = redisUtil.getConnection();
		jedis.set(TransCoderUtil.serialString(key), TransCoderUtil.serial(list,T,true));
		redisUtil.closeConnection(jedis);
	}
	
	@Override
	public Map<byte[], byte[]> getString(byte[] key) {
		if(!redisUtil.useRedis()) return new HashMap<byte[], byte[]>();
		Jedis jedis = redisUtil.getConnection();
		Map<byte[], byte[]> result = jedis.hgetAll(key);
		redisUtil.closeConnection(jedis);
		return result;
	}

	@Override
	public Map<byte[], byte[]> getByteMap(String key) {
		byte[] keyby = TransCoderUtil.serialString(key);	
		return getString(keyby);
	}
	
	
	public <T extends Serializable> T getByteObject(String key,String field,Class<T> T){
		if(!redisUtil.useRedis()) return null;
		byte[] keyby = TransCoderUtil.serialString(key);	
		Jedis jedis = redisUtil.getConnection();
		byte[] valueby = jedis.hget(keyby, TransCoderUtil.serialString(field));
		T tt = TransCoderUtil.deserialize(valueby, T);
		redisUtil.closeConnection(jedis);	
		return tt;
	}
	
	@Override
	public void removeRedisByteInfo(String key, String[] fields) {
		if(!redisUtil.useRedis()) return;
		Jedis jedis = redisUtil.getConnection();
		for(int i=0;i<fields.length;i++){
			jedis.hdel( TransCoderUtil.serialString(key), TransCoderUtil.serialString(fields[i]));
		}	
		redisUtil.closeConnection(jedis);
	}

	@Override
	public void expireByte(String key, int maxage) {
		if(!redisUtil.useRedis()) return;
		if(StringUtils.isEmpty(key)) return;
		byte[] keyby = TransCoderUtil.serialString(key);	
		Jedis jedis = redisUtil.getConnection();
		jedis.expire(keyby, maxage*60);
		redisUtil.closeConnection(jedis);	
	}

	@Override
	public void addRedisInfo(String key, String field, byte[] data) {
		if(!redisUtil.useRedis()) return;
		byte[] keyby = TransCoderUtil.serialString(key);	
		byte[] fieldby = TransCoderUtil.serialString(field);	
		Jedis jedis = redisUtil.getConnection();
		jedis.hset(keyby, fieldby, data);
		redisUtil.closeConnection(jedis);	
		
	}

	@Override
	public void addRedisInfo(byte[] key, Map<byte[], byte[]> data) {	
		addRedisInfo(key,data,-1);
	}

	@Override
	public String getByteString(String key, String field) {
		if(!redisUtil.useRedis()) return null;
		byte[] keyby = TransCoderUtil.serialString(key);	
		byte[] fieldby = TransCoderUtil.serialString(field);	
		Jedis jedis = redisUtil.getConnection();
		byte[]  valueby = jedis.hget(keyby, fieldby);
		String res = TransCoderUtil.deserializeString(valueby);
		redisUtil.closeConnection(jedis);	
		return res;
	}
	@Override
	public byte[]  getByteArray(String key,String field){
		if(!redisUtil.useRedis()) return null;
		byte[] keyby = TransCoderUtil.serialString(key);	
		byte[] fieldby = TransCoderUtil.serialString(field);	
		Jedis jedis = redisUtil.getConnection();
		byte[]  valueby = jedis.hget(keyby, fieldby);
		redisUtil.closeConnection(jedis);	
		return valueby;
	}
	@Override
	public <T extends Serializable> T getByteObject(String key, Class<T> T) {
		if(!redisUtil.useRedis()) return null;
		byte[] keyby = TransCoderUtil.serialString(key);	
		Jedis jedis = redisUtil.getConnection();
		byte[] valueby = jedis.get(keyby);
		T tt = TransCoderUtil.deserialize(valueby, T);
		redisUtil.closeConnection(jedis);	
		return tt;
	}
	
	@Override
	public <T extends Serializable> List<T> getByteList(String key,Class<T> T){
		if(!redisUtil.useRedis()) return null;
		byte[] keyby = TransCoderUtil.serialString(key);	
		Jedis jedis = redisUtil.getConnection();
		byte[] valueby = jedis.get(keyby);
		List<T> tt = TransCoderUtil.deserializelist(valueby, T);
		redisUtil.closeConnection(jedis);	
		return tt;
	}
	
	@Override
	public boolean useRedis() {
		return redisUtil.useRedis();
	}

	@Override
	public boolean existByteRedisInfo(String key) {
		if(!redisUtil.useRedis()) return false;
		Jedis jedis = redisUtil.getConnection();	
		boolean b = jedis.exists(TransCoderUtil.serialString(key));
		redisUtil.closeConnection(jedis);
		return b;
	}

	@Override
	public Set getListKey(String key) {
		if(!redisUtil.useRedis()) return null;
		Jedis jedis = redisUtil.getConnection();	
		Set<String> setlist = jedis.keys(key);
		redisUtil.closeConnection(jedis);
		return setlist;
	}

	@Override
	public List getListData(String key,Class c) {
		if(!redisUtil.useRedis()) return null;
		Jedis jedis = redisUtil.getConnection();	
		Set<String> setlist = jedis.keys(key);	
		Iterator<String> ite = setlist.iterator();
		List<Object> objlist = new ArrayList<Object>();
		while(ite.hasNext()){
			String field = ite.next();
			String jsondata = jedis.get(field);
			 if(jsondata.startsWith("{")){
				 objlist.add(JSONObject.toBean(JSONObject.fromObject(jsondata),c));
			}else{
				objlist.add(jsondata);
			}					
		}
		redisUtil.closeConnection(jedis);
		return objlist;
	}

	@Override
	public List getByteListData(String key,String field,Class c){
		if(!redisUtil.useRedis()) return null;
		Jedis jedis = redisUtil.getConnection();	
		Set<String> setlist = jedis.keys(key);	
		Iterator<String> ite = setlist.iterator();
		List<Object> objlist = new ArrayList<Object>();
		while(ite.hasNext()){
			String k = ite.next();
			byte[] keyby = jedis.hget(TransCoderUtil.serialString(k), TransCoderUtil.serialString(field));
			objlist.add(TransCoderUtil.deserialize(keyby, c));				
		}
		redisUtil.closeConnection(jedis);
		return objlist;
	}

	

}
