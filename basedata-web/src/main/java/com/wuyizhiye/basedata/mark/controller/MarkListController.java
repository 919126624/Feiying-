package com.wuyizhiye.basedata.mark.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.model.Mark;
import com.wuyizhiye.basedata.basic.service.MarkService;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName MarkListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping("basedata/mark/*")
public class MarkListController extends TreeListController{

	@Autowired
	private MarkService markBasicService ;
	
	@Override
	protected String getTreeDataMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return "com.wuyizhiye.basedata.basic.dao.MarkDao.getSimpleTreeData";
	}

	@Override
	protected CoreEntity createNewEntity() {
		return new Mark();
	}

	@Override
	protected String getListView() {
		return "basedata/mark/markList";
	}

	@Override
	protected String getEditView() {
		return "basedata/mark/markEidt";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.basic.dao.MarkDao.select";
	}

	@Override
	protected BaseService getService() {
		return markBasicService;
	}
	@Override
	public void simpleTreeData(HttpServletResponse response) {
		Mark isSys=new Mark();
		Mark noSys=new Mark();
		isSys.setId("ISSYS");//系统标签ID
		isSys.setName("系统级");//系统级名称
		
		noSys.setId("NOSYS");//非系统标签ID
		noSys.setName("用户级");
		
		List<Mark> treeData = queryExecutor.execQuery(getSimpleTreeDataMapper(), getSimpleTreeDataFilter(), Mark.class);
		//遍历所有的节点
		for(int i=0;i<treeData.size();i++){
			Mark mark=treeData.get(i);
			if(!StringUtils.isNotNull(mark.getParent())){
				mark.setPid(isSys.getId());
			}
		}
		treeData.add(noSys);
		treeData.add(isSys);
		outPrint(response, JSONArray.fromObject(treeData, getDefaultJsonConfig()).toString());
	}
	
	@Override
	public void delete(@RequestParam(required=true,value="id")String id,HttpServletResponse response) {
		CoreEntity entity = getService().getEntityById(id);
		if(entity!=null){
			if(isAllowDelete(entity)){
				getService().deleteEntity(entity);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "删除成功");
			}else{
				getOutputMsg().put("MSG", "该记录下存在子记录，不允许删除！");
				getOutputMsg().put("STATE", "FAIl");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	@Override
	protected boolean isAllowDelete(CoreEntity entity) {
		//判断是否存在子节点
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("pid", entity.getId());
		int count=queryExecutor.execCount(Mark.MAPPER+".select", param);
		if(count>0){
			return false;
		}
		return true;
	}
	@RequestMapping("isReName")
	public void isReName(String id,String newName,HttpServletResponse response){
		//判断数据库中是否存在该名字
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("currentRcoderId", id);
		param.put("existName", newName);
		int count=queryExecutor.execCount(Mark.MAPPER+".isExistName", param);
		if(count>0){
			getOutputMsg().put("STATE", "FAIl");
			getOutputMsg().put("MSG", "标签名称已在数据库中存在！");
		}else{
			getOutputMsg().put("STATE", "SUCCESS");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
