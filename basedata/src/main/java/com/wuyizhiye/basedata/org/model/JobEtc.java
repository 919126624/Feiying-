package com.wuyizhiye.basedata.org.model;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;

/**
 * @ClassName JobEtc
 * @Description 职等
 * @author li.biao
 * @date 2015-4-2
 */
public class JobEtc extends DataEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String MAPPER = "com.wuyizhiye.basedata.org.dao.JobEtcDao";
	
	/**
	 * 状态
	 */
	private UserStatusEnum status;

	public UserStatusEnum getStatus() {
		return status;
	}

	public void setStatus(UserStatusEnum status) {
		this.status = status;
	}
}
