package com.wuyizhiye.hr.affair.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.hr.affair.service.ProcessApplyService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;
/**
 * 消息中心流程操作处理
 * @author hlz
 *
 */
@Controller
@RequestMapping(value="hr/processApply/*")
public class HrProcessApplyListController extends ListController {

	@Autowired
	private ProcessApplyService processApplyService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status", BillStatusEnum.APPROVED);
		param.put("personId", getCurrentUser().getId());
		int count = queryExecutor.execOneEntity("com.wuyizhiye.hr.affair.dao.ProcessApplyEntityDao.selectCount", param, Integer.class);//("com.wuyizhiye.hr.affair.dao.ProcessApplyEntityDao.selectCount", param,Integer.c);
		getRequest().setAttribute("approvedCount", count);
		param.put("status", BillStatusEnum.REJECT);
		count = queryExecutor.execOneEntity("com.wuyizhiye.hr.affair.dao.ProcessApplyEntityDao.selectCount", param, Integer.class);
		getRequest().setAttribute("rejectCount", count);
		Set<String> personPermission = (Set<String>) getRequest().getSession().getAttribute("PERSONAL-PERMISSION");
		getRequest().setAttribute("demotionEnable", this.hasPermission("e5d17b90-33c8-428d-8982-d176cb5c7dea"));//降职申请发起
		getRequest().setAttribute("orientationEnable", this.hasPermission("6085f704-f70a-49a4-8d3c-bf8461fbe520"));//入职申请发起
		getRequest().setAttribute("positiveEnable", this.hasPermission("6291a625-1593-48ab-8ae1-dd28be8fe474"));//转正申请发起
		getRequest().setAttribute("leaveOfficeEnable", this.hasPermission("28538931-259e-4f88-a27c-ea14ace2adae"));//离职申请发起
		getRequest().setAttribute("reinstatementEnable", this.hasPermission("43962261-db22-44b7-82fc-891e9539144f"));//复职申请发起
		getRequest().setAttribute("dismissptjobEnable", this.hasPermission("0b01d589-a599-4a1c-a3c1-9b24b7dde798"));//撤职申请发起
		getRequest().setAttribute("changeEnable", this.hasPermission("c1e5adbc-b799-4c6b-8991-9d21eb088365"));//调职申请发起
		getRequest().setAttribute("promotionEnable", this.hasPermission("dce344e9-6f05-40f3-a7a5-6730b0d85b31"));//晋升申请发起
		getRequest().setAttribute("creaseptjobEnable", this.hasPermission("26a7c232-6dc1-486d-bc78-dbe186e9cad5"));//兼职申请发起
		getRequest().setAttribute("runDiskEnable", this.hasPermission("59c7942f-1fb3-42c3-8397-181e7fec9c28"));//跑盘员录入发起
		getRequest().setAttribute("delRunDiskEnable", this.hasPermission("4428d4c5-f30d-4f00-a7b9-b9b6bf20e43a"));//跑盘员删除发起
		return "interflow/process/processApply";
	}

	@Override
	protected Map<String,Object> getListDataParam(){
		Map<String,String> param = getParamMap();
		Map<String,Object> params = new HashMap<String, Object>();
		Set<String> keys = param.keySet();
		for(String key : keys){
			if(key.equals("queryStartDate")){
				params.put(key, DateUtil.convertStrToDateHms(param.get(key)+" 00:00:00"));
			}else if(key.equals("queryEndDate")){
				params.put(key, DateUtil.convertStrToDateHms(param.get(key)+" 23:59:59"));
			}else{
				if(key.equals("changeType")){
					String changeType = param.get("changeType").toString();
					changeType = "'"+changeType+"'";
					changeType = changeType.replaceAll(";", "','");
					params.put(key,changeType);
				}else{
					params.put(key, param.get(key));
				}
			}
			
			
		}
		return params;
	}
	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.hr.affair.dao.ProcessApplyEntityDao.selectProcessApply";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
		config.registerJsonValueProcessor(PositionChangeTypeEnum.class, new JsonValueProcessor() {
			
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof PositionChangeTypeEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((PositionChangeTypeEnum)value).getLabel());
					json.put("value", ((PositionChangeTypeEnum)value).getValue());
					return json;
				}
				return null;
			}
			
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof PositionChangeTypeEnum){
					return ((PositionChangeTypeEnum)value).getLabel();
				}
				return null;
			}
		});
		config.registerJsonValueProcessor(BillStatusEnum.class, new JsonValueProcessor() {
			
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof BillStatusEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((BillStatusEnum)value).getLabel());
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
		return config;
	}
	
	@RequestMapping(value="updateRead")
	public void updateRead(HttpServletResponse response){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status", getString("status"));
		param.put("personId", getCurrentUser().getId());
		//queryExecutor.executeUpdate("com.wuyizhiye.hr.affair.dao.ProcessApplyEntityDao.update", param);
		processApplyService.updateProcess(param);
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@RequestMapping(value="getProcessCount")
	public void getCount(HttpServletResponse response){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status", BillStatusEnum.APPROVED);
		param.put("personId", getCurrentUser().getId());
		int count = getProcessCount(param);//("com.wuyizhiye.hr.affair.dao.ProcessApplyEntityDao.selectCount", param,Integer.c);
		param.put("status", BillStatusEnum.REJECT);
		int count2 = getProcessCount(param);
		getOutputMsg().put("processCount", count+count2);
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@RequestMapping(value="getAllCount")
	public void getAllProcessCount(HttpServletResponse response){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status", BillStatusEnum.APPROVED);
		getOutputMsg().put("all", getProcessCount(param));
		param.put("changeType", PositionChangeTypeEnum.ENROLL);//入职
		getOutputMsg().put("enrollCount", getProcessCount(param));
		param.put("changeType", PositionChangeTypeEnum.POSITIVE);//转正
		getOutputMsg().put("positiveCount", getProcessCount(param));
		param.put("changeType", PositionChangeTypeEnum.REINSTATEMENT);//复职
		getOutputMsg().put("reinstatementCount", getProcessCount(param));
		param.put("changeType", PositionChangeTypeEnum.LEAVE);//离职
		getOutputMsg().put("leaveCount", getProcessCount(param));
		param.put("changeType", PositionChangeTypeEnum.TRANSFER);//调职
		getOutputMsg().put("transferCount", getProcessCount(param));
		param.put("changeType", PositionChangeTypeEnum.PROMOTION);//晋升
		getOutputMsg().put("promotionCount", getProcessCount(param));
		param.put("changeType", PositionChangeTypeEnum.DEMOTION);//降职
		getOutputMsg().put("demotionCount", getProcessCount(param));
		param.put("changeType", PositionChangeTypeEnum.DISMISS_PARTTIMEJOB);//撤职
		getOutputMsg().put("missjobCount", getProcessCount(param));
		param.put("changeType", PositionChangeTypeEnum.INCREASE_PARTTIMEJOB);//兼职
		getOutputMsg().put("increasejobCount", getProcessCount(param));
	}
	
	private int getProcessCount(Map<String, Object> param){
		return queryExecutor.execOneEntity("com.wuyizhiye.hr.affair.dao.ProcessApplyEntityDao.selectCount", param, Integer.class);
	}
}
