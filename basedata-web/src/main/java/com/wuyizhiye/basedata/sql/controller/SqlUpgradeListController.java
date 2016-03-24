package com.wuyizhiye.basedata.sql.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.module.enums.BusinessTypeEnum;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.sql.enums.SqlRunStatusEnum;
import com.wuyizhiye.basedata.sql.model.SqlUpgrade;
import com.wuyizhiye.basedata.sql.service.BaseDataSyncRespService;
import com.wuyizhiye.basedata.sql.service.SqlUpgradeService;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SyncDataUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName SqlUpgradeListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value = "basedata/sqlupgrade/*")
public class SqlUpgradeListController extends ListController {

	@Autowired
	private SqlUpgradeService sqlUpgradeService;
	
	@Autowired
	private BaseDataSyncRespService baseDataSyncService;
	
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		getRequest().setAttribute("isModuleType",getString("isModuleType"));
		getRequest().setAttribute("moduleType",getString("moduleType"));
		getRequest().setAttribute("moduleEnumList", ModuleEnum.values());
		getRequest().setAttribute("runStatusList", SqlRunStatusEnum.toList());
		this.getRequest().setAttribute("businessTypeList", BusinessTypeEnum.toList());
		String maxtime = 
		this.queryExecutor.execOneEntity("com.wuyizhiye.basedata.sql.dao.SqlUpgradeDao.selectmaxtime", null, String.class);
		getRequest().setAttribute("maxtime", maxtime);
		return "basedata/sql/sqlUpgradeList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return "basedata/sql/sqlUpgradeEdit";
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.basedata.sql.dao.SqlUpgradeDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return sqlUpgradeService;
	}
	
	@Override
	protected Map<String, String> getParamMap() {
		Map<String, String> param = super.getParamMap();
		String isModuleType = param.get("isModuleType");
		if("FALSE".equals(isModuleType)){
			StringBuilder str = new StringBuilder("");
			str.append("'").append(param.get("moduleType")).append("'");
			param.put("modules", str.toString());
			return param;
		}
		if(param.containsKey("moduleType") && !StringUtils.isEmpty(param.get("moduleType"))){
			BusinessTypeEnum moduleType = Enum.valueOf(BusinessTypeEnum.class, param.get("moduleType"));
			BusinessTypeEnum[] allModuleType = BusinessTypeEnum.values();
			List<BusinessTypeEnum> types = new ArrayList<BusinessTypeEnum>(); 
			for(BusinessTypeEnum e : allModuleType){
				if(e==moduleType || e.getParent() == moduleType){
					types.add(e);
				}
			}
			ModuleEnum[] allModule = ModuleEnum.values();
			StringBuilder stringBuilder = new StringBuilder("");
			for(ModuleEnum e : allModule){
				for(BusinessTypeEnum t : types){
					if(e.getParent() == t){
						stringBuilder.append("'").append(e).append("',");
					}
				}
			}
			if(stringBuilder.length() > 1){
				stringBuilder = stringBuilder.deleteCharAt(stringBuilder.length()-1);
			}else{
				stringBuilder.append("'nodata'");
			}
			param.put("modules", stringBuilder.toString());
		}
		return param;
	}

	@RequestMapping(value = "loadSyncSqlData")
	public void loadSyncSqlData(HttpServletRequest req, HttpServletResponse resp){
		String dbtype = ParamUtils.getParamValue("DataBaseType");
		String dbname = ParamUtils.getParamValue("DataBaseName");
		
		if(StringUtils.isEmpty(dbtype)){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "请先设置数据库类型参数值");
			this.outPrint(resp, JSONObject.fromObject(this.getOutputMsg(), getDefaultJsonConfig()));
			return;
		}
		if(StringUtils.isNotNull(dbtype)&&"MYSQL".equals(dbtype.toUpperCase())){
			if(StringUtils.isEmpty(dbname)){
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "当数据库类型为mysql时请先设置数据库名");
				this.outPrint(resp, JSONObject.fromObject(this.getOutputMsg(), getDefaultJsonConfig()));
				return;
			}
		}
		
		
		String synctype = this.getString("synctype");
		String syncdate = this.getString("syncdate");
		String url = SyncDataUtil.getSqlUrl(synctype, syncdate);
//		String burl = BaseConfigUtil.getCurrSyncSqlUrl();
//		if(StringUtils.isEmpty(burl)) burl = "http://120.25.236.193:9980/wuyiyun/basedata/basesync/getSyncData?dataCenter=dataSource_wuyiyun";	
//		String url = burl+(burl.contains("?")?"&synctype=":"?synctype=");
//		url += synctype;
//		
//		if(StringUtils.isNotNull(syncdate)){
//			url += (url.contains("?")?"&syncdate=":"?syncdate=")+syncdate;
//		}
		
		try {
			int failcount = 0;
			List<SqlUpgrade> sulist =this.baseDataSyncService.loadSyncSql(url,0);
			if(null==sulist || sulist.size()==0){
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "没有需要同步的数据");
				this.outPrint(resp, JSONObject.fromObject(this.getOutputMsg(), getDefaultJsonConfig()));
				return;
			}
			
			Map<String,Object> param = new HashMap<String,Object>();
			//如果是mysql数据库,则先运行指定参数语句
			if("MYSQL".equals(dbtype.toUpperCase())){
				param.put("templateSql",  "SET @current_database = '"+dbname+"';");
				this.queryExecutor.executeUpdate("com.wuyizhiye.basedata.sql.dao.SqlUpgradeDao.templateSql", param);	
			}
					
			for(int i=0;i<sulist.size();i++){
				SqlUpgrade sqlup = sulist.get(i);
				String sql = "";
				if("ORACLE".equals(dbtype.toUpperCase())) sql = sulist.get(i).getOracleScript();
				else  {
					
					sql = sulist.get(i).getMysqlScript();
					//sql = "SET @current_database = '"+dbname+"';"+sql;
				}
				param.put("templateSql", sql);
				try{
					this.queryExecutor.executeUpdate("com.wuyizhiye.basedata.sql.dao.SqlUpgradeDao.templateSql", param);
					sqlup.setRunStatus(SqlRunStatusEnum.RUNED);
				}catch(Exception e){
					sqlup.setRunStatus(SqlRunStatusEnum.FAIL);	
					failcount ++;
				}
				sqlup.setRunTime(new Date());			
			}
			this.sqlUpgradeService.updateBatch(sulist);
			if(failcount>0){
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "存在运行失败的sql");
			}else{
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "同步成功");
			}
			
		} catch (Exception e) {
			getOutputMsg().put("MSG", e.getMessage());
			getOutputMsg().put("STATE", "FAIl");
		}
		this.outPrint(resp, JSONObject.fromObject(this.getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value = "runSql")
	public void runSql(HttpServletRequest req, HttpServletResponse resp){
		String dbtype = ParamUtils.getParamValue("DataBaseType");
		String dbname = ParamUtils.getParamValue("DataBaseName");
		Map<String,Object> param = new HashMap<String,Object>();
		if(StringUtils.isEmpty(dbtype)){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "请先设置数据库类型参数值");
			this.outPrint(resp, JSONObject.fromObject(this.getOutputMsg(), getDefaultJsonConfig()));
			return;
		}
		if(StringUtils.isNotNull(dbtype)&&"MYSQL".equals(dbtype.toUpperCase())){
			if(StringUtils.isEmpty(dbname)){
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "当数据库类型为mysql时请先设置数据库名");
				this.outPrint(resp, JSONObject.fromObject(this.getOutputMsg(), getDefaultJsonConfig()));
				return;
			}
			
			param.put("templateSql",  "SET @current_database = '"+dbname+"';");
			this.queryExecutor.executeUpdate("com.wuyizhiye.basedata.sql.dao.SqlUpgradeDao.templateSql", param);	
			
		}
		
		String id = this.getString("sqlId");
		SqlUpgrade sqlup = 
		this.sqlUpgradeService.getEntityById(id);
		String sql = "";
		try{
		if("ORACLE".equals(dbtype)) {
			sql = sqlup.getOracleScript();		
		}else{			
			sql = sqlup.getMysqlScript();
				
		}
			param.put("templateSql", sql);
			this.queryExecutor.executeUpdate("com.wuyizhiye.basedata.sql.dao.SqlUpgradeDao.templateSql", param);				
			sqlup.setRunStatus(SqlRunStatusEnum.RUNED);
			sqlup.setRunTime(new Date());	
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "执行成功");
		}catch(Exception e){
			sqlup.setRunStatus(SqlRunStatusEnum.FAIL);	
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", e.getMessage());
		}
		this.sqlUpgradeService.updateRun(sqlup);
		this.outPrint(resp, JSONObject.fromObject(this.getOutputMsg(), getDefaultJsonConfig()));
		
	}
}
