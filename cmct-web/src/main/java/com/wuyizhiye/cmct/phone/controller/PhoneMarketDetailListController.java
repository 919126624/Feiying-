package com.wuyizhiye.cmct.phone.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.cmct.phone.model.PhoneMarket;
import com.wuyizhiye.cmct.phone.model.PhoneMarketDetail;
import com.wuyizhiye.cmct.phone.service.PhoneMarketDetailService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneMarketDetailListController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneMarketDetail/*")
public class PhoneMarketDetailListController extends ListController {

	@Autowired
	private PhoneMarketDetailService phoneMarketDetailService;
	@Override
	protected CoreEntity createNewEntity() {
		return new PhoneMarketDetail();
	}

	@Override
	protected String getListView() {
		getRequest().setAttribute("workID", getString("workID"));
		return "cmct/phone/phoneMarketDetailList" ;
	}

	@RequestMapping(value="analysis")
	public String analysis(){
		return "cmct/phone/phoneMarketAnalysis" ;
	}
	
	@Override
	protected String getEditView() {
		return null;
	}

	@Override
	protected String getListMapper() {
		return PhoneMarketDetail.MAPPER+".select";
	}

	@Override
	protected BaseService getService() {
		return phoneMarketDetailService;
	}

	/**
	 * json转换config
	 * @return
	 */
	@Override
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
		return config;
	}
	
	@Override
	protected Map<String, Object> getListDataParam() {
		DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
		Map<String,Object> param=super.getListDataParam();
		try{
			if(null!=param.get("startDate")){
				param.put("startDate", df.parse(param.get("startDate").toString()));
			}
			if(null!=param.get("endDate")){
				param.put("endDate", DateUtil.getNextDay(df.parse(param.get("endDate").toString())));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return param;
	}
	
	@RequestMapping(value="getAnalysisData")
	public void getAnalysisData(Pagination<?> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery(PhoneMarket.MAPPER+".getAnalysisData", pagination, getListDataParam());
		outPrint(response, JSONObject.fromObject(pagination, getAnalysisDataJsonConfig()));
	}
	
	protected JsonConfig getAnalysisDataJsonConfig(){
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
		return config;
	}
}
