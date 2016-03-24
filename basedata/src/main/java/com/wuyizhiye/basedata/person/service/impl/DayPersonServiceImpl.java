package com.wuyizhiye.basedata.person.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.dao.DayPersonDao;
import com.wuyizhiye.basedata.person.model.DayPerson;
import com.wuyizhiye.basedata.person.service.DayPersonService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName DayPersonServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="dayPersonService")
@Transactional
public class DayPersonServiceImpl extends DataEntityService<DayPerson> implements DayPersonService {

	@Autowired
	private DayPersonDao dayPersonDao;

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
	    		savePersonData();//  同步员工数据
	    	}
	    }
	}
	
	/**
	 * 定时器 同步人员数据
	 */
	@SuppressWarnings("rawtypes")
	public void savePersonData(){
		try{
			 List<DayPerson> psList=this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.DayPersonDao.selectBackupDataForPerson", null, DayPerson.class);
			 if(psList!=null && psList.size()>0){
				 for(DayPerson p:psList){
					 p.setId(UUID.randomUUID().toString());
					 p.setGenDay(StringUtils.getCurrdate("yyyy-MM-dd")); 
					 dayPersonDao.addEntity(p);
				 }
			 }
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	protected BaseDao getDao() {
		return dayPersonDao;
	}	
}