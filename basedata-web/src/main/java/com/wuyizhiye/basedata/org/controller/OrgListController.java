package com.wuyizhiye.basedata.org.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.module.enums.BusinessTypeEnum;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.TreeEntity;
import com.wuyizhiye.basedata.bank.model.City;
import com.wuyizhiye.basedata.code.model.Area;
import com.wuyizhiye.basedata.org.enums.OrgTypeEnum;
import com.wuyizhiye.basedata.org.model.BusinessType;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.OrgLevelDesc;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.org.service.OrgHistoryService;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.basedata.param.model.ParamConstants;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.TreeListController;
import com.wuyizhiye.framework.qqmial.model.QQToken;
import com.wuyizhiye.framework.qqmial.util.QQMailUtil;

/**
 * @ClassName OrgListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/org/*")
public class OrgListController extends TreeListController {
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private OrgHistoryService orgHistoryService;
	
	@Autowired
	private PositionService positionService;
	
	@Override
	protected CoreEntity createNewEntity() {
		String parentId = getString("parent");
		Org org = new Org();
		org.setLeaf(true);
		org.setOrgType(OrgTypeEnum.TEAM);
		if(!StringUtils.isEmpty(parentId)){
			Org parent = orgService.getEntityById(parentId);
			org.setParent(parent);
		}
		org.setEffectiveDate(new Date());
		return org;
	}
	
	@Override
	protected String getListView() {
		this.getRequest().setAttribute("isBusinessType", this.hasPermission("cf38e4c9-5d2d-4b52-b1d8-b8808e7fc99b"));//业务类型权限
		this.getRequest().setAttribute("isOrglevel", this.hasPermission("5331c602-397a-490d-aba8-8365b7c5a2ae"));//组织级别权限
		this.getRequest().setAttribute("deleteOrg", this.hasPermission("9b886a17-a051-4398-b348-84aa86d73c35"));//组织级别权限
		return "basedata/org/orgList";
	}

	@Override
	protected String getEditView() {
		getRequest().setAttribute("orgTypes", OrgTypeEnum.values());
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("enable", true);
		Map<String,Object> businessList = new LinkedHashMap<String, Object>();
		for (BusinessTypeEnum bte : BusinessTypeEnum.values()) {
			param.put("type",bte.getValue());
			getRequest().setAttribute(bte.getValue(), getBusinessTypeList(param));
			businessList.put(bte.getLabel(), getBusinessTypeList(param));
		}
		getRequest().setAttribute("businessList", businessList);
		List<OrgLevelDesc> orglevellist = 
		this.queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.OrgLevelDescDao.select", null, OrgLevelDesc.class);
		getRequest().setAttribute("orglevellist", orglevellist);
		getRequest().setAttribute("dataType", getString("dataType"));//是否复制新增
		String move = this.getString("move");
		getRequest().setAttribute("move", move);
		if(!"ADD".equals(this.getString("VIEWSTATE"))){
			Org org = orgService.getEntityById(this.getString("id"));
			if(org!=null){
				String citys = org.getCityIds();
				if(citys!=null&&citys!=""){
				String[] list = citys.split(",");
				String ids = "";
				if(list.length>0){
					for(int i=0;i<list.length;i++){
						if(i==(list.length-1)){
							ids = ids+"'"+list[i]+"'";
						}else{
							ids = ids+"'"+list[i]+"',"; 
						}
					}
				}
				Map<String,Object> pa = new HashMap<String,Object>();
				pa.put("ids", ids);
				List<City> citylist = this.queryExecutor.execQuery("com.wuyizhiye.basedata.bank.dao.CityDao.select", pa, City.class);
				getRequest().setAttribute("citylist", citylist);
			}
			}
		}
		return "basedata/org/orgEdit";
	}
	protected List<BusinessType> getBusinessTypeList(Map<String,Object> param){
	    return queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.BusinessTypeDao.select", param, BusinessType.class);
   }
	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.org.dao.OrgDao.select";	
	}

	@Override
	protected BaseService<Org> getService() {
		return orgService;
	}

	@Override
	protected String getTreeDataMapper() {
		return "com.wuyizhiye.basedata.org.dao.OrgDao.getChild";
	}
	
	/**
	 * ${base}/basedata/org/orgDataPicker?multi=true为多选
	 * @param model
	 * @return
	 */
	@RequestMapping(value="orgDataPicker")
	public String orgDataPicker(ModelMap model){
		Map<String,String> param = getParamMap();
		Set<String> keys = param.keySet();
		for(String s : keys){
			model.put(s, param.get(s));
		}
		return "basedata/org/orgDataPicker";
	}
	@RequestMapping(value="orgCityPicker")
	public String orgCityPicker(ModelMap model){
		Map<String,String> param = getParamMap();
		Set<String> keys = param.keySet();
		for(String s : keys){
			model.put(s, param.get(s));
		}
		return "basedata/org/orgCityPicker";
	}
	
	@RequestMapping(value="orgList")
	public void orgList(ModelMap model,HttpServletResponse response){
		String key  = getString("term");
		int maxRows  = getInt("maxRows");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("key", key);
		//map.put("level", 7);
		map.put("longNumber", SystemUtil.getCurrentOrg().getLongNumber());
		this.putControlUnitIdToMap(map);
		Pagination<Org> pagination=new Pagination<Org>(maxRows, 0);
		pagination = queryExecutor.execQuery(getListMapper(), pagination, map);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	/**
	 * 延迟加载方式获取树数据
	 * @param response
	 */
	@RequestMapping(value="orgDataTree")
	public void orgDataTree(HttpServletResponse response){
		String root = getString("root");
		String parent = getString("parent");
		String includeChild = getString("includeChild");
		Map<String,Object> param = getTreeFilterParam();
		String flag = this.getString("flag");//by wlx
		
		/****************** 根据当前登录人判断 start******************/
		Org currentPersonOrg = orgService.getEntityById(SystemUtil.getCurrentOrg().getId());
		if(!(OrgUtils.isType(currentPersonOrg, "T02") || OrgUtils.isType(currentPersonOrg, "T01"))){	//如果当前组织的业务类型为 HR业务,财务业务的话，则可以看到所有的组织
			if(StringUtils.isEmpty(flag) && currentPersonOrg.getParent() != null && parent == null){
				parent = currentPersonOrg.getParent().getId();
				param.put("longNumber", currentPersonOrg.getLongNumber());
				
			}
		}
		/**************** end ***************/
		
		CoreEntity pare = null;
		if(!StringUtils.isEmpty(parent)){
			pare = getTreeService().getEntityById(parent);
			if(pare!=null){
				if(!StringUtils.isEmpty(includeChild) && Boolean.getBoolean(includeChild)==true){
					param.put("includeChild", includeChild);
					param.put("longNumber", ((TreeEntity)pare).getLongNumber());
				}else{
					param.put("parent", pare.getId());
				}
			}
			List<Org> orgs = queryExecutor.execQuery(getTreeDataMapper(), param,Org.class);
			String result = JSONArray.fromObject(orgs, getDefaultJsonConfig()).toString();
			outPrint(response, result);
		}else{
			if(!StringUtils.isEmpty(root)){
				List<Org> orgs = new ArrayList<Org>();
				Org org = orgService.getEntityById(root);
				orgs.add(org);
				outPrint(response, JSONArray.fromObject(orgs, getDefaultJsonConfig()).toString());
			}else{
				List<Org> orgs = queryExecutor.execQuery(getTreeDataMapper(), param,Org.class);
				String result = JSONArray.fromObject(orgs, getDefaultJsonConfig()).toString();
				outPrint(response, result);
			}
		}
		
	}
	
	/**
	 * dataPicker取组织数据
	 */
	@RequestMapping(value="orgData")
	public void orgData(Pagination<?> pagination,HttpServletResponse response){
		Map<String,Object> param = getListDataParam();
		if(param.get("parent")==null && param.get("longNumber")==null){
			param.put("parent", "nodata");
		}
		pagination = queryExecutor.execQuery(getListMapper(), pagination, param);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	
	@RequestMapping(value="positionSetting")
	public String positionSetting(ModelMap model){
		String orgId = getString("org");
		Org org = orgService.getEntityById(orgId);
		model.put("org", org);
		List<Position> positions = positionService.getByOrg(orgId);
		model.put("positions", positions);
		return "basedata/org/positionSetting";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value="positionSave")
	public void positionSave(HttpServletResponse response){
		String orgId = getString("org.id");
		String posJson = getString("positionJson");
		if(!StringUtils.isEmpty(orgId)){
			Collection<Position> posColl = JSONArray.toCollection(JSONArray.fromObject(posJson), Position.class);
			List<Position> positions = new ArrayList<Position>(posColl);
			Org org = new Org();
			org.setId(orgId);
			for(Position p : positions){
				p.setBelongOrg(org);
			}
			positionService.savePositions(org,positions);
		}
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return "com.wuyizhiye.basedata.org.dao.OrgDao.getSimpleTreeData";
	}
	
	@RequestMapping(value="delete")
	@Dependence(method="list")
	public void delete(@RequestParam(required=true,value="id")String id,HttpServletResponse response){
		Org entity = getService().getEntityById(id);
		if(entity!=null){
			if(isAllowDelete(entity)){
				if("000000000000000000000000000000F".equals(entity.getId())){
					getOutputMsg().put("MSG", "该组织是顶级组织不能删除！");
				}else{
					getService().deleteEntity(entity);
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "删除成功");
					//把组织从QQ企业邮箱中删除
					if(!StringUtils.isEmpty(entity.getDisplayName())){
						Object token = getSession().getAttribute("qqmail_token");
						if(token != null){
							QQToken qqMailToken = (QQToken) token ;
							QQMailUtil.syncParty(qqMailToken, entity, 1 ,null);
						}
					}
				}
			}else{
				getOutputMsg().put("STATE", "FAIl");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@Override
	protected boolean isAllowDelete(CoreEntity entity) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("parent", entity.getId());
		if(queryExecutor.execCount("com.wuyizhiye.basedata.org.dao.OrgDao.select", param)>0){
			setOutputMsg("MSG", "存在下级组织,不能删除");
			return false;
		}
//		if(!OrgStatusEnum.DISABLED.equals(((Org)entity).getStatus())){
//			setOutputMsg("MSG", "只能删除禁用状态下的组织");
//			return false;
//		}
		/*param.put("belongOrg", entity.getId());
		if(queryExecutor.execCount("com.wuyizhiye.basedata.org.dao.PositionDao.select", param)>0){
			setOutputMsg("MSG", "该组织下已经创建职位,请先删除职位信息");
			return false;
		}*/
		
		return super.isAllowDelete(entity);
	}
	
	
	/**
	 * 启用/禁用
	 * @param org 
	 * @param response
	 */
	@RequestMapping(value="enable")
	public void enable(Org org,HttpServletResponse response){
		Org old = orgService.getEntityById(org.getId());
		old.setStatus(org.getStatus());
		orgService.updateEntity(old);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	
	/**
	 * by htf  将 orgTypes 业务的组织  用 "," 号分割 
	 * @param orgTypes
	 * @return
	 */
	private String getOrgTypes(String orgTypes){
		if(!StringUtils.isEmpty(orgTypes)){
			StringBuilder   strBuilder = new StringBuilder("");
			if(orgTypes.indexOf(",")==-1){
				return "'"+orgTypes+"'";
			}
			String[]  orgArr= orgTypes.split(",");
			for(String o : orgArr){
				strBuilder.append("'").append(o).append("',");
			}
			if(strBuilder.length() > 1){
				strBuilder = strBuilder.deleteCharAt(strBuilder.length()-1);
			}
			return strBuilder.toString();
		}
		return null;
	}
	
	
	/**
	 * 列表数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value = "listDeptStoreData")
	public void listDeptStoreData(Pagination<?> pagination, HttpServletResponse response) {
		Map<String,Object> param = super.getParaMap();
		param.put("orgTypes",getOrgTypes(getString("orgTypes")));
		pagination = queryExecutor.execQuery(getListMapper(), pagination,param);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("items", pagination.getItems());
		page.put("recordCount", pagination.getRecordCount());
		outPrint(response, JSONObject.fromObject(page, getDefaultJsonConfig()));
	}
	
	/**
	 * ${base}/basedata/org/deptStoreList?
	 * @param model
	 * @return
	 */
	@RequestMapping(value="orgDeptStoreList")
	public String orgDeptStoreList(ModelMap model){
		Map<String,String> param = getParamMap();
		Set<String> keys = param.keySet();
		for(String s : keys){
			model.put(s, param.get(s));
		}
		return "basedata/org/orgDeptStoreList";
	}
	
	/**
	 * ${base}/basedata/org/orgHisDataPicker?multi=true为多选
	 * @param model
	 * @return
	 */
	@RequestMapping(value="orgHisDataPicker")
	public String orgHisDataPicker(ModelMap model){
		Map<String,String> param = getParamMap();
		Set<String> keys = param.keySet();
		for(String s : keys){
			model.put(s, param.get(s));
		}
		return "basedata/org/orgHisDataPicker";
	}
	
	/**
	 * 历史组织树
	 * @return
	 */
	protected String getOrgTreeDataMapper() {
		return "com.wuyizhiye.basedata.org.dao.OrgHistoryDao.getHistoryOrgTree";
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="orgHisTreeData")
	public void orgHisTreeData(HttpServletResponse response){
		Map<String,Object> parameters = new HashMap<String, Object>();
		String longNumber = getString("longNumber");
		Date recordDate = null;
		String period = getString("period");
		String isLastDay = getString("isLastDay");
		if(StringUtils.isNotNull(period)){
		 recordDate = DateUtil.convertStrToDate(period, "yyyy-MM");
		}else{
			recordDate = new Date();
		}
		
		Date endDate = DateUtil.getLastDay(recordDate);//考勤月 最后一天
		Date begDate = endDate;
		if(!"Y".equals(isLastDay)){
			begDate = DateUtil.getFirstDay(recordDate);//考勤月 第一天
		}
		parameters.put("begDate", begDate);
		parameters.put("endDate", endDate);
		List<Map> treeData = new ArrayList<Map>();
		if(StringUtils.isNotNull(longNumber)){
		  Map<String,Object> p = new HashMap<String, Object>();
		  p.put("id",SystemUtil.getCurrentOrg().getId());
		  p.put("begDate", begDate);
		  p.put("endDate", endDate);
		  List<Map> orgHis = queryExecutor.execQuery(getOrgTreeDataMapper(), p, Map.class);
		  if(orgHis==null){
			  orgHis  = new ArrayList<Map>();
		  }
		  for(Map o : orgHis){
			  parameters.put("longnumber", o.get("longNumber"));
			  List<Map> childOrg = queryExecutor.execQuery(getOrgTreeDataMapper(), parameters, Map.class);
			  if(childOrg!=null && childOrg.size()>0){
			    treeData.addAll(childOrg);
			  }
		  }
		  
		}else{
		 //不用长编码
		 treeData = queryExecutor.execQuery(getOrgTreeDataMapper(), parameters, Map.class);
		}
		outPrint(response, JSONArray.fromObject(treeData, getDefaultJsonConfig()).toString());
	}
	
	protected String getOrgHisListMapper() {
		return "com.wuyizhiye.basedata.org.dao.OrgHistoryDao.getHistoryOrg";
	}
	
	/**
	 * orgHisDataPicker取组织数据
	 */
	@RequestMapping(value="orgHisData")
	public void orgHisData(Pagination<?> pagination,HttpServletResponse response){
		Map<String,Object> param = getListDataParam();
		if(param.get("parent")==null && param.get("longnumber")==null){
			param.put("parent", "nodata");
		}
		Date recordDate = null;
		String period = getString("period");
		String isLastDay = getString("isLastDay");
		if(StringUtils.isNotNull(period)){
		 recordDate = DateUtil.convertStrToDate(period, "yyyy-MM");
		}else{
			recordDate = new Date();
		}
		recordDate = DateUtil.getLastDay(recordDate);//考勤月 最后一天
		Date begDate = recordDate;
		if(!"Y".equals(isLastDay)){
			begDate = DateUtil.getFirstDay(recordDate);//考勤月 第一天
		}
		param.put("begDate", recordDate);
		param.put("endDate", begDate);
		/*Map<String,Object> p = new HashMap<String, Object>();
		  p.put("id",SystemUtil.getCurrentOrg().getId());
		  p.put("begDate", begDate);
		  p.put("endDate", recordDate);
		  List<Map> orgHis = queryExecutor.execQuery(getOrgTreeDataMapper(), p, Map.class);
		  if(orgHis!=null && orgHis.size()>0){
			  param.put("longnumber", orgHis.get(0).get("longNumber"));
		  }*/
		  
		  pagination = queryExecutor.execQuery(getOrgHisListMapper(), pagination, param);
		  
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="getOrgLevel")
	public void getOrgLeave(Pagination<?> pagination,HttpServletResponse response){
		Org org=this.orgService.getEntityById(getString("id"));
		if(!StringUtils.isEmpty(org.getOrgLevelDesc())){
			Map<String, Object>param=new HashMap<String, Object>();
			param.put("orgleveldesc", org.getOrgLevelDesc());
			pagination=queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.OrgLevelDescDao.select", pagination, param);
		}		
		outPrint(response, JSONObject.fromObject(pagination));
	}
	@RequestMapping(value="toMapMark")
	public String toMapMark(ModelMap model, HttpServletResponse response){
		 Org current = orgService.getEntityById(getString("fid"));
		 Org parent = current.getParent();
		 if(parent!=null){
			 parent = orgService.getEntityById(current.getParent().getId());
		 }
		 
		model.put("id", getString("fid"));
		model.put("mapx", current.getMapx());
		model.put("mapy", current.getMapy());
		model.put("zoom", current.getZoom());
		model.put("orgName", current.getName());
		//如果未标过点，则设置默认数据
		if(!StringUtils.isNotNull(current.getMapx())){
			if(parent!=null&&parent.getMapoint()!=null&&parent.getMapoint()==1){
				model.put("defaultMapx", parent.getMapx());
				model.put("defaultMapy", parent.getMapy());
				model.put("defaultZoom", parent.getZoom());	
			}else{
		model.put("defaultMapx", ParamUtils.getParamValue(ParamConstants.DEFAULT_MAPX_KEY));
		model.put("defaultMapy", ParamUtils.getParamValue(ParamConstants.DEFAULT_MAPY_KEY));
		model.put("defaultZoom", ParamUtils.getParamValue(ParamConstants.DEFAULT_ZOOM_KEY));
			}
		}
		return "basedata/org/orgMapMark";
	}
}
