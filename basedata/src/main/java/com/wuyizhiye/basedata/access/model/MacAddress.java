package com.wuyizhiye.basedata.access.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.access.enums.AccessTypeEnum;
import com.wuyizhiye.basedata.access.enums.TerminalTypeEnum;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName MacAddress
 * @Description Mac地址
 * @author li.biao
 * @date 2015-4-2
 */
public class MacAddress extends CoreEntity {
	private static final long serialVersionUID = 8377956877450610484L;
	
	/**
	 * 所属部门
	 */
	private Org org;
	
	/**
	 * 使用人
	 */
	private Person person;
	
	/**
	 *mac字段的内容不一定是 mac地址，可以是其他的识别方式
	 */
	private String mac;
	
	/**
	 * IP
	 */
	private String ip;
	
	/**
	 * 开始时间
	 */
	private Date createTime;
	
	/**
	 * 是否启用
	 */
	private Boolean enable;
	
	/**
	 * 备注
	 */
	private String description;
	/**
	 * 权限类型
	 */
	private AccessTypeEnum accessType;
	/**
	 * 终端类型类型
	 */
	private TerminalTypeEnum terminalType;
	/**
	 * 限本人登录(YES/NO)
	 */
	private CommonFlagEnum isLoginSelf;
	
	/**
	 * 共享部门
	 * @return
	 */
	private Org shareOrg;

	public AccessTypeEnum getAccessType() {
		return accessType;
	}

	public void setAccessType(AccessTypeEnum accessType) {
		this.accessType = accessType;
	}

	public TerminalTypeEnum getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(TerminalTypeEnum terminalType) {
		this.terminalType = terminalType;
	}

	public CommonFlagEnum getIsLoginSelf() {
		return isLoginSelf;
	}

	public void setIsLoginSelf(CommonFlagEnum isLoginSelf) {
		this.isLoginSelf = isLoginSelf;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Org getShareOrg() {
		return shareOrg;
	}

	public void setShareOrg(Org shareOrg) {
		this.shareOrg = shareOrg;
	}
}
