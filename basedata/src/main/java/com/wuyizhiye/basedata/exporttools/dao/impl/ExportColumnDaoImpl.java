package com.wuyizhiye.basedata.exporttools.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.exporttools.dao.ExportColumnDao;

/**
 * @ClassName ExportColumnDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="exportColumnDao")
public class ExportColumnDaoImpl extends BaseDaoImpl implements ExportColumnDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.exporttools.dao.ExportColumnDao";
	}
}
