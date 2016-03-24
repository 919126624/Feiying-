package com.wuyizhiye.basedata.code.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.module.enums.BusinessTypeEnum;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.code.enums.PriorityEnum;
import com.wuyizhiye.basedata.code.model.BillType;
import com.wuyizhiye.basedata.code.service.BillTypeService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName BillTypeListController
 * @Description 单据类型列表控制器
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value = "basedata/billType/*")
public class BillTypeListController extends ListController{

	@Autowired
	private BillTypeService billTypeService;
	
	@Override
	protected CoreEntity createNewEntity() {
		BillType bt=new BillType();
		bt.setModule(getString("moduleType"));
		return bt;
	}

	@Override
	protected String getListView() {
		getRequest().setAttribute("isModuleType",getString("isModuleType"));
		getRequest().setAttribute("moduleType",getString("moduleType"));
		getRequest().setAttribute("moduleEnumList", ModuleEnum.values());
		return "basedata/code/billTypeList";
	}

	@Override
	protected String getEditView() {
		getRequest().setAttribute("priorityTypes", PriorityEnum.values());
		getRequest().setAttribute("moduleType", getString("moduleType"));
		return "basedata/code/billTypeEdit";
	}
	
	@RequestMapping(value="toBillTypePicker")
	protected String toBillTypePicker() {
		getRequest().setAttribute("isModuleType",getString("isModuleType"));
		getRequest().setAttribute("moduleType",getString("moduleType"));
		return "basedata/code/billTypePicker";
	}
	
	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.code.BillTypeDao.select";
	}

	@Override
	protected BaseService getService() {
		return billTypeService;
	}
	
	
	/**
	 * 列表数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="listData")
	@Dependence(method="list")
	public void listData(Pagination<?> pagination,HttpServletResponse response){
		if(StringUtils.isNotNull(getListDataParam().get("moduleType"))){
			pagination = queryExecutor.execQuery(getListMapper(), pagination, getListDataParam());
			afterFetchListData(pagination);
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
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
