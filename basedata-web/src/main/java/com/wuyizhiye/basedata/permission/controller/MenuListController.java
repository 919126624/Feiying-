package com.wuyizhiye.basedata.permission.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.module.enums.BusinessTypeEnum;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.permission.enums.MenuTypeEnum;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.permission.service.MenuService;
import com.wuyizhiye.basedata.util.Validate;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName MenuListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="permission/menu/*")
public class MenuListController extends TreeListController {
	@Autowired
	private MenuService menuService;
	@Override
	protected String getTreeDataMapper() {
		return "com.wuyizhiye.basedata.permission.dao.MenuDao.getChild";
	}

	@Override
	protected CoreEntity createNewEntity() {
		String parent = getString("parent");
		Menu menu = new Menu();
		if(!StringUtils.isEmpty(parent)){
			Menu parentMenu = menuService.getEntityById(parent);
			menu.setParent(parentMenu);
			menu.setModuleType(parentMenu.getModuleType());//设置关联模块枚举
		}
		return menu;
	}

	@Override
	protected String getListView() {
		String bustr = Validate.getCurrBuinessTypeStr();
		this.getRequest().setAttribute("bustr", bustr);
		return "permission/menu/menu2List";
	}

	@Override
	protected String getEditView() {
		getRequest().setAttribute("menuTypes", MenuTypeEnum.values());
		return "permission/menu/menuL2Edit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.permission.dao.MenuDao.select";
	}

	@Override
	protected BaseService<Menu> getService() {
		return menuService;
	}
	
	@RequestMapping(value="getAllMenu")
	public void getAllMenu(HttpServletResponse response){
		Map<String,Object> param = getTreeFilterParam();
		Pagination<Object> page = new Pagination<Object>(Integer.MAX_VALUE,0);
		page = queryExecutor.execQuery(getTreeDataMapper(), page, param);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
		List<Object> menus = page.getItems();
		for(Object obj : menus){
			children = getChildMenu(((Menu) obj).getId());
			if(children!=null && children.size()>0){
				for(int i = 0; i < children.size(); i++){
					result.add(children.get(i));
				}
			}
		}
		outPrint(response, JSONArray.fromObject(result).toString());
	}
	
	private Map<String,Object> menu2map(Menu m){
		Map<String,Object> menu = new HashMap<String, Object>();
		menu.put("id", m.getId());
		menu.put("name", m.getName());
		menu.put("link", m.getLink());
		menu.put("icon", m.getMiniIcon());
		return menu;
	}
	
	private List<Map<String,Object>> getChildMenu(String parent){
		Map<String,Object> param = getTreeFilterParam();
		param.put("parent", parent);
		Pagination<Object> page = new Pagination<Object>(Integer.MAX_VALUE,0);
		page = queryExecutor.execQuery(getTreeDataMapper(), page, param);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
		List<Object> menus = page.getItems();
		Map<String,Object> menu = null;
		for(Object obj : menus){
			menu = menu2map((Menu) obj);
			result.add(menu);
			children = getChildMenu(((Menu) obj).getId());
			if(children!=null && children.size()>0){
				menu.put("children", children);
			}
		}
		return result;
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return "com.wuyizhiye.basedata.permission.dao.MenuDao.getSimpleTreeData";
	}
	
	@Override
	protected boolean isAllowDelete(CoreEntity entity) {
		Menu menu = (Menu) entity;
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("parent", menu.getId());
		List<Menu> children = queryExecutor.execQuery(getTreeDataMapper(), param, Menu.class);
		if(children!=null && children.size()>0){
			setOutputMsg("STATE", "FAIL");
			setOutputMsg("MSG", "存在下级菜单,不能删除");
			return false;
		}
		return super.isAllowDelete(entity);
	}
	
	/**
	 * 列表数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="listMenuL1")
	@Dependence(method="list")
	public void listMenuL1(Pagination<?> pagination,HttpServletResponse response){
		String mapper = "com.wuyizhiye.basedata.permission.dao.MenuDao.getParentMenu";
		String bustr = null ; // Validate.getCurrBuinessTypeStr();
		Map<String,Object> m = getListDataParam();
		m.put("businessType", bustr);
		pagination = queryExecutor.execQuery(mapper, pagination, m);
		afterFetchListData(pagination);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	protected String getMenuL1EditView() {
		getRequest().setAttribute("menuTypes", MenuTypeEnum.values());
		getRequest().setAttribute("businessTypes", BusinessTypeEnum.values());
		getRequest().setAttribute("moduleTypes", ModuleEnum.values());
		
		return "permission/menu/menuL1Edit";
	}
	
	@RequestMapping(value="addMenuL1")
	@Dependence(method="list")
	public String addMenuL1(ModelMap model){
		model.put("data", createNewEntity());
		return getMenuL1EditView();
	}
	@RequestMapping(value="editMenuL1")
	@Dependence(method="list")
	public String editMenuL1(ModelMap model,@RequestParam(required=true,value="id")String id){
		model.put("data", getService().getEntityById(id));
		//将当前状态放入到界面 addded by taking.wang 2013-01-24
		model.put("edit_viewStatus", this.getString("VIEWSTATE"));
		return getMenuL1EditView();
	}
}
