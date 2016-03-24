package com.wuyizhiye.cmct.phone.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName PhoneCallLevel
 * @Description call客级别,统计表,定时查找通话记录表,把数据跟新到此表
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneCallLevel extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String MAPPER="com.wuyizhiye.cmct.phone.dao.PhoneCallLevelDao";
	/**
	 * 关联人员id
	 */
	private Person person;
	
	/**
	 * 组织id
	 */
	private Org org;
	
	/**
	 * 累计成功电话量
	 */
	private Integer callSuccCumulative;
	
	/**
	 * 累计拨打电话量
	 */
	private Integer callTotalCumulative;
	
	/**
	 * 累计通话时长
	 */
	private Integer durationCumulative;
	
	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;

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

	public Integer getCallSuccCumulative() {
		return callSuccCumulative;
	}

	public void setCallSuccCumulative(Integer callSuccCumulative) {
		this.callSuccCumulative = callSuccCumulative;
	}

	public Integer getCallTotalCumulative() {
		return callTotalCumulative;
	}

	public void setCallTotalCumulative(Integer callTotalCumulative) {
		this.callTotalCumulative = callTotalCumulative;
	}

	public Integer getDurationCumulative() {
		return durationCumulative;
	}

	public void setDurationCumulative(Integer durationCumulative) {
		this.durationCumulative = durationCumulative;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
}
