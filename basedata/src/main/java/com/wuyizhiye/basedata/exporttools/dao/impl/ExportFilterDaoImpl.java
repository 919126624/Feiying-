package com.wuyizhiye.basedata.exporttools.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.exporttools.dao.ExportFilterDao;

/**
 * @ClassName ExportFilterDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="exportFilterDao")
public class ExportFilterDaoImpl extends BaseDaoImpl implements ExportFilterDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.exporttools.dao.ExportFilterDao";
	}
}
