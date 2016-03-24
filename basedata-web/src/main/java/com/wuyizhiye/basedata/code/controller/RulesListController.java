package com.wuyizhiye.basedata.code.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.module.enums.BusinessTypeEnum;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.code.enums.PriorityEnum;
import com.wuyizhiye.basedata.code.model.RuleItems;
import com.wuyizhiye.basedata.code.model.Rules;
import com.wuyizhiye.basedata.code.service.RulesService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName RulesListController
 * @Description 编号规则列表controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value = "basedata/code/*")
public class RulesListController extends ListController {

	@Autowired
	private RulesService rulesService;

	@Override
	protected CoreEntity createNewEntity() {
		return new Rules();
	}

	@Override
	protected String getListView() {
		getRequest().setAttribute("isModuleType",getString("isModuleType"));
		getRequest().setAttribute("moduleType",getString("moduleType"));
		getRequest().setAttribute("moduleEnumList", ModuleEnum.values());
		return "basedata/code/rulesList";
	}
	
	
	@RequestMapping(value="add")
	@Dependence(method="list")
	public String add(ModelMap model){
		model.put("data", createNewEntity());
		getRequest().setAttribute("moduleType", getString("moduleType"));
		return getEditView();
	}
	
	@RequestMapping(value="edit")
	@Dependence(method="list")
	public String edit(ModelMap model,@RequestParam(required=true,value="id")String id){
		Rules rules=(Rules)getService().getEntityById(id);
		model.put("data", rules);
		if(getString("moduleType")==null){
			getRequest().setAttribute("moduleType", rules.getModule());
		}else{
			getRequest().setAttribute("moduleType", getString("moduleType"));
		}
		model.put("edit_viewStatus", this.getString("VIEWSTATE"));
		return getEditView();
	}
	
	
	@Override
	protected String getEditView() {
		String typeid = getString("typeid");
		String typename = getString("typename");
		if(typeid!=null){
			getRequest().setAttribute("typeid",typeid);
			getRequest().setAttribute("typename", typename);	
		}
		getRequest().setAttribute("priorityTypes", PriorityEnum.values());
		return "basedata/code/rulesEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.code.RulesDao.select";
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		return rulesService;
	}

	/**
	 * 列表数据
	 * 
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value = "listData")
	public void listData(Pagination<?> pagination, HttpServletResponse response) {
		if(StringUtils.isNotNull(getListDataParam().get("moduleType"))){
			pagination = queryExecutor.execQuery(getListMapper(), pagination,
					getListDataParam());
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	@RequestMapping(value = "listItemsData")
	@ResponseBody
	public Pagination<RuleItems> listItemsData(Pagination<?> pagination,HttpServletResponse response) {
		return getItemsData(pagination);
	}

	@SuppressWarnings("unchecked")
	private Pagination<RuleItems> getItemsData(Pagination<?> pagination) {
		return (Pagination<RuleItems>) queryExecutor
						.execQuery("com.wuyizhiye.basedata.code.RuleItemsDao.select",
								pagination, getListDataParam());
	}
	
	@RequestMapping(value="updateStatus")
	public void enable(Rules rules,HttpServletResponse response){
		rulesService.updateStatus(rules);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	
	
	@Override
	protected Map<String, String> getParamMap() {
		Map<String, String> param = super.getParamMap();
		String isModuleType = param.get("isModuleType");
		if("FALSE".equals(isModuleType)){
			StringBuilder str = new StringBuilder("");
			str.append("'").append(param.get("moduleType")).append("'");
			param.put("modules", str.toString());
			return param;
		}
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
}
