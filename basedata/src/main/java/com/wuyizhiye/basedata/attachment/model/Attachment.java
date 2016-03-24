package com.wuyizhiye.basedata.attachment.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName Attachment
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class Attachment extends DataEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1235264161616660797L;
	
	/**
	 * 附件所属数据id
	 */
	private String belong;
	/**
	 * 附件类型
	 */
	private String fileType; 
	/**
	 * 附件大小
	 */
	private String fileLength;
	/**
	 * 存放位置
	 */
	private String path;
	
	public String getBelong() {
		return belong;
	}
	public void setBelong(String belong) {
		this.belong = belong;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileLength() {
		return fileLength;
	}
	public void setFileLength(String fileLength) {
		this.fileLength = fileLength;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
