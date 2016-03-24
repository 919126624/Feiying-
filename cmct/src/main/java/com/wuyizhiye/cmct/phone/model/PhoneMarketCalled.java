package com.wuyizhiye.cmct.phone.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName PhoneMarketCalled
 * @Description 营销被叫实体
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneMarketCalled extends CoreEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 任务号ID
	 */
	private String modeID;
	
	/**
	 * 关联营销实体id
	 */
	private PhoneMarket phoneMarket;
	
	/**
	 * 被叫号码
	 */
	private String calleeNbr;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModeID() {
		return modeID;
	}

	public void setModeID(String modeID) {
		this.modeID = modeID;
	}

	public PhoneMarket getPhoneMarket() {
		return phoneMarket;
	}

	public void setPhoneMarket(PhoneMarket phoneMarket) {
		this.phoneMarket = phoneMarket;
	}

	public String getCalleeNbr() {
		return calleeNbr;
	}

	public void setCalleeNbr(String calleeNbr) {
		this.calleeNbr = calleeNbr;
	}
}
