package com.wuyizhiye.basedata.helpinfo.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.module.model.ModuleLic;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.org.enums.WorkBenchTypeEnum;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.permission.enums.MenuTypeEnum;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.basedata.util.Validate;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName HelpInfoController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/helpInfo/*")
public class HelpInfoController extends TreeListController{

	@Override
	protected String getTreeDataMapper() {
		return "com.wuyizhiye.projectm.helpinfo.dao.getChild";
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return "com.wuyizhiye.basedata.permission.dao.MenuDao.getSimpleTreeData";
	}

	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		return "basedata/helpInfo/helpInfoList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.projectm.helpinfo.dao.HelpInfoDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void listData(Pagination<?> pagination,HttpServletResponse response){
		String requestData = HttpClientUtil.callHttpUrl(SystemConfig.getParameter("helpinfourl")+"?menuId="+getString("menuId"), "");
//		String requestData = HttpClientUtil.callHttpUrl("http://localhost:8080/web/projectm/helpInfo/queryHelpInfoData?menuId="+getString("menuId"), "");
		JSONObject jObj = null;
		if(!StringUtils.isEmpty(requestData)){
			jObj = JSONObject.fromObject(requestData);
		}
		outPrint(response, jObj);
	}
	
	@Override
	public void simpleTreeData(HttpServletResponse response){
		Person person = SystemUtil.getCurrentUser();
		Position position = SystemUtil.getCurrentPosition();
		
		WorkBenchTypeEnum workBenchType = WorkBenchTypeEnum.SIMPLE;
		if(position!=null){
			Job job = position.getJob();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("id", job.getId());
			job = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobDao.select", param , Job.class).get(0);
			if(job.getWorkBenchType()!=null){
				workBenchType = job.getWorkBenchType();
			}
		}
		List<Menu> templist = getMenu(person,position,workBenchType);
		outPrint(response, JSONArray.fromObject(templist, getDefaultJsonConfig()));
	}
	
	/***
	 * 取电脑菜单方法
	 * @param person
	 * @param position
	 * @param workBenchType
	 * @return
	 */
	private List<Menu> getMenu(Person person,Position position,WorkBenchTypeEnum workBenchType){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", MenuTypeEnum.PC.getValue());
		param.put("notEnableFlag", "Y");
		List<Menu> menus = null;
		if(WorkBenchTypeEnum.ICON.equals(workBenchType)){
			param.put("checkPermission", true);//是否检查权限
			param.put("person", person.getId());
			param.put("position", position.getId());
			param.put("includeLink", true);
			
			menus = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.select", param, Menu.class);
		}else{
			menus = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.getChild", param , Menu.class);
			Iterator<Menu> iterator = menus.iterator();
			param.clear();
			param.put("notEnableFlag", "Y");
			param.put("checkPermission", true);//是否检查权限
			param.put("person", person.getId());
			param.put("position", position.getId());
			while(iterator.hasNext()){
				Menu m = iterator.next();
				param.put("longNumber", m.getLongNumber());
				param.put("includeLink", true);
				List<Menu> children = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.getChild", param, Menu.class);
				if(children==null || children.size() == 0){
					iterator.remove();
				}else{
					m.setChildren(children);
				}
			}
		}
		/**
		 * 根据license过滤菜单
		 */
		if(menus!=null && menus.size()>0){
			Map<ModuleEnum,ModuleLic> perms = Validate.getCurrPerms();
			if(perms!=null && perms.size()>0){
				Iterator<Menu> iterator = menus.iterator();
				while(iterator.hasNext()){
					Menu m = iterator.next();
					if(perms.get(m.getModuleType())==null){
						iterator.remove();
					}
				}
			}else{
				return null;
			} 
		}
		return menus;
	}
	
	
}
