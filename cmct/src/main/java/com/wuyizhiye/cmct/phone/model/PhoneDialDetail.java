package com.wuyizhiye.cmct.phone.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName PhoneDialDetail
 * @Description 话单明细实体，（存放接口同步数据）
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneDialDetail extends CoreEntity{
	
	public static final String MAPPER = "com.wuyizhiye.cmct.phone.dao.PhoneDialDetailDao";
	private static final long serialVersionUID = 1L;
	
	//接口返回：通话id
	private String callId ;
	
	//所属月份
	private String period ;
	
	/**
	 * 计费id
	 */
	private String costId;
	
	/**
	 * 固话号码
	 */
	private String infoNumber;
	/**
	 * 业务类型
	 */
	private String busType;
	
	/**
	 * 对方号码
	 */
	private String outherNumber;
	
	/**
	 * 起始时间
	 */
	private Date startTime;
	
	/**
	 * 结束时间
	 */
	private Date endTime;
	
	/**
	 * 通话时长
	 */
	private String callDuration;
	
	/**
	 * 费率
	 */
	private Double rate;
	
	/**
	 * 话费
	 */
	private Double callCost;
	
	/**
	 * 套餐类型
	 * @return
	 */
	private String comboType;
	
	//同步历史ID
	private String syncId ;
	
	
	/* 以下字段在数据表中不存在，作显示用*/
	
	//合计
	private Double totalCost ;
	
	//使用模式
	private String useType ;
	
	//使用组织
	private String orgName ;
	
	//电话通数，报表统计
	private Integer numberCount ;
	
	//本地固话费用
	private Double phoneCost ;
	
	//本地手机费用
	private Double moblieCost ;
	
	//长途费用
	private Double longLineCost ;
	
	//低消扣费
	private Double offsetSum;
	
	//套餐扣费
	private Double monthCostSum;
	
	//本地固话的通话数量
	private Integer fixNumberCount;
	
	//本地手机的通话数量
	private Integer phoneNumberCount;
	
	//长途的通话数量
	private Integer longPhoneCount;
	
	//总的通话时长
	private Integer totalDuration;
	
	//平均通话时长
	private Double avgDuration;
	
	//充值总额
	private Double totalPayCost;
	
	//月末余额
	private Double monthSurplusCost;
	
	public Double getAvgDuration() {
		return avgDuration;
	}
	public void setAvgDuration(Double avgDuration) {
		this.avgDuration = avgDuration;
	}
	//0-20秒通数
	private Integer zeroTotwenty;
	
	//20秒-1分钟通数
	private Integer twentyToOneMinute;
	
	//1分钟-3分钟通数
	private Integer oneMinuteToThreeMinute;
	
	//3分钟以上通数
	private Integer threeMinuteAbove;
	
	
	public Double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
	public Double getPhoneCost() {
		return phoneCost;
	}
	public void setPhoneCost(Double phoneCost) {
		this.phoneCost = phoneCost;
	}
	public Double getMoblieCost() {
		return moblieCost;
	}
	public void setMoblieCost(Double moblieCost) {
		this.moblieCost = moblieCost;
	}
	public Double getLongLineCost() {
		return longLineCost;
	}
	public void setLongLineCost(Double longLineCost) {
		this.longLineCost = longLineCost;
	}
	public Integer getNumberCount() {
		return numberCount;
	}
	public void setNumberCount(Integer numberCount) {
		this.numberCount = numberCount;
	}
	public String getSyncId() {
		return syncId;
	}
	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getUseType() {
		return useType;
	}
	public void setUseType(String useType) {
		this.useType = useType;
	}
	public String getCallId() {
		return callId;
	}
	public void setCallId(String callId) {
		this.callId = callId;
	}
	public String getCostId() {
		return costId;
	}
	public void setCostId(String costId) {
		this.costId = costId;
	}
	
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getOutherNumber() {
		return outherNumber;
	}
	public void setOutherNumber(String outherNumber) {
		this.outherNumber = outherNumber;
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
	public String getCallDuration() {
		return callDuration;
	}
	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Double getCallCost() {
		return callCost;
	}
	public void setCallCost(Double callCost) {
		this.callCost = callCost;
	}
	public String getInfoNumber() {
		return infoNumber;
	}
	public void setInfoNumber(String infoNumber) {
		this.infoNumber = infoNumber;
	}
	public String getComboType() {
		return comboType;
	}
	public void setComboType(String comboType) {
		this.comboType = comboType;
	}
	public Integer getFixNumberCount() {
		return fixNumberCount;
	}
	public void setFixNumberCount(Integer fixNumberCount) {
		this.fixNumberCount = fixNumberCount;
	}
	public Integer getPhoneNumberCount() {
		return phoneNumberCount;
	}
	public void setPhoneNumberCount(Integer phoneNumberCount) {
		this.phoneNumberCount = phoneNumberCount;
	}
	public Integer getLongPhoneCount() {
		return longPhoneCount;
	}
	public void setLongPhoneCount(Integer longPhoneCount) {
		this.longPhoneCount = longPhoneCount;
	}
	public Integer getTotalDuration() {
		return totalDuration;
	}
	public void setTotalDuration(Integer totalDuration) {
		this.totalDuration = totalDuration;
	}
	public Integer getZeroTotwenty() {
		return zeroTotwenty;
	}
	public void setZeroTotwenty(Integer zeroTotwenty) {
		this.zeroTotwenty = zeroTotwenty;
	}
	public Integer getTwentyToOneMinute() {
		return twentyToOneMinute;
	}
	public void setTwentyToOneMinute(Integer twentyToOneMinute) {
		this.twentyToOneMinute = twentyToOneMinute;
	}
	public Integer getOneMinuteToThreeMinute() {
		return oneMinuteToThreeMinute;
	}
	public void setOneMinuteToThreeMinute(Integer oneMinuteToThreeMinute) {
		this.oneMinuteToThreeMinute = oneMinuteToThreeMinute;
	}
	public Integer getThreeMinuteAbove() {
		return threeMinuteAbove;
	}
	public void setThreeMinuteAbove(Integer threeMinuteAbove) {
		this.threeMinuteAbove = threeMinuteAbove;
	}
	public Double getTotalPayCost() {
		return totalPayCost;
	}
	public void setTotalPayCost(Double totalPayCost) {
		this.totalPayCost = totalPayCost;
	}
	public Double getMonthSurplusCost() {
		return monthSurplusCost;
	}
	public void setMonthSurplusCost(Double monthSurplusCost) {
		this.monthSurplusCost = monthSurplusCost;
	}
	public Double getOffsetSum() {
		return offsetSum;
	}
	public void setOffsetSum(Double offsetSum) {
		this.offsetSum = offsetSum;
	}
	public Double getMonthCostSum() {
		return monthCostSum;
	}
	public void setMonthCostSum(Double monthCostSum) {
		this.monthCostSum = monthCostSum;
	}

}
