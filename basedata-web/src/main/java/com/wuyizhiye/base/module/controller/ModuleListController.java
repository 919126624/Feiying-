package com.wuyizhiye.base.module.controller;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.module.enums.BusinessTypeEnum;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.module.enums.ModuleStatusEnum;
import com.wuyizhiye.base.module.model.Module;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.module.service.ModuleService;
import com.wuyizhiye.basedata.util.LoginHolder;
import com.wuyizhiye.basedata.util.Validate;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.framework.listener.OnlineListener;

/**
 * @ClassName ModuleListController
 * @Description 模块管理列表
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="base/module/*")
public class ModuleListController extends ListController {
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private LoginHolder loginHolder;
	@Override
	protected CoreEntity createNewEntity() {
		return new Module();
	}

	@Override
	protected String getListView() {
		
		return "base/module/moduleManage";
	}

	@RequestMapping(value="moduleList")
	protected String moduleManage() {
		this.getRequest().setAttribute("pconlinesize",  OnlineListener.getCurrPcOnLine());
		this.getRequest().setAttribute("mobileonlinesize", OnlineListener.getCurrMobileOnLine());
		this.getRequest().setAttribute("licenseinfo",  Validate.getCurrLicenseInfo());
		this.getRequest().setAttribute("modulenum", Validate.getCurrPermNum());
		this.getRequest().setAttribute("processId", Validate.getStaticSignature());
		return "base/module/moduleList";
	}
	
	@Override
	protected String getEditView() {
		return "base/module/moduleEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.base.module.dao.ModuleDao.select";
	}

	@Override
	protected ModuleService getService() {
		return moduleService;
	}
	
	@Override
	protected void afterFetchListData(Pagination<?> pagination) {
		List<Module> listData = (List<Module>) pagination.getItems();
		Date now = new Date();
		//小于100天提醒,大于100年则为永久
		long limit = 100*24*60*60*1000L;
		long permanent = 100*365*24*60*60*60*1000L;
		
		for(int i = listData.size()-1;i >=0;i--){
			Module m = (Module)listData.get(i);
			if(m.getEnd()==null || m.getStart()==null || now.compareTo(m.getStart()) < 0 || now.compareTo(m.getEnd()) > 0){
				m.setStatus(ModuleStatusEnum.FORBID);
				//dellist.add(m);
				//listData.remove(m);
			}else{
				m.setStatus(ModuleStatusEnum.ALLOW);
				if((m.getEnd().getTime() - now.getTime()) <= limit){
					m.setStatus(ModuleStatusEnum.WARNING);
				}
				if((m.getEnd().getTime() - now.getTime()) >= permanent){
					m.setStatus(ModuleStatusEnum.PERMANENT);
				}
				
			}
		}
		
		
	}
	
	@Override
	protected JsonConfig getDefaultJsonConfig() {
		JsonConfig config = super.getDefaultJsonConfig();
		config.registerJsonValueProcessor(ModuleEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof ModuleEnum){
					JSONObject jo = new JSONObject();
					jo.put("number", value);
					jo.put("name", ((ModuleEnum)value).getName());
					return jo;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				return null;
			}
		});
		return config;
	}

	@Override
	protected Map<String, String> getParamMap() {
		Map<String, String> param = super.getParamMap();
		if(param.containsKey("moduleType") && !StringUtils.isEmpty(param.get("moduleType"))){
			BusinessTypeEnum moduleType = Enum.valueOf(BusinessTypeEnum.class, param.get("moduleType"));
			BusinessTypeEnum[] allModuleType = BusinessTypeEnum.values();
			List<BusinessTypeEnum> types = new ArrayList<BusinessTypeEnum>(); 
			for(BusinessTypeEnum e : allModuleType){
				if(e==moduleType || e.getParent() == moduleType){
					types.add(e);
				}
			}
			ModuleEnum[] allModule = ModuleEnum.values();
			StringBuilder stringBuilder = new StringBuilder("");
			for(ModuleEnum e : allModule){
				for(BusinessTypeEnum t : types){
					if(e.getParent() == t){
						stringBuilder.append("'").append(e).append("',");
					}
				}
			}
			if(stringBuilder.length() > 1){
				stringBuilder = stringBuilder.deleteCharAt(stringBuilder.length()-1);
			}else{
				stringBuilder.append("'nodata'");
			}
			param.put("modules", stringBuilder.toString());
		}
		return param;
	}
	
	@RequestMapping(value="simpleTreeData")
	@Dependence(method="list")
	public void simpleTreeData(HttpServletResponse response) {
		BusinessTypeEnum[] types = BusinessTypeEnum.values();
		List<Map<String,Object>> treeData = new ArrayList<Map<String,Object>>();
		for(BusinessTypeEnum e : types){
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("id", e);
			m.put("name", e.getName());
			m.put("pid", e.getParent());
			treeData.add(m);
		}
		outPrint(response, JSONArray.fromObject(treeData));
	}
	
	@RequestMapping(value="refreshLicense")
	@Dependence(method="list")
	public void refreshLicense(HttpServletResponse response){
		loginHolder.refreshData();
		Validate.refreshData();
		moduleService.refreshLicense();
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@RequestMapping(value="enableModule")
	@Dependence(method="list")
	public void enableModule(@RequestParam(value="id")String id,@RequestParam(value="enable")Boolean enable,HttpServletResponse response){
		Module module = moduleService.getEntityById(id);
		if(module!=null && !enable.equals(module.isEnable())){
			String msg = moduleService.enable(module, enable);
			if(StringUtils.isEmpty(msg)){
				getOutputMsg().put("STATE", "SUCCESS");
			}else{
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", msg );
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "该模块不存在或己" + (enable?"启用":"禁用") );
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
}
