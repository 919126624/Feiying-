package com.wuyizhiye.basedata.org.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.module.enums.BusinessTypeEnum;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.BusinessType;
import com.wuyizhiye.basedata.org.service.BusinessTypeService;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName BusinessTypeListController
 * @Description 业务类型controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/businessType/*")
public class BusinessTypeListController extends TreeListController {
	@Autowired
	private BusinessTypeService businessTypeService;
	@Override
	protected String getTreeDataMapper() {
		return null;
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return null;
	}

	@Override
	protected CoreEntity createNewEntity() {
		BusinessType data = new BusinessType();
		String type = getString("type");
		if(StringUtils.isEmpty(type)){
			data.setType(BusinessTypeEnum.TYHY);
		}else{
			BusinessTypeEnum btype = Enum.valueOf(BusinessTypeEnum.class, type);
			data.setType(btype);
		}
		return data;
	}

	@Override
	protected String getListView() {
		return "basedata/org/businessTypeList";
	}

	@Override
	protected String getEditView() {
		getRequest().setAttribute("types", BusinessTypeEnum.values());
		return "basedata/org/businessTypeEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.org.dao.BusinessTypeDao.select";
	}

	@Override
	protected BaseService<BusinessType> getService() {
		return businessTypeService;
	}
	
	@Override
	public void simpleTreeData(HttpServletResponse response) {
		BusinessTypeEnum[] types = BusinessTypeEnum.values();
		List<Map<String,Object>> treeData = new ArrayList<Map<String,Object>>();
		for(BusinessTypeEnum e : types){
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("id", e);
			m.put("name", e.getLabel());
			m.put("value", e.getValue());
			treeData.add(m);
		}
		outPrint(response, JSONArray.fromObject(treeData));
	}
}
