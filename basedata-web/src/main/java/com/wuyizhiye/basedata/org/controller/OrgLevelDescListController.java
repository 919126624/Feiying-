package com.wuyizhiye.basedata.org.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.service.OrgLevelDescService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName OrgLevelDescListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/orgleveldesc/*")
public class OrgLevelDescListController extends ListController {

	@Autowired
	private OrgLevelDescService orgLevelDescService;
	
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return "basedata/org/orgLevelDescList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return "basedata/org/orgLevelDescEdit";
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.basedata.org.dao.OrgLevelDescDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return orgLevelDescService;
	}
	
	@Override
	protected boolean isAllowDelete(CoreEntity entity) {
		String orgdevelId = entity.getId();
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("orgLevelDesc", orgdevelId);
		int count = 
		this.queryExecutor.execCount("com.wuyizhiye.basedata.org.dao.OrgDao.select", param);
		if(count>0){
			getOutputMsg().put("MSG", "有组织关联该组织级别,请先取消关联");
			return false;
		}
		return true;
	}
	
}
