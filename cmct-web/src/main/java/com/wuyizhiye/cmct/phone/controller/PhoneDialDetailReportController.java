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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.cmct.phone.model.PhoneCostPay;
import com.wuyizhiye.cmct.phone.model.PhoneDialDetail;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.cmct.phone.service.PhoneCostPayService;
import com.wuyizhiye.cmct.phone.service.PhoneDialDetailService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneDialDetailReportController
 * @Description 话费管理-话费统计
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneDialDetailReport/*")
public class PhoneDialDetailReportController extends ListController {

	@Autowired
	private PhoneDialDetailService phoneDialDetailService;
	
	@Autowired
	private PhoneCostPayService phoneCostPayService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		return "cmct/phone/phoneDialDetailReport";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return PhoneDialDetail.MAPPER + ".selectByInfo";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return phoneDialDetailService;
	}
	
	@RequestMapping(value="query")
	public void queryByInfo(Pagination<PhoneDialDetail> pagination,HttpServletResponse response) throws ParseException{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Map<String,Object> payParam=new HashMap<String, Object>();
		Map<String,Object> param = getListDataParam() ;
		//查找出开通的电话，过滤出使用模式
		List<PhoneMember> callUserList = queryExecutor.execQuery(PhoneMember.MAPPER+".select", null, PhoneMember.class);
		pagination = queryExecutor.execQuery(getListMapper(), pagination, param);
		if(pagination!=null && pagination.getItems()!=null && pagination.getItems().size() > 0){
			for(PhoneDialDetail cd : pagination.getItems()){
				
				if(StringUtils.isEmpty(cd.getPeriod())){
					continue ;
				}
				
				//得出每一行数据的充值总额记录,根据月份得到
				String start=cd.getPeriod()+"-01";;
				String end=getDayAfter(getMonthEnd(cd.getPeriod()));
				payParam.put("startTime", df.parse(start));
				payParam.put("endTime", df.parse(end));
				cd.setTotalPayCost(phoneCostPayService.getPaycostByPeriod(payParam));
				
				//得到月末余额
				List<PhoneCostPay>ps=queryExecutor.execQuery(PhoneCostPay.MAPPER + ".selectByMonthSurplusCost", payParam, PhoneCostPay.class);
				if(null!=ps&&ps.size()>0&&null!=ps.get(0).getPaySurplusCost()){
					cd.setMonthSurplusCost(ps.get(0).getPaySurplusCost());
				}else{
					cd.setMonthSurplusCost(0D);
				}
				
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
