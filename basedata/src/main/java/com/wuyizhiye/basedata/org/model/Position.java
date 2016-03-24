package com.wuyizhiye.basedata.org.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName Position
 * @Description 职位
 * @author li.biao
 * @date 2015-4-2
 */
public class Position extends DataEntity {
	private static final long serialVersionUID = 7282949147076329320L;
	
	/**
	 * 所属组织
	 */
	private Org belongOrg;
	
	/**
	 * 岗位
	 */
	private Job job;
	
	/**
	 * 负责人职位
	 */
	private Boolean leading;
	
	/**
	 * 汇报职位
	 */
	private Position report;

	public void setLeading(Boolean leading) {
		this.leading = leading;
	}

	public Boolean getLeading() {
		return leading;
	}

	public void setBelongOrg(Org belongOrg) {
		this.belongOrg = belongOrg;
	}

	public Org getBelongOrg() {
		return belongOrg;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Job getJob() {
		return job;
	}

	public Position getReport() {
		return report;
	}

	public void setReport(Position report) {
		this.report = report;
	}
}
