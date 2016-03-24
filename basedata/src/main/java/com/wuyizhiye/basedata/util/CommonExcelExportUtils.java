package com.wuyizhiye.basedata.util;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * @ClassName CommonExcelExportUtils
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class CommonExcelExportUtils {

	/**
	 * 
	 * @param dataList 数据集合
	 * @param excelHeader 表头名称
	 * @param excelField 属性名称
	 * @param os 输出流
	 * @param sheetName 
	 * @param isUseIndex 是否要序号
	 * @throws Exception 
	 */
	public static <T> void exportExcelByDataList(List<T> dataList,String[] excelHeader,String[] excelField,
			OutputStream os,String sheetName,boolean isUseIndex,String dateFormater) throws Exception{
		if(dataList==null || dataList.size()<1){
			os.write("没有可导出的考勤信息".getBytes());
			return ;
		}
		SimpleDateFormat df = new SimpleDateFormat(dateFormater);
		SXSSFWorkbook  workBook = new SXSSFWorkbook(); 
		Sheet sheet = workBook.createSheet(sheetName);
		int rowIndex = 0;
		Row row = sheet.createRow(rowIndex);
		Cell cell;
		String fieldName;
		createHead(row,excelHeader,isUseIndex);
		Object value;
		os.flush();
		for(T data :dataList){
			rowIndex++;
			row = sheet.createRow(rowIndex);
			int count = 0;
			if(isUseIndex){
				cell = row.createCell(0);
				cell.setCellValue(rowIndex);
				count++;
			}
			for(int i = 0+count; i < excelField.length+count; i++){
				fieldName = excelField[i-count];
				cell = row.createCell(i);
				String methodName = "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
				value = data.getClass().getMethod(methodName).invoke(data);
				if(value!=null && value instanceof Date){
					value = df.format(value);
				} 
				setCellValue(cell, value);
			}
			
		} 
		workBook.write(os);
		os.flush();
	}
	
	private static void setCellValue(Cell cell,Object value){
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
	private static void createHead(Row row,String[] excelHeader,boolean isUseIndex){
		Cell cell;
		String headerName;
		int count = 0;
		if(isUseIndex){
			cell = row.createCell(0);
			cell.setCellValue("序号");
			count++;
		}
		for(int i = 0+count; i < excelHeader.length+count; i++){
			headerName = excelHeader[i-count];
			cell = row.createCell(i);
			 
			cell.setCellValue(headerName);
			cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
		}
	}
}
