package com.wuyizhiye.basedata.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook;

import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.service.impl.DeleteBarCodeServiceImpl;

/**
 * @ClassName ExcelExportUtil
 * @Description 数据导出工具类
 * @author li.biao
 * @date 2015-4-2
 */
public class ExcelExportUtil {
	private static Logger logger=Logger.getLogger(DeleteBarCodeServiceImpl.class);
	/**
	 * 导出Excel，可以放自定义信息的
	 * @param headers 列头
	 * @param properties 字段
	 * @param totalCells 自定义行
	 * @param data 
	 * @param os
	 * @throws IOException
	 */
	public static void export(String[] headers ,String[] properties ,String[] totalCells,List data ,OutputStream os )throws IOException{
		//创建工作薄
		SXSSFWorkbook workBook = new SXSSFWorkbook(300);
		
		//创建 sheet
		Sheet sheet = workBook.createSheet();
		
		if(headers == null || headers.length == 0 || properties == null || properties.length == 0 ){
			return ;
		}
		
		//设置Excel的头部
		setHeadRow(sheet,headers);
		
		//设置Excel的行列数据
		setRowData(sheet, properties, data);
		
		//汇总
		if(totalCells!=null && totalCells.length > 0){
			setTotalRow(sheet, totalCells, data);
		}
		
		//输出
		workBook.write(os);
		os.flush();
		
	}
	
	public static void exportMap(String[] headers ,String[] properties,List<Map<String,Object>> data ,OutputStream os )throws IOException{
		
		//创建工作薄
		SXSSFWorkbook workBook = new SXSSFWorkbook(300);
		
		//创建 sheet
		Sheet sheet = workBook.createSheet();
		
		if(headers == null || headers.length == 0 || properties == null || properties.length == 0 ){
			return ;
		}
		
		//设置Excel的头部
		setHeadRow(sheet,headers);
		
		//设置Excel的行列数据
		setRowDataMap(sheet, properties, data);
		
		//输出
		workBook.write(os);
		os.flush();
		
	}
	
	/**
	 * 导出Excel
	 * @param headers 列头
	 * @param properties 字段
	 * @param data
	 * @param os
	 * @throws IOException
	 */
	public static void export(String[] headers ,String[] properties ,List data ,OutputStream os )throws IOException{
		export(headers, properties,null, data, os);
	}
	
	
	/**
	 * 设置数据(支持.符号  多少个点都行)
	 * @param properties
	 * @param data
	 */
	private static void setRowDataMap(Sheet sheet ,String[] properties,List<Map<String,Object>> data){
		int i = 0 ;
		for(Map<String,Object> bean : data){
			//创建行
			Row row = sheet.createRow(i + 1);
			//遍历属性创建单元格
			int j = 0 ;
			for(String property : properties){
				try {
					Cell cell = row.createCell(j);
					if(null != bean.get(property)){
						String cellValue = bean.get(property).toString();
						cell.setCellValue(cellValue);
					}else{
						cell.setCellValue("");
					}
					
				} catch (Exception e) {
					logger.error("", e);
				} 
				j ++ ;
			}
			i++ ;
		}
	}
	
	/**
	 * 设置数据(支持.符号  多少个点都行)
	 * @param properties
	 * @param data
	 */
	private static void setRowData(Sheet sheet ,String[] properties,List data){
		int i = 0 ;
		for(Object bean : data){
			//创建行
			Row row = sheet.createRow(i + 1);
			//遍历属性创建单元格
			int j = 0 ;
			for(String property : properties){
				try {
					Cell cell = row.createCell(j);
					String cellValue = "" ;
					if(property.indexOf(".")>0){
						String[] params=property.split("\\.");
						Method getMethod =null;
						Object value=null;
						for(int n=0;n<params.length;n++){
							 if(n==0){
								 getMethod = bean.getClass().getMethod(StringUtils.getMethodName("get",params[n])); 
								 value = getMethod.invoke(bean);
							 }else if(n<params.length-1){
								 getMethod = value.getClass().getMethod(StringUtils.getMethodName("get",params[n])); 
								 value = getMethod.invoke(value);
							 } 
						} 
						if(value!=null){
							cellValue=getValueByProperty(value, params[params.length-1]);
						}
						/*String obj=property.split("\\.")[0];
						String pp=property.split("\\.")[1];
						Method getMethod = bean.getClass().getMethod(StringUtils.getMethodName("get",obj));
						Object value = getMethod.invoke(bean);
						if(value!=null){
							cellValue=getValueByProperty(value, pp);
						}*/
					}else{
						cellValue=getValueByProperty(bean, property);
					}
					
					/*Method getMethod = bean.getClass().getMethod(StringUtils.getMethodName("get",property));
					//获取方法返回类型
					Object value = getMethod.invoke(bean);
					if(value!=null){
						if(value instanceof Date){
							Date dv = (Date)value ;
							cellValue = DateUtil.convertDateToStr(dv);
						}else if(value instanceof Enum){
							Method enumGetMenthod = value.getClass().getMethod("getName");
							Object enumValue = enumGetMenthod.invoke(value);
							cellValue = enumValue == null ? "" : enumValue.toString();
						}else{
							cellValue = value.toString();
						}
					}else{
						cellValue = "" ;
					}*/
					
					cell.setCellValue(cellValue);
				} catch (Exception e) {
					logger.error("", e);
				} 
				j ++ ;
			}
			i++ ;
		}
	}
	
    public static String getValueByProperty(Object obj,String property) throws Exception{
    	String val="";
    	String pp=property.indexOf("[")>0?property.split("\\[")[0]:property;
    	String fmt=property.indexOf("[")>0?property.split("\\[")[1].replace("]", ""):""; 
    	
    	Method getMethod = obj.getClass().getMethod(StringUtils.getMethodName("get",pp));
		//获取方法返回类型
		Object value = getMethod.invoke(obj);
		if(value!=null){
			if(value instanceof Date){
				Date dv = (Date)value ;
				if(StringUtils.isEmpty(fmt)){ 
					val = DateUtil.convertDateToStr(dv);
				}else{
					SimpleDateFormat format=new SimpleDateFormat(fmt);
					val=format.format(dv); 
				}
			}else if(value instanceof Enum){
				Method enumGetMenthod = value.getClass().getMethod("getName");
				Object enumValue = enumGetMenthod.invoke(value);
				val = enumValue == null ? "" : enumValue.toString();
			}else if(!StringUtils.isEmpty(fmt) && "phonefmt".equals(fmt) ){ //格式化电话号码 
				if(value.toString().length()>=8){
					val=value.toString().replace(value.toString().substring(3,8),"****");
				}else{
					val = value.toString();
				}
			}else{
				val = value.toString();
			}
		}else{
			val = "" ;
		}
    	
    	return val;
    }
	
	/**
	 * 创建列头
	 * @param sheet
	 * @param headers
	 */
	private static void setHeadRow(Sheet sheet , String[] headers){
		// 创建第一行
		Row row = sheet.createRow(0);
		int i = 0 ;
		for(String head : headers){
			Cell cell = row.createCell(i);
			cell.setCellValue(head);
			i++ ;
		}
	}
	
	/**
	 * 创建汇总行
	 * @param sheet
	 * @param headers
	 */
	private static void setTotalRow(Sheet sheet , String[] totalCells ,List data){
		// 创建行
		Row row = sheet.createRow(data.size()+2);
		int i = 0 ;
		for(String total : totalCells){
			Cell cell = row.createCell(i);
			cell.setCellValue(total);
			i++ ;
		}
	}
	
}
