package com.wuyizhiye.basedata.person.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName SecondPassword
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class SecondPassword extends CoreEntity {
	private String module;//功能标志
	private Person person;
	private String password;
	private boolean samelogin; //是否和登陆密码一致
	private String status;// ENABLE DISABLE
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean getSamelogin() {
		return samelogin;
	}
	public void setSamelogin(boolean samelogin) {
		this.samelogin = samelogin;
	}
}
