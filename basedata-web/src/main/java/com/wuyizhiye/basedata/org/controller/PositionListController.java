package com.wuyizhiye.basedata.org.controller;

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
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PositionListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/position/*")
public class PositionListController extends ListController {
	@Autowired
	private PositionService positionService;
	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		return null;
	}

	@Override
	protected String getEditView() {
		return null;
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.org.dao.PositionDao.select";
	}

	@Override
	protected BaseService<Position> getService() {
		return positionService;
	}
	
	@RequestMapping(value="getByOrg")
	public void getByOrg(HttpServletResponse response){
		List<Position> pos = new ArrayList<Position>();
		String org = getString("org");
		if(!StringUtils.isEmpty(org)){
			pos = positionService.getByOrg(org);
		}
		outPrint(response, JSONArray.fromObject(pos, getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="getJobLevelByPosition")
	public void getJobLevelByPosition(HttpServletResponse response){
		List<JobLevel> jls = new ArrayList<JobLevel>();
		String position = getString("position");
		if(!StringUtils.isEmpty(position)){
			Position pos = positionService.getEntityById(position);
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("job", pos.getJob().getId());
			jls = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobLevelDao.select", param, JobLevel.class);
		}
		outPrint(response, JSONArray.fromObject(jls, getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="dataPicker")
	public String dataPicker(ModelMap model){
		model.put("multi", getString("multi"));
		return "basedata/org/positionDataPicker";
	}
	
	@RequestMapping(value="checkDelete")
	public void checkDelete(@RequestParam(value="id")String id,HttpServletResponse response){
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("oldPositionId", id);
		int count  = queryExecutor.execCount("com.wuyizhiye.basedata.person.dao.PersonPositionHistoryDao.selectByExample", param);
		getOutputMsg().put("count", count);
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
}
