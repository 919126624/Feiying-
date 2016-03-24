/**
 * com.wuyizhiye.hr.affair.service.impl.AttendanceRuleServiceImpl.java
 */
package com.wuyizhiye.hr.attendance.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.basedata.info.dao.InfomationDao;
import com.wuyizhiye.basedata.info.enums.InfomationStatusEnum;
import com.wuyizhiye.basedata.info.model.InfoTypeEnum;
import com.wuyizhiye.basedata.info.model.Infomation;
import com.wuyizhiye.basedata.info.model.RemindTypeEnum;
import com.wuyizhiye.basedata.info.service.InfocenterService;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.hr.attendance.dao.AttendanceRuleDao;
import com.wuyizhiye.hr.attendance.model.AttendanceRule;
import com.wuyizhiye.hr.attendance.service.AttendanceRuleService;
import com.wuyizhiye.hr.enums.AttendanceRuleTypeEnum;

/**
 * 考勤规则
 * @author ouyangyi
 * @since 2013-11-20 上午10:21:33
 */
@Component(value="attendanceRuleService")
@Transactional
public class AttendanceRuleServiceImpl extends BaseServiceImpl<AttendanceRule> implements AttendanceRuleService {
	
	private Logger log = Logger.getLogger(AttendanceRuleService.class);
	@Resource
	private AttendanceRuleDao attendanceRuleDao;
	
	@Resource
	private InfomationDao infomationDao;
	@Resource
	private InfocenterService infocenterService;
	
	@Override
	protected BaseDao getDao() {
		return attendanceRuleDao;
	}
	
	@Override
	public void execute(){
		 
		//获取环境下所有的数据源
		List<String> dclist = getDataSourceSingleList();
		for(int i=0;i<dclist.size();i++){
			String dc = dclist.get(i);
			TaskThread taskThred = new TaskThread(dc);
			taskThred.start() ;
		}
	}
	
	/**
	 * 得到数据中心列表 返回list
	 */
	private List<String> getDataSourceSingleList(){
		ApplicationContext ctx = ApplicationContextAware.getApplicationContext();
		String[] beanNames = ctx.getBeanNamesForType(BasicDataSource.class);
		List<String> names = new ArrayList<String>();
		for(String n : beanNames){
			names.add(n);
		}
		return names;
	}
	
	/**
	 * 线程实现各个数据源同步
	 * @author li.biao
	 * @since 2013-11-5
	 */
	class TaskThread extends Thread { 
		private String dataCenter = "" ;
		public TaskThread(String dc) {
			this.dataCenter = dc ;
	    }  
	    public void run() { 
	    	if(!StringUtils.isEmpty(dataCenter)){
	    		DataSourceHolder.setDataSource(dataCenter);	
	    		saveAttendanceInfo();// 新增考勤提醒
	    	}
	    }
	}
	
	/**
	 * 定点执行  新增考勤提醒
	 */
	@SuppressWarnings("rawtypes")
	public void saveAttendanceInfo(){
		try{
			Map<String ,Object> param = new HashMap<String,Object>();
			param.put("recordDate",DateUtil.convertStrToDate(DateUtil.convertDateToStr(new Date(), DateUtil.GENERAL_FORMATTER), DateUtil.GENERAL_FORMATTER));
			List<Map> personList = queryExecutor.execQuery(AttendanceRule.Mapper+".getPersonByRule", param, Map.class);
			if(personList==null || personList.size()<1){
				return ;
			}
			for(Map obj :personList){
				String ruleId = obj.get("id").toString();
				String personId = obj.get("personId").toString();
				String personName = obj.get("personName").toString();
				String ruleType = obj.get("ruleType").toString();
				String hourStr = obj.get("hour").toString();
				String minuteStr = obj.get("minute").toString();
				int hour = Integer.valueOf(hourStr);
				int minute = Integer.valueOf(minuteStr);
				Calendar cal = Calendar.getInstance();
				Calendar nowCal = Calendar.getInstance();
				int week = nowCal.get(Calendar.DAY_OF_WEEK);
				
				if(AttendanceRuleTypeEnum.FIVE.toString().equals(ruleType) && (week==1 || week==7)){
					//五天制  星期六和星期天不用提醒
					  continue ;
				 }else if(AttendanceRuleTypeEnum.SIX.toString().equals(ruleType) && week==1){
					//六天制   星期天不用提醒
					  continue ;
				 } 
				
				if(cal.get(Calendar.HOUR_OF_DAY)>hour || (cal.get(Calendar.HOUR_OF_DAY)==hour && cal.get(Calendar.MINUTE)>=minute)){
					cal.set(Calendar.HOUR_OF_DAY, hour);
					cal.set(Calendar.MINUTE, minute);
					cal.set(Calendar.SECOND, 0);
//					Person p = new Person();
//					p.setId(personId);
//					Infomation infomation = new Infomation();
//					infomation.setCreateDate(cal.getTime());
//					infomation.setContent("温馨提示："+personName+" 您好,您今天还没有录考勤！");
//					infomation.setModuleId(ruleId);
//					infomation.setModuleType("HR_ATTENDANCE");
//					infomation.setPerson(p);
//					infomation.setStatus(InfomationStatusEnum.NO_WARN);
//					infomationDao.addEntity(infomation);
					infocenterService.insertInfocenter("考勤", InfoTypeEnum.SYSTEM, "温馨提示： 您好,您今天还没有录考勤！", RemindTypeEnum.FLOAT_BOX, personId, personId, "", "url");
				}
			}
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}

	@Override
	public void addEntity(AttendanceRule entity) {
		if(null!=entity.getOrg()){
			entity.setControlUnit(OrgUtils.getCUByOrg(entity.getOrg().getId()));
		}
		super.addEntity(entity);
	}

	@Override
	public void updateEntity(AttendanceRule entity) {
		if(null!=entity.getOrg()){
			entity.setControlUnit(OrgUtils.getCUByOrg(entity.getOrg().getId()));
		}
		super.updateEntity(entity);
	}
	
	
}
