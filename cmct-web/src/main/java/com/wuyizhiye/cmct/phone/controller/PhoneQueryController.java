package com.wuyizhiye.cmct.phone.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.enums.DialResultEnum;
import com.wuyizhiye.cmct.phone.model.PhoneDialRecord;
import com.wuyizhiye.cmct.phone.service.PhoneDialRecordService;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.framework.util.Constant;

/**
 * @ClassName PhoneQueryController
 * @Description 呼叫中心HTTP接口 话务查询 控制器
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneQuery/*")
public class PhoneQueryController extends ListController {

	@Autowired
	private PhoneDialRecordService phoneDialRecordService ;
	
	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		SimpleDateFormat day=new SimpleDateFormat("yyyy/MM/dd");
		getRequest().setAttribute("startDay",day.format(new Date()).substring(0, 7)+"/01");
		getRequest().setAttribute("endDay",day.format(new Date()));
		
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		getRequest().setAttribute("showMonth",df.format(new Date()).substring(0, 7));
		getRequest().setAttribute("showDay",df.format(new Date()));
		getRequest().setAttribute("startDay",(df.format(new Date()).substring(0, 7)+"-01").replaceAll("-", "/"));
		getRequest().setAttribute("endDay",df.format(new Date()).replaceAll("-", "/"));
		return "cmct/phone/phoneQuery";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	protected Map<String, Object> getListDataParam() {
		Map<String,Object> param=super.getListDataParam();
		param.put("longNumber", SystemUtil.getCurrentOrg().getLongNumber()+"%");
		String callResult = param.get("callResult") == null ? "" : (String)param.get("callResult");
		if(!StringUtils.isEmpty(callResult)){
			if(DialResultEnum.C_128.getValue().equals(callResult)){
				//param.put("callResult",null);
				//param.put("No128", "Y");
				param.put("noCostTime", null);
				param.put("hasCostTime", "Y");
			}else{
				//param.put("No128", null);
				param.put("noCostTime", "Y");
				param.put("hasCostTime", null);
			}
		}
		param.put("callResult", null);
		//{queryStartDate=2013/09/01, pageSize=30, orderByClause= ic.fcalltime desc, callResult=null, currentPage=1, longNumber=DJSF%, queryEndDate=2013/09/12}
		Object queryEndDateObj = param.get("queryEndDate");
		if(queryEndDateObj!=null && !StringUtils.isEmpty(queryEndDateObj.toString())){
			String queryEndDate = queryEndDateObj.toString().replace("/", "-");
			queryEndDateObj = DateUtil.convertDateToStr(DateUtil.getDateByDays(DateUtil.convertStrToDate(queryEndDate), -1)) ;
			queryEndDateObj = queryEndDateObj.toString().replace("-", "/");
			param.put("queryEndDate",queryEndDateObj);
		}
		//跟进数据源判断查询SQL
		String dataBaseType = ParamUtils.getParamValue(Constant.DATABASETYPE);
		if(StringUtils.isEmpty(dataBaseType)){//默认为MYSQL
			dataBaseType = Constant.DATABASETYPE_MYSQL ;
		}else{
			dataBaseType = dataBaseType.toUpperCase() ;//转成大写
		}
		Object queryStartDateObj = param.get("queryStartDate");
		if(Constant.DATABASETYPE_MYSQL.equals(dataBaseType)){
			if(queryStartDateObj!=null && !StringUtils.isEmpty(queryStartDateObj.toString())){
				param.put("queryStartDateMySql", queryStartDateObj);
				param.put("queryStartDate", null);
			}
			if(queryEndDateObj!=null && !StringUtils.isEmpty(queryEndDateObj.toString())){
				param.put("queryEndDateMySql", queryEndDateObj);
				param.put("queryEndDate", null);
			}
		}else if(Constant.DATABASETYPE_ORACLE.equals(dataBaseType)){
			if(queryStartDateObj!=null && !StringUtils.isEmpty(queryStartDateObj.toString())){
				param.put("queryStartDateOracle", queryStartDateObj);
				param.put("queryStartDate", null);
			}
			if(queryEndDateObj!=null && !StringUtils.isEmpty(queryEndDateObj.toString())){
				param.put("queryEndDateOracle", queryEndDateObj);
				param.put("queryEndDate", null);
			}
		}else if(Constant.DATABASETYPE_SQLSERVER.equals(dataBaseType)){
			
		}
		
		String dateType=getString("dateType");//类型
    	String showMonth=getString("showMonth");//月份
    	String showDay=getString("showDay");//天
    	String start="";
        String end="";
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		 if("month".equals(dateType)){//按月查
	        	if(null != showMonth && !"".equals(showMonth)){
	        		start=showMonth+"-01";
	            	end=getMonthEnd(showMonth);
	            	param.put("suffix", "_"+showMonth.replace("-", "_"));
	        	}
	        	
	        }else if("day".equals(dateType)){//按天查
	        	if(null != showDay && !"".equals(showDay)){
	        		start=showDay;
	            	end=showDay;
	            	param.put("suffix", "_"+showDay.substring(0,7).replace("-", "_"));
	        	}
	        }
	        try {
	        	if(!"".equals(start)){
	        		param.put("startTime", df.parse(start));
	        	}
				if(!"".equals(end)){
					param.put("endTime", DateUtil.getNextDay(df.parse(end)));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		return param;
	}

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
	
	@Override
	protected String getListMapper() {
		return PhoneDialRecord.MAPPER + ".select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return phoneDialRecordService;
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
	
}
