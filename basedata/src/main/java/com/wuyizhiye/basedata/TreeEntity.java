package com.wuyizhiye.basedata;

/**
 * @ClassName TreeEntity
 * @Description 树形数据基类
 * @author li.biao
 * @date 2015-4-2
 * @param <T>
 */
public abstract class TreeEntity<T extends TreeEntity<?>> extends DataEntity {
	private static final long serialVersionUID = 5103305903950701749L;
	
	protected T parent;
	
	/**
	 * 长编码
	 */
	private String longNumber;
	
	/**
	 * 等级
	 */
	private Integer level;
	
	/**
	 * 叶子节点
	 */
	private boolean leaf;
	
	/**
	 * @param longNumber the longNumber to set
	 */
	public void setLongNumber(String longNumber) {
		this.longNumber = longNumber;
	}

	/**
	 * @return the longNumber
	 */
	public String getLongNumber() {
		return longNumber;
	}

	/**
	 * @param leaf the leaf to set
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	/**
	 * @return the leaf
	 */
	public boolean isLeaf() {
		return leaf;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getLevel() {
		return level;
	}
}
