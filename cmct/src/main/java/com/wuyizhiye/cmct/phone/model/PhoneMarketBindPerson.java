package com.wuyizhiye.cmct.phone.model;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName PhoneMarketBindPerson
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneMarketBindPerson extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhoneMarketBindPersonDao";
	
	/**
	 * 营销账号
	 */
	private String marketNumber;
	
	/**
	 * 营销密码
	 */
	private String marketPassWord;
	
	/**
	 * 当前绑定人
	 */
	private Person bindPerson;

	/**
	 * 鼎尖云当前memberId
	 */
	private String djMemberId;
	
	//by lxl 14.9.9
	/**
	 * appid
	 */
	private String appid;
	
	/**
	 * appKey
	 */
	private String appKey;
	
	/**
	 * spid
	 */
	private String spid;
	
	/**
	 * passWd
	 */
	private String passWd;
	
	/**
	 * httpurl
	 * @return
	 */
	private String httpUrl;
	
	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getSpid() {
		return spid;
	}

	public void setSpid(String spid) {
		this.spid = spid;
	}

	public String getPassWd() {
		return passWd;
	}

	public void setPassWd(String passWd) {
		this.passWd = passWd;
	}

	public String getDjMemberId() {
		return djMemberId;
	}

	public void setDjMemberId(String djMemberId) {
		this.djMemberId = djMemberId;
	}

	public String getMarketNumber() {
		return marketNumber;
	}

	public void setMarketNumber(String marketNumber) {
		this.marketNumber = marketNumber;
	}

	public String getMarketPassWord() {
		return marketPassWord;
	}

	public void setMarketPassWord(String marketPassWord) {
		this.marketPassWord = marketPassWord;
	}

	public Person getBindPerson() {
		return bindPerson;
	}

	public void setBindPerson(Person bindPerson) {
		this.bindPerson = bindPerson;
	}
}
