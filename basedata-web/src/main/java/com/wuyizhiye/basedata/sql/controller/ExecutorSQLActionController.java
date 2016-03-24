package com.wuyizhiye.basedata.sql.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.controller.BdLeaveOfficeController;
import com.wuyizhiye.basedata.sql.model.ConnectParamBean;
import com.wuyizhiye.basedata.sql.service.ConnectParamBeanService;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName ExecutorSQLActionController
 * @Description 用于SQLQueryPanel 相关的功能Action
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value = "basedata/dbeSql/*")
public class ExecutorSQLActionController extends BaseController {
	private static Logger logger=Logger.getLogger(ExecutorSQLActionController.class);
	@Resource(name="queryExecutor")
	protected QueryExecutor queryExecutor;
	
	@Autowired
	private ConnectParamBeanService connectParamBeanService;

	// 执行SQL语句
	@RequestMapping(value = "excuteSql")
	public void excuteSql(HttpServletRequest req, HttpServletResponse resp){
		String ids = getString("ids");
		String sql = getString("sql");
		String[] sqls = sql.split(";");
		Map<String,Object> paramData = new HashMap<String, Object>();
		paramData.put("ids", ids);
		List<ConnectParamBean> connectParamBeans =  queryExecutor.execQuery(ConnectParamBean.MAPPER + ".select", paramData, ConnectParamBean.class);
		Map<String,Object> dataMap = new HashMap<String, Object>();
		String message = "";
		for(ConnectParamBean item : connectParamBeans){
			if(item != null){
				try {
					Connection  connection = getConnection(item.getUrl(),item.getUser(),item.getPassword(),item.getDbtype());
					ResultSet rs = null;
					PreparedStatement ps = null;
					message += item.getDbtype() + "-->" +  item.getName() + "\n";
					for(String sqlStr : sqls){
						if(!StringUtils.isEmpty(sqlStr)){
							ps = connection.prepareStatement(sqlStr);
							if (sqlStr.startsWith("SELECT") || sqlStr.startsWith("select")) {
								rs  = ps.executeQuery();
								int count = 0;
								while(rs.next()){
//									String fname = rs.getString("FID");
									count++;
								}
								message = message + sqlStr + ":返回" + count + "行     \n" ;
								rs.close();
							} else {
								ps.execute();
//								message = message + item.getDbtype() + ":" + "执行成功\n";
							}
						}
					}
					ps.close();
					connection.close();
				} catch (Exception e) {
					message = message + item.getDbtype() + "-->" +  item.getName() + ":" + e.getMessage();
				} 
			}
		}
		dataMap.put("msg", message); 
		outPrint(resp, JSONObject.fromObject(dataMap, getDefaultJsonConfig()));
	}
	
	// 执行SQL语句
	@RequestMapping(value = "excuteSqlNew")
	public void excuteSqlNew(Pagination<Map> pagination,HttpServletResponse response){
		String sql = getString("sql");
		if(!StringUtils.isEmpty(sql) && (sql.startsWith("SELECT") || sql.startsWith("select"))){
			if(sql.endsWith(";")){
				sql=sql.substring(0, sql.length()-1);
			}
			Map<String,Object> param=new HashMap<String, Object>();
			param.put("sql", sql);
			pagination = queryExecutor.execQuery(ConnectParamBean.MAPPER + ".excuteSql", pagination, param);
			outPrint(response, JSONObject.fromObject(pagination));
		}
	}
	 
	
	@RequestMapping(value = "toEditDB")
	public String toEditDB(ModelMap modelMap,HttpServletRequest req, HttpServletResponse resp){
		String id = getString("id");
		if(!StringUtils.isEmpty(id)){
			ConnectParamBean connectParamBean = connectParamBeanService.getEntityById(id);
			modelMap.put("connectParamBean", connectParamBean);
		}
		return "sql/dbEdit";
	}
	
	@RequestMapping(value = "toDBList")
	public String toDBList(ModelMap modelMap){
		
		return "sql/dbManage";
	}
	
	@RequestMapping(value="list")
	public String list(ModelMap modelMap){
		List<ConnectParamBean> connectParamBeanList = queryExecutor.execQuery(ConnectParamBean.MAPPER + ".select", null, ConnectParamBean.class);
		modelMap.put("connectParamBeanList", connectParamBeanList);
		return "sql/dbList";
	}
	
	// 执行SQL语句
	@RequestMapping(value = "save")
	public void save(HttpServletRequest req, HttpServletResponse resp, ConnectParamBean connectParamBean){
		Map<String,Object> dataMap = new HashMap<String, Object>();
		if(!StringUtils.isNotNull(connectParamBean.getId())){
			connectParamBean.setId(StringUtils.getUUID());
			queryExecutor.executeInsert(ConnectParamBean.MAPPER + ".insert", connectParamBean);
		}else{
			connectParamBeanService.updateEntity(connectParamBean);
		}
		dataMap.put("STATE", "SUCCESS");
		dataMap.put("MSG", "保存成功.");	
		outPrint(resp, JSONObject.fromObject(dataMap, getDefaultJsonConfig()));
	}
	
	// 执行SQL语句
	@RequestMapping(value = "test")
	public void test(HttpServletRequest req, HttpServletResponse response, ConnectParamBean connectParamBean){
		Connection conn = getConnection(connectParamBean.getUrl(),connectParamBean.getUser(),connectParamBean.getPassword(),connectParamBean.getDbtype());
		if (conn != null) {
			getOutputMsg().put("MSG", "连接成功.");
		} else {
			getOutputMsg().put("MSG", "连接失败.");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	
	@RequestMapping(value="deleteDB")
	@ResponseBody
	public void deleteDB(HttpServletResponse response,ModelMap modelMap){
		Map<String,Object> dataMap = new HashMap<String, Object>();
		String id = getString("id");
		connectParamBeanService.deleteById(id);
		dataMap.put("STATE", "SUCCESS");
		dataMap.put("MSG", "删除成功.");
		outPrint(response, JSONObject.fromObject(dataMap, getDefaultJsonConfig()));
	}
	
	public Connection getConnection(String url,String user, String passwd,String type){
		Connection conn = null;
		// load class
		try {
			if("ORACLE".equals(type)){
				Class.forName("oracle.jdbc.driver.OracleDriver");
			}else if("MYSQL".equals(type)){
				Class.forName("com.mysql.jdbc.Driver");
			}else if("SQLSERVER".equals(type)){
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			}
		} catch (ClassNotFoundException e) {
		}
		try {
			conn = DriverManager.getConnection(url, user, passwd);
		} catch (SQLException e) {
			String msg = "链接数据库出错：" + e.getMessage();
			getOutputMsg().put("msg", msg);
		}
		return conn;
	}
	
	@RequestMapping(value="systemTableList")
	public String systemTableList(ModelMap modelMap){
		List<ConnectParamBean> connectParamBeanList = queryExecutor.execQuery(ConnectParamBean.MAPPER + ".select", null, ConnectParamBean.class);
		modelMap.put("connectParamBeanList", connectParamBeanList);
		return "sql/systemTableList";
	}
	
	@RequestMapping(value="getSystemTables")
	public void getSystemTables(Pagination<Map> pagination,HttpServletResponse response){
		Map<String,Object> dataMap = new HashMap<String, Object>();
		dataMap.put("tbname", getString("tbname"));
		String  dataBaseType=ParamUtils.getStringValueByNumber("DataBaseType");
		if(StringUtils.isEmpty(dataBaseType) || "ORACLE".equals(dataBaseType)){
			pagination = queryExecutor.execQuery(ConnectParamBean.MAPPER + ".getSystemTables_oracle", pagination, dataMap);
		}else{
			pagination = queryExecutor.execQuery(ConnectParamBean.MAPPER + ".getSystemTables_mysql", pagination, dataMap);
			if(pagination!=null && pagination.getItems()!=null && pagination.getItems().size()>0){
				for(int i=0;i<pagination.getItems().size();i++){
					Map<String,String> map=pagination.getItems().get(i);
					if(map.get("tbName")!=null){ 
						String sql="select count(1) as 'dataCount' from "+map.get("tbName");
						Map<String,Object> param=new HashMap<String, Object>();
						param.put("sql", sql);
						Map count=queryExecutor.execOneEntity(ConnectParamBean.MAPPER + ".excuteSql", param, Map.class);
					    pagination.getItems().get(i).put("dataCount", count.get("dataCount")); 
					}
				}
			}
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	
	@RequestMapping(value="performance")
	public String performance(HttpServletResponse response){
		String  dataBaseType=ParamUtils.getStringValueByNumber("DataBaseType");
		this.getRequest().setAttribute("dataBaseType", dataBaseType);
		return "sql/performanceList";
	}
	
	@RequestMapping(value="performanceData")
	public void performanceData(HttpServletResponse response){
		Map<String, Object>param=super.getParaMap();
		DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
		 if(null!=param.get("startDate") && !StringUtils.isEmpty(param.get("startDate").toString())){
	        	try {
					param.put("startDate", df.parse(param.get("startDate").toString()));
				} catch (Exception e) {
					logger.error("", e);
				}
	        }
	        if(null!=param.get("endDate") && !StringUtils.isEmpty(param.get("endDate").toString())){
	        	try {
					param.put("endDate", DateUtil.getNextDay(df.parse(param.get("endDate").toString())));
				} catch (Exception e) {
					logger.error("", e);
				}
	        }
		List<Map>maps=queryExecutor.execQuery(ConnectParamBean.MAPPER+".getDBPerformance", param, Map.class);
		getOutputMsg().put("Rows", maps);
		getOutputMsg().put("Total", maps.size());
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@RequestMapping(value="getDbCount")
	public void getDbCount(HttpServletResponse response){
		Map currJoin=queryExecutor.execOneEntity(ConnectParamBean.MAPPER+".getCurrJoinOracle", null,Map.class);
		Map maxJoin=queryExecutor.execOneEntity(ConnectParamBean.MAPPER+".getMaxJoinOracle", null,Map.class);
		Map currSessioin=queryExecutor.execOneEntity(ConnectParamBean.MAPPER+".getCurrSessionOracle", null,Map.class);
		getOutputMsg().put("currJoin", currJoin.get("CURRJOIN"));
		getOutputMsg().put("maxJoin", maxJoin.get("MAXJOIN"));
		getOutputMsg().put("currSessioin", currSessioin.get("CURRSESSION"));
		outPrint(response, JSONObject.fromObject(getOutputMsg(),getDefaultJsonConfig()));
	}
}
