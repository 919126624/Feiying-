package com.wuyizhiye.basedata.code.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.code.dao.RecordDao;
import com.wuyizhiye.basedata.code.model.Record;
import com.wuyizhiye.basedata.code.service.RecordService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName RecordServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="recordService")
public class RecordServiceImpl extends DataEntityService<Record> implements RecordService{
	@Autowired
	private RecordDao recordDao;
	@Override
	protected BaseDao getDao() {
		return recordDao;
	}
}
