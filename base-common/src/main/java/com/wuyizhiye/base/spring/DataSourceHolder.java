package com.wuyizhiye.base.spring;


/**
 * 数据源存放类
 * @ClassName DataSourceHolder
 * @Description TODO
 * @author li.biao
 * @date 2015-4-1
 */
public class DataSourceHolder {
	private static final ThreadLocal<String> currentDataSource = new ThreadLocal<String>();
	private static final String DATA_SOURCE = "DATA_SOURCE";
	public static void setDataSource(String dataSource) {
		currentDataSource.set(dataSource);
	}

	/**
	 * 得到当前线程数据源
	 * @return	当前数据源
	 */
	public static String getDataSource() {
		return currentDataSource.get();
	}

	public static void clear() {
		currentDataSource.remove();
	}
}
