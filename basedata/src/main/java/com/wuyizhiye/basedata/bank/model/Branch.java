package com.wuyizhiye.basedata.bank.model;

import com.wuyizhiye.basedata.TreeEntity;
import com.wuyizhiye.basedata.bank.model.BaseBank;

/**
 * @ClassName Branch
 * @Description 银行网点
 * @author li.biao
 * @date 2015-4-2
 */
public class Branch extends TreeEntity<Branch> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BaseBank baseBank ;//基础银行
	
	private String number ; //编号

	private String address;  //地址
	private String tel; //电话 
	private String fax; //传真
	private Branch parent; //上级机构
	private String contacter ; //联系人
	private String overSeas; //是否境内
	private String remark; //备注
	private Integer dr; //是否有效
	private String countyId;//城市id
	private String provincesId;//省份id
	public String getCountyId() {
		return countyId;
	}
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}
	public String getProvincesId() {
		return provincesId;
	}
	public void setProvincesId(String provincesId) {
		this.provincesId = provincesId;
	}
	private City provinces;// 省份
	
	private City county;//城市
	
	
	public BaseBank getBaseBank() {
		return baseBank;
	}
	public void setBaseBank(BaseBank baseBank) {
		this.baseBank = baseBank;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public Branch getParent() {
		return parent;
	}
	public void setParent(Branch parent) {
		this.parent = parent;
	}
	public String getContacter() {
		return contacter;
	}
	public void setContacter(String contacter) {
		this.contacter = contacter;
	}
	public String getOverSeas() {
		return overSeas;
	}
	public void setOverSeas(String overSeas) {
		this.overSeas = overSeas;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public City getProvinces() {
		return provinces;
	}
	public void setProvinces(City provinces) {
		this.provinces = provinces;
	}
	public City getCounty() {
		return county;
	}
	public void setCounty(City county) {
		this.county = county;
	}
}
