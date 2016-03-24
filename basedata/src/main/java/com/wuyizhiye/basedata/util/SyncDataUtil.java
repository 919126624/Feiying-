package com.wuyizhiye.basedata.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wuyizhiye.base.module.model.Module;
import com.wuyizhiye.base.timing.model.Task;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.apiCenter.model.APIItem;
import com.wuyizhiye.basedata.apiCenter.model.APIParameter;
import com.wuyizhiye.basedata.bank.model.City;
import com.wuyizhiye.basedata.code.model.BillType;
import com.wuyizhiye.basedata.code.model.PrintConfig;
import com.wuyizhiye.basedata.org.model.BusinessType;
import com.wuyizhiye.basedata.param.model.ParamHeader;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.permission.model.PermissionItem;
import com.wuyizhiye.basedata.portlet.model.Portlet;
import com.wuyizhiye.basedata.sql.model.DbScript;
import com.wuyizhiye.basedata.sync.model.DataSyncContants;

/**
 * @ClassName SyncDataUtil
 * @Description 数据同步工具类
 * @author li.biao
 * @date 2015-4-2
 */
public class SyncDataUtil {
	public static final String TIP_KEY_STATE = "STATE" ;
	public static final String TIP_KEY_MSG = "MSG" ;
	public static final String TIP_KEY_SUCC = "SUCCESS" ;
	public static final String TIP_KEY_FAIL= "FAIL" ;
	public static final String TIP_VALUE_EXCEPTION = "EXCEPTION" ;
	
	public static String getSyncUrl(){
		String sycUrl=BaseConfigUtil.getCurrSyncDataUrl();
		if(StringUtils.isEmpty(sycUrl)){
			sycUrl="http://120.25.236.193:9980/wuyiyun";
		}
//		return sycUrl;
//"http://192.168.1.103/testerp"  "http://localhost:8080/web"
		return sycUrl;
	}
	public static String getUrl(String type){
		StringBuffer sb=new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_sycnData");
		sb.append("&params=").append("{type:").append("'"+type+"'").append("}");
		String url=getSyncUrl()+sb.toString();
		return url;
	}
	public static String getSqlUrl(String type,String syncdate){
		StringBuffer sb=new StringBuffer();
		sb.append("/api?").append("apiNumber=").append("701_sycnData");
		sb.append("&params=").append("{type:").append("'"+type+"'");
		if(!StringUtils.isEmpty(syncdate)){
			sb.append(",syncdate:").append("'"+syncdate+"'");
		}
		sb.append("}");
		String url=getSyncUrl()+sb.toString();
		return url;
	}
	public static Map<String, Object> getSyncData(String url){
		Map<String, Object>  resMap=new HashMap<String, Object>();
		String requestData = HttpClientUtil.callHttpUrlGet(url);
		setResMap(requestData, resMap);
		return resMap;
	}
	private static Map<String, Object> setResMap(String requestData,Map<String, Object>resMap){
		if(!StringUtils.isEmpty(requestData)){
			JSONObject jsonObj=JSONObject.fromObject(requestData);
			if(jsonObj.getInt("resultType")==1){
				JSONObject jsonObject=JSONObject.fromObject(jsonObj.get("resultData"));
					Set<String>keySet=jsonObject.keySet();
					for(String key:keySet){
						JSONArray jsonArray = JSONArray.fromObject(jsonObject.getString(key));
						if(DataSyncContants.PERMISSION.equals(key)){
						List<PermissionItem> pmlist = (List<PermissionItem>)JSONArray.toCollection(jsonArray, PermissionItem.class);
						resMap.put(key, pmlist);
						}
						if(DataSyncContants.MODULE.equals(key)){
							List<Module> mlist = (List<Module>)JSONArray.toCollection(jsonArray, Module.class);
							resMap.put(key, mlist);
							}
						if(DataSyncContants.MENU.equals(key)){
							List<Menu> melist = (List<Menu>)JSONArray.toCollection(jsonArray, Menu.class);
							resMap.put(key, melist);
						}
						if(DataSyncContants.BUSINESS.equals(key)){
							List<BusinessType> btlist = (List<BusinessType>)JSONArray.toCollection(jsonArray, BusinessType.class);
							resMap.put(key, btlist);
						}
						if(DataSyncContants.BILLTYPE.equals(key)){
							List<BillType> btlist = (List<BillType>)JSONArray.toCollection(jsonArray, BillType.class);
							resMap.put(key, btlist);
						}
						if(DataSyncContants.TASK.equals(key)){
							List<Task> tlist = (List<Task>)JSONArray.toCollection(jsonArray, Task.class);
							resMap.put(key, tlist);
						}
						if(DataSyncContants.CITY.equals(key)){
							List<City> ctlist = (List<City>)JSONArray.toCollection(jsonArray, City.class);
							resMap.put(key, ctlist);
						}
						if(DataSyncContants.PORTLET.equals(key)){
							List<Portlet> portlist = (List<Portlet>)JSONArray.toCollection(jsonArray, Portlet.class);
							resMap.put(key, portlist);
						}
						if(DataSyncContants.PRINT_CONFIG.equals(key)){
							List<PrintConfig> pclist = (List<PrintConfig>)JSONArray.toCollection(jsonArray, PrintConfig.class);
							resMap.put(key, pclist);
						}
						if(DataSyncContants.PARAM.equals(key)){
							List<ParamHeader> parmlist = (List<ParamHeader>)JSONArray.toCollection(jsonArray, ParamHeader.class);
							resMap.put(key, parmlist);
						}
						if(DataSyncContants.INTERFACE.equals(key)){
							List<APIItem> ailist = (List<APIItem>)JSONArray.toCollection(jsonArray, APIItem.class);
							resMap.put(key, ailist);
							
						}
						if(DataSyncContants.INTERFACEPARAM.equals(key)){
							List<APIParameter> aplist = (List<APIParameter>)JSONArray.toCollection(jsonArray, APIParameter.class);
							resMap.put(key, aplist);		
						}
						if(DataSyncContants.SQL.equals(key)){
							List<DbScript> dblist = (List<DbScript>)JSONArray.toCollection(jsonArray, DbScript.class);
							resMap.put(key, dblist);
						}
					}
					resMap.put(TIP_KEY_STATE, TIP_KEY_SUCC);
			}else{
				resMap.put(TIP_KEY_STATE, TIP_KEY_FAIL);
				resMap.put(TIP_KEY_MSG, jsonObj.get("resultMsg"));
			}
		}else{
			resMap.put(TIP_KEY_STATE, TIP_KEY_FAIL);
			resMap.put(TIP_KEY_MSG, "返回数据为空");
		}
		return resMap;
	}
}
