package com.wuyizhiye.cmct.sms.model;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName CmctContact
 * @Description  通讯录 临时实体
 * @author li.biao
 * @date 2015-5-26
 */
public class CmctContact extends CoreEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5420848336998119663L;
	private String name;
	private String mobile;
	private String tel;
	private String shortnum;
	private String qq;
	private String email;
	private String remark;
	private Person person;
	private String simplePinyin;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getShortnum() {
		return shortnum;
	}
	public void setShortnum(String shortnum) {
		this.shortnum = shortnum;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public String getSimplePinyin() {
		return simplePinyin;
	}
	public void setSimplePinyin(String simplePinyin) {
		this.simplePinyin = simplePinyin;
	}
	
}
