package com.wuyizhiye.basedata.permission.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.permission.model.PermissionItem;
import com.wuyizhiye.basedata.permission.service.PermissionItemService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PermissionAssignListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="permission/assgin/*")
public class PermissionAssignListController extends ListController {

	@Autowired
	private PermissionItemService permissionService;

	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return "permission/permissionAssignManage";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.basedata.permission.dao.PermissionAssignDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@RequestMapping(value="permissionByFunc")
	protected String permissionByFunc() {
		
		return "permission/permissionByFuncList";
	}
	
	@RequestMapping(value="listPmData")
	public void listPmData(Pagination<PermissionItem> pagination,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		String mid = this.getString("moduleId");
		String ismoduleType = this.getString("ismoduleType");
		String key = this.getString("key");
		String modules = "";
		if(StringUtils.isNotNull(mid)){
		if("TRUE".equals(ismoduleType)){
			ModuleEnum[] mary = ModuleEnum.values();
			for(int i=0;i<mary.length;i++){
				if(mid.equals(mary[i].getParent().toString())){
					modules += "'"+mary[i]+"',";
				}
			}
			if(modules.length()>0){
				modules = modules.substring(0, modules.length()-1);		
				map.put("modules", modules);
			}else{
				map.put("modules", "'FFFFF'");
			}
		}else{
			modules += "'"+mid+"'";
			map.put("modules", modules);
		}
		}
		map.put("key", key);
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.PermissionItemDao.select", pagination, map);
		afterFetchListData(pagination);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
}
