package com.wuyizhiye.basedata.person.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.TreeEntity;
import com.wuyizhiye.basedata.basic.model.BasicData;
import com.wuyizhiye.basedata.basic.service.BasicDataService;
import com.wuyizhiye.basedata.enums.PersonShowTypeEnum;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.person.enums.CardTypeEnum;
import com.wuyizhiye.basedata.person.enums.JobStatusEnum;
import com.wuyizhiye.basedata.person.enums.OperateTypeEnum;
import com.wuyizhiye.basedata.person.enums.PersonStatusEnum;
import com.wuyizhiye.basedata.person.enums.PositionChangeTypeEnum;
import com.wuyizhiye.basedata.person.enums.RecruitMethodEnum;
import com.wuyizhiye.basedata.person.enums.SexEnum;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.basedata.person.enums.WedStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.basedata.person.service.PersonPositionHistoryService;
import com.wuyizhiye.basedata.person.service.PersonPositionService;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName PersonListController
 * @Description 职员列表controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/person/*")
public class PersonListController extends TreeListController {
	@Autowired
	private PersonService personService;
	@Autowired 
	private PersonPositionService personPositionService;
	
	@Resource
	private PersonPositionHistoryService personPositionHistoryService;
	
	@Autowired
	private OrgService orgService;
	@Autowired
	private BasicDataService basicDataService;
	@Override
	protected String getTreeDataMapper() {
		return "com.wuyizhiye.basedata.org.dao.OrgDao.getChild";
	}

	@Override
	protected CoreEntity createNewEntity() {
		String orgid = getString("parent");
		if(!StringUtils.isEmpty(orgid)){
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("belongOrg", orgid);
			List<Position> positions = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.PositionDao.select", param, Position.class);
			if(positions.size() > 0){
				List<PersonPosition> personPosition = new ArrayList<PersonPosition>();
				PersonPosition pp = new PersonPosition();
				pp.setPosition(positions.get(0));
				pp.setPrimary(true);
				personPosition.add(pp);
				getRequest().setAttribute("personPositions", personPosition);
			}
		}
		return new Person();
	}

	@RequestMapping(value="checkNumber")
	public void checkNumber(String personName,HttpServletResponse response){
		Map<String , Object> params = new HashMap<String, Object>();
		params.put("name", personName);
		List<Person> catelogs = queryExecutor.execQuery(Person.MAPPER+".select", params, Person.class);
		if(catelogs!=null && catelogs.size()>0){
			getOutputMsg().put("STATE", "SUCCESS");
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	@Override
	public void treeData(HttpServletResponse response) {
		// TODO Auto-generated method stub
		String parent = getString("parent");
		String includeChild = getString("includeChild");
		Map<String,Object> param = getTreeFilterParam();
		String flag = this.getString("flag");
		if(!(!StringUtils.isEmpty(flag) && "addressBook".equalsIgnoreCase(flag))){
			Org org = orgService.getEntityById(SystemUtil.getCurrentOrg().getId());
			if(org.getParent() != null && parent == null){
				parent = org.getParent().getId();
				param.put("longNumber", org.getLongNumber());
			}
		}
		
		CoreEntity pare = null;
		if(!StringUtils.isEmpty(parent)){
			pare = getTreeService().getEntityById(parent);
		}
		if(pare!=null){
			if(!StringUtils.isEmpty(includeChild) && Boolean.getBoolean(includeChild)==true){
				param.put("includeChild", includeChild);
				param.put("longNumber", ((TreeEntity)pare).getLongNumber());
			}else{
				param.put("parent", pare.getId());
			}
		}
		Pagination<Object> page = new Pagination<Object>(Integer.MAX_VALUE,0);
		page = queryExecutor.execQuery(getTreeDataMapper(), page, param);
		String result = JSONArray.fromObject(page.getItems(), getDefaultJsonConfig()).toString();
		outPrint(response, result);
	}

	@Override
	protected String getListView() {
		/******************* 判断是否有权限 start **********************/
		String personPermissionId = "b9d274a7-c800-4dbe-94e6-5311a346ccec";
		boolean hasPermission = this.hasPermission(personPermissionId);
		this.getRequest().setAttribute("hasPermission", hasPermission);
		//*******************判断是否有登陆设置的权限*******************
		String loginSetPermissionId="4174754c-202f-40b8-8239-9abe82795af2";
		boolean hasLoginSetPermission=this.hasPermission(loginSetPermissionId);
		this.getRequest().setAttribute("hasLoginSetPermission", hasLoginSetPermission);
		/************** end *****************/
		return "basedata/person/personList";
	}

	@Override
	public String add(ModelMap model) {
		this.putEnumsToModelMap(model);
		return super.add(model);
	}
	
	/**
	 * 将枚举放到modelmap中
	 * @param model
	 */
	private void putEnumsToModelMap(ModelMap model){
		//婚姻状况
		model.put("wedStatusList", WedStatusEnum.toList());
		//招聘渠道
		model.put("recruitmethodList", RecruitMethodEnum.toList());
		//员工状态
		model.put("personStatusList", PersonStatusEnum.toList());
		//岗位状态
		model.put("jobStatusList", JobStatusEnum.toList());
		//学历
	}

	@Override
	protected String getEditView() {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("typenumber", "GWZT");//岗位状态编码
		param.put("enableflag", 1);
		param.put("order", "updateTime");
		
		List<BasicData> jobStatus = basicDataService.getBasicDataList(param);
		getRequest().setAttribute("jobStatus", jobStatus);
		
		//TODO 此处添加证件类型,操作人:wen
		getRequest().setAttribute("cardTypes", CardTypeEnum.values());
		
		//TODO 此处添加证件类型结束,操作人:wen
		
		//TODO 人员在通讯录上显示的信息类型,操作人:sht
		getRequest().setAttribute("showTypes", PersonShowTypeEnum.values());
		
		return "basedata/person/personEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.person.dao.PersonDao.getPersonInfoByOrg";
	}
	
	@RequestMapping(value="card")
	public String card(@RequestParam(value="id",required=false)String id,ModelMap model){
		if(StringUtils.isEmpty(id)){
			id = SystemUtil.getCurrentUser().getId();
		}
		Person person = personService.getEntityById(id);
		List<PersonPosition> pps = personPositionService.getByPerson(id);
		if(pps.size()>0){
			person.setPersonPosition(pps.get(0));
		}
		model.put("data", person);
		
		return "basedata/person/card";
	}
	
	@RequestMapping(value="simpleList")
	public String simpleList(){
		return "basedata/person/simplePersonList";
	}
	
	/**
	 * 人员分页数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="simpleListData")
	public void simpleListData(Pagination<Person> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.select", pagination, getListDataParam());
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	/**
	 * 按组织查询人员,传入参数组织编码
	 * @param response
	 */
	@RequestMapping(value="selectPersonListByOrg")
	public void selectPersonListByOrg(HttpServletResponse response){
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		List<Person> list = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.select", getListDataParam(),Person.class);
		outPrint(response, JSONArray.fromObject(list, getDefaultJsonConfig()));
	}
	
	/**
	 * 按组织查询人员(不包括离职),传入组织ID
	 * @param response
	 */
	@RequestMapping(value="selectPersonByOrg")
	public void selectPersonByOrg(HttpServletResponse response){
		List<Person> list = queryExecutor.execQuery(Person.MAPPER+".selectPersonByOrg", getListDataParam(),Person.class);
		outPrint(response, JSONArray.fromObject(list, getDefaultJsonConfig()));
	}
	
	/**
	 * 按组织查询人员(包括离职),传入组织ID
	 * @param response
	 */
	@RequestMapping(value="selectAllPersonByOrg")
	public void selectAllPersonByOrg(HttpServletResponse response){
		List<Person> list = queryExecutor.execQuery(Person.MAPPER+".selectPersonByOrgId", getListDataParam(),Person.class);
		queryExecutor.getExecuteSql(Person.MAPPER+".selectPersonByOrgId", getListDataParam());
		outPrint(response, JSONArray.fromObject(list, getDefaultJsonConfig()));
	}

	@Override
	protected BaseService<Person> getService() {
		return personService;
	}
	
	@Override
	protected BaseService<Org> getTreeService() {
		return orgService;
	}
	
	//编辑界面
	@Override
	public String edit(ModelMap model, @RequestParam(required=true,value="id")String id) {
		//任职信息
		List<PersonPosition> personPosition = personPositionService.getByPerson(id);
		model.put("personPositions", personPosition);
		return super.edit(model, id);
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return null;
	}
	
	
	/**
	 * 人员F7(CU限制)
	 * url带参:
	 * 样式:(左树右表)type=tree
	 *        (纯列表)type=list
	 *                --- 指定根节点root:orgid
	 *  左树限制: |--     
	 * 
	 *                ---【指定组织范围(树形无效):orgLongNumber=org.longNumber 注意组织长编码后带感叹号!则不包含本组织
	 *  右表过滤 ｜---  指定岗位:pt=XXXXX  参数见系统参数里面所设置岗位编码
	 *                ---【指定组织(树形无效):org=orgid
	 * @param model
	 * @return
	 */
	@RequestMapping(value="dataPicker")
	public String dataPicker(ModelMap model){
		String type = getString("type");//样式
		String orgLongNumber = getString("orgLongNumber");
		String org = getString("org");
		String pt = getString("pt");
		String curPerson = getString("cp"); //是否需要过滤当前人 hetengfeng
		String validstatus = getString("validstatus");
		if(!StringUtils.isEmpty(curPerson)){
			model.put("curPerson", SystemUtil.getCurrentUser().getId());
		}
		model.put("orgLongNumber", orgLongNumber);
		model.put("org", org);
		model.put("multi", getString("multi"));
		model.put("personIds", getString("personIds"));
		model.put("validstatus", validstatus);
		if(!StringUtils.isEmpty(pt)){
			String[] pts = pt.split(",");
			StringBuilder jobs = new StringBuilder("nodata;"); 
			for(String p : pts){
				if(!StringUtils.isEmpty(p)){
					String jobNumber = ParamUtils.getParamValue(null, p);
					if(!StringUtils.isEmpty(jobNumber)){
						Map<String,Object> param = new HashMap<String, Object>();
						param.put("number", jobNumber);
						this.putControlUnitIdToMap(param);
						Job job = queryExecutor.execOneEntity("com.wuyizhiye.basedata.org.dao.JobDao.select", param, Job.class);
						if(job!=null){
							jobs.append(job.getId()).append(";");
						}
					}
				}
			}
			if(jobs.length() > 0){
				model.put("job", jobs.toString());
			}
		}
		if(!StringUtils.isEmpty(type)){
			String root = getString("root");
			if(!StringUtils.isEmpty(root)){
				Org o = orgService.getEntityById(root);
				if(o == null){
					model.put("rootLongNumber", "nodata");
				}else{
					model.put("rootLongNumber", o.getLongNumber());
				}
			}
			return "basedata/person/treeDataPicker";
		}
		return "basedata/person/dataPicker";
	}
	
	@RequestMapping(value="dataPickerData")
	public void dataPickerData(Pagination<Person> pagination, HttpServletResponse response){
		//TODO 待加CU过滤
		Map<String,Object> param = getListDataParam();
		if(param.get("job")!=null){
			String jobs = param.get("job").toString();
			jobs = jobs.replace(";", "','");
			jobs = "'" + jobs + "'";
			param.put("job", jobs);
		}
		if(param.get("historyDate")!=null){ //如果任职历史日期查询条件不为空  则查询任职历史
			pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.dataPickerHistor", pagination, param);
		}else{
			pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.dataPicker", pagination, param);
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	@Override
	protected JsonConfig getDefaultJsonConfig() {
		JsonConfig config = super.getDefaultJsonConfig();
		config.registerJsonValueProcessor(UserStatusEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof UserStatusEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((UserStatusEnum)value).getName());
					json.put("value", ((UserStatusEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof UserStatusEnum){
					return ((UserStatusEnum)value).getName();
				}
				return null;
			}
		});
		config.registerJsonValueProcessor(SexEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SexEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((SexEnum)value).getName());
					json.put("value", ((SexEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SexEnum){
					return ((SexEnum)value).getName();
				}
				return null;
			}
		});
		return config;
	}
	
	
	@RequestMapping(value="personList")
	public void personList(ModelMap model,HttpServletResponse response){
		String key  = getString("term");
		int maxRows  = getInt("maxRows");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("key", key);
		Pagination<Person> pagination=new Pagination<Person>(maxRows, 0);
		pagination = queryExecutor.execQuery(getListMapper(), pagination, map);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	/**
	 * 人员指定
	 * @return
	 */
	@RequestMapping(value="personAssignList")
	protected String personAssignList() {
	 
		return "basedata/person/personAssignList";
	}
	
	/**
	 * 人员指定 列表数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="personAssignListData")
	@Dependence(method="list")
	public void personAssignListData(Pagination<?> pagination,HttpServletResponse response){
		String mapper = "com.wuyizhiye.basedata.person.dao.PersonDao.getPerson4Assign";
		pagination = queryExecutor.execQuery(mapper, pagination, getListDataParam());
		afterFetchListData(pagination);
		outPrint(response, JSONObject.fromObject(pagination, getPersonAssignJsonConfig()));
	}
	
	protected JsonConfig getPersonAssignJsonConfig() {
		JsonConfig config = super.getDefaultJsonConfig();
		config.registerJsonValueProcessor(SexEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SexEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((SexEnum)value).getName());
					json.put("value", ((SexEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SexEnum){
					return ((SexEnum)value).getName();
				}
				return null;
			}
		});
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
			
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
		});
		return config;
	}
	
	/**
	 * 人员任职历史调整
	 * @return
	 */
	@RequestMapping(value="personPositionHisList")
	protected String personPositionHisList() {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("typenumber", "GWZT");//岗位状态编码
		param.put("enableflag", 1);
		List<BasicData> jobStatus = basicDataService.getBasicDataList(param);
		getRequest().setAttribute("jobStatus", jobStatus);
		return "basedata/person/personPositionHistoryList";
	}
	
	/**
	 * 人员任职历史调整
	 * @return
	 */
	@RequestMapping(value="personPositionHisLogList")
	protected String personPositionHisLogList() {
	 
		getRequest().setAttribute("operateTypeList", OperateTypeEnum.toList());
		return "basedata/person/personPositionHistoryLogList";
	}
	
	/**
	 * 人员任职历史调整
	 * @return
	 */
	@RequestMapping(value="personPositionHisEdit")
	protected String personPositionHisEdit(){
		String mapper = "com.wuyizhiye.basedata.person.dao.PersonDao.getPerson4PositionHisEdit";
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("id", getString("id"));
		Person  person = queryExecutor.execOneEntity(mapper, param, Person.class);
		param.clear();
		param.put("typenumber", "GWZT");//岗位状态编码
		param.put("enableflag", 1);
		List<BasicData> jobStatus = basicDataService.getBasicDataList(param);
		getRequest().setAttribute("changeTypeList", PositionChangeTypeEnum.toList());
		getRequest().setAttribute("jobStatus", jobStatus);
		List<PersonPositionHistory> positionHistoryList = personPositionHistoryService.getAllPersonPositionHistory(getString("id"));
		getRequest().setAttribute("positionHistoryList", positionHistoryList);
		getRequest().setAttribute("person", person);
		return "basedata/person/personPositionHistoryEdit";
	}
	
	/**
	 * 人员任职历史调整 列表数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="personPositionHisListData")
	public void personPositionHisListData(Pagination<?> pagination,HttpServletResponse response){
		String mapper = "com.wuyizhiye.basedata.person.dao.PersonDao.getPerson4PositionHisEdit";
		pagination = queryExecutor.execQuery(mapper, pagination, getListDataParam());
		afterFetchListData(pagination);
		outPrint(response, JSONObject.fromObject(pagination, getPersonAssignJsonConfig()));
	}
	
	
	/**
	 * 人员任职历史调整 日志列表数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="personPositionHisLogListData")
	public void personPositionHisLogListData(Pagination<?> pagination,HttpServletResponse response){
		String mapper = "com.wuyizhiye.basedata.person.dao.PersonPositionHistoryLogDao.select";
		pagination = queryExecutor.execQuery(mapper, pagination, getListDataParam());
		afterFetchListData(pagination);
		outPrint(response, JSONObject.fromObject(pagination, getPersonPstHisLogJsonConfig()));
	}
	
	protected JsonConfig getPersonPstHisLogJsonConfig() {
		JsonConfig config = super.getDefaultJsonConfig();
		config.registerJsonValueProcessor(OperateTypeEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof OperateTypeEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((OperateTypeEnum)value).getName());
					json.put("value", ((OperateTypeEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof OperateTypeEnum){
					return ((OperateTypeEnum)value).getName();
				}
				return null;
			}
		});
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
			
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
		});
		return config;
	}
}
