package com.wuyizhiye.framework.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;

/**
 * @ClassName ListController
 * @Description 列表界面Controller
 * @author li.biao
 * @date 2015-4-7
 */
public abstract class ListController extends BaseController {
	
	/**
	 * 列表数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="listData")
	@Dependence(method="list")
	public void listData(Pagination<?> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery(getListMapper(), pagination, getListDataParam());
		afterFetchListData(pagination);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	/**
	 * 获取列表数据后
	 * @param pagination
	 */
	protected void afterFetchListData(Pagination<?> pagination){
	}
	
	@RequestMapping(value="listAllData")
	@Dependence(method="list")
	public void listAllData(HttpServletResponse response){
		List<Object> datalist = queryExecutor.execQuery(getListMapper(), getListDataParam(),Object.class);
		outPrint(response, JSONArray.fromObject(datalist, getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="list")
	public String list(){
		return getListView();
	}
	
	@RequestMapping(value="add")
	@Dependence(method="list")
	public String add(ModelMap model){
		model.put("data", createNewEntity());
		model.put("edit_viewStatus", this.getString("VIEWSTATE"));
		return getEditView();
	}
	@RequestMapping(value="edit")
	@Dependence(method="list")
	public String edit(ModelMap model,@RequestParam(required=true,value="id")String id){
		model.put("data", getService().getEntityById(id));
		//将当前状态放入到界面 addded by taking.wang 2013-01-24
		model.put("edit_viewStatus", this.getString("VIEWSTATE"));
		return getEditView();
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value="delete")
	@Dependence(method="list")
	public void delete(@RequestParam(required=true,value="id")String id,HttpServletResponse response){
		CoreEntity entity = getService().getEntityById(id);
		if(entity!=null){
			if(isAllowDelete(entity)){
				getService().deleteEntity(entity);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "删除成功");
			}else{
				getOutputMsg().put("STATE", "FAIl");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	protected boolean isAllowDelete(CoreEntity entity) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="deleteBatch")
	@Dependence(method="list")
	public void deleteBatch(HttpServletResponse response){
		String ids = getString("ids");
		if(!StringUtils.isEmpty(ids)){
			String[] idAtt = ids.split(";");
			List<String> idList = new ArrayList<String>();
			for(String id : idAtt){
				idList.add(id);
			}
			getService().deleteBatch(idList);
		}
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "删除成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	
	@RequestMapping(value="getOneDataById")
	@Dependence(method="list")
	public void getOneDataById(HttpServletResponse response,@RequestParam(required=true,value="id")String id){
		
		Object obj = getService().getEntityById(id);
		//将当前状态放入到界面 addded by taking.wang 2013-01-24
		outPrint(response, JSONObject.fromObject(obj).toString());
	}
	
	protected abstract CoreEntity createNewEntity();

	/**
	 * 获取列表view
	 * @return
	 */
	protected abstract String getListView();
	
	/**
	 * 获取编辑界面view
	 * @return
	 */
	protected abstract String getEditView();
	
	/**
	 * mybatis查询列表数据的mapper
	 * @return
	 */
	protected abstract String getListMapper();
	
	/**
	 * 查询参数
	 * @return
	 */
	protected Map<String,Object> getListDataParam(){
		
		Map<String,String> param = getParamMap();
		Map<String,Object> params = new HashMap<String, Object>();
		Set<String> keys = param.keySet();
		for(String key : keys){
			params.put(key, param.get(key));
		}
		//加入控制单元id
		this.putControlUnitIdToMap(params);
		return params;
	}
	
	/**
	 * 获取Service
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected abstract BaseService getService();
	
	@RequestMapping(value="listbuilding")
	public String listbuilding(){
		return "common/building";
	}
}
