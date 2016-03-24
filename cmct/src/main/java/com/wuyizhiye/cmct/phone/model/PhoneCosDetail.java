package com.wuyizhiye.cmct.phone.model;

import java.math.BigDecimal;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName PhoneCosDetail
 * @Description 话费明细表。后期从接口读取数据
 * @author li.biao
 * @date 2015-5-26
 */
public class PhoneCosDetail extends CoreEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 固话号码
	 */
	private String fixNumber;
	
	/**
	 * 账单周期
	 */
	private String period;
	
	/**
	 * 总消费合计
	 */
	private BigDecimal totalCost;
	
	/**
	 * 本地手机话费
	 */
	private BigDecimal localPhoneCost;
	
	/**
	 * 本地固话话费
	 */
	private BigDecimal localTelCost;
	
	/**
	 * 长途话费
	 */
	private BigDecimal longCost;
	
	/**
	 * 国际话费
	 */
	private BigDecimal sosCost;
	
	/**
	 * 短信费用
	 */
	private BigDecimal smsCost;
	
	/**
	 * 隐藏呼出费用
	 */
	private BigDecimal hideOutCost;
	
	/**
	 * 费用合计
	 */
	private BigDecimal costSum;
	
	/**
	 * 月租合计
	 */
	private BigDecimal monthCostSum;
	
	/**
	 * 低消合计
	 */
	private BigDecimal offsetSum;

	public String getFixNumber() {
		return fixNumber;
	}

	public void setFixNumber(String fixNumber) {
		this.fixNumber = fixNumber;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public BigDecimal getLocalPhoneCost() {
		return localPhoneCost;
	}

	public void setLocalPhoneCost(BigDecimal localPhoneCost) {
		this.localPhoneCost = localPhoneCost;
	}

	public BigDecimal getLocalTelCost() {
		return localTelCost;
	}

	public void setLocalTelCost(BigDecimal localTelCost) {
		this.localTelCost = localTelCost;
	}

	public BigDecimal getLongCost() {
		return longCost;
	}

	public void setLongCost(BigDecimal longCost) {
		this.longCost = longCost;
	}

	public BigDecimal getSosCost() {
		return sosCost;
	}

	public void setSosCost(BigDecimal sosCost) {
		this.sosCost = sosCost;
	}

	public BigDecimal getSmsCost() {
		return smsCost;
	}

	public void setSmsCost(BigDecimal smsCost) {
		this.smsCost = smsCost;
	}

	public BigDecimal getHideOutCost() {
		return hideOutCost;
	}

	public void setHideOutCost(BigDecimal hideOutCost) {
		this.hideOutCost = hideOutCost;
	}

	public BigDecimal getCostSum() {
		return costSum;
	}

	public void setCostSum(BigDecimal costSum) {
		this.costSum = costSum;
	}

	public BigDecimal getMonthCostSum() {
		return monthCostSum;
	}

	public void setMonthCostSum(BigDecimal monthCostSum) {
		this.monthCostSum = monthCostSum;
	}

	public BigDecimal getOffsetSum() {
		return offsetSum;
	}

	public void setOffsetSum(BigDecimal offsetSum) {
		this.offsetSum = offsetSum;
	}
	
}
