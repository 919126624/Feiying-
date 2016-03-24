package com.wuyizhiye.base.export.cvs;

import java.io.OutputStream;

import com.wuyizhiye.base.export.DataField;
import com.wuyizhiye.base.export.ExportDataSource;
import com.wuyizhiye.base.export.txt.TxtDataExportor;

/**
 * @ClassName CsvDataExportor
 * @Description 描述: csv格式数据导出工具
 * @author li.biao
 * @date 2015-4-1
 * @param <T>
 */
public class CsvDataExportor<T> extends TxtDataExportor<T> {
	
	public CsvDataExportor(DataField[] fields, ExportDataSource<T> dataSource,OutputStream os) {
		super(fields, dataSource, os,",");
	}
	
}
