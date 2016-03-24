package com.wuyizhiye.cmct.phone.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.cmct.phone.model.PhoneDialDetail;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneDurationAnalyticalListController
 * @Description 话务分析-时长分析
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/durationAnalytical/*")
public class PhoneDurationAnalyticalListController extends ListController {

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
		//put("org", SystemUtil.getCurrentOrg());
		return "cmct/phone/phoneDurationAnalytical";
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
		return PhoneDialDetail.MAPPER + ".selectByDurationAnalytical";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@RequestMapping(value="queryByDurationAnalytical")
	public void queryByCallAnalytical(Pagination<PhoneDialDetail> pagination,HttpServletResponse response){
		Map<String, Object>pararm=this.getListDataParam();
		Map<String, Object>param=setPararm(pararm);
		List<PhoneMember> callUserList = queryExecutor.execQuery(PhoneMember.MAPPER+".select", null, PhoneMember.class);
		pagination = queryExecutor.execQuery(getListMapper(), pagination, param);
		if(pagination!=null && pagination.getItems()!=null && pagination.getItems().size() > 0){
			for(PhoneDialDetail cd : pagination.getItems()){
				//cd.setAvgDuration(Double.valueOf(cd.getTotalDuration()/cd.getNumberCount()));
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
	public String getPagintion(){
		Pagination<PhoneDialDetail> pagination=new Pagination<PhoneDialDetail>();
		Map<String,Object> param=getListDataParam();
		List<PhoneMember> callUserList = queryExecutor.execQuery(PhoneMember.MAPPER+".select", null, PhoneMember.class);
		pagination = queryExecutor.execQuery(getListMapper(), pagination,null);
		if(pagination!=null && pagination.getItems()!=null && pagination.getItems().size() > 0){
			for(PhoneDialDetail cd : pagination.getItems()){
				cd.setAvgDuration(Double.valueOf(cd.getTotalDuration()/cd.getNumberCount()));
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
		String xstr="天";
		String dateType=(String) param.get("dateType");
		if("month".equals(dateType)){
			xstr="月份";
		}
		put("titleName",xstr);
		String result="<graph caption='时长分析' xAxisName='"+xstr+"' yAxisName='时长' showNames='1' decimalPrecision='2' formatNumberScale='0'>";
		for(PhoneDialDetail ph:pagination.getItems()){
			result+="<set name='"+ph.getPeriod()+"' value='"+ph.getAvgDuration()+"' color='F6BD0F' />";
		}
		result+="</graph>";
		return result;
	}
	protected Map<String, Object> setPararm(Map<String, Object>pararm) {
		Map<String,Object> param=new HashMap<String, Object>();
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
    	String type=(String)pararm.get("dateType");//类型
    	String showMonth=(String)pararm.get("showMonth");//月份
    	String showDay=(String)pararm.get("showDay");//天
    	String startDay=(String)pararm.get("startDay");//开始时间
    	String endDay=(String)pararm.get("endDay");//结束时间
//    	String orgId=(String)pararm.get("orgId");//组织
    	String infoNumber=(String)pararm.get("infoNumber");//固话号码
        String start="";
        String end="";
        if("month".equals(type)){//按月查
        	if(null != showMonth && !"".equals(showMonth)){
        		param.put("period", showMonth);
        	}
        	
        }else if("day".equals(type)){//按天查
        	if(null != showDay && !"".equals(showDay)){
        		start=showDay;
            	end=getAfter(showDay);
        	}
        }else if("period".equals(type)){//按期间查
        	if(null != startDay && !"".equals(startDay)){
        		start=startDay.replaceAll("/", "-");
        	}
        	if(null != endDay && !"".equals(endDay)){
        		end=getAfter(endDay.replaceAll("/", "-"));
        	}
        }
        try {
        	if(!"".equals(start)){
        		param.put("startTime", df.parse(start));
        	}
			if(!"".equals(end)){
				param.put("endTime", df.parse(end));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		param.put("infoNumber", infoNumber);
//		param.put("orgId", orgId);
		return param;
	}
	/** 
	* 获得指定日期的后一天 
	* @param specifiedDay 
	* @return 
	*/ 
	public static String getAfter(String specifiedDay){ 
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
