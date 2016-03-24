package com.wuyizhiye.cmct.phone.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.cmct.phone.enums.PhonePayStateEnum;

/**
 * @ClassName PhoneCostPay
 * @Description 话费充值实体
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneCostPay extends CoreEntity {
	
	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhoneCostPayDao";
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 充值日期
	 */
	private Date paytime;
	
	/**
	 * 充值人
	 */
	private Person person;
	
	/**
	 * 充值的组织
	 */
	private Org org;
	
	/**
	 * 充值金额
	 */
	private Double paycost;
	
	/**
	 * 充值后余额
	 */
	private Double paySurplusCost;
	
	/**
	 * 充值方式
	 */
	private String payway;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 充值状态，
	 * @return
	 */
	private PhonePayStateEnum flag;
	
	/**
	 * 关联的核算渠道orgId
	 * by lxl 14.5.14
	 */
	private String orgId;

	public Date getPaytime() {
		return paytime;
	}

	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public Double getPaycost() {
		return paycost;
	}

	public void setPaycost(Double paycost) {
		this.paycost = paycost;
	}

	public Double getPaySurplusCost() {
		return paySurplusCost;
	}

	public void setPaySurplusCost(Double paySurplusCost) {
		this.paySurplusCost = paySurplusCost;
	}

	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public PhonePayStateEnum getFlag() {
		return flag;
	}

	public void setFlag(PhonePayStateEnum flag) {
		this.flag = flag;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}
