package com.wuyizhiye.base.export.txt;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.export.DataExportor;
import com.wuyizhiye.base.export.DataField;
import com.wuyizhiye.base.export.ExportDataSource;
import com.wuyizhiye.base.util.StringUtils;

/**
 * @ClassName TxtDataExportor
 * @Description 描述: Txt格式数据导出工具
 * @author li.biao
 * @date 2015-4-1
 * @param <T>
 */
public class TxtDataExportor<T> extends DataExportor<T> {
	
	/**
	 * 间隔符
	 */
	private String split = ",";
	
	private StringBuilder stringBuilder = new StringBuilder();
	
	public TxtDataExportor(DataField[] fields,ExportDataSource<T> dataSource,OutputStream os){
		super(fields, dataSource, os);
	}
	
	public TxtDataExportor(DataField[] fields,ExportDataSource<T> dataSource,OutputStream os,String split){
		this(fields, dataSource, os);
		if(!StringUtils.isEmpty(split)){
			this.split = split;
		}
	}
	
	@Override
	public void export() throws IOException {
		Pagination<T> pagination = new Pagination<T>();
		pagination.setPageSize(pageSize);
		pagination.setCurrentPage(0);
		List<T> dataList;
		outputBOM();
		outputHead();
		do{
			pagination.setCurrentPage(pagination.getCurrentPage()+1);
			pagination = dataSource.getData(pagination);
			dataList = pagination.getItems();
			output(dataList);
		}while(pagination.getPageSize() * pagination.getCurrentPage() <= pagination.getRecordCount());
		if(count > 0){
			writeString(stringBuilder.toString());
			stringBuilder.delete(0, stringBuilder.length());
			count = 0;
		}
		os.flush();
	}
	
	private void outputHead() throws IOException{
		StringBuilder b = new StringBuilder();
		if(isUseIndex()){
			b.append("序号").append(split);
		}
		for(DataField field : fields){
			b.append("\t").append(field.getName()).append(split);
		}
		b.deleteCharAt(b.lastIndexOf(split));
		b.append("\n");
		writeString(b.toString());
	}

	private int count = 0;
	protected void output(List<T> dataList) throws IOException {
		for(T data : dataList){
			stringBuilder.append(formatToLine(data));
			stringBuilder.append("\n");
			count++;
			if(count >= 100){
				writeString(stringBuilder.toString());
				stringBuilder.delete(0, stringBuilder.length());
				count = 0;
			}
		}
	}
	
	private String formatToLine(T data){
		StringBuilder tmp = new StringBuilder();
		if(isUseIndex()){
			tmp.append(++currentIndex).append(split);;
		}
		for(DataField field : fields){
			Object value = getValue(data, field.getField());
			if(!StringUtils.isNotNull(value)){
				value = "";
			}
			tmp.append("\t").append(value).append(split);
		}
		tmp.deleteCharAt(tmp.lastIndexOf(split));
		return tmp.toString();
	}
	
	/**
	 * 输出UTF-8的BOM头,UTF-8无BOM打开有可能乱码
	 * @throws IOException
	 */
	private void outputBOM() throws IOException{
		String[] bom = new String[]{"EF","BB","BF"};
		byte[] bytes = new byte[bom.length];
		for(int i = 0; i < bom.length; i++){
			bytes[i] = (byte) Integer.parseInt(bom[i], 16);
		}
		os.write(bytes);
	}
	
	private void writeString(String v) throws IOException{
		os.write(v.getBytes("UTF-8"));
	}
}
