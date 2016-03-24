package com.wuyizhiye.cmct.sms.model;

import java.math.BigDecimal;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.person.enums.SexEnum;
import com.wuyizhiye.cmct.sms.enums.CountryEnum;
import com.wuyizhiye.cmct.sms.enums.CustomerRelationEnum;

/**
 * @ClassName ContactPerson
 * @Description 客户联系人 （T_BK_CONTACTPERSON）
 * 只是一个单独实体对象，用于更新客户联系人信息
 * @author li.biao
 * @date 2015-5-26
 */
public class ContactPerson extends DataEntity {
	
	private static final long serialVersionUID = -7048519229004988453L;
	
	private String contactType;//联系人类型
	private CountryEnum country; //地域 
	private CustomerRelationEnum relation; //客户关系
	private String mobile; //手机  
	private String tel; //固定电话
	private String contractInformation;//联系方式(手机号固定电话存放在一起)
	private String idcard; //身份证
	private String email; //邮箱
	private String qq; //qq
	private String address; //地址
	private String msn; //MSN
	private String minBlog;//微博
	private String minMail;//微信
	private String customer;//客户
	private SexEnum sex;//性别
	
	//数据库没有的冗余字段
	private String dealmenberId;
	//数据库没有的冗余字段
	private BigDecimal radio;
	
	
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getContactType() {
		return contactType;
	}
	public void setContactType(String contactType) {
		this.contactType = contactType;
	}
	public CountryEnum getCountry() {
		return country;
	}
	public void setCountry(CountryEnum country) {
		this.country = country;
	}
	public CustomerRelationEnum getRelation() {
		return relation;
	}
	public void setRelation(CustomerRelationEnum relation) {
		this.relation = relation;
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
	public String getContractInformation() {
		return contractInformation;
	}
	public void setContractInformation(String contractInformation) {
		this.contractInformation = contractInformation;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMsn() {
		return msn;
	}
	public void setMsn(String msn) {
		this.msn = msn;
	}
	public String getMinBlog() {
		return minBlog;
	}
	public void setMinBlog(String minBlog) {
		this.minBlog = minBlog;
	}
	public String getMinMail() {
		return minMail;
	}
	public void setMinMail(String minMail) {
		this.minMail = minMail;
	}
	public BigDecimal getRadio() {
		return radio;
	}
	public void setRadio(BigDecimal radio) {
		this.radio = radio;
	}
	public String getDealmenberId() {
		return dealmenberId;
	}
	public void setDealmenberId(String dealmenberId) {
		this.dealmenberId = dealmenberId;
	}
	public SexEnum getSex() {
		return sex;
	}
	
	public String getSexStr() {
		return sex ==  null ? "" : sex.getName();
	}
	
	public void setSex(SexEnum sex) {
		this.sex = sex;
	}
}
