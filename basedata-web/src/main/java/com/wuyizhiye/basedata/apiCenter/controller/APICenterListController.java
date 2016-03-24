package com.wuyizhiye.basedata.apiCenter.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.enums.DataTypeEnum;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.apiCenter.model.APIItem;
import com.wuyizhiye.basedata.apiCenter.model.APIParameter;
import com.wuyizhiye.basedata.apiCenter.service.APIItemService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName APICenterListController
 * @Description 接口中心列表controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/apiCenter/*")
public class APICenterListController extends ListController {
	@Autowired
	private APIItemService apiItemService;
	
	@Override
	protected CoreEntity createNewEntity() {
		
		 APIItem item=new APIItem();
		 item.setModuleType(getString("moduleType"));
		 
		 return item;
	}
	
	@Override
	protected String getListView() {
		getRequest().setAttribute("isModuleType",getString("isModuleType"));
		getRequest().setAttribute("moduleType",getString("moduleType"));
		getRequest().setAttribute("moduleEnumList", ModuleEnum.values());
		return "basedata/apiCenter/apiItemList";
	}
	 
	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.apiCenter.dao.APIItemDao.select";	
	}

	@Override
	protected BaseService<APIItem> getService() {
		return apiItemService;
	}
 
	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		getRequest().setAttribute("dataTypeList", DataTypeEnum.values());
		String id=getString("id");
		if(!StringUtils.isEmpty(id)){
			Map<String, Object> param=new HashMap<String, Object>();
			param.put("itemId", id);
			List<APIParameter> parameterList=queryExecutor.execQuery("com.wuyizhiye.basedata.apiCenter.dao.APIParameterDao.select", param, APIParameter.class);
			getRequest().setAttribute("parameterList", parameterList);
		}
		return "basedata/apiCenter/apiItemEdit";
	}
	  
}
