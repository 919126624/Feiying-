package com.wuyizhiye.basedata.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.model.BaseConfig;

/**
 * @ClassName LoginHolder
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="loginHolder")
@Lazy(value=false)
public class LoginHolder {
	
	@Resource(name="queryExecutor")
	protected QueryExecutor queryExecutor;
	
	@Autowired
	private ApplicationContextAware app;
	
	private static final Map<String,Map<String,String>> ConfigMap = new HashMap<String,Map<String,String>>();
	private static Logger log=Logger.getLogger(LoginHolder.class); 
	
	@PostConstruct
	public void init(){
		List<String> dslist = SystemUtil.getDataSourceSingleList();
		for(int i=0;i<dslist.size();i++){
		//设置数据中心
		DataSourceHolder.setDataSource(dslist.get(i));
		Map<String,String> configtemp = new HashMap<String,String>();
		
		List<BaseConfig> cflist = null;
		try{
		cflist = (List<BaseConfig>)queryExecutor.execQuery("com.wuyizhiye.basedata.basic.dao.BaseConfigDao.select",null, BaseConfig.class);
		for(int j=0;j<cflist.size();j++){
			BaseConfig temp = cflist.get(j);			
			configtemp.put(temp.getNumber(), temp.getParamValue());			
			
		}
		}catch(Exception e){
			log.error(e.getMessage());
		}
		
		ConfigMap.put(dslist.get(i), configtemp);
		
		}
		
		
	}
	
	/**
	 * 刷新当前数据中心数据
	 */
	public  void refreshData(){
		
		String curDataSource = DataSourceHolder.getDataSource(); 		
		
		Map<String,String> configtemp = new HashMap<String,String>();
		
		List<BaseConfig> cflist = null;
		try{
		cflist = (List<BaseConfig>)queryExecutor.execQuery("com.wuyizhiye.basedata.basic.dao.BaseConfigDao.select",null, BaseConfig.class);
		for(int j=0;j<cflist.size();j++){
			BaseConfig temp = cflist.get(j);			
			configtemp.put(temp.getNumber(), temp.getParamValue());			
			
		}
		}catch(Exception e){
			log.error(e.getMessage());
		}
		
		ConfigMap.put(curDataSource, configtemp);
		
		DataSourceHolder.setDataSource(curDataSource);
	}
	
	/**
	 * 得到当前数据中心的基础配置 若当前没有数据中心取第一个
	 * @return
	 */
	public static Map<String,String> getCurrBaseConfig(){
		if(StringUtils.isEmpty(DataSourceHolder.getDataSource())){
			Set<String> dset = ConfigMap.keySet();
			for(String e : dset){
				if(!ConfigMap.get(e).isEmpty())
				return ConfigMap.get(e);
			}
			return null;
		}
		return ConfigMap.get(DataSourceHolder.getDataSource());
	}
	
	public static Map<String,Map<String,String>> getAllBaseConfig(){
		return ConfigMap;
	}
}
