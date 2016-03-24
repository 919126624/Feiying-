package com.wuyizhiye.cmct.phone.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName PhoneMainShow
 * @Description 主显号码
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneMainShow extends DataEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String MAPPER="com.wuyizhiye.cmct.phone.dao.PhoneMainShowDao";
	
	/**
	 * 主显号码
	 */
	private String displayNbr;
	
	/**
	 * 计费号码
	 */
	private String chargeNbr;
	
	/**
	 * 状态
	 */
	private String status;

	private String code;//验证码,传值
	
	private String matchId;//云端匹配id
	
	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public String getName() {
		return displayNbr;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDisplayNbr() {
		return displayNbr;
	}

	public void setDisplayNbr(String displayNbr) {
		this.displayNbr = displayNbr;
	}

	public String getChargeNbr() {
		return chargeNbr;
	}

	public void setChargeNbr(String chargeNbr) {
		this.chargeNbr = chargeNbr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
