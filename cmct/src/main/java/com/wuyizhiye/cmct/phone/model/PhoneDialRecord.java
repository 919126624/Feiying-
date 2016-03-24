package com.wuyizhiye.cmct.phone.model;

import java.util.Date;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.cmct.phone.enums.DialResultEnum;
import com.wuyizhiye.cmct.phone.enums.DialTypeEnum;
import com.wuyizhiye.cmct.phone.enums.PhoneMemberUseEnum;

/**
 * @ClassName PhoneDialRecord
 * @Description 呼叫中心人员
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneDialRecord extends DataEntity{
 
	private static final long serialVersionUID = 1786332591896447902L;
	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao" ;

	//呼叫类型
	private DialTypeEnum callType ;
	
	//呼叫时间
	private Date callTime ;
	
	//呼叫人ID
	private String callId ;
	
	//呼叫人名称
	private String callName ;
	
	//呼出号码（呼叫中心人员的话机号码）
	private String fromPhone ;
	
	//被叫号码
	private String toPhone ;
	
	//被叫名字
	private String toName ;
	
	//通话结果
	private DialResultEnum callResult ;
	
	//通话时长（存秒数）
	private Integer costTime ;
	
	//备注
	private String remark ;
	
	//呼叫话单ID，用于接口,电信的是sessionId
	private String callBillId ;
	
	private Integer atime ;
	
	private Integer btime ;
	
	//当前去电显示号码
	private String currShowPhone ;
	
	//关联对象ID
	private String objectId ;
	
	
	//by lxl 14.4.16,电信时的通话id,后续看电话的录音根据此字段查找
//	private String sessionId;
	
	//---------------------------
	//---------------------------
	//固话使用模式
	private PhoneMemberUseEnum useType;
	
	//当前统计的月份
	private String mounth;
	
	//当前固话所属的月份统计的固话费用合计
	private String tatalCost;
	
	//固话号码
	private String solidNumber;
	
	//当前记录所属的组织
	private Org org ;
	
	//总话费
	private String tat;
	
	//注册号码
	private String loginNumber;
	
	//别名
	private String alais;
	
	//套餐c1类型话费
	private Double c1Cost;
	//c2 类型话费
	private Double c2Cost;
	//c3类型话费
	private Double c3Cost;
	
	private String suffix;//by lxl 14.12.29加入分表后缀
	
	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getCurrShowPhone() {
		return currShowPhone;
	}

	public void setCurrShowPhone(String currShowPhone) {
		this.currShowPhone = currShowPhone;
	}

	public Integer getAtime() {
		return atime;
	}

	public void setAtime(Integer atime) {
		this.atime = atime;
	}

	public Integer getBtime() {
		return btime;
	}

	public void setBtime(Integer btime) {
		this.btime = btime;
	}

	public DialTypeEnum getCallType() {
		return callType;
	}

	public void setCallType(DialTypeEnum callType) {
		this.callType = callType;
	}

	public Date getCallTime() {
		return callTime;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getCallName() {
		return callName;
	}

	public void setCallName(String callName) {
		this.callName = callName;
	}

	public String getFromPhone() {
		return fromPhone;
	}

	public void setFromPhone(String fromPhone) {
		this.fromPhone = fromPhone;
	}

	public String getToPhone() {
		return toPhone;
	}

	public void setToPhone(String toPhone) {
		this.toPhone = toPhone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public DialResultEnum getCallResult() {
		return callResult;
	}

	public void setCallResult(DialResultEnum callResult) {
		this.callResult = callResult;
	}

	public Integer getCostTime() {
		return costTime;
	}

	public void setCostTime(Integer costTime) {
		this.costTime = costTime;
	}

	public String getCallBillId() {
		return callBillId;
	}

	public void setCallBillId(String callBillId) {
		this.callBillId = callBillId;
	}

	public PhoneMemberUseEnum getUseType() {
		return useType;
	}

	public void setUseType(PhoneMemberUseEnum useType) {
		this.useType = useType;
	}

	public String getMounth() {
		return mounth;
	}

	public void setMounth(String mounth) {
		this.mounth = mounth;
	}

	public String getSolidNumber() {
		return solidNumber;
	}

	public void setSolidNumber(String solidNumber) {
		this.solidNumber = solidNumber;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public String getTatalCost() {
		return tatalCost;
	}

	public void setTatalCost(String tatalCost) {
		this.tatalCost = tatalCost;
	}

	public String getTat() {
		return tat;
	}

	public void setTat(String tat) {
		this.tat = tat;
	}

	public String getLoginNumber() {
		return loginNumber;
	}

	public void setLoginNumber(String loginNumber) {
		this.loginNumber = loginNumber;
	}

	public String getAlais() {
		return alais;
	}

	public void setAlais(String alais) {
		this.alais = alais;
	}

	public Double getC1Cost() {
		return c1Cost;
	}

	public Double getC2Cost() {
		return c2Cost;
	}

	public void setC2Cost(Double c2Cost) {
		this.c2Cost = c2Cost;
	}

	public void setC1Cost(Double c1Cost) {
		this.c1Cost = c1Cost;
	}

	public Double getC3Cost() {
		return c3Cost;
	}

	public void setC3Cost(Double c3Cost) {
		this.c3Cost = c3Cost;
	}

}
