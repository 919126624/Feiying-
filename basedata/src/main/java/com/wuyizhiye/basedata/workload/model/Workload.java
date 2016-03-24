package com.wuyizhiye.basedata.workload.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName Workload
 * @Description 工作量bean
 * @author li.biao
 * @date 2015-4-3
 */
public class Workload extends DataEntity{

	private static final long serialVersionUID = 1L;

	private String url;//连接地址
	private String ioc;//图标地址
	private String remark;//方法连接 命名不标准
	private String isAccredit;//冗余字段  如果关联当前岗位为：true  否则为空
	private WorkloadTypeEnum type;//提醒类型  用于前台颜色区分或后期查询条件的添加
	private Integer seq;
	
	public WorkloadTypeEnum getType() {
		return type;
	}
	public void setType(WorkloadTypeEnum type) {
		this.type = type;
	}
	public String getIsAccredit() {
		return isAccredit;
	}
	public void setIsAccredit(String isAccredit) {
		this.isAccredit = isAccredit;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRemark() {
		return remark;
	}
	public String getIoc() {
		return ioc;
	}
	public void setIoc(String ioc) {
		this.ioc = ioc;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTypeLabel(){
		return this.getType() == null ? "" : this.getType().getLabel();
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}
