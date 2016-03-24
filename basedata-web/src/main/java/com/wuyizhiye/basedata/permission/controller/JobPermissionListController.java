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
import com.wuyizhiye.base.module.model.Module;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.permission.model.JobPermission;
import com.wuyizhiye.basedata.permission.model.PermissionItem;
import com.wuyizhiye.basedata.permission.service.JobPermissionService;
import com.wuyizhiye.basedata.util.Validate;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName JobPermissionListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="permission/jobPermission/*")
public class JobPermissionListController extends ListController {
	@Autowired
	private JobPermissionService jobPermissionService;
	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		return "permission/permissionAssignManage";
	}

	@Override
	protected String getEditView() {
		return null;
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.permission.dao.JobPermissionDao.select";
	}

	@Override
	protected BaseService<JobPermission> getService() {
		return jobPermissionService;
	}
	
	@Override
	protected Map<String, Object> getListDataParam() {
		Map<String, Object> param = super.getListDataParam();
		if(param.get("job")==null){
			param.put("job", "nodata");
		}
		return param;
	}
	
	@RequestMapping(value="saveJobPermission")
	public void saveJobPermission(HttpServletResponse response){
		String pids = getString("permissions");
		String jobid = getString("job");
		if(!StringUtils.isEmpty(jobid) && !StringUtils.isEmpty(pids)){
			String[] pidArr = pids.split(";");
			List<JobPermission> jps = new ArrayList<JobPermission>();
			Job job = new Job();
			job.setId(jobid);
			for(String pid : pidArr){
				PermissionItem pi = new PermissionItem();
				pi.setId(pid);
				JobPermission jp = new JobPermission();
				jp.setJob(job);
				jp.setPermissionItem(pi);
				jps.add(jp);
			}
			jobPermissionService.saveJobPermission(jps, new ArrayList<String>());
		}
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "授权成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	
	@RequestMapping(value="singleJobPermission")
	public String singleJobPermission(@RequestParam(value="job")String job,ModelMap model){
		model.put("job", job);
		return "permission/singleJobPermission";
	}
	
	@RequestMapping(value="simpleTreeData")
	@Dependence(method="list")
	public void simpleTreeData(HttpServletResponse response) {
//		2016-3-22 修改 直接拿出所有的类型
//		List<BusinessTypeEnum> types = Validate.getCurrBuinessType();
		List<BusinessTypeEnum> types = new ArrayList<BusinessTypeEnum>();
		types.add(BusinessTypeEnum.TYHY);
		types.add(BusinessTypeEnum.FDCZJ);
		types.add(BusinessTypeEnum.JRDB);
		types.add(BusinessTypeEnum.JRZJ);
		types.add(BusinessTypeEnum.RJHY);
		List<Module>  moduleList =  queryExecutor.execQuery("com.wuyizhiye.base.module.dao.ModuleDao.select",null,Module.class);
		List<Map<String,Object>> treeData = new ArrayList<Map<String,Object>>();
		Map<String,Object> params;
		String count;
		Map<String,Object> All = new HashMap<String, Object>();
		params = new HashMap<String, Object>();
		params.put("job",getString("job"));
		count=queryExecutor.execOneEntity("com.wuyizhiye.basedata.permission.dao.JobPermissionDao.selectCount_",params ,String.class);
		 All.put("id", "ALL");
		 All.put("name", "全部("+count+")");
		 All.put("pid",null);
		treeData.add(All);
		for(BusinessTypeEnum e : types){
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("id", e);
			params=getpaMap((e.getParent()!=null?"FALSE":"TRUE"),e.toString());
			params.put("job",getString("job"));
			count=queryExecutor.execOneEntity("com.wuyizhiye.basedata.permission.dao.JobPermissionDao.selectCount_",params ,String.class);
			m.put("name", e.getName()+"("+count+")");
			m.put("pid", e.getParent());
			m.put("pid", "ALL");
			treeData.add(m);
			for (int i = 0; i < moduleList.size(); i++) {
				//找出付模块,并且模块必须启动了.才能设置参数
				if(e.equals(moduleList.get(i).getType().getParent()) && moduleList.get(i).isEnable()){
					Map<String,Object> moMap = new HashMap<String, Object>();
					moMap.put("id", moduleList.get(i).getType());
					params = new HashMap<String, Object>();
					params.put("modules","'"+moduleList.get(i).getType()+"'");
					params.put("job",getString("job"));
					count=queryExecutor.execOneEntity("com.wuyizhiye.basedata.permission.dao.JobPermissionDao.selectCount_", params,String.class);
					moMap.put("name",moduleList.get(i).getType().getName()+"("+count+")");
					moMap.put("pid",moduleList.get(i).getType().getParent());
					treeData.add(moMap);
				}
			}
		}
		outPrint(response, JSONArray.fromObject(treeData));
	}
	@RequestMapping(value="simpleTreeData_job")
	@Dependence(method="list")
	public void simpleTreeData_job(HttpServletResponse response) {
		List<Map> jobCategoryList =  queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobCategoryDao.getSimpleTreeData",null,Map.class);
		List<Map<String,Object>> treeData = new ArrayList<Map<String,Object>>();
		Map<String,Object> params;
		Map<String,Object> All = new HashMap<String, Object>();
		params = new HashMap<String, Object>();
		for(Map jc : jobCategoryList){
			Map<String,Object> m = new HashMap<String, Object>();
			params.put("type", jc.get("id"));
			List<Job> jobList =  queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobDao.select",params,Job.class);
			m.put("id", jc.get("id"));
			m.put("name",jc.get("name"));
			m.put("pid", null);
			treeData.add(m);
			for (Job jb:jobList) {
					Map<String,Object> moMap = new HashMap<String, Object>();
					moMap.put("id", jb.getId());
					moMap.put("name",jb.getName());
					moMap.put("pid",jc.get("id"));
					treeData.add(moMap);
			}
		}
		outPrint(response, JSONArray.fromObject(treeData));
	}
	/**
	 * 岗位权限
	 * @param job
	 * @param model
	 * @return
	 */
	@RequestMapping(value="jobPermissionEdit")
	public String jobPermissionEdit(@RequestParam(value="job")String job,ModelMap model){
		model.put("jobId", job);
		return "permission/jobPermissionEdit";
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
	
	protected Map<String,Object> getpaMap(String isModuleType,String moduleTypeString) {
		Map<String, String> param = super.getParamMap();
		param.put("moduleType",moduleTypeString);
		Map<String,Object> params = new HashMap<String, Object>();
		if("FALSE".equals(isModuleType)){
			StringBuilder str = new StringBuilder("");
			str.append("'").append(param.get("moduleType")).append("'");
			params.put("modules", str.toString());
			return params;
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
			params.put("modules", stringBuilder.toString());
		}
		return params ;
	}
	
	@RequestMapping(value="jobPermissionList")
	public String jobPermissionList(ModelMap model){
		return "permission/jobPermissionList";
		
	}
	
	@RequestMapping(value="listPMData")
	public void listPMData(Pagination<?> pagination,HttpServletResponse response){
		//TODO key
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.JobPermissionDao.selectPM", pagination, getListDataParam());
		afterFetchListData(pagination);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
}
