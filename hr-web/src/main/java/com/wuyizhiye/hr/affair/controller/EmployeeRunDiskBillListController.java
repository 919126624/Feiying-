package com.wuyizhiye.hr.affair.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.enums.CardTypeEnum;
import com.wuyizhiye.basedata.person.enums.SexEnum;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.workflow.service.WorkFlowService;
import com.wuyizhiye.hr.affair.model.EmployeeRunDiskBill;
import com.wuyizhiye.hr.affair.service.EmployeeRunDiskService;
import com.wuyizhiye.hr.enums.BillStatusEnum;

/**
 * 跑盘员入职
 * @author ouyangyi
 * @since 2013-4-23 下午04:10:26
 */
@Controller
@RequestMapping(value="hr/employeerundiskbill/*")
public class EmployeeRunDiskBillListController extends ListController{
	static final String JOBNUMBERZY = "ZYGW";//置业顾问
	static final String JOBNUMBERXS = "LDXSDB";//LDXSDB 销售代表 
	static final String KXXSDB = "KXXSDB";//KXXSDB  快销 销售代表 
	static final String JOBLEVEL = "PPY";
	
	@Resource
	private EmployeeRunDiskService employeeRunDiskService ;
	
	
	@Resource
	private WorkFlowService workFlowService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="delete")
	@Dependence(method="list")
	public void delete(@RequestParam(required=true,value="id")String id,HttpServletResponse response){
		EmployeeRunDiskBill  entity = employeeRunDiskService.getEntityById(id);
		if(entity!=null){
			if(isAllowDelete(entity)){
				getService().deleteEntity(entity);
				if(StringUtils.isNotNull(entity.getProcessInstance())){
				  workFlowService.deleteProcessInstance(entity.getProcessInstance(), "", true);
				}else{
					workFlowService.deleteProcessView(entity.getId());
				}
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "删除成功");
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
	protected CoreEntity createNewEntity() {
		EmployeeRunDiskBill bill = new EmployeeRunDiskBill();
		bill.setEffectdate(new Date());
		return bill;
	}

	@Override
	protected String getListView() {
		
		getRequest().setAttribute("billStatusList", BillStatusEnum.toList());
		return "hr/affair/employeerundiskbill/employeeRunDiskBillList";
	}
	
	@RequestMapping(value="indexList")
	public String indexList(){
		return "hr/affair/runDiskIndex";
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
		getRequest().setAttribute("jobNumbers","\\'"+jobNumber+"\\',\\'"+jobNumberXs+"\\',\\'"+kxxsdb+"\\'" );
		getRequest().setAttribute("jobLevel", jobLevel);
		getRequest().setAttribute("cardTypes", CardTypeEnum.values());
		return "hr/affair/employeerundiskbill/employeeRunDiskBillEdit";
	}

	@Override
	protected String getListMapper() {
		 
		return "com.wuyizhiye.hr.affair.dao.EmployeeRunDiskDao.getEmployeeRunDiskBills";
	}

	@Override
	protected BaseService getService() {
		 
		return employeeRunDiskService;
	}
	
	@Override
	protected JsonConfig getDefaultJsonConfig() {
		JsonConfig config = super.getDefaultJsonConfig();
		config.registerJsonValueProcessor(BillStatusEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof BillStatusEnum){
					JSONObject json = new JSONObject();
					json.put("label", ((BillStatusEnum)value).getLabel());
					json.put("value", ((BillStatusEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof BillStatusEnum){
					return ((BillStatusEnum)value).getLabel();
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

}
