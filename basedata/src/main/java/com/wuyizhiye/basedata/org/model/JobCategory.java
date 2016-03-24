package com.wuyizhiye.basedata.org.model;
import com.wuyizhiye.basedata.TreeEntity;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;

/**
 * @ClassName JobCategory
 * @Description 岗位大类
 * @author li.biao
 * @date 2015-4-2
 */
public class JobCategory extends TreeEntity<JobCategory> {
	
	private static final long serialVersionUID = 136925852072366883L;
	public static final String MAPPER = "com.wuyizhiye.basedata.org.dao.JobCategoryDao";

	//编码
	private String number ;
	
	public String getNumber() {
		return number;
	}

	//名字
	private String name ; 
	
	//备注
	private String remark ;
	
	//是否系统初始化
	private CommonFlagEnum isSystem  ;



	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public CommonFlagEnum getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(CommonFlagEnum isSystem) {
		this.isSystem = isSystem;
	}
	

	public JobCategory getParent() {
		return this.parent;
	}
	
	public void setParent(JobCategory parent) {
		this.parent = parent;
	}
}

