package com.wuyizhiye.cmct.phone.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.model.PhoneConfig;
import com.wuyizhiye.cmct.phone.model.PhoneSyncHistory;
import com.wuyizhiye.cmct.phone.service.PhoneDialDetailService;
import com.wuyizhiye.cmct.phone.util.PhoneMemberUtil;
import com.wuyizhiye.cmct.phone.util.ProjectMApiRemoteServer;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneSyncManageController
 * @Description 同步话单
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/syncManage/*")
public class PhoneSyncManageController extends ListController {

	@Autowired
	private PhoneDialDetailService phoneDialDetailService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		put("showMonth",df.format(new Date()).substring(0, 7));
		put("showDay",df.format(new Date()));
		put("startDay",(df.format(new Date()).substring(0, 7)+"-01").replaceAll("-", "/"));
		put("endDay",df.format(new Date()).replaceAll("-", "/"));
		/*put("chartData",getPagintion());*/
		put("org", SystemUtil.getCurrentOrg());
		
		Map<String,String> configParam = new HashMap<String,String>();
		List<PhoneConfig>pcs=new ArrayList<PhoneConfig>();
		//根据系统参数从鼎尖yun上读取核算渠道,本地核算渠道表已经无效  by lxl 14.5.13 
		configParam.put("customerId", ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID));
		Map<String, String> result=ProjectMApiRemoteServer.getCustomerByOrgId(configParam);//获取核算渠道,一个客户下可能有多个核算渠道
		if(result!=null && "SUCCESS".equals(result.get("STATE"))){
			String strConfig=result.get("pcs");
			JSONArray jsonArr=JSONArray.fromObject(strConfig);
			if(null!=jsonArr && jsonArr.size()>0){
				for(int i=0;i<jsonArr.size();i++){
					JSONObject jsonObj=jsonArr.getJSONObject(i);
					PhoneConfig pc=new PhoneConfig();
					pc.setConfigName(jsonObj.getString("configName"));
					pc.setOrgId(jsonObj.getString("orgId"));
					pc.setPartners(jsonObj.getString("partners"));
					pc.setOrgKey(jsonObj.getString("orgKey"));
					pc.setGetCallUrl(jsonObj.getString("getCallUrl"));
					pcs.add(pc);
				}
			}
			
		}
		put("configList", pcs);
		return "cmct/phone/phoneSyncManage";
	}

	public void put(String key,Object o){
		this.getRequest().setAttribute(key, o);
	}
	
	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return PhoneSyncHistory.MAPPER+".selectBySync";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return null;
	}
	@RequestMapping(value="queryData")
	public void queryData(Pagination<PhoneSyncHistory> pagination,HttpServletResponse response) throws ParseException{
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("syncOrgId", getString("syncOrgId",""));
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String period=(String) getListDataParam().get("showMonth");
		String startTime=period+"-01";
		String endTime=getMonthEnd(period);
		if(!"".equals(startTime)){
			param.put("startTime", df.parse(startTime));
    	}
		if(!"".equals(endTime)){
			param.put("endTime", df2.parse(endTime));
		}
		String[] dayArray= getDayArrays(startTime,endTime);
		List<PhoneSyncHistory> pss = queryExecutor.execQuery(getListMapper(), param, PhoneSyncHistory.class);
		List<String> sameDay=new ArrayList<String>();
		List<PhoneSyncHistory> result=new ArrayList<PhoneSyncHistory>();
		for(PhoneSyncHistory ps:pss){
			if(!sameDay.contains(df.format(ps.getStartTime()))){
				sameDay.add(df.format(ps.getStartTime()));
				ps.setFlag(true);
				ps.setDateStr(df.format(ps.getStartTime()).substring(8, 10));
				result.add(ps);
			}
		}
		for(String str:dayArray){
			if(!sameDay.contains(str)){
				PhoneSyncHistory ps=new PhoneSyncHistory();
				ps.setFlag(false);
				ps.setDateStr(str.substring(8, 10));
				result.add(ps);
			}
		}
		
		Collections.sort(result);//重新排序
		pagination.setItems(result);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
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
			e.printStackTrace();
		}
		  int MaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
		  c.set( c.get(Calendar.YEAR), c.get(Calendar.MONTH), MaxDay, 23, 59, 59);
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   return sdf.format(c.getTime()); 
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
	
	//取出 两个时间段之间的所有数据集合
	public static String[] getDayArrays(String start,String end) throws ParseException {
	      DateFormat df = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd 格式 下面那个参数是7
	      @SuppressWarnings("rawtypes")
	      ArrayList ret = new ArrayList();
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(df.parse(start));
	      Date tmpDate = calendar.getTime();
	      long endTime = df.parse(end).getTime();
	      while (tmpDate.before(df.parse(end)) || tmpDate.getTime() == endTime) {
	        ret.add(calendar.getTime());
	        calendar.add(7, 1);//2 按月分   7  按天分
	        tmpDate = calendar.getTime();
	      }
	      Date[] dates = new Date[ret.size()];
	      Date[] dat=(Date[]) ret.toArray(dates);
	      String result="";
	      for(Date d:dat){
	       result+=df.format(d)+",";
	      }
	      String str=getTurnOver(result);
	      return str.split(",");
	    }
	 public static  String getTurnOver(String str){
		 String result="";
		 String[] arr=str.split(",");
		 int len=arr.length;
		 for(int i=0;i<arr.length;i++){
			 result+=arr[len-1-i]+",";
		 }
		 return result;
	 }
	 /**
		 * 按月同步话单
		 * @param response
		 * @throws Exception 
		 */
		@RequestMapping(value="syncCallList")
		public void syncCallList(HttpServletResponse response) throws Exception{
			Map<String,Object> param = this.getParaMap() ;
			String periond=getString("periond");
			param.put("periond", periond);
			Map<String,Object> result = phoneDialDetailService.syncCallListDay(param);
			outPrint(response, JSONObject.fromObject(result,getDefaultJsonConfig()));
		}
		
	/**
	 * 获01日同步数量的状态
	 * getTimeFlagSysCount
	 */
		@RequestMapping(value="getTimeFlagSysCount")
		public void getTimeFlagSysCount(HttpServletResponse response){
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String periond=getString("periond");
			Date startTime=null;
			Map<String, Object>pararm=new HashMap<String, Object>();
			if(!"".equals(periond)){
				try {
					startTime=df.parse((periond+"-01"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			pararm.put("startTime", startTime);
			pararm.put("syncOrgId", getString("syncOrgId"));
			PhoneSyncHistory ps=null;
			Date endTime=null;
			
			List<PhoneSyncHistory>list=queryExecutor.execQuery("com.wuyizhiye.cmct.phone.dao.PhoneSyncHistoryDao.getTimeFlagSysCount", pararm, PhoneSyncHistory.class);
			if(null!=list && list.size()>0){
				ps=list.get(0);
			}
			if(null!=ps && ps.getEndTime()!=null){
				endTime=ps.getEndTime();
			}
			
			Long day=DateUtil.getDifferDay(endTime,startTime);
			if(day>=28){
				getOutputMsg().put("STATE", "MONTH");
			}else if(day==1){
				getOutputMsg().put("STATE", "DAY");
			}else{
				getOutputMsg().put("STATE", "FAIl");
			}
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		}
}
