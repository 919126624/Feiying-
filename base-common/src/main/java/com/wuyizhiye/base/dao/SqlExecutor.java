package com.wuyizhiye.base.dao;

import java.util.List;

/**
 * @ClassName SqlExecutor
 * @Description SQL执行器
 * @author li.biao
 * @date 2015-4-1
 */
public interface SqlExecutor {
	
	/**
	 * @Title executeSqls
	 * @Description 执行多条SQL
	 * @param sqls
	 * @param exception
	 * @return void
	 * @throws
	 */
	void executeSqls(List<String> sqls,boolean exception);
}
