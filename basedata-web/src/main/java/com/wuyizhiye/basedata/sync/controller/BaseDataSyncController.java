package com.wuyizhiye.basedata.sync.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.basedata.sql.service.BaseDataSyncRespService;
import com.wuyizhiye.basedata.util.BaseConfigUtil;
import com.wuyizhiye.basedata.util.SyncDataUtil;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName BaseDataSyncController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value = "basedata/basesync/*")
public class BaseDataSyncController extends BaseController {
	
	@Autowired
	private BaseDataSyncRespService baseDataSyncService;
	
	
	@RequestMapping(value = "getSyncData")
	public void getSyncData(HttpServletRequest req, HttpServletResponse resp){
//		String synctype = this.getString("synctype");
//		if(StringUtils.isEmpty(synctype)){
//			throw new RuntimeException("参数不对");
//		}
//		String[] syncary = DataSyncContants.syncary;
//		String[] stary = synctype.split(",");
//		List<String> slist = new ArrayList<String>();
//		for(int i=0;i<stary.length;i++){		
//			for(int j=0;j<syncary.length;j++){
//				if(syncary[j].equals(stary[i])){
//					slist.add(syncary[j]);
//					break;
//				}
//			}
//		}
//		
//		if(slist.contains(DataSyncContants.MODULE)){
//			List<Module> modulelist = 
//					this.queryExecutor.execQuery("com.wuyizhiye.base.module.dao.ModuleDao.select", null, Module.class);
//			getOutputMsg().put(DataSyncContants.MODULE, modulelist);
//		}
//		if(slist.contains(DataSyncContants.PERMISSION)){
//			List<PermissionItem> pmlist = 
//					this.queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.PermissionItemDao.selectNoJoin", null, PermissionItem.class);
//			getOutputMsg().put(DataSyncContants.PERMISSION, pmlist);
//		}
//		
//		if(slist.contains(DataSyncContants.MENU)){
//			List<Menu> menulist = 
//					this.queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.select", null, Menu.class);
//			getOutputMsg().put(DataSyncContants.MENU, menulist);
//		}
//		
//		if(slist.contains(DataSyncContants.BUSINESS)){
//			List<BusinessType> bustypelist = 
//					this.queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.BusinessTypeDao.select", null, BusinessType.class);
//			getOutputMsg().put(DataSyncContants.BUSINESS, bustypelist);
//		}
//		
//		if(slist.contains(DataSyncContants.PARAM)){
//			List<ParamHeader> phlist = 
//					this.queryExecutor.execQuery("com.wuyizhiye.basedata.param.ParamHeaderDao.select", null, ParamHeader.class);
//			List<ParamLines> pllist = 
//					this.queryExecutor.execQuery("com.wuyizhiye.basedata.param.dao.ParamLinesDao.select", null, ParamLines.class);
//			
//			getOutputMsg().put(DataSyncContants.PARAM, phlist);
//		}
//		if(slist.contains(DataSyncContants.INTERFACE)){
//			List<APIItem> itemlist = this.queryExecutor.execQuery("com.wuyizhiye.basedata.apiCenter.model.APIItem.select", null, APIItem.class);
//			List<APIParameter> aplist = this.queryExecutor.execQuery("com.wuyizhiye.basedata.apiCenter.model.APIParameter.select",null,APIParameter.class);
//			
//			getOutputMsg().put(DataSyncContants.INTERFACE, itemlist);
//			getOutputMsg().put(DataSyncContants.INTERFACEPARAM, aplist);
//		}
//		if(slist.contains(DataSyncContants.PORTLET)){
//			List<Portlet> porlist = this.queryExecutor.execQuery("com.wuyizhiye.basedata.portlet.dao.PortletDao.select", null, Portlet.class);
//			getOutputMsg().put(DataSyncContants.PORTLET, porlist);
//		}
//		if(slist.contains(DataSyncContants.SQL)){
//			Map<String,Object> param = new HashMap<String,Object>();
//			String syncdate = this.getString("syncdate");
//			param.put("createTime", DateUtil.convertStrToDate(syncdate));
//			List<DbScript> sqllist = 
//					this.queryExecutor.execQuery("com.wuyizhiye.basedata.sql.dao.SqlUpgradeDao.selectDbScript", param,DbScript.class);
//			getOutputMsg().put(DataSyncContants.SQL, sqllist);
//		}
//		if(slist.contains(DataSyncContants.BILLTYPE)){
//			List<BillType> billlist = 
//					this.queryExecutor.execQuery("com.wuyizhiye.basedata.code.BillTypeDao.select", null,BillType.class);
//			getOutputMsg().put(DataSyncContants.BILLTYPE, billlist);
//		}
//		if(slist.contains(DataSyncContants.TASK)){
//			List<Task> billlist = 
//					this.queryExecutor.execQuery("com.wuyizhiye.base.timing.dao.TaskDao.selectSync", null,Task.class);
//			getOutputMsg().put(DataSyncContants.TASK, billlist);
//		}
//		if(slist.contains(DataSyncContants.PRINT_CONFIG)){
//			List<PrintConfig> billlist = this.queryExecutor.execQuery("com.wuyizhiye.basedata.code.PrintConfigDao.select", null,PrintConfig.class);
//			getOutputMsg().put(DataSyncContants.PRINT_CONFIG, billlist);
//		}
//		if(slist.contains(DataSyncContants.CITY)){
//			List<City> billlist = this.queryExecutor.execQuery("com.wuyizhiye.basedata.bank.dao.CityDao.select", null,City.class);
//			getOutputMsg().put(DataSyncContants.CITY, billlist);
//		}
//		
//		this.outPrint(resp, JSONObject.fromObject(this.getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value = "loadSyncData")
	public void loadSyncData(HttpServletRequest req, HttpServletResponse resp){
		String synctype = this.getString("synctype");	
		int isCover = this.getInt("isCover");//是否为覆盖更新，即先删除在更新
	    if(StringUtils.isEmpty(synctype)){
	    	getOutputMsg().put("MSG", "参数为空");
			getOutputMsg().put("STATE", "FAIl");
	    }else{
		String url = SyncDataUtil.getUrl(synctype);
		try {
			String msg =this.baseDataSyncService.loadSyncData(url,isCover);
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", msg);
		} catch (Exception e) {
			getOutputMsg().put("MSG", e.getMessage());
			getOutputMsg().put("STATE", "FAIl");
		}
	    }
		this.outPrint(resp, JSONObject.fromObject(this.getOutputMsg(), getDefaultJsonConfig()));
	}
		
	@RequestMapping(value = "loadSyncSqlData")
	public void loadSyncSqlData(HttpServletRequest req, HttpServletResponse resp){
		String synctype = this.getString("synctype");	
		int isCover = this.getInt("isCover");//是否为覆盖更新，即先删除在更新
		String burl = BaseConfigUtil.getCurrSyncDataUrl();
		if(StringUtils.isEmpty(burl)) burl = "http://120.25.236.193:9980/wuyiyun/basedata/basesync/getSyncData?dataCenter=dataSource_wuyiyun";	
		String url = burl+(burl.contains("?")?"&synctype=":"?synctype=");
		url += synctype;
		try {
			this.baseDataSyncService.loadSyncData(url,isCover);
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "同步成功");
		} catch (Exception e) {
			getOutputMsg().put("MSG", e.getMessage());
			getOutputMsg().put("STATE", "FAIl");
		}
		this.outPrint(resp, JSONObject.fromObject(this.getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="imgCompr")
	public void ImgCompr() throws NumberFormatException, Exception{
		if(StringUtils.isEmpty(getString("url")) && StringUtils.isEmpty(getString("width")) 
				&& StringUtils.isEmpty(getString("hight")) && StringUtils.isEmpty(getString("isSonFile")) ){
			throw new RuntimeException("参数不完整，正确参数：url=路径，width=压缩宽度，hight=压缩高度，isSonFile=是否包括子文件夹");
		}else{
			com.wuyizhiye.basedata.util.ImgCompr.resize(getString("url"), Integer.parseInt(getString("width")),
					Integer.parseInt(getString("hight")), getString("isSonFile").equals("true"));
		}
	}
	
	@RequestMapping(value="list")
	public String list(){
		String sycUrl = SyncDataUtil.getSyncUrl();
		getRequest().setAttribute("sycUrl", sycUrl);
		return "sql/syncList";
	}
	
	/**
	 * 列表数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="listData")
	@Dependence(method="list")
	public void listData(Pagination<?> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.sync.dao.BillTypeCopyDao.selectSyncDataItem", pagination, null);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
}
