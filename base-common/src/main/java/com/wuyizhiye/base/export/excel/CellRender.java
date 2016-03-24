package com.wuyizhiye.base.export.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * @ClassName CellRender
 * @Description 描述: 表格渲染
 * @author li.biao
 * @date 2015-4-1
 */
public interface CellRender {
	
	void render(Row row,Cell cell,Object rowdata,Object fieldValue);
}
