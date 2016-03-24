
package com.wuyizhiye.basedata.orepationlog.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.orepationlog.dao.OperationLogDao;

/**
 * @ClassName OperationLogDaoImpl
 * @Description 操作日志
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="operationLogDao")
public class OperationLogDaoImpl extends BaseDaoImpl implements OperationLogDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.orepationlog.dao.OperationLogDao";
	}
}
