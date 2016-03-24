package com.wuyizhiye.basedata.changyongyu.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.model.ChangYongYu;
import com.wuyizhiye.basedata.basic.service.ChangYongYuService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName ChangYongYuListController
 * @Description 自定义常用语controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/changyongyu/*")
public class ChangYongYuListController extends TreeListController {
	
	@Autowired
	private ChangYongYuService changYongYuService;
	
	@Override
	protected String getTreeDataMapper() {
		return ChangYongYu.MAPPER + ".select";
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return ChangYongYu.MAPPER + ".getSimpleTreeData";
	}

	@Override
	protected CoreEntity createNewEntity() {
		ChangYongYu changyongyu = new ChangYongYu();
		String parent = getString("parentId");
		String objectId = getString("objectId");
		if(!StringUtils.isEmpty(parent)){
			changyongyu.setParent(getService().getEntityById(parent));
		}
		changyongyu.setObjectId(objectId);
		return changyongyu;
	}

	@Override
	protected String getListView() {
		String number = getString("number","OTHER");
		String name = getString("name","其他");
		String objectId = getString("objectId","");
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("number", number);
		ChangYongYu changyongyu = queryExecutor.execOneEntity(ChangYongYu.MAPPER + ".getByNumber", param, ChangYongYu.class);
		if(changyongyu == null || StringUtils.isEmpty(changyongyu.getId())){
			ChangYongYu newModel = new ChangYongYu() ;
			newModel.setUUID() ;
			newModel.setName(name);
			newModel.setNumber(number);
			newModel.setCreateTime(new Date());
			newModel.setCreator(SystemUtil.getCurrentUser());
			changYongYuService.addEntity(newModel);
		}
		getRequest().setAttribute("objectId", objectId);
		return "basedata/changyongyu/changyongyuList";
	}

	@Override
	protected String getEditView() {
		return "basedata/changyongyu/changyongyuEdit";
	}

	@Override
	protected String getListMapper() {
		return ChangYongYu.MAPPER + ".select";
	}

	@Override
	protected BaseService<ChangYongYu> getService() {
		return changYongYuService;
	}
	
	@RequestMapping(value="listData")
	@Dependence(method="list")
	public void listData(Pagination<?> pagination,HttpServletResponse response){
		Map<String,Object> queryMap = getListDataParam() ;
		queryMap.put("personId", SystemUtil.getCurrentUser().getId());
		pagination = queryExecutor.execQuery(getListMapper(), pagination, queryMap);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	/**
	 * 
	 */
	@RequestMapping(value="self")
	public void self(Pagination<ChangYongYu> pagination,HttpServletResponse response){
		Map<String,Object> param = new HashMap<String,Object>();
		int pageIndex = Integer.valueOf(getString("page","1"));
		int pageSize = Integer.valueOf(getString("pageSize","10"));
		pagination.setCurrentPage(pageIndex);
		pagination.setPageSize(pageSize);
		param.put("personId", SystemUtil.getCurrentUser().getId());
		param.put("parentNumber", getString("number","OTHER"));
		pagination = queryExecutor.execQuery(ChangYongYu.MAPPER + ".select", pagination, param);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
}
