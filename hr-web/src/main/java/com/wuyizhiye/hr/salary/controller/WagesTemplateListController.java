package com.wuyizhiye.hr.salary.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.module.enums.BusinessTypeEnum;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.util.GenerateKey;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.hr.salary.model.SalaryItem;
import com.wuyizhiye.hr.salary.model.WagesItem;
import com.wuyizhiye.hr.salary.model.WagesTemplate;
import com.wuyizhiye.hr.salary.service.WagesTemplateService;

/**
 * 工资条模板controller
 * @author hyl
 *
 * @since 2014-02-12
 */
@Controller
@RequestMapping(value="hr/wagestemplate/*")
public class WagesTemplateListController extends ListController{
	@Autowired
	private WagesTemplateService wagesTemplateService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		WagesTemplate data = new WagesTemplate();
		try {
			data.setWageNumber(GenerateKey.getKeyCode(null, "GZT"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return "hr/salary/salarysetting/wagesTemplateList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return "hr/salary/salarysetting/wagesTemplateEdit";
	}	
	/**
	 * json转换config
	 * @return
	 */
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
		//日期
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
		});
		//是否为默认值
		config.registerJsonValueProcessor(CommonFlagEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof CommonFlagEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((CommonFlagEnum)value).getLabel());
					json.put("value", ((CommonFlagEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof CommonFlagEnum){
					return ((CommonFlagEnum)value).getLabel();
				}
				return null;
			}
		});
		return config;
	}
	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.hr.salary.dao.WagesTemplateDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return wagesTemplateService;
	}
	/**
	 * 列表数据  岗位权限
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="listData4NoSelect")
	public void listData4NoSelect(HttpServletResponse response){
		Map<String, Object> param = new HashMap<String, Object>();
		String id=getString("wagesTemplateId");
		if(!StringUtils.isEmpty(id)){
		param.put("wagesTemplateId", id);
		List<WagesItem> alist=queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.WagesItemDao.select", param, WagesItem.class);
		StringBuilder stringBuilder = new StringBuilder("");
		for(WagesItem wi : alist){
					stringBuilder.append("'").append(wi.getSalaryItem().getId()).append("',");
		}
		if(stringBuilder.length() > 1){
			stringBuilder = stringBuilder.deleteCharAt(stringBuilder.length()-1);
		}else{
			stringBuilder.append("'nodata'");
		}
		param.put("sids", stringBuilder.toString());}
		param.put("status", "ENABLE");
		List<SalaryItem> salaryItemList = queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.SalaryItemDao.select4NoSelect", param, SalaryItem.class);
		outPrint(response, JSONArray.fromObject(salaryItemList,getDefaultJsonConfig()));
	}
	/**
	 * 列表数据  岗位权限
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="listData4Select")
	public void listData4Select(HttpServletResponse response){
		String id=getString("wagesTemplateId");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status", "ENABLE");
		param.put("wagesTemplateId", id);
		List<WagesItem> alist=queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.WagesItemDao.select", param, WagesItem.class);
		outPrint(response, JSONArray.fromObject(alist,getDefaultJsonConfig()));
	}	
	/**
	 *  设置默认模板
	 * @param 
	 * @param response
	 */
	@RequestMapping(value="setDfult")
	public void setDefult(HttpServletResponse response){
		WagesTemplate data;
		data = wagesTemplateService.getEntityById(getString("id"));
		Map<String, Object> param = new HashMap<String, Object>();
		if(getString("isDefult").equals("YES")){
		param.put("isDefult", "YES");
		List<WagesTemplate> alist=queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.WagesTemplateDao.select", param, WagesTemplate.class);
		if(alist.size()>0){
		getOutputMsg().put("STATE", "FAIL");
		getOutputMsg().put("MSG", "已有设置默认模板！");
		}else{
		data.setIsDefult(CommonFlagEnum.YES);
		wagesTemplateService.updateEntity(data);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");}}else{
		data.setIsDefult(CommonFlagEnum.NO);
		wagesTemplateService.updateEntity(data);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
}
