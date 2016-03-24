package com.wuyizhiye.basedata.portlet.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.portlet.model.QuickJobItem;
import com.wuyizhiye.basedata.portlet.model.QuickSet;
import com.wuyizhiye.basedata.portlet.service.QuickJobItemService;
import com.wuyizhiye.basedata.portlet.service.QuickSetService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName QuickSetListController
 * @Description 快捷设置 
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping("interflow/quickSet/*")
public class QuickSetListController extends ListController{
	
	@Autowired
	private QuickSetService quickSetService;
	
	@Autowired
	private QuickJobItemService quickJobItemService;

	@Override
	protected CoreEntity createNewEntity() {
		
		return new QuickSet();
	}

	@Override
	protected String getListView() {
		
		return "interflow/portlet/quickSetList";
	}
	
	@RequestMapping("selectMenu")
	public String selectMenu(ModelMap model){
		return "interflow/portlet/quickMenuList";
	}
	
	@RequestMapping("setJobPage")
	public String setJobPage(ModelMap model){
	    String id=getString("id");
	    QuickSet qs=this.quickSetService.getEntityById(id);
	    model.put("data", qs);
		return "interflow/portlet/selectQuickJob";
	}
	
	@RequestMapping("selectJob")
	public String selectJob(ModelMap model){
		return "interflow/portlet/quickJobList";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("saveJobs")
	@ResponseBody
	public void saveJobs(HttpServletResponse response){
		String dataId=getString("dataId");//快捷设置ID
		QuickSet qs=this.quickSetService.getEntityById(dataId);
		qs.setId(dataId);
		String jobJson=getString("jobJson");
		/**
		 * 先删除以前的岗位信息
		 */
		quickJobItemService.deleteById(dataId);
		String jobName="";
		if(!StringUtils.isEmpty(jobJson)){
			JSONArray array=JSONArray.fromObject(jobJson);
			List<QuickJobItem> qlist=(List<QuickJobItem>) JSONArray.toCollection(array, QuickJobItem.class);
			/**
			 * 验证  一个岗位不能同时存在两个快捷设置中
			 */
			List<QuickJobItem> olist=queryExecutor.execQuery("com.wuyizhiye.basedata.portlet.dao.QuickJobItemDao.select", null, QuickJobItem.class);
			for(QuickJobItem qj:qlist){
				boolean flag=true;
				for(QuickJobItem q:olist){
					if(qj.getJob().getId().equals(q.getJob().getId())){
						flag=false;
					}
				}
				if(flag){
					qj.setId(UUID.randomUUID().toString());
					qj.setQuickSet(qs);
					quickJobItemService.addEntity(qj);
					jobName+=qj.getJob().getName()+",";
				}
			}
			if(jobName.contains(",")){
				jobName=jobName.substring(0, jobName.length()-1);
			}
		}
		qs.setJobName(jobName);
		queryExecutor.executeUpdate("com.wuyizhiye.basedata.portlet.dao.QuickSetDao.update", qs);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");
		outPrint(response,JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	@Override
	protected String getEditView() {
		
		return "interflow/portlet/quickSetEdit";
	}

	@Override
	protected String getListMapper() {
		
		return "com.wuyizhiye.basedata.portlet.dao.QuickSetDao.select";
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		
		return quickSetService;
	}

}
