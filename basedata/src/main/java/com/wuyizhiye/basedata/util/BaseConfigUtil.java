package com.wuyizhiye.basedata.util;

import java.util.Map;

import com.wuyizhiye.basedata.basic.model.BaseConfigConstants;

/**
 * @ClassName BaseConfigUtil
 * @Description 基础设置UTIL
 * @author li.biao
 * @date 2015-4-2
 */
public class BaseConfigUtil {
	
	/**
	 * 按参数得到当前的配置
	 * @param para
	 * @return
	 */
	public static String getCurrBaseConfig(String para){
		Map<String,String> map = LoginHolder.getCurrBaseConfig();
		if(null==map || map.isEmpty()) return "";
		return map.get(para);
	}
	
	/**
	 * 得到当前的登录页
	 * @return
	 */
	public static String  getCurrHomeUrl(){
		return getCurrBaseConfig(BaseConfigConstants.HOMEPAGE_KEY);
	}
	
	/**
	 * 得到当前的控制方式
	 * @return
	 */
	public static String  getCurrControlMode(){
		return getCurrBaseConfig(BaseConfigConstants.CONTROLMODE_KEY);
	}
	
	/**
	 * 得到当前的索引服务器地址
	 * @return
	 */
	public static String  getCurrSolrServer(){
		return getCurrBaseConfig(BaseConfigConstants.INDEXSERVER_KEY);
	}
	
	/**
	 * 得到当前的权限盘源索引
	 * @return
	 */
	public static String  getCurrHouseIndex(){
		return getCurrBaseConfig(BaseConfigConstants.HOUSEINDEX_KEY);
	}
	
	/**
	 * 得到当前的启用权限盘源索引
	 * @return
	 */
	public static String  getCurrHousePowerIndex(){
		return getCurrBaseConfig(BaseConfigConstants.HOUSEPOWERINDEX_KEY);
	}
	
	/**
	 * 得到当前的资源客索引
	 * @return
	 */
	public static String  getCurrResourceCustomIndex(){
		return getCurrBaseConfig(BaseConfigConstants.RESOURCEINDEX_KEY);
	}
	
	/**
	 * 得到当前配置的邮件client_id
	 * @return
	 */
	public static String  getCurrMailClientId(){
		return getCurrBaseConfig(BaseConfigConstants.MAIL_CLIENT_ID);
	}
	
	/**
	 * 得到当前配置的邮件client_secret
	 * @return
	 */
	public static String  getCurrMailClientSecret(){
		return getCurrBaseConfig(BaseConfigConstants.MAIL_CLIENT_SECRET);
	}
	
	/**
	 * 得到当前配置的客户no
	 * @return
	 */
	public static String  getCurrCustomerNo(){
		return getCurrBaseConfig(BaseConfigConstants.CUSTOMER_NO);
	}
	
	/**
	 * 得到同步基础数据url
	 * @return
	 */
	public static String  getCurrSyncDataUrl(){
		return getCurrBaseConfig(BaseConfigConstants.SYNCBASEDATAURL);
	}
	
	/**
	 * 远程HTTP接口路径
	 * @return
	 */
	public static String  getCurrRemoteHttpUrl(){
		return getCurrBaseConfig(BaseConfigConstants.REMOTE_SERVERURL);
	}
	
	
	/**
	 * 得到同步基础数据url
	 * @return
	 */
	public static String  getCurrSyncSqlUrl(){
		return getCurrBaseConfig(BaseConfigConstants.SYNCSQLURL);
	}
	
	
	/**
	 * 按数据中心参数 参数名取得属性
	 * @param dataCenter
	 * @param para
	 * @return
	 */
	public static String getBaseConfigProp(String dataCenter,String para){
		Map<String,Map<String,String>> allConfig = LoginHolder.getAllBaseConfig();
		Map<String,String> map = allConfig.get(dataCenter);
		if(null==map || map.isEmpty()) return "";
		return map.get(para);
	}
	
}
