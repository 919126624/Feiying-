package com.wuyizhiye.basedata.sql.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.module.model.Module;
import com.wuyizhiye.base.timing.TaskStatusEnum;
import com.wuyizhiye.base.timing.model.Task;
import com.wuyizhiye.base.timing.service.TaskService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.apiCenter.model.APIItem;
import com.wuyizhiye.basedata.apiCenter.model.APIParameter;
import com.wuyizhiye.basedata.apiCenter.service.APIItemService;
import com.wuyizhiye.basedata.apiCenter.service.APIParameterService;
import com.wuyizhiye.basedata.bank.model.City;
import com.wuyizhiye.basedata.bank.service.CityService;
import com.wuyizhiye.basedata.code.model.BillType;
import com.wuyizhiye.basedata.code.model.PrintConfig;
import com.wuyizhiye.basedata.code.service.BillTypeService;
import com.wuyizhiye.basedata.code.service.PrintConfigService;
import com.wuyizhiye.basedata.module.service.ModuleService;
import com.wuyizhiye.basedata.org.model.BusinessType;
import com.wuyizhiye.basedata.org.service.BusinessTypeService;
import com.wuyizhiye.basedata.param.model.ParamHeader;
import com.wuyizhiye.basedata.param.service.ParamHeaderService;
import com.wuyizhiye.basedata.param.service.ParamLinesService;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.permission.model.PermissionItem;
import com.wuyizhiye.basedata.permission.service.MenuService;
import com.wuyizhiye.basedata.permission.service.PermissionItemService;
import com.wuyizhiye.basedata.portlet.model.Portlet;
import com.wuyizhiye.basedata.portlet.service.PortletService;
import com.wuyizhiye.basedata.sql.enums.SqlRunStatusEnum;
import com.wuyizhiye.basedata.sql.model.DbScript;
import com.wuyizhiye.basedata.sql.model.SqlUpgrade;
import com.wuyizhiye.basedata.sql.service.BaseDataSyncRespService;
import com.wuyizhiye.basedata.sql.service.SqlUpgradeService;
import com.wuyizhiye.basedata.sync.model.DataSyncContants;
import com.wuyizhiye.basedata.sync.service.BillTypeCopyService;
import com.wuyizhiye.basedata.util.SyncDataUtil;

/**
 * @ClassName BaseDataSyncRespServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="baseDataSyncService")
@Transactional
public class BaseDataSyncRespServiceImpl implements BaseDataSyncRespService   {

	@Autowired
	private MenuService menuService;
	@Autowired
	private PermissionItemService permissionItemService;
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private ParamHeaderService paramHeaderService; 
	@Autowired
	private ParamLinesService paramLinesService;
	@Autowired
	private BusinessTypeService businessTypeService;
	@Autowired
	private SqlUpgradeService sqlUpgradeService;
	@Autowired
	private BillTypeCopyService billTypeCopyService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private BillTypeService billTypeService;
	@Autowired
	private PrintConfigService printConfigService;
	@Autowired
	private CityService cityService;
	@Autowired
	private PortletService portletService;
	@Autowired
	private APIItemService apiItemService;
	@Autowired
	private APIParameterService apiParameterService;
	
	public static final String SYNC_TYPE="CLOUD";//同步类型,若为空则不会更新云ID字段和同步时间
	@Override
	public Map getBaseData(String url) throws Exception {	
		return SyncDataUtil.getSyncData(url);
	}	
	@Override
	public void syncPermission(List<PermissionItem> list,int isCover) {
		if(null==list || list.size()==0) return;
		Map<String, Object>  param=new HashMap<String, Object>();
		param.put("syncType", "CLOUD");
		List<PermissionItem> pmcurlist = this.permissionItemService.getPermissionItemList(param);
		List<String> ids=new ArrayList<String>();
		Date syncDate = new Date();
		Map<String,PermissionItem> pmmap = new HashMap<String,PermissionItem>();
		for(PermissionItem p : pmcurlist){
			pmmap.put(p.getCloudId(), p);
			ids.add(p.getId());
		}
		if(ids.size()>0 && isCover==1){
			for(int i=0;i<list.size();i++){
				list.get(i).setSyncType(SYNC_TYPE);
				list.get(i).setSyncDate(syncDate);
			}
//			this.permissionItemService.deleteBatch(ids);
//			this.permissionItemService.addBatch(list);
		}else{
			List<PermissionItem> addlist = new ArrayList<PermissionItem>();
			List<PermissionItem> updatelist = new ArrayList<PermissionItem>();
			for(int i=0;i<list.size();i++){
				PermissionItem pi = list.get(i);
				pi.setSyncType(SYNC_TYPE);
				pi.setSyncDate(syncDate);
				pi.setCloudId(pi.getId());
				if(pmmap.containsKey(pi.getId())){
					pi = setPermissionItem(pi,pmmap.get(pi.getId()));
					updatelist.add(pi);
				}else{
					addlist.add(pi);
				}
			}
			this.permissionItemService.updateBatch(updatelist);
			this.permissionItemService.addBatch(addlist);
		}
	}
	public PermissionItem setPermissionItem(PermissionItem cloud,PermissionItem local){
		local.setCloudId(cloud.getCloudId());
		local.setDescription(cloud.getDescription());
		local.setGroup(cloud.getGroup());
		local.setMenuPerm(cloud.getMenuPerm());
		local.setMethod(cloud.getMethod());
		local.setModule(cloud.getModule());
		local.setName(cloud.getName());
		local.setNumber(cloud.getNumber());
		local.setPermissionType(cloud.getPermissionType());
		local.setSyncDate(cloud.getSyncDate());
		local.setSyncType(cloud.getSyncType());
		return local;
	}

	@Override
	public void syncModule(List<Module> list,int isCover) {
		if(null==list || list.size()==0) return;
		List<Module> modlist = this.moduleService.getModuleList(null);
		List<String> ids=new ArrayList<String>();
		
		Map<String,Module> modmap = new HashMap<String,Module>();
		for(Module m : modlist){
			modmap.put(m.getId(), m);
			ids.add(m.getId());
		}
		if(ids.size()>0 && isCover==1){
//			this.moduleService.deleteBatch(ids);
//			this.moduleService.addBatch(list);
		}else{
			List<Module> addlist = new ArrayList<Module>();
			List<Module> updatelist = new ArrayList<Module>();
			for(int i=0;i<list.size();i++){
				Module m = list.get(i);
				if(modmap.containsKey(m.getId())){
					updatelist.add(m);
				}else{
					addlist.add(m);
				}
			}
			this.moduleService.addBatch(addlist);
			this.moduleService.updatePartBatch(updatelist);
		}
	}

	@Override
	public void syncMenu(List<Menu> list,int isCover) {
		if(null==list || list.size()==0) return;
		Map<String, Object>  param=new HashMap<String, Object>();
		param.put("syncType", "CLOUD");
		List<Menu> menulist = this.menuService.getMenuList(param);
		List<String> ids=new ArrayList<String>();
		Date syncDate = new Date();
		Map<String,Menu> menumap = new HashMap<String,Menu>();
		for(Menu m : menulist){
			menumap.put(m.getCloudId(), m);
			ids.add(m.getId());
		}
		if(ids.size()>0 && isCover==1){
			for(int i=0;i<list.size();i++){
				list.get(i).setSyncType(SYNC_TYPE);
				list.get(i).setSyncDate(syncDate);
			}
//			this.menuService.deleteBatch(ids);
//			this.menuService.addBatch(list);
		}else{
			List<Menu> addlist = new ArrayList<Menu>();
			List<Menu> updatelist = new ArrayList<Menu>();
			for(int i=0;i<list.size();i++){
				Menu m = list.get(i);
				m.setSyncType(SYNC_TYPE);
				m.setSyncDate(syncDate);
				m.setCloudId(m.getId());
				if(menumap.containsKey(m.getId())){
					m = setMenu(m,menumap.get(m.getId()));//更新本地数据部分字段
					updatelist.add(m);
				}else{
					addlist.add(m);
				}
			}
			this.menuService.addBatch(addlist);
			this.menuService.updateBatch(updatelist);
		}
	}
			public Menu setMenu(Menu cloud,Menu local){
				local.setBusinessType(cloud.getBusinessType());
				local.setCloudId(cloud.getCloudId());
				local.setDescription(cloud.getDescription());
				local.setLargeIcon(cloud.getLargeIcon());
				local.setLink(cloud.getLink());
				local.setMenuType(cloud.getMenuType());
				local.setMiniIcon(cloud.getMiniIcon());
				local.setModuleType(cloud.getModuleType());
				local.setName(cloud.getName());
				local.setNumber(cloud.getNumber());
				local.setParent(cloud.getParent());
				local.setPermissionItem(cloud.getPermissionItem());
				local.setSyncDate(cloud.getSyncDate());
				local.setSyncType(cloud.getSyncType());
				return local;
			}
	@Override
	public void syncParam(List<ParamHeader> list,int isCover) {
		if(null==list || list.size()==0) return;
		Map<String, Object>  param=new HashMap<String, Object>();
		param.put("syncType", "CLOUD");
		List<ParamHeader> phlist = this.paramHeaderService.getParamHeaderList(param);
		List<String> ids=new ArrayList<String>();
		Date syncDate = new Date();
		Map<String,ParamHeader> phmap = new HashMap<String,ParamHeader>();
		for(ParamHeader m : phlist){
			phmap.put(m.getCloudId(), m);
			ids.add(m.getId());
		}
		
		if(ids.size()>0 && isCover==1){
			for(int i=0;i<list.size();i++){
				list.get(i).setSyncType(SYNC_TYPE);
				list.get(i).setSyncDate(syncDate);
			}
//			this.paramHeaderService.deleteBatch(ids);
//			this.paramHeaderService.addBatch(list);
		}else{
			List<ParamHeader> addlist = new ArrayList<ParamHeader>();
			List<ParamHeader> updatelist = new ArrayList<ParamHeader>();
			for(int i=0;i<list.size();i++){
				ParamHeader m = list.get(i);
				m.setSyncType(SYNC_TYPE);
				m.setSyncDate(syncDate);
				m.setCloudId(m.getId());
				if(phmap.containsKey(m.getId())){
					m = setParam(m,phmap.get(m.getId()));
					updatelist.add(m);
				}else{
					addlist.add(m);
				}
			}
			this.paramHeaderService.addBatch(addlist);
			this.paramHeaderService.updateBatch(updatelist);
			
			
		}
	}
	public ParamHeader setParam(ParamHeader cloud,ParamHeader local){
		local.setCloudId(cloud.getCloudId());
		local.setDescription(cloud.getDescription());
		local.setModule(cloud.getModule());
		local.setLevel(cloud.getLevel());
		local.setName(cloud.getName());
		local.setNumber(cloud.getNumber());
		local.setSyncDate(cloud.getSyncDate());
	    local.setSyncType(cloud.getSyncType());
		return local;
	}

	@Override
	public String loadSyncData(String url,int isCover) throws Exception {
		    String  msg =null;
		    Map amap = getBaseData(url);
		    Boolean status = false;
		    if(amap.size()>0){
		    if("SUCCESS".equals(amap.get("STATE").toString())){
		    	status = true;
		    	msg = "同步成功";
		    	amap.remove("STATE");
		    }else{
		    	msg = amap.get("MSG").toString();
		    }
		    }else{
		    	msg = "云端无数据可同步";
		    }
		    if(status){
			Map<String, List> map = amap;	
			Set<String> set = map.keySet();
			for(String s : set){
				if(DataSyncContants.MENU.equals(s)&&map.get(s).size()>0){
					List<Menu> pmlist = (List<Menu>)map.get(s);
					syncMenu(pmlist,isCover);
				}else if(DataSyncContants.MODULE.equals(s)&&map.get(s).size()>0){
					List<Module> pmlist = (List<Module>)map.get(s);
					syncModule(pmlist,isCover);
				}else if(DataSyncContants.PARAM.equals(s)&&map.get(s).size()>0){
					List<ParamHeader> pmlist = (List<ParamHeader>)map.get(s);
					syncParam(pmlist,isCover);
				}else if(DataSyncContants.PORTLET.equals(s)&&map.get(s).size()>0){
					List<Portlet> pmlist = (List<Portlet>)map.get(s);
					syncPortlet(pmlist,isCover);
				}else if(DataSyncContants.PERMISSION.equals(s)&&map.get(s).size()>0){
					List<PermissionItem> pmlist = (List<PermissionItem>)map.get(s);
					syncPermission(pmlist,isCover);
				}else if(DataSyncContants.BUSINESS.equals(s)&&map.get(s).size()>0){
					List<BusinessType> pmlist = (List<BusinessType>)map.get(s);
					syncBusiness(pmlist,isCover);
				}else if(DataSyncContants.SQL.equals(s)&&map.get(s).size()>0){
					List<DbScript> pmlist = (List<DbScript>)map.get(s);
					syncSql(pmlist,isCover);
				}else if(DataSyncContants.BILLTYPE.equals(s)&&map.get(s).size()>0){
					List<BillType> pmlist = (List<BillType>)map.get(s);
					syncBillType(pmlist,isCover);
				}else if(DataSyncContants.TASK.equals(s)&&map.get(s).size()>0){
					List<Task> pmlist = (List<Task>)map.get(s);
					syncTask(pmlist,isCover);
				}else if(DataSyncContants.PRINT_CONFIG.equals(s)&&map.get(s).size()>0){
					List<PrintConfig> pmlist = (List<PrintConfig>)map.get(s);
					syncPrintConfig(pmlist,isCover);
				}else if(DataSyncContants.CITY.equals(s)&&map.get(s).size()>0){
					List<City> pmlist = (List<City>)map.get(s);
					syncCity(pmlist,isCover);
				}else if(DataSyncContants.INTERFACE.equals(s)&&map.get(s).size()>0){
					List<APIItem> itemlist = (List<APIItem>)map.get(s);
					List<APIParameter>  aplist = (List<APIParameter>)map.get(DataSyncContants.INTERFACEPARAM);
					syncInterface(itemlist,aplist,isCover);
				}
	
			}
		    }
		    	return msg;


	}

	@Override
	public void syncBusiness(List<BusinessType> list,int isCover) {
		if(null==list || list.size()==0) return;
		Map<String, Object>  param=new HashMap<String, Object>();
		param.put("syncType", "CLOUD");
		List<BusinessType> phlist = this.businessTypeService.getAllBusinessTypes(param);
		List<String> ids=new ArrayList<String>();
		Date syncDate = new Date();
		Map<String,BusinessType> phmap = new HashMap<String,BusinessType>();
		for(BusinessType m : phlist){
			phmap.put(m.getCloudId(), m);
			ids.add(m.getId());
		}
		if(ids.size()>0 && isCover==1){
			for(int i=0;i<list.size();i++){
				list.get(i).setSyncType(SYNC_TYPE);
				list.get(i).setSyncDate(syncDate);
			}
//			this.businessTypeService.deleteBatch(ids);
//			this.businessTypeService.addBatch(list);
		}else{
			List<BusinessType> addlist = new ArrayList<BusinessType>();
			List<BusinessType> updatelist = new ArrayList<BusinessType>();
			for(int i=0;i<list.size();i++){
				BusinessType m = list.get(i);
				m.setSyncType(SYNC_TYPE);
				m.setSyncDate(syncDate);
				m.setCloudId(m.getId());
				if(phmap.containsKey(m.getId())){
					m = setBusinessType(m,phmap.get(m.getId()));
					updatelist.add(m);
				}else{
					addlist.add(m);
				}
			}
			this.businessTypeService.addBatch(addlist);
			this.businessTypeService.updateBatch(updatelist);
		}
	}
	public BusinessType setBusinessType(BusinessType cloud,BusinessType local){
		local.setCloudId(cloud.getCloudId());
		local.setDescription(cloud.getDescription());
        local.setName(cloud.getName());
        local.setNumber(cloud.getNumber());
        local.setSyncDate(cloud.getSyncDate());
        local.setSyncType(cloud.getSyncType());
        local.setType(cloud.getType());
		return local;
	}

	@Override
	public List<SqlUpgrade> syncSql(List<DbScript> list,int isCover) {
		//通过生产环境系统许可证过滤ERP数据库脚本
		List<Module> modlist = this.moduleService.getModuleList(null);
		Map<String,String>  exc = new HashMap<String,String>();
		List<DbScript> newlist = new ArrayList<DbScript>();
		Date now = new Date();
		for(int i = modlist.size()-1;i >=0;i--){
			Module m = (Module)modlist.get(i);
			if(m.getEnd()!=null && m.getStart()!=null && now.compareTo(m.getStart()) > 0 && now.compareTo(m.getEnd()) < 0){	
				exc.put(m.getType().name(), m.getType().name());
			}
		}
		for(int i=0;i<list.size();i++){
			if(exc.containsKey(list.get(i).getModule())){
			   newlist.add(list.get(i))	;
			}	
		}
			list=newlist;//将list替换为过滤后的脚本newlist

		
		//原数据库管理逻辑
		if(null==list || list.size()==0) return null;
		StringBuffer sb = new StringBuffer("");
		for(int i=0;i<list.size();i++){
			sb.append("'").append(list.get(i).getId()).append("',");
		}
		if(sb.length()>0) sb.deleteCharAt(sb.length()-1);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("sqlids", sb.toString());
		List<SqlUpgrade> sulist = sqlUpgradeService.getSqlUpgradeList(param);
		Map<String,SqlUpgrade> phmap = new HashMap<String,SqlUpgrade>();
		for(int i=0;i<sulist.size();i++){
			phmap.put(sulist.get(i).getId(),sulist.get(i));
		}
		List<SqlUpgrade> addlist = new ArrayList<SqlUpgrade>();
		List<SqlUpgrade> updatelist = new ArrayList<SqlUpgrade>();
		SqlUpgrade sutemp = null;
		for(int i=0;i<list.size();i++){
			DbScript m = list.get(i);
			sutemp = convertToSql(m);
			if(phmap.containsKey(m.getId())){
				updatelist.add(sutemp);
			}else{
				sutemp.setRunStatus(SqlRunStatusEnum.WAIT);
				//创建时间即同步时间
				if(StringUtils.isNotNull(m.getFormatCreateTime())){
					sutemp.setCreateTime(DateUtil.convertStrToDateHms(m.getFormatCreateTime()));
				}else{
					sutemp.setCreateTime(new Date());
				}
				addlist.add(sutemp);
			}
		}
		this.sqlUpgradeService.addBatch(addlist);
		this.sqlUpgradeService.updateSqlBatch(updatelist);
		return addlist;
	}
	
	private SqlUpgrade convertToSql(DbScript db){
		SqlUpgrade su = new SqlUpgrade();
		su.setId(db.getId());
		su.setName(db.getName());
		su.setModule(db.getModule());
		su.setOracleScript(db.getOracleScript());
		su.setMysqlScript(db.getMysqlScript());
		return su;
		
	}

	@Override
	public List<SqlUpgrade> loadSyncSql(String url,int isCover) throws Exception {
		Map<String, List> map = getBaseData(url);
		if(null!=map.get(DataSyncContants.SQL)&&map.get(DataSyncContants.SQL).size()>0){
			List<DbScript> pmlist = (List<DbScript>)map.get(DataSyncContants.SQL);
			return syncSql(pmlist,isCover);
		}
		return null;
	}

	@Override
	public void syncBillType(List<BillType> list,int isCover) {
		
		if(null==list || list.size()==0) return;
		Map<String, Object>  param=new HashMap<String, Object>();
		param.put("syncType", "CLOUD");
		Date syncDate = new Date();
		List<BillType> phlist = this.billTypeService.getAllBillTypeList(param);
		List<String> ids=new ArrayList<String>();
		
		Map<String,BillType> phmap = new HashMap<String,BillType>();
		for(BillType m : phlist){
			phmap.put(m.getCloudId(), m);
			ids.add(m.getId());
		}
		if(ids.size()>0 && isCover==1){
//			this.billTypeService.deleteBatch(ids);
//			this.billTypeService.addBatch(list);
		}else{
			List<BillType> addlist = new ArrayList<BillType>();
			List<BillType> updatelist = new ArrayList<BillType>();
			for(int i=0;i<list.size();i++){
				BillType m = list.get(i);
				m.setSyncType(SYNC_TYPE);
				m.setSyncDate(syncDate);
				m.setCloudId(m.getId());
				if(phmap.containsKey(m.getId())){
					m = setBillType(m,phmap.get(m.getId()));
					updatelist.add(m);
				}else{
					addlist.add(m);
				}
			}
			this.billTypeService.addBatch(addlist);
			this.billTypeService.updateBatch(updatelist);
		}
	}
    public BillType setBillType(BillType cloud,BillType local){
    	local.setAddLink(cloud.getAddLink());
    	local.setAddSize(cloud.getAddSize());
    	local.setCheckedIcon(cloud.getCheckedIcon());
    	local.setCloudId(cloud.getCloudId());
    	local.setDefalutIcon(cloud.getDefalutIcon());
    	local.setDeleteLink(cloud.getDeleteLink());
        local.setDescription(cloud.getDescription());
        local.setDisabledIcon(cloud.getDisabledIcon());
        local.setEditLink(cloud.getEditLink());
        local.setEditSize(cloud.getEditSize());
        local.setIdx(cloud.getIdx());
        local.setMobileViewLink(cloud.getMobileViewLink());
        local.setModule(cloud.getModule());
    	local.setName(cloud.getName());
    	local.setNumber(cloud.getNumber());
    	local.setPermissionItem(cloud.getPermissionItem());
    	local.setSyncDate(cloud.getSyncDate());
    	local.setSyncType(cloud.getSyncType());
    	local.setViewLink(cloud.getViewLink());
    	local.setViewSize(cloud.getViewSize());
    	return local;
    }
	
	public void syncTask(List<Task> list,int isCover) {
		if(null==list || list.size()==0) return;
		Map<String, Object>  param=new HashMap<String, Object>();
		param.put("syncType", "CLOUD");
		Date syncDate = new Date();
		List<Task> tlist = this.taskService.getTaskList(param);
		List<String> ids=new ArrayList<String>();
		Map<String,Task> phmap = new HashMap<String,Task>();
		for(Task t : tlist){
			phmap.put(t.getCloudId(), t);
			ids.add(t.getId());
		}
		if(ids.size()>0 && isCover==1){
//			this.taskService.deleteBatch(ids);
//			this.taskService.addBatch(list);
		}else{
			List<Task> addlist = new ArrayList<Task>();
			List<Task> updatelist = new ArrayList<Task>();
			for(int i=0;i<list.size();i++){
				Task m = list.get(i);
				m.setSyncType(SYNC_TYPE);
				m.setSyncDate(syncDate);
				m.setCloudId(m.getId());
				if(phmap.containsKey(m.getId())){
					m = setTask(m,phmap.get(m.getId()));
					updatelist.add(m);
				}else{
					m.setCreateTime(new Date());
					m.setStatus(TaskStatusEnum.WAIT);
					addlist.add(m);
				}
			}
			this.taskService.addBatch(addlist);
			this.taskService.updateBatchSync(updatelist);
		}
	}
	public Task setTask(Task cloud,Task local){
		local.setCloudId(cloud.getCloudId());
		local.setDescription(cloud.getDescription());
		local.setName(cloud.getName());
		local.setParams(cloud.getParams());
		local.setSyncDate(cloud.getSyncDate());
		local.setSyncType(cloud.getSyncType());
		local.setTarget(cloud.getTarget());
		local.setType(cloud.getType());
		return local;
	}
	
	public void syncPrintConfig(List<PrintConfig> list,int isCover) {
		if(null==list || list.size()==0) return;
		Map<String, Object>  param=new HashMap<String, Object>();
		param.put("syncType", "CLOUD");
	    Date syncDate = new Date();
		List<PrintConfig> tlist = this.printConfigService.getList(param);
		List<String> ids=new ArrayList<String>();
		Map<String,PrintConfig> phmap = new HashMap<String,PrintConfig>();
		for(PrintConfig t : tlist){
			phmap.put(t.getCloudId(), t);
			ids.add(t.getId());
		}
		if(ids.size()>0 && isCover==1){
			for(int i=0;i<list.size();i++){
				list.get(i).setSyncType(SYNC_TYPE);
				list.get(i).setSyncDate(syncDate);
			}
//			this.printConfigService.deleteBatch(ids);
//			this.printConfigService.addBatch(list);
		}else{
			List<PrintConfig> addlist = new ArrayList<PrintConfig>();
			List<PrintConfig> updatelist = new ArrayList<PrintConfig>();
			for(int i=0;i<list.size();i++){
				PrintConfig m = list.get(i);
				m.setSyncType(SYNC_TYPE);
				m.setSyncDate(syncDate);
				m.setCloudId(m.getId());
				if(phmap.containsKey(m.getId())){
					m = setPrintConfig(m,phmap.get(m.getId()));
					updatelist.add(m);
				}else{
					addlist.add(m);
				}
			}
			this.printConfigService.addBatch(addlist);
			this.printConfigService.updateBatch(updatelist);
		}
	}
	public PrintConfig setPrintConfig(PrintConfig cloud,PrintConfig local){
		local.setBillType(cloud.getBillType());
		local.setCloudId(cloud.getCloudId());
		local.setDescription(cloud.getDescription());
		local.setFetchConfig(cloud.getFetchConfig());
		local.setFetchType(cloud.getFetchType());
		local.setModule(cloud.getModule());
		local.setName(cloud.getName());
	    local.setNumber(cloud.getNumber());
	    local.setPrintModel(cloud.getPrintModel());
	    local.setSyncDate(cloud.getSyncDate());
	    local.setSyncType(cloud.getSyncType());
		return local;
	}
	
	public void syncInterface(List<APIItem> itemlist,List<APIParameter> aplist,int isCover){
		if(null==itemlist || itemlist.size()==0) return;
		Map<String, Object>  param=new HashMap<String, Object>();
		param.put("syncType", "CLOUD");
	    Date syncDate = new Date();
		List<APIItem> itlist = this.apiItemService.getList(param);
		List<APIParameter> plist = this.apiParameterService.getList(param);
		List<String> iids=new ArrayList<String>();
		List<String> pids=new ArrayList<String>();
		Map<String,APIItem> imap = new HashMap<String,APIItem>();
		Map<String,APIParameter> pmap = new HashMap<String,APIParameter>();
		for(APIItem t : itlist){
			imap.put(t.getCloudId(), t);
			iids.add(t.getId());
		}
		if(iids.size()>0 && isCover==1){
			for(int i=0;i<itemlist.size();i++){
				itemlist.get(i).setSyncType(SYNC_TYPE);
				itemlist.get(i).setSyncDate(syncDate);
			}
//			this.apiItemService.deleteBatch(iids);
//			this.apiItemService.addBatch(itemlist);
		}else{
			List<APIItem> addlist = new ArrayList<APIItem>();
			List<APIItem> updatelist = new ArrayList<APIItem>();
			for(int i=0;i<itemlist.size();i++){
				APIItem at = itemlist.get(i);
				at.setSyncType(SYNC_TYPE);
				at.setSyncDate(syncDate);
				at.setCloudId(at.getId());
				if(imap.containsKey(at.getId())){
					at = setAPIItem(at,imap.get(at.getId()));
					updatelist.add(at);
				}else{
					addlist.add(at);
				}
			}
			this.apiItemService.addBatch(addlist);
			this.apiItemService.updateBatch(updatelist);
		}
		for(APIParameter t : plist){
			pmap.put(t.getCloudId(), t);
			pids.add(t.getId());
		}
		if(pids.size()>0 && isCover==1){
			for(int i=0;i<aplist.size();i++){
				aplist.get(i).setSyncType(SYNC_TYPE);
				aplist.get(i).setSyncDate(syncDate);
			}
//			this.apiParameterService.deleteBatch(pids);
//			this.apiParameterService.addBatch(aplist);
		}else{
			List<APIParameter> addlist = new ArrayList<APIParameter>();
			List<APIParameter> updatelist = new ArrayList<APIParameter>();
			for(int i=0;i<aplist.size();i++){
				APIParameter ap = aplist.get(i);
				ap.setSyncType(SYNC_TYPE);
				ap.setSyncDate(syncDate);
				ap.setCloudId(ap.getId());
				if(pmap.containsKey(ap.getId())){
					ap = setAPIParameter(ap, pmap.get(ap.getId()));
					updatelist.add(ap);
				}else{
					addlist.add(ap);
				}
			}
			this.apiParameterService.addBatch(addlist);
			this.apiParameterService.updateBatch(updatelist);
		}
	}
	public APIItem setAPIItem(APIItem cloud,APIItem local){
		local.setApiUrl(cloud.getApiUrl());
		local.setCloudId(cloud.getCloudId());
		local.setDescription(cloud.getDescription());
		local.setEntityJson(cloud.getEntityJson());
		local.setModuleType(cloud.getModuleType());
		local.setName(cloud.getName());
		local.setNumber(cloud.getNumber());
		local.setParamDeclare(cloud.getParamDeclare());
		local.setReturnsDeclare(cloud.getReturnsDeclare());
		local.setSyncDate(cloud.getSyncDate());
		local.setSyncType(cloud.getSyncType());
		return local;
	}
	public APIParameter setAPIParameter(APIParameter cloud,APIParameter local){
		local.setCloudId(cloud.getCloudId());
		local.setDescription(cloud.getDescription());
		local.setIsNotNull(cloud.getIsNotNull());
		local.setItem(cloud.getItem());
        local.setName(cloud.getName());
        local.setNumber(cloud.getNumber());
        local.setSyncDate(cloud.getSyncDate());
        local.setSyncType(cloud.getSyncType());
        local.setType(cloud.getType());
		return local;
	}
	public void syncCity(List<City> list,int isCover) {
		if(null==list || list.size()==0) return;
		Map<String, Object>  param=new HashMap<String, Object>();
		param.put("syncType", "CLOUD");
		Date syncDate = new Date();
		List<City> tlist = this.cityService.getList(param);
		List<String> ids=new ArrayList<String>();
		Map<String,City> phmap = new HashMap<String,City>();
		for(City t : tlist){
			phmap.put(t.getCloudId(), t);
			ids.add(t.getId());
		}
		if(ids.size()>0 && isCover==1){
//			this.cityService.deleteBatch(ids);
//			this.cityService.addBatch(list);
		}else{
			List<City> addlist = new ArrayList<City>();
			List<City> updatelist = new ArrayList<City>();
			for(int i=0;i<list.size();i++){
				City m = list.get(i);
				m.setSyncType(SYNC_TYPE);
				m.setSyncDate(syncDate);
				m.setCloudId(m.getId());
				if(phmap.containsKey(m.getId())){
					m = setCity(m,phmap.get(m.getId()));
					updatelist.add(m);
				}else{
					addlist.add(m);
				}
			}
			this.cityService.addBatch(addlist);
			this.cityService.updateBatch(updatelist);
		}
	}
	public City setCity(City cloud,City local){
		local.setCloudId(cloud.getCloudId());
		local.setDescription(cloud.getDescription());
		local.setFullName(cloud.getFullName());
		local.setIsModel(cloud.getIsModel());
		local.setName(cloud.getName());
		local.setNumber(cloud.getNumber());
		local.setParent(cloud.getParent());
		local.setSimpleName(cloud.getSimpleName());
		local.setSyncDate(cloud.getSyncDate());
		local.setSyncType(cloud.getSyncType());
		return local;
	}
	
	public void syncPortlet(List<Portlet> list,int isCover) {
		if(null==list || list.size()==0) return;
		Map<String, Object>  param=new HashMap<String, Object>();
		param.put("syncType", "CLOUD");
		Date syncDate = new Date();
		List<Portlet> tlist = this.portletService.getListForSync(param);
		List<String> ids=new ArrayList<String>();
		Map<String,Portlet> phmap = new HashMap<String,Portlet>();
		for(Portlet t : tlist){
			phmap.put(t.getCloudId(), t);
			ids.add(t.getId());
		}
		if(ids.size()>0 && isCover==1){
//			this.portletService.deleteBatch(ids);
//			this.portletService.addBatch(list);
		}else{
			List<Portlet> addlist = new ArrayList<Portlet>();
			List<Portlet> updatelist = new ArrayList<Portlet>();
			for(int i=0;i<list.size();i++){
				Portlet m = list.get(i);
				m.setSyncType(SYNC_TYPE);
				m.setSyncDate(syncDate);
				m.setCloudId(m.getId());
				if(phmap.containsKey(m.getId())){
					m = setPortlet(m,phmap.get(m.getId()));
					updatelist.add(m);
				}else{
					addlist.add(m);
				}
			}
			this.portletService.addBatch(addlist);
			this.portletService.updateBatch(updatelist);
		}
	}
	public Portlet setPortlet(Portlet cloud,Portlet local){
		local.setBusinessType(cloud.getBusinessType());
		local.setCloudId(cloud.getCloudId());
		local.setConfigPageSize(cloud.getConfigPageSize());
		local.setConfigUrl(cloud.getConfigUrl());
		local.setDescription(cloud.getDescription());
		local.setFormatSize(cloud.getFormatSize());
		local.setJsName(cloud.getJsName());
		local.setLayout(cloud.getLayout());
		local.setName(cloud.getName());
		local.setNumber(cloud.getNumber());
		local.setSyncDate(cloud.getSyncDate());
		local.setSyncType(cloud.getSyncType());
		local.setUrl(cloud.getUrl());
		return local;
	}
	
}
