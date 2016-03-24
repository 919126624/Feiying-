/**
 * com.wuyizhiye.basedata.person.controller.PersonListController.java
 */
package com.wuyizhiye.hr.controller;

import java.io.IOException;
import java.io.OutputStream;
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
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.TreeEntity;
import com.wuyizhiye.basedata.logoconfig.LogoConfig;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.basedata.person.enums.CardTypeEnum;
import com.wuyizhiye.basedata.person.enums.JobStatusEnum;
import com.wuyizhiye.basedata.person.enums.PersonStatusEnum;
import com.wuyizhiye.basedata.person.enums.RecruitMethodEnum;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.basedata.person.enums.WedStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.basedata.person.service.PersonPositionHistoryService;
import com.wuyizhiye.basedata.person.service.PersonPositionService;
import com.wuyizhiye.basedata.util.ExcelExportUtil;
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
 * 职员列表controller
 * @author FengMy
 *
 * @since 2012-10-9
 */
@Controller
@RequestMapping(value="hr/person/*")
public class HrPersonListController extends TreeListController {
	static final String ADD_PERSON = "ADD_PERSON";//是否允许新增
	
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
		return "com.wuyizhiye.basedata.org.dao.OrgDao.getChild";
	}
	
	@Override
	public void treeData(HttpServletResponse response) {
		// TODO Auto-generated method stub
		String parent = getString("parent");
		String includeChild = getString("includeChild");
		Map<String,Object> param = getTreeFilterParam();
		Org org = orgService.getEntityById(SystemUtil.getCurrentOrg().getId());
		if(getString("longNumber")!=null){
			param.put("longNumber", getString("longNumber"));
			param.put("parent","807daadd-5011-41c0-ae1d-2976035225db");
		}
		if(org.getParent() != null && parent == null){
			parent = org.getParent().getId();
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
		String personPermissionId = "b9d274a7-c800-4dbe-94e6-5311a346ccec";
		boolean hasPermission = this.hasPermission(personPermissionId);
		this.getRequest().setAttribute("hasPermission", hasPermission);
		//是否允许新增员工
		String addPersonFlag = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), ADD_PERSON);
		if(StringUtils.isEmpty(addPersonFlag)){
			addPersonFlag ="N";//默认不允许新增
		}
		this.getRequest().setAttribute("addPersonFlag", addPersonFlag);
		/************** end *****************/
		return "hr/hrPersonList";
	}
	
	
	@RequestMapping(value="listAll")
	public String toAllListView(){
		String personPermissionId = "b9d274a7-c800-4dbe-94e6-5311a346ccec";
		boolean hasPermission = this.hasPermission(personPermissionId);
		this.getRequest().setAttribute("hasPermission", hasPermission);
		//是否只是查看员工
		String personPermissionId2="eec8a8cc-e8d1-416a-ae67-c1dd8d72ad80";
		boolean hasPermission2=this.hasPermission(personPermissionId2);
		this.getRequest().setAttribute("hasPermission2", hasPermission2);
		//是否允许新增员工
		String addPersonFlag = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), ADD_PERSON);
		if(StringUtils.isEmpty(addPersonFlag)){
			addPersonFlag ="N";//默认不允许新增
		}
		this.getRequest().setAttribute("addPersonFlag", addPersonFlag);
		return "hr/hrPersonAllList";
	}
	
	@RequestMapping(value="exportPersonByCond")
	public void exportMonthByCond(HttpServletResponse response)throws IOException {
		String longNumber = getString("longNumber");
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("orgLongNumber", longNumber);
		if(StringUtils.isNotNull(getString("jobStatus"))){
			if("DIMISSION".equals(getString("jobStatus"))){
				cond.put("jobStatusNotIn", "ONDUTY");
			}else{
				cond.put("jobStatusIn", getString("jobStatus"));
			}
		}else{
			cond.put("jobStatusIn", "ONDUTY");
		}
		cond.put("includeChild", getString("includeChild"));
		response.setContentType("application/octet-stream");
		String fileName = "导出人员.xlsx";
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"),"iso8859-1"));
        OutputStream os = response.getOutputStream();
        try{
        	//hrPersonService.exportPersonByCond(cond, os);
        	
        	String[] excelHeader = new String[] {"员工编号","姓 名","部门","职位","职级","入职日期","身份证","出生年月","性别","手机号码","Email","紧急联系人","联系人电话","户籍","现居住地","家庭住址","最高学历","民族","婚姻状况","招聘渠道","社保电脑号","公积金账号","备注"};
    		String[] excelField = new String[] {"number","name","org.name","postion.name","ppjobname","innerDate[yyyy-MM-dd]","idCard","birthday[yyyy-MM-dd]","sex","phone","email","crashContract","contractPhone","household","nowLivePlace","familyAddress"
    				,"highestEducation.label","folk.name","wedStatus.label","recruitmethod.label","computerNumber","fundNumber",""};
    		
    		List<Person> dataList=this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.getPersonToExport", cond, Person.class);
    		queryExecutor.getExecuteSql("com.wuyizhiye.basedata.person.dao.PersonDao.getPersonToExport", cond);
        	ExcelExportUtil.export(excelHeader, excelField, dataList, response.getOutputStream());
        }catch(Exception e){
        	e.printStackTrace();
        	//os.write(e.getMessage().getBytes());
        }
		os.close();
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
//		Map<String, Object> param=new HashMap<String, Object>();
//		param.put("personId", SystemUtil.getCurrentUser().getId());
//		model.put("secPass", queryExecutor.execOneEntity("com.wuyizhiye.broker.dao.CustomerProtectDao.selectCount", param,Integer.class));//二手房私客查看密码
//		model.put("newPass", queryExecutor.execOneEntity("com.wuyizhiye.fastsale.dao.IntentionProtectDao.selectCount", param,Integer.class));//新房私客查看密码
		//启用了企业邮箱，禁止修改邮箱信息 add by li.biao since 2014-4-16
		Map<String,Object> param=new HashMap<String, Object>();
		List<LogoConfig> cfglist=queryExecutor.execQuery("com.wuyizhiye.basedata.dao.LogoConfigDao.getLogoConfigByCon", param, LogoConfig.class);
		LogoConfig logoConfig = null ;
		if(cfglist!=null && cfglist.size()>0){
			logoConfig = cfglist.get(0);
		}else{
			logoConfig = new LogoConfig();
		}
		model.put("qyyxflag", StringUtils.isEmpty(logoConfig.getToolCheck()) ? false : logoConfig.getToolCheck().contains("QYYX"));
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
	 * 数据列表
	 */
	@Override
	public void listData(Pagination<?> pagination,HttpServletResponse response){
		pagination = this.queryExecutor.execQuery(getListMapper(), pagination, getListDataParam());
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
}
