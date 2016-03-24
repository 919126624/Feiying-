package com.wuyizhiye.cmct.phone.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.cmct.phone.model.PhoneDialDetail;
import com.wuyizhiye.cmct.phone.model.PhoneSyncHistory;
import com.wuyizhiye.cmct.phone.service.PhoneDialDetailService;
import com.wuyizhiye.cmct.phone.util.PhoneMemberUtil;
import com.wuyizhiye.cmct.phone.util.ProjectMApiRemoteServer;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.framework.util.Constant;

/**
 * @ClassName PhoneDialDetailListController
 * @Description 话费管理-话单明细
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneDialDetail/*")
public class PhoneDialDetailListController extends ListController {

	private static Log log = LogFactory.getLog(PhoneDialDetailListController.class);
	
	@Autowired
	private PhoneDialDetailService phoneDialDetailService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return new PhoneDialDetail();
	}
	
	@Override
	public String list() {
		String infoNumber = getString("infoNumber","");
		if(!StringUtils.isEmpty(infoNumber)){
			String period = getString("period","");
			String comboType = getString("comboType","");
			String partners = getString("partners");
			Date startDate = DateUtil.convertStrToDate(period + "-01");
			Date endDate = DateUtil.getLastDay(startDate);
			this.getRequest().setAttribute("startDate", DateUtil.convertDateToStr(startDate).replace("-", "/"));
			this.getRequest().setAttribute("endDate", DateUtil.convertDateToStr(endDate).replace("-", "/"));
			this.getRequest().setAttribute("infoNumber", "HIDE".equals(infoNumber) ? "隐藏呼出" : infoNumber);
			this.getRequest().setAttribute("period", period);
			this.getRequest().setAttribute("comboType", comboType);
			this.getRequest().setAttribute("partners", partners);
		}else{
			//默认查询本月
			Date currDate = DateUtil.getCurDate() ;
			Date firstDate = DateUtil.getFirstDay(currDate);
			Date lastDate = DateUtil.getLastDay(currDate);
			this.getRequest().setAttribute("startDate", DateUtil.convertDateToStr(firstDate).replace("-", "/"));
			this.getRequest().setAttribute("endDate", DateUtil.convertDateToStr(lastDate).replace("-", "/"));
		}
		return getListView();
	}

	@Override
	protected String getListView() {
		this.getRequest().setAttribute("currDay", DateUtil.getCurDate());
		this.getRequest().setAttribute("otherNumber",ParamUtils.getParamValue(PhoneMemberUtil.CMCT_DIALDETAIL));//是否显示对方号码
		return "cmct/phone/phoneDialDetailList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	protected String getListMapper() {
		return PhoneDialDetail.MAPPER+".select";
	}
	
	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return phoneDialDetailService;
	}

	@Override
	protected Map<String, Object> getListDataParam() {
		Map<String,String> param = getParamMap();
		Map<String,Object> params = new HashMap<String, Object>();
		Set<String> keys = param.keySet();
		for(String key : keys){
			params.put(key, param.get(key));
		}
		//判断是否隐藏呼出
		String infoNumber = getString("infoNumber");
		if("HIDE".equals(infoNumber)){
			params.put("infoNumber", null);
			params.put("hidePhone", "HIDE");
		}
		
		//数据源判断
		String dataBaseType = ParamUtils.getParamValue(Constant.DATABASETYPE);
		if(StringUtils.isEmpty(dataBaseType)){//默认为MYSQL
			dataBaseType = Constant.DATABASETYPE_MYSQL ;
		}else{
			dataBaseType = dataBaseType.toUpperCase() ;//转成大写
		}
		params.put("queryStartDate", getString("queryStartDate","").replace("/", "-"));
		String endDate  = getString("queryEndDate","").replace("/", "-") ;
		if(!StringUtils.isEmpty(endDate)){
			Date endD = DateUtil.getNextDay(DateUtil.convertStrToDate(endDate));
			params.put("queryEndDate", DateUtil.convertDateToStr(endD));
		}
		if(Constant.DATABASETYPE_MYSQL.equals(dataBaseType)){
			params.put("startDateMySql", params.get("queryStartDate"));
			params.put("endDateMySql", params.get("queryEndDate"));
			params.put("queryStartDate", null);
			params.put("queryEndDate", null);
		}else if(Constant.DATABASETYPE_ORACLE.equals(dataBaseType)){
			params.put("startDateOracle", params.get("queryStartDate"));
			params.put("endDateOracle", params.get("queryEndDate"));
			params.put("queryStartDate", null);
			params.put("queryEndDate", null);
		}
		return params;
	}
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
	@RequestMapping(value="phoneDialdetailQuery")
	public String queryDtailList(HttpServletResponse response){
		String infoNumber = getString("infoNumber","");
		if(!StringUtils.isEmpty(infoNumber)){
			String period = getString("period","");
			String comboType = getString("comboType","");
			String partners = getString("partners");
			Date startDate = DateUtil.convertStrToDate(period + "-01");
			Date endDate = DateUtil.getLastDay(startDate);
			this.getRequest().setAttribute("startDate", DateUtil.convertDateToStr(startDate).replace("-", "/"));
			this.getRequest().setAttribute("endDate", DateUtil.convertDateToStr(endDate).replace("-", "/"));
			this.getRequest().setAttribute("infoNumber", "HIDE".equals(infoNumber) ? "隐藏呼出" : infoNumber);
			this.getRequest().setAttribute("period", period);
			this.getRequest().setAttribute("comboType", comboType);
			this.getRequest().setAttribute("partners", partners);
		}else{
			//默认查询本月
			Date currDate = DateUtil.getCurDate() ;
			Date firstDate = DateUtil.getFirstDay(currDate);
			Date lastDate = DateUtil.getLastDay(currDate);
			this.getRequest().setAttribute("startDate", DateUtil.convertDateToStr(firstDate).replace("-", "/"));
			this.getRequest().setAttribute("endDate", DateUtil.convertDateToStr(lastDate).replace("-", "/"));
		}
		
		return "cmct/phone/phoneDialdetailQuery";
		
	}
	@RequestMapping(value="queryDtail")
	public void queryDtail(HttpServletResponse response){
		Map<String, String>map=getParamMap();
		String infoNumber = getString("infoNumber","");
		if(!StringUtils.isEmpty(infoNumber)){
		String period = getString("period","");
		String busType=getString("busType","");
		map.put("infoNumber", infoNumber);
		map.put("period", period);
		map.put("busType", busType);
		}
		Map<String, String>result=ProjectMApiRemoteServer.getQueryDetail(map);
		if(result!=null && "SUCCESS".equals(result.get("STATE"))){
			String pdd=result.get("phoneDial");
			outPrint(response, pdd);
		}else{
			outPrint(response, JSONObject.fromObject("{}"));
		}
		
	}
	/**
	 * 同步话单
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="syncCallList")
	public void syncCallList(HttpServletResponse response){
		try{
			Map<String,Object> param = this.getParaMap() ;
			Map<String,Object> result = phoneDialDetailService.syncCallList(param);
			outPrint(response, JSONObject.fromObject(result,getDefaultJsonConfig()));
		}catch(Exception e){
			e.printStackTrace();
			log.error("同步话单异常 syncCallList："+e.getMessage());
		}
	}
	
	/**
	 * 按天同步话单
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="syncCallListDay")
	public void syncCallListDay(HttpServletResponse response) {
		Map<String,Object> result=new HashMap<String, Object>();
		try{
			Map<String,Object> param = this.getParaMap() ;
			if(null!=param.get("partners") && "TTEN".equals(param.get("partners").toString())){
				result = phoneDialDetailService.syncCallListDay(param);
			}else if(null!=param.get("partners") && "HW".equals(param.get("partners").toString())){
				result = phoneDialDetailService.syncDxCallListDay(param);
			}
			outPrint(response, JSONObject.fromObject(result,getDefaultJsonConfig()));
		}catch(Exception e){
			e.printStackTrace();
			log.error("按天同步话单异常 syncCallListDay："+e.getMessage());
		}
	}
	
	/**
	 * 获取最后同步时间
	 * @param response
	 */
	@RequestMapping(value="syncLastTime")
	public void syncLastTime(HttpServletResponse response) {
		List<PhoneSyncHistory> syncHistList = queryExecutor.execQuery(PhoneSyncHistory.MAPPER+".select", null, PhoneSyncHistory.class);
		if(syncHistList!=null && syncHistList.size()>0){
			getOutputMsg().put("TIME", syncHistList.get(0).getEndTime());
		}else{
			getOutputMsg().put("TIME", "");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(),getSyncTimeJsonConfig()));
	}
	private JsonConfig getSyncTimeJsonConfig(){
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
