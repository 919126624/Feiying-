package com.wuyizhiye.basedata.permission.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.permission.service.MenuService;
import com.wuyizhiye.basedata.permission.util.SecurityUtil;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName MenuEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="permission/menu/*")
public class MenuEditController extends EditController {
	@Autowired
	private MenuService menuService;
	@Override
	protected Class<Menu> getSubmitClass() {
		return Menu.class;
	}

	@Override
	protected BaseService<Menu> getService() {
		return menuService;
	}
	
	@Override
	protected boolean validate(Object data) {
		Menu menu = (Menu) data;
		if(StringUtils.isEmpty(menu.getNumber())){
			setOutputMsg("STATE","FAIL");
			setOutputMsg("MSG", "编码不能为空");
			return false;
		}
		if(StringUtils.isEmpty(menu.getName())){
			setOutputMsg("STATE","FAIL");
			setOutputMsg("MSG", "名称不能为空");
			return false;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("number", menu.getNumber());
		List<Menu> menus = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.select", param, Menu.class);
		if(menus!=null && menus.size()>0){
			for(Menu m : menus){
				if(!m.getId().equals(menu.getId())){
					setOutputMsg("STATE","FAIL");
					setOutputMsg("MSG", "该编码己存在");
					return false;
				}
			}
		}
		param.clear();
		/* 菜单去掉名字相同限制 黄文 2013-09-11
		param.put("name", menu.getName());
		menus = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.select", param, Menu.class);
		if(menus!=null && menus.size()>0){
			for(Menu m : menus){
				if(!m.getId().equals(menu.getId())){
					setOutputMsg("STATE","FAIL");
					setOutputMsg("MSG", "该名称己存在");
					return false;
				}
			}
		}*/
		
		return true;
	}

	@Override
	protected String getImagePath() {
		// TODO Auto-generated method stub
		String flag = this.getString("flag");
		if("largeIcon".equalsIgnoreCase(flag)){
			return "initSystem/menu/largeIcon";
		}else if("miniIcon".equalsIgnoreCase(flag)){
			return "initSystem/menu/miniIcon";
		}else if("mobileIcon".equalsIgnoreCase(flag)){
			return "initSystem/menu/mobileIcon";
		}
		return "initSystem/menu/miniIcon";
	}
	
	@RequestMapping(value="updateEnableFlag")
	public void updateEnableFlag(HttpServletResponse response){
		getOutputMsg().put("STATE", "SUCCESS");
		String id = getString("id");
		String enableFlag = getString("enableFlag","Y");
		Map<String ,Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("enableFlag", enableFlag);
		queryExecutor.executeUpdate("com.wuyizhiye.basedata.permission.dao.MenuDao.updateEnableFlag", param);
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
}
