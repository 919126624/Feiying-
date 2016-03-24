package com.wuyizhiye.basedata.orepationlog.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.dao.impl.QueryExecutorImpl;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.operationlog.model.OperationLog;
import com.wuyizhiye.basedata.orepationlog.dao.OperationLogDao;
import com.wuyizhiye.basedata.orepationlog.service.OperationLogService;
import com.wuyizhiye.basedata.param.model.ParamLines;
import com.wuyizhiye.basedata.util.OperationLogUtil;

/**
 * @ClassName OperationLogServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="operationLogService")
public class OperationLogServiceImpl extends BaseServiceImpl<OperationLog> implements OperationLogService {
	
	@Resource
	private OperationLogDao operationLogDao;
	
	@Override
	protected BaseDao getDao() {
		return operationLogDao;
	}

	@Override
	public void taskCreateTable()  throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tableName", "T_BD_OPERATIONLOG"+OperationLogUtil.getSuffix(new Date()));
		map.put("paramnumber", "DataBaseType");
		ParamLines paramLines = QueryExecutorImpl.getInstance().execOneEntity("com.wuyizhiye.basedata.param.dao.ParamLinesDao.selectByTypeNumber", map, ParamLines.class);
		String dataBaseType = paramLines.getParamValue();
		if(dataBaseType.equals("ORACLE")){
			QueryExecutorImpl.getInstance().executeUpdate("com.wuyizhiye.basedata.orepationlog.dao.OperationLogDao.createTable", map);
		}else{
			QueryExecutorImpl.getInstance().executeUpdate("com.wuyizhiye.basedata.orepationlog.dao.OperationLogDao.createTableOfMysql", map);
		}
	}

}
