package com.wuyizhiye.cmct.phone.model;

import java.util.Date;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName ApiCallBack
 * @Description 呼叫中心-呼叫回调实体
 * @author li.biao
 * @date 2015-5-26
 */
public class ApiCallBack  extends DataEntity{

	private static final long serialVersionUID = 2500150753233358734L;
	
	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.ApiCallBackDao";
	
	//回调报告类型
	private String vType ;
	
	//能力类型
	private String vServiceType ;
	
	//通话标志 
	private String vSessionsId ;
	
	//主叫号码
	private String vCallerNbr ;
	
	//原被叫
	private String vCalleeNbr ;
	
	//IMS状态
	private String vCallState ;
	
	//触发类型
	private String vIsinconmingcall ;
	
	//IMS状态产生时间
	private String vStateTime ;
	
	//绑定人
	private Person person ;
	
	//最后更新时间
	private Date   lastTime ;

	//当前振铃状态的次数
	private Integer statusRingCount;
	
	//当前接听状态的次数
	private Integer statusAnswCount;

	public Integer getStatusRingCount() {
		return statusRingCount;
	}

	public void setStatusRingCount(Integer statusRingCount) {
		this.statusRingCount = statusRingCount;
	}

	public Integer getStatusAnswCount() {
		return statusAnswCount;
	}

	public void setStatusAnswCount(Integer statusAnswCount) {
		this.statusAnswCount = statusAnswCount;
	}

	public String getvType() {
		return vType;
	}

	public void setvType(String vType) {
		this.vType = vType;
	}

	public String getvServiceType() {
		return vServiceType;
	}

	public void setvServiceType(String vServiceType) {
		this.vServiceType = vServiceType;
	}

	public String getvSessionsId() {
		return vSessionsId;
	}

	public void setvSessionsId(String vSessionsId) {
		this.vSessionsId = vSessionsId;
	}

	public String getvCallerNbr() {
		return vCallerNbr;
	}

	public void setvCallerNbr(String vCallerNbr) {
		this.vCallerNbr = vCallerNbr;
	}

	public String getvCalleeNbr() {
		return vCalleeNbr;
	}

	public void setvCalleeNbr(String vCalleeNbr) {
		this.vCalleeNbr = vCalleeNbr;
	}

	public String getvCallState() {
		return vCallState;
	}

	public void setvCallState(String vCallState) {
		this.vCallState = vCallState;
	}

	public String getvIsinconmingcall() {
		return vIsinconmingcall;
	}

	public void setvIsinconmingcall(String vIsinconmingcall) {
		this.vIsinconmingcall = vIsinconmingcall;
	}

	public String getvStateTime() {
		return vStateTime;
	}

	public void setvStateTime(String vStateTime) {
		this.vStateTime = vStateTime;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	
	
	
	
}
