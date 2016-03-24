package com.wuyizhiye.basedata.info.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.info.model.InfoCenterUnread;
import com.wuyizhiye.basedata.info.model.InfoTypeEnum;
import com.wuyizhiye.basedata.info.model.Remind;
import com.wuyizhiye.basedata.info.model.RemindTypeEnum;
import com.wuyizhiye.basedata.info.service.InfoCenterUnreadService;
import com.wuyizhiye.basedata.info.service.InfocenterService;
import com.wuyizhiye.basedata.info.service.RemindTaskService;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonService;

/**
 * @ClassName RemindTaskSerivceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="remindTaskService")
public class RemindTaskSerivceImpl implements RemindTaskService{
	private Logger logger= Logger.getLogger(InfocenterServiceImpl.class);
	@Autowired
	private QueryExecutor queryExecutor;
	
	@Autowired
	private InfocenterService infocenterService;
	
	@Autowired
	private PersonService personService;
	 
	@Autowired
	private InfoCenterUnreadService infoCenterUnreadService;
	
	
	
	public void executeGo(Calendar cal) {
		int month = cal.get(Calendar.MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int minute = cal.get(Calendar.MINUTE);
		int year = cal.get(Calendar.YEAR);
		
		Map<String,Object> params = new HashMap<String, Object>();
		List<Remind> remindList = queryExecutor.execQuery("com.wuyizhiye.basedata.info.dao.InfocenterDao.selectRemind", params, Remind.class);
		Map<String,Remind> notReadMap=new HashMap<String, Remind>(); //未读系统消息map
		for(Remind remind : remindList){
			Date time = remind.getTime();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);
			int months = calendar.get(Calendar.MONTH);
			int days = calendar.get(Calendar.DAY_OF_MONTH);
			int hours = calendar.get(Calendar.HOUR_OF_DAY);
			int minutes = calendar.get(Calendar.MINUTE);
			int years = calendar.get(Calendar.YEAR);
			if(month == months && day == days && hour == hours && minute == minutes && year == years){
				queryExecutor.executeUpdate("com.wuyizhiye.basedata.info.dao.InfocenterDao.updateRemindRead", remind);
				if(remind.getSystem()==1){
					String [] personIdArr = remind.getPersonIds().split(",");
					for(String personId:personIdArr){
						infocenterService.insertInfocenter(remind.getTitle(),  InfoTypeEnum.BUSINESS,remind.getContent(),
								RemindTypeEnum.FLOAT_BOX, personId, "", remind.getObjId(), remind.getUrl());
						notReadMap.put(personId, remind);//将需要做系统消息提醒的  已人员ID为KEY  提醒对象为value存入MAP中  只存最后一条
					}
					 
				}
				if(remind.getSms()==1){
					
				}
				if(remind.getWeixin()==1){
					try {
						String [] personIdArr = remind.getPersonIds().split(",");
						for(String personId:personIdArr){
							Person person = personService.getEntityById(personId);
							String path = SystemConfig.getParameter("path");
							HttpClientUtil.callHttpsUrl(path+"/weixinapi/sendMessage?m_teyp=text&m_opiden="+person.getNumber()+"&m_module=mycustomer&m_contten="+
									URLEncoder.encode(remind.getContent(),"utf-8"), "");
						}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						logger.error("", e);
					} 
				}
				
			}
		}
		//循环更新未读消息表
		if(!notReadMap.isEmpty()){
			Set<String> keys = notReadMap.keySet();
			Iterator<String> it = keys.iterator();
			while(it.hasNext()){
				String key = it.next();
				Remind r=notReadMap.get(key);
				 
				InfoCenterUnread unread=new InfoCenterUnread();
				unread.setId(key);
				unread.setLastContent(r.getContent());
				unread.setLastTitle(r.getContent());
				unread.setNeedOpenDialog(1);
				infoCenterUnreadService.saveNotRead(unread);
			}
		}
	}
	
	@Override
	public void execute() {
			//获取环境下所有的数据源
		List<String> dclist = getDataSourceSingleList();
		for(int i=0;i<dclist.size();i++){
			String dc = dclist.get(i);
			TaskThread taskThred = new TaskThread(dc,Calendar.getInstance());
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
		private Calendar cal;
		private String dataCenter = "" ;
		public TaskThread(String dc,Calendar cal) {
			this.dataCenter = dc ;
			this.cal = cal;
	    }  
	    public void run() { 
	    	if(!StringUtils.isEmpty(dataCenter)){
	    		DataSourceHolder.setDataSource(dataCenter);	
				executeGo(cal);
	    	}
	    }
	}

}
