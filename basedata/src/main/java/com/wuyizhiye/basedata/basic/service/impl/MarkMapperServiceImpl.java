package com.wuyizhiye.basedata.basic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.basic.dao.MarkMapperDao;
import com.wuyizhiye.basedata.basic.model.MarkMapper;
import com.wuyizhiye.basedata.basic.service.MarkMapperService;

/**
 * @ClassName MarkMapperServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="markMapperService")
@Transactional
public class MarkMapperServiceImpl extends BaseServiceImpl<MarkMapper> implements MarkMapperService {
	@Autowired
	private MarkMapperDao markMapperDao;
	@Override
	protected BaseDao getDao() {
		return markMapperDao;
	}	
}