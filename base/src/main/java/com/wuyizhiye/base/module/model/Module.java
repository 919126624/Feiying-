package com.wuyizhiye.base.module.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.module.enums.ModuleStatusEnum;

/**
 * @ClassName Module
 * @Description 系统模块
 * @author li.biao
 * @date 2015-4-1
 */
public class Module extends CoreEntity {
	private static final long serialVersionUID = 3372994203604121015L;
	
	/**
	 * 是否启用
	 */
	private boolean enable;
	
	/**
	 * 模块类型
	 */
	private ModuleEnum type;
	
	/**
	 * 许可开始时间
	 */
	private Date start;
	
	/**
	 * 许可结束时间
	 */
	private Date end;
	
	/**
	 * 代号
	 */
	private String number ;
	
	/**
	 * 备注说明
	 */
	private String remark ;
	
	/**
	 * 许可状态,根据许可时间自动判断，不固化
	 */
	private ModuleStatusEnum status;

	/**
	 * 可允许最大在线人数
	 */
	private Integer count ;
	
	public String getTypeName() {
		return type.getName();
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public ModuleEnum getType() {
		return type;
	}

	public void setType(ModuleEnum type) {
		this.type = type;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public ModuleStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ModuleStatusEnum status) {
		this.status = status;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
