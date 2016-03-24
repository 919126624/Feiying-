package com.wuyizhiye.basedata.permission.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.permission.model.JobPermission;
import com.wuyizhiye.basedata.permission.model.PersonPermission;
import com.wuyizhiye.basedata.permission.service.PersonPermissionService;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.service.PersonPositionService;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PersonPermissionListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="permission/personPermission/*")
public class PersonPermissionListController extends ListController {
	@Autowired
	private PersonPermissionService personPermissionService;
	@Autowired
	private PersonPositionService personPositionService;
	@Autowired
	private PersonService personService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		return "permission/personPermissionList";
	}

	@Override
	protected String getEditView() {
		return null;
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.permission.dao.PersonPermissionDao.getPersonPermission";
	}

	@Override
	protected BaseService<PersonPermission> getService() {
		return personPermissionService;
	}
	
	@Override
	protected Map<String, Object> getListDataParam() {
		Map<String, Object> param = super.getListDataParam();
		param.put("person", "nodata");
		param.put("position", "nodata");
		if(param.containsKey("personPosition")){
			PersonPosition pp = personPositionService.getEntityById(param.get("personPosition").toString());
			if(pp!=null){
				param.put("person", pp.getPerson().getId());
				param.put("position", pp.getPosition().getId());
			}
		}
		return param;
	}

	@RequestMapping(value="savePersonPermission")
	public void savePersonPermission(HttpServletResponse response){
		String ppid = getString("personPosition");
		if(!StringUtils.isEmpty(ppid)){
			PersonPosition pp = personPositionService.getEntityById(ppid);
			if(pp!=null){
				String permissions = getString("permissions");
				String[] permArray = permissions.split(";");
				List<String> permissionList = new ArrayList<String>();
				for(String p : permArray){
					permissionList.add(p);
				}
				personPermissionService.savePersonPermissions(pp.getPerson(), pp.getPosition(), permissionList);
			}
		}
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	
	@RequestMapping(value="singlePersonPermission")
	public String singlePersonPermission(@RequestParam(value="person",required=true)String id,ModelMap model){
		Person person = personService.getEntityById(id);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("person", id);
		List<PersonPosition> pps = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonPositionDao.select", param , PersonPosition.class);
		model.put("person", person);
		model.put("pps", pps);
		return "permission/singlePersonPermission";
	}
	
	@RequestMapping(value="personPermission")
	public void getPersonPermission(Pagination<PersonPermission> pagination,HttpServletResponse response){
		String ppid = getString("personPosition");
		String key = getString("key");
		if(!StringUtils.isEmpty(ppid)){
			PersonPosition pp = personPositionService.getEntityById(ppid);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("position", pp.getPosition().getId());
			param.put("person", pp.getPerson().getId());
			param.put("key", key);
			pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.PersonPermissionDao.select", pagination, param);
		}
		outPrint(response, JSONObject.fromObject(pagination));
	}
	
	@RequestMapping(value="jobPermission")
	public void getJobPermission(Pagination<JobPermission> pagination,HttpServletResponse response){
		String ppid = getString("personPosition");
		String key = getString("key");
		if(!StringUtils.isEmpty(ppid)){
			PersonPosition pp = personPositionService.getEntityById(ppid);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("position", pp.getPosition().getId());
			param.put("key", key);
			pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.JobPermissionDao.getByPosition", pagination, param);
		}
		outPrint(response, JSONObject.fromObject(pagination));
	}
}
