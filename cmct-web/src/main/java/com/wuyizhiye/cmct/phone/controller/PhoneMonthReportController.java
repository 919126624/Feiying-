package com.wuyizhiye.cmct.phone.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.cmct.phone.model.PhoneCostPay;
import com.wuyizhiye.cmct.phone.model.PhoneDialDetail;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.cmct.phone.service.PhoneDialDetailService;
import com.wuyizhiye.cmct.phone.util.PhoneMemberUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneMonthReportController
 * @Description 话费管理--话单月报
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneMonthReport/*")
public class PhoneMonthReportController extends ListController {

	@Autowired
	private PhoneDialDetailService phoneDialDetailService;
	
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		getRequest().setAttribute("period", DateUtil.convertDateToStr(new Date(), "yyyy-MM"));
		getRequest().setAttribute("per", getString("per"));
		return "cmct/phone/phoneMonthReport";
	}

	@Override
	protected String getEditView() {
		return "cmct/phone/phoneMonthReportEdit";
	}
	
	@RequestMapping(value="manager")
	public String manager(){
		getRequest().setAttribute("per", getString("per"));
		getRequest().setAttribute("disPlayCallBill", ParamUtils.getParamValue(PhoneMemberUtil.CMCT_DISPLAY_DCB));
		return "cmct/phone/phoneCostManager" ;
	}

	@Override
	protected String getListMapper() {
		return PhoneDialDetail.MAPPER + ".selectByMonth";
	}

	@Override
	protected BaseService getService() {
		return null;
	}
	
	/**
	 * 列表数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="queryByMonth")
	public void queryByMonth(Pagination<PhoneDialDetail> pagination,HttpServletResponse response){
		Map<String,Object> param = getListDataParam() ;
		String period = (String) param.get("period");
		//查找出开通的电话，过滤出使用模式
		List<PhoneMember> callUserList = queryExecutor.execQuery(PhoneMember.MAPPER+".select", null, PhoneMember.class);
		
		// 点击列头排序
		if (param.get("sortname") != null) {
			String sortName = (String) param.get("sortname") ;
			if("phoneCost".equals(sortName)){
				param.put("sortname", "tt.phoneCost");
			}else if("moblieCost".equals(sortName)){
				param.put("sortname", "tt.moblieCost");
			}else if("longLineCost".equals(sortName)){
				param.put("sortname", "tt.longLineCost");
			}else if("callCost".equals(sortName)){
				param.put("sortname", "tt.callCost");
			}else if("offsetSum".equals(sortName)){
				param.put("sortname", "tt.offsetSum");
			}else if("totalCost".equals(sortName)){
				param.put("sortname", "tt.totalCost");
			}else{
				param.put("sortname", null);
			}
		}
		
		pagination = queryExecutor.execQuery(getListMapper(), pagination, param);
		if(pagination!=null && pagination.getItems()!=null && pagination.getItems().size() > 0){
			for(PhoneDialDetail cd : pagination.getItems()){
				cd.setPeriod(period);
				if(StringUtils.isEmpty(cd.getInfoNumber())){
					cd.setInfoNumber("HIDE");//隐藏呼出
				}else{
					PhoneMember cs = this.getUseTypeByShowPhone(callUserList, cd.getInfoNumber()) ;
					if(cs != null){
						cd.setUseType(cs.getUseType() == null ? "" : cs.getUseType().getName());
						cd.setOrgName(cs.getOrg() == null ? "" : cs.getOrg().getName());
					}
				}
			}
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	private PhoneMember getUseTypeByShowPhone(List<PhoneMember> callUserList,String showPhone){
		PhoneMember callSet = null ;
		for(PhoneMember cs : callUserList){
			if(cs.getShowPhone()!=null && cs.getShowPhone().contains(showPhone)){
				callSet = cs ;
				break ;
			}
		}
		return callSet;
	}
	
	@Override
	protected JsonConfig getDefaultJsonConfig(){
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
	
	@Override
	protected Map<String,Object> getListDataParam(){
		Map<String,String> param = getParamMap();
		Map<String,Object> params = new HashMap<String, Object>();
		Set<String> keys = param.keySet();
		for(String key : keys){
			params.put(key, param.get(key));
		}
		return params;
	}
	
	/**
	 * 统计合计
	 * @param response
	 * @throws ParseException 
	 */
	@RequestMapping(value="getTotalCost")
	public void getTat(HttpServletResponse response) throws ParseException{
		Map<String,Object> param = getListDataParam() ;
		
		//月合计
		PhoneDialDetail total = queryExecutor.execOneEntity(PhoneDialDetail.MAPPER+".selectTotalByMonth", param, PhoneDialDetail.class);
		getOutputMsg().put("total", total);
		
		//包月费和低消
		Map<String,Object> queryResult=queryExecutor.execOneEntity("com.wuyizhiye.cmct.phone.dao.PhoneCosDetailDao.selectMonthCostAndOffsetCost", param, Map.class);
		getOutputMsg().put("offsetCost", (queryResult == null || queryResult.get("offsetSum") == null ) ? 0.00 : queryResult.get("offsetSum"));
		getOutputMsg().put("monthCostSum", (queryResult == null || queryResult.get("monthCostSum") == null ) ? 0.00 : queryResult.get("monthCostSum"));
		
		//充值合计
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Map<String,Object> payParam=new HashMap<String, Object>();
		
		String period = (String) param.get("period");
		String start=period+"-01";;
		String end=getDayAfter(getMonthEnd(period));
		
		payParam.put("startTime", df.parse(start));
		payParam.put("endTime", df.parse(end));
		payParam.put("orgId", (String)param.get("orgId"));
		
		Double totalPaycost = queryExecutor.execOneEntity(PhoneCostPay.MAPPER+".selectPaycostByPeriod", payParam, Double.class);
		getOutputMsg().put("totalPaycost", totalPaycost == null ? 0.00 : totalPaycost);
		
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	/**
	 * 充值合计
	 * @param response
	 * @throws ParseException 
	 */
	@RequestMapping(value="getPayCallCost")
	public void getPayCallCost(HttpServletResponse response) throws ParseException{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Map<String,Object> param = getListDataParam() ;
		Map<String,Object> payParam=new HashMap<String, Object>();
		
		String period = (String) param.get("period");
		String start=period+"-01";;
		String end=getDayAfter(getMonthEnd(period));
		
		payParam.put("startTime", df.parse(start));
		payParam.put("endTime", df.parse(end));
		payParam.put("orgId", (String)param.get("orgId"));
		
		Double total = queryExecutor.execOneEntity(PhoneCostPay.MAPPER+".selectPaycostByPeriod", payParam, Double.class);
		getOutputMsg().put("total", total == null ? 0.00 : total);
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@Override
	public String edit(ModelMap model,@RequestParam(required=true,value="id")String id){
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("showPhone", getString("infoNumber"));
		List<PhoneMember> callSetList = queryExecutor.execQuery(PhoneMember.MAPPER + ".select", map, PhoneMember.class);
		PhoneMember cs = new PhoneMember() ;
		if(callSetList!=null && callSetList.size() > 0){
			cs = callSetList.get(0);
			cs.setPeriod(id);
			cs.setComboType(getString("comboType",""));
			cs.setCallCost(getString("callCost",""));
		}
		model.put("data", cs);
		model.put("edit_viewStatus", this.getString("VIEWSTATE"));
		return getEditView();
	}
	 /**
	 * 获取一个月 的最后一天
	 * @param str
	 * @return
	 */
	public String getMonthEnd(String str){
		 Calendar c = Calendar.getInstance();
		  SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM");
		  try {
			c.setTime(format.parse(str));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		  c.add(Calendar.MONTH, -1);//用于 获取上/ 下一个月
		//得到一个月最后一天日期(31/30/29/28)
		  int MaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
		  //按你的要求设置时间
		  c.set( c.get(Calendar.YEAR), c.get(Calendar.MONTH), MaxDay, 23, 59, 59);
		  //按格式输出
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   return sdf.format(c.getTime()); //该月最后一天
	}
	 /** 
	* 获得指定日期的后一天 
	* @param specifiedDay 
	* @return 
	*/ 
	public static String getDayAfter(String specifiedDay){ 
	Calendar c = Calendar.getInstance(); 
	Date date=null; 
	try { 
	date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay); 
	} catch (ParseException e) { 
	e.printStackTrace(); 
	} 
	c.setTime(date); 
	int day=c.get(Calendar.DATE); 
	c.set(Calendar.DATE,day+1); 

	String dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()); 
	return dayAfter; 
	}
}
