package com.wuyizhiye.basedata.shortcut.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.shortcut.model.Shortcut;
import com.wuyizhiye.basedata.shortcut.service.ShortcutService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName ShortcutEditController
 * @Description 快捷菜单
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/shortcut/*")
public class ShortcutEditController extends EditController{
	
	@Autowired
	private ShortcutService shortcutService;

	@Override
	protected Class<Shortcut> getSubmitClass() {
		return Shortcut.class;
	}

	@Override
	protected BaseService<Shortcut> getService() {
		return shortcutService;
	}
	
	@RequestMapping(value="setAccreditRow")
	public void setAccreditRow(HttpServletResponse response) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("id", getString("personId"));
		int count = queryExecutor.execCount("com.wuyizhiye.basedata.org.dao.JobDao.select", param);
		if(count>=1){
			shortcutService.setAccredit(getString("shortcutId"),getString("personId"));
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "设置成功");
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		}else{
			getOutputMsg().put("MSG", "请选中正确的岗位授权");
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		}
	}
	
	@RequestMapping(value="delAccreditRow")
	public void delAccreditRow(HttpServletResponse response) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("id", getString("personId"));
		int count = queryExecutor.execCount("com.wuyizhiye.basedata.org.dao.JobDao.select", param);
		if(count>=1){
			shortcutService.delAccredit(getString("shortcutId"),getString("personId"));
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "设置成功");
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		}else{
			getOutputMsg().put("MSG", "请选中正确的岗位授权");
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		}
	}
	
	@Override
	protected boolean validate(Object data) {
		Shortcut shortcut = (Shortcut)data;
		if(shortcut.getType().getValue().equals("MENU")){
			String menuId = shortcut.getMenu().getId();
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("id", menuId);
			Menu menu = queryExecutor.execOneEntity("com.wuyizhiye.basedata.permission.dao.MenuDao.getById", param, Menu.class);
			shortcut.setUrl(menu.getLink());
		}
		return true;
	}

}
