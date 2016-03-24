package com.wuyizhiye.cmct.phone.model;

import java.util.Date;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName PhoneDialLog
 * @Description 呼叫中心日志记录
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneDialLog extends DataEntity{
 
	private static final long serialVersionUID = 1786332591894467902L;
	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhoneDialLogDao" ;

	//操作人
	private Person person ;
	
	//操作时间
	private Date callTime ;
	
	//被叫名字
	private String toName ;
	
	//被叫手机号码
	private String toPhone ;
	
	//返回代号
	private String code ;
	
	//返回描述
	private String rtnDesc ;
	
	//显示号码
	private String showPhone ;
	
	public String getShowPhone() {
		return showPhone;
	}
	public void setShowPhone(String showPhone) {
		this.showPhone = showPhone;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Date getCallTime() {
		return callTime;
	}
	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}
	public String getToPhone() {
		return toPhone;
	}
	public void setToPhone(String toPhone) {
		this.toPhone = toPhone;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRtnDesc() {
		return rtnDesc;
	}
	public void setRtnDesc(String rtnDesc) {
		this.rtnDesc = rtnDesc;
	}
}
