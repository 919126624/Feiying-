package com.wuyizhiye.basedata.permission.controller;

import java.util.List;

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
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.service.PersonPositionService;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName UserListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="permission/user/*")
public class UserListController extends ListController {
	@Autowired
	private PersonService personService;
	@Autowired
	private PersonPositionService personPositionService;
	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		return "permission/user/userList";
	}

	@Override
	protected String getEditView() {
		return "permission/user/userEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.person.dao.PersonDao.selectUser";
	}

	@Override
	protected BaseService<Person> getService() {
		return personService;
	}
	
	@Override
	public String edit(ModelMap model,@RequestParam(required=true,value="id")String id){
		List<PersonPosition> personPosition = personPositionService.getByPerson(id);
		model.put("personPositions", personPosition);
		Person person = getService().getEntityById(id);
		for(PersonPosition pp : personPosition){
			if(pp.isPrimary()){
				person.setPersonPosition(pp);
				break;
			}
		}
		model.put("data", person);
		return getEditView();
	}
	
	@Override
	protected JsonConfig getDefaultJsonConfig() {
		JsonConfig config = super.getDefaultJsonConfig();
		config.registerJsonValueProcessor(UserStatusEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof UserStatusEnum){
					return ((UserStatusEnum)value).getName();
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
	
	@RequestMapping(value="enable")
	public void enable(Person person,HttpServletResponse response){
		Person old = personService.getEntityById(person.getId());
		old.setStatus(person.getStatus());
		personService.updateEntity(old);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	
	@RequestMapping(value="assignPermission")
	public String assignPermission(){
		return "permission/user/assignPermission";
	}
}
