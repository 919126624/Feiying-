package com.wuyizhiye.basedata.basic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.basic.dao.MarkDao;
import com.wuyizhiye.basedata.basic.model.Mark;
import com.wuyizhiye.basedata.basic.service.MarkService;

/**
 * @ClassName MarkServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="markBasicService")
@Transactional
public class MarkServiceImpl extends BaseServiceImpl<Mark> implements MarkService {
	@Autowired
	private MarkDao markDao;
	@Override
	protected BaseDao getDao() {
		return markDao;
	}
	@Override
	public String getMarkCode(String numberM) {
		return markDao.getMarkCode(numberM);
	}
	
	public void updateEntity2(Mark entity,List<Mark> marks) {
		markDao.updateEntity(entity);
		markDao.updateBatch(marks);
	}
}