package com.wuyizhiye.basedata.bank.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName BaseBank
 * @Description 基础银行
 * @author li.biao
 * @date 2015-4-2
 */
public class BaseBank extends DataEntity{
	
	private static final long serialVersionUID = -5002130462652222382L;
	public static final String MAPPER="com.wuyizhiye.basedata.bank.dao.BaseBankDao";
	
	//银行名称
	private String bankName ;
	
	//银行描述
	private String bankDesc ;
	
	
	//不对应数据库  wsw
	private String photoPath ;
	
	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	public BaseBank(){
		
	}
	public BaseBank(String id){
		super.setId(id);
	}
	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankDesc() {
		return bankDesc;
	}

	public void setBankDesc(String bankDesc) {
		this.bankDesc = bankDesc;
	}
	
}
