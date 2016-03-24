package com.wuyizhiye.cmct.phone.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.dao.impl.QueryExecutorImpl;
import com.wuyizhiye.basedata.param.model.ParamLines;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao;
import com.wuyizhiye.cmct.phone.model.PhoneDialRecord;
import com.wuyizhiye.cmct.phone.service.PhoneDialRecordService;
/**
 * @ClassName PhoneDialRecordServiceImpl
 * @Description 内网平台 呼叫中心 呼叫记录 业务实现
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneDialRecordService")
public class PhoneDialRecordServiceImpl extends DataEntityService<PhoneDialRecord> implements PhoneDialRecordService {

	@Autowired
	private PhoneDialRecordDao phoneDialRecordDao ;
	
	@Override
	protected BaseDao getDao() {
		return phoneDialRecordDao;
	}

	@Override
	public void addEntity(PhoneDialRecord entity) {
		try{
			super.addEntity(entity);
		}catch(Exception e){
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("paramnumber", "DataBaseType");
			ParamLines paramLines = QueryExecutorImpl.getInstance().execOneEntity("com.wuyizhiye.basedata.param.dao.ParamLinesDao.selectByTypeNumber", param, ParamLines.class);
			String dataBaseType = paramLines.getParamValue();
			if(dataBaseType.equals("ORACLE")){
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createTableOfOracle", entity);
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createIndexCallIdOfOracle", entity);
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createIndexOrgIdOfOracle", entity);
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createIndexCallTimeOfOracle", entity);
			}else{
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createTableOfMysql", entity);
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createIndexCallIdOfMySql", entity);
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createIndexOrgIdOfMySql", entity);
				queryExecutor.executeUpdate(PhoneDialRecord.MAPPER+".createIndexCallTimeOfMySql", entity);
			}
			addEntity(entity);
		}
	}
	
}
