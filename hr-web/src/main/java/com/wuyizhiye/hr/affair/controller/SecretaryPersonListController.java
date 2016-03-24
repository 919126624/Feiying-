/**
 * com.wuyizhiye.basedata.person.controller.PersonListController.java
 */
package com.wuyizhiye.hr.affair.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wuyizhiye.basedata.enums.PersonShowTypeEnum;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.basedata.person.enums.CardTypeEnum;
import com.wuyizhiye.basedata.person.enums.JobStatusEnum;
import com.wuyizhiye.basedata.person.enums.PersonStatusEnum;
import com.wuyizhiye.basedata.person.enums.RecruitMethodEnum;
import com.wuyizhiye.basedata.person.enums.SexEnum;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.basedata.person.enums.WedStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.basedata.person.service.PersonPositionHistoryService;
import com.wuyizhiye.basedata.person.service.PersonPositionService;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.TreeListController;
import com.wuyizhiye.hr.affair.util.PersonPositionHistoryUtil;
import com.wuyizhiye.hr.enums.AgentTypeEnum;
import com.wuyizhiye.hr.enums.CertificateTypeEnum;
import com.wuyizhiye.hr.enums.EducationTypeEnum;
import com.wuyizhiye.hr.enums.MemberLevelEnum;
import com.wuyizhiye.hr.enums.RewardPunishmentEnum;
import com.wuyizhiye.hr.model.AgentCertificate;
import com.wuyizhiye.hr.model.Education;
import com.wuyizhiye.hr.model.RewardPunishment;
import com.wuyizhiye.hr.model.WorkExperience;
import com.wuyizhiye.hr.service.AgentCertificateService;
import com.wuyizhiye.hr.service.EducationService;
import com.wuyizhiye.hr.service.HrPersonService;
import com.wuyizhiye.hr.service.RewardPunishmentService;
import com.wuyizhiye.hr.service.WorkExperienceService;

/**
 * 秘书查看页面
 * @author ouyangyi
 * @since 2013-4-22 上午09:51:55
 */
@Controller
@RequestMapping(value="hr/secretaryPerson/*")
public class SecretaryPersonListController extends TreeListController {
	
	static final String ADD_PERSON = "ADD_PERSON";//是否允许新增
	static final String FORBIDDEN_RUNDISK = "FORBIDDEN_RUNDISK";//禁用跑盘业务
	
	@Autowired
	private HrPersonService hrPersonService;
	@Autowired 
	private PersonPositionService personPositionService;
	@Autowired
	private WorkExperienceService workExperienceService;
	@Autowired
	private EducationService educationService;
	@Autowired
	private AgentCertificateService agentCertificateService;
	@Autowired
	private RewardPunishmentService rewardPunishmentService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private PositionService positionService;
	
	@Autowired
	private PersonPositionHistoryService personPositionHistoryService;
	
	@Override
	protected String getTreeDataMapper() {
		//return "com.wuyizhiye.basedata.org.dao.OrgDao.getChild";
		return "com.wuyizhiye.basedata.org.dao.OrgDao.getSimpleTreeData";
		
	}
	
	@Override
	public void treeData(HttpServletResponse response) {
		// TODO Auto-generated method stub
		String parent = getString("parent");
		String includeChild = getString("includeChild");
		Map<String,Object> param = getTreeFilterParam();
		if(isFilterByOrg()=="Y"){
			Org org = orgService.getEntityById(SystemUtil.getCurrentOrg().getId());
			if(org.getParent() != null && parent == null){
				parent = org.getParent().getId();
				param.put("longNumber", org.getLongNumber());
				
			}
		}else if(SystemUtil.getCurrentControlUnit()!=null){ //根据CU控制  sht 2014.7.14
			Org org = orgService.getEntityById(SystemUtil.getCurrentControlUnit().getId());
			param.put("longNumber", org.getLongNumber());
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
	
	/**
	 * 公司或集团下 或者 组织类型为 人力资源(T02)下的人 不需要根据登录人组织过滤
	 * @return
	 */
	private String isFilterByOrg(){
		Org org = SystemUtil.getCurrentOrg();
		if(org ==null || StringUtils.isEmpty(org.getBusinessTypes())){
			return  "N";//公司 和集团
		}
		//业务类型为 人力资源
		if(OrgUtils.isType(org, "T02")){
			return "N";
		}
		return "Y";
	}
	
	
	@RequestMapping(value="treeDataAll")
	public void treeDataAll(HttpServletResponse response) {
		// TODO Auto-generated method stub
		String parent = getString("parent");
		String includeChild = getString("includeChild");
		Map<String,Object> param = getTreeFilterParam();
//		Org org = orgService.getEntityById(SystemUtil.getCurrentOrg().getId());
//		if(org.getParent() != null && parent == null){
//			parent = org.getParent().getId();
//			param.put("longNumber", org.getLongNumber());
//			
//		}
		
		
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

	@Override
	protected String getListView() {
		
		/******************* 判断是否有权限 start **********************/
		String personPermissionId = "4174754c-202f-40b8-8239-9abe82795af2"; //登陆设置权限项ID
		boolean hasPermission = this.hasPermission(personPermissionId);
		this.getRequest().setAttribute("hasPermission", hasPermission);
		String hasRundiskPermission =  ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), FORBIDDEN_RUNDISK);
		this.getRequest().setAttribute("hasRundiskPermission", hasRundiskPermission);
		/************** end *****************/
		return "hr/affair/secretaryPersonView";
	}
	
	
	@RequestMapping(value="listAll")
	public String toAllListView(){
		String personPermissionId = "4174754c-202f-40b8-8239-9abe82795af2";//登陆设置权限项ID
		boolean hasPermission = this.hasPermission(personPermissionId);
		this.getRequest().setAttribute("hasPermission", hasPermission);
		String hasRundiskPermission =  ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), FORBIDDEN_RUNDISK);
		this.getRequest().setAttribute("hasRundiskPermission", hasRundiskPermission);
		return "hr/affair/secretaryPersonView";
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
		model.put("educationTypeList", EducationTypeEnum.toList());
		//奖或惩
		model.put("rewardOrPunishmentList", RewardPunishmentEnum.toList());
		//证件类型
		model.put("certificateTypeList", CertificateTypeEnum.toList());
		//经纪类型
		model.put("agentTypeList", AgentTypeEnum.toList());
		//会员级别
		model.put("memberLevelList", MemberLevelEnum.toList());
	}

	@Override
	protected String getEditView() {
		//是否允许新增员工
		String addPersonFlag = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), ADD_PERSON);
		if(StringUtils.isEmpty(addPersonFlag)){
			addPersonFlag ="N";//默认不允许新增
		}
		this.getRequest().setAttribute("addPersonFlag", addPersonFlag);
		
//		return "basedata/person/personEdit";
		
		//TODO 人员在通讯录上显示的信息类型,操作人:sht
		getRequest().setAttribute("showTypes", PersonShowTypeEnum.values());
		
		//证件类型
		getRequest().setAttribute("cardTypes", CardTypeEnum.values());
		return "hr/hrPersonEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.person.dao.PersonDao.getPersonInfoByOrg";
	}
	
	@Override
	protected BaseService<Person> getService() {
		return hrPersonService;
	}
	
	@Override
	protected BaseService<Org> getTreeService() {
		return orgService;
	}
	
	//编辑界面
	@Override
	public String edit(ModelMap model, @RequestParam(required=true,value="id")String id) {
		
		this.putOtherInfoDataToUI(model, id);
		this.putEnumsToModelMap(model);
		return super.edit(model, id);
	}
	
	/**
	 * 将职位信息，工作经历，教育经历，奖惩记录，经纪人证信息 放入到界面
	 * @param modelMap
	 * @param id
	 */
	private void putOtherInfoDataToUI(ModelMap model, String id){
		//任职信息
		List<PersonPosition> personPosition = personPositionService.getByPerson(id);
		
		Map<String,Object> positionMap = new HashMap<String, Object>();
		this.filterPersonPosition(positionMap, personPosition);
		//主要职位
		model.put("mainPosition", positionMap.get("mainPosition"));
		//兼职
		model.put("partTimeJobPosition", positionMap.get("partTimeJobPosition"));
		//工作经历
		List<WorkExperience> workExperienceList = this.workExperienceService.getByPersonId(id);
		model.put("workExperienceList", workExperienceList);
		//教育经历
		List<Education> educationList = this.educationService.getByPersonId(id);
		model.put("educationList", educationList);
		//奖惩记录
		List<RewardPunishment> rewardPunishmentList = this.rewardPunishmentService.getByPersonId(id);
		model.put("rewardPunishmentList", rewardPunishmentList);
		//经纪人证
		List<AgentCertificate> agentCertificateList = this.agentCertificateService.getByPersonId(id);
		model.put("agentCertificateList", agentCertificateList);
	}
	
	/**
	 * 筛选职位  把主要职位和兼职职位分开
	 * @param result  存放结果
	 * @param personPositionList  所有的职位
	 */
	private void filterPersonPosition(Map<String,Object> result ,List<PersonPosition> personPositionList){
		List<PersonPosition> partTimeJobPosition = new ArrayList<PersonPosition>();
		for(PersonPosition pp : personPositionList){
			if(pp.isPrimary()){
				result.put("mainPosition", pp);
			} else {
				partTimeJobPosition.add(pp);
			}
		}
		
		result.put("partTimeJobPosition", partTimeJobPosition);
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return null;
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
		config.registerJsonValueProcessor(JobStatusEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof JobStatusEnum){
					JSONObject json = new JSONObject();
					json.put("label", ((JobStatusEnum)value).getLabel());
					json.put("value", ((JobStatusEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof JobStatusEnum){
					return ((JobStatusEnum)value).getLabel();
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
	
	/**
	 * 人员查看界面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="view")
	public String view(ModelMap model,@RequestParam(required=true,value="id")String id){
		model.put("personalCenter", this.getString("personalCenter"));	//标识
		model.put("data", getService().getEntityById(id));
		model.put("educationTypeList", EducationTypeEnum.toList());
		model.put("ondutyRecordList", this.getOndutyRecord(id));
		this.putOtherInfoDataToUI(model, id);
		this.putEnumsToModelMap(model);
		return "hr/hrPersonView";
	}
	
	/**
	 * 得到某人的任职记录
	 * @param personId
	 * @return
	 */
	private List<String> getOndutyRecord(String personId){
		List<String> ondutyRecordList = new ArrayList<String>();
		List<PersonPositionHistory> ppHistoryList = this.personPositionHistoryService
														.getPersonOnDutyRecord(personId);
		if(null != ppHistoryList && ppHistoryList.size() > 0){
			for(PersonPositionHistory tmp : ppHistoryList){
				ondutyRecordList.add(PersonPositionHistoryUtil.conbineOndutyRecord(tmp));
			}
		}
		return ondutyRecordList;
	}
	

	/**
	 * 删除
	 */
	@Override
	public void delete(String id, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Person entity = (Person)getService().getEntityById(id);
		if(entity!=null){
			try{
				hrPersonService.deletePerson(entity);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "删除成功");
			} catch(Exception e){
				e.printStackTrace();
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "删除失败");
				throw new RuntimeException(e);
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	
	/**
	 * 离职列表数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="listDimissionData")
	@Dependence(method="list")
	public void listDimissionData(Pagination<?> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.getDismissionPersonByOrg", pagination, getListDataParam());
		afterFetchListData(pagination);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
}
