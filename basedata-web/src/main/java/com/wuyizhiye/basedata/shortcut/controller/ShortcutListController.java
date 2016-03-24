package com.wuyizhiye.basedata.shortcut.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.permission.service.MenuService;
import com.wuyizhiye.basedata.shortcut.enums.ShortcutTypeEnum;
import com.wuyizhiye.basedata.shortcut.model.OpenTypeEnum;
import com.wuyizhiye.basedata.shortcut.model.Shortcut;
import com.wuyizhiye.basedata.shortcut.service.ShortcutService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName ShortcutListController
 * @Description 快捷菜单
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/shortcut/*")
public class ShortcutListController extends ListController{
	
	@Autowired
	private ShortcutService shortcutService;
	
	@Autowired
	private MenuService menuService;

	@Override
	protected CoreEntity createNewEntity() {
		return new Shortcut();
	}

	@Override
	protected String getListView() {
		List<Menu> menuList = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.getParentMenu", new HashMap<String,Object>(), Menu.class);
		getRequest().setAttribute("shortcutTypeList", ShortcutTypeEnum.values());
		getRequest().setAttribute("openTypeEnum", OpenTypeEnum.values());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("menuList", menuList);
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("longNumber", menuList.get(0).getLongNumber());
		List<Menu> subMenuList = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.getSimpleTreeData", param, Menu.class);
		getRequest().setAttribute("menuList", menuList);
		getRequest().setAttribute("subMenuList", subMenuList);
		return "basedata/shortcut/shortcutList";
	}

	@Override
	protected String getEditView() {
		return "basedata/shortcut/shortcutEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.shortcut.dao.ShortcutDao.select";
	}

	@Override
	protected BaseService<Shortcut> getService() {
		return shortcutService;
	}
	
	@RequestMapping(value="getShortcutById")
	public void getShortcutById(HttpServletResponse response) {
		Shortcut shortcut = shortcutService.getEntityById(getString("id"));
		if(shortcut.getType().getValue().equals("MENU")){
			Menu menu = menuService.getEntityById(shortcut.getMenu().getId());
			if(StringUtils.isNotNull(menu)){
				getOutputMsg().put("parentLongNumber", menu.getParent().getNumber());
			}else{
				getOutputMsg().put("STATE", "fila");
				getOutputMsg().put("MSG", "菜单已经删除，请删除后新增！");
				outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
				return;
			}
		}
		getOutputMsg().put("shortcut", shortcut);
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="getMenuByLongNumber")
	public void getMenuByLongNumber(HttpServletResponse response) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("longNumber", getString("longNumber"));
		List<Menu> subMenuList = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.getSimpleTreeData", param, Menu.class);
		getOutputMsg().put("subMenuList", subMenuList);
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="toIndex")
	public String toIndex(HttpServletResponse response) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("job", SystemUtil.getCurrentPosition().getJob().getId());
		List<Shortcut> shortcutList = queryExecutor.execQuery("com.wuyizhiye.basedata.shortcut.dao.ShortcutDao.selectByJob", param,Shortcut.class);
		getRequest().setAttribute("shortcutList", shortcutList);
		return "basedata/shortcut/shortcut";
	}

}
