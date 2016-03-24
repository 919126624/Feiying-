package com.wuyizhiye.basedata.org.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.org.model.JobCategory;
import com.wuyizhiye.basedata.org.service.JobCategoryService;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName JobCategoryListController
 * @Description 岗位大类 controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/jobcategory/*")
public class JobCategoryListController extends TreeListController {
	
	@Autowired
	private JobCategoryService jobCategoryService;
	@Override
	protected String getTreeDataMapper() {
		return "com.wuyizhiye.basedata.org.dao.JobCategoryDao.select";
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return "com.wuyizhiye.basedata.org.dao.JobCategoryDao.getSimpleTreeData";
	}

	@Override
	protected CoreEntity createNewEntity() {
		JobCategory type = new JobCategory();
		return type;
	}

	@Override
	protected String getListView() {
		return "basedata/org/jobList";
	}

	@Override
	protected String getEditView() {
		return "basedata/org/jobCategoryEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.org.dao.JobCategoryDao.select";
	}

	@Override
	protected BaseService<JobCategory> getService() {
		return jobCategoryService;
	}
	@Override
	protected boolean isAllowDelete(CoreEntity entity) {
		JobCategory jc=(JobCategory)entity;		
		if(jc.getIsSystem().equals(CommonFlagEnum.YES)){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "已设为系统预置项，请勿删除！");
			return false;}else{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("type", jc.getId());
			if(queryExecutor.execCount("com.wuyizhiye.basedata.org.dao.JobDao.select", param) > 0){
				this.getOutputMsg().put("MSG", "该项正在使用中，不能删除!");
				return false;}}
		return true;
	}
	@Override
	public void simpleTreeData(HttpServletResponse response){
		List<Map> treeData = queryExecutor.execQuery(getSimpleTreeDataMapper(), getSimpleTreeDataFilter(), Map.class);
		for(int i=0;i<treeData.size();i++){
			Map m = treeData.get(i);
			m.put("pid", "FFFF");
		}
		Map treetmp = new HashMap();
		treetmp.put("id","FFFF");
		treetmp.put("name", "全部");
		treetmp.put("number", "FFFF");
		treetmp.put("isSystem", "YES");
		treeData.add(treetmp);
		outPrint(response, JSONArray.fromObject(treeData, getDefaultJsonConfig()).toString());
	}
}