package com.wuyizhiye.basedata.exporttools.dao;

import java.sql.SQLException;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.export.ExportDataSource;
import com.wuyizhiye.basedata.exporttools.model.ExportScheme;

/**
 * @ClassName ExportSchemeDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface ExportSchemeDao extends BaseDao {
	/**
	 * 根据sql和参数获取导出数据源
	 * @param sql
	 * @param param
	 * @param filters
	 * @return
	 * @throws SQLException
	 */
	ExportDataSource<Map<String, Object>> getExportDataSource(ExportScheme scheme,Map<String,String> param) throws SQLException;
}
