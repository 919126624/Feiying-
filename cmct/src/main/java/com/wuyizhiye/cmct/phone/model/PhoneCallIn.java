package com.wuyizhiye.cmct.phone.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName PhoneCallIn
 * @Description 闭环方案-- 客户呼进明细,自动从平台同步数据
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneCallIn extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhoneCallInDao";
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 主叫号码
	 */
	private String callerNbr;
	
	/**
	 * 被叫号码
	 */
	private String calledNbr;
	
	/**
	 * 开始时间
	 */
	private Date startTime;
	
	/**
	 * 结束时间
	 */
	private Date endTime ;
	
	/**
	 * 通话时长
	 */
	private Integer callDuration ;

	/**
	 * 对应的副号实体
	 */
	private PhoneDeputyNum deputyNum;
	
	/**
	 * 对应的使用人员
	 */
	private Person usePerson;

	/**
	 * 状态(0:转接失败,1:转接成功)
	 */
	private String status;

	/**
	 * sessionId
	 */
	private String sessionId;
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public PhoneDeputyNum getDeputyNum() {
		return deputyNum;
	}

	public void setDeputyNum(PhoneDeputyNum deputyNum) {
		this.deputyNum = deputyNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCallerNbr() {
		return callerNbr;
	}

	public void setCallerNbr(String callerNbr) {
		this.callerNbr = callerNbr;
	}

	public String getCalledNbr() {
		return calledNbr;
	}

	public void setCalledNbr(String calledNbr) {
		this.calledNbr = calledNbr;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(Integer callDuration) {
		this.callDuration = callDuration;
	}

	public Person getUsePerson() {
		return usePerson;
	}

	public void setUsePerson(Person usePerson) {
		this.usePerson = usePerson;
	}
}
