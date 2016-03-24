package com.wuyizhiye.cmct.phone.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.cmct.phone.model.PhoneDialDetail;
import com.wuyizhiye.cmct.phone.service.PhoneDialDetailService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneTrendAnalyticalController
 * @Description 话务分析-走势分析
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/trendAnalytical/*")
public class PhoneTrendAnalyticalController extends ListController {

	@Autowired
	private PhoneDialDetailService phoneDialDetailService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		Map<String,Object> param=new HashMap<String, Object>();	
		SimpleDateFormat mon=new SimpleDateFormat("yyyy/MM");		
		SimpleDateFormat day=new SimpleDateFormat("yyyy/MM/dd");
		String startMonth=mon.format(new Date()).substring(0, 4)+"/01";
		String endMonth=mon.format(new Date());
		String startDay=day.format(new Date()).substring(0, 7)+"/01";
		String endDay=day.format(DateUtil.getPrevDay(new Date()));
		String dateType="day";//默认按月份查询		
		putval("dateType",dateType);
		putval("startMonth",startMonth);
		putval("endMonth",endMonth);
		putval("startDay",startDay);
		putval("endDay",endDay);
		return "cmct/phone/phoneTrendAnalytical";
	}
	
	/**
	 * 读取数据
	 * @param response
	 */
	@RequestMapping("getData")
	@ResponseBody
	public void getData(HttpServletResponse response){
		Map<String,Object> json=new HashMap<String, Object>();
		String startDay=getString("startDay");//开始日
		String endDay=getString("endDay");//结束日
		String startMonth=getString("startMonth");//开始月
		String endMonth=getString("endMonth");//结束月
		String dateType=getString("dateType");//时间类型
		List<Map<String,Object>> rlist=new ArrayList<Map<String,Object>>();
		String[] months = null;
		if("month".equals(dateType)){//按月份查询
			try {
				months=getMonthArrays(startMonth, endMonth);//得到 月份期间 集合
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}else{//按日期查询
			try {
				months=getDayArrays(startDay, endDay);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(String time:months){
			Map<String,Object> result=getMap(time,dateType);
			rlist.add(result);
		}
		String xstr="天";
		if("month".equals(dateType)){
			xstr="月份";
		}
		json.put("titleName",xstr);
		json.put("showList", rlist);
		json.put("chartData",getChart(rlist,dateType));
		outPrint(response,JSONObject.fromObject(json, getDefaultJsonConfig()));
	}
	
	public Map<String,Object>getMap(String time,String type){
		Map<String,Object> param=new HashMap<String, Object>();
		DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
		String start="";//开始时间
		String end="";//结束时间
		if("month".equals(type)){
			time = time.replace("/", "-");
			param.put("period", time);
		}else{
			String period = time ;
			period = period.replace("/", "-");
			period = period.substring(0, 7);
			start=time;
			end=getDayAfter(time);
			try {
		//		param.put("period", period);
				param.put("startTime", df.parse(start));
				param.put("endTime", df.parse(end));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
//		param.put("orgId", orgId);
		List<PhoneDialDetail>ps=queryExecutor.execQuery(getListMapper(), param, PhoneDialDetail.class);
		Map<String,Object> result=new HashMap<String, Object>();
		String totalDuration="0";
		if(ps!=null && ps.size()>0 && ps.get(0).getTotalDuration()!=null){
			totalDuration= StringUtils.convSecondTimeToString(ps.get(0).getTotalDuration());
		}
		result.put("totalDuration", totalDuration);
		result.put("totalDurationSecond", ps.get(0).getTotalDuration());
		result.put("numberCount", ps.get(0).getNumberCount());
		result.put("time", time);
		return result;
	}
	
	/**
	 * 取 图表数据
	 * @param mlist
	 * @return
	 */
	
	public  String getChart(List<Map<String,Object>> mlist,String type){
		/**
		 * 图表 显示 时间按正序
		 */
		List<Map<String,Object>> rlist=new ArrayList<Map<String,Object>>();
		int len=mlist.size();
		for(int i=0;i<mlist.size();i++){
			rlist.add(mlist.get(len-i-1));
		}
		String xstr="天";
		if("month".equals(type)){
			xstr="月份";
		}
		putval("titleName",xstr);
		
		String result="<graph caption='话务走势' xAxisName='"+xstr+"' yAxisName='成功通数(个)' showNames='1' decimalPrecision='2' formatNumberScale='0'>";
		for(Map<String,Object> map:rlist){
			result+="<set name='"+map.get("time")+"' value='"+map.get("numberCount")+"' color='F6BD0F' />";
		}
		result+="</graph>";
		
		
		/* 两根线String result="<graph caption='话务走势' xAxisName='"+xstr+"' yAxisName='' showNames='1' decimalPrecision='2' formatNumberScale='0'>";
		result+="<categories >";
		for(Map<String,Object> map:rlist){
			result+="<category name='"+map.get("time")+"'  />";
		}
		result+="</categories>";
		
		
		result+="<dataset seriesName='总时长(秒)' color='1D8BD1' anchorBorderColor='1D8BD1' anchorBgColor='1D8BD1'>";
		for(Map<String,Object> map:rlist){
			result+="<set value='"+map.get("totalDurationSecond")+"' />";
		}
		result+="</dataset>";
		
		
		result+="<dataset seriesName='成功通数(个)'  color='F1683C' anchorBorderColor='F1683C' anchorBgColor='F1683C'>";
		for(Map<String,Object> map:rlist){
			result+="<set value='"+map.get("numberCount")+"' />";
		}
		result+="</dataset>";
		
		result+="</graph>";*/
		return result;
	}
	
	
	/**
	 * 向页面传参
	 * @param key
	 * @param val
	 */
	public void putval(String key,Object val){
		this.getRequest().setAttribute(key, val);
	}
	
	/**
	 * 判断是否第一次 进入页面
	 * @return
	 */
	public boolean firstFlag(){
		String enterFlag=getString("enterFlag");
		boolean flag=false;
		if(null == enterFlag){
			flag=true;
		}
		putval("enterFlag","yes");
		return flag;
	
	}
	public void put(String key,Object o){
		this.getRequest().setAttribute(key, o);
	}
	
	/**
	  * 将时间格式为 yyyy-MM 的时间段 按月逐一列出  
	  * @param start
	  * @param end
	  * @return
	  * @throws ParseException
	 * @throws java.text.ParseException 
	  */
	 @SuppressWarnings("unchecked")
	 public static String[] getMonthArrays(String start,String end) throws ParseException {
	      DateFormat df = new SimpleDateFormat("yyyy/MM");//yyyy-MM-dd 格式 下面那个参数是7
	      @SuppressWarnings("rawtypes")
	      ArrayList ret = new ArrayList();
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(df.parse(start));
	      Date tmpDate = calendar.getTime();
	      long endTime = df.parse(end).getTime();
	      while (tmpDate.before(df.parse(end)) || tmpDate.getTime() == endTime) {
	        ret.add(calendar.getTime());
	        calendar.add(2, 1);//2 按月分   7  按天分
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
	 
	 /**
	  * 取出 两个时间段之间的所有数据集合
	  * @param start
	  * @param end
	  * @return
	  * @throws ParseException
	  */
	 @SuppressWarnings("unchecked")
	public static String[] getDayArrays(String start,String end) throws ParseException {
	      DateFormat df = new SimpleDateFormat("yyyy/MM/dd");//yyyy-MM-dd 格式 下面那个参数是7
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
	 /**
		 * 获取一个月 的最后一天
		 * @param str
		 * @return
		 */
		public String getMonthEnd(String str){
			 Calendar c = Calendar.getInstance();
			  SimpleDateFormat format =  new SimpleDateFormat("yyyy/MM");
			  try {
				c.setTime(format.parse(str));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			  c.add(Calendar.MONTH, -1);//用于 获取上/ 下一个月
			//得到一个月最后一天日期(31/30/29/28)
			  int MaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
			  //按你的要求设置时间
			  c.set( c.get(Calendar.YEAR), c.get(Calendar.MONTH), MaxDay, 23, 59, 59);
			  //按格式输出
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			   return sdf.format(c.getTime()); //该月最后一天
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
		* 获得指定日期的后一天 
		* @param specifiedDay 
		* @return 
		*/ 
		public static String getDayAfter(String specifiedDay){ 
		Calendar c = Calendar.getInstance(); 
		Date date=null; 
		try { 
		date = new SimpleDateFormat("yy/MM/dd").parse(specifiedDay); 
		} catch (ParseException e) { 
		e.printStackTrace(); 
		} 
		c.setTime(date); 
		int day=c.get(Calendar.DATE); 
		c.set(Calendar.DATE,day+1); 

		String dayAfter=new SimpleDateFormat("yyyy/MM/dd").format(c.getTime()); 
		return dayAfter; 
		}
		
	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListMapper() {
		return PhoneDialDetail.MAPPER + ".selectByTrendAnalytical";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return null;
	}

}
