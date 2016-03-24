package com.wuyizhiye.basedata.exporttools.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.exporttools.dao.ExportColumnDao;
import com.wuyizhiye.basedata.exporttools.model.ExportColumn;
import com.wuyizhiye.basedata.exporttools.service.ExportColumnService;

/**
 * @ClassName ExportColumnServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="exportColumnService")
@Transactional
public class ExportColumnServiceImpl extends BaseServiceImpl<ExportColumn> implements ExportColumnService {
	@Autowired
	private ExportColumnDao exportColumnDao;
	@Override
	protected BaseDao getDao() {
		return exportColumnDao;
	}	
}