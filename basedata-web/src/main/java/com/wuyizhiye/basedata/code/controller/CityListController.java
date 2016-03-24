package com.wuyizhiye.basedata.code.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.TreeEntity;
import com.wuyizhiye.basedata.bank.model.City;
import com.wuyizhiye.basedata.bank.service.CityService;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName CityListController
 * @Description 省份城市ListController
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/cityList/*")
public class CityListController extends TreeListController {
	@Autowired
	private CityService cityService;

	@Override
	protected String getTreeDataMapper() {
		return "com.wuyizhiye.basedata.bank.dao.CityDao.getChild";
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return "com.wuyizhiye.basedata.bank.dao.CityDao.getSimpleTreeData";
	}
	
	@RequestMapping(value="getNode") 
	public void getNode(ModelMap model,HttpServletResponse response){  
		String result = this.getChildsNode();
		getOutputMsg().put("treeData",result);
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	
	public String getChildsNode(){
		StringBuffer buf = new StringBuffer("[");
		List<City> cityList = queryExecutor.execQuery("com.wuyizhiye.basedata.bank.dao.CityDao.getChild", null, City.class);
		for(int i=0;i<cityList.size();i++){
			if(i!=0){
				buf.append(",");
			}
			City cityTemp = cityList.get(i);
			buf.append("{id:'")
				.append(cityTemp.getId())
				.append("',open:'true")
				.append("',name:'")
				.append(cityTemp.getName())
				.append("'");
			buf.append("}");
		}
		buf.append("]"); 
		return buf.toString();
	}

	@Override
	protected CoreEntity createNewEntity() {
		String parentId = getString("parent");
		City city = new City();
		if(!StringUtils.isEmpty(parentId)){
			City parent = cityService.getEntityById(parentId);
			city.setParent(parent);
		}
		return city;
	}

	@Override
	protected String getListView() {
		return "basedata/code/cityList";
	}

	@Override
	protected String getEditView() {
		return "basedata/code/cityEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.bank.dao.CityDao.select";
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		return cityService;
	}
	
	/**
	 * ${base}/basedata/code/cityDataPicker?multi=true为多选
	 * @param model
	 * @return
	 */
	@RequestMapping(value="cityDataPicker")
	public String cityDataPicker(ModelMap model){
		Map<String,String> param = getParamMap();
		Set<String> keys = param.keySet();
		for(String s : keys){
			model.put(s, param.get(s));
		}
		return "basedata/code/cityDataPicker";
	}
	
	@RequestMapping(value="add")
	@Dependence(method="list")
	public String add(ModelMap model){
		String id = getString("id");
		String parentId = getString("parentId");
		City city = null;
		if(StringUtils.isEmpty(id)){
			if(StringUtils.isEmpty(parentId)){
				model.put("data", createNewEntity());
			}else{
				
				city = (City)createNewEntity();
				City parent = cityService.getEntityById(parentId);
				parent.setId(parentId);
				city.setParent(parent);
				model.put("data", city);
			}
		}else{
			city = cityService.getEntityById(id);
			model.put("data", city);
		}
		return getEditView();
	}
	
	/**
	 * 延迟加载方式获取树数据
	 * @param response
	 */
	@RequestMapping(value="cityDataTree")
	public void treeData(HttpServletResponse response){
		String root = getString("root");
		String parent = getString("parent");
		String includeChild = getString("includeChild");
		String bankId = getString("bankId","");
		Map<String,Object> param = getTreeFilterParam();
		CoreEntity pare = null;
		
		if(!StringUtils.isEmpty(bankId)){
			param.put("bankId", bankId);
		}
		if(!StringUtils.isEmpty(parent)){
			pare = getTreeService().getEntityById(parent);
			if(pare!=null){
				if(!StringUtils.isEmpty(includeChild) && Boolean.getBoolean(includeChild)==true){
					param.put("includeChild", includeChild);
					param.put("longNumber", ((TreeEntity)pare).getLongNumber());
				}else{
					param.put("parent", pare.getId());
				}
			}
			List<City> Citys = queryExecutor.execQuery(getTreeDataMapper(), param,City.class);
			String result = JSONArray.fromObject(Citys, getDefaultJsonConfig()).toString();
			outPrint(response, result);
		}else{
			if(!StringUtils.isEmpty(root)){
				List<City> citys = new ArrayList<City>();
				City city = cityService.getEntityById(root);
				citys.add(city);
				outPrint(response, JSONArray.fromObject(citys, getDefaultJsonConfig()).toString());
			}else{
				List<City> citys = queryExecutor.execQuery(getTreeDataMapper(), param,City.class);
				String result = JSONArray.fromObject(citys, getDefaultJsonConfig()).toString();
				outPrint(response, result);
			}
		}
		
	}
	
	/**
	 * dataPicker取数据
	 */
	@RequestMapping(value="cityData")
	public void cityData(Pagination<?> pagination,HttpServletResponse response){
		Map<String,Object> param = getListDataParam();
		if(param.get("parent")==null && param.get("longNumber")==null){
			param.put("parent", "nodata");
		}
		pagination = queryExecutor.execQuery(getListMapper(), pagination, param);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	
	/**
	 * dataPicker取数据
	 */
	@RequestMapping(value="getCityByCon")
	public void getCityByCon(HttpServletResponse response){
		Map<String,Object> param = getListDataParam();
		List<City> cityList = queryExecutor.execQuery(getListMapper(), param, City.class);
//		String result = getJsonStr(cityList);
		getOutputMsg().put("results",cityList);
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		
	}
	
	
	public String getJsonStr(List<City> list){
		StringBuffer jsonStr = new StringBuffer("[");
		for (City item : list) {
			if(!"[".equals(jsonStr.toString())){
				jsonStr.append(",");
			}
			jsonStr.append("{id:'")
					.append(item.getId())
					.append("',name:'")
					.append(item.getName())
					.append("'}");
		}
		jsonStr.append("]");
		return jsonStr.toString();
	}
	

}
