package com.wuyizhiye.base.export.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.export.DataExportor;
import com.wuyizhiye.base.export.DataField;
import com.wuyizhiye.base.export.ExportDataSource;
import com.wuyizhiye.base.export.cvs.CsvDataExportor;
import com.wuyizhiye.base.util.StringUtils;

/**
 * @ClassName ExcelDataExportor
 * @Description 描述: Excel数据导出工具,默认为CSV格式,CSV格式文件可以用excel正常打开(兼容03和07+)
 * 如果指定为Excel,则导出Excel07文件,不对Excel03进行支持
 * @author li.biao
 * @date 2015-4-1
 * @param <T>
 */
public class ExcelDataExportor<T> extends DataExportor<T> {
	
	private MODE mode;
	
	private String sheetName;
	
	public ExcelDataExportor(DataField[] fields,ExportDataSource<T> dataSource, OutputStream os) {
		this(fields, dataSource, os,MODE.CSV);
	}
	
	public ExcelDataExportor(DataField[] fields,ExportDataSource<T> dataSource, OutputStream os,MODE mode) {
		super(fields, dataSource, os);
		this.mode = mode;
	}

	@Override
	public void export() throws IOException {
		if(mode == null || mode == MODE.CSV){
			new CsvDataExportor<T>(fields, dataSource, os).export();
		}else{
			exportExcel07();
		}
	}
	
	private void exportExcel07() throws IOException{
		Pagination<T> pagination = new Pagination<T>();
		pagination.setPageSize(pageSize);
		pagination.setCurrentPage(0);
		List<T> dataList;
		SXSSFWorkbook  workBook = new SXSSFWorkbook(300); 
		int sheetIndex = 1;
		Sheet sheet = workBook.createSheet(createSheetName(sheetIndex));
		int rowIndex = 0;
		Row row = sheet.createRow(rowIndex);
		Cell cell;
		DataField field;
		createHead(row);
		Object value;
		os.flush();
		do{
			pagination.setCurrentPage(pagination.getCurrentPage()+1);
			pagination = dataSource.getData(pagination);
			dataList = pagination.getItems();
			for(T data : dataList){
				rowIndex++;
				if(rowIndex >= 1048575){
					sheetIndex ++;
					rowIndex = 0;
					sheet = workBook.createSheet(createSheetName(sheetIndex));
					row = sheet.createRow(rowIndex);
					createHead(row);
					rowIndex++;
				}
				row = sheet.createRow(rowIndex);
				int count = 0;
				if(isUseIndex()){
					cell = row.createCell(0);
					cell.setCellValue(++currentIndex);
					count++;
				}
				for(int i = 0+count; i < fields.length+count; i++){
					field = fields[i-count];
					cell = row.createCell(i);
					value = getValue(data, field.getField());
					if(field instanceof ExcelDataField && ((ExcelDataField) field).getRender()!=null){
						((ExcelDataField) field).getRender().render(row,cell,data,value);
						continue;
					}
					setCellValue(cell, value);
				}
			}
		}while(pagination.getPageSize() * pagination.getCurrentPage() <= pagination.getRecordCount());
		workBook.write(os);
		os.flush();
	}
	
	private void setCellValue(Cell cell,Object value){
		if(value == null){
			return;
		}
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(value.toString());
	}
	
	/**
	 * 将指定行创建为表头
	 * @param row
	 */
	private void createHead(Row row){
		Cell cell;
		DataField field;
		int count = 0;
		if(isUseIndex()){
			cell = row.createCell(0);
			cell.setCellValue("序号");
			count++;
		}
		for(int i = 0+count; i < fields.length+count; i++){
			field = fields[i-count];
			cell = row.createCell(i);
			if(field instanceof ExcelDataField && ((ExcelDataField) field).getHeadRender()!=null){
				((ExcelDataField) field).getHeadRender().render(row,cell,field, field.getName());
				continue;
			}
			cell.setCellValue(field.getName());
			cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
		}
	}
	
	private String createSheetName(int index){
		if(StringUtils.isEmpty(sheetName)){
			this.sheetName = "sheet";
		}
		return this.sheetName + index;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
}
