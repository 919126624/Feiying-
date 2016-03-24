package com.wuyizhiye.hr.salary.controller;

import java.text.SimpleDateFormat;
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
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.GenerateKey;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.hr.enums.SalaryItemTypeEnum;
import com.wuyizhiye.hr.salary.model.Duration;
import com.wuyizhiye.hr.salary.model.Rate;
import com.wuyizhiye.hr.salary.model.SalaryItem;
import com.wuyizhiye.hr.salary.service.RateService;
import com.wuyizhiye.hr.salary.service.SalaryItemService;

/**
 * 薪酬项目controller
 * @author hyl
 *
 * @since 2014-02-12
 */
@Controller
@RequestMapping(value="hr/salaryItem/*")
public class SalaryItemListController extends ListController{
	@Autowired
	private SalaryItemService salaryItemService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		SalaryItem data=new SalaryItem();
		try {
			data.setNumber(GenerateKey.getKeyCode(null, "SALARYITEM"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return "hr/salary/salarysetting/salaryItemList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		this.getRequest().setAttribute("SalaryItemType", SalaryItemTypeEnum.values());//方式
		return "hr/salary/salarysetting/salaryItemEdit";
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
		//方式
		config.registerJsonValueProcessor(SalaryItemTypeEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SalaryItemTypeEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((SalaryItemTypeEnum)value).getLabel());
					json.put("value", ((SalaryItemTypeEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SalaryItemTypeEnum){
					return ((SalaryItemTypeEnum)value).getLabel();
				}
				return null;
			}
		});
		return config;
	}
	/**
	 * 启用/禁用
	 * @param org 
	 * @param response
	 */
	@RequestMapping(value="enable")
	public void enable(SalaryItem salaryItem,HttpServletResponse response){
		SalaryItem  old = salaryItemService.getEntityById(salaryItem.getId());
		old.setStatus(salaryItem.getStatus());
		salaryItemService.updateEntity(old);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.hr.salary.dao.SalaryItemDao.select";
	}
	@RequestMapping(value="getItemBycolumns")
	public void getItemBycolumns(HttpServletResponse response){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status", "ENABLE");
		List<SalaryItem> salaryItemList = queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.SalaryItemDao.select", param, SalaryItem.class);
		outPrint(response, JSONArray.fromObject(salaryItemList,getDefaultJsonConfig()));
	}
	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return salaryItemService;
	}
	/**
	 * 列表数据  薪酬项目
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="listData4NoSelecte")
	@Dependence(method="list")
	public void listData4NoJobPms(Pagination<?> pagination,HttpServletResponse response){
			String mapper = "com.wuyizhiye.hr.salary.dao.SalaryItemDao.select";
			pagination = queryExecutor.execQuery(mapper, pagination, getListDataParam());
			afterFetchListData(pagination);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	/**
	 * 列表数据    薪酬项目
	 * @param pagination
	 * @param response
	 *//*
	@RequestMapping(value="listData4JobPms")
	@Dependence(method="list")
	public void listData4JobPms(Pagination<?> pagination,HttpServletResponse response){
		if(StringUtils.isNotNull(getListDataParam().get("moduleType"))){
			String mapper = "com.wuyizhiye.basedata.permission.dao.JobPermissionDao.select";
			pagination = queryExecutor.execQuery(mapper, pagination, getListDataParam());
			afterFetchListData(pagination);
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}*/
}
