package com.wuyizhiye.hr.salary.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.hr.enums.SchemeObjectTypeEnum;
import com.wuyizhiye.hr.salary.model.SalaryItem;
import com.wuyizhiye.hr.salary.model.SalaryScheme;
import com.wuyizhiye.hr.salary.model.SalarySchemeObject;
import com.wuyizhiye.hr.salary.service.SalarySchemeService;

@Controller
@RequestMapping(value="hr/salaryScheme/*")
public class SalarySchemeListController extends ListController {

	@Autowired
	private SalarySchemeService salarySchemeService;
	@Override
	protected CoreEntity createNewEntity() {
		return new SalaryScheme();
	}

	@Override
	protected String getListView() {
		return "hr/salary/salaryscheme/salarySchemeList";
	}

	@Override
	protected String getEditView() {
		return "hr/salary/salaryscheme/salarySchemeEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.hr.salary.dao.SalarySchemeDao.select";
	}

	@Override
	protected BaseService getService() {
		return salarySchemeService;
	}

	@Override
	public void listData(Pagination<?> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery(getListMapper(), pagination, getListDataParam());
		List<SalaryScheme> salarySchemeList = (List<SalaryScheme>) pagination.getItems();
		Map<String, Object> param = new HashMap<String, Object>();
		for (SalaryScheme salaryScheme : salarySchemeList) {
			param.put("id", salaryScheme.getId());
			String selectSql = SchemeObjectTypeEnum.ORG==salaryScheme.getObjectType() ||SchemeObjectTypeEnum.COMPANY == salaryScheme.getObjectType()?"selectOrgName":"selectJobName";
			List<String> objectName = queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.SalarySchemeObjectDao."+selectSql, param, String.class);
			if(SchemeObjectTypeEnum.COMPANY == salaryScheme.getObjectType()){
				salaryScheme.setOrgName(SchemeObjectTypeEnum.COMPANY.getName());
			}else if(SchemeObjectTypeEnum.ORG == salaryScheme.getObjectType()){
				salaryScheme.setOrgName(getLongName(objectName));
			}else if(SchemeObjectTypeEnum.JOB == salaryScheme.getObjectType()){
				salaryScheme.setPositionName(getLongName(objectName));
			}
		}
		afterFetchListData(pagination);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	private String getLongName(List<String> list){
		String name = "";
		for(String s : list){
			name +=s+";";
		}
		if(list.size()>0){
			if(name.indexOf(";")!=-1){
				name = name.substring(0,name.length()-1);
			}
		}
		return name;
	}
	
	@RequestMapping(value="formulaPicker")
	public String openFormulaPicker(ModelMap model){
		model.put("formula", getString("formula"));
		model.put("id", getString("id"));
		model.put("salaryItems", queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.SalaryItemDao.select", null, SalaryItem.class));
		model.put("itemList", JSONArray.fromObject(getString("arr").replaceAll("\r", "#!#").replaceAll("\n", "~!~")));
		return "hr/salary/salaryscheme/formulaPicker";
	}
	
	@RequestMapping(value="pickerItem")
	public String pickerSalaryItem(){
		return "hr/salary/salaryscheme/pickerSalaryItemList";
	}
	
	@Override
	public String edit(ModelMap model,@RequestParam(required=true,value="id")String id){
		SalaryScheme scheme = (SalaryScheme) getService().getEntityById(id);
		scheme.setId(id);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", scheme.getId());
		String selectSql = SchemeObjectTypeEnum.ORG==scheme.getObjectType() ||SchemeObjectTypeEnum.COMPANY == scheme.getObjectType()?"selectOrgName":"selectJobName";
		List<String> objectName = queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.SalarySchemeObjectDao."+selectSql, param, String.class);
		if(SchemeObjectTypeEnum.COMPANY == scheme.getObjectType()){
			scheme.setOrgName(SchemeObjectTypeEnum.COMPANY.getName());
			model.put("objectName", scheme.getOrgName());
		}else if(SchemeObjectTypeEnum.ORG == scheme.getObjectType()){
			scheme.setOrgName(getLongName(objectName));
			model.put("objectName", scheme.getOrgName());
		}else if(SchemeObjectTypeEnum.JOB == scheme.getObjectType()){
			scheme.setPositionName(getLongName(objectName));
			model.put("objectName", scheme.getPositionName());
			
		}
		param.clear();
		param.put("schemeId", scheme.getId());
		List<SalarySchemeObject> objectList = queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.SalarySchemeObjectDao.select", param, SalarySchemeObject.class);
		String objId = "";
		for(SalarySchemeObject obj : objectList){
			objId += obj.getObjId()+",";
		}
		if(!StringUtils.isEmpty(objId)){
			if(objId.indexOf(",")!=-1){
				objId = objId.substring(0,objId.length()-1);
			}
			model.put("objectId", objId);
		}
		model.put("data", scheme);
		model.put("edit_viewStatus", this.getString("VIEWSTATE"));
		return getEditView();
	}
	
	@RequestMapping(value="updateState")
	@ResponseBody
	public void updateState(HttpServletResponse response){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", getString("id"));
		param.put("state", getInt("state"));
		queryExecutor.executeUpdate("com.wuyizhiye.hr.salary.dao.SalarySchemeDao.updateState", param);
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@RequestMapping(value="jobDataPicker")
	public String jobDataPicker(ModelMap model){
		model.put("multi", getString("multi"));
		model.put("schemeId", getString("schemeId"));
		return "hr/salary/salaryscheme/jobDataPicker";
	}
	
	
	@RequestMapping(value="listJobData")
	public void listJobData(Pagination<?> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobDao.selectJobBySalaryCond", pagination, getListDataParam());
		afterFetchListData(pagination);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
}
