/**
 * com.wuyizhiye.basedata.person.controller.PersonListController.java
 */
package com.wuyizhiye.hr.affair.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.ObjectCopyUtils;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.enums.CardTypeEnum;
import com.wuyizhiye.basedata.person.enums.JobStatusEnum;
import com.wuyizhiye.basedata.person.enums.PersonStatusEnum;
import com.wuyizhiye.basedata.person.enums.RecruitMethodEnum;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.basedata.person.enums.WedStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.workflow.service.WorkFlowService;
import com.wuyizhiye.hr.affair.model.Education;
import com.wuyizhiye.hr.affair.model.EmployeeOrientation;
import com.wuyizhiye.hr.affair.model.PositionHistoryBill;
import com.wuyizhiye.hr.affair.model.WorkExperience;
import com.wuyizhiye.hr.affair.service.EducationTempService;
import com.wuyizhiye.hr.affair.service.EmployeeOrientationService;
import com.wuyizhiye.hr.affair.service.WorkExperienceTempService;
import com.wuyizhiye.hr.enums.AgentTypeEnum;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.CertificateTypeEnum;
import com.wuyizhiye.hr.enums.EducationTypeEnum;
import com.wuyizhiye.hr.enums.MemberLevelEnum;
import com.wuyizhiye.hr.enums.RewardPunishmentEnum;
import com.wuyizhiye.hr.service.EducationService;
import com.wuyizhiye.hr.service.WorkExperienceService;

/**
 *入职申请单列表
 * @author Cai.xing
 * @since 2013-04-03
 */
@Controller
@RequestMapping(value="hr/employeeOrientation/*")
public class EmployeeOrientationListController extends ListController {
	static final String JOBNUMBERZY = "ZYGW";//置业顾问
	static final String JOBNUMBERXS = "LDXSDB";//LDXSDB 销售代表 
	static final String KXXSDB = "KXXSDB";//KXXSDB 快销 销售代表 
	static final String JOBLEVEL = "PPY";
	@Autowired
	private EmployeeOrientationService employeeOrientationService;
	@Autowired
	private WorkExperienceTempService workExperienceTempService;
	@Autowired
	private EducationTempService educationTempService;
	
	@Autowired
	private WorkExperienceService workExperienceService;
	@Autowired
	private EducationService educationService;
	@Autowired
	private PersonService personService;
	
	@Resource
	private WorkFlowService workFlowService;
	 
	
	@Override
	protected String getListView() {
		
		/******************* 判断是否有权限 start **********************/
		String personPermissionId = "b9d274a7-c800-4dbe-94e6-5311a346ccec";
		boolean hasPermission = this.hasPermission(personPermissionId);
		this.getRequest().setAttribute("longNumber",this.getCurrentUser().getOrg().getLongNumber());
		this.getRequest().setAttribute("hasPermission", hasPermission);
		/************** end *****************/
		getRequest().setAttribute("billStatusList", BillStatusEnum.toList());
		return "hr/affair/employeeOrientationList";
	}
	
	
	@RequestMapping(value="listAll")
	public String toAllListView(){
		String personPermissionId = "b9d274a7-c800-4dbe-94e6-5311a346ccec";
		boolean hasPermission = this.hasPermission(personPermissionId);
		this.getRequest().setAttribute("hasPermission", hasPermission);
		return "hr/hrPersonAllList";
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
		
		//置业顾问 编码
		String jobNumber = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), JOBNUMBERZY);
		if(StringUtils.isEmpty(jobNumber)){
			jobNumber ="8016";//默认8016
		}
		//销售代表 编码
		String jobNumberXs = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), JOBNUMBERXS);
		if(StringUtils.isEmpty(jobNumberXs)){
			jobNumberXs ="8020";//默认8020
		}
		String kxxsdb = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), KXXSDB);
		//跑盘员职级 level
		String jobLevel = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), JOBLEVEL);
		if(StringUtils.isEmpty(jobLevel)){
			jobLevel ="1";//默认1
		}
		getRequest().setAttribute("jobNumbers",jobNumber+","+jobNumberXs+","+kxxsdb );
		getRequest().setAttribute("jobLevel", jobLevel);
		
		getRequest().setAttribute("cardTypes", CardTypeEnum.values());
		
		return "hr/affair/employeeOrientationEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.hr.affair.dao.EmployeeOrientationDao.select";
	}
	
	@Override
	protected BaseService< EmployeeOrientation> getService() {
		return employeeOrientationService;
	}
	
	
	//编辑界面
	@Override
	public String edit(ModelMap model, @RequestParam(required=true,value="id")String id) {
		//如果身份证号码不为空
		if(getString("idcard")!=null){
			//根据身份证号码得到对应人的信息
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("idcard", getString("idcard"));
			List<Person> lisP = this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.select",params, Person.class);
			if(lisP!=null && lisP.size()>0){
				String perosnId =lisP.get(0).getId();
				this.putOtherInfoDataToUI(model, perosnId);
				this.putEnumsToModelMap(model);
				Person p =  personService.getEntityById(perosnId);
				EmployeeOrientation emp = new EmployeeOrientation();
				ObjectCopyUtils.copy(p, emp);
				emp.setApplyPerson(p);
				emp.setId(null);
				emp.setInnerDate(null);
				model.put("data", emp);
				//将当前状态放入到界面 addded by taking.wang 2013-01-24
				model.put("isPersonInfo", "1");
				model.put("edit_viewStatus", this.getString("VIEWSTATE"));
				return getEditView();
			}
		}
		this.putOtherInfoDataToUI(model, id);
		this.putEnumsToModelMap(model);
		model.put("data", getService().getEntityById(id));
		//将当前状态放入到界面 addded by taking.wang 2013-01-24
		model.put("edit_viewStatus", this.getString("VIEWSTATE"));
		return getEditView();

	}
	/**
	 * 将职位信息，工作经历，教育经历放入到界面
	 * @param modelMap
	 * @param id
	 */
	private void putOtherInfoDataToUI(ModelMap model, String id){
		
		//如果
		//工作经历
		if(getString("idcard")!=null){
			List<com.wuyizhiye.hr.model.WorkExperience> workExperienceList = this.workExperienceService.getByPersonId(id);
			model.put("workExperienceList", workExperienceList);
			//教育经历
			List<com.wuyizhiye.hr.model.Education> educationList = this.educationService.getByPersonId(id);
			model.put("educationList", educationList);
		}else{
			List<WorkExperience> workExperienceList = this.workExperienceTempService.getByPersonId(id);
			model.put("workExperienceList", workExperienceList);
			//教育经历
			List<Education> educationList = this.educationTempService.getByPersonId(id);
			model.put("educationList", educationList);
		}
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
		this.putOtherInfoDataToUI(model, id);
		this.putEnumsToModelMap(model);
		return "hr/affair/employeeOrientationView";
	}
	/**
	 * 删除
	 */
	@Override
	public void delete(String id, HttpServletResponse response) {
		 EmployeeOrientation employeeOrientation = ( EmployeeOrientation)getService().getEntityById(id);
		if(employeeOrientation!=null){
			try{
				 employeeOrientationService.deleteEmployeeOrientation(employeeOrientation);
				 if(StringUtils.isNotNull(employeeOrientation.getProcessInstance())){
					  workFlowService.deleteProcessInstance(employeeOrientation.getProcessInstance(), "", true);
					}else{
						workFlowService.deleteProcessView(employeeOrientation.getId());
					}
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
		Map<String,Object> map = getListDataParam();
		if(map.get("beginDate")!=null && !map.get("beginDate").toString().equals("")){
			map.put("beginDate",DateUtil.convertStrToDate(map.get("beginDate").toString(),"yyyy/MM/dd"));
		}else{
			map.put("beginDate",null);
		}
		if(map.get("endDate")!=null && !map.get("endDate").toString().equals("")){
			map.put("endDate",DateUtil.convertStrToDate(map.get("endDate").toString(),"yyyy/MM/dd"));
		}else{
			map.put("endDate",null);
		}
		//map.put("orgLongNumber", this.getCurrentUser().getOrg().getLongNumber());
		pagination = this.queryExecutor.execQuery(getListMapper(), pagination,map);   
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	@Override
	protected CoreEntity createNewEntity() {
		EmployeeOrientation emp = new EmployeeOrientation();
		emp.setBillStatu(BillStatusEnum.SAVE);
		return emp;
	}
}
