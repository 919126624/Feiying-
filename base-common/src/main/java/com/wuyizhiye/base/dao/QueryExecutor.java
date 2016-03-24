package com.wuyizhiye.base.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.common.Pagination;

/**
 * @ClassName QueryExecutor
 * @Description 查询执行工具
 * @author li.biao
 * @date 2015-4-1
 */
public interface QueryExecutor {

	/**
	 * @Title execQuery
	 * @Description 普通分页查询
	 * @param <T>
	 * @param mapper
	 * @param pagination
	 * @param param
	 * @return
	 * @return Pagination<T>
	 * @throws
	 */
	<T> Pagination<T> execQuery(String mapper,Pagination<T> pagination,Map<String,Object> param);
	
	/**
	 * @Title execQuery
	 * @Description 查询所有数据(数据量大时，慎用)
	 * @param @param <T>
	 * @param @param mapper
	 * @param @param param
	 * @param @param type
	 * @param @return
	 * @return List<T>
	 * @throws
	 */
	<T> List<T> execQuery(String mapper,Map<String,Object> param,Class<T> type);
	
	/**
	 * @Title execOneEntity
	 * @Description 返回单个实体
	 * @param @param <T>
	 * @param @param mapper
	 * @param @param param
	 * @param @param type
	 * @param @return
	 * @return T
	 * @throws
	 */
	public <T> T execOneEntity(String mapper,Map<String,Object> param,Class<T> type);
	
	/**
	 * @Title execCount
	 * @Description 查询数量
	 * @param @param mapper
	 * @param @param param
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int execCount(String mapper, Map<String, Object> param);
	
	/**
	 * @Title executeInsert
	 * @Description 执行Insert
	 * @param @param mapper
	 * @param @param param
	 * @return void
	 * @throws
	 */
	public void executeInsert(String mapper,Object param);
	
	/**
	 * @Title executeUpdate
	 * @Description 执行update
	 * @param @param mapper
	 * @param @param param
	 * @return void
	 * @throws
	 */
	public void executeUpdate(String mapper,Object param);
	
	/**
	 * @Title executeDelete
	 * @Description 执行delete
	 * @param @param mapper
	 * @param @param param
	 * @return void
	 * @throws
	 */
	public void executeDelete(String mapper,Object param);
	
	/**
	 * @Title getExecuteSql
	 * @Description 获得执行的SQL
	 * @param @param mapper
	 * @param @param param
	 * @param @return
	 * @return String
	 * @throws
	 */
	public  String getExecuteSql(String mapper,Map<String,Object> param);
}
