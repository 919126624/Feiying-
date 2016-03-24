package com.wuyizhiye.basedata.exporttools.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.exporttools.dao.ExportFilterDao;
import com.wuyizhiye.basedata.exporttools.model.ExportFilter;
import com.wuyizhiye.basedata.exporttools.service.ExportFilterService;

/**
 * @ClassName ExportFilterServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="exportFilterService")
@Transactional
public class ExportFilterServiceImpl extends BaseServiceImpl<ExportFilter> implements ExportFilterService {
	@Autowired
	private ExportFilterDao exportFilterDao;
	@Override
	protected BaseDao getDao() {
		return exportFilterDao;
	}	
}