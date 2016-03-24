package com.wuyizhiye.framework.base.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.TreeEntity;


/**
 * @ClassName TreeListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
public abstract class TreeListController extends ListController {
	
	/**
	 * 延迟加载方式获取树数据
	 * @param response
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="treeData")
	public void treeData(HttpServletResponse response){
		String parent = getString("parent");
		String includeChild = getString("includeChild");
		Map<String,Object> param = getTreeFilterParam();
		CoreEntity pare = null;
		if(!StringUtils.isEmpty(parent)){
			pare = getTreeService().getEntityById(parent);
		}
		if(pare!=null){
			if(!StringUtils.isEmpty(includeChild) && Boolean.getBoolean(includeChild)==true){
				param.put("includeChild", includeChild);
				param.put("longNumber", ((TreeEntity)pare).getLongNumber());
			}else{
				param.put("parent", pare.getId());
			}
		}
		Pagination<Object> page = new Pagination<Object>(Integer.MAX_VALUE,0);
		page = queryExecutor.execQuery(getTreeDataMapper(), page, param);
		String result = JSONArray.fromObject(page.getItems(), getDefaultJsonConfig()).toString();
		outPrint(response, result);
	}
	
	/**
	 * 树形数据
	 * @return
	 */
	protected abstract String getTreeDataMapper();
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="simpleTreeData")
	public void simpleTreeData(HttpServletResponse response){
		List<Map> treeData = queryExecutor.execQuery(getSimpleTreeDataMapper(), getSimpleTreeDataFilter(), Map.class);
		afterFetchTreeListData(treeData);
		outPrint(response, JSONArray.fromObject(treeData, getDefaultJsonConfig()).toString());
	}
	
	/**
	 * 获取数 形数据后
	 * @param pagination
	 */
	@SuppressWarnings("rawtypes")
	protected void afterFetchTreeListData(List<Map> treeData){
		
	}
	
	protected Map<String, Object> getSimpleTreeDataFilter() {
		return getParaMap();
	}

	/**
	 * 获取
	 * @return
	 */
	protected abstract String getSimpleTreeDataMapper();

	protected Map<String, Object> getTreeFilterParam() {
		return new HashMap<String, Object>();
	}
	
	/**
	 * 取树数据的service，默认为通用Service
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected BaseService getTreeService(){
		return getService();
	}
}
