package com.wuyizhiye.basedata.orepationlog.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.operationlog.model.OperationLog;

/**
 * @ClassName OperationLogService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface OperationLogService extends BaseService<OperationLog>{
	/**
	 * 根据月份生成对应的表   例如：T_BD_OPERATIONLOG_2014_11
	 */
	void taskCreateTable() throws Exception;
}
