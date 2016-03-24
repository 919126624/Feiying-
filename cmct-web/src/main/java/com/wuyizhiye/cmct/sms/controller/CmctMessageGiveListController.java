package com.wuyizhiye.cmct.sms.controller;

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
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.sms.enums.SMSControlTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSStrategyTypeEnum;
import com.wuyizhiye.cmct.sms.model.MessageGive;
import com.wuyizhiye.cmct.sms.model.SMSControl;
import com.wuyizhiye.cmct.sms.model.ShortMessage;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName CmctMessageGiveListController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping("cmct/messageGive/*")
public class CmctMessageGiveListController extends ListController{

	@Override
	protected CoreEntity createNewEntity() {
		return new MessageGive();
	}

	@Override
	protected String getListView() {
		return null;
	}

	@Override
	protected String getEditView() {
		this.getRequest().setAttribute("now", new Date());
		getLastNum();
		return "cmct/sms/messageGiveEdit";
	}
	
	@RequestMapping("judgePerson")
	public void judgePerson(HttpServletResponse response){
		Map<String,Object> json=new HashMap<String, Object>();
		String personId=getString("personId");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("controlType", SMSControlTypeEnum.PERSONAL_TYPE);//控制类型 个人
		params.put("objectId", personId);//控制类型 个人
		List<SMSControl> list = queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.SMSControlDao.getSMSControlByCond", params, SMSControl.class);
		boolean flag=false;//只有开通短信控制 以及 类型为  余额控制的 才可以
		if(null != list && list.size()>0){//接受人 有 开通短信控制
			SMSControl sc=list.get(0);//可以给该人赠送短信
			if(sc.getBalanceStrategyFlag()){
				flag=true;
			}
		}
		json.put("flag", flag);
		outPrint(response,JSONObject.fromObject(json, getDefaultJsonConfig()));
	}
	
	

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.cmct.sms.dao.MessageGiveDao.select";
	}

	@Override
	protected Map<String, Object> getListDataParam() {
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Map<String,Object> param=super.getListDataParam();
		String queryStartDate=getString("queryStartDate");
		if(null != queryStartDate && !"".equals(queryStartDate)){
			try {
				param.put("startTime", df.parse(queryStartDate.replaceAll("/", "-")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String queryEndDate=getString("queryEndDate");
		if(null != queryEndDate && !"".equals(queryEndDate)){
			try {
				param.put("endTime", df.parse(getAfter(queryEndDate.replaceAll("/", "-"))));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return param;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		return null;
	}
	
	public void getLastNum(){
		SMSControl smsControl = null;
		Person person=SystemUtil.getCurrentUser();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("controlType", SMSControlTypeEnum.PERSONAL_TYPE);//控制类型 个人
		params.put("objectId", person.getId());//控制类型 个人
		List<SMSControl> list = queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.SMSControlDao.getSMSControlByCond", params, SMSControl.class);
		if(list==null || list.size()<1){
			this.getRequest().setAttribute("lastNum", 0);
		}else{
			smsControl = list.get(0);
			if(smsControl.getBalanceStrategyFlag()){//是余额控制
				//余额控制
				this.getRequest().setAttribute("lastNum", smsControl.getBalanceAmout());
			}
			params.clear();
			params.put("senderId", person.getId());//发送人
			if(smsControl.getLimitStrategyFlag()){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date sendTime=new Date();
				if(SMSStrategyTypeEnum.BY_DAY_TYPE.equals(smsControl.getLimitStrategyType())){
					//上限控制类型 为按日
					params.put("sendDate", df.format(sendTime));
				}else{
					Date firstDay = DateUtil.getFirstDay(sendTime);
					Date lastDay = DateUtil.getLastDay(sendTime);
					params.put("queryStartDate", df.format(firstDay));
					params.put("queryEndDate", df.format(lastDay));
				}
				List<ShortMessage> msgList = queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.ShortMessageDao.getShortMessageByCond", params, ShortMessage.class);
				if(msgList!=null){
					int hasSendedCount = 0 ;
					for(ShortMessage msg:msgList){
						if(null != msg.getSendSmsCount()){
							hasSendedCount += msg.getSendSmsCount();
						}
					}
					this.getRequest().setAttribute("lastNum", (smsControl.getTopLimitAmount()-hasSendedCount));
				}
				
			}
		}
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
