package com.wuyizhiye.basedata.sync.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wuyizhiye.base.common.Resp;
import com.wuyizhiye.base.module.model.Module;
import com.wuyizhiye.base.timing.model.Task;
import com.wuyizhiye.basedata.code.model.BillType;
import com.wuyizhiye.basedata.org.model.BusinessType;
import com.wuyizhiye.basedata.param.model.ParamHeader;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.permission.model.PermissionItem;
import com.wuyizhiye.basedata.portlet.model.Portlet;
import com.wuyizhiye.basedata.sql.model.DbScript;

/**
 * @ClassName BaseDataSyncResp
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class BaseDataSyncResp implements Resp {
	
	private Map<String,List> map = new HashMap<String,List>();
	
	@Override
	public Resp praseResp(String doc) {
		JSONObject objlist = JSONObject.fromObject(doc);
		Set<String> set = objlist.keySet();
		for(String s : set){
			JSONArray jary = objlist.getJSONArray(s);
			if(DataSyncContants.PERMISSION.equals(s)){
				map.put(s, (List) JSONArray.toCollection(jary,PermissionItem.class));
			}
			if(DataSyncContants.MENU.equals(s)){
				map.put(s, (List) JSONArray.toCollection(jary,Menu.class));
			}
			if(DataSyncContants.MODULE.equals(s)){
				map.put(s, (List) JSONArray.toCollection(jary,Module.class));
			}
			if(DataSyncContants.PARAM.equals(s)){
				map.put(s, (List) JSONArray.toCollection(jary,ParamHeader.class));
			}
			if(DataSyncContants.PORTLET.equals(s)){
				map.put(s, (List) JSONArray.toCollection(jary,Portlet.class));
			}
			if(DataSyncContants.BUSINESS.equals(s)){
				map.put(s, (List) JSONArray.toCollection(jary,BusinessType.class));
			}
			if(DataSyncContants.SQL.equals(s)){
				map.put(s, (List) JSONArray.toCollection(jary,DbScript.class));
			}
			if(DataSyncContants.BILLTYPE.equals(s)){
				map.put(s, (List) JSONArray.toCollection(jary,BillType.class));
			}
			if(DataSyncContants.TASK.equals(s)){
				map.put(s, (List) JSONArray.toCollection(jary,Task.class));
			}
		}
		return this;
	}

	public Map<String,List> getObjResp(){
		return map;
	}
}
