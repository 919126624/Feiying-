package com.wuyizhiye.base.spring;

import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @ClassName RoutingDataSource
 * @Description 动态数据源
 * @author li.biao
 * @date 2015-4-1
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

	private Object defaultSource;
	private Map<Object, Object> targetDataSourcesMap ;
	private String defaultSourceKey;
	
	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceHolder.getDataSource();
	}
	
	
	public void setDefaultTargetDataSource(Object defaultTargetDataSource){
		super.setDefaultTargetDataSource(defaultTargetDataSource);
		defaultSource = defaultTargetDataSource;
		
	}
	
	public void setTargetDataSources(Map<Object, Object> targetDataSources){
	    super.setTargetDataSources(targetDataSources);
	    targetDataSourcesMap = targetDataSources;
	    
	}
	
	/**
	 * 获取配置中默认的数据源
	 * @return
	 */
	public String getDefaultSourceKey(){
		if(null==defaultSourceKey){
			for (Map.Entry entry : this.targetDataSourcesMap.entrySet()) {
				if(entry.getValue().equals(defaultSource)){
					defaultSourceKey = entry.getKey().toString();
					break;
				}
			}		
		}
		return defaultSourceKey;
	}


	public Object getDefaultSource() {
		return defaultSource;
	}


	public void setDefaultSource(Object defaultSource) {
		this.defaultSource = defaultSource;
	}


	public Map<Object, Object> getTargetDataSourcesMap() {
		return targetDataSourcesMap;
	}


	public void setTargetDataSourcesMap(Map<Object, Object> targetDataSourcesMap) {
		this.targetDataSourcesMap = targetDataSourcesMap;
	}
}
