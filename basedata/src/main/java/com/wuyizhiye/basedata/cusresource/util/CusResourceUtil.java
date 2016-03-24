package com.wuyizhiye.basedata.cusresource.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.cusresource.model.CusResource;

/**
 * @ClassName CusResourceUtil
 * @Description 客户来源工具类
 * @author li.biao
 * @date 2015-4-2
 */
public class CusResourceUtil {
	
	private static QueryExecutor queryExecutor ;
	
	public static String PHONE_CUSTOMER="aae04fc4-94a8-4014-93e8-4daab7622b86";//电话客
	
	public static String STREET_CUSTOMER="9d32b605-19ed-4bc2-bac5-a7a8924c6bdc";//街霸客
	
	public static String NETWORK_CUSTOMER="3766b0c4-2f79-4143-aa98-6f8229b32aa5";//网络客
	
	public static String IMPORT_CUSTOMER="1a390fe8-f07d-4ad9-9b4f-e580de8903d0";//导入客
	
	public static String ZHIYE_CUSTOMER="ZHIYEW";//置业网,
	
	static{
		queryExecutor = (QueryExecutor)ApplicationContextAware.getApplicationContext().getBean("queryExecutor");
	}
	
	/**
	 * 取出所有客户来源
	 */
	public static List<CusResource> getAllCusResource(){
		List<CusResource>cusres=queryExecutor.execQuery("com.wuyizhiye.basedata.cusresource.dao.CusResourceDao.select", null, CusResource.class);
		return cusres;
	}
	
	/**
	 * 取出第二级启用的客户来源
	 * @return
	 */
	public static List<CusResource> getCusResource(){
		Map<String, Object>param=new HashMap<String, Object>();
		param.put("parentIsNotNull", "Y");
		param.put("enableflag", "Y");
		List<CusResource>cusres=queryExecutor.execQuery("com.wuyizhiye.basedata.cusresource.dao.CusResourceDao.select", null, CusResource.class);
		return cusres;
	}
	
	/**
	 * 自定义条件取客户来源
	 * @return
	 */
	public static List<CusResource> getCusResource(Map<String, Object>param){
		List<CusResource>cusres=queryExecutor.execQuery("com.wuyizhiye.basedata.cusresource.dao.CusResourceDao.select", param, CusResource.class);
		return cusres;
	}
	
	/**
	 * 返回来源名称
	 */
	public static String getCusResourceName(String id){
		Map<String, Object>param=new HashMap<String, Object>();
		if(StringUtils.isEmpty(id)){
			id="-1";
		}
		param.put("id", id);
		CusResource cusResource=queryExecutor.execOneEntity("com.wuyizhiye.basedata.cusresource.dao.CusResourceDao.select", param, CusResource.class);
		return cusResource.getName()==null?"":cusResource.getName();
	}
	
	public static CusResource getCusResource(String id){
		Map<String, Object>param=new HashMap<String, Object>();
		param.put("id", id);
		CusResource cusResource=queryExecutor.execOneEntity("com.wuyizhiye.basedata.cusresource.dao.CusResourceDao.select", param, CusResource.class);
		return cusResource;
	}
	
	public static CusResource getPhoneResource(){
		return getCusResource(PHONE_CUSTOMER);
	}
	
	public static CusResource getStreetResource(){
		return getCusResource(STREET_CUSTOMER);
	}
	
	public static CusResource getNetResource(){
		return getCusResource(NETWORK_CUSTOMER);
	}
	
	public static CusResource getImportResource(){
		return getCusResource(IMPORT_CUSTOMER);
	}
	
	public static CusResource getZhiYeWResource(){
		CusResource cusResource=new CusResource();
		cusResource.setId(ZHIYE_CUSTOMER);
		return cusResource;
	}
}
