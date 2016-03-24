package com.wuyizhiye.cmct.phone.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName PhoneCode
 * @Description 号码归属地查询
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneCode extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhoneCodeDao";
	
	/**
	 * 排序号,固定唯一的
	 */
	private Integer number;
	
	/**
	 * H码
	 */
	private String hCode;
	
	/**
	 * 归属省份
	 */
	private String province;
	
	/**
	 * 归属城市
	 */
	private String city;
	
	/**
	 *运营商 
	 */
	private String corp;
	
	/**
	 * 不包含数据库字段
	 * @return
	 */
	private Integer page;
	
	private Integer pageSize;
	
	/**
	 * 标志是同步还是同步更新
	 */
	private String syncFlag;
	
	public String getSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String gethCode() {
		return hCode;
	}

	public void sethCode(String hCode) {
		this.hCode = hCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCorp() {
		return corp;
	}

	public void setCorp(String corp) {
		this.corp = corp;
	}
}
