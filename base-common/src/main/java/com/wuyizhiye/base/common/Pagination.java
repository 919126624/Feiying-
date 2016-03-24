package com.wuyizhiye.base.common;


import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Pagination
 * @Description 通用分页排序类
 * @author li.biao
 * @date 2015-4-1
 * @param <T>
 */
public class Pagination<T> {
	
	//默认页数
	private static final int DEFAULT_PAGE = 1;
	
	//默认每页记录数
	public static int DEFAULT_PAGE_SIZE = 20;
	
	//默认水位
	public static int DEFAULT_WATER_LINE = 0 ;
	
	//数据集合
	private List<T> items;
	
	//总记录数
	private int recordCount;
	
	//每页记录数
	private int pageSize = DEFAULT_PAGE_SIZE;
	
	//当前页
	private int currentPage = DEFAULT_PAGE;
	
	//查询水位
	private int waterLine = DEFAULT_WATER_LINE ;
	
	//执行时间
	private String exceTime;
	
	//执行SQL
	private String exceSql;
	
	/**
	 * 
	 * <p>Title: Pagination.java</p>  
	 * <p>Description: 构造函数</p>
	 */
	public Pagination(){
		
	}
	
	/**
	 * 
	 * <p>Title: Pagination.java</p>  
	 * <p>Description: 带参构造Pagination对象</p>  
	 * @param pageSize 每页记录数
	 * @param page 当前页
	 */
	public Pagination(int pageSize, int page) {
		if (pageSize < 1) {
			throw new IllegalArgumentException("Count should be greater than zero!");
		} else if (currentPage < 1) {
			page = 1;
		} else {
			this.pageSize = pageSize;
			this.currentPage = page;
		}
	}
	
	public Pagination(int pageSize, int page ,int waterLine) {
		if (pageSize < 1) {
			throw new IllegalArgumentException("Count should be greater than zero!");
		} else if (currentPage < 1) {
			page = 1;
		} else {
			this.pageSize = pageSize;
			this.currentPage = page;
			this.waterLine = waterLine;
		}
	}
	
	public String getExceTime() {
		return exceTime;
	}
	public void setExceTime(String exceTime) {
		this.exceTime = exceTime;
	}
	public String getExceSql() {
		return exceSql;
	}
	public void setExceSql(String exceSql) {
		this.exceSql = exceSql;
	}
	
	public String getCount(){
		return this.getRecordCount()+"";
	}
	
	public void setPageSize(int countOnEachPage) {
		this.pageSize = countOnEachPage;
	}

	public List<T> getItems() {
		if(null==this.items)
			this.items=new ArrayList<T>();
		return items;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public void setRecordCount(int totalCount) {
		this.recordCount = totalCount;
	}
	
	public int getPageCount() {
		return (recordCount == 0) ? 1 : ((recordCount % pageSize == 0) ? (recordCount / pageSize)
				: (recordCount / pageSize) + 1);
	}

	public int getPreviousPage() {
		if(currentPage > 1) return currentPage - 1;
		else return DEFAULT_PAGE;
	}
	
	public int getNextPage() {
		if(currentPage < getPageCount()) return currentPage + 1;
		else return getPageCount();
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getWaterLine() {
		return waterLine;
	}

	public void setWaterLine(int waterLine) {
		this.waterLine = waterLine;
	}
	
}