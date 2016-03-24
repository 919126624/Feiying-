package com.wuyizhiye.cmct.wechat.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.cmct.wechat.enums.WechatAmountEnum;

/**
 * @ClassName WechatAmount
 * @Description 阅读量,注册量
 * @author li.biao
 * @date 2015-5-26
 */
public class WechatAmount extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * READ:阅读量,REGIST:注册
	 */
	private WechatAmountEnum status;
	
	/**
	 * 推广id
	 */
	private WechatPromote wechatPromote;
	
	/**
	 * 所属人员id
	 */
	private Person person;

	/**
	 *创建时间 
	 */
	private Date createTime;
	
	private String ipaddr;
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public WechatAmountEnum getStatus() {
		return status;
	}

	public void setStatus(WechatAmountEnum status) {
		this.status = status;
	}

	public WechatPromote getWechatPromote() {
		return wechatPromote;
	}

	public void setWechatPromote(WechatPromote wechatPromote) {
		this.wechatPromote = wechatPromote;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}
}
