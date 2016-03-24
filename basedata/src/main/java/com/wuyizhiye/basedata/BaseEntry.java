package com.wuyizhiye.basedata;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName BaseEntry
 * @Description 单据分录基类
 * @author li.biao
 * @date 2015-4-2
 * @param <T>
 */
public abstract class BaseEntry<T extends BillEntity> extends CoreEntity {
	private static final long serialVersionUID = 6381126080872890537L;
	
	/**
	 * 对应的单据
	 */
	private T bill;
	
	/**
	 * 行编号
	 */
	private Integer index;

	/**
	 * @param bill the bill to set
	 */
	public void setBill(T bill) {
		this.bill = bill;
	}

	/**
	 * @return the bill
	 */
	public T getBill() {
		return bill;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
}
