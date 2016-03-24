package com.wuyizhiye.cmct.sms.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName MessageGive
 * @Description 短信赠送
 * @author li.biao
 * @date 2015-5-26
 */
public class MessageGive extends CoreEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8322513851451499190L;
	
	private Person geter;//接收人
	
	private Person giver;//赠送人
	
	private Date giveDate;//赠送日期
	
	private int giveNum;//赠送条数
	
	private String remark;//备注

	public Person getGeter() {
		return geter;
	}

	public void setGeter(Person geter) {
		this.geter = geter;
	}

	public Person getGiver() {
		return giver;
	}

	public void setGiver(Person giver) {
		this.giver = giver;
	}

	public Date getGiveDate() {
		return giveDate;
	}

	public void setGiveDate(Date giveDate) {
		this.giveDate = giveDate;
	}

	public int getGiveNum() {
		return giveNum;
	}

	public void setGiveNum(int giveNum) {
		this.giveNum = giveNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
