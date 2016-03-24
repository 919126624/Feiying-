package com.wuyizhiye.basedata.exporttools.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.export.DataExportor;
import com.wuyizhiye.base.export.cvs.CsvDataExportor;
import com.wuyizhiye.base.export.excel.ExcelDataExportor;
import com.wuyizhiye.base.export.excel.MODE;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.exporttools.dao.ExportSchemeDao;
import com.wuyizhiye.basedata.exporttools.model.ExportColumn;
import com.wuyizhiye.basedata.exporttools.model.ExportFilter;
import com.wuyizhiye.basedata.exporttools.model.ExportScheme;
import com.wuyizhiye.basedata.exporttools.service.ExportColumnService;
import com.wuyizhiye.basedata.exporttools.service.ExportFilterService;
import com.wuyizhiye.basedata.exporttools.service.ExportSchemeService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName ExportSchemeServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="exportSchemeService")
@Transactional
public class ExportSchemeServiceImpl extends DataEntityService<ExportScheme> implements ExportSchemeService {
	@Autowired
	private ExportSchemeDao exportSchemeDao;
	@Autowired
	private ExportColumnService exportColumnService;
	@Autowired
	private ExportFilterService exportFilterService;
	@Override
	protected BaseDao getDao() {
		return exportSchemeDao;
	}	
	
	@Override
	public void addEntity(ExportScheme entity) {
		entity.setId(UUID.randomUUID().toString());
		super.addEntity(entity);
		List<ExportColumn> columns = entity.getColumns();
		if(columns != null){
			int index = 0;
			for(ExportColumn c : columns){
				c.setScheme(entity);
				c.setId(UUID.randomUUID().toString());
				c.setIndex(index++);
			}
			exportColumnService.addBatch(columns);
		}
		List<ExportFilter> filters = entity.getFilters();
		if(filters != null){
			int index = 0;
			for(ExportFilter f : filters){
				f.setScheme(entity);
				f.setId(UUID.randomUUID().toString());
				f.setIndex(index++);
			}
			exportFilterService.addBatch(filters);
		}
	}
	
	@Override
	public void updateEntity(ExportScheme entity) {
		super.updateEntity(entity);
		exportColumnService.deleteById(entity.getId());
		exportFilterService.deleteById(entity.getId());
		List<ExportColumn> columns = entity.getColumns();
		if(columns != null){
			int index = 0;
			for(ExportColumn c : columns){
				c.setScheme(entity);
				c.setId(UUID.randomUUID().toString());
				c.setIndex(index++);
			}
			exportColumnService.addBatch(columns);
		}
		List<ExportFilter> filters = entity.getFilters();
		if(filters != null){
			int index = 0;
			for(ExportFilter f : filters){
				f.setScheme(entity);
				f.setId(UUID.randomUUID().toString());
				f.setIndex(index++);
			}
			exportFilterService.addBatch(filters);
		}
	}
	
	@Override
	public void deleteEntity(ExportScheme entity) {
		super.deleteEntity(entity);
		exportColumnService.deleteById(entity.getId());
		exportFilterService.deleteById(entity.getId());
	}
	
	@Override
	public void executeExport(String id, Map<String, String> param,OutputStream os,String type) throws IOException {
		if(StringUtils.isEmpty(type)){
			type = "csv";
		}
		ExportScheme scheme = getEntityById(id);
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("scheme", scheme.getId());
		scheme.setColumns(queryExecutor.execQuery("com.wuyizhiye.basedata.exporttools.dao.ExportColumnDao.select", params, ExportColumn.class));
		scheme.setFilters(queryExecutor.execQuery("com.wuyizhiye.basedata.exporttools.dao.ExportFilterDao.select", params, ExportFilter.class));
		ExportColumn[] columns = new ExportColumn[scheme.getColumns().size()];
		columns = scheme.getColumns().toArray(columns);
		try {
			DataExportor<Map<String,Object>> exportor = null;
			if("csv".equals(type) || "txt".equals(type)){
				exportor = new CsvDataExportor<Map<String,Object>>(columns, exportSchemeDao.getExportDataSource(scheme, param), os);
			}else{
				exportor = new ExcelDataExportor<Map<String,Object>>(columns, exportSchemeDao.getExportDataSource(scheme, param), os, MODE.EXCEL);
			}
			exportor.setUseIndex(scheme.getUseIndex());
			exportor.export();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}