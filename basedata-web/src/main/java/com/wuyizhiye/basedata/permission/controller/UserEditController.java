package com.wuyizhiye.basedata.permission.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.permission.util.SecurityUtil;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.framework.util.BeanUtils;

/**
 * @ClassName UserEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="permission/user/*")
public class UserEditController extends EditController {
	@Autowired
	private PersonService personService;
	@Override
	protected Class<Person> getSubmitClass() {
		return Person.class;
	}

	@Override
	protected BaseService<Person> getService() {
		return personService;
	}
	
	protected Object getSubmitEntity(){
		String id = getString("id");
		Person entity = null;
		entity = getService().getEntityById(id);
		String oldPassword = entity.getPassword();
		entity = BeanUtils.fillentity(getParamMap(),entity, Person.class);
		String pwd1 = getString("pwd1");
		if(!StringUtils.isEmpty(pwd1)){
			entity.setPassword(SecurityUtil.encryptPassword(pwd1));
		} else{
			entity.setPassword(null);
		}
		/*if(StringUtils.isEmpty(entity.getPassword())){
			entity.setPassword(oldPassword);
		}*/
		return entity;
	}
	
	protected boolean validate(Object data) {
		Person person = (Person)data;
		if(StringUtils.isEmpty(person.getUserName())){
			getOutputMsg().put("MSG", "用户名不能为空");
			getOutputMsg().put("STATE", "FAIL");
			return false;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userName", person.getUserName());
		List<Person> persons = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.selectUser", param , Person.class);
		if(persons!=null && persons.size()>0){
			for(Person p : persons){
				if(!p.getId().equals(person.getId())){
					getOutputMsg().put("MSG", "该用户名己存在");
					getOutputMsg().put("STATE", "FAIL");
					return false;
				}
			}
		}
		String pwd1 = getString("pwd1");
		String pwd2 = getString("pwd2");
		if(!StringUtils.isEmpty(pwd1) && pwd1.equals(pwd2)){
			return true;
		}
		if(StringUtils.isEmpty(pwd1) && StringUtils.isEmpty(pwd2)){
			return true;
		}
		getOutputMsg().put("MSG", "密码不一致");
		getOutputMsg().put("STATE", "FAIL");
		return false;
	}
	
	
	@RequestMapping(value="resetPassword")
	public void resetPassword(HttpServletResponse response){
		getOutputMsg().put("STATE", "SUCCESS");
		String pwd1 = getString("pwd1");
		String pwd2 = getString("pwd2");
		if(!StringUtils.isEmpty(pwd1) && pwd1.equals(pwd2)){
			Person person = personService.getEntityById(SystemUtil.getCurrentUser().getId());
			person.setPassword(SecurityUtil.encryptPassword(pwd1));
			personService.updateEntity(person);
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "密码为空或密码不一致");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	/**
	 * 判断原密码
	 * @param response
	 */
	@RequestMapping(value="judge/password")
	public void judgePassword(HttpServletResponse response){
		String oriPassword = this.getString("oriPassword");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("userName", this.getCurrentUser().getUserName());
		param.put("password", SecurityUtil.encryptPassword(oriPassword));	//加密
		List<Person> persons = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.loginUser", new Pagination<Person>(), param).getItems();
		this.getOutputMsg().clear();
		if(persons != null && persons.size() > 0){
			this.getOutputMsg().put("STATE", "SUCCESS");
		} else {
			this.getOutputMsg().put("STATE", "FAIL");
			this.getOutputMsg().put("MSG", "原密码错误！");
		}
		this.outPrint(response, JSONObject.fromObject(this.getOutputMsg(),this.getDefaultJsonConfig()));
	}
}
