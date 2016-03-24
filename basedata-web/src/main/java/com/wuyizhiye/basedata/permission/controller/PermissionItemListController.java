package com.wuyizhiye.basedata.permission.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.module.enums.BusinessTypeEnum;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.module.model.Module;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.permission.enums.PermissionTypeEnum;
import com.wuyizhiye.basedata.permission.model.PermissionGroup;
import com.wuyizhiye.basedata.permission.model.PermissionItem;
import com.wuyizhiye.basedata.permission.service.PermissionGroupService;
import com.wuyizhiye.basedata.permission.service.PermissionItemService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PermissionItemListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller(value="permissionListController")
@RequestMapping(value="permission/permissionItem/*")
public class PermissionItemListController extends ListController {
	@Autowired
	private PermissionItemService permissionService;
	@Autowired
	private PermissionGroupService permissionGroupService;
	@Override
	protected CoreEntity createNewEntity() {
		String groupid = getString("group");
		PermissionItem permission = new PermissionItem(); 
		if(!StringUtils.isEmpty(groupid)){
			PermissionGroup group = permissionGroupService.getEntityById(groupid);
			permission.setGroup(group);
		}
		return permission;
	}

	@Override
	protected String getListView() {
		getRequest().setAttribute("isModuleType",getString("isModuleType"));
		getRequest().setAttribute("moduleType",getString("moduleType"));
		getRequest().setAttribute("moduleEnumList", ModuleEnum.values());
		return "permission/permissionItemModuleList";
	}

	@Override
	protected String getEditView() {
		List<Module> listModule=queryExecutor.execQuery("com.wuyizhiye.base.module.dao.ModuleDao.select",null,Module.class);
		for (int i = 0; i < listModule.size(); i++) {
			if(!listModule.get(i).isEnable()){
				listModule.remove(i);
			}
		}
		PermissionTypeEnum[] pmtlist = PermissionTypeEnum.values();
		getRequest().setAttribute("permtypeList", pmtlist);
		getRequest().setAttribute("moduleEnumList", listModule);
		getRequest().setAttribute("moduleType", getString("moduleType"));
		return "permission/permissionItemModuleEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.permission.dao.PermissionItemDao.select";
	}

	@Override
	protected BaseService<PermissionItem> getService() {
		return permissionService;
	}

	/**
	 * 多选数据选择器
	 * @param model
	 * @return
	 */
	@RequestMapping(value="permissionDataPicker")
	public String permissionDataPicker(ModelMap model){
		Map<String,String> param = getParamMap();
		Set<String> keys = param.keySet();
		for(String s : keys){
			model.put(s, param.get(s));
		}
		model.put("param", param);
		return "permission/permissionDataPicker";
	}
	
	/**
	 * 树形多选数据选择器(一次全加载)
	 * @return
	 */
	@RequestMapping(value="permissionTreeDataPicker")
	public String permissionTreeDataPicker(){
		return "permission/permissionTreeDataPicker";
	}
	
	@RequestMapping(value="permissionAll")
	public void permissionAll(HttpServletResponse response){
		@SuppressWarnings("rawtypes")
		List<Map> allData = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.PermissionItemDao.getPermissionItemTree", null, Map.class);
		outPrint(response, JSONArray.fromObject(allData));
	}
	
	@RequestMapping(value="loadPermissions")
	public synchronized void loadPermissions(){
		List<String> methods = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.PermissionItemDao.selectMethodList", new HashMap<String,Object>(), String.class);
		Set<String> permissionMethods = new HashSet<String>();
		for(String method : methods){
			permissionMethods.add(method);
		}
		getServletContext().setAttribute("WHOLE-PERMISSIONMETHODS-"+DataSourceHolder.getDataSource(), permissionMethods);
	}
	
	@RequestMapping(value = "validateNumber")
	@ResponseBody
	public Pagination<PermissionItem> validateNumber(){
		Map<String,Object> paraMap =new HashMap<String,Object>();
		paraMap.put("number", getString("number"));
		//功能权限不判断编码重复,只判断菜单权限
		paraMap.put("permissiontype", "MENU");
		Pagination<PermissionItem> permissions = queryExecutor.execQuery(getListMapper(), new Pagination<PermissionItem>(10, 1), paraMap);
		return permissions;
	}
	
	
	/**
	 * 列表数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="listData")
	@Dependence(method="list")
	public void listData(Pagination<?> pagination,HttpServletResponse response){
		if(StringUtils.isNotNull(getListDataParam().get("moduleType"))){
			pagination = queryExecutor.execQuery(getListMapper(), pagination, getListDataParam());
			afterFetchListData(pagination);
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	/**
	 * 列表数据  岗位权限
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="listData4NoJobPms")
	@Dependence(method="list")
	public void listData4NoJobPms(Pagination<?> pagination,HttpServletResponse response){
		if(StringUtils.isNotNull(getListDataParam().get("moduleType"))){
			String mapper = "com.wuyizhiye.basedata.permission.dao.PermissionItemDao.select4JobPms";
			pagination = queryExecutor.execQuery(mapper, pagination, getListDataParam());
			afterFetchListData(pagination);
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	/**
	 * 列表数据  岗位权限
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="listData4JobPms")
	@Dependence(method="list")
	public void listData4JobPms(Pagination<?> pagination,HttpServletResponse response){
		if(StringUtils.isNotNull(getListDataParam().get("moduleType"))){
			String mapper = "com.wuyizhiye.basedata.permission.dao.JobPermissionDao.select";
			pagination = queryExecutor.execQuery(mapper, pagination, getListDataParam());
			afterFetchListData(pagination);
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	@Override
	protected Map<String, String> getParamMap() {
		Map<String, String> param = super.getParamMap();
		String isModuleType = param.get("isModuleType");
		if("FALSE".equals(isModuleType)){
			StringBuilder str = new StringBuilder("");
			str.append("'").append(param.get("moduleType")).append("'");
			param.put("modules", str.toString());
			return param;
		}
		if(param.containsKey("moduleType") && !StringUtils.isEmpty(param.get("moduleType"))){
			BusinessTypeEnum moduleType = Enum.valueOf(BusinessTypeEnum.class, param.get("moduleType"));
			BusinessTypeEnum[] allModuleType = BusinessTypeEnum.values();
			List<BusinessTypeEnum> types = new ArrayList<BusinessTypeEnum>(); 
			for(BusinessTypeEnum e : allModuleType){
				if(e==moduleType || e.getParent() == moduleType){
					types.add(e);
				}
			}
			ModuleEnum[] allModule = ModuleEnum.values();
			StringBuilder stringBuilder = new StringBuilder("");
			for(ModuleEnum e : allModule){
				for(BusinessTypeEnum t : types){
					if(e.getParent() == t){
						stringBuilder.append("'").append(e).append("',");
					}
				}
			}
			if(stringBuilder.length() > 1){
				stringBuilder = stringBuilder.deleteCharAt(stringBuilder.length()-1);
			}else{
				stringBuilder.append("'nodata'");
			}
			param.put("modules", stringBuilder.toString());
		}
		return param;
	}
	
}
