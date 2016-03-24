package com.wuyizhiye.basedata.org.model;

import com.wuyizhiye.basedata.TreeEntity;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;

/**
 * @ClassName PostCategory
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class PostCategory extends TreeEntity<PostCategory> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String mapper = "com.wuyizhiye.basedata.org.dao.PostCategoryDao";

	// 编码
	private String number;

	// 名字
	private String name;

	// 备注
	private String remark;

	// 是否系统初始化
	private CommonFlagEnum isSystem;

	public String getNumber() {
		return number;
	}

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

}
