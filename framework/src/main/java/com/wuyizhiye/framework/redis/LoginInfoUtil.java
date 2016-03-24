package com.wuyizhiye.framework.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.enums.UserTypeEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.redis.serialize.TransCoderUtil;
import com.wuyizhiye.framework.listener.OnlineUser;
import com.wuyizhiye.framework.qqmial.model.QQToken;

/**
 * @ClassName LoginInfoUtil
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
public class LoginInfoUtil {
	public static final String CLIENT_LOGIN_INFO = "_COOKIE__CLIENT_LOGIN_INFO";
	public static class KEYSET{
		public static String CURRENTDATASOURCE="currentDataSource"; 
		public static String CURRENTCU="currentCU"; 
		public static String CURRENTORG="currentOrg"; 
		public static String CURRENTPOSITION="currentPosition"; 
		public static String CURRENTUSERTYPE="currentUserType"; 
		public static String CURRENTUSER="currentUser"; 
		public static String PERSONALPERMISSION = "personPermission";
		public static String PERSONALPERMISSIONMAP = "personPermissionMap";
		public static String CURRENTONLINEINFO = "onlineinfo";
		public static String CURRENTSKIN = "skinName";
		public static String SIGNATURE = "signatureKey";
		public static String BROWSER = "browser";
		public static String SINGLESOURCE = "singlesource";
		public static String QQMAIL_TOKEN = "qqmail_token";
		public static String QQMAIL_AUTHKEY = "qqmail_authKey";
	}
	
	public static String[] KEYARRY = 
	{
		"currentDataSource",
		"currentCU",
		"currentOrg",
		"currentPosition",
		"currentUserType",
		"currentUser",
		"personPermission",
		"personPermissionMap",
		"skinName",
		"signatureKey",
		"browser",
		"singlesource",
		"qqmail_token",
		"qqmail_authKey"
	};
	
	public static Map<String,Class> KEYCLASSMAP = new HashMap<String,Class>();
	static{
		KEYCLASSMAP.put(KEYSET.CURRENTCU, Org.class);
		KEYCLASSMAP.put(KEYSET.CURRENTORG, Org.class);
		KEYCLASSMAP.put(KEYSET.CURRENTPOSITION, Position.class);
		KEYCLASSMAP.put(KEYSET.CURRENTUSER, Person.class);
		KEYCLASSMAP.put(KEYSET.QQMAIL_TOKEN, QQToken.class);
		KEYCLASSMAP.put(KEYSET.CURRENTONLINEINFO, OnlineUser.class);
	}
	public static Map<String,String> KEYTYPEMAP = new HashMap<String,String>();
	static{
		KEYTYPEMAP.put(KEYSET.PERSONALPERMISSION, "list");
		KEYTYPEMAP.put(KEYSET.PERSONALPERMISSIONMAP, "map");
	}
	public static Map<byte[], byte[]> toMap(Map<String,Object> info){
		Map<byte[], byte[]> data = new HashMap<byte[], byte[]>();
		Set<String> keyset =  info.keySet();
		Iterator<String>  ite = keyset.iterator();
		while(ite.hasNext()){
			String key = ite.next();
			if(null!=info.get(key)){
				String type = KEYTYPEMAP.get(key);
				Class c = KEYCLASSMAP.get(key);
				if(null==c) c= String.class;
				if(null==type){
					data.put(TransCoderUtil.serialString(key), 
							TransCoderUtil.serial(info.get(key),c));			
				}else if("list".equals(type)){
					Object obj = info.get(key);
					if (obj instanceof ArrayList<?>){
						List datalist = (ArrayList)obj;
				        data.put(TransCoderUtil.serialString(key),
								TransCoderUtil.serial(datalist,c,true));	
					}
				}else if("map".equals(type)){
					data.put(TransCoderUtil.serialString(key),
							TransCoderUtil.serial(info.get(key),HashMap.class));
				}
			}
		}
		return data;
	}
	
	public static Map<String,Object> toInfo(Map<byte[], byte[]> b){
		Map<String,Object> data = new HashMap<String,Object>();
		Set<byte[]> bset = b.keySet();
		Iterator<byte[]>  ite = bset.iterator();
		while(ite.hasNext()){
			byte[] keybyte = ite.next();
			String key= TransCoderUtil.deserializeString(keybyte);
			String type = KEYTYPEMAP.get(key);
			Class c = KEYCLASSMAP.get(key);
			if(null==c) c= String.class;
			if(null==type){
				data.put(key,TransCoderUtil.deserialize(b.get(keybyte), c));
			}else  if("list".equals(type)){
				data.put(key,TransCoderUtil.deserializelist(b.get(keybyte),c));
			}else if("map".equals(type)){
				data.put(key,TransCoderUtil.deserialize(b.get(keybyte),HashMap.class));
			}		
		}
		return data;
	}
	
	public static String getCurrentDataSource(Map<byte[], byte[]> data){
		return TransCoderUtil.deserializeString(
				data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.CURRENTDATASOURCE)));
	}
	
	public static Org getCurrentCu(Map<byte[], byte[]> data){
		return 
		TransCoderUtil.deserialize(data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.CURRENTCU)), Org.class);
		//TransCoderUtil.deserializeOrg(data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.CURRENTCU)));
	}
	
	public static Org getCurrentOrg(Map<byte[], byte[]> data){
		return 
		TransCoderUtil.deserialize(data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.CURRENTORG)), Org.class);
		//TransCoderUtil.deserializeOrg(data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.CURRENTORG)));
	}

	public static Position getCurrentPosition(Map<byte[], byte[]> data){
		return 
		TransCoderUtil.deserialize(data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.CURRENTPOSITION)), Position.class);
		//TransCoderUtil.deserializePosition(data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.CURRENTPOSITION)));
	}

	public static Person getCurrentUser(Map<byte[], byte[]> data){
		return 
		TransCoderUtil.deserialize(data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.CURRENTUSER)), Person.class);
		//TransCoderUtil.deserializePerson(data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.CURRENTUSER)));
	}

	public static UserTypeEnum getCurrentUserType(Map<byte[], byte[]> data){
		if(null==data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.CURRENTUSERTYPE))) return null;
		return 
		UserTypeEnum.valueOf(TransCoderUtil.deserializeString(data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.CURRENTUSERTYPE))));
	}
	
	public static Set<String> getPersonPermisson(Map<byte[], byte[]> data){
		return new HashSet(TransCoderUtil.deserializelist(data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.PERSONALPERMISSION)),String.class)
		);
	}
	
	public static String getCurrentSkin(Map<byte[], byte[]> data){
		return  TransCoderUtil.deserializeString(
				data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.CURRENTSKIN)));
	}
	
	
	public static Map<String, String> getPersonPermissionMap(Map<byte[], byte[]> data) {
	
		return TransCoderUtil.deserialize(data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.PERSONALPERMISSIONMAP)),HashMap.class);
		
	}
	
	public static String getCurrentSignature(Map<byte[], byte[]> data){
		return TransCoderUtil.deserializeString(
				data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.SIGNATURE)));
	}
	
	public static String getCurrentBrower(Map<byte[], byte[]> data){
		return TransCoderUtil.deserializeString(
				data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.BROWSER)));
	}
	
	public static String getCurrentSingleSource(Map<byte[], byte[]> data){
		return TransCoderUtil.deserializeString(
				data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.SINGLESOURCE)));
	}
	
	public static String getQqmail_authKey(Map<byte[], byte[]> data){
		return TransCoderUtil.deserializeString(
				data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.QQMAIL_AUTHKEY)));
	}
	
	public static QQToken getQqmail_token(Map<byte[], byte[]> data){
		return 
		TransCoderUtil.deserialize(data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.CURRENTPOSITION)), QQToken.class);
		//TransCoderUtil.deserializePosition(data.get(TransCoderUtil.serialString(LoginInfoUtil.KEYSET.CURRENTPOSITION)));
	}
}
