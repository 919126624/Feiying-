package com.wuyizhiye.basedata.sql.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.module.model.Module;
import com.wuyizhiye.basedata.code.model.BillType;
import com.wuyizhiye.basedata.org.model.BusinessType;
import com.wuyizhiye.basedata.param.model.ParamHeader;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.permission.model.PermissionItem;
import com.wuyizhiye.basedata.sql.model.DbScript;
import com.wuyizhiye.basedata.sql.model.SqlUpgrade;

/**
 * @ClassName BaseDataSyncRespService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface BaseDataSyncRespService {
	
	Map getBaseData(String url)  throws Exception;
	
	void syncPermission(List<PermissionItem> list,int isCover);
	
	void syncModule(List<Module> list,int isCover);
	
	void syncMenu(List<Menu> list,int isCover);
	
	void syncParam(List<ParamHeader> list,int isCover);
	
	void syncBusiness(List<BusinessType> list,int isCover);
	
	List<SqlUpgrade> syncSql(List<DbScript> list,int isCover);
	
	List<SqlUpgrade> loadSyncSql(String url,int isCover) throws Exception;
	
	String loadSyncData(String url,int isCover) throws Exception;
	
	void syncBillType(List<BillType> list,int isCover);
}
